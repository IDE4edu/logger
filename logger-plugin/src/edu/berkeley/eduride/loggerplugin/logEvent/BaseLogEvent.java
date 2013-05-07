package edu.berkeley.eduride.loggerplugin.logEvent;

import edu.berkeley.eduride.loggerplugin.EduRideLogger;


// TODO someday, this is probably right
public class BaseLogEvent {

	private String message;
	private Class<?> klass;
	private String type;
	
	
	
	public BaseLogEvent() {
		type = this.getClass().getSimpleName();
		klass = this.getClass();
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	
	
	public void log(String message) {
		//EduRideLogger.log(klass, message);
	}
	
	public void log() {
		//EduRideLogger.log(klass, message);
	}
}
