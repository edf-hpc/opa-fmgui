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
 *  Functional Group: FabricViewer
 *
 *  File Name: NodeStatesCard.java
 *
 *  Archive Source: 
 *
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.util.EnumMap;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.IChartStyleListener;
import com.intel.stl.ui.main.view.StatusView;
import com.intel.stl.ui.model.ChartStyle;

/**
 * @author jijunwan
 * 
 */
public class StatusCard extends
        BaseCardController<IChartStyleListener, StatusView> implements
        IChartStyleListener {
    private final NodeStatusController swStatusCtl;

    private final NodeStatusController fiStatusCtl;

    public StatusCard(StatusView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        swStatusCtl = new NodeStatusController(view.getSwPanel());
        fiStatusCtl = new NodeStatusController(view.getFiPanel());

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getStatus(), helpAction.getHelpSet());
    }

    /**
     * @param swStates
     * @param totalSWs
     */
    public void updateSwStates(EnumMap<NoticeSeverity, Integer> states,
            final int total) {
        swStatusCtl.updateStates(states, total);
    }

    public void updateFiStates(EnumMap<NoticeSeverity, Integer> states,
            final int total) {
        fiStatusCtl.updateStates(states, total);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.INodeStatesListener#onStyleChange(com.intel
     * .stl.ui.main.view.NodeStatesView.Style)
     */
    @Override
    public void onStyleChange(ChartStyle style) {
        ChartStyle newStyle = null;
        if (style == ChartStyle.BAR) {
            newStyle = ChartStyle.PIE;
        } else if (style == ChartStyle.PIE) {
            newStyle = ChartStyle.BAR;
        } else {
            throw new IllegalArgumentException("Unknown chart style: " + style);
        }
        view.setStyle(newStyle);
        swStatusCtl.setStyle(newStyle);
        fiStatusCtl.setStyle(newStyle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public IChartStyleListener getCardListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#clear()
     */
    @Override
    public void clear() {
        swStatusCtl.clear();
        fiStatusCtl.clear();
    }

}
