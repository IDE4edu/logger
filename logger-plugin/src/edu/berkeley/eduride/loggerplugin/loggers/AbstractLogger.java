package edu.berkeley.eduride.loggerplugin.loggers;

import java.util.Calendar;

import edu.berkeley.eduride.loggerplugin.EduRideLogger;
import edu.berkeley.eduride.loggerplugin.LoggerInstaller;



public abstract class AbstractLogger {

	
	static String defaultType = "baseLogger";
	protected String myType = ""; 
	
	public AbstractLogger() {
		myType = this.getClass().getSimpleName();
		LoggerInstaller.trackLogger(this);
		log("loggerInstall", myType + " instantiated standard.");
	}
	
	public AbstractLogger(String name) {
		myType = name;
		LoggerInstaller.trackLogger(this);
		log("loggerInstaller", myType + " instantiated.");
	}
	
	
	// Note -- logging should move out to LogEvents
	
	public void log(String content){
		log(myType, content);
	}
	
	public void log(String action, String content) {
		EduRideLogger.log(action, content);
		System.out.println("(" + System.currentTimeMillis() + ") LOGGER " + action + ": " + content);
	}
	
	
	public static void logStatic(String action, String content) {
		EduRideLogger.log(action, content);
		System.out.println("(" + System.currentTimeMillis() + ") LOGGER " + action + ": " + content);
	}

}
