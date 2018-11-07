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
package odm.ds.kafka.producer;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

public class DataPartitioner implements Partitioner{

	Logger mylogger=Logger.getLogger(DataPartitioner.class.getName());
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public int partition(String arg0, Object arg1, byte[] arg2, Object arg3,
			byte[] arg4, Cluster arg5) {
		// TODO Auto-generated method stub
		return 0;
	}

}
