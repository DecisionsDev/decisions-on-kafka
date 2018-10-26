/*
 *
 *   Copyright IBM Corp. 2018
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package odm.ds.kafka.odmj2seclient;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageFormatter {
	

	private static final String  MESSAGE_BUNDLE="messages";
	private static ResourceBundle bundle;
	/**
	 * 
	 * @param key
	 * @param arguments
	 * @return message : the message formated
	 * 
	 */
	String getMessage(String key, Object...arguments) {
		return null;
	}
	/**
	 *  To get the message in the right format
	 * @return
	 * 
	 */
	
	private ResourceBundle getBundle() {
		
		if(bundle==null) {
			bundle=ResourceBundle.getBundle("MESSAGE_BUNDLE");
		}
		return bundle;
		
	}
}
