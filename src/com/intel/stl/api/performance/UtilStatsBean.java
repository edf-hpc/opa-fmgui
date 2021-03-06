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
 *  File Name: UtilStatsBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/08/17 18:48:41  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/07/14 18:56:03  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Fixes for Klocwork issues
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/02 20:22:59  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Moving Blobs to the database; arrays are now being saved to the database as collection tables.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class UtilStatsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalMBps; // MB per sec

    private long totalKPps; // K pkts per sec

    private long avgMBps; // unsigned int

    private long minMBps; // unsigned int

    private long maxMBps; // unsigned int

    private int numBWBuckets; // this should be fine, no need to promote to long

    private List<Integer> bwBuckets; // this should be fine, no need to promote
                                     // to long

    private long avgKPps; // unsigned int

    private long minKPps; // unsigned int

    private long maxKPps; // unsigned int

    /**
     * @return the totalMBps
     */
    public long getTotalMBps() {
        return totalMBps;
    }

    /**
     * @param totalMBps
     *            the totalMBps to set
     */
    public void setTotalMBps(long totalMBps) {
        this.totalMBps = totalMBps;
    }

    /**
     * @return the totalKPps
     */
    public long getTotalKPps() {
        return totalKPps;
    }

    /**
     * @param totalKPps
     *            the totalKPps to set
     */
    public void setTotalKPps(long totalKPps) {
        this.totalKPps = totalKPps;
    }

    /**
     * @return the avgMBps
     */
    public long getAvgMBps() {
        return avgMBps;
    }

    /**
     * @param avgMBps
     *            the avgMBps to set
     */
    public void setAvgMBps(long avgMBps) {
        this.avgMBps = avgMBps;
    }

    /**
     * @param avgMBps
     *            the avgMBps to set
     */
    public void setAvgMBps(int avgMBps) {
        this.avgMBps = Utils.unsignedInt(avgMBps);
    }

    /**
     * @return the minMBps
     */
    public long getMinMBps() {
        return minMBps;
    }

    /**
     * @param minMBps
     *            the minMBps to set
     */
    public void setMinMBps(long minMBps) {
        this.minMBps = minMBps;
    }

    /**
     * @param minMBps
     *            the minMBps to set
     */
    public void setMinMBps(int minMBps) {
        this.minMBps = Utils.unsignedInt(minMBps);
    }

    /**
     * @return the maxMBps
     */
    public long getMaxMBps() {
        return maxMBps;
    }

    /**
     * @param maxMBps
     *            the maxMBps to set
     */
    public void setMaxMBps(long maxMBps) {
        this.maxMBps = maxMBps;
    }

    /**
     * @param maxMBps
     *            the maxMBps to set
     */
    public void setMaxMBps(int maxMBps) {
        this.maxMBps = Utils.unsignedInt(maxMBps);
    }

    /**
     * @return the numBWBuckets
     */
    public int getNumBWBuckets() {
        return numBWBuckets;
    }

    /**
     * @param numBWBuckets
     *            the numBWBuckets to set
     */
    public void setNumBWBuckets(int numBWBuckets) {
        this.numBWBuckets = numBWBuckets;
    }

    /**
     * @return the bwBuckets
     */
    public List<Integer> getBwBuckets() {
        return bwBuckets;
    }

    public Integer[] getBwBucketsAsArray() {
        if (bwBuckets == null) {
            bwBuckets = new ArrayList<Integer>();
        }
        Integer[] intArray = new Integer[bwBuckets.size()];
        return bwBuckets.toArray(intArray);
    }

    /**
     * @param bwBuckets
     *            the bwBuckets to set
     */
    public void setBwBuckets(List<Integer> bwBuckets) {
        if (bwBuckets.size() != PAConstants.STL_PM_UTIL_BUCKETS) {
            throw new IllegalArgumentException("Invalid data length. Expect "
                    + PAConstants.STL_PM_UTIL_BUCKETS + ", got "
                    + bwBuckets.size());
        }

        this.bwBuckets = bwBuckets;
    }

    /**
     * @return the avgKPps
     */
    public long getAvgKPps() {
        return avgKPps;
    }

    /**
     * @param avgKPps
     *            the avgKPps to set
     */
    public void setAvgKPps(long avgKPps) {
        this.avgKPps = avgKPps;
    }

    /**
     * @param avgKPps
     *            the avgKPps to set
     */
    public void setAvgKPps(int avgKPps) {
        this.avgKPps = Utils.unsignedInt(avgKPps);
    }

    /**
     * @return the minKPps
     */
    public long getMinKPps() {
        return minKPps;
    }

    /**
     * @param minKPps
     *            the minKPps to set
     */
    public void setMinKPps(long minKPps) {
        this.minKPps = minKPps;
    }

    /**
     * @param minKPps
     *            the minKPps to set
     */
    public void setMinKPps(int minKPps) {
        this.minKPps = Utils.unsignedInt(minKPps);
    }

    /**
     * @return the maxKPps
     */
    public long getMaxKPps() {
        return maxKPps;
    }

    /**
     * @param maxKPps
     *            the maxKPps to set
     */
    public void setMaxKPps(long maxKPps) {
        this.maxKPps = maxKPps;
    }

    /**
     * @param maxKPps
     *            the maxKPps to set
     */
    public void setMaxKPps(int maxKPps) {
        this.maxKPps = Utils.unsignedInt(maxKPps);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UtilStatsBean [totalMBps=" + totalMBps + ", totalKPps="
                + totalKPps + ", avgMBps=" + avgMBps + ", minMBps=" + minMBps
                + ", maxMBps=" + maxMBps + ", numBWBuckets=" + numBWBuckets
                + ", bwBuckets=" + Arrays.toString(bwBuckets.toArray())
                + ", avgKPps=" + avgKPps + ", minKPps=" + minKPps
                + ", maxKPps=" + maxKPps + "]";
    }

}
