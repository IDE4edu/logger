package edu.berkeley.eduride.loggerplugin.loggers;

import edu.berkeley.eduride.base_plugin.model.Activity;
import edu.berkeley.eduride.base_plugin.model.Step;
import edu.berkeley.eduride.loggerplugin.EduRideLogger;
import navigatorView.NavigatorActivator;
import navigatorView.controller.NavigationListener;

public class PushOnChooseAssignmentLogger extends AbstractLogger implements NavigationListener {

	public PushOnChooseAssignmentLogger() {
		this(true);
	}
	
	public PushOnChooseAssignmentLogger(boolean install) {
		super("poca");
		if (install) {
			installMe();
		}
	}
	
	
	public void installMe() {
		NavigatorActivator.getDefault().registerListener(this);
		log("loggerInstall", "PushOnChooseAssignment is installed");
	}
	
	
	
	
	
	@Override
	public void stepChanged(Step oldstep, Step newstep) {
	}

	@Override
	public void invokeTest(Step step, String launchConfig) {
	}

	@Override
	public void openISA(Activity ass) {
		EduRideLogger.pushLogsToServer();
		System.out.println("Tried to push those logs.");
	}

	
	public void log(String action, String message) {
		super.log(action, message);
	}
	
	
}
