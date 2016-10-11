/*
The card layout class holds a single frame which manages all subframes
and presents the three top level navigation controls. 

REFACTOR REPORT: TODO
    Still uses debug name for output file. Make it use the supplied name in crunchypack input header. (HIGH)
    Uses String instead of stringbuilder for master output. (LOW: This is not a speed-critical component. Saves milliseconds at best.)
 */
package stencilui;
import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*; 
import java.nio.file.*; 
import java.io.File;
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
import java.io.*; 
import java.util.ArrayDeque;
import static stencilui.StencilUI.DEBUG_MODE;
import java.util.ArrayList;   
import java.util.Scanner; 

/**
 *
 * @author jamesl
 */

/*
the one and only card layout object that holds the panel components and porvides navigation. 
*/
public class CardsManager extends JFrame{
     static private JPanel cardPanel; //the card manager frame
     private int currentCard = 1; //the starting identifer
     private static CardLayout cards; //the one and only cards object
     public static int SRC_HEIGHT; 
     public static int SRC_WIDTH; 
     public static String IMGPATH; 
     public static String CRUNCHYPACK_PATH; 
     private ArrayList<String> crunchypack_groups; //names of the user chosen groups
     private ArrayList<ArrayList<String>> crunchypack_names; //alias of images in the groups
     final private int CRUNCHYPACK_DATA_LINES = 2; //How many lines are non color matrix data
     private Frame_3 ref_jp3; //reference to frame 3 created object
     
     
     public CardsManager() {
         this.setTitle("Stencilpack Builder 1.0"); 
         javax.swing.UIManager.put("OptionPane.font", new Font("Courier", Font.BOLD, 30)); //set the font for all popups
                 
                 
         //create the card layout containing frame
         cardPanel = new JPanel(); 
         cards = new CardLayout(); 
         cardPanel.setLayout( cards );    
         crunchypack_groups = new ArrayList<>(); 
         crunchypack_names = new ArrayList<>(); //group: item name a, item name b, item name gb_constrain_dummypanel...
        
         
         //build the jpanel button row
         JPanel buttonpack = new JPanel(); 
         buttonpack.setLayout( new GridBagLayout() );
         
         
         //----------------Gridbag alignment panel code for the card layout frame
         GridBagConstraints gb_constrain_dummypanel = new GridBagConstraints(); //container holding gridbag components
         JPanel h_panel_1 = new JPanel();
            h_panel_1.setBackground(Color.RED);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTHWEST;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 0; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_1, gb_constrain_dummypanel);

            JPanel h_panel_2 = new JPanel();
            h_panel_2.setBackground(Color.GREEN);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL;  
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 1; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_2, gb_constrain_dummypanel);

            JPanel h_panel_3 = new JPanel();
            h_panel_3.setBackground(Color.RED);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 2; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_3, gb_constrain_dummypanel);

                    JPanel h_panel_4 = new JPanel();
            h_panel_4.setBackground(Color.GREEN);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 3; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_4, gb_constrain_dummypanel);

                    JPanel h_panel_5 = new JPanel();
            h_panel_5.setBackground(Color.RED);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 4; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_5, gb_constrain_dummypanel);

                    JPanel h_panel_6 = new JPanel();
            h_panel_6.setBackground(Color.GREEN);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 5; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_6, gb_constrain_dummypanel);

                    JPanel h_panel_7 = new JPanel();
            h_panel_7.setBackground(Color.RED);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 6; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_7, gb_constrain_dummypanel);

            JPanel h_panel_8 = new JPanel();
            h_panel_8.setBackground(Color.GREEN);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 7; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_8, gb_constrain_dummypanel);

            JPanel h_panel_9 = new JPanel();
            h_panel_9.setBackground(Color.RED);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL;  
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 8; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_9, gb_constrain_dummypanel);

            JPanel h_panel_10 = new JPanel();
            h_panel_10.setBackground(Color.GREEN);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 0.1; 
            gb_constrain_dummypanel.gridx = 9; 
            gb_constrain_dummypanel.gridy = 0; 
            buttonpack.add(h_panel_10, gb_constrain_dummypanel);
            //----------------Gridbag alignment panel code for the card layout frame END
            
        //Add the static frames to the card jframe
        Frame_1 frame_display1 = new Frame_1();
        Frame_2 frame_display2 = new Frame_2(); 
        Frame_3 frame_display3; //this will be populated with frame 1 and 2 information later
        cardPanel.add(frame_display1, "1"); //panel, identifier
        cardPanel.add(frame_display2, "2"); 
        
        //Add the top level navigation buttons
        JButton button_exit = new JButton("Exit");
        button_exit.setFont(new Font("Courier", Font.PLAIN, 20));
        button_exit.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          { 
              if(currentCard == 1) {
                 System.exit(0);
              }
              else {
                  --currentCard;
                  cards.show(cardPanel, Integer.toString(currentCard));
              }
          }
        });
        GridBagConstraints gb_constrain_exitpanel = new GridBagConstraints(); //container holding gridbag components
        gb_constrain_exitpanel.anchor = GridBagConstraints.WEST;
        gb_constrain_exitpanel.fill = GridBagConstraints.HORIZONTAL; 
        gb_constrain_exitpanel.weightx = 0.1; 
        gb_constrain_exitpanel.weighty = 1; 
        gb_constrain_exitpanel.gridwidth = 2; 
        gb_constrain_exitpanel.gridheight = 2;
        gb_constrain_exitpanel.ipady = 5;
        gb_constrain_exitpanel.gridx = 1; 
        gb_constrain_exitpanel.gridy = 1; 
        buttonpack.add(button_exit, gb_constrain_exitpanel);
         
        //window 2 launch button
        JButton button_sitelaunch = new JButton("Visit the Site"); 
                button_sitelaunch.setFont(new Font("Coutier", Font.PLAIN, 20));
         button_sitelaunch.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
              //launch window 2. 
          }
        });
        
        GridBagConstraints gb_constrain_sitelaunch = new GridBagConstraints(); //container holding gridbag components
        gb_constrain_sitelaunch.anchor = GridBagConstraints.CENTER;
        gb_constrain_sitelaunch.fill = GridBagConstraints.HORIZONTAL; 
        gb_constrain_sitelaunch.weightx = 0.1; 
        gb_constrain_sitelaunch.gridwidth = 2;
        gb_constrain_sitelaunch.gridheight = 3;
        gb_constrain_sitelaunch.ipady = 5;
        gb_constrain_sitelaunch.weighty = 1; 
        gb_constrain_sitelaunch.gridx = 4; 
        gb_constrain_sitelaunch.gridy = 1; 
        buttonpack.add(button_sitelaunch, gb_constrain_sitelaunch);
            
            
        //advance to next panel button
        JButton button_next = new JButton("Next >");
                button_next.setFont(new Font("Courier", Font.PLAIN, 20));
        button_next.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
              if(currentCard == 1) {
                //advance to frame 2: enter file locations
                ++currentCard; 
                cards.show(cardPanel, Integer.toString(currentCard)); 
              }
              else if(currentCard == 2) {
                  //get the filestring and test if it exists. 
                  if(DEBUG_MODE == 1) {
                      IMGPATH = "C:\\Users\\JamesH\\Pictures\\Screenshots\\largetest.png";
                      CRUNCHYPACK_PATH = "C:\\Users\\JamesH\\Documents\\NetBeansProjects\\StencilUI\\outtest.txt"; 
                  }
                  else {
                    IMGPATH = Frame_2.screenshot_source.getText();
                    CRUNCHYPACK_PATH = Frame_2.crunchypack_source.getText(); 
                  }
                  //verify that the file paths and files are reachable and openable
                  int verif_status = verify_file(IMGPATH); 
                  int verify_cpack = verify_file(CRUNCHYPACK_PATH); 
                  if(verif_status == 0 && verify_cpack == 0) {
                      //file exists and is reachable. 
                      int filesize = assess_file_size(IMGPATH); //get the size of the image background
                      int crunchypack_status = crunchypack_tokenize(CRUNCHYPACK_PATH); //extract image information from crunchypack
                      if(filesize == 0 && crunchypack_status == 0) {
                          //dynamically create and add frame 3 on the fly. 
                          ++currentCard; 
                          Frame_3 jp3 = new Frame_3(IMGPATH, crunchypack_groups, crunchypack_names); 
                          ref_jp3 = jp3; 
                          cardPanel.add(jp3, "3"); 
                          resize_to_image(); //resize the jframe to fit the screenshot, otherwise buttons cluster on center
                          cards.show(cardPanel, Integer.toString(currentCard));
                      }
                      else {
                          //error popup. could not open a file 
                          l2_popup("There were problemns opening the screenshot or crunchypack file. \n\n"
                                + "Confirm that the filepaths are to the correct locations. \n"
                                + "Also be sure to include the full extension of the file, such as :\n"
                                  + "C:\\Users\\Kevin\\Pictures\\Screenshots\\yourfile.txt"); 
                      }   
                  }
                  else {
                      //error popup. could not locate a file. 
                      l2_popup("The screenshot or crunchypack file could not be located. \n\n"
                                + "Confirm that the filepaths are to the correct locations. \n"
                                + "Also be sure to include the full extension of the file, such as :\n"
                                  + "C:\\Users\\Kevin\\Pictures\\Screenshots\\yourfile.txt");  
                  }
              }
              else if(currentCard == 3) {
                //get the dimensions of the compressed screenshot used in frame 3 and prepare the stencilpack. 
                int screenshot_width = ref_jp3.get_screenshot_width(); 
                int screenshot_height = ref_jp3.get_screenshot_height(); 
                double screenshot_factor = ref_jp3.get_screenshot_factor(); //stretch factor of screenshot 

                String master_out = ""; 
                
                String size_header = ""; //dimensions of original image (pre-compression)
                size_header += Integer.toString(screenshot_width) + " " + Integer.toString(screenshot_height) + System.lineSeparator();  
                
                String build_order = ""; //serialized order
                    ArrayDeque<String> forder = ref_jp3.get_final_order();  
                    for(String fo: forder) {
                        build_order += fo + " "; 
                    }
                build_order += System.lineSeparator(); 
                
                
                //writes the list of watchregions, watchpoints, and overwatches to the string 
                String object_table = ""; //object(x, y) script with semicolon end
                    ArrayList<Watchregion> pkg_overwatch = ref_jp3.get_overwatch_manager(); 
                    for(int i = 0; i < pkg_overwatch.size(); ++i) {
                        Watchregion cwp = pkg_overwatch.get(i); 
                        int adj_x = (int)((double)cwp.get_xorigin()*screenshot_factor); 
                        int adj_y = (int)((double)cwp.get_yorigin()*screenshot_factor); 
                        int adj_width = (int)((double)cwp.get_width()*screenshot_factor); 
                        int adj_height = (int)((double)cwp.get_height()*screenshot_factor);
                        String overwatch_ident_script = ref_jp3.get_overwatch_script(); 
                        object_table += "O(" + Integer.toString(adj_x) + "," + Integer.toString(adj_y) + "," + Integer.toString(adj_width) + "," + Integer.toString(adj_height)+ ") "; 
                        object_table += overwatch_ident_script + ";" + System.lineSeparator();
                    }
                    ArrayList<Watchpoint> pkg_watchpoint = ref_jp3.get_watchpoint_manager(); 
                    for(int i = 0; i < pkg_watchpoint.size(); ++i) {
                        Watchpoint cwp = pkg_watchpoint.get(i); 
                        int adj_width = (int)((double)cwp.getx()*screenshot_factor); 
                        int adj_height = (int)((double)cwp.gety()*screenshot_factor); 
                        String watchpoint_ident_script = cwp.get_ident_string(); 
                        object_table += "P" + Integer.toString(cwp.get_identifier()) + "(" + Integer.toString(adj_width) + "," + Integer.toString(adj_height) + ") "; 
                        object_table += watchpoint_ident_script + ";" + System.lineSeparator();
                    }
                    ArrayList<Watchregion> pkg_watchregion = ref_jp3.get_watchregion_manager(); 
                    for(int i = 0; i < pkg_watchregion.size(); ++i) {
                        Watchregion cwp = pkg_watchregion.get(i); 
                        int adj_x = (int)((double)cwp.get_xorigin()*screenshot_factor); 
                        int adj_y = (int)((double)cwp.get_yorigin()*screenshot_factor); 
                        int adj_width = (int)((double)cwp.get_width()*screenshot_factor); 
                        int adj_height = (int)((double)cwp.get_height()*screenshot_factor);
                        String watchregion_ident_script = cwp.get_ident_string(); 
                        object_table += "R(" + Integer.toString(adj_x) + "," + Integer.toString(adj_y) + "," + Integer.toString(adj_width) + "," + Integer.toString(adj_height)+ ") ";  
                        object_table += watchregion_ident_script  + ";" + System.lineSeparator();
                    }
                
                //writes the list of actions to the string
                String action_table = ""; //table of actions
                ArrayList<String> action_list = ref_jp3.get_actions(); 
                for(int i = 0; i < action_list.size(); ++i) {
                    action_table += "A" + Integer.toString(i) + ":" + action_list.get(i) + ";" + System.lineSeparator(); 
                }
                
                //read the crunchypack file image list so only stencilpack file contains all data. 
                String crunchypack_data = ""; //full copy of crunchypack table. 
                try{
                    Scanner scanner = new Scanner(new File(CRUNCHYPACK_PATH));
                    scanner.useDelimiter(System.lineSeparator());
                        while (scanner.hasNext()) {
                            String line = scanner.next();                         
                            crunchypack_data += line + System.lineSeparator(); 
                        }
                    }
                catch(FileNotFoundException fnfe) {
                      l2_popup("The crunchypack file could not be located. \n\n"
                                + "This file is required to complete the stencilpack build. \n"
                                + "If the file has been deleted or moved, you will have to rebuild the stencil. \n"
                                + "For future reference, never delete files used in the build until the build is done."); 
                }

                //the master out string, written to file. 
                master_out += size_header + build_order + object_table + action_table + crunchypack_data; 
               
                //write the file 
                try {
                    File wsource = new File("out_ts.txt"); 
                    FileWriter fw = new FileWriter(wsource.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(master_out);
                    bw.close();
                }
                catch(IOException ioe) {
                      l2_popup("An IO error occured when writing out the stencilpack file. \n\n"
                                + "This program requires permission to write a text file. \n"
                                + "It is possible this program is not running at a sufficient privlidge level.\n"); 
                }
              }
          }
        });
            //Additional layout panels
            GridBagConstraints gb_constrain_next = new GridBagConstraints(); //container holding gridbag components
            gb_constrain_next.anchor = GridBagConstraints.EAST;
            gb_constrain_next.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_next.weightx = 0.1; 
            gb_constrain_next.gridwidth = 2;
            gb_constrain_next.gridheight = 3;
            gb_constrain_next.ipady = 5; 
            gb_constrain_next.weighty = 1; 
            gb_constrain_next.gridx = 7; 
            gb_constrain_next.gridy = 1; 
            buttonpack.add(button_next, gb_constrain_next);

            JPanel h_panel_11 = new JPanel();
            h_panel_11.setBackground(Color.GREEN);
            gb_constrain_dummypanel.anchor = GridBagConstraints.NORTH;
            gb_constrain_dummypanel.fill = GridBagConstraints.HORIZONTAL; 
            gb_constrain_dummypanel.weightx = 1; 
            gb_constrain_dummypanel.gridheight = 4; 
            gb_constrain_dummypanel.gridwidth = 10; 
            gb_constrain_dummypanel.gridx = 0; 
            gb_constrain_dummypanel.gridy = 5; 
            buttonpack.add(h_panel_11, gb_constrain_dummypanel);
            
         //add buttons and the card layout card to the master jframe. 
         this.getContentPane().add(buttonpack, BorderLayout.SOUTH); //add the button panel
         this.getContentPane().add(cardPanel, BorderLayout.CENTER); //add the changing frame area
     }
     
     /*
     Verifies that the crunchypack file supplied in frame 2 exists
     */
     private int verify_file(String candidate) {
         //attempts to locate the file. Returns 1 if it cannot locate the file at the supplied path
         Path path = FileSystems.getDefault().getPath(candidate); 
         if (Files.exists(path)) {
            return 0; 
          }
         else if (Files.notExists(path)) {
            return 1; 
          } 
         return 1; 
     }
     
     /*
       attempts to extract dimensions of the background file supplied in frame 2 used in frame 2. Returns 1 on failure.
     */
     private int assess_file_size(String candidate) {
 
        File assessing = new File(candidate); 
        BufferedImage bimg; 
        try {
             bimg = ImageIO.read(assessing);

          } catch (IOException e) {
            return 1; 
          }
        
        //on success, get the files height/width properties. 
         SRC_WIDTH = bimg.getWidth();
         SRC_HEIGHT = bimg.getHeight();
         return 0; 
     }
     
     /*
     Extracts the crunchypack header composed of pack name, group names, and image names. 
     Pixel data is only retrieved as final data string is being written
     */
     private int crunchypack_tokenize(String fpath) {
         //extracts the group and item names from the supplied crunchypack file. Returns 1 on a fatal error.
         int reader = 0; 
         try (BufferedReader br = new BufferedReader(new FileReader(fpath))) {
            String line;
            while ((line = br.readLine()) != null && (reader < CRUNCHYPACK_DATA_LINES)) {
               switch(reader) {
                   case 0:
                       //resize crunchypack names to contain this many groups
                       int itemcounter = 0; 
                       String substring = "";
                       for(int i = 0; i < line.length(); ++i) { 
                           if(line.charAt(i) == ',') {
                               crunchypack_groups.add(itemcounter, substring);
                               System.out.println("Gname:" + substring + "---");
                               substring = ""; 
                               crunchypack_names.add(new ArrayList<String>()); 
                               ++itemcounter; 
                           }
                           else {
                               substring += line.charAt(i); 
                           }
                       }
                       break; 
                   case 1:
                       int commacounter = 0; 
                       String subsum = ""; 
                       String subname = ""; 
                       for(int i = 0; i < line.length(); ++i) { 
                           if(line.charAt(i) == ',') {
                               switch(commacounter) {
                                   case 0:
                                       ++commacounter;
                                       subsum = ""; 
                                       break; 
                                   case 1:
                                       subname = subsum; 
                                       subsum = ""; 
                                       ++commacounter; 
                                       break; 
                                   case 2:
                                       crunchypack_names.get(Integer.parseInt(subsum)).add(subname); 
                                       System.out.println("iname:" + subname + "--"); 
                                       subname = ""; 
                                       subsum = ""; 
                                       commacounter = 0; 
                                       break; 
                                   default:
                                       break; 
                               }
                           }
                           else {
                               subsum += line.charAt(i); 
                           }
                       }
                       break; 
                   default:
                       break; 
               }
               //tokenize the lines
               //crunchypack_names
               ++reader; 
            }
        }
         catch(IOException e) {
             return 1; 
         }
         return 0; 
     }
     
     
     /*
       generic error displayed on failed to locate or read file. Creates a popup displaying the error code. 
     */
     private void l2_popup(String error_msg) {

        final String p_title = "An error occured:"; 
              
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        UIManager.put("OptionPane.messageFont", font); //sets the size of popup fonts. 
        JFrame popup_parent = new JFrame(); 
        JOptionPane jpop = new JOptionPane(); 
        jpop.showOptionDialog(
            popup_parent, error_msg, p_title, JOptionPane.DEFAULT_OPTION,
            JOptionPane.ERROR_MESSAGE, null, null, null
            );
        popup_parent.setVisible(false);
        popup_parent.dispose();
     }
     
     //deletes this frame. unused debug function 
     public void ui_teardown() {
         this.setVisible(false);
     }
     
     //resizes the card manager containing frame to a size appropriate for frame 3 to contain the scaled screenshot. 
     private void resize_to_image() {
         //resizes the jframe to accomodate the screenshot and all its components
         this.setSize(new Dimension(SRC_WIDTH + 800, SRC_HEIGHT + 400));
     }
}
