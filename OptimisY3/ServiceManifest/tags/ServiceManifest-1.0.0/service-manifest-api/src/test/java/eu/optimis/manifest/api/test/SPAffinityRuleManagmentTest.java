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

/**
 * Created by IntelliJ IDEA.
 * Email: karl.catewicz@scai.fraunhofer.de
 * Date: 18.01.2012
 * Time: 14:38:31
 *
 */
package eu.optimis.manifest.api.test;

import eu.optimis.manifest.api.sp.AffinityRule;
import eu.optimis.types.xmlbeans.servicemanifest.AffinityConstraintType;
import org.apache.xmlbeans.XmlException;

public class SPAffinityRuleManagmentTest extends AbstractTestApi {
    public SPAffinityRuleManagmentTest(String testName) {
        super(testName);
    }

    public void testAddAffinityRule() {
        System.out.println("\naddNewAffinityRule test invoked.");
        int previousLength = manifest.getVirtualMachineDescriptionSection().getAffinitySection().getRuleArray().length;
        manifest.getVirtualMachineDescriptionSection().addNewAffinityRule("test-affinity-rule-id", AffinityConstraintType.LOW.toString());
        int finalLength = manifest.getVirtualMachineDescriptionSection().getAffinitySection().getRuleArray().length;
        System.out.println("final no. of rules: " + finalLength);


        assertEquals(previousLength + 1, finalLength);
    }

    public void testRemoveAffinityRule() {
        System.out.println("\nremoveAffinityRule test invoked.");
        int previousLength = manifest.getVirtualMachineDescriptionSection().getAffinitySection().getRuleArray().length;
        manifest.getVirtualMachineDescriptionSection().addNewAffinityRule("test-affinity-rule-id-1", AffinityConstraintType.LOW.toString());
        manifest.getVirtualMachineDescriptionSection().removeAffinityRule(1);
        int finalLength = manifest.getVirtualMachineDescriptionSection().getAffinitySection().getRuleArray().length;
        System.out.println("final no. of rules: " + finalLength);
        assertEquals(previousLength, finalLength);
    }

    public void testAffinityRuleValues() {
        AffinityRule rule = manifest.getVirtualMachineDescriptionSection().addNewAffinityRule("xxx", AffinityConstraintType.LOW.toString());

        assertEquals(AffinityConstraintType.LOW.toString(), rule.getAffinityConstraints());

        rule.setAffinityConstraints(AffinityConstraintType.HIGH.toString());
        assertEquals("High", rule.getAffinityConstraints());
    }

}
