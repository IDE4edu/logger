package edu.berkeley.eduride.loggerplugin.loggers;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.equinox.log.LogFilter;

import navigatorView.NavigatorActivator;
import navigatorView.controller.NavigationListener;
import navigatorView.model.Assignment;
import navigatorView.model.Step;

public class NavigationLogger extends AbstractLogger implements NavigationListener {

	public NavigationLogger() {
		this(true);
	}
	
	public NavigationLogger(boolean install) {
		super("nav");
		if (install) {
			installMe();
		}
	}
	
	
	public void installMe() {
		NavigatorActivator.getDefault().registerListener(this);
		log("loggerInstall", "Navigator logger is installed");
	}
	
	
	
	
	
	@Override
	public void stepChanged(Step oldstep, Step newstep) {
		// steps might be null, yo
		log("stepChanged", "Leave " + oldstep + " goto " + newstep);
		if (oldstep != null && oldstep.isCODE()) {
			try {
				log("File", oldstep.getSourceFile());
			} catch (FileNotFoundException e) {
				log("FileError", "couldn't log file contents for step:" + oldstep.getName());
			}
		}
		if (newstep != null && newstep.isCODE()) {
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
