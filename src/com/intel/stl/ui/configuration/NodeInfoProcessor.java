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
 *  File Name: NodeInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:53:50  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/02 13:32:57  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/04 14:24:55  fernande
 *  Archive Log:    Changed Active property to be set base on actual state of the node.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:05:30  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0322_PORT_LINK_ACTIVE;
import static com.intel.stl.ui.common.STLConstants.K0524_INACTIVE;
import static com.intel.stl.ui.model.DeviceProperty.BASE_VERSION;
import static com.intel.stl.ui.model.DeviceProperty.DEVICE_ID;
import static com.intel.stl.ui.model.DeviceProperty.NODE_GUID;
import static com.intel.stl.ui.model.DeviceProperty.NODE_LID;
import static com.intel.stl.ui.model.DeviceProperty.NODE_NAME;
import static com.intel.stl.ui.model.DeviceProperty.NODE_STATE;
import static com.intel.stl.ui.model.DeviceProperty.NODE_TYPE;
import static com.intel.stl.ui.model.DeviceProperty.PARTITION_CAP;
import static com.intel.stl.ui.model.DeviceProperty.REVISION;
import static com.intel.stl.ui.model.DeviceProperty.SMA_VERSION;
import static com.intel.stl.ui.model.DeviceProperty.SYSTEM_IMAGE_GUID;
import static com.intel.stl.ui.model.DeviceProperty.VENDOR_ID;

import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.NodeTypeViz;

public class NodeInfoProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        NodeRecordBean nodeBean = context.getNode();
        NodeInfoBean nodeInfo = context.getNodeInfo();
        // TODO: need to come from API
        String nodeActive = K0322_PORT_LINK_ACTIVE.getValue();
        if (!nodeBean.isActive()) {
            nodeActive = K0524_INACTIVE.getValue();
        }
        addProperty(category, NODE_STATE, nodeActive);
        if (nodeInfo == null) {
            getEmptyNodeDeviceInfo(category);
            return;
        }
        addProperty(category, NODE_LID, hex(nodeBean.getLid()));
        addProperty(category, NODE_NAME, nodeBean.getNodeDesc());
        if (nodeInfo != null) {
            NodeTypeViz nodeTypeViz =
                    NodeTypeViz.getNodeTypeViz(nodeInfo.getNodeTypeEnum());
            if (nodeTypeViz != null) {
                addProperty(category, NODE_TYPE, nodeTypeViz.getName());
            }
            addProperty(category, NODE_GUID, hex(nodeInfo.getNodeGUID()));
            addProperty(category, SYSTEM_IMAGE_GUID,
                    hex(nodeInfo.getSysImageGUID()));
            addProperty(category, PARTITION_CAP,
                    dec(nodeInfo.getPartitionCap()));
            addProperty(category, BASE_VERSION, dec(nodeInfo.getBaseVersion()));
            addProperty(category, SMA_VERSION, dec(nodeInfo.getClassVersion()));
            addProperty(category, DEVICE_ID, hex(nodeInfo.getDeviceID()));
            addProperty(category, VENDOR_ID, hex(nodeInfo.getVendorID()));
            addProperty(category, REVISION, hex(nodeInfo.getRevision()));
        }

    }

    private void getEmptyNodeDeviceInfo(DevicePropertyCategory category) {
        addProperty(category, NODE_LID, "");
        addProperty(category, NODE_TYPE, "");
        addProperty(category, NODE_NAME, "");
        addProperty(category, NODE_GUID, "");
        addProperty(category, SYSTEM_IMAGE_GUID, "");
        addProperty(category, PARTITION_CAP, "");
        addProperty(category, BASE_VERSION, "");
        addProperty(category, SMA_VERSION, "");
        addProperty(category, DEVICE_ID, "");
        addProperty(category, VENDOR_ID, "");
        addProperty(category, REVISION, "");
    }

}
