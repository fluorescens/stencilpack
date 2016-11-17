# stencilpack
A Java-Swing GUI powered template builder and internal crunchylang interpreter for building functional stencilpacks. 

#What this Project does:
This program builds a complete stencilpack by taking in a crunchypack builder file that has been processed through ImgReduce. It uses this data, including image names and collections, to allow a user to lay out "Watchobjects" over a screenshot of the program the template will be running against. In these areas the main executable will perform search operations for objects (Currently supports watchpoint(single x,y), watchregion (rectangular region and all encompassing points) and overwatch (a special linked singleton watchregion with scatter pattern dispersal of watchpoints within the bounds)). "Trigger scripts" can be attatched to watchobjects which offer the ability to add complex logic to a watchobjects evaluation if it is in fact true. Supported operations include booleans like NOT and OR as well as operations to test if another watchobject has evaluated to true. The full documentation of "crunchylang" is included in the project: review it before writing. If a watchobjects trigger evaluates to true it executes an action script, which can be assigned priorities and output string arguments in the order their priority dictates. These strings can be pipelined into a program which translates them into keystroke injections, keeping the program modular by allowing users to drop in other programs at the end of the pipeline. 

# How to Install this Program:
This program can be run from any system with Java 8 JRE installed. To install Java 8, follow the instructions from Oracle (http://www.oracle.com/) for setting up the java runtime enviornment on your machine and make sure to add java to the system path variables. Then just double click the .jar file and it will run like a normal executable. 

# Example Usage:
I want to a count of how many times a twitch user recieves a donation during their stream. The Twitch API provides no access to this information, so it must be extracted some other way. Using a crunchypack-builder with an image composed of the unique colored font or unique color of an icon that splashes every time this event occurs, a stencilpack can be built with a watchpoint at the area within the stream window on the desktop where the donor icon or text is expected to pop up. Using the crunchylang expression IF[1.0] DO[0] attached to a watchpoint at the area where the "donor" popup would appear and making an action script 0 [0,E] will cause that every time the image at watchpoint 0 evaluates to TRUE (assuming image 1.0 was the name of your icon image) that region of the screen contained pixels that uniquely belonged to image 1 and then "E" will be output to the terminal output of the program. This "E" can then be pipelined into another program where it can take more user-defined action like just incrimenting a counter or posting an automated response into the chat program. This program is capable of managing up to 999 different watchobjects each with unique triggers and action scripts that can even check each other to see if a certain condition about another object evaluated to true and consider that in their own evaluation. Read the crunchylang docs for complete documentation. 

# Set up the Development Enviornment:
This program is offered as source code and can be built from any java 8 compiler. Latest java compilers can be downloaded from Oracle: follow their instructions for installation. 

# Submit Changes:
Submit proposed changes in plaintext with file name and line numbers to project manager: jamesl@leppek.us
