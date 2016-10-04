/*
Provides an abstract class with scaffolding panels to assist in gridbag alignment. 
All frames displayed by card manager inherit from the GFrame class. 
 */
package stencilui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import static stencilui.StencilUI.fsize;

/**
 *
 * @author jamesl
 */

//provides scaffolding and alignment panels for gridbay layout testing
abstract public class GFrame extends JPanel {
    public GFrame() {
        this.setLayout(new GridBagLayout()); //all panels use gridbag layout
        this.setBackground(Color.GRAY);
        GridBagConstraints gbconstr_alignment_panels = new GridBagConstraints(); //container holding gridbag components
        
        JPanel h_panel_1 = new JPanel();
        h_panel_1.setBackground(Color.RED);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTHWEST;
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 0; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_1, gbconstr_alignment_panels);

        JPanel h_panel_2 = new JPanel();
        h_panel_2.setBackground(Color.GREEN);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL;  
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 1; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_2, gbconstr_alignment_panels);

        JPanel h_panel_3 = new JPanel();
        h_panel_3.setBackground(Color.RED);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 2; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_3, gbconstr_alignment_panels);

                JPanel h_panel_4 = new JPanel();
        h_panel_4.setBackground(Color.GREEN);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 3; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_4, gbconstr_alignment_panels);

                JPanel h_panel_5 = new JPanel();
        h_panel_5.setBackground(Color.RED);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 4; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_5, gbconstr_alignment_panels);

                JPanel h_panel_6 = new JPanel();
        h_panel_6.setBackground(Color.GREEN);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 5; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_6, gbconstr_alignment_panels);

                JPanel h_panel_7 = new JPanel();
        h_panel_7.setBackground(Color.RED);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 6; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_7, gbconstr_alignment_panels);

        JPanel h_panel_8 = new JPanel();
        h_panel_8.setBackground(Color.GREEN);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 7; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_8, gbconstr_alignment_panels);

        JPanel h_panel_9 = new JPanel();
        h_panel_9.setBackground(Color.RED);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL;  
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 8; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_9, gbconstr_alignment_panels);

        JPanel h_panel_10 = new JPanel();
        h_panel_10.setBackground(Color.GREEN);
        gbconstr_alignment_panels.anchor = GridBagConstraints.NORTH;
        gbconstr_alignment_panels.fill = GridBagConstraints.HORIZONTAL; 
        gbconstr_alignment_panels.weightx = 0.1; 
        gbconstr_alignment_panels.gridx = 9; 
        gbconstr_alignment_panels.gridy = 0; 
        this.add(h_panel_10, gbconstr_alignment_panels);


        // The one vertical panel to prevent item accumulation at center



        JPanel v_panel_2 = new JPanel();
        GridBagConstraints gbconstrain_align_vertical = new GridBagConstraints(); //container holding gridbag components
        v_panel_2.setBackground(Color.GREEN);
        gbconstrain_align_vertical.anchor = GridBagConstraints.NORTHWEST;
        gbconstrain_align_vertical.fill = GridBagConstraints.HORIZONTAL; 
        gbconstrain_align_vertical.gridwidth = 10; 
        gbconstrain_align_vertical.weighty = 0; 
        gbconstrain_align_vertical.gridx = 0; 
        gbconstrain_align_vertical.gridy = 2; 
        this.add(v_panel_2, gbconstrain_align_vertical);

    this.setPreferredSize(fsize);


    }
    

}
