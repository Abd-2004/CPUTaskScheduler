package com.example.demo;

import java.util.ArrayList;

public abstract class Scheduler {
    protected int contextSwitchOverhead;
    protected String schedulerName = "Scheduler";
    abstract ArrayList<Integer> createSchedule(ArrayList<Process> processList);
    public String getSchedulerName() {
        return schedulerName;
    }
}
