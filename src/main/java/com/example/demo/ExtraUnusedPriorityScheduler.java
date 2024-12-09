package com.example.demo;


import java.util.ArrayList;
import java.util.PriorityQueue;

public class ExtraUnusedPriorityScheduler extends Scheduler{
    PriorityQueue<Process> processQueue = new PriorityQueue<Process>((a,b) -> a.getPriority()-b.getPriority());
    //smallest priority means most priority


    @Override
    ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        schedulerName = "Priority Scheduling";
        ArrayList<Integer> schedule = new ArrayList<>(); //return array

        int currentTime = 0;
        Process currentProcess = null;
        while(!processList.isEmpty() || !processQueue.isEmpty() || currentProcess != null){
            //long-term scheduling
            if(!processList.isEmpty()){
                for (int i = 0; i < processList.size(); i++) {
                    if (processList.get(i).getArrivalTime() == currentTime){
                        processQueue.add(processList.remove(i));
                    }
                }
            }

            //short term scheduling
                if (currentProcess == null && !processQueue.isEmpty()) {
                    currentProcess = processQueue.remove(); //choosing next process and leave context switch
                }
                else if (currentProcess !=null && currentProcess.getBurstTime() > 0){
                    schedule.add(currentProcess.getPid()); //run process
                    currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
                }
                else{
                    currentProcess = null; //enter context switch
                    schedule.add(-1); //context switch gap
                }
            //gg

            //pass time
            currentTime++;
        }

        return schedule;
    }
}
