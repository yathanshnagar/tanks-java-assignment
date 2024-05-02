package Tanks;

import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private static final int BOARD_WIDTH = App.WIDTH / App.CELLSIZE;
    private static final int BOARD_HEIGHT = App.BOARD_HEIGHT;

    private char[][] levelLayout;
    private int[] terrainHeights;

    public Terrain(String levelFilePath) {
        loadLevelLayout(levelFilePath);
    }

    private void loadLevelLayout(String levelFilePath) {
        levelLayout = new char[BOARD_HEIGHT][BOARD_WIDTH];
        terrainHeights = new int[BOARD_WIDTH];
    
        try (BufferedReader reader = new BufferedReader(new FileReader(levelFilePath))) {
            String line;
            int row = BOARD_HEIGHT - 1; // Start from the bottom row
            while ((line = reader.readLine()) != null && row >= 0) {
                for (int col = 0; col < line.length() && col < BOARD_WIDTH; col++) {
                    levelLayout[row][col] = line.charAt(col);
                    if (line.charAt(col) == 'X' && terrainHeights[col] == 0) {
                        terrainHeights[col] = BOARD_HEIGHT - row; // Assign terrain height relative to the bottom
                    }
                }
                // Fill the remaining columns with spaces if the line is shorter than BOARD_WIDTH
                for (int col = line.length(); col < BOARD_WIDTH; col++) {
                    levelLayout[row][col] = ' ';
                }
                row--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Print the terrain heights
        for (int i = 0; i < BOARD_WIDTH; i++) {
            System.out.println("Terrain height for column " + i + ": " + terrainHeights[i]);
        }
    }

    public void render(PApplet applet) {
        applet.stroke(255); // Set the stroke color to white
        applet.strokeWeight(2); // Set the stroke weight to 2 pixels
        applet.fill(255); // Set the fill color to white (or any other color you prefer)
    
        // Begin drawing the shape for the filled terrain
        applet.beginShape();
    
        // Add vertices for the top of the terrain
        for (int x = 0; x < BOARD_WIDTH; x++) {
            int terrainHeight = terrainHeights[x];
            float xCoordinate = x * App.CELLSIZE;
            float yCoordinate = applet.height - (BOARD_HEIGHT - terrainHeight) * App.CELLSIZE; // Adjust y-coordinate
            applet.vertex(xCoordinate, yCoordinate);
        }

        // Add a vertex to connect the left end to the right end
        applet.vertex(BOARD_WIDTH * App.CELLSIZE+100, applet.height); 
    
        // Add vertices for the bottom of the terrain (along the ground)
        for (int x = BOARD_WIDTH - 1; x >= 0; x--) {
            float xCoordinate = x * App.CELLSIZE;
            float yCoordinate = applet.height; // Ground level
            applet.vertex(xCoordinate, yCoordinate);
        }
    
        // End the shape and fill it
        applet.endShape(PApplet.CLOSE);
    }

    public void printTerrainLayout() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                System.out.print(levelLayout[row][col]);
            }
            
            System.out.println();
        }
    }
}