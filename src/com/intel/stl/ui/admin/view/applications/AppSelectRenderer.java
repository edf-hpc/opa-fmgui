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
 *  File Name: SelectRender.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:47  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/30 14:25:35  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:08:13  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/13 20:58:22  jijunwan
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:17  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.applications;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.intel.stl.api.management.applications.AppSelect;
import com.intel.stl.ui.admin.view.IAttrRenderer;
import com.intel.stl.ui.common.view.IntelComboBoxUI;

public class AppSelectRenderer implements IAttrRenderer<AppSelect> {
    private final JComboBox<AppSelect> list;

    public AppSelectRenderer() {
        list = new JComboBox<AppSelect>(AppSelect.values());
        IntelComboBoxUI ui = new IntelComboBoxUI() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.stl.ui.common.view.IntelComboBoxUI#getValueString(java
             * .lang.Object)
             */
            @Override
            protected String getValueString(Object value) {
                AppSelect sel = (AppSelect) value;
                return sel.getName();
            }

        };
        list.setUI(ui);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setAttr(java.lang.Object)
     */
    @Override
    public void setAttr(AppSelect attr) {
        list.setSelectedItem(attr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean isEditable) {
        list.setEnabled(isEditable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getAttr()
     */
    @Override
    public AppSelect getAttr() {
        return (AppSelect) list.getSelectedItem();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getView()
     */
    @Override
    public JComponent getView() {
        return list;
    }

}
