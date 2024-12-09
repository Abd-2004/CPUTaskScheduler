package com.example.demo;

import java.util.*;

public class ShortestJobFirst extends Scheduler{

    private int bracketLength = 100;

    public ShortestJobFirst(int contextSwitchOverhead) {
        schedulerName = "Shortest Job First";
        this.contextSwitchOverhead = contextSwitchOverhead;
    }
    @Override
    public ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        ArrayList<Integer> schedule = new ArrayList<>();
        int curTime = 0;
        while (!processList.isEmpty()) {
            Process best = null;  //a placeholder for the best process to be processed
            for (Process p : processList) { //find the best process according to arrival time (criteria 1)
                if (p.getArrivalTime() <= curTime) {
                    if (best == null) {
                        best = p;
                        continue;
                    }
                    int bracket = p.getWaitingTime() / bracketLength;  //separates the present processes into intervals according to their waiting time
                    int bestBracket = best.getWaitingTime() / bracketLength;
                    if (bracket != bestBracket) { //age brackets prevent starvation by prioritizing processes in higher brackets (aka the oldest process and criteria 2)
                        if (bracket > bestBracket) best = p;
                    }
                    else if (p.getBurstTime() < best.getBurstTime()) best = p;  //compares according to burst time(criteria 3)
                    else if (p.getBurstTime() == best.getBurstTime() && p.getArrivalTime() < best.getArrivalTime()) best = p; //compares according to arrival time as a last resort (criteria 4)
                }
            }
            if (best == null) { //handles the case the cpu being idle/no process is in the ready queue
                schedule.add(-1);
                curTime++;
                continue;
            }
            for (int i = 0; i < best.getBurstTime(); i++) {
                for (Process p : processList) {
                    if (p != best && p.getArrivalTime() <= curTime) p.setWaitingTime(p.getWaitingTime()+1); //update waiting times for all processes that are not running now
                }
                schedule.add(best.getPid());  //feeds the schedule with the currently being processed process
                curTime++;
            }
            processList.remove(best);  //removes the process from the list
            for (int i = 0; i < contextSwitchOverhead; i++) { //adds the context switch overhead
                schedule.add(-1);
                curTime++;
            }

        }
        return schedule;
    }
    public void printSchedule(ArrayList<Integer> schedule) {
        for (Integer integer : schedule) {
            System.out.print(integer + " ");
        }
        System.out.println();
    }
}