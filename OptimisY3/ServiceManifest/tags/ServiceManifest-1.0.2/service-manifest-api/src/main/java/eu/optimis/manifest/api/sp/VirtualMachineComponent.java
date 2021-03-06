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
 *  
 */
package eu.optimis.manifest.api.sp;

import eu.optimis.manifest.api.ovf.sp.OVFDefinition;
import eu.optimis.types.xmlbeans.servicemanifest.XmlBeanAffinityConstraintType;

/**
 * @author owaeld
 */
public interface VirtualMachineComponent {

    /**
     * Retrieves the OVF Definition section . It provides information on location, format, network connection and
     * virtual system description to be used for creating component instances.
     *
     * @return the ovf section
     * @see OVFDefinition
     */
    OVFDefinition getOVFDefinition();

    /**
     * Retrieves the allocation constraints. It is used to define the maximum and minimum number of component instances.
     *
     * @return the allocation constraints
     */
    AllocationConstraint getAllocationConstraints();

    /**
     * @return affinity constraint as one of [Low | Medium | High]
     * @see XmlBeanAffinityConstraintType.Enum
     */
    String getAffinityConstraints();

    /**
     * Affinity Constraints describe the level of affinity the incarnated instances must have.
     *
     * @param constraint one of [Low | Medium | High]
     * @see XmlBeanAffinityConstraintType
     */
    void setAffinityConstraints(String constraint);

    /**
     * The ID of the component. This must be unique throughout the manifest. This is set when adding a component and
     * cannot be changed afterwards.
     *
     * @return the component id
     */
    String getComponentId();


}