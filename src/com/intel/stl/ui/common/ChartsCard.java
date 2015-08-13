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
 *  File Name: ChartsCard.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:27:03  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/18 14:59:33  jijunwan
 *  Archive Log:    Added jumping to destination support to TopN chart via popup menu
 *  Archive Log:    Added label highlight for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/15 15:24:31  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 18:38:42  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/21 17:03:06  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/16 20:54:19  jijunwan
 *  Archive Log:    fixed port link
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/11 19:23:23  fernande
 *  Archive Log:    Adding event bus and linking from UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/09 14:17:14  jijunwan
 *  Archive Log:    moved JFreeChart to view side, controller side only take care dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 19:25:36  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/30 14:56:46  rjtierne
 *  Archive Log:    Changes to reflect renamed JCard and ICard
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:20:43  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:50:38  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/08 17:32:56  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/03 20:52:18  jijunwan
 *  Archive Log:    on going work on "Home" page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.List;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.view.ChartsView;
import com.intel.stl.ui.common.view.IChartsCardListener;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.PortSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.model.DatasetDescription;
import com.intel.stl.ui.model.PortEntry;

public class ChartsCard extends
        BaseCardController<IChartsCardListener, ChartsView> implements
        IChartsCardListener {
    private String currentChart;

    public ChartsCard(ChartsView view, MBassador<IAppEvent> eventBus,
            List<DatasetDescription> datasets) {
        super(view, eventBus);
        view.setDatasets(datasets);
        if (datasets != null && !datasets.isEmpty()) {
            currentChart = datasets.get(0).getName();
        }
        view.setChart(currentChart);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IChartsCardListener#onSelectChart(int)
     */
    @Override
    public void onSelectChart(String name) {
        currentChart = name;
        view.setChart(currentChart);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public IChartsCardListener getCardListener() {
        return this;
    }

    @Override
    public void jumpTo(Object content, JumpDestination destination) {
        if (content instanceof PortEntry) {
            PortEntry pe = (PortEntry) content;
            PortSelectedEvent event =
                    new PortSelectedEvent(pe.getNodeLid(), pe.getPortNum(),
                            this, destination);
            eventBus.publish(event);
        }
    }
}
