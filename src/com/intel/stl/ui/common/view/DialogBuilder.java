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
 *  File Name: DialogBuilder.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.2  2015/08/12 15:26:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5.2.1  2015/05/06 19:40:10  jijunwan
 *  Archive Log:    improvement on error dialog to show parent's title, handle special case etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/05/05 18:29:46  jijunwan
 *  Archive Log:    improvement to avoid potential sync issue
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/05/04 21:14:59  jijunwan
 *  Archive Log:    Fixed the very strange case where when we have massive error messages, sometime we get a dialog that can not respond to mouse input and but respond to keyboard. The fixes include
 *  Archive Log:    1) remove  #setAlwaysOnTop
 *  Archive Log:    2) create a new dialog after it's closed either by OK button by the win close button
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/05/01 21:29:12  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/30 18:11:45  jijunwan
 *  Archive Log:    improved to handle the case that an error dialog pops up when we have a opened setup wizard. The current solution is that
 *  Archive Log:    1) show error dialog on top of setup wizard, so a user knows the errors promptly
 *  Archive Log:    2) since the error dialog is on top, set it's modal type to Application, so the user must respond the error dialog rathe then the setup dialog under it
 *  Archive Log:    3) set error dialog's modal type back to Document when there is no opened setup wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/29 22:05:28  jijunwan
 *  Archive Log:    1) show parent's title on error dialog
 *  Archive Log:    2) add Intel log icon on title bar when no parent frame
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 21:59:29  jijunwan
 *  Archive Log:    minor improvement to be able to 1) recenter the dialog, 2) clear text after a user clicked OK button, 3) remove last '\n' in text area
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/22 22:31:55  fisherma
 *  Archive Log:    Removing html tags from error messages.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/21 20:07:24  fisherma
 *  Archive Log:    Updated dialog's appearance - centered relatively to parent if there is a parent.  Set the size of the scroll bar to 10 pixels - nicer on windows.   Updated layout.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/18 01:40:01  fisherma
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss.  The code changes address issue #2 reported in the bug.  Adding common dialog to display errors.  Needs further appearance improvements.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fisherma
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.main.view.FVMainFrame;

public class DialogBuilder {
    private String title;

    private JDialog dialog;

    private JPanel buttonsPanel;

    private JTextArea text;

    private JScrollPane jspane;

    private JLabel dialogIconLbl;

    private final Color bgColor = UIConstants.INTEL_WHITE;

    private JButton okBtn, cancelBtn;

    private int btnPressed = 0;

    private ModalityType desiredType;

    //
    // Constructor to create dialog with one button
    //
    public DialogBuilder(java.awt.Component owner, String title, boolean modal,
            String btn0) {
        this(owner, title, modal, btn0, null);
    }

    // This is constructor for a modeless dialog
    // which should always be shown on top of all other windows.
    public DialogBuilder(String btn0) {
        dialog = new JDialog();

        createButtonsPanel(btn0, null);
        initComponents();

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModalityType(java.awt.Dialog.ModalityType.MODELESS);
        dialog.setIconImage(UIImages.LOGO_24.getImage());
        dialog.setAlwaysOnTop(true);
        dialog.setPreferredSize(new Dimension(500, 300));
        dialog.pack();
    }

    public DialogBuilder(java.awt.Component owner, String title, boolean modal,
            String btn0, String btn1) {
        this.title = title;

        // Figure out the parent.
        if (owner instanceof JFrame) {
            dialog = new JDialog((JFrame) owner, title, modal);
        } else if (owner instanceof JDialog) {
            dialog = new JDialog((JDialog) owner, title, modal);
        } else {
            dialog = new JDialog();
            dialog.setTitle(title);
            dialog.setModal(modal);
            dialog.setIconImage(UIImages.LOGO_24.getImage());
            System.out.println("PARENT IS NEITHER FRAME NOR DIALOG: " + owner);
        }

        createButtonsPanel(btn0, btn1);
        initComponents();

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        // TODO: think about modality type - important for multi-subnet view
        // scanario
        // APPLICATION_MODAL
        // DOCUMENT_MODAL
        // MODELESS
        // TOOLKIT_MODAL
        dialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        dialog.setPreferredSize(new Dimension(500, 200));
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
    }

    public void initComponents() {

        Container container = dialog.getContentPane();
        container.setBackground(bgColor);
        container.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        dialogIconLbl = new JLabel();
        gc.insets = new Insets(20, 20, 20, 20);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.NORTHWEST;
        container.add(dialogIconLbl, gc);

        jspane =
                new JScrollPane(getTextArea(),
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jspane.setBorder(BorderFactory.createEmptyBorder());
        jspane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        JViewport viewport = jspane.getViewport();
        viewport.setScrollMode(JViewport.BLIT_SCROLL_MODE);
        gc.insets = new Insets(20, 0, 20, 10);
        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.BOTH;
        container.add(jspane, gc);

        gc.insets = new Insets(0, 0, 5, 5);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        container.add(buttonsPanel, gc);

    }

    protected void createButtonsPanel(String btn0, String btn1) {
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder());
        buttonsPanel.setOpaque(false);

        if (btn0 != null) {
            // this is ok button
            okBtn = ComponentFactory.getIntelActionButton(btn0);
            okBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // clear text
                    text.setText(null);
                    dialog.dispose();
                }
            });
            buttonsPanel.add(okBtn);
        }

        if (btn1 != null) {
            // this is cancel button
            cancelBtn = ComponentFactory.getIntelCancelButton(btn1);
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelAction();
                }
            });
            buttonsPanel.add(cancelBtn);
        }

        // If there are two buttons, make them same width.
        if (btn0 != null && btn1 != null) {
            JButton btnGroup[] = { okBtn, cancelBtn };
            ComponentFactory.makeSameWidthButtons(btnGroup);
        }

    }

    public void show() {
        refresh();
        // when there is an APPLICATION_MODAL dialog, we want our user
        // respond to this dialog first.
        if (desiredType == null) {
            desiredType = dialog.getModalityType();
        }
        Dialog appDlg = getAppModalDlg();
        boolean intersects =
                appDlg != null
                        && appDlg.getBounds().intersects(dialog.getBounds());
        if (intersects) {
            dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        } else if (dialog.getModalityType() != desiredType) {
            dialog.setModalityType(desiredType);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                okBtn.requestFocus();
            }
        });

        if (!dialog.isVisible()) {
            dialog.setVisible(true);
        } else {
            // Should we actually do it??? Might be disruptive if it gains
            // focus!!!
            dialog.toFront();
        }
    }

    /**
     * 
     * <i>Description:</i> parent may move or has changed name, this method
     * ensure we recent to parent's current location, and have updated title
     * 
     */
    protected void refresh() {
        Container parent = dialog.getParent();
        if (parent != null) {
            String prefix = null;
            if (parent instanceof FVMainFrame) {
                prefix = ((FVMainFrame) parent).getSubnetName();
            } else if (parent instanceof Frame) {
                prefix = ((Frame) parent).getTitle();
            } else if (parent instanceof Dialog) {
                prefix = ((Dialog) parent).getTitle();
            }
            if (prefix != null) {
                dialog.setTitle(prefix + " " + title);
            }
        }
        dialog.setLocationRelativeTo(parent);
    }

    /**
     * 
     * <i>Description:</i> Detect whether there is an APPLICATION_MODAL dialog,
     * for example, whether setup wizard is running.
     * 
     * @return
     */
    protected Dialog getAppModalDlg() {
        Window[] wins = Window.getWindows();
        for (Window win : wins) {
            if (win.isVisible() && win instanceof Dialog) {
                Dialog dlg = (Dialog) win;
                ModalityType type = dlg.getModalityType();
                if (type == ModalityType.APPLICATION_MODAL) {
                    return dlg;
                }
            }
        }
        return null;
    }

    public int getButtonPressed() {
        return btnPressed;
    }

    public void cancelAction() {
        // User pressed cancel button
        btnPressed = 1;
        dialog.setVisible(false);
    }

    private JTextArea getTextArea() {
        text = new JTextArea();
        text.setEditable(false);
        PlainDocument plaindocument = new PlainDocument();
        text.setDocument(plaindocument);
        text.revalidate();
        text.setMargin(new Insets(0, 10, 0, 0));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        text.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        text.setRows(0);

        return text;

    }

    public void appendText(String str) {
        if (text.getText().isEmpty()) {
            text.append(str);
        } else {
            text.append("\n" + str);
        }
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }

    public JDialog getDialog() {
        return dialog;
    }

    public void setImageIcon(ImageIcon dlgIcon) {
        dialogIconLbl.setIcon(dlgIcon);
    }

    public void setText(String str) {
        text.setText(str);
    }

}
