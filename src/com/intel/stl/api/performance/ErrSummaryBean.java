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
 *  File Name: ErrSummaryBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:21:44  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/04 21:37:53  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance;

import java.io.Serializable;

import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class ErrSummaryBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long integrityErrors; // unsigned int

    private long congestionErrors; // unsigned int

    private long smaCongestionErrors; // unsigned int

    private long bubbleErrors; // unsigned int

    private long securityErrors; // unsigned int

    private long routingErrors; // unsigned int

    private int congInefficiencyPct10; // promote to handle unsigned short

    private int waitInefficiencyPct10; // promote to handle unsigned short

    private int bubbleInefficiencyPct10; // promote to handle unsigned short

    private int discardsPct10; // promote to handle unsigned short

    private int congestionDiscardsPct10; // promote to handle unsigned short

    private int utilizationPct10; // promote to handle unsigned short

    /**
     * @return the integrityErrors
     */
    public long getIntegrityErrors() {
        return integrityErrors;
    }

    /**
     * @param integrityErrors
     *            the integrityErrors to set
     */
    public void setIntegrityErrors(long integrityErrors) {
        this.integrityErrors = integrityErrors;
    }

    /**
     * @param integrityErrors
     *            the integrityErrors to set
     */
    public void setIntegrityErrors(int integrityErrors) {
        this.integrityErrors = Utils.unsignedInt(integrityErrors);
    }

    /**
     * @return the congestionErrors
     */
    public long getCongestionErrors() {
        return congestionErrors;
    }

    /**
     * @param congestionErrors
     *            the congestionErrors to set
     */
    public void setCongestionErrors(long congestionErrors) {
        this.congestionErrors = congestionErrors;
    }

    /**
     * @param congestionErrors
     *            the congestionErrors to set
     */
    public void setCongestionErrors(int congestionErrors) {
        this.congestionErrors = Utils.unsignedInt(congestionErrors);
    }

    /**
     * @return the smaCongestionErrors
     */
    public long getSmaCongestionErrors() {
        return smaCongestionErrors;
    }

    /**
     * @param smaCongestionErrors
     *            the smaCongestionErrors to set
     */
    public void setSmaCongestionErrors(long smaCongestionErrors) {
        this.smaCongestionErrors = smaCongestionErrors;
    }

    /**
     * @param smaCongestionErrors
     *            the smaCongestionErrors to set
     */
    public void setSmaCongestionErrors(int smaCongestionErrors) {
        this.smaCongestionErrors = Utils.unsignedInt(smaCongestionErrors);
    }

    /**
     * @return the bubbleErrors
     */
    public long getBubbleErrors() {
        return bubbleErrors;
    }

    /**
     * @param bubbleErrors
     *            the bubbleErrors to set
     */
    public void setBubbleErrors(long bubbleErrors) {
        this.bubbleErrors = bubbleErrors;
    }

    /**
     * @param bubbleErrors
     *            the bubbleErrors to set
     */
    public void setBubbleErrors(int bubbleErrors) {
        this.bubbleErrors = Utils.unsignedInt(bubbleErrors);
    }

    /**
     * @return the securityErrors
     */
    public long getSecurityErrors() {
        return securityErrors;
    }

    /**
     * @param securityErrors
     *            the securityErrors to set
     */
    public void setSecurityErrors(long securityErrors) {
        this.securityErrors = securityErrors;
    }

    /**
     * @param securityErrors
     *            the securityErrors to set
     */
    public void setSecurityErrors(int securityErrors) {
        this.securityErrors = Utils.unsignedInt(securityErrors);
    }

    /**
     * @return the routingErrors
     */
    public long getRoutingErrors() {
        return routingErrors;
    }

    /**
     * @param routingErrors
     *            the routingErrors to set
     */
    public void setRoutingErrors(long routingErrors) {
        this.routingErrors = routingErrors;
    }

    /**
     * @param routingErrors
     *            the routingErrors to set
     */
    public void setRoutingErrors(int routingErrors) {
        this.routingErrors = Utils.unsignedInt(routingErrors);
    }

    /**
     * @return the congInefficiencyPct10
     */
    public int getCongInefficiencyPct10() {
        return congInefficiencyPct10;
    }

    /**
     * @param congInefficiencyPct10
     *            the congInefficiencyPct10 to set
     */
    public void setCongInefficiencyPct10(int congInefficiencyPct10) {
        this.congInefficiencyPct10 = congInefficiencyPct10;
    }

    /**
     * @return the discarddPct10
     */
    public int getDiscardsPct10() {
        return discardsPct10;
    }

    /**
     * @param discardPct10
     *            the discardPct10 to set
     */
    public void setDiscardsPct10(int discardsPct10) {
        this.discardsPct10 = discardsPct10;
    }

    /**
     * @return the congestionDiscardsPct10
     */
    public int getCongestionDiscardsPct10() {
        return congestionDiscardsPct10;
    }

    /**
     * @param congestionDiscardsPct10
     *            the congestionDiscardsPct10 to set
     */
    public void setCongestionDiscardsPct10(int congestionDiscardsPct10) {
        this.congestionDiscardsPct10 = congestionDiscardsPct10;
    }

    /**
     * @return the waitInefficiencyPct10
     */
    public int getWaitInefficiencyPct10() {
        return waitInefficiencyPct10;
    }

    /**
     * @param waitInefficiencyPct10
     *            the waitInefficiencyPct10 to set
     */
    public void setWaitInefficiencyPct10(int waitInefficiencyPct10) {
        this.waitInefficiencyPct10 = waitInefficiencyPct10;
    }

    /**
     * @return the bubbleInefficiencyPct10
     */
    public int getBubbleInefficiencyPct10() {
        return bubbleInefficiencyPct10;
    }

    /**
     * @param bubbleInefficiencyPct10
     *            the bubbleInefficiencyPct10 to set
     */
    public void setBubbleInefficiencyPct10(int bubbleInefficiencyPct10) {
        this.bubbleInefficiencyPct10 = bubbleInefficiencyPct10;
    }

    /**
     * @return the utilizationPct10
     */
    public int getUtilizationPct10() {
        return utilizationPct10;
    }

    /**
     * @param utilizationPct10
     *            the utilizationPct10 to set
     */
    public void setUtilizationPct10(int utilizationPct10) {
        this.utilizationPct10 = utilizationPct10;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ErrSummaryBean [integrityErrors=" + integrityErrors
                + ", congestionErrors=" + congestionErrors
                + ", smaCongestionErrors=" + smaCongestionErrors
                + ", bubbleErrors=" + bubbleErrors + ", securityErrors="
                + securityErrors + ", routingErrors=" + routingErrors
                + ", congInefficiencyPct10=" + congInefficiencyPct10
                + ", waitInefficiencyPct10=" + waitInefficiencyPct10
                + ", bubbleInefficiencyPct10=" + bubbleInefficiencyPct10
                + ", discardsPct10=" + discardsPct10
                + ", congestionDiscardsPct10=" + congestionDiscardsPct10
                + ", utilizationPct10=" + utilizationPct10 + "]";
    }

}
