package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ScheduleVisualizer extends Application {

    private static ArrayList<Process> processes;
    private static ArrayList<Integer> schedule;
    private static String schedulerName;

    private int canvasWidth = 1900;
    private int canvasHeight = 950;
    private int graphStartX = 120;
    private int graphStartY = 60;
    private int maxProcessHeight = 65;
    private int graphHeight = maxProcessHeight*10;
    private int graphWidth = 1000;
    private int xAxisLabelOffset = 20;
    private int infoSpacingX = 125;
    private int statisticsSpacingY = 30;
    int maximumXLabels = 20;

    public ScheduleVisualizer() {}

    public ScheduleVisualizer(ArrayList<Process> processList, Scheduler scheduler) {
        processes = new ArrayList<>();
        for (Process p : processList) processes.add(new Process(p));
        schedule = scheduler.createSchedule(processList);
        schedulerName = scheduler.getSchedulerName();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CPU Scheduling Graph");

        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        drawGraph(gc);

        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawGraph(GraphicsContext gc) {
        //background
        drawRect(gc, 0, 0, canvasWidth, canvasHeight, Color.LIGHTGRAY);

        int pNum = processes.size();

        writeText(gc, "CPU Scheduling Graph", graphStartX, graphStartY - 15, Color.DARKGREEN, 30, 0);

        int processHeight = graphHeight / pNum;
        if (processHeight > maxProcessHeight) processHeight = maxProcessHeight;
        else graphHeight = processHeight * pNum;
        int gridCellWidth = graphWidth / schedule.size();

        //grid lines
        int lastx = graphStartX;
        for (int x = graphStartX; x <= graphStartX+graphWidth; x += gridCellWidth) {
            drawRect(gc, x, graphStartY, 1, graphHeight, Color.DARKSLATEGRAY); //vertical lines
            lastx = x;
        }
        for (int y = graphStartY; y <= graphStartY + graphHeight; y += processHeight) {
            drawRect(gc, graphStartX, y, lastx-graphStartX, 1, Color.DARKSLATEGRAY); //horizontal lines
        }

        //labels for processes
        for (int i = 0, y = graphStartY + processHeight/2+5; i < pNum; i++, y += processHeight) {
            writeText(gc, processes.get(i).getName(), graphStartX-10, y, Color.BLACK, 20, 2);
        }

        //x axis numbers
        int mod = (schedule.size()+maximumXLabels+1)/maximumXLabels;
        for (int i = 0, x = graphStartX; x <= graphStartX + graphWidth; i++, x += gridCellWidth) {
            if (i%mod != 0) continue;
            writeText(gc, Integer.toString(i), x, graphStartY + graphHeight + xAxisLabelOffset, Color.BLACK, 18, 1);
            drawRect(gc, x, graphStartY, 2, graphHeight, Color.BLACK);
        }

        //draw blocks for processes
        for (int i = 0; i < schedule.size(); i++) {
            int x = schedule.get(i);
            if (x != -1) {
                Process p = getProcess(x);
                int index = processes.indexOf(p);
                drawRect(gc, graphStartX + i*gridCellWidth, graphStartY + index*processHeight, gridCellWidth, processHeight, getProcess(x).getColor());
            }
        }

        //info
        int infoStartX = graphStartX + graphWidth + 30;
        drawRect(gc, infoStartX - 10, graphStartY, canvasWidth - 30 - infoStartX, graphHeight, Color.DARKGRAY);
        drawRect(gc, infoStartX - 10, graphStartY-25, canvasWidth - 30 - infoStartX, 25, Color.LIGHTSLATEGRAY);
        gc.strokeRect(infoStartX - 10, graphStartY-25, canvasWidth - 30 - infoStartX, graphHeight+25);
        gc.strokeLine(infoStartX - 10, graphStartY, canvasWidth - 40, graphStartY);
        writeText(gc, "Processes Information", infoStartX, graphStartY - 30, Color.DARKGREEN, 26, 0);
        String[] labels = {"Name", "PID", "Arrival Time", "Priority", "Burst Time", "Color"};
        for (int i = 0; i < labels.length; i++) {
            writeText(gc, labels[i], infoStartX + i*infoSpacingX, graphStartY - 5, Color.DARKRED, 22, 0);
        }
        for (int i = 0, y = graphStartY + processHeight/2+5; i < pNum; i++, y += processHeight) {
            Process p = processes.get(i);
            String[] info = {p.getName(), "" + p.getPid(), "" + p.getArrivalTime(), "" + p.getPriority(), "" + p.getBurstTime()};
            for (int j = 0; j < info.length; j++) {
                writeText(gc, info[j], infoStartX + j*infoSpacingX, y, Color.DARKBLUE, 20, 0);
            }
            drawRect(gc, infoStartX + info.length*infoSpacingX, y - 20, 25, 25, p.getColor());
            gc.strokeRect(infoStartX + info.length*infoSpacingX, y - 20, 25, 25);
        }

        //calculate awt and atat
        Map<Integer, Integer> firstExecution = new HashMap<>(), lastExecution = new HashMap<>();
        for (int i = 0; i < pNum; i++) {
            int pid = processes.get(i).getPid();
            firstExecution.put(pid, -1);
            lastExecution.put(pid, -1);
        }
        for (int i = 0; i < schedule.size(); i++) {
            int x = schedule.get(i);
            if (x == -1) continue;
            if (firstExecution.get(x) == -1) firstExecution.put(x, i);
            lastExecution.put(x, i + 1);
        }
        Double avgWaitingTime = 0.0, avgTurnAroundTime = 0.0;
        for (int i = 0; i < pNum; i++) {
            int pid = processes.get(i).getPid();
            avgWaitingTime += lastExecution.get(pid) - processes.get(i).getArrivalTime() - processes.get(i).getBurstTime();
            avgTurnAroundTime += lastExecution.get(pid) - firstExecution.get(pid);
        }
        avgWaitingTime /= pNum;
        avgTurnAroundTime /= pNum;

        //stats
        writeText(gc, "Statistics", graphStartX, graphStartY + graphHeight + 75, Color.DARKBLUE, 30, 0);
        String[] stats = {"Schedule Name: " + schedulerName, "Average Waiting Time: " + avgWaitingTime, "Average Turn Around Time: " + avgTurnAroundTime};
        for (int i = 0; i < stats.length; i++) {
            writeText(gc, stats[i], graphStartX, graphStartY + graphHeight + 110 + i*statisticsSpacingY, Color.GREEN, 24, 0);
        }
    }

    private void drawRect(GraphicsContext gc, int x, int y, int width, int height, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }

    private void writeText(GraphicsContext gc, String text, int x, int y, Color color, int size, int alignment) {
        Font font = new Font("Arial", size);
        gc.setFill(color);
        gc.setFont(font);
        if (alignment > 0)
        {
            Text tx = new Text(text);
            tx.setFont(font);
            int textWidth = (int)tx.getBoundsInLocal().getWidth();
            if (alignment == 1) x = x-textWidth/2;
            else x = x-textWidth;
            gc.fillText(text, x, y);
        }
        gc.fillText(text, x, y);
    }

    private Process getProcess(int pid) {
        Process result = null;
        for (Process p : processes) {
            if (p.getPid() == pid) result = p;
        }
        return result;
    }

    public void generateGraph() {
        launch();
    }
}