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
 *  File Name: FVCmdGetFabricInfo.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/09/18 18:14:26  fernande
 *  Archive Log:    PR 130080 - Support FABRIC_INFO_RECORD. Added support in the FE Adapter for use in fabric health score improvement.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.command.sa;

import com.intel.stl.api.subnet.FabricInfoBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.fecdriver.SingleResponseCommand;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.sa.FabricInfo;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.command.InputArgument;
import com.intel.stl.fecdriver.messages.response.sa.FVRspGetFabricInfo;

public class FVCmdGetFabricInfo extends SACommand<FabricInfo, FabricInfoBean>
        implements SingleResponseCommand<FabricInfoBean, FVRspGetFabricInfo> {

    public FVCmdGetFabricInfo() {
        setResponse(new FVRspGetFabricInfo());
        setInput(new InputArgument());
    }

    @Override
    protected void fillCommonMad(CommonMad comm) {
        super.fillCommonMad(comm);
        comm.setMethod(SAConstants.SUBN_ADM_GET);
        comm.setAttributeID(SAConstants.STL_SA_ATTR_FABRICINFO_RECORD);
    }

    @Override
    protected FabricInfo buildRecord() {
        FabricInfo rec = new FabricInfo();
        rec.build(true);
        return rec;
    }

    @Override
    protected void fillInput(SAHeader header, FabricInfo record) {
        header.setComponentMask(0);
    }

}
