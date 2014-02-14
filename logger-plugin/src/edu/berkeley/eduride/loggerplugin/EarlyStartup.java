package edu.berkeley.eduride.loggerplugin;

import org.eclipse.ui.IStartup;
import edu.berkeley.eduride.base_plugin.IStartupSync;




// can be used with either the eduride or ui startup extension points
public class EarlyStartup implements IStartupSync, IStartup {

	@Override
	public void install() {
		EduRideLogger.log("Install", "Logger plugin started");
		
	}

	@Override
	public void earlyStartup() {
		install();

	}


}
