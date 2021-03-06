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
 *  File Name: IPerfSubpageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:12  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/21 16:32:25  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/02 19:24:34  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/27 22:22:21  jijunwan
 *  Archive Log:    added running indicator to Performance Subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/01 15:42:22  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Extension of the IPageController interface for performance
 *  subpage controllers
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.common;

import com.intel.stl.ui.monitor.PerformanceTreeController;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public interface IPerfSubpageController extends IPageController {

    void showNode(FVResourceNode node, IProgressObserver observer);

    void setParentController(PerformanceTreeController parentController);
}
