package edu.berkeley.eduride.logger_plugin.loggers;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class keyPressInEditor extends Base implements KeyListener {

	

//	public keyPressInEditor() {
//		super();
//	}
	
	
	public keyPressInEditor() {
		
	}
	

	public void installMe(IEditorPart editor) {
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditors()
		
	}
	
	
	/////////
	
	
	@Override
	public void keyPressed(KeyEvent ke) {
		// nothing
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		
	}


	
	
}
