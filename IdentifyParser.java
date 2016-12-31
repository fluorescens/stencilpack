/*
Given a candidate string M, this class attempts to verify the syntatic and programmatic correctness of the string before
assigning it to a watchobject.
Its functionality includes testing syntax, checking for safe refrences to other objects for GET calls, testing that GET and SET calls
only can call existing objects, and maintaining a dependency tree so that attempting to delete watchobject A, 
if B has a GET call for A, requires deleting B and any other objects that share GET calls. 

Before attempting to read how this program operates, one should review the crunchylang language documentation. 
 */
package stencilui;
import java.util.ArrayList; 
import java.util.ArrayDeque; 
import java.util.HashMap;
import java.util.regex.Pattern; 
import java.util.regex.Matcher;
import java.util.Objects;
import java.util.Map; 

/**
 *
 * @author jamesl
 */
public class IdentifyParser {
    //management objects
    /*
    Watchpoint 1 : Has SET indexes <array>{0,1,2} available for GET calls
    */
    private ArrayDeque<Integer> overwatch_sets; //the one and only overwatch set
    private HashMap<Integer, ArrayDeque<Integer>> watchpoint_hmap_sets; //this watchpoint identifer has these SET indexes available
    private HashMap<Integer, ArrayDeque<Integer>> watchregion_hmap_sets; //this watchregionID has these SET indexes available
    private ArrayDeque<String> error_notifications = new ArrayDeque<>(); //list of compile errors
    private ArrayList<ArrayList<String>> image_names; //the list of image names
    
    //constant state identifiers
    final private int watchpoint_state = Frame_3.WATCHPOINT_STATE; 
    final private int watchregion_state = Frame_3.WATCHREGION_STATE; 
    final private int overwatch_state = Frame_3.OVERWATCH_STATE;
    
    //With W and R prefix, a list of objectID this object requires to be constructred. 
    /*
    Key W3: {1,2}  : Watchpoint 0 requires watchpoint 1 and 2 to be constructed (3 depends on 2 and 1)
    Key R1: {0,1} :  Watchregion 1 requires watchpoint 0 and 1 to be constructed
    
    Each deque contains the numeric identifier of tha type of object required to build that object
    So the watchregion_depdndent links an object identifier to the numeric identifiers of watchregions required to build it. 
    */
    private Map<String, ArrayDeque<Integer>> watchpoint_dependent; 
    private Map<String, ArrayDeque<Integer>> watchregion_dependent;  
    private Map<String, ArrayDeque<Integer>> overwatch_dependent; 
    
    
    //list of available action scripts. 
    private ArrayList<String> action_scripts; 
    
    
    //initialized with the image name strings
    public IdentifyParser(ArrayList<ArrayList<String>> image_name_collection) {
        image_names = image_name_collection; 
        overwatch_sets = new ArrayDeque<>(); 
        watchpoint_hmap_sets = new HashMap <>(); 
        watchregion_hmap_sets = new HashMap<>(); 
        
        watchpoint_dependent = new HashMap <>();
        watchregion_dependent = new HashMap <>();
        overwatch_dependent = new HashMap <>();
        
        action_scripts = new ArrayList<>(); 

    }
    
    
    /*
    Takes a candidate string for an overwatch object and attempt to verify the string for correctness
    */
    public String parse_overwatch(String source) {
        /*
        The available tokens to build the overwatch object
        BUILD: Unify the similar tokens from the three classes in a master table. 
        */
        final Pattern token_overwatch_NOT = Pattern.compile("NOT");
        final Pattern token_overwatch_IF = Pattern.compile("IF\\[[0-9]+(\\.[0-9]{1,10})?\\]"); 
        final Pattern token_overwatch_AND = Pattern.compile("AND"); 
        final Pattern token_overwatch_OR = Pattern.compile("OR"); 
        final Pattern token_SET_CONDITION = Pattern.compile("SET_CONDITION\\[[0-9],[0-9]\\]");
        final Pattern token_DO_ERROR = Pattern.compile("DO\\[[0-9]{1,100}\\]");
        
        
        //Strip newlines and whitespaces from the string before processing
        //BUILD: Refactor using a trim method instead
        //BUILD: refactor with stringbuilder. 
        String testme = ""; 
        for(int i = 0; i < source.length(); ++i) {
            if(source.charAt(i) == '\n'  || source.charAt(i) == ' ') {
                //strip all newlines and whitespaces, formatting only. 
            }
            else {
                testme += source.charAt(i); 
            }
        }
        
        
        //An array of all the token objects that composed the string, discvered by regex. 
        //Read token class documentation for information
        ArrayDeque<Token> overwatch_tokens = new ArrayDeque<>(); 
        
        //A list of all the SET indexes this object makes available.
        //Will be added to the global SET array overwtach_sets. if the entire string is parsed without error
        ArrayDeque<Integer> set_sources = new ArrayDeque<>(); 
        
        //A hashmap used to make sure in O(1) that a SET is added only once to the candidate set_sources,
        //even if it is refrenced multiple times: Like in IF[2] SET[0,1] IF[3] SET[0,4] would add set 0 twice without this construct
        HashMap<Integer, Integer> hmap_occ = new HashMap<>(); 
        
        
        /*
        Scanning the entire string attempting to create a contiguous list of TOKENS. 
        If it fails to create a compleately contiguous list, the compile for the string fails with an error
        about where the gaps are located. 
        */
        Matcher m = token_overwatch_NOT.matcher(testme);
        while(m.find()) {
            //on successful match, make a token and add to the collection.
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            overwatch_tokens.add(token); 
        }
        m.reset(); 
        m = token_overwatch_IF.matcher(testme); 
        while(m.find()) {
            //search the token item for a float number which must match the available range.
            final int pad = 3; //amount of symbols before number starts (IF[ )
            int start = m.start(); 
            int end = m.end(); 
            String verif = ""; 
            verif = testme.substring(start + pad, end - 1); //extract the image numeric identifer
            int argument_ok = verify_argument(verif, pad, image_names); //verify groups and image refrenced are real
            if(argument_ok != -1) {
                //attempted to refrence an invalid image identifier, add an error. 
                if(StencilUI.DEBUG_MODE == 1) {
                    
                }
                String error = "Number " + argument_ok + " is not a valid group or item number."; 
                error_notifications.add(error); 
            }
            else {
                //System.out.println("Arg ok");
            }
            //create and add token
            Token token = new Token(start, end); 
            overwatch_tokens.add(token); 
            //System.out.println("IF " + m.start() + " " + m.end());
        }
        m.reset(); 
        m = token_overwatch_AND.matcher(testme); 
        while(m.find()) {
            //static token
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            overwatch_tokens.add(token); 
            //System.out.println("AND " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_overwatch_OR.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            overwatch_tokens.add(token); 
            //System.out.println("OR " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_SET_CONDITION.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            int extractor_distance = 14; //read this many ahead to extract the condition flag, SET_CONDITION[
            String substr = ""; //extract the SET index
            for(int i = (start + extractor_distance); i < testme.length(); ++i) {
                if(testme.charAt(i) == ',') {
                    break; 
                }
                else {
                    substr += testme.charAt(i); 
                }
            }
            //System.out.println("GP" + substr + "-");
            try {
                //test if this SET index has already been added, if not, add
                Integer item = Integer.parseInt(substr); 
                if(!hmap_occ.containsKey(item)) {
                    hmap_occ.put(item, 1); 
                    set_sources.add(item); //Add the SET index to map and the available SET sources
                }
                else {
                    //do nothing, don't double add SETS
                }
            }
            catch(NumberFormatException e) {
                error_notifications.add("Convert failure");
            }
            //extract and add set identifers
            Token token = new Token(start, end); 
            overwatch_tokens.add(token); 
            //System.out.println("SET " + m.start() + " " + m.end());
        }  
        m.reset(); 
        m = token_DO_ERROR.matcher(testme); 
        while(m.find()) {
            //Overwatch cannot implement action scripts as they would be executed for EVERY SUBWATCHOBJECT in the region
            //instead, add a watchpoint that just checks the flag state of the overwatch. 
            error_notifications.add("Overwatch is not allowed to implement a DO operation."); 
        }  
        
        //if we have some tokens...
        if(!overwatch_tokens.isEmpty()) {
            /*
            Need to sort the token fragments into order so they can be parsed in one pass and checked for gaps. 
            A gap results in compile failure. 
            */
            Qsort dat = new Qsort(overwatch_tokens); 
            overwatch_tokens = dat.get_sorted(); 
            int contig_ok = check_contig(overwatch_tokens, testme); 
            if(StencilUI.DEBUG_MODE == 1) {
                System.out.println(dat.toString());
                System.out.println(contig_ok);
            }
            //BUILD: Replace with stringbuilder. 
            String err_tmp = "";
            if(error_notifications.isEmpty()) {
                //syntatically the item is verified as an overwatch with valid calls. 
                //Make it available as an overwatch resource. 
                overwatch_sets = set_sources; 
                if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Overwatch verified"); 
                }
            }
            else { 
                //errors encountered
                for(String err: error_notifications) {
                    err_tmp += err + '\n'; 
                }
            }
            error_notifications.clear();
            return err_tmp; 
        }
        else {
            //submitted an empty or string that could not be resolved to contain any tokens
            String noitems = "No statements could be resolved. All candidates syntatically incorrect."; 
            for(String err: error_notifications) {
                noitems += '\n' + err; 
            }
            error_notifications.clear(); 
            return noitems; 
        } 
    }
    
    
    
    
    
    /*
    Read description of parse_overwatch function. Similar documentation. 
    */
    public String parse_watchpoint(int watchpoint_id, String sample) {
        final Pattern token_watchpoint_NOT = Pattern.compile("NOT");
        final Pattern token_watchpoint_AND = Pattern.compile("AND"); 
        final Pattern token_watchpoint_OR = Pattern.compile("OR"); 
        final Pattern token_watchpoint_IF = Pattern.compile("IF\\[[0-9]{1,100}+(\\.[0-9]{1,100})?\\]"); 
        final Pattern token_watchpoint_IF_GET = Pattern.compile("IF\\[GET_CONDITION\\[((O,)|(P[0-9]{1,100},)|(R[0-9]{1,100},))?[0-9]{1,100},[0-9]{1,100}\\]\\]"); 
        final Pattern token_watchpoint_SET_CONDITION = Pattern.compile("SET_CONDITION\\[[0-9]{1,100},[0-9]{1,100}\\]"); 
        final Pattern token_watchpoint_DO = Pattern.compile("DO\\[[0-9]+\\]");

        
        String testme = ""; 
        for(int i = 0; i < sample.length(); ++i) {
            if(sample.charAt(i) == '\n'  || sample.charAt(i) == ' ') {
                //strip all newlines and whitespaces, formatting only. 
            }
            else {
                testme += sample.charAt(i); 
            }
        }
        
        ArrayDeque<Token> local_tokens = new ArrayDeque<>(); 
        HashMap<Integer, Integer> hmap_occ = new HashMap<>(); 
        ArrayDeque<Integer> set_sources = new ArrayDeque<>();
        

        //make sure each get call is filtered only once.
        HashMap<Integer, Integer> foreign_calls_watchpoint = new HashMap<>(); 
        HashMap<Integer, Integer> foreign_calls_watchregion = new HashMap<>(); 
        HashMap<Integer, Integer> foreign_calls_overwatch = new HashMap<>();
        //add to dependent-on deque
        ArrayDeque<Integer> dependent_on_calls_watchpoint = new ArrayDeque<>(); 
        ArrayDeque<Integer> dependent_on_calls_watchregion = new ArrayDeque<>();
        ArrayDeque<Integer> dependent_on_calls_overwatch = new ArrayDeque<>();
        //add map contents to deque, then add deque to the global hashmap for the object. 
        //See 4: depends on watchpoints 3 which depends on 2.
        //delete 2: search all watchpoints. if 2 found in depends-on, add to depends-on deque.
        //if next in search contains any in deque, also dependent. 
        
        
        Matcher m = token_watchpoint_NOT.matcher(testme);
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("NOT " + m.start() + " " + m.end()); 
        }
        m.reset(); 
        m = token_watchpoint_IF.matcher(testme); 
        while(m.find()) {
            //search the token item for a float number which must match the available range.
            final int pad = 3; //amount of symbols before number starts
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("IF " + m.start() + " " + m.end());
        }
        m.reset(); 
        m = token_watchpoint_AND.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("AND " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_watchpoint_OR.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("OR " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_watchpoint_SET_CONDITION.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            int extractor_distance = 14; 
            String substr = ""; 
            for(int i = (start + extractor_distance); i < testme.length(); ++i) {
                if(testme.charAt(i) == ',') {
                    break; 
                }
                else {
                    substr += testme.charAt(i); 
                }
            }
            //System.out.println("GP" + substr + "-");
            try {
                Integer item = Integer.parseInt(substr); 
                if(!hmap_occ.containsKey(item)) {
                    hmap_occ.put(item, 1); 
                    set_sources.add(item); 
                }
                else {
                    //Already previously added, no need to double add. 
                }
            }
            catch(NumberFormatException e) {
                error_notifications.add("Convert failure");
            }
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("SET " + m.start() + " " + m.end());
        } 
        
        m.reset(); 
        m = token_watchpoint_IF_GET.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            //parse deque matching watchpoint_id and assure the identifier exists locally and globally
            /*
            Determine if its a local or foreign GET. Local GET exists in hash queue. Foreign GET queries the deque
            */
            final int bofset = 17; //18th character is W O R in a forgein get. local get will default. 
            int foreign_id = -1; 
            switch(testme.charAt(start + bofset)) {
                case 'P':
                    //check 
                    foreign_id = watchpoint_get_if_foreign_call(testme, start);
                    if(foreign_id == -1) {
                        //Something happened. Error state. 
                            String get_foreign_parse_failure = "Failed to parse foreign watchpoint refrence at " + (start + bofset);
                            error_notifications.add(get_foreign_parse_failure); 
                            break; 
                    }
                    //now test if foreign ID exists in available watchpoints
                    if(watchpoint_hmap_sets.containsKey(foreign_id)) {
                        Integer foreign_index = watchpoint_get_if_foreign_index(testme, start);
                        if(foreign_index == -1) {
                            //Something happened. Error state. 
                                String get_foreign_index_parse_failure = "Failed to parse the index for foreign watchpoint" + foreign_id;
                                error_notifications.add(get_foreign_index_parse_failure); 
                                break; 
                        }
                        ArrayDeque<Integer> check = watchpoint_hmap_sets.get(foreign_id); 
                        boolean did_find = false; 
                        for(Integer i: check) {
                            if(Objects.equals(i, foreign_index)) {
                               did_find = true; 
                               break; 
                            }
                        }
                        if(did_find == true) {
                            if(!foreign_calls_watchpoint.containsKey(foreign_id)){
                                foreign_calls_watchpoint.put(foreign_id, 1); 
                            }
                            else {
                                
                            }
                        }
                        else {
                            String no_such_index = "The watchpoint " + foreign_id + " doesn't have a condition " + foreign_index;
                            error_notifications.add(no_such_index); 
                        }
                    }
                    else {
                        String no_such_watchpoint = "The watchpoint " + foreign_id + " doesn't exist.";
                        error_notifications.add(no_such_watchpoint); 
                    }
                    break; 
                case 'R':
                    //same as W except check watchregion collection
                    foreign_id = watchpoint_get_if_foreign_call(testme, start);
                    if(foreign_id == -1) {
                        //Something happened. Error state. 
                            String get_foreign_parse_failure = "Failed to parse foreign watchregion refrence at " + (start + bofset);
                            error_notifications.add(get_foreign_parse_failure); 
                            break; 
                    }
                    //now test if foreign ID exists in available watchpoints
                    if(watchregion_hmap_sets.containsKey(foreign_id)) {
                        Integer foreign_index = watchpoint_get_if_foreign_index(testme, start);
                        if(foreign_index == -1) {
                            //Something happened. Error state. 
                                String get_foreign_index_parse_failure = "Failed to parse the index for foreign watchregion" + foreign_id;
                                error_notifications.add(get_foreign_index_parse_failure); 
                                break; 
                        }
                        ArrayDeque<Integer> check = watchregion_hmap_sets.get(foreign_id); 
                        boolean did_find = false; 
                        for(Integer i: check) {
                            if(Objects.equals(i, foreign_index)) {
                               did_find = true; 
                               break; 
                            }
                        }
                        if(did_find == true) {
                            if(!foreign_calls_watchregion.containsKey(foreign_id)){
                                foreign_calls_watchregion.put(foreign_id, 1); 
                            }
                            else {
                                
                            }  
                        }
                        else {
                            String no_such_index = "The watchregion " + foreign_id + " doesn't have a condition " + foreign_index;
                            error_notifications.add(no_such_index); 
                        }
                    }
                    else {
                        String no_such_watchpoint = "The watchregion " + foreign_id + " doesn't exist.";
                        error_notifications.add(no_such_watchpoint); 
                    }
                    break; 
                case 'O':
                    Integer foreign_index = watchpoint_get_if_foreign_index(testme, start);
                    if(foreign_index == -1) {
                        //Something happened. Error state. 
                        String get_foreign_index_parse_failure = "Failed to parse the index for overwatch condition" + foreign_id;
                        error_notifications.add(get_foreign_index_parse_failure); 
                        break; 
                    }
                    boolean did_find = false; 
                    for(Integer i: overwatch_sets) {
                        if(Objects.equals(i, foreign_index)) {
                            did_find = true; 
                            break; 
                        }
                    }
                    if(did_find == true) {
                            if(!foreign_calls_overwatch.containsKey(0)){
                                foreign_calls_overwatch.put(0, 1); 
                            }
                            else {
                                
                            }  
                    }
                    else {
                        String no_such_index = "The overwatch point doesn't have a condition " + foreign_index;
                        error_notifications.add(no_such_index); 
                    }
                    break; 
                default:
                    //Local condition check
                    //only need to check the local cache to see it appears before
                    //if not, enqueue error that call index before declare index
                    int local_id = watchpoint_get_if_local_call(testme, start); 
                    if(local_id == -1) {
                            String get_local_parse_failure = "Failed to parse local watchpoint index at " + (start + bofset);
                            error_notifications.add(get_local_parse_failure); 
                            break; 
                    }
                    boolean is_in_local = false; 
                    for(Integer t : set_sources) {
                        if(t == local_id) {
                            is_in_local = true;
                            break; 
                        }
                        else {
                            
                        }
                    }
                    if(is_in_local == true) {
                        
                    }
                    else {
                        String get_local_index_failure = "There is no condition " + local_id + " in this watchpoint";
                        error_notifications.add(get_local_index_failure); 
                    }
                    break; 
            }
            Token token = new Token(start, end); 
            local_tokens.add(token);  
            //System.out.println("IF-GET " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_watchpoint_DO.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            //System.out.println("FO" + start + "-" + end);
            String substr = testme.substring(start + 3, end - 1);
            try {
                int getscript = Integer.parseInt(substr);
                if( (getscript < action_scripts.size()) && (getscript >= 0) ) {
                    Token token = new Token(start, end); 
                    local_tokens.add(token);
                    if(StencilUI.DEBUG_MODE == 1) {
                       System.out.println("AS" + getscript + "-" + action_scripts.size());   
                    }
                }
                else {
                    error_notifications.add("DO statements did not include a valid script number. ");  
                }
            }
            catch(NumberFormatException e) {
                error_notifications.add("DO statements did not include a valid script number. "); 
            }
        }
        
        
        if(!local_tokens.isEmpty() || sample.length() == 0) {
            Qsort dat = new Qsort(local_tokens); 
            local_tokens = dat.get_sorted(); 
            //System.out.println(dat.toString());
            int contig_ok = check_contig(local_tokens, testme); 
            //System.out.println("SQ:" + dat.toString());
            //verify arguments called exist at this point
            String errno = "";  
            if(error_notifications.isEmpty()) {
                for (Integer key : foreign_calls_watchpoint.keySet()) {
                    dependent_on_calls_watchpoint.add(key); 
                }
                if(!dependent_on_calls_watchpoint.isEmpty()) {
                    if(StencilUI.DEBUG_MODE == 1) {
                       System.out.println("WP dependents : " + dependent_on_calls_watchpoint.size());  
                    }
                    //this object P_ID depends on these watchpoints
                    watchpoint_dependent.put("P" + Integer.toString(watchpoint_id), dependent_on_calls_watchpoint);   
                }
                for (Integer key : foreign_calls_watchregion.keySet()) {
                    dependent_on_calls_watchregion.add(key); 
                }
                if(!dependent_on_calls_watchregion.isEmpty()) {
                    if(StencilUI.DEBUG_MODE == 1) {
                       System.out.println("WR dependents : " + dependent_on_calls_watchregion.size()); 
                    }
                    //This point object depends on these watchregions
                    watchregion_dependent.put("P" + Integer.toString(watchpoint_id), dependent_on_calls_watchregion);   
                }
                for (Integer key : foreign_calls_overwatch.keySet()) {
                    dependent_on_calls_overwatch.add(key); 
                }
                if(!dependent_on_calls_overwatch.isEmpty()) {
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("O dependents : " + dependent_on_calls_overwatch.size());    
                    }
                    overwatch_dependent.put("P" + Integer.toString(watchpoint_id), dependent_on_calls_overwatch); //watchpoint depends on these items   
                }

                watchpoint_hmap_sets.put(watchpoint_id, set_sources); //this watchpoint has source indexes available
                if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Watchpoint " + watchpoint_id + " verified");    
                }
                return errno; 
            }
            else {
                for(String error: error_notifications) {
                    errno += error;
                }
                error_notifications.clear();
                return errno; 
            }
        }
        else {
            String noitems = "No statements could not be resolved."; 
            for(String err: error_notifications) {
                noitems += '\n' + err; 
            }
            error_notifications.clear(); 
            return noitems;  
        }
    }
    
    
    
    /*
    Read description of parse_overwatch function. Similar documentation. 
    */
    public String parse_watchregion(int watchregion_id, String sample) {
        final Pattern token_watchpoint_NOT = Pattern.compile("NOT");
        final Pattern token_watchpoint_AND = Pattern.compile("AND"); 
        final Pattern token_watchpoint_OR = Pattern.compile("OR"); 
        final Pattern token_watchpoint_IF = Pattern.compile("IF\\[[0-9]{1,100}+(\\.[0-9]{1,100})?\\]"); 
        final Pattern token_watchpoint_IF_GET = Pattern.compile("IF\\[GET_CONDITION\\[((O,)|(P[0-9]{1,100},)|(R[0-9]{1,100},))?[0-9]{1,100},[0-9]{1,100}\\]\\]"); 
        final Pattern token_watchpoint_SET_CONDITION = Pattern.compile("SET_CONDITION\\[[0-9]{1,100},[0-9]{1,100}\\]"); 
        final Pattern token_watchregion_DO = Pattern.compile("DO\\[[0-9]+\\]");
        
        String testme = ""; 
        for(int i = 0; i < sample.length(); ++i) {
            if(sample.charAt(i) == '\n'  || sample.charAt(i) == ' ') {
                //strip all newlines and whitespaces, formatting only. 
            }
            else {
                testme += sample.charAt(i); 
            }
        }
        
        ArrayDeque<Token> local_tokens = new ArrayDeque<>(); 
        HashMap<Integer, Integer> hmap_occ = new HashMap<>(); 
        ArrayDeque<Integer> set_sources = new ArrayDeque<>(); 
        
        //maps for building the dependency deques
        HashMap<Integer, Integer> foreign_calls_watchpoint = new HashMap<>(); 
        HashMap<Integer, Integer> foreign_calls_watchregion = new HashMap<>(); 
        HashMap<Integer, Integer> foreign_calls_overwatch = new HashMap<>();
        //add to dependent-on deque
        ArrayDeque<Integer> dependent_on_calls_watchpoint = new ArrayDeque<>(); 
        ArrayDeque<Integer> dependent_on_calls_watchregion = new ArrayDeque<>();
        ArrayDeque<Integer> dependent_on_calls_overwatch = new ArrayDeque<>();
        
        
        Matcher m = token_watchpoint_NOT.matcher(testme);
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("NOT " + m.start() + " " + m.end()); 
        }
        m.reset(); 
        m = token_watchpoint_IF.matcher(testme); 
        while(m.find()) {
            //search the token item for a float number which must match the available range.
            final int pad = 3; //amount of symbols before number starts
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("IF " + m.start() + " " + m.end());
        }
        m.reset(); 
        m = token_watchpoint_AND.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("AND " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_watchpoint_OR.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("OR " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_watchpoint_SET_CONDITION.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            int extractor_distance = 14; 
            String substr = ""; 
            for(int i = (start + extractor_distance); i < testme.length(); ++i) {
                if(testme.charAt(i) == ',') {
                    break; 
                }
                else {
                    substr += testme.charAt(i); 
                }
            }
            //System.out.println("GP" + substr + "-");
            try {
                Integer item = Integer.parseInt(substr); 
                if(!hmap_occ.containsKey(item)) {
                    hmap_occ.put(item, 1); 
                    set_sources.add(item); 
                }
                else {
                    //Already previously added, no need to double add. 
                }
            }
            catch(NumberFormatException e) {
                if(StencilUI.DEBUG_MODE == 1) {
                    
                }
                System.out.println("Convert failure");
            }
            Token token = new Token(start, end); 
            local_tokens.add(token); 
            //System.out.println("SET " + m.start() + " " + m.end());
        } 
        
        m.reset(); 
        m = token_watchpoint_IF_GET.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            //parse deque matching watchpoint_id and assure the identifier exists locally and globally
            /*
            Determine if its a local or foreign GET. Local GET exists in hash queue. Foreign GET queries the deque
            */
            final int bofset = 17; //18th character is W O R in a forgein get. local get will default. 
            int foreign_id = -1; 
            switch(testme.charAt(start + bofset)) {
                case 'P':
                    //check 
                    foreign_id = watchpoint_get_if_foreign_call(testme, start);
                    if(foreign_id == -1) {
                        //Something happened. Error state. 
                            String get_foreign_parse_failure = "Failed to parse foreign watchpoint refrence at " + (start + bofset);
                            error_notifications.add(get_foreign_parse_failure); 
                            break; 
                    }
                    //now test if foreign ID exists in available watchpoints
                    if(watchpoint_hmap_sets.containsKey(foreign_id)) {
                        Integer foreign_index = watchpoint_get_if_foreign_index(testme, start);
                        if(foreign_index == -1) {
                            //Something happened. Error state. 
                                String get_foreign_index_parse_failure = "Failed to parse the index for foreign watchpoint" + foreign_id;
                                error_notifications.add(get_foreign_index_parse_failure); 
                                break; 
                        }
                        ArrayDeque<Integer> check = watchpoint_hmap_sets.get(foreign_id); 
                        boolean did_find = false; 
                        for(Integer i: check) {
                            if(Objects.equals(i, foreign_index)) {
                               did_find = true; 
                               break; 
                            }
                        }
                        if(did_find == true) {
                            if(!foreign_calls_watchpoint.containsKey(foreign_id)){
                                foreign_calls_watchpoint.put(foreign_id, 1); 
                            }
                            else {
                                
                            }  
                        }
                        else {
                            String no_such_index = "The watchpoint " + foreign_id + " doesn't have a condition " + foreign_index;
                            error_notifications.add(no_such_index); 
                        }
                    }
                    else {
                        String no_such_watchpoint = "The watchpoint " + foreign_id + " doesn't exist.";
                        error_notifications.add(no_such_watchpoint); 
                    }
                    break; 
                case 'R':
                    //same as W except check watchregion collection
                    foreign_id = watchpoint_get_if_foreign_call(testme, start);
                    if(foreign_id == -1) {
                        //Something happened. Error state. 
                            String get_foreign_parse_failure = "Failed to parse foreign watchregion refrence at " + (start + bofset);
                            error_notifications.add(get_foreign_parse_failure); 
                            break; 
                    }
                    //now test if foreign ID exists in available watchpoints
                    if(watchregion_hmap_sets.containsKey(foreign_id)) {
                        Integer foreign_index = watchpoint_get_if_foreign_index(testme, start);
                        if(foreign_index == -1) {
                            //Something happened. Error state. 
                                String get_foreign_index_parse_failure = "Failed to parse the index for foreign watchregion" + foreign_id;
                                error_notifications.add(get_foreign_index_parse_failure); 
                                break; 
                        }
                        ArrayDeque<Integer> check = watchregion_hmap_sets.get(foreign_id); 
                        boolean did_find = false; 
                        for(Integer i: check) {
                            if(Objects.equals(i, foreign_index)) {
                               did_find = true; 
                               break; 
                            }
                        }
                        if(did_find == true) {
                            if(!foreign_calls_watchregion.containsKey(foreign_id)){
                                foreign_calls_watchregion.put(foreign_id, 1); 
                            }
                            else {
                                
                            }    
                        }
                        else {
                            String no_such_index = "The watchregion " + foreign_id + " doesn't have a condition " + foreign_index;
                            error_notifications.add(no_such_index); 
                        }
                    }
                    else {
                        String no_such_watchpoint = "The watchregion " + foreign_id + " doesn't exist.";
                        error_notifications.add(no_such_watchpoint); 
                    }
                    break; 
                case 'O':
                    Integer foreign_index = watchpoint_get_if_foreign_index(testme, start);
                    if(foreign_index == -1) {
                        //Something happened. Error state. 
                        String get_foreign_index_parse_failure = "Failed to parse the index for overwatch condition" + foreign_id;
                        error_notifications.add(get_foreign_index_parse_failure); 
                        break; 
                    }
                    boolean did_find = false; 
                    for(Integer i: overwatch_sets) {
                        if(Objects.equals(i, foreign_index)) {
                            did_find = true; 
                            break; 
                        }
                    }
                    if(did_find == true) {
                            if(!foreign_calls_overwatch.containsKey(0)){
                                foreign_calls_overwatch.put(0, 1); 
                            }
                            else {
                                
                            }   
                    }
                    else {
                        String no_such_index = "The overwatch point doesn't have a condition " + foreign_index;
                        error_notifications.add(no_such_index); 
                    }
                    break; 
                default:
                    //Local condition check
                    //only need to check the local cache to see it appears before
                    //if not, enqueue error that call index before declare index

                    int local_id = watchpoint_get_if_local_call(testme, start); 
                    if(local_id == -1) {
                            String get_local_parse_failure = "Failed to parse local watchpoint index at " + (start + bofset);
                            error_notifications.add(get_local_parse_failure); 
                            break; 
                    }
                    boolean is_in_local = false; 
                    for(Integer t : set_sources) {
                        if(t == local_id) {
                            is_in_local = true;
                            break; 
                        }
                        else {
                            
                        }
                    }
                    if(is_in_local == true) {
                        
                    }
                    else {
                        String get_local_index_failure = "There is no condition " + local_id + " in this watchpoint";
                        error_notifications.add(get_local_index_failure); 
                    }
                    break; 
            }
            Token token = new Token(start, end); 
            local_tokens.add(token);  
            //System.out.println("IF-GET " + m.start() + " " + m.end());
        } 
        m.reset(); 
        m = token_watchregion_DO.matcher(testme); 
        while(m.find()) {
            int start = m.start(); 
            int end = m.end(); 
            String substr = testme.substring(start + 3, end - 1); 
            //System.out.println("-" + substr + "-");
            try {
                int getscript = Integer.parseInt(substr);
                if( (getscript < action_scripts.size()) && (getscript >= 0) ) {
                    Token token = new Token(start, end); 
                    local_tokens.add(token);
                }
                else {
                    error_notifications.add("DO statements did not include a valid script number. ");  
                }
            }
            catch(NumberFormatException e) {
                error_notifications.add("DO statements did not include a valid script number. "); 
            }
        }
        
        
        
        
        
        //AHA! Blank objects are intentionally empty of tokens. 
        if(!local_tokens.isEmpty() || sample.length() == 0) {
            Qsort dat = new Qsort(local_tokens); //qsort crashes on empty set. 
            local_tokens = dat.get_sorted(); 
            //System.out.println(dat.toString());
            int contig_ok = check_contig(local_tokens, testme); 
            //System.out.println(contig_ok);
            //verify arguments called exist at this point
            String errorno = ""; 
            if(error_notifications.isEmpty()) {
                //watchRegion(uniqueID) is dependent on deque
                for (Integer key : foreign_calls_watchpoint.keySet()) {
                    dependent_on_calls_watchpoint.add(key); 
                }
                if(!dependent_on_calls_watchpoint.isEmpty()) {
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("WP dependents : " + dependent_on_calls_watchpoint.size());
                    }
                    watchpoint_dependent.put("R" + Integer.toString(watchregion_id), dependent_on_calls_watchpoint);   
                }
                for (Integer key : foreign_calls_watchregion.keySet()) {
                    dependent_on_calls_watchregion.add(key); 
                }
                if(!dependent_on_calls_watchregion.isEmpty()) {
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("WR dependents : " + dependent_on_calls_watchregion.size());
                    } 
                    watchregion_dependent.put("R" + Integer.toString(watchregion_id), dependent_on_calls_watchregion);   
                }
                for (Integer key : foreign_calls_overwatch.keySet()) {
                    dependent_on_calls_overwatch.add(key); 
                }
                if(!dependent_on_calls_overwatch.isEmpty()) {
                    if(StencilUI.DEBUG_MODE == 1) {
                        System.out.println("O dependents : " + dependent_on_calls_overwatch.size());
                    }
                    overwatch_dependent.put("R" + Integer.toString(watchregion_id), dependent_on_calls_overwatch); //watchpoint depends on these items   
                }

                watchregion_hmap_sets.put(watchregion_id, set_sources); //add this point and its available sets
                if(StencilUI.DEBUG_MODE == 1) {
                    System.out.println("Watchregion " + watchregion_id + " verified");  
                }
                return errorno; 
            }
            else {
                for(String error: error_notifications) {
                    errorno += error + '\n'; 
                }
                error_notifications.clear();
                return errorno; 
            }  
        }
        else {
            return "No statements could be resolved. All candidates syntatically incorrect."; 
        }
    }
    
    
    
    
    
    
    
    
    
    /*
    Utility function to check that the string of tokens is contiguous.
    Uses the start and end locations extracted by regex to sort all the tokens, then parses them
    0-25,26-31,45-55 is missing a section (numbers not contiguous) 
    Return the location where the syntatic gap occured. 
    */
    private int check_contig(ArrayDeque<Token> tokenlist, String testing) {
        int leave_off = 0; //track where the last token to be checked ended
        for (Token t : tokenlist) {
            int last = t.get_start(); 
            //if the next token doesn't start where the last token left off, there is an unrecognized token
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

        //special case: the last token was not valid
        if(leave_off != testing.length()) {
            error_notifications.add("\nSyntax matching error between " + leave_off + " and end of statement"); 
            return 1; 
        }
        
        //the string is syntatically valid. 
        return 0; 
    }
    
    /*
    Verify that calls to GET an image are a image, like 3.1
    */
    private int verify_argument(final String substr, int pad, final ArrayList<ArrayList<String>> dsource) {
                final int padding = pad; 
                String groupstring = ""; 
                String itemstring = ""; 
                boolean decimal_loc = false; 
                for(int i = 0; i < substr.length(); ++i) {
                    if(substr.charAt(i) == '.') {
                        decimal_loc = true; 
                    } 
                    else {
                        if(decimal_loc != true) {
                            groupstring += substr.charAt(i); 
                        }
                        else {
                            itemstring += substr.charAt(i); 
                        }   
                    }
                } 
                try { 
                    final int d_size = dsource.size(); 
                    int gint = Integer.valueOf(groupstring); 
                            
                    if( (gint < 0)  ||  (gint > (dsource.size() - 1))) {
                        //System.out.println("Err");
                        return gint; 
                    }
                    int iint; 
                    if(itemstring.isEmpty()) {
                        
                    }
                    else {
                        iint = Integer.valueOf(itemstring); 
                        if( (iint < 0)  ||  (iint > (dsource.get(gint).size() - 1))) {
                           // System.out.println("Err");
                            return iint; 
                        }
                    }
                    //System.out.println("OK");
                    return -1; 
                }
                catch(NumberFormatException e) {
                    //System.out.println("Err");
                    return -2; 
                } 
}
    
    /*
    Verify a GET call is calling a valid object that already exists. 
    */
    private int watchpoint_get_if_foreign_call(String source, int start) {
            final int bofset = 18; //18th character is W O R in a forgein get. local get will default. 
            String substr = "";
            int foreign_id = -1;
            for(int i = start + bofset; i < source.length(); ++i) {
                if(source.charAt(i) == ',') {
                    break; 
                }
                else {
                    substr += source.charAt(i); 
                }
            }
            try {
                foreign_id = Integer.parseInt(substr); 
            }
            catch(NumberFormatException e) {
                return -1; 
            } 
        return foreign_id; 
    }
    
    /*
    Verify a call to an objects own condition is valid. 
    */
    private int watchpoint_get_if_local_call(String source, int start) {
            final int bofset = 17; //18th character is W O R in a forgein get. local get will default. 
            String substr = "";
            int foreign_id = -1;
            for(int i = start + bofset; i < source.length(); ++i) {
                if(source.charAt(i) == ',') {
                    break; 
                }
                else {
                    substr += source.charAt(i); 
                }
            }
            try {
                foreign_id = Integer.parseInt(substr); 
            }
            catch(NumberFormatException e) {
                return -1; 
            } 
        return foreign_id; 
    }
    
    /*
    Verify the GET of a foreign object index exists
    */
    private int watchpoint_get_if_foreign_index(String source, int start) {
            final int bofset = 18; //18th character is W O R in a forgein get. local get will default. 
            String substr = "";
            int foreign_id = -1;
            int minicounter = 0; 
            for(int i = start + bofset; i < source.length(); ++i) {
                if(source.charAt(i) == ',' ) {
                      ++minicounter;   
                }
                else {
                    if(minicounter == 1) {
                       substr += source.charAt(i);  
                    }
                    if(minicounter == 2) {
                        break; 
                    }
                }
            }
            try {
                foreign_id = Integer.parseInt(substr); 
            }
            catch(NumberFormatException e) {   
                return -1; 
            } 
        return foreign_id; 
    }
    
    
    /*
    Update the action script collection. utility function. 
    */
    public int set_action_update(ArrayList<String> action_array) {
        //called to update the available action scripts. 
        action_scripts = action_array; 
        return 0; 
    }
    
    
    /*
    Verifies an object has no dependents before allowing the object to be deleted. 
    If object A has a GET call to object B, then object B cannot be deleted because A depends on B data
    A however, can be deleted 
    B can only be deleted once all its dependents have been deleted as well. 
    */
    public String delete_check_dependent(int object_code, int object_identifier) {
        /*
        Delete watchpoint 3.
        Enqueue 3 in deque
        Check watchpoint and watchregion hashmap for watchpoint 3
        Iterare whole collection with the exception of 3 itself
        If 3 is listed, enqueue the ID
        ID's are guarenteed to be sequential as generated. If 4 depends on 3 to create 4, 4 CANNOT proceed 3. 
        If object depends on anything in queue, push ID onto deque as well. 
        If size of deque greater than 1 (3 itself), push error that delete cann't be performed. 
        
        final private int watchpoint_state = 1; 
        final private int watchregion_state = 2; 
        final private int overwatch_state = 3;
        */
        
                
        HashMap<String, Integer> dependent_map = new HashMap<>(); 
        ArrayDeque<Integer> dependent_stack = new ArrayDeque<>(); 
        ArrayDeque<String> error_delete = new ArrayDeque<>(); 
        
        //adding is done here in-iteration to avoid a concurrent error modification throw
        ArrayDeque<Integer> tmp_avoid_concurrent_throw = new ArrayDeque<>();
        ArrayDeque<String> tmp_str_avoid_concurrent_throw = new ArrayDeque<>();
        
        
        switch(object_code) {
            case watchpoint_state: //list all objects with this watchpoint as a dependency
                dependent_stack.add(object_identifier);
                //all key objects have an integer list. 
                for(String key: watchpoint_dependent.keySet()) {
                   // System.out.print("Key: " + key);
                    ArrayDeque<Integer> values = watchpoint_dependent.get(key); //value list of required watchpoint ID's
                    for(Integer value: values) { //looping here
                        //System.out.print(" " + value + " "); 
                            for(Integer depend: dependent_stack) {
                                if(Objects.equals(value, depend)) {
                                    //problem arises when the point has multiple dependencies that are all depdenent on the deleated
                                    //lead to multiple queueing of object every time a hit occurs. 
                                    if(!dependent_map.containsKey(key)) {
                                        dependent_map.put(key, 1); 
                                        
                                        String extract_id = key.substring(1, key.length()); 
                                        Integer additional_restrict = Integer.parseInt(extract_id); 
                                        if(StencilUI.DEBUG_MODE == 1) {
                                            System.out.println("Add " + additional_restrict);    
                                        }
                                        tmp_avoid_concurrent_throw.add(additional_restrict); 
                                        
                                        String prefix = ""; 
                                        if(key.charAt(0) == 'P') {
                                            prefix = "Watchpoint"; 
                                        }
                                        else if(key.charAt(0) == 'R') {
                                            prefix = "Watchregion"; 
                                        }
                                        String depends = "Cannot delete Watchpoint " + object_identifier + ". " + prefix + " " + extract_id + " is a dependency. Delete " + prefix + " " + extract_id + " first.";
                                        error_delete.add(depends); 
                                        
                                    }
                                }
                        }
                    }
                    dependent_stack.addAll(tmp_avoid_concurrent_throw); 
                    tmp_avoid_concurrent_throw.clear();
                }
                break; 
            case watchregion_state:
                dependent_stack.add(object_identifier); //0
                //a list of all W or R objects that have a W dependency
                for(String key: watchregion_dependent.keySet()) {
                    //System.out.print(key);
                    ArrayDeque<Integer> values = watchregion_dependent.get(key); //value list of required watchpoint ID's
                    for(Integer value: values) {
                        //System.out.print(" " + value + " "); 
                            for(Integer depend: dependent_stack) {
                                if(Objects.equals(value, depend)) {
                                    //problem arises when the point has multiple dependencies that are all depdenent on the deleated
                                    //lead to multiple queueing of object every time a hit occurs. 
                                    if(!dependent_map.containsKey(key)) {
                                        dependent_map.put(key, 1); 
                                        
                                        String extract_id = key.substring(1, key.length()); 
                                        Integer additional_restrict = Integer.parseInt(extract_id); 
                                        if(StencilUI.DEBUG_MODE == 1) {
                                            System.out.println("Add " + additional_restrict);   
                                        }
                                        tmp_avoid_concurrent_throw.add(additional_restrict); 
                                        
                                        String prefix = ""; 
                                        if(key.charAt(0) == 'P') {
                                            prefix = "Watchpoint"; 
                                        }
                                        else if(key.charAt(0) == 'R') {
                                            prefix = "Watchregion"; 
                                        }
                                        String depends = "Cannot delete Watchregion " + object_identifier + ". " + prefix + " " + extract_id + " is a dependency. Delete " + prefix + " " + extract_id + " first.";
                                        error_delete.add(depends); 
                                        
                                    }
                                }
                        }
                    }
                    dependent_stack.addAll(tmp_avoid_concurrent_throw); 
                    tmp_avoid_concurrent_throw.clear();
                }
                break; 
            case overwatch_state:
                for (String key : overwatch_dependent.keySet()) {
                        String prefix = ""; 
                        if(key.charAt(0) == 'P') {
                            prefix = "Watchpoint"; 
                        }
                        else if(key.charAt(0) == 'R') {
                            prefix = "Watchregion"; 
                        }
                        String overwatch_del_failure = "Cannot delete overwatch. " + prefix + " " + key.substring(1, key.length()) + " is a dependency. Delete " + key.substring(1, key.length()) + " first.";
                        error_delete.add(overwatch_del_failure); 
                }
                break; 
            default:
                break; 
        }
                

        String errors = ""; 
        if(error_delete.isEmpty()) {
            //perform delet operations from global sources.
            switch(object_code) {
                case watchpoint_state:
                    String clearcode = 'P' + Integer.toString(object_identifier); 
                    watchpoint_hmap_sets.remove(object_identifier); 
                    watchpoint_dependent.remove(clearcode); 
                    if(overwatch_dependent.containsKey(clearcode)) {
                        overwatch_dependent.remove(clearcode); 
                    }
                    
                    /*
                    While we COULD re-iterate over all maps and clear the ID, since the hash maps are not used 
                    for final construction and object_identifiers are not re-used, it seems un-necessary
                    */

                    break; 
                case watchregion_state:
                    String rclearcode = 'R' + Integer.toString(object_identifier); 
                    watchregion_hmap_sets.remove(object_identifier); 
                    watchregion_dependent.remove(rclearcode); 
                    if(overwatch_dependent.containsKey(rclearcode)) {
                        overwatch_dependent.remove(rclearcode); 
                    }
                    break; 
                case overwatch_state:
                    overwatch_sets.clear();
                    overwatch_dependent.clear();
                    break; 
            }
            
        }
        else {
            for(String error: error_delete) {
                errors += error + '\n'; 
            }
        }
        return errors; 

    }
    
    
    
    
    /*
    Assembles the objhects into the execution order string;
    Objects need to be executed in an order that satisfies their dependencies, so
    if A depends on B and B depends on C, and A depends on D, the final order of exeuction will be:
    C-D-B-A
    */
    public ArrayDeque<String> string_final_pass_check_order() {
        if(StencilUI.DEBUG_MODE == 1) {
            System.out.println("SFO cal");  
        }
        HashMap<String, Integer> initialized = new HashMap<>(); 
        ArrayDeque<String> initialize_order = new ArrayDeque<>(); 
        //composed of a map of all points. 
        //any point that doesn't appear in a watchpoint or watchregion map dependency is standalone, add that.
        //then add Overwatch
        //then resolve the remaining. 
        //add to queue 
        
        //set up map
        for(Integer in: watchpoint_hmap_sets.keySet()) {
            initialized.put("P" + Integer.toString(in), 0); 
        }
        for(Integer in: watchregion_hmap_sets.keySet()) {
            initialized.put("R" + Integer.toString(in), 0);
        }
        if(!overwatch_sets.isEmpty()) {
            //there may be no overwatch set...
            initialized.put("O", 0);
        } 
        

        
        //first, every standalone point that doesn't appear in the dependent maps.
        for(String key: initialized.keySet()) {
            if(!watchregion_dependent.containsKey(key) &&  !watchpoint_dependent.containsKey(key) && !overwatch_dependent.containsKey(key) ){
                //key is standalone. 
                //includes standalones and overwatch itself
                initialize_order.add(key); 
                //make key identifier available as initialized in hmap. 
                initialized.replace(key, 1); //key set to initialized. 
            }
        } 
        
        //now objects that EXCLUSIVLY appear in overwatch dependent. 
        for(String key: initialized.keySet()) {
            if((initialized.get(key) == 0)) {
                //if it doesn't equal 1
                if(!(watchregion_dependent.containsKey(key)) &&  !(watchpoint_dependent.containsKey(key)) && overwatch_dependent.containsKey(key) ){
                    initialize_order.add(key); 
                    //make key identifier available as initialized in hmap. 
                    initialized.put(key, 1); //key set to initialized. 
                }
            }
            else {
                //do nothing. 
            }
        }

        
        while(initialized.containsValue(0)){
            for(String key: initialized.keySet()) {
                if(initialized.get(key) == 1) {
                    //do nothing. already initialized.
                }
                else {
                    //dive into dependency file 1-2
                    boolean satisfy_wp = true; 
                    boolean satisfy_wr = true; 
                    if((watchpoint_dependent.get(key)) != null) {
                        for(Integer wp: watchpoint_dependent.get(key)) {
                            if(initialized.get("P" + Integer.toString(wp)) == 0) {
                                satisfy_wp = false; 
                                break; 
                            }
                        }  
                    }
                    if((watchregion_dependent.get(key)) != null) {
                        for(Integer wr: watchregion_dependent.get(key)) {
                            if(initialized.get("R" + Integer.toString(wr)) == 0) {
                                satisfy_wr = false; 
                                break; 
                            }
                        }  
                    }
                    
                    if(satisfy_wp == true  &&  satisfy_wr == true) {
                        initialize_order.add(key); 
                        initialized.put(key, 1); 
                    }
                }
            }
        }
        
        
        return initialize_order; 
    }
    
    
    /*
    The token utility class. Each regex match generates a TOKEN that occupies space START to END in the string. 
    Used to verify that the entire string could be tokenized. Gaps result in a compile error. 
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
    The regex tokens, marked with where the START and END in each candidate string, are sorted by their start
    into a contiguous candidate array of tokens. This array will be parsed looking for sequential numbering of tokens.
    A gap in the token array where start next != last end indicates a syntatic failure to match a token
    
    This is more or less a standard quicksort implementation. 
    */
    private class Qsort {
    private ArrayList<Token> tdata; 
    
    public Qsort(ArrayDeque<Token> keydata) {
        tdata = new ArrayList<>(); 
        for (Token t : keydata) {
            tdata.add(t); 
        }
        if(keydata.size() > 1) {
            quicksort(0, tdata.size() - 1);    
        }
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