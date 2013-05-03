package edu.berkeley.eduride.loggerplugin.loggers;

//import org.eclipse.jdt.internal.ui.JavaEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
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
import org.eclipse.ui.texteditor.ITextEditor;



public class KeyPressInEditor extends Base implements KeyListener {

	public static QualifiedName isa_key = new QualifiedName("edu.berkeley.eduride","isAssignment");


	public KeyPressInEditor(IEditorPart editor) {
		super("kpie");
		install(editor);
	}

	public void install(IEditorPart editor) {
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
		} else {
			log("loggerInstall", "KeyPressInEditor failed to installed in " + editor.getTitle());
		}

	}

	
	public static boolean shouldInstall(IEditorPart editor) {
		// first, needs to be a text editor -- we only care about java, though
		if (editor instanceof AbstractDecoratedTextEditor) {
			IEditorInput input = editor.getEditorInput();
			// Check that the editor is reading from a file
			IFile file = (input instanceof FileEditorInput)
					? ((FileEditorInput)input).getFile()
					: null;
			if (file != null) {
				// look at the files in the contained project, see if there
				// is an ISA file there.
				IProject proj = file.getProject();
				boolean containsISA = false;
				try {
					proj.setSessionProperty(isa_key, null);
					proj.accept(new IResourceVisitor() {			
						@Override
						public boolean visit(IResource resource) throws CoreException {
							if (!(resource.getType() == IResource.FILE)) return true;
							String extension = resource.getFileExtension();
							if (extension != null) {
								if (extension.equalsIgnoreCase("isa")) {
									resource.getProject().setSessionProperty(isa_key, new Boolean(true));
								}
							}
							return true;
						}
					});
					if (((Boolean)proj.getSessionProperty(isa_key)).booleanValue()) {
						containsISA = true;
					}
				} catch (CoreException e) {
					//e.printStackTrace();
				}
				return containsISA;
			}
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

	@Override
	public void keyPressed(KeyEvent ke) {
		log(String.valueOf(ke.character));
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

}
