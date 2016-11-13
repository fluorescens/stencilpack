/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stencilui;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import static stencilui.StencilUI.fsize;
/**
 *
 * @author jamesl
 */
public class Frame_2 extends GFrame{
    //takes the current_frame as the constructor and adds components, returning current frame.  
    static JTextArea screenshot_source; //extracts these properties: frame 3 image source and crunchypack location
    static JTextArea crunchypack_source; 
    
    public Frame_2() {
        javax.swing.UIManager.put("OptionPane.font", new Font("defaultFont", Font.BOLD, 30));
        this.setLayout(new GridBagLayout()); //use this layout
        this.setBackground(Color.GRAY);
        GridBagConstraints c = new GridBagConstraints(); //container holding gridbag components

        JLabel local_title = new JLabel("Initial Setup: Get a Screenshot", SwingConstants.CENTER); 
            local_title.setFont(new Font("defaultFont",1, 30));
        JTextArea textarea = new JTextArea(10, 20);
        JScrollPane scroll = new JScrollPane(textarea);

       

            //-------------------------------------------------------VPANEL

            //title bar
            JPanel v_panel_1 = new JPanel();
            GridBagConstraints gb_constrain_titlepanel = new GridBagConstraints(); //container holding gridbag components
            v_panel_1.setBackground(Color.BLUE);
            gb_constrain_titlepanel.anchor = GridBagConstraints.NORTHWEST;
            gb_constrain_titlepanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_titlepanel.weighty = 0.1; 
            gb_constrain_titlepanel.weightx = 1;
            gb_constrain_titlepanel.gridwidth = 10; 
            gb_constrain_titlepanel.gridheight = 1; 
            gb_constrain_titlepanel.gridx = 0; 
            gb_constrain_titlepanel.gridy = 1; 
            gb_constrain_titlepanel.ipady = 10; 
            local_title.setForeground(Color.WHITE);
            v_panel_1.add(local_title); 
            this.add(v_panel_1, gb_constrain_titlepanel);


            
            /*
            
                        JPanel v_panel_2 = new JPanel();
            GridBagConstraints v2 = new GridBagConstraints(); //container holding gridbag components
            v_panel_2.setBackground(Color.GREEN);
            v2.anchor = GridBagConstraints.NORTHWEST;
            v2.fill = GridBagConstraints.HORIZONTAL; 
            v2.gridwidth = 10; 
            v2.gridheight = 1; 
            v2.weighty = 0; 
            v2.weightx = 0.1;
            //v2.ipady = 20; 
            v2.gridx = 0; 
            v2.gridy = 2; 
            this.add(v_panel_2, v2);
            */




        //get the location of the screenshot source 
        JLabel scr_shot = new JLabel("Screenshot Location: "); 
                       scr_shot.setFont(new Font("defaultFont",1, 18));
                       scr_shot.setForeground(Color.GREEN);
        JPanel v_panel_3 = new JPanel();
        v_panel_3.add(scr_shot); 
        GridBagConstraints gb_constrain_screenshotlabel = new GridBagConstraints(); //container holding gridbag components
        v_panel_3.setBackground(Color.GRAY);
        gb_constrain_screenshotlabel.anchor = GridBagConstraints.SOUTH;
        gb_constrain_screenshotlabel.fill = GridBagConstraints.HORIZONTAL; 
        gb_constrain_screenshotlabel.gridwidth = 1; 
        gb_constrain_screenshotlabel.weighty = 0; 
        gb_constrain_screenshotlabel.gridx = 0; 
        gb_constrain_screenshotlabel.gridy = 5; 
        this.add(v_panel_3, gb_constrain_screenshotlabel);
            
       screenshot_source = new JTextArea(); 
                 screenshot_source.setFont(new Font("defaultFont",1, 18));
        JScrollPane sscrrensource_pane = new JScrollPane(screenshot_source); 
        sscrrensource_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gb_constrain_screenshottxt = new GridBagConstraints(); //container holding gridbag components
        gb_constrain_screenshottxt.anchor = GridBagConstraints.SOUTH;
        gb_constrain_screenshottxt.fill = GridBagConstraints.BOTH; 
        gb_constrain_screenshottxt.weighty = 0;
        gb_constrain_screenshottxt.weightx = 1; 
        gb_constrain_screenshottxt.gridwidth = 8; 
        gb_constrain_screenshottxt.gridheight = 1; 
        gb_constrain_screenshottxt.gridx = 1; 
        gb_constrain_screenshottxt.gridy = 5; 
	this.add(sscrrensource_pane, gb_constrain_screenshottxt);
        
        
        //help button about getting a screenshot
        JButton pick_src = new JButton("Getting a Screenshot"); 
                pick_src.setFont(new Font("defaultFont",1, 15));
        GridBagConstraints gb_constrain_getscreen = new GridBagConstraints(); //container holding gridbag components
        gb_constrain_getscreen.anchor = GridBagConstraints.WEST;
        gb_constrain_getscreen.weighty = 0; 
        gb_constrain_getscreen.gridwidth = 2; 
        gb_constrain_getscreen.gridheight = 1; 
        gb_constrain_getscreen.gridx = 1; 
        gb_constrain_getscreen.gridy = 6; 
	this.add(pick_src, gb_constrain_getscreen);
        pick_src.addActionListener(new ActionListener() //exit
        {
          public void actionPerformed(ActionEvent e)
          {
              final String p_title = "Picking a good stencil source:"; 
              final String p_message = "This image will be used to lay out where the watch components will be placed. \n\n"
                      + "You'll be setting points that tell the computer 'Something can occur here. Watch this place'. \n"
                      + "For optimal placement, get a screenshot from the program while its running the way you would normally use it. \n"
                      + "You want to see details like notification areas, chat regions, buttons, or health bars. \n"
                      + "To capture a screenshot on Windows, press Alt+Print Screen while the game window is active (clicked on). \n"
                      + "Once the screenshot is made, paste the complete file path into the dialog box above. \n\n"
                      + "Example: C:\\Users\\Carl\\Pictures\\Screenshots\thisimg.png"; 
              
                    //displays the popup
                    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
                    UIManager.put("OptionPane.messageFont", font); //sets the size of popup fonts. 
                    JFrame popup_parent = new JFrame(); 
                    JOptionPane jpop = new JOptionPane(); 
                    jpop.showOptionDialog(
                                            popup_parent, p_message, p_title, JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE, null, null, null
                                          );
                    popup_parent.setVisible(false);
                    popup_parent.dispose();
          }
        });
        
        //alignment spacer panel
        JPanel v_panel_7 = new JPanel();
        GridBagConstraints v7 = new GridBagConstraints(); //container holding gridbag components
        v_panel_7.setBackground(Color.GRAY);
        v7.anchor = GridBagConstraints.CENTER;
        v7.fill = GridBagConstraints.BOTH; 
        v7.gridwidth = 1; 
        v7.weighty = 0.2; 
        v7.gridx = 0; 
        v7.gridy = 7; 
        this.add(v_panel_7, v7);
        
        
        //get the location of the crunchypack file generated by CrunchyUI
        JLabel scr_crunchypack = new JLabel("Crunchypack File: "); 
                       scr_crunchypack.setFont(new Font("defaultFont",1, 18));
                       scr_crunchypack.setForeground(Color.GREEN); 
        JPanel v_panel_6 = new JPanel();
        v_panel_6.add(scr_crunchypack); 
        GridBagConstraints gb_constrain_cpackpanel = new GridBagConstraints(); //container holding gridbag components
        v_panel_6.setBackground(Color.GRAY);
        gb_constrain_cpackpanel.anchor = GridBagConstraints.WEST;
        gb_constrain_cpackpanel.fill = GridBagConstraints.HORIZONTAL; 
        gb_constrain_cpackpanel.gridwidth = 1; 
        gb_constrain_cpackpanel.weighty = 0; 
        gb_constrain_cpackpanel.gridx = 0; 
        gb_constrain_cpackpanel.gridy = 8; 
        this.add(v_panel_6, gb_constrain_cpackpanel);

        crunchypack_source = new JTextArea(); 
                  crunchypack_source.setFont(new Font("defaultFont",1, 18));
                  crunchypack_source.setBackground(Color.WHITE);
         JScrollPane cpacksource_pane = new JScrollPane(crunchypack_source); 
         cpacksource_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
         GridBagConstraints gb_contrain_cpacktext = new GridBagConstraints(); //container holding gridbag components
         gb_contrain_cpacktext.anchor = GridBagConstraints.SOUTH;
         gb_contrain_cpacktext.fill = GridBagConstraints.BOTH; 
         gb_contrain_cpacktext.weighty = 0; 
         gb_contrain_cpacktext.gridwidth = 8; 
         gb_contrain_cpacktext.gridheight = 1; 
         gb_contrain_cpacktext.gridx = 1; 
         gb_contrain_cpacktext.gridy = 8; 
         this.add(cpacksource_pane, gb_contrain_cpacktext);

         //dummy alignment panel. 
        JPanel v_panel_5 = new JPanel(); 
        GridBagConstraints v5 = new GridBagConstraints(); //container holding gridbag components
        v_panel_5.setBackground(Color.BLUE);
        v5.anchor = GridBagConstraints.SOUTH;
        v5.fill = GridBagConstraints.HORIZONTAL; 
        v5.gridwidth = 10; 
        v5.weighty = 0.1; 
        v5.gridx = 0; 
        v5.gridy = 10; 
        this.add(v_panel_5, v5);



        //size the frame to fit inside the card frame
        this.setPreferredSize(fsize);
    }
}
