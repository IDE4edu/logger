package edu.berkeley.eduride.loggerplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.berkeley.eduride.base_plugin.EduRideBase;
import edu.berkeley.eduride.loggerplugin.loggers.AbstractLogger;

/**
 * The activator class controls the plug-in life cycle
 */
public class EduRideLogger extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.berkeley.eduride.loggerplugin"; //$NON-NLS-1$
	private static final String PUSH_TARGET = "/log";

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
				pushLogFromFile(getLogFile());
				retireLogFile(getLogFile());
			}
	    	openLogFile = initLogFile();
			installLoggers();
		} else {
			AbstractLogger.logStatic("loggerInitFailer", "logStorage is null, yo");
		}
		AbstractLogger.logStatic("loggerInit", "Logger bundle started");
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		log("loggerInit", "Logger bundle shutting down");
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
		if (memoryStore != null) {
			memoryStore.add(le);
		} else {
			System.out.println("Missed log event! " + action + content);
		}
		try {
			ObjectOutputStream o = new ObjectOutputStream(openLogFile);
			o.writeObject(le);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
    public static void pushLogsToServer() {
    	// start a thread for this?
    	// needs to create a json object from workspace_id and log file to send to server.  See github wiki
    	pushLogFromStore(memoryStore);
    }
	
	
    public void installLoggers() {
    	// install the listeners, yo.
    	LoggerInstaller.start();
    }
    
    
	
	//////////////////////////////
	/// This should all move out to GenericLogEntry and LogEntryList

	private static IPath logStorage = null;
	private static FileOutputStream openLogFile = null;
	private static ArrayList<LogEntry> memoryStore = null;
	
	private static class LogEntry implements Serializable {
		private static final long serialVersionUID = 2L;
		public final String action;
		public final String content;
		public final Long timestamp;
		
		public LogEntry(String action, String content, long timestamp) {
			this.action = action;
			this.content=content;
			this.timestamp = timestamp;
		}
	}

	private static void pushLogFromStore(ArrayList<LogEntry> store) {
		if (store.size() == 0) {
			return;
		}
		try {
			URL target = new URL("http", EduRideBase.getDomain(), PUSH_TARGET);
			HttpURLConnection connection = (HttpURLConnection) target.openConnection();           
    		String logParams = generateLogJson(store);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST"); 

			PrintWriter wr = new PrintWriter(connection.getOutputStream ());
			wr.write(logParams);
			wr.flush();
			wr.close();
			connection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private static void pushLogFromFile(File f) {
    	ArrayList<LogEntry> fileStore = new ArrayList<EduRideLogger.LogEntry>();
    	ObjectInputStream stream;
		try {
			FileInputStream fis = new FileInputStream(f);
			stream = new ObjectInputStream(fis);
		} catch (FileNotFoundException e) {
			// new FileInputStream failed
			e.printStackTrace();
			return;
		} catch (IOException e) {
			//ObjectInputStream failed
			e.printStackTrace();
			return;
		}
		try {
	    	while (true) {
	    		Object o;
				o = stream.readObject();
	    		fileStore.add((LogEntry) o);
	    	}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// reached end of stream
		}
		pushLogFromStore(fileStore);
    }

    
	private static String generateLogJson(ArrayList<LogEntry> store) {
		String json = "logs={w: '" + EduRideBase.getWorkspaceID() + "', logs: [";
		for (LogEntry entry: store) {
			json += "[" + entry.action + ", " + entry.content + ", " + entry.timestamp + "],";
		}
		json += "]}";
		return json;
	}

	private static FileOutputStream initLogFile() {
		FileOutputStream fw = null;
		if (logStorage != null) {
			try {
				fw = new FileOutputStream(getLogFile(), true);
			} catch (IOException e) {
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
