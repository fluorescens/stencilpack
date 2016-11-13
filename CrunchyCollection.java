/*
Used by Frame 3 as a tabbed display listing available image codes, currently active watchobjects with their codes, and identity and 
action scripts that have been parsed and added. Automatically updates as new objects and scripts are added. 
 */
package stencilui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; 

/**
 *
 * @author jamesl
 */
public class CrunchyCollection extends JPanel{
        
        String mock_text = ""; //filler text for debugging purposes
        ArrayList<JTextArea> panel_text_source; //adds the four tabs that will make up this "folder"
        
        //called with arrays of group and image names
        public CrunchyCollection(ArrayList<String> group_name_data, ArrayList<ArrayList<String>> image_name_data) { 
            this.setLayout( new GridBagLayout()); //set the layout
            this.setBackground(Color.lightGray); 
               
            panel_text_source = new ArrayList<>(); //initialize source
            panel_text_source.ensureCapacity(4); //contains 4 panels
            String group_data = prepare_groupstring(group_name_data); //package the group and item name array as a string
            String item_data = prepare_itemstring(image_name_data); 
                
            //create the tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setFont(new Font("Courier",1, 15));
                GridBagConstraints grid_tab = new GridBagConstraints(); 
                grid_tab.weightx = 1; 
                grid_tab.weighty = 1; 
                grid_tab.fill = GridBagConstraints.BOTH; 
                grid_tab.anchor = GridBagConstraints.NORTHWEST; 
                this.add(tabbedPane, grid_tab); 

            //populate and add the tabs
            JComponent group_tab = makeTextPanel(group_data);
            tabbedPane.addTab("Available groups", null, group_tab, "Tooltip placeholder");
            tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

            JComponent object_tab = makeTextPanel(item_data);
            tabbedPane.addTab("Available items", null, object_tab, "Tooltip placeholder");
            tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

            JComponent trigger_tab = makeTextPanel("None"); 
            tabbedPane.addTab("Identity Scripts", null, trigger_tab, "Tooltip placeholder");
            tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

            JPanel action_tab = makeTextPanel(mock_text); 
            tabbedPane.addTab("Action Scripts", null, action_tab, "Tooltip placeholder");
            tabbedPane.setMnemonicAt(3, KeyEvent.VK_3);
            
            //The following line enables to use scrolling tabs.
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }
        
    //creates the panel that is used to as the display area of the tab
    protected JPanel makeTextPanel(String text) {
        //contains the tabbed panel text area object, created later
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridBagLayout());
        
        //this panel is "pushed" onto the back of the tabbed panel array
        int panel_number = panel_text_source.size();
        JTextArea display_saved =  new JTextArea(text); 
        panel_text_source.add(panel_number, display_saved); //add this area as panel number panel_number
        
        //user cannot edit the tabs manually, only via object manipulation
        display_saved.setEditable(false);
        display_saved.setFont(new Font("Courier",1, 15)); //good font size
        JScrollPane container = new JScrollPane(); 
        container.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        container.add(display_saved); //add the text area to a scrollpane
        container.setViewportView(display_saved);
        
        GridBagConstraints gb_constrain_tabpanel = new GridBagConstraints();  
        gb_constrain_tabpanel.weightx = 1; 
        gb_constrain_tabpanel.weighty = 1; 
        gb_constrain_tabpanel.fill = GridBagConstraints.BOTH; 
        panel.add(container, gb_constrain_tabpanel); //add the scrollpane to the jpanel
        return panel;
    }
    
    //prepares the initial string of group data for the group tab. Initialized once, never updates
    //BUILD: Replace with stringbuilder, low priority
    private String prepare_groupstring(ArrayList<String> group_sources) {
        String ava_groups = "";
        for(int i = 0; i < group_sources.size(); ++i) {
            ava_groups += Integer.toString(i) + ": " + group_sources.get(i) + '\n'; 
        }
        return ava_groups; 
    }
    
    //prepares the initial string of image data for the image tab. Initialized once, tab never changes
    //BUILD replace with stringbuilder, low priority
    private String prepare_itemstring(ArrayList<ArrayList<String>> image_sources) {
        String ava_items = "";
        for(int i = 0; i < image_sources.size(); ++i) {
            ava_items += Integer.toString(i) + ":"; 
            for(int k = 0; k < image_sources.get(i).size(); ++k) {
                ava_items += "\t(" + Integer.toString(i) + "." + Integer.toString(k) + ") "+ image_sources.get(i).get(k) + '\n';
            } 
            ava_items += "\n\n"; 
        }
        return ava_items; 
    }
    
    //Upon adding a new object trigger script, fire an update to the tab
    //Recieves the updated string and updates its corrosponding tab text
    public void update_identify_script(String scr_udp) {
        panel_text_source.get(2).setText(scr_udp);
    }
    
    //Upon adding a new action script, fire an update to the tab
    //Recieves the updated string and updates its corrosponding tab text
    public void update_action_script(String scr_udp) {
        panel_text_source.get(3).setText(scr_udp);
    }
}

