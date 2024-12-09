package com.example.demo;

import javafx.scene.paint.Color;

public class Process {
    private int pid;
    private String name;
    private Color color;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int waitingTime;
    private int quantum;

    Process(int pid, String name, Color color, int arrivalTime, int burstTime, int priority, int quantum) {
        this.pid = pid;
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.quantum = quantum;
    }

    Process(Process p) {
        this.pid = p.pid;
        this.name = p.name;
        this.color = p.color;
        this.arrivalTime = p.arrivalTime;
        this.burstTime = p.burstTime;
        this.priority = p.priority;
        this.waitingTime = p.waitingTime;
        this.quantum = p.quantum;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getWaitingTime() {
      return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
      this.waitingTime = waitingTime;
    }

    public int getQuantum(){return quantum; }

    public void setQuantum(int quantum){
        this.quantum = quantum;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
