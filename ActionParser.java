/*
This class parses the action script candidate strings for syntatical correctness
If they are correct, it adds them to the action script collection and makes them available for refrence
by objects 

TODO LIST:
    Regex into a regex list, compact search subgroups into a single tokenizing function.
 */
package stencilui;
import java.util.ArrayList; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayDeque; 

/**
 *
 * @author jamesl
 */
public class ActionParser {
    ArrayList<String> action_manager; //list of all available action scripts
    ArrayDeque<String> error_notifications; //list of errors for a given compile run
    
    public ActionParser() {
        action_manager = new ArrayList<>();   
        error_notifications = new ArrayDeque<>(); 
    }
    
    /*
    Possible regex matches for tokens
    */
    public String add_action(String candidate) {
        final Pattern token_action_do_alpstring = Pattern.compile("DO\\[[0-9]{1,999},[a-z]{1,999}\\]");
        final Pattern token_action_do_alpnumeric = Pattern.compile("DO\\[[0-9]{1,999},[0-9]{1,999}\\]");
        final Pattern token_action_do_imgcap = Pattern.compile("DO\\[[0-9]{1,999},\\*\\]"); 
        final Pattern token_action_do_lmouse = Pattern.compile("DO\\[[0-9]{1,999},\\<\\]"); 
        final Pattern token_action_do_rmouse = Pattern.compile("DO\\[[0-9]{1,999},\\>\\]"); 
        final Pattern token_action_do_enter = Pattern.compile("DO\\[[0-9]{1,999},\\#\\]");
        final Pattern token_action_do_flush = Pattern.compile("DO\\[[0-9]{1,999},\\!\\]"); 
        final Pattern token_action_do_delay = Pattern.compile("DELAY\\[[0-9]{1,999},[0-9]{1,999}\\]"); 
        final Pattern token_action_do_center = Pattern.compile("CENTER\\[[0-9]{1,999}\\]");
        
        String testme = ""; 
        for(int i = 0; i < candidate.length(); ++i) {
            if(candidate.charAt(i) == '\n'  || candidate.charAt(i) == ' ') {
                //strip all newlines and whitespaces, formatting only. 
            }
            else {
                testme += candidate.charAt(i); 
            }
        }
        if(StencilUI.DEBUG_MODE == 1) {
            System.out.println(testme);   
        }

        ArrayDeque<Token> tmp = new ArrayDeque<>(); 
        
        
        /*
        Verify the candidate string against regex, try to tokenize into sections. 
        */
        Matcher m = token_action_do_alpstring.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("String " + m.start() + " " + m.end());      
            }
        } 
        m.reset(); 
        m = token_action_do_alpnumeric.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("numeric " + m.start() + " " + m.end());      
            }
        } 
        m.reset(); 
        m = token_action_do_imgcap.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token);
            if(StencilUI.DEBUG_MODE == 1) {
              System.out.println("imgcap " + m.start() + " " + m.end());     
            } 
        } 
        m.reset(); 
        m = token_action_do_lmouse.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("lmouse " + m.start() + " " + m.end());      
            }
        } 
        m.reset(); 
        m = token_action_do_rmouse.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
               System.out.println("rmouse " + m.start() + " " + m.end());    
            }
        } 
        m.reset(); 
        m = token_action_do_enter.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("enter " + m.start() + " " + m.end());    
            }
        } 
        m.reset(); 
        m = token_action_do_flush.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("flush " + m.start() + " " + m.end());      
            }
        } 
        m.reset(); 
        m = token_action_do_delay.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                 System.out.println("delay " + m.start() + " " + m.end()); 
            }
        } 
        m.reset(); 
        m = token_action_do_center.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            tmp.add(token); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("center " + m.start() + " " + m.end());     
            }

        }
        
        /*
        Process the tokenized string
        */
        String errno = "";  
        if(!tmp.isEmpty()) { //have tokens
            Qsort dat = new Qsort(tmp); //sort the tokens by their start number
            tmp = dat.get_sorted(); 
            System.out.println(dat.toString());
            int contig_ok = check_contig(tmp, testme); //test for contiguity
            if(error_notifications.isEmpty()) {
                for(Token t: tmp) {
                    if(StencilUI.DEBUG_MODE == 1) {
                         System.out.println(t.start + " " + t.end);
                    }
                }
                action_manager.add(candidate); 
            }
            else { 
                for(String err: error_notifications) {
                    errno += err + '\n'; 
                }
            }
        }
        else {
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println("This contains no matching tokens");   
            }
        }
        return errno; 
    }
    
    @Override
    public String toString() {
        String majdat = ""; 
        for(int i = 0; i < action_manager.size(); ++i) {
            String substr = "Action script " + i + ": \n"; 
            substr += action_manager.get(i) + "\n---------------------------\n\n"; 
            majdat += substr; 
        }
        return majdat; 
    }
    
    /*
    Check if a list of tokens sorted by their start regex location in the candidate string is contiguous 
    A gap indicates a regex match failure. 
    */
    private int check_contig(ArrayDeque<Token> tokenlist, String testing) {
        //System.out.println("SZ: " + tokenlist.size());
        int leave_off = 0; 
        if(tokenlist.isEmpty()) {
            String empty_error = "The script is empty."; 
            error_notifications.add(empty_error);
            return 1; 
        }
        for (Token t : tokenlist) {
            int last = t.get_start(); 
            if(last != (leave_off)) {
                String error = "Syntax matching error between " + leave_off + " and " + last;
                error_notifications.add(error);
                //tokenlist not added on bad return
                return 1;  
            }
            else {
                leave_off = t.get_end(); 
            }
        }
        return 0; 
    }
    
    
    public ArrayList<String> get_actions() {
        return action_manager; 
    }
    
    
    
/*
    Each valid regex match generates a token with its start-end location in the candidate string
    */
private class Token {
    final private int start; 
    final private int end;
    
    public Token(int s, int e) {
        start = s; 
        end = e; 
    }
    
    @Override
    public String toString() {
        String substr = start + "," + end;
        return substr; 
    }
    
    public int get_start() {
        return start; 
    }
    
    public int get_end() {
        return end; 
    }
    
    
}
    
    /*
QUicksort utility function to order a token array by their start value. 
*/
    private class Qsort {
    private ArrayList<Token> tdata; 
    
    public Qsort(ArrayDeque<Token> keydata) {
        tdata = new ArrayList<>(); 
        for (Token t : keydata) {
            tdata.add(t); 
        }
        quicksort(0, tdata.size() - 1); 
    }
    
    private void quicksort(int lowerindex, int higherindex) {
        //get a pivot value, choose middle of array
        int low = lowerindex; 
        int high = higherindex; 
        int pivotindex = tdata.get((lowerindex + higherindex) / 2).get_start(); 
        while(low <= high) {
            while(tdata.get(low).get_start() < pivotindex) {
                ++low; 
            }
            while(tdata.get(high).get_start() > pivotindex) {
                --high; 
            }
            if(low <= high) {
                swap(low, high); 
                ++low; 
                --high; 
            }
            
            if(lowerindex < low) {
                quicksort(lowerindex, high); 
            }
            if(low < higherindex) {
                quicksort(low, higherindex); 
            }
        } 
    }
    
    private void swap(int low, int high) {
        Token temporary = tdata.get(low); 
        tdata.set(low, tdata.get(high)); 
        tdata.set(high, temporary);  
    }
    
    @Override
    public String toString() {
        String ret = ""; 
        for(int i = 0; i < tdata.size(); ++i) {
            ret += " (" + tdata.get(i).get_start() + "," + tdata.get(i).get_end() + ")"; 
        }
        return ret; 
    }
    
    public ArrayDeque<Token> get_sorted() {
        //list to deque return
        ArrayDeque<Token> tdek = new ArrayDeque<>(); 
        for(int i = 0; i < tdata.size(); ++i) {
            tdek.add(tdata.get(i)); 
        }
        return tdek; 
    }
}
    
}
