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
 *  File Name: (LinkDownReasonViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/08/17 18:53:46  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/02 16:27:17  jijunwan
 *  Archive Log:    matched FM 320 (stl_sm.h v1.26) - added linkInitReason
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 18:02:04  jijunwan
 *  Archive Log:    PR 126417 - Explore expansion and refinement of LinkDownReason codes
 *  Archive Log:    updated to the latest version stl_sm.h v1.115
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

import static com.intel.stl.ui.common.STLConstants.K0383_NA;
import static com.intel.stl.ui.common.STLConstants.K1781_LINK_INIT_LINKUP;
import static com.intel.stl.ui.common.STLConstants.K1782_LINK_INIT_FLAPPING;
import static com.intel.stl.ui.common.STLConstants.K1788_LINK_INIT_OUTSIDE_POLICY;
import static com.intel.stl.ui.common.STLConstants.K1789_LINK_INIT_QUARANTINED;
import static com.intel.stl.ui.common.STLConstants.K1790_LINK_INIT_INSUFIC_CAPABILITY;

import java.util.HashMap;
import java.util.Map;

import com.intel.stl.api.configuration.LinkInitReason;

public enum LinkInitReasonViz {
    NONE(LinkInitReason.NONE, K0383_NA.getValue()),
    LINKUP(LinkInitReason.LINKUP, K1781_LINK_INIT_LINKUP.getValue()),
    FLAPPING(LinkInitReason.FLAPPING, K1782_LINK_INIT_FLAPPING.getValue()),
    OUTSIDE_POLICY(LinkInitReason.OUTSIDE_POLICY,
            K1788_LINK_INIT_OUTSIDE_POLICY.getValue()),
    QUARANTINED(LinkInitReason.QUARANTINED, K1789_LINK_INIT_QUARANTINED
            .getValue()),
    INSUFIC_CAPABILITY(LinkInitReason.INSUFIC_CAPABILITY,
            K1790_LINK_INIT_INSUFIC_CAPABILITY.getValue());

    private static final Map<Byte, String> linkInitReasonMap =
            new HashMap<Byte, String>();
    static {
        for (LinkInitReasonViz lirv : LinkInitReasonViz.values()) {
            linkInitReasonMap.put(lirv.reason.getCode(), lirv.value);
        }
    };

    private final LinkInitReason reason;

    private final String value;

    private LinkInitReasonViz(LinkInitReason reason, String value) {
        this.reason = reason;
        this.value = value;
    }

    public LinkInitReason getReason() {
        return reason;
    }

    public String getValue() {
        return value;
    }

    public static String getLinkInitReasonStr(byte reason) {
        return linkInitReasonMap.get(reason);
    }
}
