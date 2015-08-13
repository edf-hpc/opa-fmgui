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
 *  File Name: DeviceProperty.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/04/29 14:01:07  jypak
 *  Archive Log:    Updates to display unknown data in the address not interpretable in the QSFP port encoding based on the SFF-8636.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/10 14:23:03  jijunwan
 *  Archive Log:    PR 127495 - Add LED indicator bit to STL_PORT_STATES
 *  Archive Log:    -- display LED Enabled in port properties
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/05 23:38:00  jijunwan
 *  Archive Log:    updated link down reason to match FM 325
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/02 16:27:17  jijunwan
 *  Archive Log:    matched FM 320 (stl_sm.h v1.26) - added linkInitReason
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/01/27 19:44:30  jijunwan
 *  Archive Log:    updated PortInfo to match FM 314 (stl_sm.h v1.125)
 *  Archive Log:      - removed IsSCtoSCMappingEnabled
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/01/27 19:19:52  jijunwan
 *  Archive Log:    updated PortInfo to match FM 314 (stl_sm.h v1.125)
 *  Archive Log:      - removed downDefaultState
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/01/27 19:03:54  jijunwan
 *  Archive Log:    updated SwitchInfo to match FM 314 (stl_sm.h v1.125)
 *  Archive Log:      - added threshold to AdaptiveRouting
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/01/15 21:44:37  jijunwan
 *  Archive Log:    updated to FM Build 298 that removed PortUnsleepState from STL_PORT_STATES
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/01/13 18:22:36  jijunwan
 *  Archive Log:    support UniversalDiagCode and VendorDiagCode
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/01/11 21:36:24  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/12/31 17:49:41  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/12/18 16:33:49  jypak
 *  Archive Log:    Cable Info updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/11/19 07:13:29  jypak
 *  Archive Log:    HoQLife, VL Stall Count : property bar chart panel updates
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/11/13 00:36:49  jypak
 *  Archive Log:    MTU by VL bar chart panel updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/22 16:51:26  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/14 17:39:03  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/04 21:19:44  fernande
 *  Archive Log:    Changes to make DeviceProperties more extensible and to be able to access properties by DeviceProperty key or by DeviceCategory key (group of properties)
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.ui.common.STLConstants;

public enum DeviceProperty implements IPropertyKey {

    ACTIVE_OPTIMIZE(STLConstants.K0810_ACTIVE_OPTIM_ENA),
    ADAPTIVE_ROUTING(STLConstants.K0443_ADAPTIVE_ROUTING),
    ADAPTIVE_ROUTING_ENABLE(STLConstants.K0445_ENABLE),
    ADAPTIVE_ROUTING_PAUSE(STLConstants.K0446_PAUSE),
    ADAPTIVE_ROUTING_LOST(STLConstants.K0447_LOST_ROUTES_ONLY),
    ADAPTIVE_ROUTING_FREQ(STLConstants.K0448_FREQUENCY),
    ADAPTIVE_ROUTING_ALGORITHM(STLConstants.K0449_ALGORITHM),
    ADAPTIVE_ROUTING_THRESHOLD(STLConstants.K0489_THRESHOLD),
    AUTO_MIGR_SUPPORTED(STLConstants.K0333_PORT_AUTO_MIGRATION_SUPP),
    BASE_LID(STLConstants.K0308_BASE_LID),
    BASE_VERSION(STLConstants.K0425_BASE_VERSION),
    BUFFER_ALLOC(STLConstants.K0821_BUFF_ALLOC),
    CLIENT_REREGISTER(STLConstants.K0823_CLIENT_REREGISTER),
    COLLECTIVE_MASK(STLConstants.K0826_COLLECT_MASK),
    CONN_LABEL_SUPPORTED(STLConstants.K0339_PORT_CONN_LABEL_SUPP),
    CREDIT_ACK(STLConstants.K0820_CREDIT_ACK),
    DEVICE_ID(STLConstants.K0504_DEVICE_ID),
    DEVICE_MGMT_SUPPORTED(STLConstants.K0340_PORT_DEVICE_MAN_SUPP),
    DISTANCE_ENA(STLConstants.K0766_DISTANCE_ENA),
    DISTANCE_SUP(STLConstants.K0765_DISTANCE_SUP),
    ENH_SWITCH_PORT0(STLConstants.K0395_ENHANCED_SP0_SUPP),
    FLOW_CONTROL_DISABLE_MASK(STLConstants.K0451_FLOW_CONTROL_DISABLE_MASK),
    GID_PREFIX(STLConstants.K0312_PORT_GID_PREFIX),
    GROUP_NAME(STLConstants.K1030_GROUP_NAME),
    HOQLIFE_LABEL(STLConstants.K0348_PORT_HOQLIFE_LABEL),
    HOQLIFE(STLConstants.K1069_HOQLIFE),
    UNI_DIAG_CODE(STLConstants.K0313_PORT_UNI_DIAG_CODE),
    IPCHASSIS_NAME(STLConstants.K0396_IPCHASSIS_NAME),
    IP_ADDR_IPV6(STLConstants.K0442_IPV6_ADDR),
    IP_ADDR_IPV4(STLConstants.K0441_IPV4_ADDR),
    LARGE_PKT_LIMIT(STLConstants.K0771_LARGE_PKT_LIM),
    LFT_SERIES(STLConstants.K0398_LINEAR_FWD_TABLE),
    LFT_TABLE(STLConstants.K0398_LINEAR_FWD_TABLE),
    LID(STLConstants.K0026_LID),
    LIFETIME_VALUE(STLConstants.K0486_LIFETIME_VALUE),
    LINEAR_FDB_CAP(STLConstants.K0428_LINEAR_FDB_CAP),
    LINEAR_FDB_TOP(STLConstants.K0429_LINEAR_FDB_TOP),
    LINK_DOWN_REASON(STLConstants.K0490_LINK_DOWN_REASON),
    LINK_INIT_REASON(STLConstants.K1780_LINK_INIT_REASON),
    LMC(STLConstants.K0454_LMC),
    MASTER_SMSL(STLConstants.K0351_PORT_MASTER_SMSL),
    MASTER_SM_LID(STLConstants.K0360_PORT_MASTER_SM_LID),
    MTU_SERIES(STLConstants.K1066_MTU_SERIES),
    MAX_NEIGHBOR_MTU_SIZE(STLConstants.K0315_PORT_MAX_NEIGBHOR_SIZE),
    MAX_MTU_SIZE(STLConstants.K0316_PORT_MAX_MUT_SIZE),
    MAX_NEST_LVL_TX(STLConstants.K0767_MAX_NEST_LVL_TX_ENA),
    MAX_NEST_LVL_RX(STLConstants.K0768_MAX_NEST_LVL_RX_SUP),
    MAX_SMALL_PKT_LIMIT(STLConstants.K0773_MAX_SMALL_PKT_LIM),
    MIN_INITIAL(STLConstants.K0769_MIN_INITIAL),
    MIN_TAIL(STLConstants.K0770_MIN_TAIL),
    MFT_TABLE(STLConstants.K0399_MULTI_FWD_TABLE),
    MULTICAST_FDB_CAP(STLConstants.K0430_MULTICAST_FDB_CAP),
    MULTICAST_FDB_TOP(STLConstants.K0431_MULTICAST_FDB_TOP),
    MULTICAST_MASK(STLConstants.K0825_MULTICAST_MASK),
    MULTICAST_PKEY_TRAP(STLConstants.K0824_MULT_PKEY_TRAP),
    M_KEY(STLConstants.K0359_PORT_M_KEY),
    M_KEY_LEASE_PERIOD(STLConstants.K0361_PORT_M_KEY_LEASE_PERIOD),
    M_KEY_PROTECT(STLConstants.K0761_M_KEY_PROTECT),
    M_KEY_VIOLATIONS(STLConstants.K0352_PORT_M_KEY_VIOLATION),
    NEIGHBOR_GUID(STLConstants.K0363_NODE_GUID),
    NEIGHBOR_LINK_DOWN_REASON(STLConstants.K1300_NEIGHBOR_LINKDOWN_REASON),
    NEIGHBOR_MGMT_ALLOWED(STLConstants.K0498_MANAGEMENT_ALLOWED),
    NEIGHBOR_FW_AUTH_BYPASS(STLConstants.K0499_FWD_AUTHENT_BYPASS),
    NEIGHBOR_NODE_TYPE(STLConstants.K0083_NODE_TYPE),
    NODE_DESCRIPTION(STLConstants.K0325_PORT_NODE_DESCRIPTION),
    NODE_GUID(STLConstants.K0363_NODE_GUID),
    NODE_LID(STLConstants.K0026_LID),
    NODE_NAME(STLConstants.K0500_NODE_NAME),
    NODE_STATE(STLConstants.K0362_NODE_STATE),
    NODE_TYPE(STLConstants.K0422_TYPE),
    NOTICE_SUPPORTED(STLConstants.K0330_PORT_NOTICE_SUPP),
    NUM_ENTRIES(STLConstants.K0391_NUM_ENTRIES),
    NUM_PORTS(STLConstants.K0389_NUM_PORTS),
    NUM_VL(STLConstants.K1067_NUM_VL),
    OFFLINE_DISABLED_REASON(STLConstants.K0757_OFFLINE_DISABLED_REASON),
    OPERATIONAL_VLS(STLConstants.K0349_PORT_OPERATIONAL_VLS),
    PASSTHRU(STLConstants.K0811_PASSTHRU_ENA),
    PARTITION_CAP(STLConstants.K0424_PARTITION_CAP),
    PARTITION_ENFORCE_IN(STLConstants.K0303_PORT_PART_ENFORCE_IN),
    PARTITION_ENFORCE_OUT(STLConstants.K0304_PORT_PART_ENFORCE_OUT),
    PORT_ADRR_RANGE_CONFIG(STLConstants.K0090_ADDRRANGECONFIG_SUPPORTED),
    PORT_ASYNC_SC2VL(STLConstants.K0089_ASYNC_SC2VL_SUPPORTED),
    PORT_COUNT(STLConstants.K0364_NODE_PORT_COUNT),
    PORT_ERROR_ACTION(STLConstants.K1040_PORT_ERROR_ACTION),
    PORT_GANGED_DETAILS(STLConstants.K0097_GANGED_PORT_DETAILS),
    PORT_GROUP_CAP(STLConstants.K0432_PORT_GROUP_CAP),
    PORT_GROUP_TOP(STLConstants.K0433_PORT_GROUP_TOP),
    PORT_GUID(STLConstants.K0027_PORT_GUID),
    PORT_INDEX(STLConstants.K0327_PORT_INDEX),
    PORT_LED_ENABLED(STLConstants.K0900_LED_ENABLED),
    PORT_LID(STLConstants.K0453_PORT_LID),
    PORT_LINK_MODE_ACT(STLConstants.K0322_PORT_LINK_ACTIVE),
    PORT_LINK_MODE_ENA(STLConstants.K0320_PORT_LINK_ENABLED),
    PORT_LINK_MODE_SUP(STLConstants.K0321_PORT_LINK_SUPPORTED),
    PORT_LOCAL_NUM(STLConstants.K0816_LOCAL_PORT_NUM),
    PORT_LTP_CRC_ACT(STLConstants.K0322_PORT_LINK_ACTIVE),
    PORT_LTP_CRC_ENA(STLConstants.K0320_PORT_LINK_ENABLED),
    PORT_LTP_CRC_SUP(STLConstants.K0321_PORT_LINK_SUPPORTED),
    PORT_PACKT_FORMAT_SUP(STLConstants.K0320_PORT_LINK_ENABLED),
    PORT_PACKT_FORMAT_ENA(STLConstants.K0321_PORT_LINK_SUPPORTED),
    PORT_NEIGHBOR_NORMAL(STLConstants.K0086_NEIGHBOR_NORMAL),
    PORT_NUMBER(STLConstants.K0427_PORT_NUMBER),
    PORT_OVERALL_BUFF_SPACE(STLConstants.K0096_OVERALL_BUFF_SPACE),
    PORT_PASSTHRU(STLConstants.K0091_PASSTHRU_SUPPORTED),
    PORT_PHYSICAL_STATE(STLConstants.K0310_PORT_PHY_STATE),
    PORT_SHARED_SPACE(STLConstants.K0092_SHAREDSPACE_SUPPORTED),
    PORT_SM_CONFIG_START(STLConstants.K0085_SM_CONFIG_STARTED),
    PORT_SNOOP(STLConstants.K0088_SNOOP_SUPPORTED),
    PORT_STATE(STLConstants.K0309_PORT_STATE),
    PORT_STATE_CHANGE(STLConstants.K0434_PORT_STATE_CHANGE),
    PORT_TYPE(STLConstants.K0496_PORT_TYPE),
    PORT_VL15_MULTICAST(STLConstants.K0093_VL15MULTI_SUPPORTED),
    PORT_VL_MARKER(STLConstants.K0094_VLMARKER_SUPPORTED),
    PORT_VLR(STLConstants.K0095_VLR_SUPPORTED),
    PREEMPTION_LIMIT(STLConstants.K0774_PREEMPTION_LIM),
    P_KEY_VIOLATIONS(STLConstants.K0353_PORT_P_KEY_VIOLATION),
    Q_KEY_VIOLATIONS(STLConstants.K0354_PORT_Q_KEY_VIOLATION),
    REPLAY_DEPTH_BUFFER(STLConstants.K0098_REPLAY_DEPTH_BUFFER),
    REPLAY_DEPTH_WIRE(STLConstants.K0099_REPLAY_DEPTH_WIRE),
    RESPONSE_TIME(STLConstants.K0355_PORT_RESPONSE_TIME),
    REVISION(STLConstants.K0502_REVISION),
    ROUTING_MODE_ENABLED(STLConstants.K0320_PORT_LINK_ENABLED),
    ROUTING_MODE_SUPPORTED(STLConstants.K0321_PORT_LINK_SUPPORTED),
    SMALL_PKT_LIMIT(STLConstants.K0772_SMALL_PKT_LIM),
    SMA_VERSION(STLConstants.K0426_SMA_VERSION),
    SPEEDS_ACTIVE(STLConstants.K0322_PORT_LINK_ACTIVE),
    SPEEDS_ENABLED(STLConstants.K0320_PORT_LINK_ENABLED),
    SPEEDS_SUPPORTED(STLConstants.K0321_PORT_LINK_SUPPORTED),
    SUBNET_MANAGER(STLConstants.K0329_PORT_SUBNET_MANAGER),
    SUBNET_TIMEOUT(STLConstants.K0087_SUBNET_TIMEOUT),
    SYSTEM_IMAGE_GUID(STLConstants.K0423_SYSTEM_IMAGE_GUID),
    TRAP_QUERY_16B(STLConstants.K0814_16B_TRAP_QUERY_ENA),
    VENDOR_CLASS_SUPPORTED(STLConstants.K0341_PORT_VENDOR_CLS_SUPP),
    VENDOR_DIAG_CODE(STLConstants.K0314_PORT_VENDER_DIAG_CODE),
    VENDOR_ID(STLConstants.K0366_NODE_VENDOR_ID),
    VL15_INIT(STLConstants.K0818_VL15_INIT),
    VL15_CREDIT_RATE(STLConstants.K0819_VL15_CREDIT_RATE),
    VL_ARBITR_HIGH_CAP(STLConstants.K0345_PORT_VL_ARBIT_HI_CAP),
    VL_ARBITR_LOW_CAP(STLConstants.K0346_PORT_VL_ARBIT_LOW_CAP),
    VL_CAP(STLConstants.K0343_PORT_VL_CAP),
    VL_HIGH_LIMIT(STLConstants.K0344_PORT_VL_HI_LIMIT),
    VL_MARKER(STLConstants.K0812_VL_MARKER_ENA),
    VL_PREEMPT_CAP(STLConstants.K0452_VL_PREEMPT_CAP),
    VL_PREEMPTING_LIMIT(STLConstants.K0450_VL_PREEMPTING_LIMIT),
    VL_STALL_COUNT(STLConstants.K0347_PORT_VL_STALL_COUNT),
    WIDTHS_ACTIVE(STLConstants.K0322_PORT_LINK_ACTIVE),
    WIDTHS_ENABLED(STLConstants.K0320_PORT_LINK_ENABLED),
    WIDTHS_SUPPORTED(STLConstants.K0321_PORT_LINK_SUPPORTED),
    WIDTHS_DNGRD_TX_ACTIVE(STLConstants.K1601_PORT_LINK_TX_ACTIVE),
    WIDTHS_DNGRD_RX_ACTIVE(STLConstants.K1602_PORT_LINK_RX_ACTIVE),
    WIDTHS_DNGRD_ENABLED(STLConstants.K0320_PORT_LINK_ENABLED),
    WIDTHS_DNGRD_SUPPORTED(STLConstants.K0321_PORT_LINK_SUPPORTED),

    CABLE_ID(STLConstants.K1072_CABLE_ID),
    CABLE_EXT_ID(STLConstants.K1073_CABLE_EXT_ID),
    CABLE_CONNECTOR(STLConstants.K1074_CABLE_CONNECTOR),
    CABLE_NOMINAL_BR(STLConstants.K1075_CABLE_NOMINAL_BR),
    CABLE_SMF_LEN(STLConstants.K1076_CABLE_SMF_LEN),
    CABLE_OM3_LEN(STLConstants.K1077_CABLE_OM3_LEN),
    CABLE_OM2_LEN(STLConstants.K1078_CABLE_OM2_LEN),
    CABLE_OM1_LEN(STLConstants.K1079_CABLE_OM1_LEN),
    CABLE_COPPER_LEN(STLConstants.K1080_CABLE_COPPER_LEN),
    CABLE_DEVICE_TECH(STLConstants.K1081_CABLE_DEVICE_TECH),
    CABLE_VENDOR_NAME(STLConstants.K1082_CABLE_VENDOR_NAME),
    CABLE_EXT_MODULE(STLConstants.K1083_CABLE_EXT_MODULE),
    CABLE_VENDOR_OUI(STLConstants.K1084_CABLE_VENDOR_OUI),
    CABLE_VENDOR_PN(STLConstants.K1085_CABLE_VENDOR_PN),
    CABLE_VENDOR_REV(STLConstants.K1086_CABLE_VENDOR_REV),
    CABLE_OPTICAL_WL(STLConstants.K1087_CABLE_OPTICAL_WL),
    CABLE_MAXCASE_TEMP(STLConstants.K1088_CABLE_MAXCASE_TEMP),
    CABLE_CC_BASE(STLConstants.K1089_CABLE_CC_BASE),
    CABLE_RX_OUT_AMP_PROG(STLConstants.K1090_CABLE_RX_OUT_AMP_PROG),
    CABLE_RX_SQULECH_DIS_IMP(STLConstants.K1091_CABLE_RX_SQULECH_DIS_IMP),
    CABLE_RX_OUT_DIS_CAP(STLConstants.K1092_CABLE_RX_OUT_DIS_CAP),
    CABLE_TX_SQUELCH_DIS_IMP(STLConstants.K1093_CABLE_TX_SQUELCH_DIS_IMP),
    CABLE_TX_SQUELCH_IMP(STLConstants.K1094_CABLE_TX_SQUELCH_IMP),
    CABLE_MEM_PAGE02_PROV(STLConstants.K1095_CABLE_MEM_PAGE02_PROV),
    CABLE_MEM_PAGE01_PROV(STLConstants.K1096_CABLE_MEM_PAGE01_PROV),
    CABLE_TX_DIS_IMP(STLConstants.K1097_CABLE_TX_DIS_IMP),
    CABLE_TX_FAULT_REP_IMP(STLConstants.K1098_CABLE_TX_FAULT_REP_IMP),
    CABLE_LOS_REP_IMP(STLConstants.K1099_CABLE_LOS_REP_IMP),
    CABLE_VENDOR_SN(STLConstants.K1100_CABLE_VENDOR_SN),
    CABLE_DATA_CODE(STLConstants.K1101_CABLE_DATA_CODE),
    CABLE_LOT_CODE(STLConstants.K1102_CABLE_LOT_CODE),
    CABLE_CC_EXT(STLConstants.K1103_CABLE_CC_EXT),
    CABLE_NA(STLConstants.K0016_UNKNOWN),

    SC(STLConstants.K1105_SC),
    SL(STLConstants.K1106_SL),
    VLT(STLConstants.K1109_VLT),
    VLNT(STLConstants.K1110_VLNT);

    private final STLConstants name;

    private DeviceProperty(STLConstants label) {
        this.name = label;
    }

    @Override
    public String getName() {
        return name.getValue();
    }

}
