package com.example.demo;

import java.util.ArrayList;

public abstract class Scheduler {
    String schedulerName;
    abstract ArrayList<Integer> createSchedule(ArrayList<Process> processList);
}
