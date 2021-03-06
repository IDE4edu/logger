package edu.berkeley.eduride.loggerplugin;

import java.util.ArrayList;

import org.eclipse.ui.PlatformUI;
import edu.berkeley.eduride.loggerplugin.loggers.AbstractLogger;
import edu.berkeley.eduride.loggerplugin.loggers.EditorEventListener;
import edu.berkeley.eduride.loggerplugin.loggers.FeedbackLogger;
import edu.berkeley.eduride.loggerplugin.loggers.NavigationLogger;
import edu.berkeley.eduride.loggerplugin.loggers.PushOnChooseAssignmentLogger;

public class LoggerInstaller {

	static ArrayList <AbstractLogger> loggers = new ArrayList<AbstractLogger>();

	// this gets called from AbstractLogger constructor
	public static void trackLogger(AbstractLogger logger) {
		loggers.add(logger);
	}
	
	
	
	public static void start() {
		
		// not right now
		//initLoggers();
		
		

	}
	
	

	
	public static void stop() {
		// eh, why would we stop things?
	}
	
	
	
	private static void initLoggers() {
		AbstractLogger.logStatic("loggerInstaller", "started installing loggers");
		try {
			PlatformUI.getWorkbench();
		} catch (IllegalStateException e) {
			// getWorkbench() failed.
			AbstractLogger.logStatic("loggerInstallFailure", "Workbench wasn't installed yet...");
			return;
		} 

		
		new NavigationLogger(true);
		new FeedbackLogger(true);
		new PushOnChooseAssignmentLogger(true);
		new EditorEventListener(true);
		
		AbstractLogger.logStatic("loggerInstall", "finished installing loggers");
	}
	
}
