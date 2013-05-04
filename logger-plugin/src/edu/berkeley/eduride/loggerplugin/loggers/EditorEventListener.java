package edu.berkeley.eduride.loggerplugin.loggers;



import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/*
 * This serves as a listener for editor open, visible, etc events.  
 * 
 * It also installs the KeyPressInEditor listener on editors 
 * that are open at construction time or will open later.
 */
public class EditorEventListener extends Base implements IPartListener2 {

	
	public EditorEventListener(boolean install) {
		if (install) {
			ArrayList<String> msgs = new ArrayList<String>();
			String errString = installMe(msgs);
			if ( errString == null ) {
				log("loggerInstall", "Editor event listener installed on pages: " + Arrays.toString(msgs.toArray()));
			} else {
				log("loggerInstallFailure", "Editor event listener not installed: " + errString);
			}
			installKPIEonExistingEditors();
		}
	}
	
	
	public String installMe(ArrayList<String> msgs) {
		// add as a listener for all open editors (we throw out views in the listening methods)
		String errString = "";
		try {
			IWorkbench workbench = PlatformUI.getWorkbench(); // might throw exception
			IWorkbenchWindow windows[] = workbench == null ? null : workbench.getWorkbenchWindows();
			for (int i=0; i < windows.length ; i++) {
				IWorkbenchWindow window = windows[i];
				if (window == null) {
					errString += "Active workbench window "+ i + " is null.  ";
				} else {
					IWorkbenchPage[] pages = window.getPages();
					for (IWorkbenchPage page : pages) {
						if (page != null) {
							page.addPartListener(this);
							msgs.add(page.getLabel());
						}
						// window.getPartService().addPartListener(this); //
						// more
						// modernish
					}
					if (msgs.size() == 0) {
						errString += "No non-null pages in any workbench window.  ";
					} 
				}
			}
		} catch (IllegalStateException e) {
			errString += "getWorkbench() failed: " + e.getMessage() + ".  ";
		}
		return (errString == "" ? null : errString);
	}
	
	
	public void installKPIEonExistingEditors() {
		// find existing editors and log that they are already open, install kpie
	}

	
	private IEditorPart getEditor(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		IEditorPart editor = null;
		if (part != null && part instanceof IEditorPart) {
			editor = (IEditorPart) part.getAdapter(IEditorPart.class);
		}
		return editor;
	}

	
	///  IPartListener2 stuff

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		IEditorPart editor = getEditor(partRef);
		if (editor != null) {
		
			log("editorOpened", editor.getTitle()); 
			
			// install key listener?
			if (KeyPressInEditor.shouldInstall(editor)) {
				KeyPressInEditor kpie = new KeyPressInEditor(editor);
				LoggerInstaller.trackLogger(kpie);
			}
		}
	}

	
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		IEditorPart editor = getEditor(partRef);
		if (editor != null) {

			log("editorActivated", editor.getTitle());
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		IEditorPart editor = getEditor(partRef);
		if (editor != null) {

			log("editorBroughtToTop", editor.getTitle());
		}
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		IEditorPart editor = getEditor(partRef);
		if (editor != null) {

			log("editorHidden", editor.getTitle());
		}
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		IEditorPart editor = getEditor(partRef);
		if (editor != null) {

			log("editorVisible", editor.getTitle());
		}
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

}