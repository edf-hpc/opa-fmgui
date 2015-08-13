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
 *  File Name: VirtualLaneProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:43  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/04 21:44:14  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/23 20:15:17  jijunwan
 *  Archive Log:    PR 126673 - "Unsupported VL Cap(0X08)" for all Switch ports other than Switch port 0
 *  Archive Log:    STL is using VL Cap as a number rather than an enum. Removed VL Cap related IB enum, and represent Cap as as a byte number
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 19:26:45  jijunwan
 *  Archive Log:    PR 126421 - VL Flow Control Disable Mask not implemented on WFR HFI
 *  Archive Log:    display n/a for HFI and Switch port 0
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:06:15  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0387_UNKNOWN;
import static com.intel.stl.ui.model.DeviceProperty.FLOW_CONTROL_DISABLE_MASK;
import static com.intel.stl.ui.model.DeviceProperty.HOQLIFE_LABEL;
import static com.intel.stl.ui.model.DeviceProperty.OPERATIONAL_VLS;
import static com.intel.stl.ui.model.DeviceProperty.VL_ARBITR_HIGH_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_ARBITR_LOW_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_HIGH_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.VL_PREEMPTING_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.VL_PREEMPT_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_STALL_COUNT;

import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.api.subnet.VirtualLaneBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class VirtualLaneProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        NodeInfoBean nodeInfo = context.getNodeInfo();
        PortInfoBean portInfo = context.getPortInfo();

        if (!(nodeInfo != null && portInfo != null)) {
            getEmptyVirtualLane(category);
            return;
        }
        String unknown = K0387_UNKNOWN.getValue();
        String na = STLConstants.K0383_NA.getValue();
        VirtualLaneBean vlBean = portInfo.getVl();
        String value = unknown;
        byte cap = portInfo.getOperationalVL();
        value = hex(cap);
        addProperty(category, OPERATIONAL_VLS, value);
        cap = vlBean.getCap();
        value = hex(cap);
        addProperty(category, VL_CAP, value);
        addProperty(category, VL_HIGH_LIMIT, dec(vlBean.getHighLimit()));
        addProperty(category, VL_PREEMPT_CAP, dec(vlBean.getPreemptCap()));
        addProperty(category, VL_PREEMPTING_LIMIT,
                dec(vlBean.getPreemptingLimit()));
        if (nodeInfo.getNodeTypeEnum() == NodeType.SWITCH
                && context.getPort().getPortNum() > 0) {
            addProperty(category, FLOW_CONTROL_DISABLE_MASK,
                    hex(portInfo.getFlowControlMask()));
        } else {
            addProperty(category, FLOW_CONTROL_DISABLE_MASK, na);
        }
        addProperty(category, VL_ARBITR_HIGH_CAP,
                Short.toString(vlBean.getArbitrationHighCap()));
        addProperty(category, VL_ARBITR_LOW_CAP,
                Short.toString(vlBean.getArbitrationLowCap()));
        value = na;
        if (nodeInfo.getNodeTypeEnum() == NodeType.SWITCH) {
            value = Byte.toString(portInfo.getVlStallCount()[0]);
        }
        addProperty(category, VL_STALL_COUNT, value);
        value = na;
        if (nodeInfo.getNodeTypeEnum() != NodeType.HFI) {
            value = Byte.toString(portInfo.getHoqLife()[0]);
        }
        addProperty(category, HOQLIFE_LABEL, value);
    }

    private void getEmptyVirtualLane(DevicePropertyCategory category) {
        addProperty(category, OPERATIONAL_VLS, "");
        addProperty(category, VL_CAP, "");
        addProperty(category, VL_HIGH_LIMIT, "");
        addProperty(category, VL_PREEMPT_CAP, "");
        addProperty(category, VL_PREEMPTING_LIMIT, "");
        addProperty(category, FLOW_CONTROL_DISABLE_MASK, "");
        addProperty(category, VL_ARBITR_HIGH_CAP, "");
        addProperty(category, VL_ARBITR_LOW_CAP, "");
        addProperty(category, VL_STALL_COUNT, "");
        addProperty(category, HOQLIFE_LABEL, "");
    }
}
