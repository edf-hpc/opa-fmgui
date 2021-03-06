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
 *  File Name: NoticeListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:53:35  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/21 16:31:04  fernande
 *  Archive Log:    Renaming clearup() method name to cleanup() to be consistent with other implementations.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 15:24:25  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/26 15:04:38  jijunwan
 *  Archive Log:    notice listeners on UI side
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.alert;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.ui.framework.IAppEvent;

public class NoticeEventListener implements IEventListener<EventDescription> {
    private final IEventObserver[] observers;

    /**
     * Description:
     * 
     * @param eventBus
     */
    public NoticeEventListener(MBassador<IAppEvent> eventBus) {
        super();
        observers =
                new IEventObserver[] { new NodeEventProcessor(eventBus),
                        new PortEventProcessor(eventBus) };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.notice.IEventListener#onNewEvent(java.lang.Object[])
     */
    @Override
    public void onNewEvent(EventDescription[] data) {
        for (IEventObserver observer : observers) {
            observer.onEvents(data);
        }
    }

    public void shutdown() {
        for (IEventObserver observer : observers) {
            observer.cleanup();
        }
    }

}
