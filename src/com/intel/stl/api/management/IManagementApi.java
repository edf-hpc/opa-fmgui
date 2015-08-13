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
 *  File Name: IManagementApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:22:09  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/28 21:55:00  jijunwan
 *  Archive Log:    improved LoginAssistant to support setting owner
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/25 19:10:09  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 22:02:28  jijunwan
 *  Archive Log:    Added #getType to LongNode
 *  Archive Log:    Added devicegroup to management api
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:41:43  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:37  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

import com.intel.stl.api.ILoginAssistant;
import com.intel.stl.api.management.applications.IApplicationManangement;
import com.intel.stl.api.management.devicegroups.IDeviceGroupManagement;
import com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement;

public interface IManagementApi extends IApplicationManangement,
        IDeviceGroupManagement, IVirtualFabricManagement {
    void setLoginAssistant(ILoginAssistant loginAssistant);

    ILoginAssistant getLoginAssistant();

    /**
     * 
     * <i>Description:</i> reload file remotely from FM. We shall do this after
     * FM restart. Since we can not directly know whether FM restated, we can do
     * this every time after we established a FE connection
     * 
     * @throws Exception
     */
    void refresh() throws Exception;

    /**
     * 
     * <i>Description:</i> deploy local opafm.xml to SMs
     * 
     * @param restart
     *            indicate whether we restart FM after copy file to SMs
     */
    void deploy(boolean restart) throws Exception;
}
