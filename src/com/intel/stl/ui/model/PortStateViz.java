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
 *  File Name: PortStateViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/29 13:48:52  fernande
 *  Archive Log:    Removed repetitive conversion from FE values to API enums
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:52:34  fernande
 *  Archive Log:    Refactoring PropertyStrings into individual enums
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0368_NO_STATE_CHANGE;
import static com.intel.stl.ui.common.STLConstants.K0369_DOWN;
import static com.intel.stl.ui.common.STLConstants.K0370_INIT;
import static com.intel.stl.ui.common.STLConstants.K0371_ARMED;
import static com.intel.stl.ui.common.STLConstants.K0372_ACTIVE;

import java.util.EnumMap;

import com.intel.stl.api.configuration.PortState;

public enum PortStateViz {
    NO_ST_CHANGE(PortState.NO_ST_CHANGE, K0368_NO_STATE_CHANGE.getValue()),
    DOWN(PortState.DOWN, K0369_DOWN.getValue()),
    INITIALIZE(PortState.INITIALIZE, K0370_INIT.getValue()),
    ARMED(PortState.ARMED, K0371_ARMED.getValue()),
    ACTIVE(PortState.ACTIVE, K0372_ACTIVE.getValue());

    private final static EnumMap<PortState, String> portStateMap =
            new EnumMap<PortState, String>(PortState.class);
    static {
        for (PortStateViz psv : PortStateViz.values()) {
            portStateMap.put(psv.state, psv.value);
        }
    };

    private final PortState state;

    private final String value;

    private PortStateViz(PortState state, String value) {
        this.state = state;
        this.value = value;
    }

    public PortState getPortState() {
        return state;
    }

    public String getValue() {
        return value;
    }

    public static PortStateViz getPortStateViz(byte state) {
        for (PortStateViz psv : PortStateViz.values()) {
            if (psv.state.getId() == state) {
                return psv;
            }
        }
        return null;
    }

    public static String getPortStateStr(PortState state) {
        return portStateMap.get(state);
    }
}
