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
package eu.optimis.manifest.api.ovf.impl;

import org.dmtf.schemas.ovf.envelope.x1.XmlBeanOperatingSystemSectionType;

import eu.optimis.manifest.api.impl.AbstractManifestElement;
import eu.optimis.manifest.api.ovf.sp.OperatingSystem;
import eu.optimis.manifest.api.utils.XmlSimpleTypeConverter;

/**
 * @author arumpl
 */
public class OperatingSystemImpl extends AbstractManifestElement<XmlBeanOperatingSystemSectionType>
    implements OperatingSystem
{

    public OperatingSystemImpl( XmlBeanOperatingSystemSectionType base )
    {
        super( base );
    }

    @Override
    public int getId()
    {
        return delegate.getId();
    }

    @Override
    public void setId( int operatingSystemId )
    {
        delegate.setId( operatingSystemId );
    }

    @Override
    public String getDescription()
    {
        if ( delegate.isSetDescription() )
        {
            return delegate.getDescription().getStringValue();
        }
        return null;
    }

    @Override
    public void setDescription( String description )
    {
        delegate.setDescription( XmlSimpleTypeConverter.toMsgType( description ) );
    }

    @Override
    public String getVersion()
    {
        return delegate.getVersion();
    }

    @Override
    public void setVersion( String version )
    {
        delegate.setVersion( version );
    }

}
