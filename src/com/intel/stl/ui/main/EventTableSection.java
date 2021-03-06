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
 *  File Name: EventTableSection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.10  2015/08/17 18:53:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.9  2015/06/10 21:07:17  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs
 *  Archive Log: - manual correction on files that our tool cannot  identify
 *  Archive Log:
 *
 *  Overview: Table section of Rick's Test page
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.ui.main;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.EventTableController;
import com.intel.stl.ui.common.EventTableModel;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.view.EventTableView;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.framework.IAppEvent;

public class EventTableSection extends
        BaseSectionController<ISectionListener, JSectionView<ISectionListener>> {
    private final EventTableController tableController;

    public EventTableSection(EventTableModel tableModel,
            JSectionView<ISectionListener> view, EventTableView tableView,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        tableController = new EventTableController(tableModel, tableView);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.ISection#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return null;
    }

    /**
     * 
     * Description: updates the table on the card
     * 
     * @param event
     *            - event message
     */
    public void updateTable(EventDescription event) {
        tableController.addEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISection#clear()
     */
    @Override
    public void clear() {
        tableController.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseSectionController#getSectionListener()
     */
    @Override
    protected ISectionListener getSectionListener() {
        return this;
    }

}
