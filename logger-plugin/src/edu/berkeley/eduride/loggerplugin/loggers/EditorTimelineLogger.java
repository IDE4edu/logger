package edu.berkeley.eduride.loggerplugin.loggers;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorPart;

public class EditorTimelineLogger extends AbstractLogger implements IElementChangedListener {

	public static void installMe(EditorTimelineLogger logger) {
		JavaCore.addElementChangedListener(logger);
	}
	
	
	/*
	 * This logger has a current editor that it is following, and gets
	 * all JavaModelEvent changes, decided whether to make a EditorTimelineEvent
	 * out of them.
	 * 
	 * This is duplicated, a whole lot, the FeedbackController stuff.  Should
	 * be merged into Base, yo
	 */
	
	// this is the class that we are currently tracking.  Or, maybe we care
	// about an editor, not a class?
	ICompilationUnit current = null;
	

	
	///////
	// pass null to unfollow the current
	public void follow(IEditorPart ed) {
		// TODO sets current editor to be followed; could be null
		
	}
	
	
	
	
	
	private void log() {
		// TODO 
		// make a new EditorTimelineEvent (IJavaElementDelta, ICompilationUnit)
	}
	

	
	
	
	
	@Override
	public void elementChanged(ElementChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
