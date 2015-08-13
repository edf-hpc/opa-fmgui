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
package com.intel.stl.fecdriver.messages.command;

import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.impl.STLStatement;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.response.FVResponse;

/**
 * FVCommand.
 * 
 * @see FVMessage
 * @since JDK 1.3
 * @author Jason Wiseman
 * @version 1.0
 * 
 */
public abstract class FVCommand<E, F> extends FVMessage implements
        ICommand<FVResponse<F>> {
    private InputArgument input;

    private STLStatement submittingStatement;

    /**
     */
    private FVResponse<F> fvResponse;

    /*
     * 
     * @see com.vieo.fv.message.FVMessage#setMessageID(long)
     */
    @Override
    public void setMessageID(long messageID) {
        super.setMessageID(messageID);
        if (fvResponse != null) {
            fvResponse.setMessageID(messageID);
        }
    }

    /**
     * 
     * @param fvResponse
     *            the response associated with this command.
     */
    protected void setResponse(FVResponse<F> fvResponse) {
        fvResponse.setMessageID(getMessageID());
        this.fvResponse = fvResponse;
    }

    /**
     * 
     * @return the FVResponse associated with this FVCommand.
     */
    @Override
    public FVResponse<F> getResponse() {
        return fvResponse;
    }

    /**
     */
    public InputArgument getInput() {
        return input;
    }

    /**
     *            the input to set
     */
    public void setInput(InputArgument input) {
        this.input = input;
        fvResponse.setDescription(input.toString());
    }

    public void setSubmittingStatement(STLStatement statement) {
        this.submittingStatement = statement;
    }

    public STLStatement getSubmittingStatement() {
        return submittingStatement;
    }

    public RmppMad prepareMad() {
        throw new UnsupportedOperationException();
    }

}
