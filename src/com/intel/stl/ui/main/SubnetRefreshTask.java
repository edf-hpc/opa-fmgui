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
 *  File Name: SubnetRefreshTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/11 18:52:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/22 01:16:19  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/16 13:18:05  fernande
 *  Archive Log:    Changes to AbstractTask to support an onFinally method that is guaranteed to be called no matter what happens in the onTaskSuccess and onTaskFailure implementations for a task.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/13 21:07:16  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/09 12:59:15  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import static com.intel.stl.ui.main.FabricController.PROGRESS_NOTE_PROPERTY;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.ProgressObserver;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.framework.AbstractTask;
import com.intel.stl.ui.monitor.tree.FVTreeManager;

public class SubnetRefreshTask extends AbstractTask<FabricModel, Void, String> {

    private static Logger log = LoggerFactory.getLogger(SubnetRefreshTask.class);

    private final List<IPageController> pages;

    private final FVTreeManager builder;

    public SubnetRefreshTask(FabricModel model, FVTreeManager builder,
            List<IPageController> pages) {
        super(model);
        this.pages = pages;
        this.builder = builder;
    }

    @Override
    public Void processInBackground(Context context) throws Exception {
        log.info("Refresh subnet '" + model.getCurrentSubnet() + "'");

        builder.setDirty();
        for (int i = 0; i < pages.size(); i++) {
            IPageController page = pages.get(i);
            setProgressNote(UILabels.STL10111_REFRESHING_PAGE
                    .getDescription(page.getName()));
            int estimatedWork = page.getRefreshWeight().getWeight();
            ProgressObserver observer =
                    new ProgressObserver(this, estimatedWork);
            page.onRefresh(observer);
            observer.onFinish();
        }

        return null;
    }

    @Override
    public void onTaskSuccess(Void result) {
        getController().notifyModelChanged();
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        model.setErrorMessage(StringUtils.getErrorMessage(caught));
        getController().notifyModelUpdateFailed(caught);
    }

    @Override
    public void onFinally() {
    }

    @Override
    public void processIntermediateResults(List<String> intermediateResults) {
    }

    protected void setProgressNote(String note) {
        firePropertyChange(PROGRESS_NOTE_PROPERTY, null, note);
    }
}
