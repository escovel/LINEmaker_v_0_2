// State 0 for the title screen
void doTitleScreen(){
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
void doMainApp(){
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
