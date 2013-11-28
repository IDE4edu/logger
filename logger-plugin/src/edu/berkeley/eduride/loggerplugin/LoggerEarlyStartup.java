package edu.berkeley.eduride.loggerplugin;

import org.eclipse.ui.IStartup;

public class LoggerEarlyStartup implements IStartup {

	@Override
	public void earlyStartup() {
		EduRideLogger.log("loggerInit", "Early Startup is right now, yo");

	}

}
