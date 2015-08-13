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
 *  File Name: WorstNodesCard.java
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

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.NodeSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.IWorstNodesListener;
import com.intel.stl.ui.main.view.WorstNodesView;
import com.intel.stl.ui.model.NodeScore;

/**
 * @author jijunwan
 * 
 */
public class WorstNodesCard extends
        BaseCardController<IWorstNodesListener, WorstNodesView> implements
        IWorstNodesListener {
    public WorstNodesCard(WorstNodesView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getWorstNodes(), helpAction.getHelpSet());
    }

    /**
     * @param nodes
     */
    public void updateWorstNodes(final NodeScore[] nodes) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.updateNodes(nodes);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onNodeSelected(int,
     * com.intel.stl.api.subnet.NodeType)
     */
    @Override
    public void jumpTo(int lid, NodeType type, JumpDestination descination) {
        NodeSelectedEvent event =
                new NodeSelectedEvent(lid, type, this, descination);
        eventBus.publish(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public IWorstNodesListener getCardListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#clear()
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.clear();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onMore()
     */
    @Override
    public void onMore() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onSizeChange(int)
     */
    @Override
    public void onSizeChanged(int size) {
        view.setSize(size);
    }

}
