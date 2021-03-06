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

import eu.optimis.manifest.api.sp.Dependency;
import eu.optimis.manifest.api.sp.ServiceProviderExtension;
import org.apache.xmlbeans.XmlException;

/**
 * @author arumpl
 */
public class SPExtensionsTest extends AbstractTestApi {

    public SPExtensionsTest(String testName) {
        super(testName);
    }


    public void testShouldModifySPExtensionSection() throws XmlException {
        //given that the sp extension was set
        ServiceProviderExtension ext = manifest.getServiceProviderExtensionSection();
        Dependency dependency = ext.getSoftwareDependenciesArray(0).addNewDependency();
        dependency.setArtifactId("artifact");
        dependency.setGroupId("group");
        dependency.setVersion("1.0");

        ext.getSoftwareDependenciesArray(0).setComponentId("jboss");
        //when we change something

        ServiceProviderExtension newExt = manifest.getServiceProviderExtensionSection();
        newExt.getSoftwareDependenciesArray(0).setComponentId("jboss200");

        // then the changes are reflected in the manifest

        assertEquals("jboss200", manifest.getServiceProviderExtensionSection().getSoftwareDependenciesArray(0).getComponentId());
    }

}
