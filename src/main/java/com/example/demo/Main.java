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

        Scheduler scheduler = new PriorityScheduler();

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
                String c = color.toString();
                c = "#" + c.substring(2, c.length()-2);
                System.out.println(c);
            }

            System.out.print("Arrival time of process #" + (i+1) + ": ");
            if (askForArrivalTime) arrivalTime = scn.nextInt();
            else {
                arrivalTime = rand.nextInt(0, 100);
                System.out.println(arrivalTime);
            }

            System.out.print("Burst time of process #" + (i+1) + ": ");
            if (askForBurstTime) burstTime = scn.nextInt();
            else {
                burstTime = rand.nextInt(5, 30);
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
                quantum = rand.nextInt(1, 20);
                System.out.println(quantum);
            }

            Process p = new Process(pid, name, color, arrivalTime, burstTime, priority, quantum);
            processes.add(p);
        }
        ScheduleVisualizer sv = new ScheduleVisualizer(processes, scheduler);
        sv.generateGraph();
    }
}
