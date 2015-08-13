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
 *  File Name: PortLinkModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 18:40:02  jijunwan
 *  Archive Log:    PR 126387 - SCMappingTableRecord PortLinkMode ambiguous
 *  Archive Log:    updated to the latest version stl_sa.h v1.92
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0384_ALL_SUPP;

import java.util.EnumMap;

import com.intel.stl.api.configuration.PortLinkMode;

public enum PortLinkModeViz {
    NOP(PortLinkMode.NOP, "NOP"),
    ETH(PortLinkMode.ETH, "ETH"),
    STL(PortLinkMode.STL, "STL"),
    ALL_SUPPORTED(PortLinkMode.ALL_SUPPORTED, K0384_ALL_SUPP.getValue());

    private final static EnumMap<PortLinkMode, String> portLinkModeMap =
            new EnumMap<PortLinkMode, String>(PortLinkMode.class);
    static {
        for (PortLinkModeViz plmv : PortLinkModeViz.values()) {
            portLinkModeMap.put(plmv.portLinkMode, plmv.value);
        }
    };

    private final PortLinkMode portLinkMode;

    private final String value;

    private PortLinkModeViz(PortLinkMode portLinkMode, String value) {
        this.portLinkMode = portLinkMode;
        this.value = value;
    }

    public static String getPortLinkModeStr(PortLinkMode mode) {
        return portLinkModeMap.get(mode);
    }

    public static String getPortLinkModeStr(byte mode) {
        PortLinkMode[] modes = PortLinkMode.getPortLinkModes(mode);
        String comma = "";
        StringBuffer linkModeStr = new StringBuffer();
        for (int i = 0; i < modes.length; i++) {
            linkModeStr.append(comma);
            linkModeStr.append(portLinkModeMap.get(modes[i]));
            comma = ", ";
        }
        return linkModeStr.toString();
    }
}
