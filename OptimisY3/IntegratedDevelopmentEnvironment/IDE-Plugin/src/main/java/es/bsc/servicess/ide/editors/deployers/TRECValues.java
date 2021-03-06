/*
 *  Copyright 2011-2013 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package es.bsc.servicess.ide.editors.deployers;

public class TRECValues extends Object {
	private float trustValue;
	private float riskValue;
	private float ecoValue;
	private float costValue;
	
	public TRECValues(float trustValue, float riskValue, float ecoValue,
			float costValue) {
		super();
		this.trustValue = trustValue;
		this.riskValue = riskValue;
		this.ecoValue = ecoValue;
		this.costValue = costValue;
	}
	public float getTrustValue() {
		return trustValue;
	}
	public void setTrustValue(float trustValue) {
		this.trustValue = trustValue;
	}
	public float getRiskValue() {
		return riskValue;
	}
	public void setRiskValue(float riskValue) {
		this.riskValue = riskValue;
	}
	public float getEcoValue() {
		return ecoValue;
	}
	public void setEcoValue(float ecoValue) {
		this.ecoValue = ecoValue;
	}
	public float getCostValue() {
		return costValue;
	}
	public void setCostValue(float costValue) {
		this.costValue = costValue;
	}
	
	
}
