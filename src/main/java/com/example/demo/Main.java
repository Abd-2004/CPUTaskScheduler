package com.example.demo;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.LIME, Color.BROWN, Color.INDIGO};
        ArrayList<Process> processes = new ArrayList<>();

        boolean askForPid = false;
        boolean askForName = false;
        boolean askForColor = false;
        boolean askForArrivalTime = false;
        boolean askForBurstTime = false;
        boolean askForPriority = false;
        boolean askForQuantum = false;

        Scanner scn =  new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numOfProcesses = scn.nextInt();
        System.out.print("Enter value of context switch overhead: ");
        int c = scn.nextInt();

        Scheduler[] schedulers = {new FCAIscheduler(c), new PriorityScheduler(c), new ShortestJobFirst(c), new ShortestRemainingTimeFirst(c)};
        System.out.println("Choose which scheduler should be used:");
        for (int i = 0; i < schedulers.length; i++) {
            System.out.println("#" + (i+1) + ": " + schedulers[i].getSchedulerName());
        }
        System.out.print("Choice: ");
        int scheduler = scn.nextInt();
        if (scheduler <= 1) scheduler = 1;
        if (scheduler >= schedulers.length) scheduler = schedulers.length;

        for (int i = 0; i < numOfProcesses; i++) {
            int pid, arrivalTime, burstTime, priority, quantum;
            String name;
            Color color;

            System.out.print("Pid of process #" + (i+1) + ": ");
            if (askForPid) pid = scn.nextInt();
            else {
                pid = 3001 + i;
                System.out.println(pid);
            }

            System.out.print("Name of process #" + (i+1) + ": ");
            if (askForName) name = scn.next();
            else {
                name = "P" + (i+1);
                System.out.println(name);
            }

            System.out.print("Color of process #" + (i+1) + " (#RRGGBB): ");
            if (askForColor) {
                String input = scn.next();
                color = Color.web(input);
            }
            else {
                color = colors[i%colors.length];
                String col = color.toString();
                col = "#" + col.substring(2, col.length()-2);
                System.out.println(col);
            }

            System.out.print("Arrival time of process #" + (i+1) + ": ");
            if (askForArrivalTime) arrivalTime = scn.nextInt();
            else {
                arrivalTime = rand.nextInt(0, 80);
                System.out.println(arrivalTime);
            }

            System.out.print("Burst time of process #" + (i+1) + ": ");
            if (askForBurstTime) burstTime = scn.nextInt();
            else {
                burstTime = rand.nextInt(1, 40);
                System.out.println(burstTime);
            }

            System.out.print("Priority of process #" + (i+1) + ": ");
            if (askForPriority) priority = scn.nextInt();
            else {
                priority = rand.nextInt(1, 10);
                System.out.println(priority);
            }

            System.out.print("Quantum of process #" + (i+1) + ": ");
            if (askForQuantum) quantum = scn.nextInt();
            else {
                quantum = rand.nextInt(1, 10);
                System.out.println(quantum);
            }

            Process p = new Process(pid, name, color, arrivalTime, burstTime, priority, quantum);
            processes.add(p);
        }
        ScheduleVisualizer sv = new ScheduleVisualizer(processes, schedulers[scheduler-1]);
        sv.generateGraph();
    }
}
