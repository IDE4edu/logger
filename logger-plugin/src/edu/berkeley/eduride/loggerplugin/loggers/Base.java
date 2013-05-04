package edu.berkeley.eduride.loggerplugin.loggers;

import java.util.Calendar;

import edu.berkeley.eduride.loggerplugin.EduRideLogger;



public abstract class Base {

	protected String myType = ""; 
	
	public Base() {
		myType = this.getClass().getSimpleName();
		LoggerInstaller.trackLogger(this);
		log("loggerInstall", myType + " instantiated standard.");
	}
	
	public Base(String name) {
		myType = name;
		LoggerInstaller.trackLogger(this);
		log("loggerInstaller", myType + " instantiated.");
	}
	
	
	
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
