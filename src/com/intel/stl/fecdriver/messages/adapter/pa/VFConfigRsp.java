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
 *  File Name: PortConfig.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:21:52  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 20:04:26  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/08 18:03:12  jijunwan
 *  Archive Log:    fixed a bug in data parsing
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/15 21:38:01  jijunwan
 *  Archive Log:    1) implemented the new GroupConfig and FocusPorts queries that use separated req and rsp data structure
 *  Archive Log:    2) adapter our drive and db code to the new data structure
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:48:30  jijunwan
 *  Archive Log:    made SAConstants and PAonstants acessible from UI
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:31:10  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/01 21:37:08  jijunwan
 *  Archive Log:    Added PA attributes GroupConfig
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.pa;

import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PortConfigBean;
import com.intel.stl.api.performance.VFConfigRspBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.33
 * 
 * <pre>
 * typedef struct _STL_PA_VF_Cfg_Rsp {
 * [16]     STL_PA_IMAGE_ID_DATA    imageId;
 * [24]     uint64                  nodeGUID;
 * [88]     char                    nodeDesc[64];
 * [92]     uint32                  nodeLid;
 * [93]     uint8                   portNumber;
 * [96]     uint8                   reserved[3];
 *  } PACK_SUFFIX STL_PA_PM_GROUP_CFG_RSP;
 * </pre>
 */
public class VFConfigRsp extends SimpleDatagram<VFConfigRspBean> {
    public VFConfigRsp() {
        super(96);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public VFConfigRspBean toObject() {
        buffer.clear();
        VFConfigRspBean bean = new VFConfigRspBean();
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        buffer.position(16);
        PortConfigBean port = new PortConfigBean();
        port.setNodeGUID(buffer.getLong());
        port.setNodeDesc(StringUtils.toString(buffer.array(),
                buffer.arrayOffset() + 24, SAConstants.NODE_DESC_LENGTH));
        buffer.position(88);
        port.setNodeLid(buffer.getInt());
        port.setPortNumber(buffer.get());
        bean.setPort(port);
        return bean;
    }

}
