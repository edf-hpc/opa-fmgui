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
 *  File Name: Randomizer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/08/17 18:49:03  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/07/02 20:23:21  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Moving Blobs to the database; arrays are now being saved to the database as collection tables.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/06/25 21:04:03  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improvement on data randomization that ensure an attribute at the same time point get the same data where
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/09 03:29:24  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/04 21:38:00  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 21:02:42  jijunwan
 *  Archive Log:    minor change - reduced randomization range on image info
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/15 21:38:07  jijunwan
 *  Archive Log:    1) implemented the new GroupConfig and FocusPorts queries that use separated req and rsp data structure
 *  Archive Log:    2) adapter our drive and db code to the new data structure
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/17 18:54:23  jijunwan
 *  Archive Log:    minor improvement on random performance generation
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 21:36:56  jijunwan
 *  Archive Log:    added randomizer to fully support all error counters
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.intel.stl.api.IRandomable;
import com.intel.stl.api.performance.ErrBucketBean;
import com.intel.stl.api.performance.ErrStatBean;
import com.intel.stl.api.performance.FocusPortsRspBean;
import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.UtilStatsBean;
import com.intel.stl.api.performance.VFFocusPortsRspBean;
import com.intel.stl.api.performance.VFInfoBean;

public class Randomizer implements IRandomable {
    private final Random random;

    private boolean isActive;

    private long allBandwidth;

    private long allPacketRate;

    private long allCongestion;

    private long allSignalIntegrity;

    private long allSmaCongestion;

    private long allSecurity;

    private long allRouting;

    private long bandwidth;

    private long packetRate;

    private long congestion;

    private long signalIntegrity;

    private long smaCongestion;

    private long security;

    private long routing;

    public Randomizer() {
        random = new Random();
        isActive = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.IRandomable#setSeed(long)
     */
    @Override
    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.IRandomable#setRandom(boolean)
     */
    @Override
    public void setRandom(boolean b) {
        isActive = b;
    }

    private final List<ImageInfoBean> recentImageInfos =
            new ArrayList<ImageInfoBean>(4);

    public void randomImageInfo(ImageInfoBean imageInfo, int nodes) {
        if (!isActive) {
            return;
        }

        int index = recentImageInfos.indexOf(imageInfo);
        if (index >= 0) {
            ImageInfoBean last = recentImageInfos.get(index);
            imageInfo.setNumFailedNodes(last.getNumFailedNodes());
            imageInfo.setNumSkippedNodes(last.getNumSkippedNodes());
            imageInfo.setNumFailedPorts(last.getNumFailedPorts());
            imageInfo.setNumSkippedPorts(last.getNumSkippedPorts());
            return;
        }

        if (recentImageInfos.size() >= 4) {
            recentImageInfos.remove(0);
        }
        recentImageInfos.add(imageInfo);

        imageInfo.setNumFailedNodes(random.nextInt(nodes) / 10);
        nodes -= imageInfo.getNumFailedNodes();
        imageInfo.setNumSkippedNodes(random.nextInt(nodes) / 10);

        long ports = imageInfo.getNumHFIPorts() + imageInfo.getNumSwitchPorts();
        imageInfo.setNumFailedPorts((long) (random.nextDouble() * ports / 10));
        ports -= imageInfo.getNumFailedPorts();
        imageInfo.setNumSkippedPorts((long) (random.nextDouble() * ports / 10));

    }

    private final List<GroupInfoBean> recentGroupInfos =
            new ArrayList<GroupInfoBean>(4);

    public void randomGroupInfo(GroupInfoBean info) {
        if (!isActive) {
            return;
        }

        int index = recentGroupInfos.indexOf(info);
        if (index >= 0) {
            GroupInfoBean last = recentGroupInfos.get(index);
            info.setInternalUtilStats(last.getInternalUtilStats());
            info.setInternalErrors(last.getInternalErrors());
            return;
        }

        if (recentGroupInfos.size() >= 4) {
            recentGroupInfos.remove(0);
        }
        recentGroupInfos.add(info);

        boolean isAll = info.getGroupName().equals("All");

        UtilStatsBean internalUtil = info.getInternalUtilStats();
        if (isAll) {
            bandwidth =
                    Math.max(bandwidth,
                            internalUtil.getTotalMBps() + random.nextInt(1000));
            allBandwidth = bandwidth;
            packetRate =
                    Math.max(packetRate,
                            internalUtil.getTotalKPps() + random.nextInt(1000));
            allPacketRate = packetRate;
        } else {
            bandwidth = (long) (allBandwidth * random.nextDouble());
            packetRate = (long) (allPacketRate * random.nextDouble());
        }
        internalUtil.setTotalMBps(bandwidth);
        internalUtil.setTotalKPps(packetRate);

        randomHistogram(internalUtil.getBwBucketsAsArray());

        ErrStatBean errStat = info.getInternalErrors();
        if (isAll) {
            congestion =
                    Math.max(congestion, errStat.getErrorMaximums()
                            .getCongestionErrors() + random.nextInt(99));
            allCongestion = congestion;

            signalIntegrity =
                    Math.max(signalIntegrity, errStat.getErrorMaximums()
                            .getIntegrityErrors() + random.nextInt(99));
            allSignalIntegrity = signalIntegrity;

            smaCongestion =
                    Math.max(smaCongestion, errStat.getErrorMaximums()
                            .getSmaCongestionErrors() + random.nextInt(99));
            allSmaCongestion = smaCongestion;

            security =
                    Math.max(security, errStat.getErrorMaximums()
                            .getSecurityErrors() + random.nextInt(99));
            allSecurity = security;

            routing =
                    Math.max(routing, errStat.getErrorMaximums()
                            .getRoutingErrors() + random.nextInt(99));
            allRouting = routing;
        } else {
            congestion = (int) (allCongestion * random.nextDouble());
            signalIntegrity = (int) (allSignalIntegrity * random.nextDouble());
            smaCongestion = (int) (allSmaCongestion * random.nextDouble());
            security = (int) (allSecurity * random.nextDouble());
            routing = (int) (allRouting * random.nextDouble());
        }
        errStat.getErrorMaximums().setCongestionErrors(congestion);
        errStat.getErrorMaximums().setIntegrityErrors(signalIntegrity);
        errStat.getErrorMaximums().setSmaCongestionErrors(smaCongestion);
        errStat.getErrorMaximums().setSecurityErrors(security);
        errStat.getErrorMaximums().setRoutingErrors(routing);

        ErrBucketBean[] ports = errStat.getPorts();
        int[] congestions = new int[ports.length];
        int[] integrities = new int[ports.length];
        int[] smaCongestions = new int[ports.length];
        int[] securities = new int[ports.length];
        int[] routings = new int[ports.length];
        for (int i = 0; i < ports.length; i++) {
            congestions[i] = ports[i].getCongestionErrors();
            integrities[i] = ports[i].getIntegrityErrors();
            smaCongestions[i] = ports[i].getSmaCongestionErrors();
            securities[i] = ports[i].getSecurityErrors();
            routings[i] = ports[i].getRoutingErrors();
        }
        randomHistogram(congestions);
        randomHistogram(integrities);
        randomHistogram(smaCongestions);
        randomHistogram(securities);
        randomHistogram(routings);
        for (int i = 0; i < ports.length; i++) {
            ports[i].setCongestionErrors(congestions[i]);
            ports[i].setIntegrityErrors(integrities[i]);
            ports[i].setSmaCongestionErrors(smaCongestions[i]);
            ports[i].setSecurityErrors(securities[i]);
            ports[i].setRoutingErrors(routings[i]);
        }
    }

    private final List<VFInfoBean> recentVFInfos = new ArrayList<VFInfoBean>(4);

    public void randomVFInfo(VFInfoBean info) {
        if (!isActive) {
            return;
        }

        int index = recentVFInfos.indexOf(info);
        if (index >= 0) {
            VFInfoBean last = recentVFInfos.get(index);
            info.setInternalUtilStats(last.getInternalUtilStats());
            info.setInternalErrors(last.getInternalErrors());
            return;
        }

        if (recentVFInfos.size() >= 4) {
            recentVFInfos.remove(0);
        }
        recentVFInfos.add(info);

        UtilStatsBean internalUtil = info.getInternalUtilStats();
        long bandwidth = internalUtil.getTotalMBps() + random.nextInt(1000);
        long packetRate = internalUtil.getTotalKPps() + random.nextInt(1000);
        internalUtil.setTotalMBps(bandwidth);
        internalUtil.setTotalKPps(packetRate);

        randomHistogram(internalUtil.getBwBucketsAsArray());

        ErrStatBean errStat = info.getInternalErrors();
        long value =
                errStat.getErrorMaximums().getCongestionErrors()
                        + random.nextInt(99);
        errStat.getErrorMaximums().setCongestionErrors(value);

        value =
                errStat.getErrorMaximums().getIntegrityErrors()
                        + random.nextInt(99);
        errStat.getErrorMaximums().setIntegrityErrors(value);

        value =
                errStat.getErrorMaximums().getSmaCongestionErrors()
                        + random.nextInt(99);
        errStat.getErrorMaximums().setSmaCongestionErrors(value);

        value =
                errStat.getErrorMaximums().getSecurityErrors()
                        + random.nextInt(99);
        errStat.getErrorMaximums().setSecurityErrors(value);

        value =
                errStat.getErrorMaximums().getRoutingErrors()
                        + random.nextInt(99);
        errStat.getErrorMaximums().setRoutingErrors(value);

        ErrBucketBean[] ports = errStat.getPorts();
        int[] congestions = new int[ports.length];
        int[] integrities = new int[ports.length];
        int[] smaCongestions = new int[ports.length];
        int[] securities = new int[ports.length];
        int[] routings = new int[ports.length];
        for (int i = 0; i < ports.length; i++) {
            congestions[i] = ports[i].getCongestionErrors();
            integrities[i] = ports[i].getIntegrityErrors();
            smaCongestions[i] = ports[i].getSmaCongestionErrors();
            securities[i] = ports[i].getSecurityErrors();
            routings[i] = ports[i].getRoutingErrors();
        }
        randomHistogram(congestions);
        randomHistogram(integrities);
        randomHistogram(smaCongestions);
        randomHistogram(securities);
        randomHistogram(routings);
        for (int i = 0; i < ports.length; i++) {
            ports[i].setCongestionErrors(congestions[i]);
            ports[i].setIntegrityErrors(integrities[i]);
            ports[i].setSmaCongestionErrors(smaCongestions[i]);
            ports[i].setSecurityErrors(securities[i]);
            ports[i].setRoutingErrors(routings[i]);
        }
    }

    protected void randomHistogram(int[] counts) {
        int delta = 0;
        for (int i = 0; i < counts.length - 1; i++) {
            double tmp =
                    (int) (counts[i] * random.nextDouble() - delta
                            * random.nextDouble());
            counts[i] -= (int) tmp;
            delta += (int) tmp;
        }
        counts[counts.length - 1] += delta;
    }

    protected void randomHistogram(Integer[] counts) {
        int delta = 0;
        for (int i = 0; i < counts.length - 1; i++) {
            double tmp =
                    (int) (counts[i] * random.nextDouble() - delta
                            * random.nextDouble());
            counts[i] -= (int) tmp;
            delta += (int) tmp;
        }
        counts[counts.length - 1] += delta;
    }

    public void randomFocusPorts(List<FocusPortsRspBean> focusPorts) {
        if (!isActive) {
            return;
        }

        int[] values = new int[focusPorts.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * 100);
        }
        Arrays.sort(values);
        for (int i = 0; i < values.length; i++) {
            focusPorts.get(i).setValue(values[values.length - 1 - i]);
        }
    }

    public void randomVFFocusPorts(List<VFFocusPortsRspBean> focusPort) {
        if (!isActive) {
            return;
        }

        int[] values = new int[focusPort.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * 100);
        }
        Arrays.sort(values);
        for (int i = 0; i < values.length; i++) {
            focusPort.get(i).setValue(values[values.length - 1 - i]);
        }
    }

    Map<Point, Long[]> counters = new HashMap<Point, Long[]>();

    public synchronized void randomPortCounters(PortCountersBean counter) {
        Point id = new Point(counter.getNodeLid(), counter.getPortNumber());
        Long[] last = counters.get(id);
        if (last == null) {
            counters.put(
                    id,
                    new Long[] { counter.getPortRcvData(),
                            counter.getPortXmitData() });
            return;
        }
        last[0] = getRandomTraffic(last[0]);
        counter.setPortRcvData(last[0]);
        last[1] = getRandomTraffic(last[1]);
        counter.setPortXmitData(last[1]);
    }

    private long getRandomTraffic(long val) {
        if (val < (0x01 << 30)) {
            val = 0x01 << 30;
        }
        long res = val + (long) Math.ceil(random.nextDouble() * (0x01L << 34));
        return res;
    }
}
