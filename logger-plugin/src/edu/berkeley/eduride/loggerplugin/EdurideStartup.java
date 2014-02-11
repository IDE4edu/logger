package edu.berkeley.eduride.loggerplugin;

import edu.berkeley.eduride.base_plugin.IStartupSync;

public class EdurideStartup implements IStartupSync {

	@Override
	public void install() {
		EduRideLogger.log("Install", "Logger plugin started");
		
	}


}
