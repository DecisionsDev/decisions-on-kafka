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
package odm.ds.loanvalidation;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import loan.Report;

/**
 * 
 * Creates an object Reply which has as members an object Report from Loan Validation and a string key
 *
 */
public class Reply {
	
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	private Report report;
	
	public String ExtractKeyFromJson( String payload) throws IOException {
			 
			 ObjectMapper objectMapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 Reply reply=new Reply();
			 try {
					reply=objectMapper.readValue(payload, Reply.class);
				} catch(IOException e) {
					e.printStackTrace();
					// Throw the exception above to user
					throw e;
				}
				return reply.getKey();
		
		 }

}
