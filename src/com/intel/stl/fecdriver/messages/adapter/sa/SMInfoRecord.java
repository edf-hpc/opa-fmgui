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
 *  File Name: SMInfoRecord.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.5  2015/08/17 18:48:48  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.4  2015/06/10 19:36:40  jijunwan
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
package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.fecdriver.messages.adapter.ComposedDatagram;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 * SMInfoRecord
 * 
 * STL Differences:
 * 		LID extended to 32 bits.
 * 		Added Reserved to ensure word-alignment of SMInfo.
 * 		Added Reserved2 to ensure word-alignment of GetTable() responses.
 * 
 * typedef struct {
 * 	struct {
 * 		uint32	LID;
 * 	} PACK_SUFFIX RID;
 * 	
 * 	uint32		Reserved;
 * 	
 * 	STL_SM_INFO		SMInfo;
 * 	
 * } PACK_SUFFIX STL_SMINFO_RECORD;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class SMInfoRecord extends ComposedDatagram<SMRecordBean> {
    private final SimpleDatagram<Void> header;

    private final SMInfo smInfo;

    public SMInfoRecord() {
        header = new SimpleDatagram<Void>(8);
        addDatagram(header);

        smInfo = new SMInfo();
        addDatagram(smInfo);
    }

    public void setLid(int lid) {
        header.getByteBuffer().putInt(0, lid);
    }

    public int getLid() {
        return header.getByteBuffer().getInt(0);
    }

    /**
     * @return the smInfo
     */
    public SMInfo getSmInfo() {
        return smInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#toObject()
     */
    @Override
    public SMRecordBean toObject() {
        SMRecordBean bean = new SMRecordBean(getLid(), smInfo.toObject());
        return bean;
    }

}
