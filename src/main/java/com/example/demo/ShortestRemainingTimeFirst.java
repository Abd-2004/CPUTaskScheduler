package com.example.demo;

import java.util.*;

public class ShortestRemainingTimeFirst extends Scheduler{
    private int bracketLength = 100;
    public ShortestRemainingTimeFirst() {
        schedulerName = "Shortest Remaining Time First";
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
                    int bracket = p.getWaitingTime() / bracketLength;
                    int bestBracket = best.getWaitingTime() / bracketLength;
                    if (bracket != bestBracket) { //age brackets prevent starvation by prioritizing processes in higher brackets
                        if (bracket > bestBracket) best = p;
                    }
                    else if (p.getBurstTime() < best.getBurstTime()) best = p;
                }
            }
            if (best == null) { //no process needs to run at this time
                schedule.add(-1);
                curTime++;
                continue;
            }

            for (Process p : processList) {
                if (p != best && p.getArrivalTime() <= curTime) p.setWaitingTime(p.getWaitingTime()+1); //update waiting times for all processes that are not running now
            }
            schedule.add(best.getPid());
            curTime++;
            best.setBurstTime(best.getBurstTime()-1);
            if (best.getBurstTime() == 0) processList.remove(best);
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