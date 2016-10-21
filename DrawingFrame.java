/*
This class displays an image (screenshot) reduced by a certain factor in order to fit in the frame background.
This image can be drawn on and these coordinates will be extrapolated to place watchobjects when connected to a 
    screen buffer of similar size, as will be done by the stencilpack interpreter. These points will be unpacked and 
    their coordinates adjusted by the reduction scale factor in order to place them where they would be on the 
    screenshot at its original size. 
 */
package stencilui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import java.util.ArrayList; 
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 


/**
 *
 * @author jamesl
 */
public class DrawingFrame extends JLabel{
    
    //these properties are used to draw the "selected" rectangle around the currently selected object. 
    private int square_origin_x;
    private int square_origin_y;
    private final int squareW = 8;
    private final int squareH = 8;
    private final int HALFCENTER = squareW/2 + 1; 
    
    
    //image dimension properties
    final private int WMAX = 2100; //chosen as the fixed max width and length: the image is compressed to fit into these. 
    final private int LMAX = 1000; 
    final public int ORIGINAL_WIDTH; //saves the original width and height numbers
    final public int ORIGINAL_HEIGHT; 
    public double maxratio_reduction = 1; //factor by which the image has been reduced. 
    
    //these properties manage the various watchobjects drawn on the object. 
    private boolean init_nodraw = true; 
    private ArrayList<Watchpoint> watchpoint_manager; 
    private ArrayList<Watchregion> watchregion_manager;  
    private ArrayList<Watchregion> overwatch_manager; 
    private String overwatch_string; 
    private int counter = 0; 
    private int region_counter = 0; 
    private int overwatch_counter = 0; 
    private boolean add_flag = false; 
    private Graphics2D gph_mgr; 
    private Graphics2D bkg_mgr; 
    private BufferedImage background; //the actual scaled ImageIcon being used as the background
    private final BufferedImage reset_background; 
    
    //keep track of the last selected watchobject in each category, for prev-next navigation
    private int watchpoint_currently_selected = 0; 
    private int watchregion_currently_selected = 0; 
    private int overwatch_currently_selected = 0; 
    
    private int region_construction_called = 0; 
    private int region_tmp_x = 0; 
    private int region_tmp_y = 0; 
    
    private boolean adder_flag = false; 
    private boolean region_adder_flag = false; 
    private volatile boolean background_reset_flag = false; 
    
    //essencially enumerations for the various drawing state properties. 
    volatile public int outline_state = 0; 
    final private int watchpoint_state = 1; 
    final private int watchregion_state = 2; 
    final private int overwatch_state = 3;
    
    //enforces singleton of overwatch if set to nonzero. (an overwatch exists.)
    public int overwatch_set = 0; 
    
    //the colors that the various watchobjects are drawn with. 
    final private Color watchregion_color = new Color(255, 255, 0, 120);
    final private Color watchpoint_color = new Color(0, 0, 255, 120);
    final private Color overwatch_color = new Color(0, 255, 0, 50);
    
    
    //initialized with the location of a screenshot supplied and validated by frame 2 in cardmanager
    public DrawingFrame(final String sourcedata) {
        File bigsamp = new File(sourcedata); //create a new file
        this.setLayout( new GridBagLayout() );
        BufferedImage bufferedImage = null;
        
        //try to read the screenshot into memory
        try {
            bufferedImage = ImageIO.read(bigsamp); 
        } catch (IOException ex) {
            //splash an error about the screenshot not existing. 
            JOptionPane.showMessageDialog(null,"The source screenshot file does not exist.","Fatal Error",JOptionPane.INFORMATION_MESSAGE);
            System.exit(1); //kill the program. it cannot continue without a source file. 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("Failed to locate screenshot source.");       
            }
        }
        
        //program did not abourt, so it found an image and read it into memory. Get pixel width and height. 
        ORIGINAL_WIDTH = bufferedImage.getWidth();
        ORIGINAL_HEIGHT = bufferedImage.getHeight(); 
        //if the image within size restructions, render as is
        if(ORIGINAL_HEIGHT <= LMAX  &&  ORIGINAL_WIDTH <= WMAX) {
            ImageIcon screenshot = new ImageIcon(bufferedImage); 
            this.setIcon(screenshot);
            this.setMinimumSize( new Dimension(bufferedImage.getHeight(), bufferedImage.getWidth()));
            background = bufferedImage; 
        }
        else {
            //it exceeds space bounds. need to scale down. 
            //to preserve ratio, only scale by the largest of L or W
            if(bufferedImage.getHeight() > LMAX  &&  bufferedImage.getWidth() > WMAX) {
                //both too large. Find which violates the largest. 
                int diffe_height = bufferedImage.getHeight() - LMAX; 
                int diffe_width = bufferedImage.getWidth() - WMAX; 
                if(diffe_width > diffe_height) {
                    //
                    maxratio_reduction = (double)((double)bufferedImage.getWidth()/(double)WMAX); //amount 
                }
                else {
                    maxratio_reduction = (double)((double)bufferedImage.getHeight()/(double)LMAX); //amount 
                }
            }
            else if(bufferedImage.getHeight() > LMAX  &&  bufferedImage.getWidth() <= WMAX) {
                //width OK, height too large
                maxratio_reduction = (double)((double)bufferedImage.getHeight()/(double)LMAX); //amount 
            }
            else if(bufferedImage.getHeight() <= LMAX  &&  bufferedImage.getWidth() > WMAX) {
                //height OK, width too large
                maxratio_reduction = (double)((double)bufferedImage.getWidth()/(double)WMAX); //amount
            }

            //render a scaled down copy to use within the frame.
            //The original size and scale factors are saved so when the final stencilpack is made the coordinates
                //are scaled up to their original image location. 
            double new_width = (double)bufferedImage.getWidth()/maxratio_reduction; 
            double new_length = (double)bufferedImage.getHeight()/maxratio_reduction; 
            BufferedImage resizedImage = new BufferedImage((int)new_width, (int)new_length, BufferedImage.TYPE_INT_ARGB); //create a resize
            Graphics2D g = resizedImage.createGraphics(); //associate temp with g object
            g.drawImage(bufferedImage, 0, 0, (int)new_width, (int)new_length, null); //draw buffered into resized through a graphics
            g.dispose();

            //set the scaled image as the background for this class, an imageicon
            ImageIcon screenshot = new ImageIcon(resizedImage); 
            this.setIcon(screenshot);
            //resize the containing jframe to appropriate dimensions for the scaled image
            this.setMinimumSize( new Dimension(resizedImage.getHeight(), resizedImage.getWidth())); 
            //keep a refrence to the background image available. 
            background = resizedImage; 
        }
          
        //make a copy of the resized image to be used as a basis for redrawing the background.  
        BufferedImage copy = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = copy.createGraphics();
        g.drawImage(background, 0, 0, null); 
        g.dispose(); 
        reset_background = copy; 
        
        //Turns the mouse into a crosshair when drawing on frame 3, for improved placement. 
        this.setCursor( new Cursor(Cursor.CROSSHAIR_CURSOR));      
        this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point p = e.getLocationOnScreen();
                    if(StencilUI.DEBUG_MODE == 1) {
                        
                    }
                    System.out.println((p.getX() + "   " + p.y));
                    System.out.println(adder_flag + " " + region_adder_flag);
                    if(adder_flag == true) {
                        if(StencilUI.DEBUG_MODE == 1) {

                        }
                        System.out.println("Adding watchpoint s1");
                        watchpoint_construction(p); 
                    }
                    else if(region_adder_flag == true) {
                    if(StencilUI.DEBUG_MODE == 1) {
                        
                    }
                        System.out.println("Adding watchregion s1."); 
                        if(region_construction_called < 2) {
                            watchregion_construction(p); 
                        }
                    }

                    //if watchpoint clicked, call one thing.
                    //if region clciked, call another
                }
        }); 
        
        
        
        /*
                this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                
                System.out.println("componentResized");
                //redraw_on_move(); 
                //repaint(); 
            }
            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("componentVisible");
                //repaint(); 
            }
        });
        
        */

        
        //initialize the lists of all created watchobjects and the single shared overwtach string. 
        watchpoint_manager = new ArrayList<>(); //the manager of all watchpoints. 
        watchregion_manager = new ArrayList<>(); 
        overwatch_manager = new ArrayList<>(); 
        overwatch_string = ""; 
    }
    
    //invoked to redraw the image
    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, null);
    }
    
    //creates a watchpoint at the clicked location 
    private void watchpoint_construction(Point p) {
            Graphics temp_gph = background.createGraphics(); //temporary drawing surface
            SwingUtilities.convertPointFromScreen(p, this); //make coordinates relative to the parent, which is the label
            square_origin_x = p.x; 
            square_origin_y = p.y;  
            //draw the watchpoint on the background
            temp_gph.setColor(watchpoint_color);
            temp_gph.fillOval(square_origin_x - HALFCENTER, square_origin_y - HALFCENTER, squareW, squareH);
            temp_gph.drawOval(square_origin_x - HALFCENTER, square_origin_y - HALFCENTER, squareW, squareH);
            //create the watchpoint and add to the collection
            Watchpoint nwpt = new Watchpoint(counter, square_origin_x, square_origin_y); 
            if(StencilUI.DEBUG_MODE == 1) {
                
            }
            System.out.println("Added " + p.x + " " + p.y); 
            watchpoint_manager.add(nwpt); 
            watchpoint_currently_selected = watchpoint_manager.size() - 1; //currently is the last addition to watchpoint mgr
            ++counter; // keeps track of the actual size of watchpoint manager
            temp_gph.dispose();
            this.repaint();  
            adder_flag = false;            
           //TO DELETE: select the point from thr wmanager, clear it, then repaint background and iterate wmgr again
    }
    
    /*
    build a watchregion object and add it to the watchregion collection
    Watchregions require TWO clicks. One to set the upper left point, and another to set the lower right (make a square)
    */
    private void watchregion_construction(Point p) {
        //first click event, log the location of the upper left
        if(region_construction_called == 0) {
            SwingUtilities.convertPointFromScreen(p, this); //make coordinates relative to the parent, which is the label
            region_tmp_x = p.x; 
            region_tmp_y = p.y; 
            ++region_construction_called; 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("Watchregion sequence 1 set");
            }
        }
        //second click, log location of lower left. 
        else if(region_construction_called == 1){
            SwingUtilities.convertPointFromScreen(p, this); //make coordinates relative to the parent, which 
            int terminalx = p.x; 
            int terminaly = p.y; 
            int tempx = region_tmp_x; 
            int tempy = region_tmp_y; 
            //we are creating a region that is the overwatch object
            if(overwatch_set == 1) {
                Watchregion nwchr = new Watchregion(overwatch_counter, tempx, tempy, terminalx, terminaly);
                overwatch_manager.add(nwchr); 
                ++overwatch_counter; 
            }
            //creating a normal watchregion
            else {
                Watchregion nwchr = new Watchregion(region_counter, tempx, tempy, terminalx, terminaly); 
                watchregion_manager.add(nwchr); 
                ++region_counter;
            }

            //draw the object
            region_tmp_x = 0; 
            region_tmp_y = 0;   
            Graphics temp_gph = background.createGraphics(); //temporary drawing surface
            if(overwatch_set == 1) {
                temp_gph.setColor(overwatch_color);
                temp_gph.fillRect(tempx, tempy, Math.abs(terminalx - tempx), Math.abs(terminaly - tempy));
                temp_gph.drawRect(tempx, tempy, Math.abs(terminalx - tempx), Math.abs(terminaly - tempy));
                overwatch_set = 0; 
            }
            else {
                temp_gph.setColor(watchregion_color);
                temp_gph.fillRect(tempx, tempy, Math.abs(terminalx - tempx), Math.abs(terminaly - tempy));
                temp_gph.drawRect(tempx, tempy, Math.abs(terminalx - tempx), Math.abs(terminaly - tempy));
            }

            temp_gph.dispose();
            this.repaint();  
            region_adder_flag = false; 
            region_construction_called = 0;  
        }
    }
    
    //need to track if this is the first or seconc click. object isnt created until it has two valid points
    public void watchregion_add() {
        region_adder_flag = true; 
    }
    
    //forbids creating a single-click watchpoint inbetween the expected two clicks to make a watchregion.
    public void watchpoint_add() {
        //draw a selection circle around this object
        //if region adder is true and someone tries to inject a watchpoint, stop the adder state
        adder_flag = true;   
    }
   
    
    
    /*
    updates the "currently selected" number for which object is selected by the next/previous frame 3 navigation controls.
    Provides wraparound selection at ends. 
    */
    public int select_previous() {
        /*
            draw a selection circle around this object
            All 3 objects follow similar selection criteria to update their most recently selected value
        */
        if(outline_state == watchpoint_state) {
            //no such objects exist
            if(watchpoint_manager.isEmpty()) {
                return 0; 
            }
            //decrease the most-recently-selected number
            --watchpoint_currently_selected; 
            //if it happens to be 0, wrap around
            if(watchpoint_currently_selected < 0) {
                watchpoint_currently_selected = watchpoint_manager.size() - 1; 
            }
            else {
                //just decrease. no special condition
            }
        }
        else if(outline_state == watchregion_state) {
            if(watchregion_manager.isEmpty()) {
                return 0; 
            }
            --watchregion_currently_selected; 
            if(watchregion_currently_selected < 0) {
               watchregion_currently_selected = watchregion_manager.size() - 1; 
            }
            else {
                //equals same
            }
        }
        else if(outline_state == overwatch_state){
            if(overwatch_manager.isEmpty()) {
                return 0; 
            }
            --overwatch_currently_selected; 
            if(overwatch_currently_selected < 0) {
               overwatch_currently_selected = overwatch_manager.size() - 1; 
            }
            else {
                //equals same
            }
        }  
        else {
            //No selection. 
        }
        
        //valid selection operation complete with nonempty collection. 
        return 1; 
    }
    
    /*
        updates the "currently selected" number for which object is selected by the next/previous frame 3 navigation controls.
        Provides wraparound selection at ends.
    */
    public int select_next() {
        /*
            draw a selection circle around this object
            All 3 objects follow similar selection criteria to update their most recently selected value
        */
        if(StencilUI.DEBUG_MODE == 1) {
            System.out.println("State " + outline_state); 
        }
        if(outline_state == watchpoint_state) {
            if(watchpoint_manager.isEmpty()) {
                return 0; 
            }
            ++watchpoint_currently_selected; 
            if(watchpoint_currently_selected > watchpoint_manager.size() - 1) {
                watchpoint_currently_selected = 0; 
            }
            else {
                //equals same. 
            }
            return 1; 
        }
        else if(outline_state == watchregion_state) {
            if(watchregion_manager.isEmpty()) {
                return 0; 
            }
            ++watchregion_currently_selected; 
            if(watchregion_currently_selected > watchregion_manager.size() - 1) {
               watchregion_currently_selected = 0; 
            }
            else {
                //equals same. 
            }
            return 1; 
        }
        else if(outline_state == overwatch_state){
            if(overwatch_manager.isEmpty()) {
                if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("EMP");      
                }
                return 0; 
            }
            ++overwatch_currently_selected; 
            if(overwatch_currently_selected > overwatch_manager.size() - 1) {
               overwatch_currently_selected = 0; 
            }
            else {
                //equals same. 
            }
            return 1;   
        }
        else {
            return 0; 
        }
}
    
    

    
    /*
    When invoked, redraws the watchobjects on top of the background screenshot. 
    */
    public void redraw_collections() {  
        //responsible for redrawing the unselected items
        if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("RC");     
        }
        
        //redraw background rectangle. 
        Graphics temp_gph = background.createGraphics();
        temp_gph.clearRect(0, 0, background.getWidth(), background.getHeight());
        temp_gph.drawImage(reset_background, 0, 0, null); //draws a blank background
        
        //draw the overwatch, regions, then the points from each collection.  
        for(int i = 0; i < overwatch_manager.size(); ++i) {
            int originx = overwatch_manager.get(i).get_xorigin();
            int originy = overwatch_manager.get(i).get_yorigin();  
           
            temp_gph.setColor(overwatch_color);
            temp_gph.fillRect(originx, originy, overwatch_manager.get(i).get_width(), overwatch_manager.get(i).get_height());
            temp_gph.drawRect(originx, originy, overwatch_manager.get(i).get_width(), overwatch_manager.get(i).get_height());
        }
        for(int i = 0; i < watchregion_manager.size(); ++i) {
            int originx = watchregion_manager.get(i).get_xorigin();
            int originy = watchregion_manager.get(i).get_yorigin();  
           
            temp_gph.setColor(watchregion_color);
            temp_gph.fillRect(originx, originy, watchregion_manager.get(i).get_width(), watchregion_manager.get(i).get_height());
            temp_gph.drawRect(originx, originy, watchregion_manager.get(i).get_width(), watchregion_manager.get(i).get_height());
        }
        for(int i = 0; i < watchpoint_manager.size(); ++i) {
           int localx = watchpoint_manager.get(i).getx(); 
           int localy = watchpoint_manager.get(i).gety();   
           temp_gph.setColor(watchpoint_color);//orange points are redrawn
           temp_gph.fillOval(localx - HALFCENTER, localy - HALFCENTER, squareW, squareH);
           temp_gph.drawOval(localx - HALFCENTER, localy - HALFCENTER, squareW, squareH);
           //System.out.println("Redrawing " + localx + " " + localy); 
        }
        temp_gph.dispose(); 
        this.repaint();
    }
    
    //a debug only method. Erases all drawn obejcts. 
    public void clear_all() {
        Graphics temp_gph = background.createGraphics();
        temp_gph.drawImage(reset_background, 0, 0, null);
        this.repaint();
        temp_gph.dispose();       
    }
     
    /*
    draws the selection rectangle around the selected item, point or center.
    */
    public void select_rect_draw() {
        if(StencilUI.DEBUG_MODE == 1) {
            System.out.println("OLS: " + outline_state);
            System.out.println(watchpoint_manager.size() + " " + watchregion_manager.size());   
        }

        //draw the selection rectangle around the object 
        if(outline_state == watchpoint_state) {
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("render point");   
            }
            Watchpoint currently = watchpoint_manager.get(watchpoint_currently_selected); 
            int drawx = currently.getx(); 
            int drawy = currently.gety(); 

            Graphics2D temp_gph = background.createGraphics();
            //Stroke oldStroke = gph_mgr.getStroke();
            temp_gph.setStroke(new BasicStroke(4));
            temp_gph.setColor(Color.MAGENTA);
            temp_gph.drawRect(drawx - HALFCENTER, drawy - HALFCENTER, 10, 10);
            //gph_mgr.setStroke(oldStroke);
            this.repaint();
            temp_gph.dispose();
        }
        else if(outline_state == watchregion_state) {
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("render region");
                System.out.println(watchregion_currently_selected);
                for(int i = 0; i < watchregion_manager.size(); ++i) {
                    System.out.println(watchregion_manager.get(i).toString());
                }
            }

            Watchregion currently = watchregion_manager.get(watchregion_currently_selected); 
            Graphics2D temp_gph = background.createGraphics();
            //Stroke oldStroke = gph_mgr.getStroke();
            temp_gph.setStroke(new BasicStroke(6));
            temp_gph.setColor(Color.MAGENTA);
            temp_gph.drawRect(currently.get_xorigin(), currently.get_yorigin(), currently.get_width(), currently.get_height());
            //gph_mgr.setStroke(oldStroke);
            this.repaint();
            temp_gph.dispose();
        }
        else if(outline_state == overwatch_state) {
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("render over");
            }
            Watchregion currently = overwatch_manager.get(overwatch_currently_selected); 
            Graphics2D temp_gph = background.createGraphics();
            temp_gph.setStroke(new BasicStroke(6));
            temp_gph.setColor(Color.MAGENTA);
            temp_gph.drawRect(currently.get_xorigin(), currently.get_yorigin(), currently.get_width(), currently.get_height());
            this.repaint();
            temp_gph.dispose();
        }
    }
    
    //returns the most recently selected object ID as a string
    public String watch_string() {
        if(outline_state == watchpoint_state) {
            return watchpoint_manager.get(watchpoint_currently_selected).toString();
        }
        else if(outline_state == watchregion_state) {
            return watchregion_manager.get(watchregion_currently_selected).toString();
        }
        else if(outline_state == overwatch_state) {
            return overwatch_manager.get(overwatch_currently_selected).toString();
        }
        else {
            
        } 
        return "-1"; 
    }
    
    
    /*
    notifies the appropriate object manager of a request to delete the currently selected object. 
    May be accepted or rejested based on object dependents. 
    */
    public void delete_current() {
        //deletes the currently selected object from its respective collection.
        switch(outline_state) {
            case watchpoint_state:
                if(watchpoint_manager.isEmpty()) {
                    break; 
                }
                else {
                    watchpoint_manager.remove(watchpoint_currently_selected); 
                }
                break; 
            case watchregion_state:
                if(watchregion_manager.isEmpty()) {
                    break; 
                }
                else {
                    watchregion_manager.remove(watchregion_currently_selected); 
                }
                break; 
            case overwatch_state:
                if(overwatch_manager.isEmpty()) {
                    break; 
                }
                else {
                    overwatch_manager.remove(overwatch_currently_selected); 
                    if(overwatch_manager.isEmpty()) {
                        overwatch_string = ""; 
                    }
                }
                break;
            default:
                break; 
        }
    }
    
    /*
    Sets the object trigger script candidate as the object trigger for the current object
    */
    public void set_current_object_script(String script_candidate) {
        switch(outline_state) {
            case watchpoint_state:
                if(watchpoint_manager.isEmpty()) {
                    break; 
                }
                else {
                     watchpoint_manager.get(watchpoint_currently_selected).set_ident_string(script_candidate);
                }
                break; 
            case watchregion_state:
                if(watchregion_manager.isEmpty()) {
                    break; 
                }
                else {
                    watchregion_manager.get(watchregion_currently_selected).set_ident_string(script_candidate); 
                }
                break; 
            case overwatch_state:
                if(overwatch_manager.isEmpty()) {
                    break; 
                }
                else {
                    overwatch_string = script_candidate; //all overwatch panels implement same
                }
                break;
            default:
                break; 
        }
    }
    
    /*
    Gets the object trigger script of the currently selected object
    */
    public String get_current_object_script() {
        String fetch = "";
        switch(outline_state) {
            case watchpoint_state:
                if(watchpoint_manager.isEmpty()) {
                    break; 
                }
                else {
                     fetch = watchpoint_manager.get(watchpoint_currently_selected).get_ident_string();
                }
                break; 
            case watchregion_state:
                if(watchregion_manager.isEmpty()) {
                    break; 
                }
                else {
                   fetch = watchregion_manager.get(watchregion_currently_selected).get_ident_string(); 
                }
                break; 
            case overwatch_state:
                if(overwatch_manager.isEmpty()) {
                    overwatch_string = ""; 
                    break; 
                }
                else {
                   fetch = overwatch_string; 
                }
                break;
            default:
                break; 
        }
        return fetch; 
    }
    
    
    //Miscellanious get methods for internal data objects
    
    /*
    Get the current ID of the watchpoint
    */
    public int get_current_watchpoint_id() {
        int current_wpid = watchpoint_manager.get(watchpoint_currently_selected).get_identifier(); 
        return current_wpid; 
    }
    
        /*
    Get the current ID of the watchregion
    */
    public int get_current_watchregion_id() {
        int current_wrid = watchregion_manager.get(watchregion_currently_selected).get_identifier(); 
        return current_wrid; 
    }
    
    
    /*
        Get the watchobject manager objects which contain the object
    */
    public ArrayList<Watchpoint> get_watchpoint_manager() {
        return watchpoint_manager; 
    }
    
    public ArrayList<Watchregion> get_watchregion_manager() {
        return watchregion_manager; 
    }
    
    public ArrayList<Watchregion> get_overwatch_manager() {
        return overwatch_manager; 
    }
    
    public String get_overwatch_script() {
        return overwatch_string; 
    }
    
    /*
    Returns a formatted list of all the script objects and their identifiers
    */
    public String get_object_script_list_tabbedprint() {
        String wmaster = ""; 
        if(!overwatch_string.isEmpty()) {
         wmaster += "Overwatch: \n" + overwatch_string + "\n--------------------------------------------\n\n";   
        }       
        for(Watchpoint w: watchpoint_manager) {
            String substr = "Watchpoint " + w.get_identifier() + ":\n"; 
            substr += w.get_ident_string() + "\n--------------------------------------------\n\n";  
            wmaster += substr; 
        }
        for(Watchregion r: watchregion_manager) {
            String substr = "Watchregion " + r.get_identifier() + ":\n"; 
            substr += r.get_ident_string() + "\n--------------------------------------------\n\n"; 
            wmaster += substr; 
        }
        return wmaster; 
    }
    
    
    /*
    Utility to create a deep copy of the image background for use in case the file passed in frame 2 is deleted for some reason
    during execution between repaints. 
    */
    private  BufferedImage deepCopy(BufferedImage bi) {
        //used to make a deep copy of the background image for reset and redrawing
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = copy.createGraphics();
       // Color myColour = new Color(0, 0, 0, 255); //totally transparent
        //g.setColor(myColour);
        g.drawImage(bi, 0, 0, null); 
        g.dispose(); 
        return copy; 
    }
}
