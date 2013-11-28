package edu.berkeley.eduride.loggerplugin.loggers;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

public class JavaModelListener extends AbstractLogger implements IElementChangedListener {
	
	public JavaModelListener() {
	}
	
	
	static public void InstallNew(IEditorPart editor) {
		IEditorInput in = editor.getEditorInput();
		IJavaElement element = JavaUI.getEditorInputJavaElement(in);
		if (element != null) {
			JavaModelListener jvl = new JavaModelListener();
			jvl.installMe(element);
		} else {
			logStatic("loggerInstallFailure", "JavaModelListener: No java element underlying editor " + editor.getTitle());
		}
	}
	
	public void installMe(IJavaElement element) {
		JavaCore.addElementChangedListener(this);
	}
	
	
	
	////////////////////
	
	@Override
	public void elementChanged(ElementChangedEvent ev) {
		IJavaElementDelta[] deltas = ev.getDelta().getAffectedChildren();

	}

}
