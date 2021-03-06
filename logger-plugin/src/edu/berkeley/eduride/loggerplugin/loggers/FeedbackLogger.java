package edu.berkeley.eduride.loggerplugin.loggers;

import edu.berkeley.eduride.feedbackview.EduRideFeedback;
import edu.berkeley.eduride.feedbackview.FeedbackListener;

public class FeedbackLogger extends AbstractLogger implements FeedbackListener {

	StringBuilder msg = new StringBuilder(200);
	
	public FeedbackLogger() {
		this(true);
	}
	
	public FeedbackLogger(boolean install) {
		super("feedback");
		if (install) {
			installMe();
		}
	}
	
	
	public void installMe() {
		EduRideFeedback.registerListener(this);
		log("loggerInstall", "Feedback logger is installed");
	}


	@Override
	public void logTestResult(String testClassName, String testName,
			boolean success, String message, String observed) {
		msg.setLength(0);  // clear the current contents
		msg.append("<tr><class>");
		msg.append(testClassName);
		msg.append("</class><testname>");
		msg.append(testName);
		msg.append("</testname><success>");
		msg.append(success);
		msg.append("</success><msg><![CDATA[");
		msg.append(message);
		msg.append("]]></msg><observed>");
		msg.append(observed);
		msg.append("</observed></tr>");
		log("testResult", msg.toString());
	}

	

	
	
}
