package edu.berkeley.eduride.loggerplugin.loggers;

import navigatorView.NavigatorActivator;
import navigatorView.controller.NavigationListener;
import navigatorView.model.Assignment;
import navigatorView.model.Step;

public class NavigatorLogger extends Base implements NavigationListener {

	public NavigatorLogger() {
		this(true);
	}
	
	public NavigatorLogger(boolean install) {
		super("nav");
		if (install) {
			installMe();
		}
	}
	
	
	public void installMe() {
		NavigatorActivator.getDefault().registerListener(this);
	}
	
	
	
	
	
	@Override
	public void stepChanged(Step oldstep, Step newstep) {
		log("stepChanged", "Leave " + oldstep + " goto " + newstep);
		if (newstep.isCODE()) {
			log("enterCodeStep", newstep.getSource());
		}

	}

	@Override
	public void invokeTest(Step step, String launchConfig) {
		log("navInvokeTest", "Step " + step + " launchConfig " + launchConfig);
	}

	@Override
	public void openISA(Assignment ass) {
		log("openISA", ass.getName() + " (" + ass.getProjectName() + ")");
	
	}

	
	
}
