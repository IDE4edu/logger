package edu.berkeley.eduride.loggerplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;

import edu.berkeley.eduride.base_plugin.EduRideBase;
import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.loggerplugin.logEntry.LogEntry;
import edu.berkeley.eduride.loggerplugin.loggers.AbstractLogger;

/**
 * The activator class controls the plug-in life cycle
 */
public class EduRideLogger extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.berkeley.eduride.loggerplugin"; //$NON-NLS-1$
	private static final String PUSH_TARGET = "/log/";

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
		makeWorkspaceIDFile();
		memoryStore = new ArrayList<LogEntry>(5000);

		//TODO set a global to keep logger from initializing.

		if (logStorage != null) {
			if (getObjectLogFile().exists()) {
		    	// TODO this doesn't seem to work
				pushObjectLogFromFile(getObjectLogFile());
				retireLogFile(getObjectLogFile());
			}
	    	openTextLogFileWriter = initTextLogFile();
	    	openObjectLogStream = initObjectLogFile();
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
		pushLogsToServer(false);
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


	
	////
	
	private static void reportLoggerError(String msg) {
		Console.err("LOGGER: " + msg);
	}
	
	private static void reportLoggerError(Exception e) {
		Console.err("LOGGER::" + e.toString());
	}
	
	private static void reportLoggerError(String locMsg, Exception e) {
		Console.err("LOGGER " + locMsg + "\n" + e.toString());
	}
	
	////////////////////////////////
	
	
	
	public static void log(String action, String content) {
		// this should do a low latency append to a file.
		// needs to keep timestamp on each entry!
		LogEntry le = new LogEntry(action, content, System.currentTimeMillis());
		if (memoryStore != null) {
			memoryStore.add(le);
		} else {
			Console.msg ("Missed log event! " + action + content);
		}
		// this is ugly slow right now -- speed it up!
		try {
			openObjectLogStream.writeObject(le);
			openObjectLogStream.flush();
		} catch (IOException e) {
			reportLoggerError("log() #1", e);
		}
		try {
			openTextLogFileWriter.write(le.asJSONObject().toString());
			openTextLogFileWriter.write(",");
			openTextLogFileWriter.flush();
		} catch (IOException e) {
			reportLoggerError("log() #2", e);
		}
	}
	
	
	public static void pushLogsToServer() {
		pushLogsToServer(true);
	}
	
    private static void pushLogsToServer(boolean logit) {
    	// start a thread for this?
    	// needs to create a json object from workspace_id and log file to send to server.  See github wiki
    	int sent = pushLogFromStore(memoryStore);
    	if (logit) {
    		log("logsSent", "number of log entrys just sent: " + sent);
    	}
    }
	
	
    public void installLoggers() {
    	// install the listeners, yo.
    	LoggerInstaller.start();
    }
    
    
	
	//////////////////////////////
	/// This should all move out to GenericLogEntry and LogEntryList

	private static IPath logStorage = null;
	private static FileWriter openTextLogFileWriter = null;
	private static ObjectOutputStream openObjectLogStream = null;
	private static ArrayList<LogEntry> memoryStore = null;
	
	private static int pushLogFromStore(ArrayList<LogEntry> store)  {
		if (store.size() == 0) {
			return 0;
		}
		int sent = 0;
		HttpURLConnection connection = null;
		String domain = EduRideBase.getDomain();
		URL target = null;
		BufferedReader rd = null;
		try {
			target = new URL("http", domain, PUSH_TARGET);
			connection = (HttpURLConnection) target.openConnection();
			String logParams = generateLogJson(store);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(logParams.getBytes().length));
			PrintWriter wr = new PrintWriter(connection.getOutputStream());
			wr.write(logParams);
			wr.flush();
			wr.close();

			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}
			String jsonstr = sb.toString();
			JSONObject jsonobj = new JSONObject(jsonstr);
			String success = jsonobj.getString("status");
			if (!(success.equals("success"))) {
				// failed, for some reason
				throw new IOException(jsonobj.getString("message"));
			} else {
				sent = store.size();
				clearLogs();
			}
		} catch (MalformedURLException e) {
			reportLoggerError("The default domain " + domain + " does not form a proper URL.");
		} catch (IOException e) {
			// lots of ways to get here
			if (connection == null) {
				reportLoggerError("Unable to connect to " + domain + ".  Is the server down?");
			} else if (rd == null) {
				reportLoggerError("Unable to get input stream to " + domain + ".  Is the server down?");
			} else {
				reportLoggerError("pushLogFromStore()" , e);
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return sent;

	}
	
	// TODO fails right now on second read...  class AC unknown?
    private static int pushObjectLogFromFile(File f) {
    	ArrayList<LogEntry> fileStore = new ArrayList<LogEntry>();
    	ObjectInputStream stream = null;
    	FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			stream = new ObjectInputStream(fis);
		} catch (FileNotFoundException e) {
			// new FileInputStream failed
			reportLoggerError("pushObjectLogFromFile() new FileInputStream failed", e);
			return 0;
		} catch (IOException e) {
			//ObjectInputStream failed
			reportLoggerError("pushObjectLogFromFile() new ObjectInputStream failed", e);
			return 0;
		}
		try {
	    	while (true) {
	    		Object o;
				o = stream.readObject();
	    		fileStore.add((LogEntry) o);
	    	}
		} catch (ClassNotFoundException e) {
			reportLoggerError("pushObjectLogFromFile()", e);
		} catch (IOException e) {
			// reached end of stream
			//reportLoggerError(e);
		}
		try {
			if (fis != null) {
				fis.close();
			}
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			reportLoggerError("pushObjectLogFromFile()", e);
		}
		return pushLogFromStore(fileStore);
    }

    
    // not gonna check is store is empty, since it won't break
	private static String generateLogJson(ArrayList<LogEntry> store) {
		JSONObject rootjson = new JSONObject();
		rootjson.put("w", EduRideBase.getWorkspaceID());
		JSONArray logjson = new JSONArray();
		for (LogEntry entry : store) {
			logjson.put(entry.asJSONObject());
		}
		rootjson.put("logs", logjson);
		return rootjson.toString();
	}

	private static FileWriter initTextLogFile() {
		FileWriter fw = null;
		if (logStorage != null) {
			try {
				fw = new FileWriter(getTextLogFile(), true);
				fw.write("{\"w\":\"" + EduRideBase.getWorkspaceID() + "\",\"logs\":[");  // start the regular log file, why not
			} catch (IOException e) {
				reportLoggerError("initTextLogFile()", e);
			}
		}
		return fw;
	}
	
	private static ObjectOutputStream initObjectLogFile() {
		FileOutputStream fw = null;
		ObjectOutputStream oos = null;
		if (logStorage != null) {
			try {
				fw = new FileOutputStream(getObjectLogFile(), true);
				oos = new ObjectOutputStream(fw);
			} catch (IOException e) {
				reportLoggerError("initObjectLogFile()", e);
			}
		}
		return oos;
	}
	
	
	private void makeWorkspaceIDFile() {
		File f = logStorage.append("workspaceID").toFile();
		if (!f.exists()) {
			//FileOutputStream fos = new FileOutputStream(f);
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(EduRideBase.getWorkspaceID());
				fw.flush();
				fw.close();
				//log("install", "made the workspace ID file");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//log("installFail", "couldn't write the workspace ID file");
				reportLoggerError("makeWorkspaceIDFile()", e);
			}
		}

		
	}

	
	private static File getTextLogFile() {
		File lf = null;
		if (logStorage != null) {
			lf = logStorage.append(getTextLogFileName()).toFile();
		}	
		return lf;
	}
	
	private static File getObjectLogFile() {
		File lf = null;
		if (logStorage != null) {
			lf = logStorage.append(getObjectLogFileName()).toFile();
		}	
		return lf;
	}
	
	private static String getTextLogFileName() {
		return "eduRideTextLogFile.log";
		// maybe someday will rotate, etc.
	}
	
	private static String getObjectLogFileName() {
		return "eduRideObjectLogFile.log";
		// maybe someday will rotate, etc.
	}
	
	
	private static void clearLogs() {
		memoryStore.clear();
		// ignore the file for now, hey.
	}
	
	private static void retireLogFile(File f) {
		f.delete();
		// maybe someday we'll rotate, etc.
	}
	
	//////////
	

    

}
