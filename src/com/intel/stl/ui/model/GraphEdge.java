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
 *  File Name: GraphEdge.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/03 22:13:47  jijunwan
 *  Archive Log:    1) added normalization to GraphEdge, so we can identify the same edges represented in different directions
 *  Archive Log:    2) added helper method to the neighbor of a port
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/23 04:56:57  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:55  jijunwan
 *  Archive Log:    init version of topology page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.Map;
import java.util.TreeMap;

public class GraphEdge extends GraphCell {
    private static final long serialVersionUID = 6368674272408088705L;
    private int fromLid;
    private int toLid;
    private Map<Integer, Integer> links;
    
    /**
     * Description: 
     * 
     */
    public GraphEdge() {
        super();
    }

    public GraphEdge(int fromLid, int toLid, Map<Integer, Integer> links) {
        super();
        this.fromLid = fromLid;
        this.toLid = toLid;
        this.links = links;
        this.name = createName();
    }
    
    /**
     * Description: 
     *
     * @param fromLid
     * @param toLid
     * @param links 
     */
    public GraphEdge(String name, int fromLid, int toLid, Map<Integer, Integer> links) {
        super(name);
        this.fromLid = fromLid;
        this.toLid = toLid;
        this.links = links;
    }
    
    public GraphEdge normalize() {
        if (fromLid < toLid) {
            Map<Integer, Integer> reversedLinks = new TreeMap<Integer, Integer>();
            for (Integer key : links.keySet()) {
                reversedLinks.put(links.get(key), key);
            }
            return new GraphEdge(toLid, fromLid, reversedLinks);
        } else {
            return this;
        }
    }
    
    /* (non-Javadoc)
     * @see com.intel.stl.ui.model.GraphCell#isVertex()
     */
    @Override
    public boolean isVertex() {
        return false;
    }

    protected String createName() {
        return fromLid+"-"+toLid;
    }

    /**
     * @return the fromLid
     */
    public int getFromLid() {
        return fromLid;
    }

    /**
     * @param fromLid the fromLid to set
     */
    public void setFromLid(int fromLid) {
        this.fromLid = fromLid;
    }

    /**
     * @return the toLid
     */
    public int getToLid() {
        return toLid;
    }

    /**
     * @param toLid the toLid to set
     */
    public void setToLid(int toLid) {
        this.toLid = toLid;
    }

    /**
     * @return the links
     */
    public Map<Integer, Integer> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(Map<Integer, Integer> links) {
        this.links = links;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fromLid;
        result = prime * result + toLid;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GraphEdge other = (GraphEdge) obj;
        if (fromLid != other.fromLid)
            return false;
        if (toLid != other.toLid)
            return false;
        return true;
    }

    public String toString() {
        return fromLid+"-"+toLid+":"+links.size()+" "+links;
    }
}
