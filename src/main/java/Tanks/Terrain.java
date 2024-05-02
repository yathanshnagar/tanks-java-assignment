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
        // smoothTerrain();
    }

    private void loadLevelLayout(String levelFilePath) {
        levelLayout = new char[BOARD_HEIGHT][BOARD_WIDTH];
        terrainHeights = new int[BOARD_WIDTH];
    
        try (BufferedReader reader = new BufferedReader(new FileReader(levelFilePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < BOARD_HEIGHT) {
                for (int col = 0; col < line.length() && col < BOARD_WIDTH; col++) {
                    levelLayout[row][col] = line.charAt(col);
                    if (line.charAt(col) == 'X') {
                        terrainHeights[col] = row;
                    }
                }
                // Fill the remaining columns with spaces if the line is shorter than BOARD_WIDTH
                for (int col = line.length(); col < BOARD_WIDTH; col++) {
                    levelLayout[row][col] = ' ';
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private void smoothTerrain() {
    //     // Step 1: Compute the moving average once
    //     int[] smoothedTerrainHeights = new int[BOARD_WIDTH];
    //     for (int i = 0; i < BOARD_WIDTH; i++) {
    //         int sum = 0;
    //         for (int j = Math.max(0, i - App.CELLAVG / 2); j < Math.min(BOARD_WIDTH, i + App.CELLAVG / 2); j++) {
    //             sum += terrainHeights[j];
    //         }
    //         smoothedTerrainHeights[i] = sum / App.CELLAVG;
    //     }

    //     // Step 2: Compute the moving average again
    //     for (int i = 0; i < BOARD_WIDTH; i++) {
    //         int sum = 0;
    //         for (int j = Math.max(0, i - App.CELLAVG / 2); j < Math.min(BOARD_WIDTH, i + App.CELLAVG / 2); j++) {
    //             sum += smoothedTerrainHeights[j];
    //         }
    //         terrainHeights[i] = sum / App.CELLAVG;
    //     }
    // }

    public void render(PApplet applet) {
        applet.stroke(255); // Set the stroke color to white
        applet.strokeWeight(2); // Set the stroke weight to 2 pixels
        applet.fill(255); // Set the fill color to white (or any other color you prefer)
    
         // Draw the terrain as lines
        for (int x = 0; x < BOARD_WIDTH - 1; x++) {
            int terrainHeight1 = terrainHeights[x];
            int terrainHeight2 = terrainHeights[x + 1];

            // Calculate coordinates
            float x1 = x * App.CELLSIZE;
            float x2 = (x + 1) * App.CELLSIZE;
            float y1 = (BOARD_HEIGHT - terrainHeight1) * App.CELLSIZE;
            float y2 = (BOARD_HEIGHT - terrainHeight2) * App.CELLSIZE;

            // Draw line segment
            applet.line(x1, y1, x2, y2);
        }
    }
}