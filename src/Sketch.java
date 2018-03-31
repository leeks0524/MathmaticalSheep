
import processing.core.PApplet;

public class Sketch extends PApplet{
    public void settings() {  size(640, 360); }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Sketch" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    NumberMon [] visibleMons;


    class NumberMon {
        float x, y; //x and y coordinates
        float speed;
        float radius = 20;

        NumberMon (float xpos, float ypos, float initSpeed) {
            x = xpos;
            y = ypos;
            speed = initSpeed;
        }
    }


    String message = "tickle";
    float x, y; // X and Y coordinates of text
    float hr, vr;  // horizontal and vertical radius of the text

    public void setup() {
        size(640, 360);

        // Create the font
        //textFont(createFont("Consolas.ttf", 36));
        textAlign(CENTER, CENTER);

        hr = textWidth(message) / 2;
        vr = (textAscent() + textDescent()) / 2;
        noStroke();
        x = width / 2;
        y = height / 2;
    }


    public void draw() {
        // Instead of clearing the background, fade it by drawing
        // a semi-transparent rectangle on top
        fill(204, 200);
        rect(0, 0, width, height);

        // If the cursor is over the text, change the position
        if (abs(mouseX - x) < hr &&
                abs(mouseY - y) < vr) {
            x += random(-5, 5);
            y += random(-5, 5);
        }
        fill(0);
        text("tickle", x, y);
    }





}