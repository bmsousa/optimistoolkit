/**

Copyright 2013 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors :
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atosresearch.eu 

**/

package eu.optimis.tf.ip.service.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import eu.optimis.tf.ip.service.IPOperation;
import eu.optimis.tf.ip.service.thread.TrustTimerIP;
import eu.optimis.tf.ip.service.utils.PropertiesUtils;

@Path("/operation/ip")
public class OperationIPtrustAPI {
	
	Logger log = Logger.getLogger(this.getClass().getName());
	
	IPOperation ipo;
	int scale = 1;
	public OperationIPtrustAPI(){
		PropertyConfigurator.configure(PropertiesUtils.getConfigFilePath("LOG"));
		TrustTimerIP ttip = TrustTimerIP.instance();
		ipo = new IPOperation();
		scale = Integer.valueOf(PropertiesUtils.getProperty("TRUST","maxRate"));
	}
	
	/**
	 * Provides the trust level for the provider asked
	 * @param entityId
	 * @return trust level
	 */
	@GET
	@Path("/getTrust/{providerId}")
	public String getTrust(@PathParam("providerId") String providerId){
		int trustvalue = 5;
		log.info("getTrust of "+providerId+" = " + trustvalue);
//		return String.valueOf(trustvalue);
		return String.valueOf(ipo.getTrust(providerId) * scale);
	}
	
	/**
	 * Provides the trust level for the provider asked
	 * @param entityId
	 * @return social network trust level
	 */
	@GET
	@Path("/sn/getTrust/{providerId}")
	public String getTrustSN(@PathParam("providerId") String providerId){
		int trustsnvalue = 4;
		log.info("getTrustSN of "+providerId+" = " + trustsnvalue);
//		return String.valueOf(trustsnvalue);
		return String.valueOf(ipo.getTrustSN(providerId) * 5);
	}
	
	/**
	 * Provides the trust level for the service asked
	 * @param entityId
	 * @return trust level
	 */
	@GET
	@Path("/getServiceTrust/{serviceId}")
	public String getServiceTrust(@PathParam("serviceId") String serviceId){
		int servicetrustvalue = 3;
		log.info("getServiceTrust of "+serviceId+" = " + servicetrustvalue);
//		return String.valueOf(servicetrustvalue);
		return String.valueOf(ipo.getServiceTrust(serviceId)*scale);
	}
	
	/**
	 * Returns the historical trust for a provider 
	 * 
	 * @param entityId
	 * @return xml file with 
	 */
	@GET
	@Path("/getHistoricTrust/{providerId}")
	public String getHistoricTrust(@PathParam("providerId") String providerId){
		return ipo.getHistoricTrust(providerId);
		//return "<trust/>";
	}
	
	/**
	 * Get an Id list for a given entity type
	 * 
	 * @param type
	 * @return a List with the entities IDs for the given type
	 */
	@GET
	@Path("/getHistoricServiceTrust/{seriveId}")
	public String getHistoricServiceTrust(@PathParam("seriveId") String serviceId) {

		return ipo.getHistoricServiceTrust(serviceId);
	}
	
}
