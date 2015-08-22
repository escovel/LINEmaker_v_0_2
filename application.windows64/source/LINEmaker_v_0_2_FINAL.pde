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
import controlP5.*; // The controlP5 libary is used for GUI elements (button and textfield)
import processing.pdf.*; // For PDF export feature
import java.util.Calendar; // For timestamp

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

void setup() {
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

void draw() {
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
void keyReleased() {
  // export pdf and png
  if (keyCode == CONTROL) doSave = true;
}

// Timestamp
String timestamp() {
  Calendar now = Calendar.getInstance();
  return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now);
}
