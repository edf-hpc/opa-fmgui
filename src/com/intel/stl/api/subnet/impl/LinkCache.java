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
 *  File Name: LinkCache.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:22:01  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/04 21:37:54  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/05 15:35:53  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/12 20:27:41  jijunwan
 *  Archive Log:    1) changed specific xxxxNotFoundExceptions to SubnetDataNotFoundException or PerformanceDataNotFoundException
 *  Archive Log:    2) added throws SubnetException to ISubnetApi
 *  Archive Log:    3) added throws PerformanceException to IPerformanceApi
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/11 13:06:59  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
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

import java.util.List;

import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;

public interface LinkCache {

    /**
     * 
     * <i>Description:</i> get all links in a subnet. It can include inactive
     * links (ports) if <code>includeInactive</code> is true
     * 
     * @param includeInactive
     * @return
     * @throws SubnetDataNotFoundException
     */
    List<LinkRecordBean> getLinks(boolean includeInactive)
            throws SubnetDataNotFoundException;

    /**
     * 
     * <i>Description:</i> get link by source node lid and port number. It may
     * return inactive link (port)
     * 
     * @param lid
     * @param portNum
     * @return
     * @throws SubnetDataNotFoundException
     */
    LinkRecordBean getLinkBySource(int lid, short portNum)
            throws SubnetDataNotFoundException;

    /**
     * 
     * <i>Description:</i> get link by destination node lid and port number. It
     * may
     * return inactive link (port)
     * 
     * @param lid
     * @param portNum
     * @return
     * @throws SubnetDataNotFoundException
     */
    LinkRecordBean getLinkByDestination(int lid, short portNum)
            throws SubnetDataNotFoundException;

}
