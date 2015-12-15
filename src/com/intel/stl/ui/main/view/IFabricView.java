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
 *  File Name: IFabricView.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.35  2015/08/17 18:54:02  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.34  2015/08/10 17:30:07  robertja
 *  Archive Log: PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log: Revision 1.33  2015/08/05 02:47:03  jijunwan
 *  Archive Log: PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log: - introduced UndoHandler to manage undo/redo
 *  Archive Log: - added undo/redo to main frame
 *  Archive Log: - improved FabricController to support undoHandler and undo action on page selection
 *  Archive Log: - improved FabricController to support the new page name based IPageListener
 *  Archive Log:
 *  Archive Log: Revision 1.32  2015/06/10 19:58:57  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jijunwan
 * 
 ******************************************************************************/
package com.intel.stl.ui.main.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.Action;

import com.intel.stl.ui.common.EventTableController;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.view.EventSummaryBarPanel;

/**
 * @author jijunwan
 * 
 */
public interface IFabricView {
    void setSubnetName(String subnetName);

    String getSubnetName();

    void showInitScreen(Rectangle bounds, boolean maximized);

    void setTitle(String title);

    void showMessageAndExit(String message, String title);

    void showMessage(String message, String title);

    void displayErrorMessage(String windowTitle, Exception exception);

    void showErrors(List<Throwable> errors);

    void showContent(List<IPageController> pages);

    void close();

    void resetConnectMenu();

    void setCurrentTab(IPageController page);

    void setReady(boolean b);

    boolean isReady();

    void showProgress(String label, boolean b);

    void setProgress(int progress);

    void setProgressNote(String note);

    void toggleEventSummaryTable();

    void showEventSummaryTable();

    void hideEventSummaryTable();

    /**
     * 
     * Description: this method should be unnecessary if we have a good MVC
     * design pattern. On view we should have no any cleanup to do since it
     * suppose to be a dummy renderer. We are adding this method just in case.
     * For now, this is mainly for stopping timer used for testing purpose.
     * 
     */
    void cleanup();

    void clear();

    void bringToFront();

    Rectangle getFrameBounds();

    boolean isFrameMaximized();

    void setWizardAction(ActionListener listener);

    void setLoggingAction(ActionListener listener);
    
    void setAboutDialogAction(ActionListener listener);
    
    void setEmailSettingsAction(ActionListener listener);

    void setRandomAction(ActionListener listener);

    void setWindowAction(WindowListener listener);

    void setRefreshAction(ActionListener listener);

    void setRefreshRunning(boolean isRunning);

    void setUndoAction(Action action);

    void setRedoAction(Action action);

    void setPageListener(IPageListener listener);

    EventSummaryBarPanel getEventSummaryBarPanel();

    EventTableController getEventTableController();

    Component getView();

    Dimension getScreenSize();

    void setScreenSize(Dimension dimension);

    Point getScreenPosition();

}
