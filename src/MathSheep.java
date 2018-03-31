import processing.core.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.String;

public class MathSheep extends PApplet {
    public void settings() {
        size(640, 360);
    } //size of game screen

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"MathSheep"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
    //SETTING global variables
    NumMon[] mons; //floating number monsters on the screen 
    float diameter = 40; //diameter of number monster
    float playerDiameter = 50; //detecting size of llama to later calculate when lama reach number monster
    Player player;
    ArrayList<Integer> primeNumList = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199));
    boolean playerAlive; //llama or explosion image ~ numbers moving or numbers stop moving
    boolean historyModeOn;
    int score;
    ArrayList <Integer> scoreHistory;
    int MAX_SCORE_HISTORY = 5; //top five scores are shown in history 
    int counter; // switching llama image to make it look walking
    PImage[] llama;
    PImage[] explosion;

    public void setup() {
        mons = new NumMon[4]; //only four number monsters are shown in screen - array of four number monsters
        for (int i = 0; i < mons.length; i++) {
            mons[i] = new NumMon(random((height / 4 * i + diameter / 2), (height / 4 * (i + 1) - diameter / 2)), (int) random(200));
        } //locating number monster - random (random number is selected between 0-200), (int): casting - from float to int,
        player = new Player();
        playerAlive = true;
        historyModeOn = false;
        score = 0;
        scoreHistory = new ArrayList<>();
        for (int i = 0; i < MAX_SCORE_HISTORY; i++) {
            scoreHistory.add(0);
        }
        textAlign(CENTER);
        counter = 0;
        llama = new PImage[4];
        explosion = new PImage[4];
        for (int i = 0; i < llama.length; i++) {
            llama[i] = loadImage("llama_" + i + ".png");
            explosion[i] = loadImage("explosion_" + i + ".png");
        }
    }

    public void reset() {
        playerAlive = true;
        historyModeOn = false;
        score = 0;
        mons = new NumMon[4];
        for (int i = 0; i < mons.length; i++) {
            mons[i] = new NumMon(random((height / 4 * i + diameter / 2), (height / 4 * (i + 1) - diameter / 2)), (int) random(200));
        }
        player = new Player();
        counter = 0;
    }

    public void draw() {
        if (historyModeOn) {
            background(132);
            textSize(40);
            fill(23, 140, 210);
            text("Score Board", width / 2, 50);
            for (int i = 0; i < scoreHistory.size(); i++) {
                text((i + 1) + ". " + scoreHistory.get(i), width / 2, 50 + 40 * (i + 1));
            }
        } else {
            background(0);
            for (int i = 0; i < mons.length; i++) {
                if (mons[i].getX() < diameter * -1) {
                    mons[i] = new NumMon(random((height / 4 * i + diameter / 2), ((height / 4 * (i + 1)) - diameter / 2)), (int) random(200));
                } else {
                    if (playerAlive) {
                        mons[i].move();
                    }
                }
                mons[i].show();
                mons[i].collisionDetection(player);
            }
            player.show(counter);

            textSize(30);
            fill(0, 102, 153);
            text("Score: " + score, width / 2, 40);
            textSize(14);
            text("Press 'r' to restart the game, and 's' to see the scoreboard.", width / 2, height - 20);

        }
        if (counter < 79) {
            counter = counter + 1;
        } else {
            counter = 0;
        }
    }

    public void keyPressed() {
        if (key == 'r') {
            historyModeOn = false;
            reset();
        } else if (key == 's') {
            historyModeOn = true;
        }

        if (playerAlive) {
            if (keyCode == UP) {
                player.move(0, -5);
            } else if(keyCode == DOWN) {
                player.move(0, 5);
            }
            player.show(counter);
        }
    }

    class Player {
        float x, y;

        Player () {
            x = playerDiameter / 2;
            y = height / 2;
        }

        void move(float moveX, float moveY) {
            x = x + moveX;
            y = y + moveY;
        }

        void show(int counter) {
            //ellipse(x, y, playerDiameter,playerDiameter);
            if (playerAlive) {
                image(llama[counter / 20], x - playerDiameter / 2, y - playerDiameter / 2, playerDiameter, playerDiameter);
            } else {
                image(explosion[counter / 20], x - playerDiameter / 2, y - playerDiameter / 2, playerDiameter, playerDiameter);
            }
        }

        PVector getPosition() {
            return new PVector(x, y);
        }
    }

    class NumMon {
        float x, y;
        float speed;
        int sign;
        boolean visible;
        boolean collected;

        NumMon (float initY, int sign) { //initY : y coordinate of number monster, sign : number 
            x = width + diameter; // number monster's starting point is right side of screen
            y = initY;
            speed = 3;
            this.sign = sign;
            visible = true; //not eaten by llama
            collected = false; //eaten by llama
        }

        void move() {
            x = x - speed;
        }

        void show() {
            if (visible) {
                fill(255);
                stroke(138, 43);
                ellipse(x, y, diameter, diameter);
                textSize(20);
                fill(0, 102, 153);
                text(sign, x,  y+ 8);
            }

        }

        float getX() {
            return x;
        }

        void collisionDetection(Player p) {
            float distance = p.getPosition().dist(new PVector(x, y));
            if (distance < (diameter + playerDiameter) / 2) {
                visible = false;
                if (!collected) {
                    if (primeNumList.contains(sign)) {
                        score = score + 1;
                    } else {
                        playerAlive = false;
                        int tempScore;
                        int scoreCopy = score;
                        for (int i = 0; i < scoreHistory.size(); i++) {
                            if (scoreHistory.get(i) == 0) {
                                scoreHistory.set(i, scoreCopy);
                                break;
                            } else if (scoreCopy >= scoreHistory.get(i)) {
                                tempScore = scoreHistory.get(i);
                                scoreHistory.set(i, scoreCopy);
                                scoreCopy = tempScore;
                            }
                        }
                    }
                    collected = true;
                }
            }
        }
    }
}
