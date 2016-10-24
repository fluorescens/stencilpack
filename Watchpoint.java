/*
The watchpoint watchobject class, whose region consists of a single X, Y point. 
Used when locations of a pixel are known exactly and only need to determine when a certain pixel color appears there. 
 */
package stencilui;

/**
 *
 * @author jamesl
 */
public class Watchpoint {
    private int identifier; //the unique ID of this point, assigned by manager and used as primary key
    private int solid_x; //the X and Y
    private int solid_y; 
    private String script_code; //the object trigger script associated with this object
    
    public Watchpoint(int counter, int x, int y) {
        identifier = counter; 
        solid_x = x;
        solid_y = y; 
        script_code = ""; 
        
        if(StencilUI.DEBUG_MODE == 1) {
            System.out.println("Added wchpt: " + solid_x + ", " + solid_y);   
        }
    }
    
    //get the x and y 
    public int getx() {
        return solid_x; 
    }
    
    public int gety() {
        return solid_y; 
    }
    
    
    //get/sets the associated object trigger script
    public void set_ident_string(String str) {
        script_code = str; 
    }
    
    public String get_ident_string() {
        return script_code; 
    }
    
    
    //get the unique identifier as int or string
    public int get_identifier() {
        return identifier; 
    }
    
    public String get_identifier_as_string() {
        return Integer.toString(identifier); 
    }
    
    
    //a to-string method for verbose debugging
    @Override
    public String toString() {
        return Integer.toString(identifier) + " (" + Integer.toString(solid_x) + ", " + Integer.toString(solid_y) + ")"; 
    }
}
