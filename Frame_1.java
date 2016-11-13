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
    final private String advisory_data = "Welcome to Crunchypack: Stencil Builder\n\n"
            + "This program takes a screenshot of the program you want to lay a stencil over and a complete crunchypack "
            + "of image information. It allows a user to draw watchobjects over the stencil which is where the main"
            + " executable program will look for certain subimages.\n\n"
            + "Each of these watchobjects can be assigned logic in the form of object trigger scripts written in "
            + "\"crunchylang\", a scripting language created for this program to deal with image matching and evaluation."
            + " The full documentation for the language is available on the repository site, but the essentials;"
            + "\n\t-Watchpoint: Evaluates if a single pixel point is a match."
            + "\n\t-Watchregion: Evaluates if any point in the region(width,length) is a match."
            + "\n\t-Overwatch: Places a watchpoint at every Nth point in the bounds"
            + "\n\nTrigger scripts can evaluate complex logic for if the image within the watchobject is a match for"
            + " any image prepared by the crunchypack/imgreduce. If a script evaluates to true, it executes an action script."
            + " An action script is composed of a numerical priority (lowest number is highest priority) and a string argument."
            + "\n\nAn object trigger tests if each logical condition leading up to an action trigger call is true. If the path ALL "
            + "evaluates to true, the action trigger is enqueue'd by the main executable. A false statement causes the object trigger "
            + "interpreter to skip ahead to the next argument and not enqueue the action script. EVERY preceeding condition "
            + "for a single script must be true in order to enqueue the script.\n\n"
            + "This program comtains verification capabilities to assure that trigger scripts are validated before they are attached."
            + "The functions contain:"
            + "\n\t-Syntatical correctness"
            + "\n\t-Object existance verification for index calls"
            + "\n\t-Dependency checking for GET calls."
            + "\n\nWhen the priority queue in the main executable is emptied at the end of each processing cycle, it will"
            + " push the string arguments of all true enqueue'd scripts in their priority order. These strings can be "
            + "pipelined to other programs to do tasks like create log files, increment counters, or inject keystrokes."; 
        //the dialogue presented in the initial textarea about program use, features, and required files. 
    
    public Frame_1() {
        //one of the unique frames, displays the title of the frame in a large JLabel, 
        JLabel local_title = new JLabel("Building a Stencilpack", SwingConstants.CENTER); 
               local_title.setFont(new Font("defaultFont",1, 30));
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
        textarea.setFont(textarea.getFont().deriveFont(20f)); 
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
