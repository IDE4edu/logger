package edu.berkeley.eduride.loggerplugin.loggers;

import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import edu.berkeley.eduride.loggerplugin.loggers.Base;

public class LoggerInstaller {

	static ArrayList <Base> loggers = new ArrayList<Base>();

	// this gets called from Base constructor
	public static void trackLogger(Base logger) {
		loggers.add(logger);
	}
	
	
	
	public static void start() {
		
		try {
			PlatformUI.getWorkbench();
		} catch (IllegalStateException e) {
			// getWorkbench() failed.
			Base.logStatic("loggerInstallFailure", "Workbench wasn't installed yet...");
			return;
		} 

		
		new NavigatorLogger(true);

		new EditorEventListener(true);
		
		Base.logStatic("loggerInstall", "finished installing loggers");
	}
	
	

	
	public static void stop() {
		// eh, why would we stop things?
	}
}
