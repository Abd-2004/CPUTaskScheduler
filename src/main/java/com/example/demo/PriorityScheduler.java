package com.example.demo;

import java.util.*;

public class PriorityScheduler extends Scheduler{
    public PriorityScheduler() {
        schedulerName = "Priority Scheduler";
    }
    @Override
    public ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        ArrayList<Integer> schedule = new ArrayList<>();
        int curTime = 0;
        while (!processList.isEmpty()) {
            Process best = null;
            for (Process p : processList) { //find which process should go next
                if (p.getArrivalTime() <= curTime) {
                    if (best == null) {
                        best = p;
                        continue;
                    }
                    if (p.getPriority() > best.getPriority()) best = p;
                    else if (p.getPriority() == best.getPriority() && p.getArrivalTime() < best.getArrivalTime()) best = p;
                }
            }
            if (best == null) { //no process needs to run at this time
                schedule.add(-1);
                curTime++;
                continue;
            }
            for (int i = 0; i < best.getBurstTime(); i++) {
                schedule.add(best.getPid());
                curTime++;
            }
            processList.remove(best);
        }
        return schedule;
    }
}