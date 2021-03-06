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
 *  File Name: SMLogController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/11/24 15:35:58  rjtierne
 *  Archive Log:    PR 130965 : ESM Support on Log Viewer
 *  Archive Log:    - Moved HelpAction references to the view since it uses a view component
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/11/18 23:54:33  rjtierne
 *  Archive Log:    PR 130965 - ESM support on Log Viewer
 *  Archive Log:    - Now supports user configured login/logout feature
 *  Archive Log:    - Schedule commands on a SwingWorker to prevent UI lockup
 *  Archive Log:    - Keep track of last SshUserName used when in auto-config
 *  Archive Log:    - Install the new help button in the Help Broker
 *  Archive Log:    - Moved showLoginView() from view
 *  Archive Log:    - Additional refactoring of commanding by scheduling on SwingWorker
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/10/06 15:51:56  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - In method startLog(), when hostType is null or ESM, call onEsmHost()
 *  Archive Log:    - Added onEsmHost() to display a message on the Log Viewer indicating that it isn't supported and grey
 *  Archive Log:    out the log controls
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/09/29 15:31:22  rjtierne
 *  Archive Log:    PR 130332 - windows FM GUI - Admin-Logs - when logging in it displays error message about NULL log
 *  Archive Log:    - Implemented cancelLogin() to call stopLog() in SMLogController to stop the log if the cancel
 *  Archive Log:    button is clicked
 *  Archive Log:    - Clear password on error or when the cancel button is clicked
 *  Archive Log:    - Display error only when no log file can be found
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/09/28 17:54:18  fisherma
 *  Archive Log:    PR 130425 - added cancel button to the Admin tab login page to allow user to cancel out of hung or slow ssh logins.  Cancel action terminates sftp connection and closes remote ssh session. This fix also addresses PR 130386 and 130390.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/09/25 13:56:10  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Implemented ITextMenuListener interface to handle actions brought on by menu selections
 *  Archive Log:    - Facilitate search and filter tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/18 21:37:37  jijunwan
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    - checked in Rick's code that treat search key as literal pattern string to handle special chars
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/17 14:22:41  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *
 *  Overview: Controller for the SM log view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.logs.ILogApi;
import com.intel.stl.api.logs.ILogStateListener;
import com.intel.stl.api.logs.LogConfigType;
import com.intel.stl.api.logs.LogErrorType;
import com.intel.stl.api.logs.LogInitBean;
import com.intel.stl.api.logs.LogMessageType;
import com.intel.stl.api.logs.LogResponse;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.admin.impl.SMLogModel;
import com.intel.stl.ui.admin.view.ILoginListener;
import com.intel.stl.ui.admin.view.logs.LogViewType;
import com.intel.stl.ui.admin.view.logs.SMLogView;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.console.LoginBean;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.LogErrorTypeViz;

public class SMLogController implements ILogController, ILogViewListener,
        ILogStateListener, ILoginListener, ITextMenuListener {

    private final static Logger log = LoggerFactory
            .getLogger(SMLogController.class);

    private final SMLogModel model;

    private final SMLogView view;

    private ILogApi logApi;

    public SubnetDescription subnet;

    public boolean endOfFile = true;

    public boolean initInProgress;

    public boolean reconfiguring;

    private IProgressObserver observer;

    private boolean firstPage;

    private boolean lastPage;

    private SearchTask searchTask;

    private FilterTask filterTask;

    private GetLogTask logTask;

    private boolean dirty;

    private DocumentListener setDirtyListener;

    private String subnetUserName;

    public SMLogController(SMLogModel model, SMLogView view) {
        this.model = model;
        this.view = view;
        this.view.setLoginListener(this);
        this.view.setLogController(this);
        this.view.setLogViewListener(this);
        this.view.setTextMenuListener(this);
    }

    public void setContext(Context context, IProgressObserver observer) {
        this.observer = observer;
        logApi = context.getLogApi();
        subnet = context.getSubnetDescription();
        logApi.setLogStateListener(this);

        if (observer != null) {
            observer.onFinish();
        }
    }

    public void onRefresh() {
        view.onEndOfPage();

        if (observer != null) {
            observer.onFinish();
        }
    }

    public HostInfo getHostInfo() {
        return subnet.getCurrentFE();
    }

    public SMLogView getView() {
        return view;
    }

    public void showView(String name) {
        view.setView(name);
    }

    public void showLoginView() {
        // When the application is first started, the model is updated with
        // whatever host information was stored in the SubnetDescription
        HostInfo hostInfo = getHostInfo();
        LoginBean credentials =
                new LoginBean(hostInfo.getSshUserName(), hostInfo.getHost(),
                        String.valueOf(hostInfo.getSshPortNum()));
        model.setCredentials(credentials);
        model.setLogFilePath(logApi.getLogFilePath());

        // Then the Login View is updated with the credentials in the model
        view.clearLoginData();
        view.updateLoginView(model);
        showView(LogViewType.LOGIN.getValue());
    }

    public LoginBean getCredentials() {
        return view.getCredentials();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onPrevious(long)
     */
    @Override
    public void onPrevious(long numLines) {
        checkLogTask();
        logTask =
                new GetLogTask(logApi, LogMessageType.PREVIOUS_PAGE, numLines);
        logTask.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onNext(long)
     */
    @Override
    public void onNext(long numLines) {
        checkLogTask();
        logTask = new GetLogTask(logApi, LogMessageType.NEXT_PAGE, numLines);
        logTask.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onConfigure()
     */
    @Override
    public void onConfigure() {
        // Shut down logging
        logApi.stopLog();

        // Clear any searches
        onCancelSearch(view.getLastSearchKey());
        view.resetSearchField();
        view.resetLogin();

        view.clearLoginData();
        view.updateLoginView(model);
        showView(LogViewType.LOGIN.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onLastLines(long)
     */
    @Override
    public void onLastLines(long numLines) {
        checkLogTask();
        logTask = new GetLogTask(logApi, LogMessageType.LAST_LINES, numLines);
        logTask.execute();
    }

    public void onNumLines() {
        checkLogTask();
        logTask = new GetLogTask(logApi, LogMessageType.NUM_LINES, 0);
        logTask.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.logs.ILogStateListener#onResponse(com.intel.stl.api
     * .logs.LogResponse)
     */
    @Override
    public synchronized void onResponse(LogResponse response) {
        switch (response.getMsgType()) {
            case NUM_LINES:
                try {
                    long totalLines =
                            Long.parseLong(response.getEntries().get(0));
                    model.setNumLines(totalLines);
                    view.showTotalLines(model);
                    view.showLineRange(model.getStartLine(), model.getEndLine());
                    if (initInProgress) {
                        logApi.scheduleLastLines(view.getNumLinesRequested());
                        initInProgress = false;

                        // Reset the login view
                        view.resetLogin();
                    }

                } catch (NumberFormatException e) {
                    log.error(e.getCause().getMessage());
                }
                break;

            case NEXT_PAGE:
            case PREVIOUS_PAGE:
                model.setLogMsg(response);
                model.setFilteredDoc(response.getEntries());
                view.updateLogView(model);
                view.restoreUserActions(firstPage, lastPage);
                view.setPageRunningVisible(false);
                break;

            case LAST_LINES:
                model.setLogMsg(response);
                model.setFilteredDoc(response.getEntries());
                view.showProgress(false);
                view.showLogView();
                view.updateLogView(model);
                view.restoreUserActions(firstPage, lastPage);
                view.setRefreshRunningVisible(false);
                break;

            case CHECK_FOR_FILE:
                String fileName = response.getEntries().get(0);
                model.setLogFilePath(fileName);
                view.showFileName(fileName);
                break;

            case UNKNOWN:
                log.error(STLConstants.K2152_UNKNOWN_RESPONSE.getValue());
                break;

            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.ILoginListener#credentialsReady()
     */
    @Override
    public void credentialsReady() {

        if (!logApi.isRunning()) {
            // Get the log config type and log file path
            LogConfigType configType = view.getConfigType();
            String logFilePath = view.getLogFilePath();

            // Update the model with the user's login entries
            LoginBean credentials = view.getCredentials();
            model.setCredentials(credentials);
            model.setLogFilePath(logFilePath);

            // Save the last user name used for auto-config
            if (configType == LogConfigType.AUTO_CONFIG) {
                subnetUserName = view.getCredentials().getUserName();
            }

            // Update SshUserName in the subnet
            subnet.getCurrentFE().setSshUserName(
                    view.getCredentials().getUserName());

            // Create a LogInitBean with all the needed information
            String logHost = credentials.getHostName();
            LogInitBean logInitBean =
                    new LogInitBean(subnet, configType, logHost, logFilePath,
                            false, view.getCredentials().getUserName());

            initInProgress = true;
            logApi.startLog(logInitBean, view.getCredentials().getPassword());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.logs.ILogStateListener#onError(com.intel.stl.api.logs
     * .LogErrorType, java.lang.Object[])
     */
    @Override
    public void onError(LogErrorType errorCode, Object... data) {
        int code = errorCode.getId();
        view.showProgress(false);
        view.setPageRunningVisible(false);
        view.setRefreshRunningVisible(false);
        view.restoreUserActions(firstPage, lastPage);
        view.clearLoginData();
        view.showError(LogErrorTypeViz.values()[code].getLabel()
                .getDescription(data));
        logApi.stopLog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogStateListener#onReady()
     */
    @Override
    public void onReady() {
        if (initInProgress) {
            logApi.scheduleLastLines(view.getNumLinesRequested());
            view.setNumLineIcon(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogViewListener#setLogMsg(com.intel
     * .stl.api.logs.LogResponse)
     */
    @Override
    public void setLogMessage(LogResponse msg) {
        model.setLogMsg(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setFirstPage(boolean)
     */
    @Override
    public void setFirstPage(boolean b) {
        firstPage = b;
        view.setPreviousEnabled(!b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setLastPage(boolean)
     */
    @Override
    public void setLastPage(boolean b) {
        lastPage = b;
        view.setNextEnabled(!b);
    }

    @Override
    public synchronized void onSearch(SearchState searchState) {
        String text = view.getDocument();
        List<FilterType> filters = view.getSelectedFilters();
        model.resetSearchResults();

        // Create search keys
        List<SearchKey> searchKeys = new ArrayList<SearchKey>();
        for (FilterType filter : filters) {
            searchKeys.add(new SearchKey(SearchState.FILTERED_SEARCH, filter
                    .getName(), filter.getColor()));
        }

        String searchKey = view.getSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            searchKeys.add(new SearchKey(searchState, searchKey,
                    UIConstants.INTEL_ORANGE));
        }
        view.enableSearch(false);

        // Search for the filter names in the displayed text and highlight them
        boolean showErrors =
                searchState.equals(SearchState.STANDARD_SEARCH) ? true : false;
        doSearch(text, showErrors, searchState, searchKeys);
    }

    public void doSearch(String text, boolean showErrors,
            SearchState searchState, SearchKey searchKey) {
        doSearch(text, showErrors, searchState,
                Collections.singletonList(searchKey));
    }

    public void doSearch(String text, boolean showErrors,
            SearchState searchState, List<SearchKey> searchKeys) {
        checkFilter();
        checkSearch();

        searchTask = new SearchTask(view, model, text, searchState, searchKeys);
        searchTask.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onCancelSearch(
     * JTextComponent)
     */
    @Override
    public void onCancelSearch(String searchKey) {
        checkSearch();

        // Reset ALL search results and set the number of matches to 0
        model.resetSearchResults();

        // Reset number of matches to 0
        view.showNumMatches(0);

        // Un-highlight the search results
        view.unHighlightText();

        // Re-run the search to highlight the filters
        onSearch(SearchState.FILTERED_SEARCH);
    }

    protected synchronized void checkSearch() {
        try {
            if (searchTask != null && !searchTask.isDone()) {
                searchTask.cancel(true);
            }
        } catch (CancellationException e) {
            log.error(e.getMessage());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogStateListener#onSessionDown()
     */
    @Override
    // This method is not used at this time!
    public void onSessionDown(String errorMessage) {
        view.showError(errorMessage);
        view.showLoginView();
        logApi.stopLog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onFilter()
     */
    @Override
    public void onFilter() {
        checkSearch();
        checkFilter();

        filterTask = new FilterTask(this, model, view.getSelectedFilters());
        filterTask.execute();
    }

    protected synchronized void checkFilter() {
        if (filterTask != null && !filterTask.isDone()) {
            filterTask.cancel(true);
        }
    }

    protected synchronized void checkLogTask() {
        if (logTask != null && !logTask.isDone()) {
            logTask.cancel(true);
        }
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setStartLine(long)
     */
    @Override
    public void setStartLine(long lineNum) {
        model.setStartLine(lineNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setEndLine(long)
     */
    @Override
    public void setEndLine(long lineNum) {
        model.setEndLine(lineNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ITextMenuListener#doAction(TextEvent)
     */
    @Override
    public void doAction(TextEvent e) {

        switch (e.getEventType()) {
            case COPY:
            case PASTE:
                // The copy and paste actions uses the DefaultEditorKit
                // CopyAction() and PasteAction() in the TextContentPanel
                break;

            case HIGHLIGHT:
                view.setSearchField(e.getText());
                onSearch(SearchState.MARKED_SEARCH);
                break;

            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ITextListener#resetHighlights()
     */
    @Override
    public void resetSearch() {
        model.resetSearchResults();
        view.resetSearchField();
        view.unHighlightText();
        view.showNumMatches(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    protected void onDirty() {
        dirty = false;
        view.saveLastSearchKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ITextMenuListener#unHighlightSelection
     * (String, int, int)
     */
    @Override
    public void unHighlightSelection(String key, int start, int end) {
        view.unHighlightSelection(key, start, end);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ITextMenuListener#getSelectionStart()
     */
    @Override
    public int getSelectionStart() {
        return model.getSelectionStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ITextMenuListener#getSelectionEnd()
     */
    @Override
    public int getSelectionEnd() {
        return model.getSelectionEnd();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogViewListener#setCurrentSelection
     * (int, int)
     */
    @Override
    public void setCurrentSelection(String key, int start, int end) {
        model.setSelection(key, start, end);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ITextMenuListener#getSelectedKey()
     */
    @Override
    public String getSelectedKey() {
        return model.getSelectedKey();
    }

    @Override
    public void cancelLogin() {
        view.clearLoginData();
        view.updateLoginView(model);
        logApi.stopLog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogController#getDocumentListener()
     */
    @Override
    public DocumentListener getDocumentListener() {

        if (setDirtyListener == null) {
            setDirtyListener = new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    dirty = true;
                    onDirty();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    dirty = true;
                    onCancelSearch(view.getLastSearchKey());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    dirty = true;
                    onDirty();
                }
            };
        }

        return setDirtyListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogController#isDirty()
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    public void setCredentials(LoginBean credentials) {
        model.setCredentials(credentials);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#updateLoginView()
     */
    @Override
    public void updateLoginView() {
        // model.setLogFilePath(logApi.getLogFilePath());
        view.updateLoginView(model);
    }

    public boolean isInitInProgress() {
        return initInProgress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogViewListener#restoreAutoConfigView()
     */
    @Override
    public void restoreAutoConfigView() {
        HostInfo hostInfo = subnet.getCurrentFE();
        hostInfo.setSshUserName(subnetUserName);
        LoginBean credentials =
                new LoginBean(hostInfo.getSshUserName(), hostInfo.getHost(),
                        String.valueOf(hostInfo.getSshPortNum()));
        model.setCredentials(credentials);
        model.setLogFilePath(logApi.getDefaultLogFilePath());
        view.updateLoginView(model);

    }

}
