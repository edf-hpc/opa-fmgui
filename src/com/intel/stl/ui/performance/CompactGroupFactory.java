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
 *  File Name: FullViewFactory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2015/08/17 18:53:49  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/08/10 17:06:27  jypak
 *  Archive Log:    PR 129919 - Change name from signal integrity to integrity.
 *  Archive Log:    STLConstants and class names are changed.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/06/25 20:50:03  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/06/09 18:37:23  jijunwan
 *  Archive Log:    PR 129069 - Incorrect Help action
 *  Archive Log:    - moved help action from view to controller
 *  Archive Log:    - only enable help button when we have HelpID
 *  Archive Log:    - fixed incorrect HelpIDs
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/27 15:48:34  jijunwan
 *  Archive Log:    changed K0072_SECURITY_ERROR to K0072_SECURITY
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/11 21:14:59  jypak
 *  Archive Log:    1. For 'current' history scope, default max data points need to be set.
 *  Archive Log:    2. History icon fixed.
 *  Archive Log:    3. Home Page performance section trend charts should show history scope selections.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/30 04:12:56  jijunwan
 *  Archive Log:    PR 126775 - "Bubble" error metric graph is not being plotted even though "opatop" shows bubble errors
 *  Archive Log:     - the chart used wrong data. corrected to use bubble counter
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/11 21:38:26  jijunwan
 *  Archive Log:    added bubble error charts on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/15 15:24:30  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/22 18:41:42  jijunwan
 *  Archive Log:    added DataType support for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 17:30:44  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/16 21:38:05  jijunwan
 *  Archive Log:    added 3 type error counters
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:09:00  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.PinDescription.PinID;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.item.BBHistogramItem;
import com.intel.stl.ui.performance.item.BBTopNItem;
import com.intel.stl.ui.performance.item.BBTrendItem;
import com.intel.stl.ui.performance.item.BWHistogramItem;
import com.intel.stl.ui.performance.item.BWTopNItem;
import com.intel.stl.ui.performance.item.BWTrendItem;
import com.intel.stl.ui.performance.item.CGHistogramItem;
import com.intel.stl.ui.performance.item.CGTopNItem;
import com.intel.stl.ui.performance.item.CGTrendItem;
import com.intel.stl.ui.performance.item.PRTopNItem;
import com.intel.stl.ui.performance.item.PRTrendItem;
import com.intel.stl.ui.performance.item.REHistogramItem;
import com.intel.stl.ui.performance.item.RETopNItem;
import com.intel.stl.ui.performance.item.RETrendItem;
import com.intel.stl.ui.performance.item.SCHistogramItem;
import com.intel.stl.ui.performance.item.SCTopNItem;
import com.intel.stl.ui.performance.item.SCTrendItem;
import com.intel.stl.ui.performance.item.SEHistogramItem;
import com.intel.stl.ui.performance.item.SETopNItem;
import com.intel.stl.ui.performance.item.SETrendItem;
import com.intel.stl.ui.performance.item.IntegrityHistogramItem;
import com.intel.stl.ui.performance.item.IntegrityTopNItem;
import com.intel.stl.ui.performance.item.IntegrityTrendItem;

public class CompactGroupFactory {
    public static CompactGroupController createBandwidthGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0041_BANDWIDTH.getValue(),
                        new BWTrendItem(), new BWHistogramItem(),
                        new BWTopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_BW);
        setUnitGroupHelp(res);
        return res;
    }

    public static CompactGroupController createPacketRateGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0065_PACKET_RATE.getValue(),
                        new PRTrendItem(), null, new PRTopNItem(topN),
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_PR);
        setUnitGroupHelp(res);
        return res;
    }

    private static void setUnitGroupHelp(CompactGroupController group) {
        HelpAction helpAction = HelpAction.getInstance();
        group.setHelpIDs(helpAction.getUnitGroup(), helpAction.getUnitGroup());
    }

    public static CompactGroupController createCongestionGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0043_CONGESTION_ERROR.getValue(),
                        new CGTrendItem(), new CGHistogramItem(),
                        new CGTopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_CG);
        setErrorGroupHelp(res);
        return res;
    }

    public static CompactGroupController createSignalIntegrityGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0067_INTEGRITY_ERROR.getValue(),
                        new IntegrityTrendItem(), new IntegrityHistogramItem(),
                        new IntegrityTopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_SI);
        setErrorGroupHelp(res);
        return res;
    }

    public static CompactGroupController createSmaCongestionGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0070_SMA_CONGESTION_ERROR.getValue(),
                        new SCTrendItem(), new SCHistogramItem(),
                        new SCTopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_SC);
        setErrorGroupHelp(res);
        return res;
    }

    public static CompactGroupController createBubbleGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0487_BUBBLE_ERROR.getValue(),
                        new BBTrendItem(), new BBHistogramItem(),
                        new BBTopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_BB);
        setErrorGroupHelp(res);
        return res;
    }

    public static CompactGroupController createSecurityGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0072_SECURITY.getValue(),
                        new SETrendItem(), new SEHistogramItem(),
                        new SETopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_SE);
        setErrorGroupHelp(res);
        return res;
    }

    public static CompactGroupController createRoutingGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        CompactGroupController res =
                new CompactGroupController(eventBus,
                        STLConstants.K0074_ROUTING_ERROR.getValue(),
                        new RETrendItem(), new REHistogramItem(),
                        new RETopNItem(topN), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.SUBNET_RT);
        setErrorGroupHelp(res);
        return res;
    }

    // when we have help for each counter type, we will do this in each
    // createXXXGroup method
    private static void setErrorGroupHelp(CompactGroupController group) {
        HelpAction helpAction = HelpAction.getInstance();
        group.setHelpIDs(helpAction.getErrorGroup(), helpAction.getErrorGroup());
    }
}
