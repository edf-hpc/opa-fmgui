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
 *  File Name: HoqLifeRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:52  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/30 14:25:37  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 15:47:46  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.virtualfabrics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import com.intel.stl.api.management.virtualfabrics.HoqLife;
import com.intel.stl.api.management.virtualfabrics.HoqLife.TimeOut;
import com.intel.stl.api.management.virtualfabrics.HoqLife.TimeOut.Unit;
import com.intel.stl.ui.admin.view.AbstractAttrRenderer;
import com.intel.stl.ui.common.view.IntelComboBoxUI;

public class HoqLifeRenderer extends AbstractAttrRenderer<HoqLife> {
    private final JPanel panel;

    private final JFormattedTextField field;

    private final JComboBox<Unit> unitBox;

    public HoqLifeRenderer() {
        super();
        panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        field = new JFormattedTextField(new DecimalFormat("###"));
        field.setValue(new Integer(0));
        panel.add(field, BorderLayout.CENTER);
        unitBox = new JComboBox<Unit>(Unit.values());
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
                return ((Unit) value).name().toLowerCase();
            }

        };
        unitBox.setUI(ui);
        unitBox.setPreferredSize(new Dimension(50, 20));
        panel.add(unitBox, BorderLayout.EAST);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setAttr(com.intel.stl.api.
     * management.IAttribute)
     */
    @Override
    public void setAttr(HoqLife attr) {
        TimeOut to = attr.getObject();
        field.setValue(to.getValue());
        unitBox.setSelectedItem(to.getUnit());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean isEditable) {
        field.setEditable(isEditable);
        unitBox.setEnabled(isEditable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getAttr()
     */
    @Override
    public HoqLife getAttr() {
        int value = ((Number) field.getValue()).intValue();
        Unit unit = (Unit) unitBox.getSelectedItem();
        return new HoqLife(new TimeOut(value, unit));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.AbstractAttrRenderer#getFields()
     */
    @Override
    protected Component[] getFields() {
        return new Component[] { panel };
    }

}
