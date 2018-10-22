package loan;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	static private ResourceBundle m_bundle;
	static public String getMessage(String messageKey) {
		prepareMessages();
		String message;
		if(m_bundle==null) return messageKey;
		try {
			message=m_bundle.getString(messageKey);
			
		} catch(MissingResourceException e1) {
			message=messageKey;
		}
		
		return message;
	}

	
	static private void prepareMessages() {
		if(m_bundle==null)
			try {
				m_bundle=ResourceBundle.getBundle("loan.messages");
			}catch(MissingResourceException e) {
				m_bundle=null;
			}
	}
}
