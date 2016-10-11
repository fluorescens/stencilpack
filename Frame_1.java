/*
First frame to be displayed by card layout manager. 
Presents a textbox that includes information about using the program and the files it requires. 
 */

package stencilui;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import static stencilui.StencilUI.fsize;
/**
 *
 * @author jamesl
 */
public class Frame_1 extends GFrame{
    public volatile boolean frameflag = false; //the flag that a frame update event has occured.  
    final private String advisory_data = "this\n\nshould\n\n\n\nscroll\n\n\nthis is the bottom"; 
        //the dialogue presented in the initial textarea about program use, features, and required files. 
    
    public Frame_1() {
        //one of the unique frames, displays the title of the frame in a large JLabel, 
        JLabel local_title = new JLabel("Building a Stencilpack", SwingConstants.CENTER); 
               local_title.setFont(new Font("Courier",1, 30));
        JPanel v_panel_1 = new JPanel();
        GridBagConstraints gb_constrain_titlepanel = new GridBagConstraints(); //container holding gridbag components
        v_panel_1.setBackground(Color.BLUE);
        gb_constrain_titlepanel.anchor = GridBagConstraints.NORTHWEST;
        gb_constrain_titlepanel.fill = GridBagConstraints.HORIZONTAL; 
        gb_constrain_titlepanel.weighty = 0; 
        gb_constrain_titlepanel.gridwidth = 10; 
        gb_constrain_titlepanel.gridheight = 1; 
        gb_constrain_titlepanel.ipady = 10; 
        gb_constrain_titlepanel.gridx = 0; 
        gb_constrain_titlepanel.gridy = 1; 
        local_title.setForeground(Color.WHITE);
        v_panel_1.add(local_title);  
        this.add(v_panel_1, gb_constrain_titlepanel);

        
        
        //special frame that displays the initial advisory_data dialogue in textarea
        JTextArea textarea = new JTextArea(10, 20);
        JScrollPane scroll = new JScrollPane(textarea);
        textarea.append(advisory_data);
        textarea.setBackground(Color.WHITE);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setEditable(false);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setViewportView(textarea);
        scroll.setWheelScrollingEnabled(true);
        GridBagConstraints gb_constrain_splashtext = new GridBagConstraints(); //container holding gridbag components
        gb_constrain_splashtext.anchor = GridBagConstraints.NORTH; 
        gb_constrain_splashtext.fill = GridBagConstraints.BOTH;
        gb_constrain_splashtext.weightx = 1; 
        gb_constrain_splashtext.weighty = 1; 
        gb_constrain_splashtext.gridwidth = 6; 
        gb_constrain_splashtext.gridheight = 5; 
        gb_constrain_splashtext.ipady = 100; 
        gb_constrain_splashtext.gridx = 2; 
        gb_constrain_splashtext.gridy = 3; 
        this.add(scroll, gb_constrain_splashtext); 
    }
}
