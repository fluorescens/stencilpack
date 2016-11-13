/*
Entry point. Invokes the GUI thread and creates a card layout object which manages the interface panels. 

REFACTOR REPORT: TODO
    Still uses debug name for output file. Make it use the supplied name in crunchypack input header. (HIGH)
    Uses String instead of stringbuilder for master output. (LOW: This is not a speed-critical component. Saves milliseconds at best.)
    DrawingFrame.select_rect_draw method should put all rect drawing graphics in a single method. (MEDIUM)
    
 */
package stencilui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author jamesl
 */
public class StencilUI {
     public static final int FHEIGHT = 500; //size of a window
     public static final int FWIDTH = 1100;
     static Dimension fsize = new Dimension(FWIDTH, FHEIGHT); //default window size
     public final static int DEBUG_MODE = 0; //debug bit. 1 for on

     
    public static void main(String[] args) {
        
            SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        //GUI queue
        FrameManager(); 
      }
    });
    }
    
    /*
    Create card layout object. All frames are displayed within the cardlayout and use cardlayout navigation controls. 
    */
    static void FrameManager() {
        CardsManager c1 = new CardsManager();
        c1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c1.setLocationRelativeTo(null); 
        c1.pack();
        c1.setVisible(true); 
    }

}