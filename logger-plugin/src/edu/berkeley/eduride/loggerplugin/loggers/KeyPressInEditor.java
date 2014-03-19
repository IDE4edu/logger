package edu.berkeley.eduride.loggerplugin.loggers;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.berkeley.eduride.loggerplugin.LoggerInstaller;



public class KeyPressInEditor extends AbstractLogger implements KeyListener {

	public static QualifiedName isa_key = new QualifiedName("edu.berkeley.eduride","isAssignment");
	ITextEditor editor = null;
	IDocument doc = null;
	ITextSelection sel = null;
	int line = -1;

	public KeyPressInEditor(IEditorPart editor) {
		super("kpie");
		this.installMe(editor);
	}

	
	public static void installNew(IEditorPart editor) {
		// install key listener?
		if (KeyPressInEditor.shouldInstall(editor)) {
			KeyPressInEditor kpie = new KeyPressInEditor(editor);
			LoggerInstaller.trackLogger(kpie);
		}
	}
	
	
	
	public void installMe(IEditorPart editor) {
		StyledText text = null;
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditors()
		if (editor != null) {
//			if only I could import the internal package, sigh
//			if (editor instanceof JavaEditor) {
//				text = editor.getViewer().getTextWidget();
//			}
//			ITextOperationTarget target = (ITextOperationTarget)editorPart.getAdapter(ITextOperationTarget.class);
//			text = target.getTextWidget();
			
			text = (StyledText) editor.getAdapter(Control.class);
	    } 
		if (text != null) {
			text.addKeyListener(this);
			log("loggerInstall", "KeyPressInEditor installed in " + editor.getTitle());
			this.editor = (ITextEditor) editor;
			IDocumentProvider dp = this.editor.getDocumentProvider();
			this.doc = dp.getDocument(editor.getEditorInput());
			ISelectionProvider sp = this.editor.getSelectionProvider();
			sel = (ITextSelection) sp.getSelection();
			sp.addSelectionChangedListener(new selChanged(this));
		} else {
			log("loggerInstall", "KeyPressInEditor failed to installed in " + editor.getTitle());
		}

	}

	
	
// TODO	
//	public static void updateSelector(IEditorPart editor) {
//		if (editor != null) {
//			StyledText text = (StyledText) editor.getAdapter(Control.class);
//			if (text != null) {
//				Listener[] listeners = text.getListeners(SWT.KeyDown);
//				for (Listener listener : listeners) {
//					if (listener instanceof KeyPressInEditor) {
//						listener.
//						IDocumentProvider dp = this.editor.getDocumentProvider();
//						this.doc = dp.getDocument(editor.getEditorInput());
//						ISelectionProvider sp = this.editor.getSelectionProvider();
//						sel = (ITextSelection) sp.getSelection();
//						sp.addSelectionChangedListener(new selChanged(this));
//
//					}
//				}
//			}
//		}
//	}
	
	
	public static boolean shouldInstall(IEditorPart editor) {
		// first, needs to be a text editor -- we only care about java, though
		if (editor instanceof AbstractDecoratedTextEditor) {
			IEditorInput input = editor.getEditorInput();
			// Check that the editor is reading from a file
			IFile file = (input instanceof FileEditorInput)
					? ((FileEditorInput)input).getFile()
					: null;
			return edu.berkeley.eduride.base_plugin.isafile.ISAUtil.withinISAProject(file);
		}
		return false;
	}



	/// helpers
	
	public static IEditorPart getActiveEditor() {
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		return editor;
	}

	
	// if there are multipage editor dealios
	// from
	// https://github.com/emmetio/emmet-eclipse/blob/master/io.emmet.eclipse/src/io/emmet/eclipse/EclipseEmmetHelper.java
	public static IEditorPart getTextEditor(IEditorPart editor) {
		if (editor instanceof MultiPageEditorPart) {
			IEditorPart currentPage = (IEditorPart) ((MultiPageEditorPart) editor)
					.getSelectedPage();

			if (currentPage instanceof ITextEditor) {
				editor = (ITextEditor) currentPage;
			} else {
				ITextEditor adapter = (ITextEditor) ((MultiPageEditorPart) editor)
						.getAdapter(ITextEditor.class);
				if (adapter != null) {
					editor = adapter;
				} else {
					editor = null;
				}
			}
		}

		return editor;
	}
	
	// ///////

	private class selChanged implements ISelectionChangedListener {

		KeyPressInEditor kpie;
		
		public selChanged(KeyPressInEditor kpie) {
			this.kpie = kpie;
		}
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			kpie.sel = (ITextSelection) event.getSelection();
		}
		
	}
	
	
	
	private StringBuilder keys = new StringBuilder(100);
	
	@Override
	public void keyPressed(KeyEvent ke) {
		try {
			if (sel != null) {
				int curline;
				curline = doc.getLineOfOffset(sel.getOffset());
				if (curline == line) {
					keys.append(ke.character);
				} else {
					String out = keys.toString();
					line = curline;
					keys.setLength(0);
					keys.append(line);
					keys.append("::");
					keys.append(ke.character);
					log(out); // do this at the end, since its the slowest
				}
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

}
