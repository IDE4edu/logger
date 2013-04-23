package edu.berkeley.eduride.loggerplugin.loggers;

import java.util.Calendar;

import edu.berkeley.eduride.loggerplugin.EduRideLogger;



public abstract class Base {

	protected String myType = ""; 
	
	public Base() {
		myType = this.getClass().getSimpleName();
	}
	
	public Base(String name) {
		myType = name;
	}
	
	
	public void log(String content){
		log(myType, content);
	}
	
	public void log(String action, String content) {
		EduRideLogger.log(action, content);
		System.out.println("(" + System.currentTimeMillis() + ") LOGGER " + action + ": " + content);
	}
	

}
