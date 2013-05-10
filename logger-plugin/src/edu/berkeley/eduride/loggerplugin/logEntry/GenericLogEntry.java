package edu.berkeley.eduride.loggerplugin.logEntry;

import java.io.Serializable;

import edu.berkeley.eduride.loggerplugin.EduRideLogger;


// TODO someday, this is probably right
public class GenericLogEntry implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private String message = null;
	private long timestamp;
	private String type = null;
	
	
	
	public GenericLogEntry(String message, boolean commitToLog) {
		//type = this.getClass().getSimpleName();   // this is slow
		this.message = message;
		this.timestamp = System.currentTimeMillis();
		if (commitToLog) {
			this.log();
		}
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
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void log(String message) {
		//EduRideLogger.log(klass, message);
	}

	
	public void log() {
		//EduRideLogger.log(klass, message);
	}
}
