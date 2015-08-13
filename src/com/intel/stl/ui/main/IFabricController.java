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
package com.intel.stl.ui.main;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.framework.ITask;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.publisher.TaskScheduler;

/**
 * @author jijunwan
 * 
 */
public interface IFabricController extends ISubnetEventListener {
    void init();

    void doShowInitScreen(Rectangle bounds, boolean maximized);

    void doShowMessageAndExit(String message, String title);

    void doShowErrors(List<Throwable> errors);

    void doShowContent();

    void doClose();

    void reset();

    SubnetDescription getCurrentSubnet();

    void resetSubnet(SubnetDescription subnet);

    Context getCurrentContext();

    void selectSubnet(String subnetName);

    void resetConnectMenu();

    void initializeContext(Context context);

    void resetContext(Context newContext);

    void cleanup();

    IFabricView getView();

    TaskScheduler getTaskScheduler();

    void showSetupWizard(String subnetName);

    void addPendingTask(ITask pendingTask);

    void bringToFront();

    Rectangle getBounds();

    boolean isMaximized();

    void applyHelpAction(ActionEvent event);
}
