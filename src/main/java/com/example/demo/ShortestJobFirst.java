package com.example.demo;

import java.util.*;

public class ShortestJobFirst extends Scheduler{
    @Override
    public ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        processList.sort(Comparator.comparing(Process::getArrivalTime).thenComparing(Process::getBurstTime));
        Queue<Process> readyQueue = new PriorityQueue<>(Comparator.comparing(Process::getBurstTime).thenComparing(Process::getWaitingTime));
        int totalTime= processList.stream().mapToInt(Process::getBurstTime).sum();  //loops to get the total running time for the processes
        ArrayList<Integer> schedule=new ArrayList<>(totalTime);  //the returned schedule for the graph representation
        int currentTime=0;  //used as a tracker for time and index for the schedule array
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
            for (int i = 0; i < currentProcess.getBurstTime(); i++) {  //handles updating the schedule array
                schedule.add(currentTime, currentProcess.getPid());
                currentTime++;
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
