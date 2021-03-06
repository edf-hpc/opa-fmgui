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
 *  File Name: EventDescription.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:48:43  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/19 21:14:22  jijunwan
 *  Archive Log:    PR 129578 - Event table entries dont obviously correspond to log file
 *  Archive Log:    - added event to log file at info level
 *  Archive Log:    - improved toString for EventDescription and PortSource
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/05/19 19:07:19  jijunwan
 *  Archive Log:    PR 128797 - Notice update failed to update related notes
 *  Archive Log:    - created a new class NoticeWrapper to store information about related nodes, and then pass this infor to EventDescription that will allow UI to upate related nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/08 18:59:21  jijunwan
 *  Archive Log:    added Notice Simulator to simulate notices from FM
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/06 20:24:02  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.intel.stl.api.configuration.EventType;

public class EventDescription implements Serializable {
    private static final long serialVersionUID = -7742471691304907204L;

    private long id;

    private Date date;

    private NoticeSeverity severity;

    private IEventSource source;

    private EventType type;

    private Set<Integer> relatedNodes;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the serverity
     */
    public NoticeSeverity getSeverity() {
        return severity;
    }

    /**
     * @param serverity
     *            the serverity to set
     */
    public void setSeverity(NoticeSeverity serverity) {
        this.severity = serverity;
    }

    /**
     * @return the source
     */
    public IEventSource getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(IEventSource source) {
        this.source = source;
    }

    /**
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * @return the relatedNodes
     */
    public Set<Integer> getRelatedNodes() {
        return relatedNodes;
    }

    /**
     * @param relatedNodes
     *            the relatedNodes to set
     */
    public void setRelatedNodes(Set<Integer> relatedNodes) {
        this.relatedNodes = relatedNodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EventDescription [id=" + id + ", date=" + date + ", severity="
                + severity + ", source=" + source + ", type=" + type
                + ", relatedNodes (lids)=" + relatedNodes + "]";
    }

}
