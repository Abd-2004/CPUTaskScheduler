package com.example.demo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class ShortestRemainingTimeFirst extends Scheduler{
    @Override
    public ArrayList<Integer> createSchedule(ArrayList<Process> processList) {//Shortest Remaining Time First Algorithm
        processList.sort(Comparator.comparing(Process::getArrivalTime).thenComparing(Process::getBurstTime));
        Queue<Process> readyQueue = new PriorityQueue<>(Comparator.comparing(Process::getBurstTime).thenComparing(Process::getWaitingTime));
        int totalTime= processList.stream().mapToInt(Process::getBurstTime).sum();
        ArrayList<Integer> schedule=new ArrayList<>(totalTime);
        int currentTime=0;
        int arrivingProcess=0;
        while(currentTime<totalTime){
            while (arrivingProcess < processList.size() && processList.get(arrivingProcess).getArrivalTime() <= currentTime) {  //handles the dynamic insertion of the processes in the ready queue
                readyQueue.add(processList.get(arrivingProcess));
                arrivingProcess++;
            }
            for(Process process: readyQueue){  //responsible for aging updates for the processes in the ready queue
                process.setWaitingTime(process.getWaitingTime()+1);
            }
            if (readyQueue.isEmpty()) {  //handles if no process is currently using the CPU
                schedule.add(currentTime, -1);
                currentTime++;
                continue;
            }
            Process currentProcess = readyQueue.poll();  //pops the process with the highest priority
            currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
            schedule.add(currentTime, currentProcess.getPid());
            currentTime++;
            if(currentProcess.getBurstTime()>0){
                readyQueue.add(currentProcess);
            }
        }
        return schedule;
    }
}
