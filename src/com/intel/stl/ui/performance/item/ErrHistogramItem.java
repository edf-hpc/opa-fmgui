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
 *  File Name: ErrHistogramItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/08/12 15:26:55  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/17 23:22:14  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.item;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.ErrorBucket;

public abstract class ErrHistogramItem extends AbstractPerformanceItem {
    protected DefaultCategoryDataset dataset;

    /**
     * Description:
     * 
     * @param name
     * @param fullName
     */
    public ErrHistogramItem(String fullName) {
        super(STLConstants.K0079_HISTOGRAM.getValue(), fullName);
        initDataProvider();
        initDataset();
    }

    protected void initDataset() {
        dataset = createHistogramDataset();
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    private DefaultCategoryDataset createHistogramDataset() {
        return new DefaultCategoryDataset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.item.IPerformanceItem#clear()
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (dataset != null) {
                    dataset.clear();
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#getDataset()
     */
    @Override
    protected Dataset getDataset() {
        return dataset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#isJumpable()
     */
    @Override
    protected boolean isJumpable() {
        return false;
    }

    public void updateHistogram(final int[] values) {
        if (dataset == null || values == null || values.length <= 1) {
            return;
        }

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                dataset.setNotify(false);
                dataset.clear();
                for (int i = 0; i < values.length; i++) {
                    dataset.addValue(values[i], getName(),
                            ErrorBucket.values()[i].getName());
                }
                dataset.setNotify(true);
            }

        });
    }
}
