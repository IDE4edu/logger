package edu.berkeley.eduride.loggerplugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
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
		logStorage = plugin.getStateLocation();
		memoryStore = new ArrayList<LogEntry>();
		memoryStore.ensureCapacity(5000);

    	
		if (logStorage != null) {
			if (getLogFile().exists()) {
				// old log file exists!
				pushLogFileToServer(getLogFile());
				retireLogFile(getLogFile());
			}
	    	openLogFile = initLogFile();
			installLoggers();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		pushLogsToServer();
		
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


	////////////////////////////////
	
	
	
	public static void log(String action, String content) {
		// this should do a low latency append to a file.
		// needs to keep timestamp on each entry!
		LogEntry le = new LogEntry(action, content, System.currentTimeMillis());
		memoryStore.add(le);
		// maybe use serialization for the LogEntry? 
	}
	
	
    public static void pushLogsToServer() {
    	// start a thread for this?
    	// needs to create a json object from workspace_id and log file to send to server.  See github wiki

    }
	
	
    public static void installLoggers() {
    	// install the listeners, yo.
    	
    }
    
    
	
	//////////////////////////////
	

	private static IPath logStorage = null;
	private static FileWriter openLogFile = null;
	private static ArrayList<LogEntry> memoryStore = null;
	
	private static class LogEntry implements Serializable {
		private final long serialVersionUID = 12980178901023L;
		private final String action;
		private final String content;
		private final Long timestamp;
		
		public LogEntry(String action, String content, long timestamp) {
			this.action = action;
			this.content=content;
			this.timestamp = timestamp;
		}
	}

	
    private static void pushLogFileToServer(File f) {
    	
    }

    
	private static FileWriter initLogFile() {
		FileWriter fw = null;
		if (logStorage != null) {
			try {
				fw = new FileWriter(getLogFile(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fw;
	}
	
	
	private static File getLogFile() {
		File lf = null;
		if (logStorage != null) {
			lf = logStorage.append(getLogFileName()).toFile();
		}	
		return lf;
	}
	
	private static String getLogFileName() {
		return "eduRideLogFile.log";
		// maybe someday will rotate, etc.
	}
	
	private static void retireLogFile(File f) {
		f.delete();
		// maybe someday we'll rotate, etc.
	}
	
	//////////
	

    

}
