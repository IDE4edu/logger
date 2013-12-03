package edu.berkeley.eduride.loggerplugin.logEntry;

import org.json.JSONObject;

public class EditorTimelineEvent extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8091237048895320954L;

	private String source;
	private boolean compilable;
	// If null, edit not in a method.
	private String methodName;

	// IJavaElement stuff

	public EditorTimelineEvent(String action, String content, long timestamp) {
		super(action, content, timestamp);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns a JSONArray representation of this class. The JSONArray will have four elements:
	 * the action field, the content field, the serialized string version of this class, and 
	 * the timestamp. 
	 * 
	 * NOTE: This is opposite behavior from the parent class.
	 * 
	 * @return a JSONArray with the action, content, the serialized string version of this class,
	 *  and timestamp. 
	 */
	public JSONObject asJSONObject() {
		return asJSONObject(true);
	}

}
