package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScheduleVisualizer extends Application {

    // Customizable variables
    private int canvasWidth = 1800;
    private int canvasHeight = 850;
    private int graphStartX = 100;
    private int graphStartY = 50;
    private int graphHeight = 400;
    private int graphWidth = 1200;
    private int legendOffset = 50;
    private int xAxisLabelOffset = 20;
    private int processLabelOffsetX = 10;
    private int statisticsYStart = 850;
    private int statisticsLineSpacing = 20;
    private String[] processLabels = {"Process1", "Process2", "Process3", "Process4", "Process5"};
    private Color[] processColors = {Color.YELLOW, Color.PINK, Color.CYAN, Color.ORANGE, Color.PURPLE};
    int maximumXLabels = 20;

    int[] processTimes = {0, 0, 1, 1, 2, 1, 1, 3, 3, 3, 3, 1, 1, 1, 1, 1};



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CPU Scheduling Graph");

        // Set up the layout
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // Draw the CPU Scheduling graph
        drawGraph(gc);

        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawGraph(GraphicsContext gc) {
        // Background
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        int gridCellWidth = graphWidth / (processTimes.length+1);
        int gridCellHeight = graphHeight / processLabels.length;

        // Grid lines
        gc.setStroke(Color.LIGHTGRAY);
        int lastx = graphStartX;
        for (int x = graphStartX; x <= graphStartX+graphWidth; x += gridCellWidth) {
            gc.strokeLine(x, graphStartY, x, graphStartY + graphHeight); // Vertical lines
            lastx = x;
        }
        for (int y = graphStartY; y <= graphStartY + graphHeight; y += gridCellHeight) {
            gc.strokeLine(graphStartX, y, lastx, y); // Horizontal lines
        }

        // Labels for processes
        gc.setFont(new Font("Arial", 16));
        gc.setFill(Color.BLACK);
        for (int i = 0; i < processLabels.length; i++) {
            gc.fillText(processLabels[i], processLabelOffsetX, graphStartY + i*gridCellHeight + gridCellHeight/2);
        }

        // X-axis numbers
        gc.setFill(Color.WHITE);
        int mod = (processTimes.length+maximumXLabels+1)/maximumXLabels;
        for (int i = 0, x = graphStartX; x <= graphStartX + graphWidth; i++, x += gridCellWidth) {
            if (i%mod != 0) continue;
            String text = Integer.toString(i);
            gc.fillText(text, x - 5*text.length(), graphStartY + graphHeight + xAxisLabelOffset);
        }

        // Draw blocks for processes
        for (int i = 0; i < processTimes.length; i++) {
            int x = processTimes[i];
            gc.setFill(processColors[x]);
            gc.fillRect(graphStartX + i*gridCellWidth, graphStartY + x*gridCellHeight, gridCellWidth, gridCellHeight);
        }

        // Legend
        drawLegend(gc);

        // Statistics Section
        drawStatistics(gc);
    }


    private void drawLegend(GraphicsContext gc) {
        double legendX = graphStartX + graphWidth + legendOffset;
        double legendSpacing = 20;

        gc.setFill(Color.RED);
        gc.fillText("Processes Information", legendX, graphStartY);

        gc.setFill(Color.WHITE);
        for (int i = 0; i < processColors.length; i++) {
            gc.fillText(processLabels[i], legendX, graphStartY + (i + 1) * legendSpacing);
        }
    }

    private void drawStatistics(GraphicsContext gc) {
        // Statistics Section
        gc.setFill(Color.RED);
        gc.fillText("Statistics", 50, statisticsYStart);

        gc.setFill(Color.WHITE);
        gc.fillText("Schedule Name: ABC Schedule", 50, statisticsYStart + statisticsLineSpacing);
        gc.fillText("AWT: 3125", 50, statisticsYStart + 2 * statisticsLineSpacing);
        gc.fillText("ATA: 12331", 50, statisticsYStart + 3 * statisticsLineSpacing);
    }

    public void generateGraph() {
        launch();
    }
}