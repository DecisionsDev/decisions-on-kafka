/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2017. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package loan;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	static private ResourceBundle m_bundle;

	static public String getMessage(String messageKey) {
		prepareMessages();
		String message;
		if (m_bundle == null) return messageKey;
		try {
			message = m_bundle.getString(messageKey);
		} catch (MissingResourceException e1) {
				message = messageKey;
		}
		return message;
	}

	static private void prepareMessages() {
		if (m_bundle == null)
			try {
				m_bundle = ResourceBundle.getBundle("loan.messages");
			} catch (MissingResourceException e) {
				m_bundle = null;
			}
	}

}
