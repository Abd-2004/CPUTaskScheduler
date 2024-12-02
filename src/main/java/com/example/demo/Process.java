package com.example.demo;

public class Process {
    private int pid;
    private String name;
    private String color;
    private int arrivalTime;
    private int burstTime;
    private int priority;

    Process(int pid, String name, String color, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }
}
