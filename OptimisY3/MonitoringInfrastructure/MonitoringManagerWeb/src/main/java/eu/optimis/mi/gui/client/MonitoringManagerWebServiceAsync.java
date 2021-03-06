/**

Copyright 2012 University of Stuttgart

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

**/

package eu.optimis.mi.gui.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import eu.optimis.mi.gui.client.model.MonitoringResource;

public interface MonitoringManagerWebServiceAsync {

	public void getMonitoringResources(String level, String id,
			AsyncCallback<List<MonitoringResource>> callback);

	public void getIdMetricDateStrMonitoringResources(String id, String level,
			String metricName, String dfrom, String dto,
			AsyncCallback<String> callback);

	public void getIdMetricDateListMonitoringResources(String id, String level,
			String metricName, String dfrom, String dto,
			AsyncCallback<List<MonitoringResource>> callback);

}
