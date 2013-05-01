package edu.berkeley.eduride.loggerplugin.loggers;

//import org.eclipse.jdt.internal.ui.JavaEditor;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;



public class KeyPressInEditor extends Base implements KeyListener {

	// public KeyPressInEditor() {
	// super();
	// }

	public KeyPressInEditor(IEditorPart editor) {
		super("kpie");
		installMe(editor);
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
			log("LoggerInstall", "KeyPressInEditor installed in " + editor.getTitle());
		} else {
			log("LoggerInstall", "KeyPressInEditor failed to installed in " + editor.getTitle());
		}

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

	@Override
	public void keyPressed(KeyEvent ke) {
		log(String.valueOf(ke.character));
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

}
