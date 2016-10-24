/*
The watchpoint watchregion class is represented by two X,Y points that are the upper left and lower right of the bounding rectangle
A watchregion includes the entire area it contains within the width/length of those two points and will check every point contained
within those bounds. Use when the object searched for is known to be within a certain area but too small to reliably detect with 
an overwatch scattering pattern and not known exactly enough to use a watchpoint. 
 */
package stencilui;

/**
 *
 * @author JamesH
 */
public class Watchregion {
    private int identifier; //unique identifying digit, assigned by manager
    private int origin_x; //upper left x and y
    private int origin_y; 
    private int terminal_x; //lower right x and y
    private int terminal_y; 
    private int width; //computed from origin/terminal upon construction
    private int height; 
    private String script_code; //the associated object trigger script with this object
    
    public Watchregion(int identify, int startx, int starty, int endx, int endy) {
        identifier = identify; 
        origin_x = startx; 
        origin_y = starty; 
        terminal_x = endx; 
        terminal_y = endy; 
        script_code = ""; 
        
        width = terminal_x - origin_x; 
        height = terminal_y - origin_y; 
        
        if(StencilUI.DEBUG_MODE == 1) {
          System.out.println("Added wregion: " + origin_x + ", " + origin_y + " " + terminal_x + ", " + terminal_y);  
        } 
    }
    
    
    //get the X/Y
    public int get_xorigin() {
        return origin_x; 
    }
    
    public int get_yorigin() {
        return origin_y; 
    }
    
    
    //get W/L
    public int get_width() {
        return width; 
    }
    public int get_height() {
        return height; 
    }
    
    
    //Set/get object trigger
    public void set_ident_string(String str) {
        script_code = str; 
    }
    
    public String get_ident_string() {
        return script_code; 
    }
    
    
    //get identifier
    public int get_identifier() {
        return identifier; 
    }
    
    //printing debug method
    @Override
    public String toString() {
        return identifier + " (" + Integer.toString(origin_x) + ", " + Integer.toString(origin_y) + ")"; 
    }
}
