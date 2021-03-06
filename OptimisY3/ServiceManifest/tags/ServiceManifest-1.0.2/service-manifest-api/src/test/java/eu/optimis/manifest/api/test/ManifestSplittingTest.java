/*
 * Copyright (c) 2012, Fraunhofer-Gesellschaft
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the disclaimer at the end.
 *     Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (2) Neither the name of Fraunhofer nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * DISCLAIMER
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.optimis.manifest.api.test;

import eu.optimis.manifest.api.ip.Manifest;
import eu.optimis.manifest.api.sp.AffinityRule;
import eu.optimis.manifest.api.sp.ElasticityRule;
import eu.optimis.types.xmlbeans.servicemanifest.XmlBeanServiceManifestDocument;

/**
 * @author arumpl
 */
public class ManifestSplittingTest extends AbstractTestApi {

    public ManifestSplittingTest(String testName) {
        super(testName);
    }

   public void testShouldExctractAComponentByItsIdToANewManifest(){
       String componentId = "Grumpy";
       //for the tests we add the component we are going to extract:
       manifest.getVirtualMachineDescriptionSection().addNewVirtualMachineComponent(componentId);

       //allow federation
       manifest.getVirtualMachineDescriptionSection().setIsFederationAllowed(true);
       //set affinity to the existing component to low

       //add an elasticity rule
       ElasticityRule eRule = manifest.getElasticitySection().addElasticityRule(componentId);
       eRule.setTolerance(20);

       //add our componentId to the affinity rule at first position, which holds the rule "Low" for the first the jboss component.
       manifest.getVirtualMachineDescriptionSection().getAffinitySection().getRuleArray(0).getScope().addComponentId(componentId);



       //first we need an IP Manifest.
       XmlBeanServiceManifestDocument xmlBeanManifest = manifest.toXmlBeanObject();
        
       Manifest ipManifest =   Manifest.Factory.newInstance(xmlBeanManifest);



       //now we want to extract

       Manifest extractedManifest =  ipManifest.extractComponent(componentId);

       //check that the extracted manifest contains no extension sections
       assertNull(extractedManifest.getInfrastructureProviderExtensions());

   }


}
