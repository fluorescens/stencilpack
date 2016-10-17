/*
This frame holds the scaled screenshot and allows the user to assign watch objects by drawing them on the location.
This frame also hosts the interface for the built-in syntatic parser that allows for using the crunchylang scripting language
to write object trigger code for the watch objects. 
This frame also holds the built-in syntatic parser / interpreter for the action script writer which allows watch objects to trigger an
output or action once a chain of their conditions has evaluated to true. 
The action scripts support assignment priority. 
 */
package stencilui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList; 
import java.util.ArrayDeque; 
import java.awt.Dimension;
import javax.swing.JPanel; 

/**
 *
 * @author jamesl
 */

//Frame 3 is NOT a Gframe style frame. Its layout is unique and should be entirely self contained. 
public class Frame_3 extends JPanel {
    ArrayList<String> group_names; //the available groups
    ArrayList<ArrayList<String>> image_names; //the available image names
    final int WATCHPOINT_STATE = 1; //the 3 constant states used for drawing objects
    final int WATCHREGION_STATE = 2; 
    final int OVERWATCH_STATE = 3; 
    IdentifyParser parser; //the syntatic parser for verifying object trigger code
    ActionParser actionparse; //the syntatic parser for verifying action script code
    final DrawingFrame drawframe; //the frame object which holds the screenshot 
    
    public Frame_3(final String fpath, final ArrayList<String> gnames, final ArrayList<ArrayList<String>> inames) {
        //initialized with path to screenshot source file, and the arraylists of group and item names. 
        UIManager.put("OptionPane.messageFont", new Font("System", Font.PLAIN, 20));
        group_names = gnames; //available group names for object trigger writing use
        image_names = inames; //available image names for object trigger
        parser = new IdentifyParser(image_names); //the syntatic parser for object trigger code
        actionparse = new ActionParser(); //the syntatic parser for action script
        this.setLayout( new GridBagLayout());
        this.setBackground(Color.lightGray); 

        //---------------------------------------------------------------ALIGNMENT PANELS. 
                    GridBagConstraints gb_constrain_alignpanel = new GridBagConstraints();
                    JPanel h_panel_1 = new JPanel();
                    h_panel_1.setBackground(Color.RED);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTHWEST;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 0; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_1, gb_constrain_alignpanel);

                    JPanel h_panel_2 = new JPanel();
                    h_panel_2.setBackground(Color.GREEN);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL;  
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 1; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_2, gb_constrain_alignpanel);

                    JPanel h_panel_3 = new JPanel();
                    h_panel_3.setBackground(Color.RED);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 2; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_3, gb_constrain_alignpanel);

                            JPanel h_panel_4 = new JPanel();
                    h_panel_4.setBackground(Color.GREEN);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 3; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_4, gb_constrain_alignpanel);

                            JPanel h_panel_5 = new JPanel();
                    h_panel_5.setBackground(Color.RED);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 4; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_5, gb_constrain_alignpanel);

                            JPanel h_panel_6 = new JPanel();
                    h_panel_6.setBackground(Color.GREEN);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 5; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_6, gb_constrain_alignpanel);

                            JPanel h_panel_7 = new JPanel();
                    h_panel_7.setBackground(Color.RED);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 6; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_7, gb_constrain_alignpanel);

                    JPanel h_panel_8 = new JPanel();
                    h_panel_8.setBackground(Color.GREEN);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 7; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_8, gb_constrain_alignpanel);

                    JPanel h_panel_9 = new JPanel();
                    h_panel_9.setBackground(Color.RED);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL;  
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 8; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_9, gb_constrain_alignpanel);

                    JPanel h_panel_10 = new JPanel();
                    h_panel_10.setBackground(Color.GREEN);
                    gb_constrain_alignpanel.anchor = GridBagConstraints.NORTH;
                    gb_constrain_alignpanel.fill = GridBagConstraints.HORIZONTAL; 
                    gb_constrain_alignpanel.weightx = 0.1; 
                    gb_constrain_alignpanel.gridx = 9; 
                    gb_constrain_alignpanel.gridy = 0; 
                    this.add(h_panel_10, gb_constrain_alignpanel);



                    //------------------------------------------------------ Vertical scaffolding
                    GridBagConstraints gb_constrain_verticalalign = new GridBagConstraints();
                    JPanel h_panel_11 = new JPanel();
                    h_panel_1.setBackground(Color.RED);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.NORTHWEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 1; 
                    this.add(h_panel_1, gb_constrain_verticalalign);

                    JPanel h_panel_12 = new JPanel();
                    h_panel_12.setBackground(Color.GREEN);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 2; 
                    this.add(h_panel_12, gb_constrain_verticalalign);

                    JPanel h_panel_13 = new JPanel();
                    h_panel_13.setBackground(Color.RED);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 3; 
                    this.add(h_panel_13, gb_constrain_verticalalign);

                    JPanel h_panel_14 = new JPanel();
                    h_panel_14.setBackground(Color.GREEN);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 4; 
                    this.add(h_panel_14, gb_constrain_verticalalign);

                    JPanel h_panel_15 = new JPanel();
                    h_panel_15.setBackground(Color.RED);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 5; 
                    this.add(h_panel_15, gb_constrain_verticalalign);

                    JPanel h_panel_16 = new JPanel();
                    h_panel_16.setBackground(Color.GREEN);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 6; 
                    this.add(h_panel_16, gb_constrain_verticalalign);

                    JPanel h_panel_17 = new JPanel();
                    h_panel_17.setBackground(Color.RED);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 7; 
                    this.add(h_panel_17, gb_constrain_verticalalign);

                    JPanel h_panel_18 = new JPanel();
                    h_panel_18.setBackground(Color.GREEN);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 8; 
                    this.add(h_panel_18, gb_constrain_verticalalign);

                    JPanel h_panel_19 = new JPanel();
                    h_panel_19.setBackground(Color.RED);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 9; 
                    this.add(h_panel_19, gb_constrain_verticalalign);

                    JPanel h_panel_20 = new JPanel();
                    h_panel_20.setBackground(Color.GREEN);
                    gb_constrain_verticalalign.anchor = GridBagConstraints.WEST;
                    gb_constrain_verticalalign.fill = GridBagConstraints.VERTICAL; 
                    gb_constrain_verticalalign.weighty = 0.1; 
                    gb_constrain_verticalalign.gridx = 0; 
                    gb_constrain_verticalalign.gridy = 10; 
                    this.add(h_panel_20, gb_constrain_verticalalign);
            
            //--------------------------------------------------------------End dummy scaffolding panel
          
            //the frame that contains the screenshot and allows drawing on the frame. 
            DrawingFrame draw = new DrawingFrame(fpath); 
            drawframe = draw; 
            GridBagConstraints gb_constrain_drawpanel = new GridBagConstraints(); 
            gb_constrain_drawpanel.anchor = GridBagConstraints.CENTER; 
            gb_constrain_drawpanel.fill = GridBagConstraints.NONE; 
            gb_constrain_drawpanel.weightx = 0; 
            gb_constrain_drawpanel.weighty = 0; 
            gb_constrain_drawpanel.gridheight = 6; 
            gb_constrain_drawpanel.gridwidth = 3; 
            gb_constrain_drawpanel.gridx = 1; 
            gb_constrain_drawpanel.gridy = 4; 
            this.add(draw, gb_constrain_drawpanel);
            

            //select a drawing operation. 
            JLabel point = new JLabel("Select: "); 
                point.setFont(new Font("Courier",1, 20));
            GridBagConstraints gb_constrain_toplevel = new GridBagConstraints(); 
            gb_constrain_toplevel.anchor = GridBagConstraints.CENTER; 
            gb_constrain_toplevel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_toplevel.weightx = 0; 
            gb_constrain_toplevel.weighty = 0; 
            gb_constrain_toplevel.gridheight = 1; 
            gb_constrain_toplevel.gridwidth = 1; 
            gb_constrain_toplevel.gridx = 4; 
            gb_constrain_toplevel.gridy = 2; 
            this.add(point, gb_constrain_toplevel);
            
            //text are that displays the objects identifying number
            JTextArea object_number = new JTextArea(); 
                object_number.setFont(new Font("Courier",1, 25));
                object_number.setEditable(false);
            GridBagConstraints n_control = new GridBagConstraints(); 
            n_control.anchor = GridBagConstraints.WEST; 
            n_control.fill = GridBagConstraints.BOTH; 
            n_control.weightx = 0; 
            n_control.weighty = 0; 
            n_control.gridheight = 1; 
            n_control.gridwidth = 2; 
            n_control.gridx = 5; 
            n_control.gridy = 2; 
            this.add(object_number, n_control);
            
            //Selectors for which object to draw. Sets the draw state to object value
            JRadioButton wpbutton = new JRadioButton("Watchpoint");
            wpbutton.setMnemonic(KeyEvent.VK_B);
            wpbutton.setActionCommand("Watchpoint");
                wpbutton.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_radiowp = new GridBagConstraints(); 
            gb_constrain_radiowp.anchor = GridBagConstraints.WEST; 
            gb_constrain_radiowp.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_radiowp.weightx = 0; 
            gb_constrain_radiowp.weighty = 0; 
            gb_constrain_radiowp.gridheight = 1; 
            gb_constrain_radiowp.gridwidth = 1; 
            gb_constrain_radiowp.gridx = 5; 
            gb_constrain_radiowp.gridy = 1; 
            this.add(wpbutton, gb_constrain_radiowp);
            wpbutton.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Selecting points");  
                  }
                  draw.outline_state = WATCHPOINT_STATE; 
              }
            });
            
            //Selectors for which object to draw. Sets the draw state to object value
            JRadioButton wrbutton = new JRadioButton("Watchregion");
            wrbutton.setMnemonic(KeyEvent.VK_C);
            wrbutton.setActionCommand("Watchregion");
                wrbutton.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_radiowr = new GridBagConstraints(); 
            gb_constrain_radiowr.anchor = GridBagConstraints.WEST; 
            gb_constrain_radiowr.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_radiowr.weightx = 0; 
            gb_constrain_radiowr.weighty = 0; 
            gb_constrain_radiowr.gridheight = 1; 
            gb_constrain_radiowr.gridwidth = 1; 
            gb_constrain_radiowr.gridx = 6; 
            gb_constrain_radiowr.gridy = 1; 
            this.add(wrbutton, gb_constrain_radiowr);
            wrbutton.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Selecting regions");
                  }
                  draw.outline_state = WATCHREGION_STATE; 
                  //set flag to draw around regions
              }
            });
            
             //Selectors for which object to draw. Sets the draw state to object value
            JRadioButton wobutton = new JRadioButton("Overwatch");
            wobutton.setMnemonic(KeyEvent.VK_D);
            wobutton.setActionCommand("Overwatch");
                wobutton.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_radioow = new GridBagConstraints(); 
            gb_constrain_radioow.anchor = GridBagConstraints.EAST; 
            gb_constrain_radioow.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_radioow.weightx = 0; 
            gb_constrain_radioow.weighty = 0; 
            gb_constrain_radioow.gridheight = 1; 
            gb_constrain_radioow.gridwidth = 1; 
            gb_constrain_radioow.gridx = 7; 
            gb_constrain_radioow.gridy = 1; 
            this.add(wobutton, gb_constrain_radioow);
            wobutton.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Selecting overwatch rects");   
                  } 
                  draw.outline_state = OVERWATCH_STATE; 
              }
            });
            
            //deselect the current object by clearing the current state
            JRadioButton wnbutton = new JRadioButton("Deselect");
            wnbutton.setMnemonic(KeyEvent.VK_E);
            wnbutton.setActionCommand("Deselect");
                wnbutton.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_radiodesel = new GridBagConstraints(); 
            gb_constrain_radiodesel.anchor = GridBagConstraints.EAST; 
            gb_constrain_radiodesel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_radiodesel.weightx = 0; 
            gb_constrain_radiodesel.weighty = 0; 
            gb_constrain_radiodesel.gridheight = 1; 
            gb_constrain_radiodesel.gridwidth = 1; 
            gb_constrain_radiodesel.gridx = 8; 
            gb_constrain_radiodesel.gridy = 1; 
            this.add(wnbutton, gb_constrain_radiodesel);
            wnbutton.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                     System.out.println("deselected all"); 
                  }
                  draw.redraw_collections();
                  object_number.setText("");
                  draw.outline_state = 0; 
              }
            });
            

            //Group the radio buttons.
            ButtonGroup radio_group = new ButtonGroup();
            radio_group.add(wpbutton);
            radio_group.add(wrbutton);
            radio_group.add(wobutton);
            radio_group.add(wnbutton);
            
            //text area to write object trigger scripts
            JTextArea criteria = new JTextArea("Write selection code here."); 
                criteria.setFont(new Font("Courier",1, 15));
                criteria.setEditable(true);
                criteria.setPreferredSize(new Dimension(550, 230));
            GridBagConstraints gb_constrain_objtrigger = new GridBagConstraints(); 
            gb_constrain_objtrigger.anchor = GridBagConstraints.NORTHWEST; 
            gb_constrain_objtrigger.fill = GridBagConstraints.BOTH; 
            gb_constrain_objtrigger.weightx = 0; 
            gb_constrain_objtrigger.weighty = 0; 
            gb_constrain_objtrigger.gridheight = 1; 
            gb_constrain_objtrigger.gridwidth = 5; 
            gb_constrain_objtrigger.gridx = 4; 
            gb_constrain_objtrigger.gridy = 4; 
            JScrollPane criteria_container = new JScrollPane(criteria); 
            this.add(criteria_container, gb_constrain_objtrigger);
            
            //a list of group and image codes available from the crunchypack for writing. 
            CrunchyCollection groups_and_images = new CrunchyCollection(group_names, image_names);
            GridBagConstraints gb_constrain_groupandimg = new GridBagConstraints(); 
            gb_constrain_groupandimg.anchor = GridBagConstraints.CENTER;  
            gb_constrain_groupandimg.fill = GridBagConstraints.BOTH; 
            gb_constrain_groupandimg.weightx = 0; 
            gb_constrain_groupandimg.weighty = 0; 
            gb_constrain_groupandimg.gridheight = 1; 
            gb_constrain_groupandimg.gridwidth = 5; 
            gb_constrain_groupandimg.gridx = 4; 
            gb_constrain_groupandimg.gridy = 6; 
            this.add(groups_and_images, gb_constrain_groupandimg);
            
            //delete a selected object, checking for dependents before delete.
            /*
            Checks against objects existing object trigger scripts and only deletes object if no
               other objects get this object state in evaluating their own state.
            */
            JButton delete = new JButton("Delete"); 
                delete.setFont(new Font("Courier",1, 15));
                delete.setBackground(Color.RED);
            GridBagConstraints gb_constrain_delete = new GridBagConstraints(); 
            gb_constrain_delete.anchor = GridBagConstraints.WEST; 
            gb_constrain_delete.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_delete.weightx = 0; 
            gb_constrain_delete.weighty = 0; 
            gb_constrain_delete.gridheight = 1; 
            gb_constrain_delete.gridwidth = 2; 
            gb_constrain_delete.gridx = 7; 
            gb_constrain_delete.gridy = 3; 
            this.add(delete, gb_constrain_delete);
            delete.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  int radio_state = draw.outline_state; 
                  //delete object from the parser source, then delete the object and redraw the collection. 
                  switch(radio_state) {
                      case WATCHPOINT_STATE:
                          //get the unique ID of the object
                          int wpid = draw.get_current_watchpoint_id(); 
                          //returns all the unique object ID's that depend on this point in evaluating their own triggers
                          //if it returns empty, the object has no trigger dependents and can be deleted. 
                          String wpoperation = parser.delete_check_dependent(WATCHPOINT_STATE, wpid);
                          if(wpoperation.isEmpty()) {
                              //no dependents, delete
                              criteria.setText("");
                              if(StencilUI.DEBUG_MODE == 1) {
                                    System.out.println("Successful delete");                             
                              }
                              draw.delete_current();
                              draw.redraw_collections();
                          }
                          else {
                              if(StencilUI.DEBUG_MODE == 1) {
                                    System.out.println(wpoperation);
                                    System.out.println("Delete rejected"); 
                              }
                              //don't allow delete if the object has dependent objects
                              //display a popup with the objects dependents
                              JOptionPane.showMessageDialog(null,wpoperation,"Cannot Delete Script: Dependencies Found",JOptionPane.INFORMATION_MESSAGE);
                          } 
                          
                          break;
                      case WATCHREGION_STATE:
                          int wrid = draw.get_current_watchregion_id(); 
                          String wroperation = parser.delete_check_dependent(WATCHREGION_STATE, wrid);
                          if(wroperation.isEmpty()) {
                              criteria.setText("");
                              if(StencilUI.DEBUG_MODE == 1) {
                                 System.out.println("Successful delete");    
                              }
                                draw.delete_current();
                                draw.redraw_collections();
                          }
                          else {
                              System.out.println(wroperation);
                              if(StencilUI.DEBUG_MODE == 1) {
                                  System.out.println("Delete rejected"); 
                              }
                              JOptionPane.showMessageDialog(null,wroperation,"Cannot Delete Script: Dependencies Found",JOptionPane.INFORMATION_MESSAGE);
                          }                   
                          break; 
                      case OVERWATCH_STATE:
                          String operation = parser.delete_check_dependent(OVERWATCH_STATE, 0);
                          if(operation.isEmpty()) {
                              criteria.setText("");
                              if(StencilUI.DEBUG_MODE == 1) {
                                  System.out.println("Successful delete");
                              }
                                draw.delete_current();
                                draw.redraw_collections();
                          }
                          else {
                              System.out.println(operation);
                              if(StencilUI.DEBUG_MODE == 1) {
                                System.out.println("Delete rejected");  
                              }
                              JOptionPane.showMessageDialog(null,operation,"Cannot Delete Script: Dependencies Found",JOptionPane.INFORMATION_MESSAGE);
                              operation = ""; 
                          }
                          break; 
                      default:
                          break; 
                  }
                  //gets the list of available objects from draw after the delete
                  String newactions = draw.get_object_script_list_tabbedprint(); 
                  //updates the list of available objects in Frame 3
                  groups_and_images.update_identify_script(newactions); 
              }
            });
            
            //navigation button for selecting different drawn objects in the frame
            JButton previous = new JButton(" << "); 
                previous.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_previous = new GridBagConstraints(); 
            gb_previous.anchor = GridBagConstraints.WEST; 
            gb_previous.fill = GridBagConstraints.HORIZONTAL; 
            gb_previous.weightx = 0; 
            gb_previous.weighty = 0; 
            gb_previous.gridheight = 1; 
            gb_previous.gridwidth = 1; 
            gb_previous.gridx = 5; 
            gb_previous.gridy = 3; 
            this.add(previous, gb_previous);
            previous.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                     System.out.println("Previous called"); 
                  }
                  //check if that object class has any objects. if nonzero, returns the object index
                  int is_possible = draw.select_previous();
                  //there are objects
                  if(is_possible == 1) {
                    draw.redraw_collections(); //wipe out all selections & rebuild
                    draw.select_rect_draw(); //draw the selection rectangle around the object
                    object_number.setText(draw.watch_string()); //set the object ID in the textarea
                    criteria.setText(draw.get_current_object_script()); //draw this objects trigger in the textarea
                  }
              }
            });
            
            
            //navigation button for selecting the next available object in that object class collection
            JButton next = new JButton(" >> "); 
                next.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_next = new GridBagConstraints(); 
            gb_next.anchor = GridBagConstraints.WEST; 
            gb_next.fill = GridBagConstraints.HORIZONTAL; 
            gb_next.weightx = 0; 
            gb_next.weighty = 0; 
            gb_next.gridheight = 1; 
            gb_next.gridwidth = 1; 
            gb_next.gridx = 6; 
            gb_next.gridy = 3; 
            this.add(next, gb_next);
            next.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                      System.out.println("PNext called");    
                  }
                  //if this object class has objects, get the index of the next object from current
                  int is_possible = draw.select_next();
                  //if nonzero, select this object
                  if(is_possible == 1) { 
                    draw.redraw_collections(); //wipe out all selections & rebuild
                    draw.select_rect_draw(); //redraw collection
                    object_number.setText(draw.watch_string()); //set textarea to this objects number
                    criteria.setText(draw.get_current_object_script()); //set textarea to this objects trigger 
                  }
              }
            });
            
            
            //button clicked to make the next click within the drawframe draw a watchpoint at that coordinate
            JButton button_add_watchpoint = new JButton("      Add Watchpoint      "); 
                button_add_watchpoint.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_watchpoint = new GridBagConstraints(); 
            gb_constrain_watchpoint.anchor = GridBagConstraints.CENTER;  
            gb_constrain_watchpoint.weightx = 0; 
            gb_constrain_watchpoint.weighty = 0; 
            gb_constrain_watchpoint.gridheight = 1; 
            gb_constrain_watchpoint.gridwidth = 1; 
            gb_constrain_watchpoint.gridx = 1; 
            gb_constrain_watchpoint.gridy = 3; 
            this.add(button_add_watchpoint, gb_constrain_watchpoint);
            button_add_watchpoint.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                      System.out.println("Add watchpoint called");
                  }
                  draw.watchpoint_add(); //puts us in an add state
              }
            });
            
             //button clicked to make the next click within the drawframe draw a watchregion at that coordinate
            JButton button_add_watchregion = new JButton("      Add Watchregion      "); 
                button_add_watchregion.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_watchregion = new GridBagConstraints(); 
            gb_constrain_watchregion.anchor = GridBagConstraints.CENTER;  
            gb_constrain_watchregion.weightx = 0; 
            gb_constrain_watchregion.weighty = 0; 
            gb_constrain_watchregion.gridheight = 1; 
            gb_constrain_watchregion.gridwidth = 1; 
            gb_constrain_watchregion.gridx = 2; 
            gb_constrain_watchregion.gridy = 3; 
            this.add(button_add_watchregion, gb_constrain_watchregion);
            button_add_watchregion.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                     System.out.println("Add watchregion called"); 
                  }
                  draw.watchregion_add();
              }
            });
            
             //button clicked to make the next click within the drawframe draw an overwatch at that coordinate
            JButton button_add_overwatch = new JButton("      Add Overwatch      "); 
                button_add_overwatch.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_overwatch = new GridBagConstraints(); 
            gb_constrain_overwatch.anchor = GridBagConstraints.CENTER;  
            gb_constrain_overwatch.weightx = 0; 
            gb_constrain_overwatch.weighty = 0; 
            gb_constrain_overwatch.gridheight = 1; 
            gb_constrain_overwatch.gridwidth = 1; 
            gb_constrain_overwatch.gridx = 3; 
            gb_constrain_overwatch.gridy = 3; 
            this.add(button_add_overwatch, gb_constrain_overwatch);
            button_add_overwatch.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Add overwatch called");   
                  }
                  draw.overwatch_set = 1; 
                  draw.watchregion_add();
              }
            });
            
            
            //button that attaches this object trigger script to the currently selected object
            JButton button_add_identscript = new JButton("Save Identity Script"); 
                button_add_identscript.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_idscript = new GridBagConstraints(); 
            gb_constrain_idscript.anchor = GridBagConstraints.NORTH; 
            gb_constrain_idscript.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_idscript.weightx = 0; 
            gb_constrain_idscript.weighty = 0; 
            gb_constrain_idscript.gridheight = 1; 
            gb_constrain_idscript.gridwidth = 5; 
            gb_constrain_idscript.gridx = 4; 
            gb_constrain_idscript.gridy = 5; 
            this.add(button_add_identscript, gb_constrain_idscript);
            button_add_identscript.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if(StencilUI.DEBUG_MODE == 1) {
                     System.out.println("Add region called");  
                  }
                  //need to find who has current focus. 
                  //need the current select criterinumber. 
                  //script contains only text and an object identifier. Parser will handle verification. 
                  String ident_candidate = criteria.getText(); 
                  //verify syntax before adding
                  String parse_ok = "";  
                  int radio_state = draw.outline_state; 
                  //attempts to parse the object trigger script for syntatic and dependency errors. 
                  //if the string returns empty, no errors were detected and the object script is attached
                  //if it returns errors, the script is not attached and a popup with errors is displayed. 
                  switch(radio_state) {
                      case WATCHPOINT_STATE:
                          //get the object ID of the object we're attempting to attatch a script to
                          int wpid = draw.get_current_watchpoint_id(); 
                          //hand off the candidate string and ID to the parser
                          parse_ok = parser.parse_watchpoint(wpid, ident_candidate); 
                          break;
                      case WATCHREGION_STATE:
                          int wrid = draw.get_current_watchregion_id();
                          parse_ok = parser.parse_watchregion(wrid, ident_candidate);
                          break; 
                      case OVERWATCH_STATE:
                          parse_ok = parser.parse_overwatch(ident_candidate); 
                          break; 
                      default:
                          break; 
                  }
                  //returned with no errors
                  if(parse_ok.isEmpty()) {
                    //set the script with the object
                    draw.set_current_object_script(ident_candidate); 
                    //update the script list in frame 3
                    String newactions = draw.get_object_script_list_tabbedprint(); 
                    //update the frame 3 display of scripts
                    groups_and_images.update_identify_script(newactions); 
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("Successful script add");
                        System.out.println(newactions); 
                    }
                  }
                  else {
                    //show a pop-up with problems
                    JOptionPane.showMessageDialog(null,parse_ok,"Cannot Add Script: Compile Errors",JOptionPane.INFORMATION_MESSAGE);
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("Add rejected"); 
                        System.out.println(parse_ok);
                    }
                  } 
              }
            });
            

            //area for writing action scripts. 
            JTextArea action_script_area = new JTextArea("Write action script code here."); 
                action_script_area.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_constrain_scriptarea = new GridBagConstraints(); 
            gb_constrain_scriptarea.anchor = GridBagConstraints.NORTH; 
            gb_constrain_scriptarea.fill = GridBagConstraints.BOTH; 
            gb_constrain_scriptarea.weightx = 0; 
            gb_constrain_scriptarea.weighty = 0; 
            gb_constrain_scriptarea.gridheight = 1; 
            gb_constrain_scriptarea.gridwidth = 5; 
            gb_constrain_scriptarea.gridx = 4; 
            gb_constrain_scriptarea.gridy = 8; 
            JScrollPane script_container = new JScrollPane(action_script_area); 
            this.add(script_container, gb_constrain_scriptarea);
            
            
            //button to add an action script
            JButton button_add_action_script = new JButton("Save Action Script"); 
                button_add_action_script.setFont(new Font("Courier",1, 15));
            GridBagConstraints gb_script = new GridBagConstraints(); 
            gb_script.anchor = GridBagConstraints.NORTH;  
            gb_script.fill = GridBagConstraints.HORIZONTAL; 
            gb_script.weightx = 0; 
            gb_script.weighty = 0; 
            gb_script.gridheight = 1; 
            gb_script.gridwidth = 5; 
            gb_script.gridx = 4; 
            gb_script.gridy = 9; 
            this.add(button_add_action_script, gb_script);
            button_add_action_script.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  //call the action parser with the candidate
                  String actreturn = actionparse.add_action(action_script_area.getText()); 
                  //parser detected no syntatic or dependency errors, action script is valid. 
                  if(actreturn.isEmpty()) {
                    String strform = actionparse.toString(); 
                    groups_and_images.update_action_script(strform);
                    parser.set_action_update(actionparse.get_actions()); 
                  }
                  //detected problems. Display popup. 
                  else {
                    JOptionPane.showMessageDialog(null,actreturn,"Cannot Add Script: Compile Errors",JOptionPane.INFORMATION_MESSAGE);
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("Group group list called");
                        System.out.println(actreturn); 
                    } 
                  }
              }
            }); 
    }
    
    //returns the original pixel width of the screenshot used to create drawframe
    public int get_screenshot_width() {
        return drawframe.ORIGINAL_WIDTH; 
    }
    
    //returns the original pixel height of the screenshot used to create drawframe
    public int get_screenshot_height() {
        return drawframe.ORIGINAL_HEIGHT; 
    }
    
    //returns the fraction by which the screenhot used to create drawframe has been reduced to fit in the frame
    public double get_screenshot_factor() {
        return drawframe.maxratio_reduction; 
    }
    
    //Return the list of all watchpoints contained in drawframe
    public ArrayList<Watchpoint> get_watchpoint_manager() {
        return drawframe.get_watchpoint_manager(); 
    }
    
     //Return the list of all watchregions contained in drawframe
    public ArrayList<Watchregion> get_watchregion_manager() {
        return drawframe.get_watchregion_manager(); 
    }
    
     //Return the list of all overwatch contained in drawframe
    public ArrayList<Watchregion> get_overwatch_manager() {
        return drawframe.get_overwatch_manager(); 
    }
    
    //all overwatch objects share the same string: returns this string used by all overwatch objects
    public String get_overwatch_script() {
        return drawframe.get_overwatch_script(); 
    }
    
    //get the list of strings validated as actions
    public ArrayList<String> get_actions() {
        return actionparse.get_actions(); 
    }
    
    //returns the string of object ID's in the order in which the compiler will execute them. 
    public ArrayDeque<String> get_final_order() {
        //prepares the execution order string in preparation for final packaging and stencilpack creation
        return parser.string_final_pass_check_order(); 
    }
}
