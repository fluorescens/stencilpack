/*
The watchobject overwatch is a unique singleton. While a watchregion checks every point in its bounds, an overwatch bounds its region like 
a watchregion but places an internal watchpoint at every "scalability multiple" location within the region. So an overwatch may only check every 10th 
or 100th pixel in its bounds, saving a massive factor of lookup power. All overwatch regions share the same object trigger script, even if
they are drawn as seperate regions. Use these as a much faster way to search a large region of area where the location will not be known
but searching every pixel (like a whole screen for semi-large objects) would take prohibitivly long. 
 */
package stencilui;
import java.util.ArrayList; 

/**
 *
 * @author jamesl
 */

//there is only ever ONE overwatch object. 
public class Overwatch {
    //...creating on overwatch region really creates an internal rectangle to manage in this class
    ArrayList<Watchregion> rectangle_manager; 
    //each added overwatch rectangle 
    //ArrayList<Watchpoint> internal_points;  //DEPRECIATED possibly depreciated in next build. 
    
    
    int point_density = 0; // DEPRECIATED this factor is actually set on the interpreter side via user entry. Has no effect here. 
    
    final int original_w; //DEPRECIATED, used for testing purposes
    final int original_h; 
    
    public Overwatch(int original_width, int original_height) {
        rectangle_manager = new ArrayList<>(); //manages the internal watchregions that make up overwatch
        
        
        original_w = original_width; //DEPRECIATED 
        original_h = original_height; 
    }
    
    //Adds an externally created watchregion as an overwatch region.
    //BUILD internalize this functionality, construct via points instead of external creation. 
    public void add_member(Watchregion adder) {
        rectangle_manager.add(adder); 
    }
    
    //depreciated
    public void set_density(int density_factor) {
        //DEPRECIATED
    }
    
    //provide access to the composite regions
    public ArrayList<Watchregion> get_rectangles() {
        return rectangle_manager; 
    }
}
