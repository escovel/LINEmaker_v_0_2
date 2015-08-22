import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import processing.pdf.*; 
import java.util.Calendar; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class LINEmaker_v_0_2_FINAL extends PApplet {

/**
 * LINEmaker  ||  v. 0.2 
 *
 * A program for interactive composition of visual poems composed of single
 * lines of characters condensed and given transparency for visual effect |
 *
 * Add text to the text field at the bottom of the program and compress/expand
 * the characters using the mouse. You may edit the text in the text field as
 * you go |
 *
 * [MOUSE INTERACTION] 
 * Vertical (Y)           : adjust character spacing | 
 * Horizontal (X)         : adjust character alpha value (transparency) | 
 * 
 * [INPUT TEXT FIELD] 
 * A-Z, 1-0, Punc.        : text input (keyboard) | 
 * RIGHT AND LEFT ARROWS  : move forwards and backwards in your text to edit |
 * BACKSPACE              : deletes the character preceeding the text cursor |
 * ENTER                  : clears all text from screen and textfield |
 *
 * [SAVE POEM]
 * CTRL                   : saves current display as both PDF and PNG to the 
 *                          program's main folder |
 */

// Import required libraries
 // The controlP5 libary is used for GUI elements (button and textfield)
 // For PDF export feature
 // For timestamp

// Declare ControlP5 object names
ControlP5 cp5;
Textfield inputTextfield;

// Initialize default save and inputText states
boolean doSave = false;
String inputText = "";

// Declare fonts
PFont poemFont;
PFont inputFont;
PFont sectionTitleFont;

// Declare both program states for title screen and main app screen (see draw())
final int titleScreenState = 0;
final int mainAppState = 1;

//Begin with the title screen
int programState = 0;

// Declare PImage variables
PImage startButtonImage;
PImage startButtonImageOver;
PImage titleImage;

public void setup() {
  size(1320, 300);
  background(255); // Draw background for font and graphics sharpness

  // Load fonts for the title screen, main app and textfield areas
  poemFont = createFont("Times New Roman",75,true); // Times New Roman, 75 Point , anti-alias true, for visual poem text  
  inputFont = createFont("SansSerif.plain",16); // Sans-serif, 16 Point, for input text field
  sectionTitleFont = createFont("SansSerif.bold",16); // Sans-serif, 16 Point, for input text field
  
  // Create and assign properties to the textfield for text input  
  cp5 = new ControlP5(this);
  inputTextfield = cp5.addTextfield("inputTextfield")
     .setPosition(20,260)
     .setSize(1280,20)
     .setFont(inputFont)
     .setFocus(true)
     .setColorBackground(color(75))
     .setColor(color(175,175,175))
     ;
     
  // Create and assign properties to the start button  
  startButtonImage = loadImage("start-button.png");
  startButtonImageOver = loadImage("start-button-over.png");
  cp5.addButton("startButton")
     .setValue(programState)
     .setImages(startButtonImage, startButtonImage, startButtonImageOver)
     .setPosition(620,240)
     .updateSize()
     .addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent event) {
        if (event.getAction() == ControlP5.ACTION_RELEASED) {
          println("button released.");
          programState = mainAppState;
          cp5.remove(event.getController().getName());
        }
      }
    }
  );
}

public void draw() {
  // Because the program has two main states, a switch (activated by a button) is used to move from one to the other
  switch(programState){
    case titleScreenState: 
      inputTextfield.hide(); // Hides the textfield from the title screen
      doTitleScreen(); // Run commands for the title and instructions screen
    break;  
    case mainAppState: 
      inputTextfield.show(); // Reveals the textfield for use in the main application
      doMainApp(); // Run commands for the main program screen
    break;
  }
}

// Set keys for saving poem
public void keyReleased() {
  // export pdf and png
  if (keyCode == CONTROL) doSave = true;
}

// Timestamp
public String timestamp() {
  Calendar now = Calendar.getInstance();
  return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now);
}
// State 0 for the title screen
public void doTitleScreen(){
  background(255); // Draw background again for smoothing of fonts and lines
  //Draw title image to screen
  titleImage = loadImage("title-image.png");
  imageMode(CENTER);
  image(titleImage, 660, 60);
  //Draw description of program and instructions. First a rectangular text box is drawn, then the text
  fill(220);
  stroke(230);
  strokeWeight(4);
  rect(160, 115, 1000, 96, 12);
  noStroke();
  textAlign(LEFT);
  textFont(sectionTitleFont, 12);
  fill(0);
  text("DIRECTIONS", 185, 140);
  fill(20);
  textFont(inputFont, 11);
  text("Click on the text field to add characters to the poem. Move the mouse from left to right to expand/contract the text. Move the mouse up and down to adjust transparency.", 185, 160);
  text("You may click in the text field at any time to edit as you go.", 185, 172);
  text("Press CTRL at any time to save your poem as PDF and PNG files. They will be saved to the main program directory.", 185, 192); 
}

// State 1 for the main application
public void doMainApp(){
  if (doSave) beginRecord(PDF, timestamp()+".pdf"); // For save to PDF
  background(255); // Draw background again for smoothing of fonts and lines
  textFont(poemFont,75); // Declare the text font for drawing
  String inputText = inputTextfield.getText(); // Get text input from the textfield to draw the visual poem
  // Main drawing loop. Redraws characters one at a time each time a character is added or removed from the textfield
  for (int i = 0; i < inputText.length(); i++) {
    char letter = inputText.charAt(i); // Takes each character one at a time from the inputText (textfield)
    fill(0, 25+(mouseY)); // Transparency is set by setting the fill's alpha to vary with the mouse's vertical position
    text(letter, 20+(i*mouseX/60), 170); // Space between characters is determined by the mouse's horizontal position

    // A white rectangle is drawn as a border of 20 pixels around the full window
    fill(255);
    noStroke();
    rect(1380, 0, 20, 240);
  }
  
//  Works with keyReleased() to save the poem when CTRL is pressed (changes doSave state)
  if (doSave) {
    doSave = false;
    endRecord();
    saveFrame(timestamp()+"_##.png");
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "LINEmaker_v_0_2_FINAL" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
