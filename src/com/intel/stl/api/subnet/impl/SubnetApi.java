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
 *  File Name: SubnetApi.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.46  2015/09/26 06:17:06  jijunwan
 *  Archive Log: 130487 - FM GUI: Topology refresh required after enabling Fabric Simulator
 *  Archive Log: - added reset to clear all caches and update DB topology
 *  Archive Log:
 *  Archive Log: Revision 1.45  2015/09/25 20:47:43  fernande
 *  Archive Log: PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log: Revision 1.44  2015/08/17 18:48:53  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.43  2015/08/11 17:37:19  jijunwan
 *  Archive Log: PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log: - improved to get distribution data with argument "refresh". When it's true, calculate distribution rather than get it from cache
 *  Archive Log:
 *  Archive Log: Revision 1.42  2015/07/30 19:30:27  fernande
 *  Archive Log: PR 129592 - removing a subnet a user is monitoring cause internal DB exception. Added flag to SubnetContext indicating the subnet has been deleted. If the flag is set, no saving of subnet information occurs.
 *  Archive Log:
 *  Archive Log: Revision 1.41  2015/07/10 20:45:03  fernande
 *  Archive Log: PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log: Revision 1.40  2015/07/09 18:51:02  fernande
 *  Archive Log: PR 129447 - Database size increases a lot over a short period of time. Added method to expose application settings in the settings.xml file to higher levels in the app
 *  Archive Log:
 *  Archive Log: Revision 1.39  2015/06/11 17:46:13  fernande
 *  Archive Log: PR 129034 Support secure FE. NPE when getSMs returns a null; included check and changed misleading message
 *  Archive Log:
 *  Archive Log: Revision 1.38  2015/06/10 19:36:39  jijunwan
 *  Archive Log: PR 129153 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jijunwan
 * 
 ******************************************************************************/
package com.intel.stl.api.subnet.impl;

import java.util.EnumMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.impl.SubnetContextImpl;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.FabricInfoBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.LFTRecordBean;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.MFTRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.P_KeyTableRecordBean;
import com.intel.stl.api.subnet.PathRecordBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SC2SLMTRecordBean;
import com.intel.stl.api.subnet.SC2VLMTRecordBean;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.api.subnet.TraceRecordBean;
import com.intel.stl.api.subnet.VLArbTableRecordBean;
import com.intel.stl.common.STLMessages;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

/**
 * @author jijunwan
 * 
 */
public class SubnetApi implements ISubnetApi {
    private static Logger log = LoggerFactory.getLogger(SubnetApi.class);

    private final SubnetContextImpl subnetContext;

    private final CacheManager cacheMgr;

    public SubnetApi(SubnetContextImpl subnetContext) {
        this.subnetContext = subnetContext;
        this.cacheMgr = this.subnetContext.getCacheManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getConnectionDescription()
     */
    @Override
    public SubnetDescription getConnectionDescription() {
        return subnetContext.getSubnetDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getNodes()
     */
    @Override
    public List<NodeRecordBean> getNodes(boolean includeInactive)
            throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNodes(includeInactive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getNode(int)
     */
    @Override
    public NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNode(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getNode(long)
     */
    @Override
    public NodeRecordBean getNode(long portGuid)
            throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNode(portGuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getNodesTypeDist()
     */
    @Override
    public EnumMap<NodeType, Integer> getNodesTypeDist(boolean includeInactive,
            boolean refresh) throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNodesTypeDist(includeInactive, refresh);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getLinks()
     */
    @Override
    public List<LinkRecordBean> getLinks(boolean includeInactive)
            throws SubnetDataNotFoundException {
        LinkCache linkCache = cacheMgr.acquireLinkCache();
        return linkCache.getLinks(includeInactive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getFromLink(int)
     */
    @Override
    public LinkRecordBean getLinkBySource(int lid, short portNum)
            throws SubnetDataNotFoundException {
        LinkCache linkCache = cacheMgr.acquireLinkCache();
        return linkCache.getLinkBySource(lid, portNum);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getToLink(int)
     */
    @Override
    public LinkRecordBean getLinkByDestination(int lid, short portNum)
            throws SubnetDataNotFoundException {
        LinkCache linkCache = cacheMgr.acquireLinkCache();
        return linkCache.getLinkByDestination(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getPorts()
     */
    @Override
    public List<PortRecordBean> getPorts() throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPorts();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getSwitchPort(int, short)
     */
    @Override
    public PortRecordBean getPortByPortNum(int lid, short portNum)
            throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPortByPortNum(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getPort(int, short)
     */
    @Override
    public PortRecordBean getPortByLocalPortNum(int lid, short localPortNum)
            throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPortByLocalPortNum(lid, localPortNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#hasPort(int, short)
     */
    @Override
    public boolean hasPort(int lid, short portNum) {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.hasPort(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#hasLocalPort(int, short)
     */
    @Override
    public boolean hasLocalPort(int lid, short localPortNum) {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.hasLocalPort(lid, localPortNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getPortsTypeDist()
     */
    @Override
    public EnumMap<NodeType, Long> getPortsTypeDist(
            boolean countInternalMgrPort, boolean refresh)
            throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPortsTypeDist(countInternalMgrPort, refresh);
    }

    public long getSubnetPrefix() {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getSubnetPrefix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSwitches()
     */
    @Override
    public List<SwitchRecordBean> getSwitches() {
        SwitchCache switchCache = cacheMgr.acquireSwitchCache();
        return switchCache.getSwitches();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getSwitch(int)
     */
    @Override
    public SwitchRecordBean getSwitch(int lid) {
        SwitchCache switchCache = cacheMgr.acquireSwitchCache();
        return switchCache.getSwitch(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getLFTs()
     */
    @Override
    public List<LFTRecordBean> getLFTs() {
        LFTCache lftCache = cacheMgr.acquireLFTCache();
        return lftCache.getLFTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getLFT(int)
     */
    @Override
    public List<LFTRecordBean> getLFT(int lid) {
        LFTCache lftCache = cacheMgr.acquireLFTCache();
        return lftCache.getLFT(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getMFTs()
     */
    @Override
    public List<MFTRecordBean> getMFTs() {
        MFTCache mftCache = cacheMgr.acquireMFTCache();
        return mftCache.getMFTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getMFT(int)
     */
    @Override
    public List<MFTRecordBean> getMFT(int lid) {
        MFTCache mftCache = cacheMgr.acquireMFTCache();
        return mftCache.getMFT(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getPKeyTables()
     */
    @Override
    public List<P_KeyTableRecordBean> getPKeyTables() {
        PKeyTableCache pkeyTableCache = cacheMgr.acquirePKeyTableCache();
        return pkeyTableCache.getPKeyTables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getVLArbTables()
     */
    @Override
    public List<VLArbTableRecordBean> getVLArbTables() {
        VLArbTableCache vlarbTableCache = cacheMgr.acquireVLArbTableCache();
        return vlarbTableCache.getVLArbTables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SMRecordBean> getSMs() {
        // the UptimeInSeconds field in SMInfo is dynamic. So we should always
        // directly query from FE
        try {
            return getHelper().getSMs();
        } catch (Exception e) {
            throw getSubnetException(e);
        }

        // SMCache smCache = cacheMgr.acquireSMCache();
        // return smCache.getSMs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public SMRecordBean getSM(int lid) {
        // the UptimeInSeconds field in SMInfo is dynamic. So we should always
        // directly query from FE
        try {
            List<SMRecordBean> all = getHelper().getSMs();
            if (all != null) {
                for (SMRecordBean sm : all) {
                    if (sm.getLid() == lid) {
                        return sm;
                    }
                }
            }
            throw new SubnetDataNotFoundException(
                    STLMessages.STL40006_SMRECORD_NOT_FOUND, lid);
        } catch (Exception e) {
            throw getSubnetException(e);
        }

        // SMCache smCache = cacheMgr.acquireSMCache();
        // return smCache.getSM(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<CableRecordBean> getCables() {
        CableCache cableCache = cacheMgr.acquireCableCache();
        return cableCache.getCables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public List<CableRecordBean> getCable(int lid) {
        CableCache cableCache = cacheMgr.acquireCableCache();
        return cableCache.getCable(lid);
    }

    @Override
    public List<CableRecordBean> getCable(int lid, short portNum) {
        CableCache cableCache = cacheMgr.acquireCableCache();
        return cableCache.getCable(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SC2SLMTRecordBean> getSC2SLMTs() {
        SC2SLMTCache sc2slCache = cacheMgr.acquireSC2SLMTCache();
        return sc2slCache.getSC2SLMTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public SC2SLMTRecordBean getSC2SLMT(int lid) {
        SC2SLMTCache sc2slCache = cacheMgr.acquireSC2SLMTCache();
        return sc2slCache.getSC2SLMT(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLTMTs() {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLTMT(int lid) {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMT(lid);

    }

    @Override
    public SC2VLMTRecordBean getSC2VLTMT(int lid, short portNum) {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMT(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLNTMTs() {
        SC2VLNTMTCache sc2vlntCache = cacheMgr.acquireSC2VLNTMTCache();
        return sc2vlntCache.getSC2VLNTMTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLNTMT(int lid) {
        SC2VLNTMTCache sc2vlntCache = cacheMgr.acquireSC2VLNTMTCache();
        return sc2vlntCache.getSC2VLNTMT(lid);

    }

    @Override
    public SC2VLMTRecordBean getSC2VLNTMT(int lid, short portNum) {
        SC2VLNTMTCache sc2vltCache = cacheMgr.acquireSC2VLNTMTCache();
        return sc2vltCache.getSC2VLNTMT(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getPath(int)
     */
    @Override
    public List<PathRecordBean> getPath(int lid) {
        List<PathRecordBean> res = null;
        try {
            res = getHelper().getPath(getGid(lid));
        } catch (Exception e) {
            e.printStackTrace();
            throw getSubnetException(e);
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getTrace(int, int)
     */
    @Override
    public List<TraceRecordBean> getTrace(int sourceLid, int targetLid) {
        List<TraceRecordBean> res = null;
        try {
            res = getHelper().getTrace(getGid(sourceLid), getGid(targetLid));
        } catch (Exception e) {
            e.printStackTrace();
            throw getSubnetException(e);
        }
        return res;
    }

    @Override
    public FabricInfoBean getFabricInfo() {
        FabricInfoBean res = null;
        try {
            res = getHelper().getFabricInfo();
        } catch (Exception e) {
            e.printStackTrace();
            throw getSubnetException(e);
        }
        return res;
    }

    public GID.Global getGid(int lid) throws SubnetDataNotFoundException {
        NodeRecordBean node = getNode(lid);
        return new GID.Global(getSubnetPrefix(), node.getNodeInfo()
                .getPortGUID());
    }

    public static SubnetException getSubnetException(Exception e) {
        SubnetException se =
                new SubnetException(STLMessages.STL60002_SUBNET_DATA_FAILURE,
                        e, StringUtils.getErrorMessage(e));
        log.error(StringUtils.getErrorMessage(se), e);
        return se;
    }

    private SAHelper getHelper() {
        return subnetContext.getSession().getSAHelper();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#reset()
     */
    @Override
    public void reset() {
        cacheMgr.reset();
        cacheMgr.startTopologyUpdateTask();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#cleanup()
     */
    @Override
    public void cleanup() {
        // Nothing to cleanup
    }

    // For testing
    protected CacheManager getCacheManager() {
        return cacheMgr;
    }

}
