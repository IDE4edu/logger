package edu.berkeley.eduride.loggerplugin.loggers;

import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import edu.berkeley.eduride.loggerplugin.loggers.BaseLogger;

public class LoggerInstaller {

	static ArrayList <BaseLogger> loggers = new ArrayList<BaseLogger>();

	// this gets called from BaseLogger constructor
	public static void trackLogger(BaseLogger logger) {
		loggers.add(logger);
	}
	
	
	
	public static void start() {
		
		BaseLogger.logStatic("loggerInstaller", "started installing loggers");
		try {
			PlatformUI.getWorkbench();
		} catch (IllegalStateException e) {
			// getWorkbench() failed.
			BaseLogger.logStatic("loggerInstallFailure", "Workbench wasn't installed yet...");
			return;
		} 

		
		new NavigatorLogger(true);

		new EditorEventListener(true);
		
		BaseLogger.logStatic("loggerInstall", "finished installing loggers");
	}
	
	

	
	public static void stop() {
		// eh, why would we stop things?
	}
}
