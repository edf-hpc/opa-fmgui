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
 *  File Name: Statement.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/12/17 22:38:29  jijunwan
 *  Archive Log:    PR 131988 - Failover as I switch networks results in ERROR - Statement is closed to be dispalyed
 *  Archive Log:    - changed code to be silent on StatementClose on UI side
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/09/08 18:34:14  jijunwan
 *  Archive Log:    PR 130277 - FM GUI Locked up due to [AWT-EventQueue-0] ERROR - Unsupported MTUSize 0x0d java.lang.IllegalArgumentException: Unsupported MTUSize 0x0d
 *  Archive Log:    - moved isDev to FMGuiPlugin so both backend and frontend can access it
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/09/08 14:58:38  jijunwan
 *  Archive Log:    PR 130277 - FM GUI Locked up due to [AWT-EventQueue-0] ERROR - Unsupported MTUSize 0x0d java.lang.IllegalArgumentException: Unsupported MTUSize 0x0d
 *  Archive Log:    - moved isDev logic to backend
 *  Archive Log:    - when isDev, we try to check whether we are querying FE from EDT and print out stack trace
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:49:23  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/14 21:53:02  fernande
 *  Archive Log:    PR 128703 - Fail over doesn't work on A0 Fabric. Fix for issues in the retry logic of SMFailoverManager
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/06/16 15:57:52  fernande
 *  Archive Log:    PR 129034 Support secure FE. Changes to the IResponse interface to throw only checked IOExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/29 20:39:32  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/05/26 15:41:45  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.session;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.FMGuiPlugin;
import com.intel.stl.api.StatementClosedException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.MultipleResponseCommand;
import com.intel.stl.fecdriver.SingleResponseCommand;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;

public class Statement implements IStatement {
    private static Logger log = LoggerFactory.getLogger(Statement.class);

    private static boolean DEBUG = false;

    protected static final int DEFAULT_TIMEOUT = 30000; // in milliseconds

    private int timeout = DEFAULT_TIMEOUT;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final Session session;

    public Statement(Session session) {
        this.session = session;
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public void close() {
        boolean isClosed = isClosed();
        if (!isClosed) {
            if (closed.compareAndSet(isClosed, true)) {
                session.removeStatement(this);
            }
        }
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int milliseconds) {
        this.timeout = milliseconds;
    }

    @Override
    public SubnetDescription getSubnetDescription() {
        return session.getSubnetDescription();
    }

    @Override
    public <F, E extends IResponse<F>> List<F> execute(
            MultipleResponseCommand<F, E> cmd) throws Exception {
        if (FMGuiPlugin.IS_DEV && SwingUtilities.isEventDispatchThread()) {
            new Exception("Query FM from EDT!").printStackTrace();
        }

        List<F> result = null;
        submit(cmd);
        try {
            result = cmd.getResults(getTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException toe) {
            if (processTimeout(cmd)) {
                // This timeout will be reset by FailoverManager. If it
                // times out again, caller will get it
                result = cmd.getResults(getTimeout(), TimeUnit.MILLISECONDS);
            } else {
                throw toe;
            }
        }
        debugResponse(cmd);
        return result;
    }

    @Override
    public <F, E extends IResponse<F>> F execute(SingleResponseCommand<F, E> cmd)
            throws Exception {
        if (FMGuiPlugin.IS_DEV && SwingUtilities.isEventDispatchThread()) {
            new Exception("Query FM from EDT!").printStackTrace();
        }

        F result = null;
        submit(cmd);
        try {
            result = cmd.getResult(getTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException toe) {
            if (processTimeout(cmd)) {
                // This timeout will be reset by FailoverManager. If it
                // times out again, caller will get it
                result = cmd.getResult(getTimeout(), TimeUnit.MILLISECONDS);
            } else {
                throw toe;
            }
        }
        debugResponse(cmd);
        return result;
    }

    @Override
    public <E extends IResponse<F>, F> void submit(ICommand<E, F> cmd)
            throws Exception {
        if (isClosed()) {
            RuntimeException rte = new StatementClosedException();
            cmd.getResponse().setError(rte);
            throw rte;
        }
        prepareCommand(cmd);
        log.debug("submit cmd " + cmd + " with argument " + cmd.getInput());
        session.submitCmd(cmd);
    }

    // Used in testing
    public <E extends IResponse<F>, F> void submitCommand(ICommand<E, F> cmd) {
        session.submitCmd(cmd);
    }

    // Used in testing
    public <E extends IResponse<F>, F> void prepareCommand(ICommand<E, F> cmd) {
        OobPacket sendPacket = createSendPacket(cmd);
        cmd.setPacket(sendPacket);
        cmd.setStatement(this);
    }

    protected boolean processTimeout(ICommand<?, ?> cmd) {
        log.info("Timeout waiting for response for cmd " + cmd);
        // Tell the STLAdapter about this
        session.fireOnRequestTimeout(new TimeoutException(
                "Timeout waiting for response for command "
                        + cmd.getClass().getSimpleName() + " with id "
                        + cmd.getMessageID()));
        return true;
    }

    private <E extends IResponse<F>, F> OobPacket createSendPacket(
            ICommand<E, F> cmd) {
        OobPacket sendPacket = new OobPacket();
        sendPacket.build(true);
        RmppMad rmppMad = cmd.prepareMad();
        sendPacket.setRmppMad(rmppMad);
        sendPacket.fillPayloadSize();
        sendPacket.setExpireTime(System.currentTimeMillis() + timeout * 1000);
        return sendPacket;
    }

    private <E extends IResponse<F>, F> void debugResponse(ICommand<E, F> cmd) {
        E response = cmd.getResponse();
        List<F> results;
        try {
            results = response.get();
            if (DEBUG && results != null) {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println(i + " " + results.get(i));
                }
            }
        } catch (Exception e) {
        }
    }

}
