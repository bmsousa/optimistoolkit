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

package eu.optimis.manifest.api.impl;

import java.util.ArrayList;
import java.util.List;

import eu.optimis.manifest.api.ip.AllocationPattern;
import eu.optimis.types.xmlbeans.servicemanifest.infrastructure.XmlBeanAllocationPatternType;

/**
 * @author arumpl
 */
public class AllocationPatternImpl extends AbstractManifestElement<XmlBeanAllocationPatternType>
    implements AllocationPattern
{
    /**
     * 
     * @param base
     */
    public AllocationPatternImpl( XmlBeanAllocationPatternType base )
    {
        super( base );
    }

    @Override
    public PhysicalHostImpl[] getPhysicalHostArray()
    {
        List<PhysicalHostImpl> list = new ArrayList<PhysicalHostImpl>();
        for ( XmlBeanAllocationPatternType.PhysicalHost type : delegate.getPhysicalHostArray() )
        {
            list.add( new PhysicalHostImpl( type ) );
        }
        return list.toArray( new PhysicalHostImpl[list.size()] );
    }

    @Override
    public PhysicalHostImpl getPhysicalHostArray( int i )
    {
        return new PhysicalHostImpl( delegate.getPhysicalHostArray( i ) );
    }

    @Override
    public PhysicalHostImpl addNewPhysicalHost( String hostName )
    {
        PhysicalHostImpl host = new PhysicalHostImpl( delegate.addNewPhysicalHost() );
        host.setHostName( hostName );
        return host;
    }

    @Override
    public void removePhysicalHost( int i )
    {
        delegate.removePhysicalHost( i );
    }

    @Override
    public String getComponentId()
    {
        return delegate.getComponentId();
    }

    @Override
    public void setComponentId( String componentId )
    {
        delegate.setComponentId( componentId );
    }
}
