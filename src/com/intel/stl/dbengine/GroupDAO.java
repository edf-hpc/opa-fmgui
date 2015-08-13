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
 *  File Name: SubnetDAO.java
 *
 *  Archive Source: $Source$
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.dbengine;

import java.util.List;

import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.GroupListBean;
import com.intel.stl.api.performance.PerformanceDataNotFoundException;
import com.intel.stl.api.performance.PortConfigBean;
import com.intel.stl.datamanager.GroupConfigId;
import com.intel.stl.datamanager.GroupConfigRecord;
import com.intel.stl.datamanager.SubnetRecord;

public interface GroupDAO {

    void saveGroupList(SubnetRecord subnet, List<GroupListBean> groupList);

    GroupConfigRecord saveGroupConfig(SubnetRecord subnet, String groupName,
            List<PortConfigBean> ports);

    List<PortConfigBean> getGroupConfig(GroupConfigId configId)
            throws PerformanceDataNotFoundException;

    List<PortConfigBean> getPortConfig(SubnetRecord subnet)
            throws PerformanceDataNotFoundException;

    void saveGroupInfos(SubnetRecord subnet, List<GroupInfoBean> groupInfoBeans);

    List<GroupInfoBean> getGroupInfoList(SubnetRecord subnet, String groupName,
            long startTime, long stopTime)
            throws PerformanceDataNotFoundException;

}
