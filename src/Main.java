import processing.core.PApplet;
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;


public class Main extends PApplet {

//complete tutorial given
//create a simple project that leverages graphics or interactivity feature

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

    PImage[] images = new PImage[8];

    public void setup()
    {
        for (int i = 0; i < images.length; i++)
        {
            images[i] = loadImage("explosion_" + nf(i+1, 1) + ".png"); // nf() allows to generate 01, 02, etc.
        }
    }

    public void settings() {
        size(100, 100);
    }

    int counter; // Automatically initialized at 0

    public void draw()
    {
        delay(100);
        //background(200);
        image(images[counter], 0, 0);
        // Increment counter, then compute its modulo, ie. reset it at zero when reaching images.length
        counter = ++counter % images.length;
    }

}