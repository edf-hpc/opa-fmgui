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
 *  File Name: FVBaseView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:48  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 01:15:24  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/09 12:54:07  fernande
 *  Archive Log:    Added support for PropertyChange events bubbling up from an SwingWorker, thru an AbstractTask into the controller.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/14 17:38:32  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 21:48:13  fernande
 *  Archive Log:    Changes to framework to support model update lifecycle by notifying views of changes
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 13:31:46  fernande
 *  Archive Log:    Moving MVC framework to its own package and renaming for consistency
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:27:15  fernande
 *  Archive Log:    Basic MVC framework with SwingWorker support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.framework;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class AbstractView<M extends AbstractModel, C extends IController>
        extends JPanel implements IView<M, C>, IModelListener<M> {

    private static final long serialVersionUID = 1L;

    protected C controller;

    protected JComponent mainComponent;

    @Override
    public C getController() {
        return controller;
    }

    @Override
    public void setController(C controller) {
        this.controller = controller;
    }

    @Override
    public abstract void modelUpdateFailed(M model, Throwable caught);

    @Override
    public abstract void modelChanged(M model);

    @Override
    public Component add(Component comp) {
        return mainComponent.add(comp);
    }

    @Override
    public Component add(Component comp, int index) {
        return mainComponent.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        mainComponent.add(comp, constraints);
    }

    /**
     * Only the mainComponent should be laid out on this component; we override
     * the add(Component comp) methods to make sure that no extending class
     * inadvertently adds a component. Extenders should add components to the
     * mainComponent.
     */
    @Override
    public void initView() {
        this.mainComponent = getMainComponent();
        setLayout(new BorderLayout());
        super.add(mainComponent);
        initComponents();
    }

    /**
     * Defines the main component for this widget; this component is added to
     * the view and occupies all the screen area of the view (see initView())
     */
    public abstract JComponent getMainComponent();

    /**
     * Initializes all the components on the main component of this widget
     */
    public abstract void initComponents();

}
