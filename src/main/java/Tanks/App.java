package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    PImage backgroundImage;
    JSONObject config;

    private int[] terrain;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        config = loadJSONObject(configPath);
        JSONArray levels = config.getJSONArray("levels");
        JSONObject level1 = levels.getJSONObject(0);
        String backgroundImagePath = "src/main/resources/Tanks/" + level1.getString("background");
        backgroundImage = loadImage(backgroundImagePath);
		//See PApplet javadoc:
		//loadJSONObject(configPath)
		//loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));

         // Load the level layout from the file
        String layoutFilePath = sketchPath("level1.txt");
        String[] lines = loadStrings(layoutFilePath);

        // Convert the layout to height values
        terrain = new int[BOARD_WIDTH * BOARD_HEIGHT];
        int index = 0;
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            if (y >= lines.length) break; // Stop if we run out of lines
            String line = lines[y];
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (x >= line.length()) break; // Stop if we run out of characters in the line
                char c = line.charAt(x);
                terrain[index++] = getHeightFromChar(c);
        }
    }

        // Apply moving average filter twice to smooth the terrain
        terrain = applyMovingAverage(terrain, CELLAVG);
        terrain = applyMovingAverage(terrain, CELLAVG);

        System.out.println("Smoothed Terrain Values:");
        for (int i = 0; i < terrain.length; i++) {
            System.out.print(terrain[i] + " ");
            if ((i + 1) % BOARD_WIDTH == 0) {
                System.out.println();
            }
        }

    }

    private int getHeightFromChar(char c) {
        // Map characters to height values
        switch (c) {
            case 'X': return CELLHEIGHT;
            case 'T': return CELLHEIGHT / 2;
            case 'B': return CELLHEIGHT;
            case 'A': return CELLHEIGHT;
            case 'C': return CELLHEIGHT;
            case 'D': return CELLHEIGHT;
            case ' ': return 0;
            // Add more cases for other characters if needed
            default: return 0;
        }
    }

    private int[] applyMovingAverage(int[] values, int windowSize) {
        int[] smoothedValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            int start = Math.max(0, i - windowSize / 2);
            int end = Math.min(values.length, i + windowSize / 2 + 1);
            int sum = 0;
            int count = 0;
            for (int j = start; j < end; j++) {
                sum += values[j];
                count++;
            }
            smoothedValues[i] = sum / count;
        }
        return smoothedValues;
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        image(backgroundImage, 0, 0); // Draw background image at (0, 0) coordinates

        // Draw the terrain
        stroke(255); // Set the stroke color to white
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
                int index = y * BOARD_WIDTH + x;
                int terrainHeight = terrain[index];
                int posX = x * CELLSIZE;
                int posY = HEIGHT - CELLSIZE - terrainHeight;
                rect(posX, posY, CELLSIZE, terrainHeight);
            }
        }
        

        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
    }


    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}
