//Generated by GuiGenie - Copyright (c) 2004 Mario Awad.
//Home Page http://guigenie.cjb.net - Check often for new versions!

package com.manicken;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SettingsDialog extends JPanel {
    public JTextField txtRetryCount;
    private JLabel jcomp2;

    public SettingsDialog() {
        //construct components
        txtRetryCount = new JTextField (5);
        jcomp2 = new JLabel ("upload retry count");

        //adjust size and set layout
        setPreferredSize (new Dimension (228, 38));
        setLayout (null);

        //add components
        add (txtRetryCount);
        add (jcomp2);

        //set component bounds (only needed by Absolute Positioning)
        txtRetryCount.setBounds (120, 5, 100, 25);
        jcomp2.setBounds (10, 5, 115, 25);
    }

}
