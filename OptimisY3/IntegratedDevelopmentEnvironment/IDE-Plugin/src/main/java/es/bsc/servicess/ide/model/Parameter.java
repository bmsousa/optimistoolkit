/*
 *  Copyright 2011-2012 Barcelona Supercomputing Center (www.bsc.es)
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

package es.bsc.servicess.ide.model;

import org.eclipse.jdt.core.Signature;

public class Parameter {
	private String type;
	private String name;

	public Parameter(String type, String name) {
		setType(type);
		this.name = name;
	}

	public String getType() {
		if (this.type.equalsIgnoreCase("Type.FILE")) {
			return type;
		} else {
			return Signature.toString(type);
		}
	}

	public void setType(String type) {
		if (type.equalsIgnoreCase("Type.FILE")) {
			this.type = type;
		} else {
			this.type = Signature.createTypeSignature(type, false);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeAsSignature() {
		return this.type;
	}

}
