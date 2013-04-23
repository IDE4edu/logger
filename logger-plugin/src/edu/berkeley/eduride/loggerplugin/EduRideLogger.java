package edu.berkeley.eduride.loggerplugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EduRideLogger extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.berkeley.eduride.loggerplugin"; //$NON-NLS-1$

	// The shared instance
	private static EduRideLogger plugin;
	
	/**
	 * The constructor
	 */
	public EduRideLogger() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EduRideLogger getDefault() {
		return plugin;
	}


	public static void log(String action, String content) {
		// subject/who is the workspaceID
	}
	
	
    public static void pushLogsToServer() {
    	// start a thread for this?
    	
    }
}
