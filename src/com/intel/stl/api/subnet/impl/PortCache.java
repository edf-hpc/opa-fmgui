/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: PortCache.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/08/17 18:48:53  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/11 17:37:19  jijunwan
 *  Archive Log:    PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log:    - improved to get distribution data with argument "refresh". When it's true, calculate distribution rather than get it from cache
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/04 21:37:54  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/05 15:35:53  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 20:27:41  jijunwan
 *  Archive Log:    1) changed specific xxxxNotFoundExceptions to SubnetDataNotFoundException or PerformanceDataNotFoundException
 *  Archive Log:    2) added throws SubnetException to ISubnetApi
 *  Archive Log:    3) added throws PerformanceException to IPerformanceApi
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/11 13:06:59  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/07 16:50:39  fernande
 *  Archive Log:    Changing WeakReferences to SoftReference and List to Map for NodeCache and PortCache
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/03 21:35:54  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.subnet.impl;

import java.util.EnumMap;
import java.util.List;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;

public interface PortCache {

    /**
     * 
     * <i>Description:</i> all active ports in a subnet
     * 
     * @return
     * @throws SubnetDataNotFoundException
     */
    List<PortRecordBean> getPorts() throws SubnetDataNotFoundException;

    PortRecordBean getPortByPortNum(int lid, short portNum)
            throws SubnetDataNotFoundException;

    PortRecordBean getPortByLocalPortNum(int lid, short localPortNum)
            throws SubnetDataNotFoundException;

    boolean hasPort(int lid, short portNum);

    boolean hasLocalPort(int lid, short localPortNum);

    /**
     * 
     * <i>Description:</i> distribution of active ports
     * 
     * @param countInternalMgrPort
     * @param refresh
     * @return
     * @throws SubnetDataNotFoundException
     */
    EnumMap<NodeType, Long> getPortsTypeDist(boolean countInternalMgrPort,
            boolean refresh) throws SubnetDataNotFoundException;

    long getSubnetPrefix();

}
