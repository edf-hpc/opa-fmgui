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
 *  File Name: ITty.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:27  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/10 14:09:07  rjtierne
 *  Archive Log:    PR 126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added method closeChannel() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/09 21:13:23  rjtierne
 *  Archive Log:    126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added method closeChannel() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/28 22:22:23  rjtierne
 *  Archive Log:    Added remote host "history" to command dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/23 19:46:16  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Interface for the IntelTty class
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.console;

import com.jcraft.jsch.Session;
import com.wittams.gritty.Tty;

// Added this comment to correct PR 126675 comment above
public interface ITty extends Tty {

    public boolean isConnected();

    public boolean initialize() throws Exception;

    public Session getSession();

    public void setSession(Session session);

    public boolean isEnableMsgListener();

    public void enableMsgListener(boolean enableMsgListener);

    public void closeChannel();

}
