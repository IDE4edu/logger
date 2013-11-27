package edu.berkeley.eduride.loggerplugin.logEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.json.JSONArray;

public class LogEntry implements Serializable {
	private static final long serialVersionUID = 2L;
	private final String action;
	private final String content;
	private final Long timestamp;
	
	
	public LogEntry(String action, String content, long timestamp) {
		this.action = action;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	/**
	 * Returns a JSONArray representation of this class. The JSONArray will have four elements:
	 * the action field, the content field, an empty string, and timestamp. The empty string is
	 * a placeholder for where the serialized string version of this class would go.
	 * 
	 * @return a JSONArray with the action, content, and timestamp. 
	 */
	public JSONArray asJSONArray() {
		return asJSONArray(false);
	}
	
	/**
	 * Return a JSONArray representation of this class.The JSONArray will have four elements:
	 * the action field, the content field, the string representation of the serialized version
	 * of this object and timestamp. The serialized version can be omitted by passing in false.
	 * 
	 * @param includeSerialization true if you want the serialized version, false otherwise
	 * @return a JSONArray with the action, content, and timestamp. And serialized object if requested.
	 */
	public JSONArray asJSONArray(boolean includeSerialization) {
		String[] sarr = {this.action, this.content, "", this.timestamp.toString()};
		if(includeSerialization) {
			sarr[2] = getSerialization();
		}
		
		return (new JSONArray(sarr));
	}
	
	public String getSerialization() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			oos.flush();
			return baos.toString();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getAction() {
		return action;
	}

	public String getContent() {
		return content;
	}

	public Long getTimestamp() {
		return timestamp;
	}
	
	
	
}
