package edu.berkeley.eduride.loggerplugin.loggers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import edu.berkeley.eduride.base_plugin.util.Base64Coder;
import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.base_plugin.util.FileUtil;
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

	public void log(String content) {
		log(myType, content);
	}

	public void log(File txtfile) throws FileNotFoundException {
		log(myType, txtfile);
	}

	public void log(String action, String content) {
		EduRideLogger.log(action, content);
		//Console.msg("(" + System.currentTimeMillis() + ") LOGGER " + action + ": " + content);
	}


	public void log(String action, File txtfile) throws FileNotFoundException {
		String fStr = FileUtil.getContents(txtfile);
		char[] charr = Base64Coder.encode(fStr.getBytes());
		log(action, new String(charr));
	}

	
	
	public static void logStatic(String action, String content) {
		EduRideLogger.log(action, content);
//		Console.msg("(" + System.currentTimeMillis() + ") LOGGER " + action + ": " + content);
	}

	
	
	
}
