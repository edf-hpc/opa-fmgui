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

package com.intel.stl.api.logs;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.fecdriver.network.ssh.impl.JSchSession;

/**
 * TimerTask class to periodically request the number of lines in the log file
 */
public class LogStatusTask extends TimerTask implements IResponseListener,
        ILogErrorListener {

    @SuppressWarnings("unused")
    private final static Logger log = LoggerFactory
            .getLogger(LogStatusTask.class);

    private final static long DELAY_MS = 2000; // 2 seconds

    private final static long TIME_BETWEEN_EXEC = 1000; // initially 1 seconds

    private final static int RESPONSE_TIMEOUT = 10000; // 10 seconds

    private final FileInfoBean fileInfo;

    private final Timer timer;

    private final LogCommandProcessor fileStatusProcessor;

    private IResponseListener responseListener;

    private ILogErrorListener errorListener;

    private final LogCommander logCommander;

    private final LogMessageType msgType;

    private final LogResponse response = new LogResponse(
            LogMessageType.NUM_LINES);

    public LogStatusTask(String fileName, LogMessageType msgType,
            JSchSession jschSession) {
        this.msgType = msgType;
        timer = new Timer();

        fileInfo = new FileInfoBean(fileName, 0, 0, 0);
        logCommander = new LogCommander(fileInfo);
        fileStatusProcessor =
                new LogCommandProcessor(jschSession, RESPONSE_TIMEOUT, this
                        .getClass().getSimpleName());
        fileStatusProcessor.setResponseListener(this);
    }

    public void setResponseListener(IResponseListener listener) {
        responseListener = listener;
    }

    public void setErrorListener(ILogErrorListener listener) {
        errorListener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        logCommander.setFileInfo(fileInfo);
        String cmd = logCommander.getCommand(msgType, 0);
        fileStatusProcessor.executeCommand(msgType, cmd);
    }

    protected void stop() {
        timer.purge();
        timer.cancel();
        fileStatusProcessor.stop();
    }

    public void start() {
        timer.schedule(this, DELAY_MS, TIME_BETWEEN_EXEC);
    }

    public void start(long delay, long timeBetweenExecutions) {
        timer.schedule(this, delay, timeBetweenExecutions);

    }

    public LogResponse getResponse() {
        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ICommandListener#responseReceived()
     */
    @Override
    public void onResponseReceived(LogResponse response) {
        long totalNumLines = Long.parseLong(response.getEntries().get(0));
        fileInfo.setTotalNumLines(totalNumLines);
        responseListener.onResponseReceived(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.logs.IResponseListener#onResponseError(LogErrorType,
     * LogMessageType)
     */
    @Override
    public void onResponseError(LogErrorType errorCode, LogMessageType type) {
        responseListener.onResponseError(errorCode, type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.IErrorListener#stopLog()
     */
    @Override
    public void stopLog() {
        errorListener.stopLog();
    }
}
