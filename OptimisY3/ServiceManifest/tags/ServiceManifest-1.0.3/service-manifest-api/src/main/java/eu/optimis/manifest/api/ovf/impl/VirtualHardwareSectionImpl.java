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

package eu.optimis.manifest.api.ovf.impl;

import eu.optimis.manifest.api.impl.AbstractManifestElement;
import eu.optimis.manifest.api.ovf.ip.VirtualHardwareSection;
import eu.optimis.manifest.api.utils.XmlSimpleTypeConverter;
import org.dmtf.schemas.ovf.envelope.x1.XmlBeanRASDType;
import org.dmtf.schemas.ovf.envelope.x1.XmlBeanVirtualHardwareSectionType;

import java.util.Vector;

/**
 * @author arumpl
 */
class VirtualHardwareSectionImpl extends AbstractManifestElement<XmlBeanVirtualHardwareSectionType>
        implements VirtualHardwareSection, eu.optimis.manifest.api.ovf.sp.VirtualHardwareSection {
    public VirtualHardwareSectionImpl(XmlBeanVirtualHardwareSectionType base) {
        super(base);
    }

    @Override
    public String getInfo() {
        return delegate.getInfo().getStringValue();
    }

    @Override
    public SystemImpl getSystem() {
        if (delegate.isSetSystem())
            return new SystemImpl(delegate.getSystem());
        return null;
    }

    @Override
    public ItemImpl[] getItemArray() {
        Vector<ItemImpl> vector = new Vector<ItemImpl>();
        for (XmlBeanRASDType type : delegate.getItemArray()) {
            vector.add(new ItemImpl(type));
        }
        return vector.toArray(new ItemImpl[vector.size()]);
    }

    @Override
    public ItemImpl getItemArray(int i) {
        return new ItemImpl(delegate.getItemArray(i));
    }

    @Override
    public String getVirtualHardwareFamily() {
        return delegate.getSystem().getVirtualSystemType().getStringValue();
    }

    @Override
    public void setVirtualHardwareFamily(String virtualHardwareFamily) {
        delegate.getSystem().setVirtualSystemType(XmlSimpleTypeConverter.toCimString(virtualHardwareFamily));
    }

    @Override
    public int getNumberOfVirtualCPUs() {
        return delegate.getItemArray(0).getVirtualQuantity().getBigIntegerValue().intValue();
    }

    @Override
    public void setNumberOfVirtualCPUs(int numberOfVirtualCPUs) {
        delegate.getItemArray(0).setVirtualQuantity(XmlSimpleTypeConverter.toCimUnsignedLong(numberOfVirtualCPUs));
    }

    @Override
    public int getMemorySize() {
        return delegate.getItemArray(1).getVirtualQuantity().getBigIntegerValue().intValue();
    }

    @Override
    public void setMemorySize(int memorySize) {
        delegate.getItemArray(1).setVirtualQuantity(XmlSimpleTypeConverter.toCimUnsignedLong(memorySize));
    }
}
