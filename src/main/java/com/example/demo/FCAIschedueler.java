package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;



public class FCAIschedueler extends Scheduler {

    int calcFactor(int Priority, int Arrival, int V1, int RemBurst, int V2)
    {
        return (int)ceil((10 - Priority) + (Arrival / (float)V1) + (RemBurst / (float)V2));
    }

    private int getV1(ArrayList<Process> processes)
        {
            int last = Integer.MIN_VALUE;
            for (Process process : processes)
            {
                if (process.getArrivalTime() > last)
                    last = process.getArrivalTime();
            }
            return (int)ceil(last / 10);
        }
    private int getV2(ArrayList<Process> processes)
        {
            int max = Integer.MIN_VALUE;
            for (Process process : processes)
            {
                if (process.getBurstTime() > max)
                    max = process.getBurstTime();
            }
            return (int)ceil(max / 10);
        }
    private int processFactor(Process p, int v1, int v2)
    {
        return calcFactor(p.getPriority(),p.getArrivalTime(),v1,p.getBurstTime(),v2);
    }
    private void updateQuantum(ArrayList<Process> processList, ArrayList<Integer> quantumWithID, Process prevProcess) {
        if (quantumWithID.get(prevProcess.getPid()) == 0) {
            prevProcess.setQuantum(prevProcess.getQuantum() + 2);
            quantumWithID.set(prevProcess.getPid(), prevProcess.getQuantum());
            processList.set(prevProcess.getPid(), prevProcess);
        } else {
            prevProcess.setQuantum(prevProcess.getQuantum() + quantumWithID.get(prevProcess.getPid()));
            quantumWithID.set(prevProcess.getPid(), prevProcess.getQuantum());
            processList.set(prevProcess.getPid(), prevProcess);
        }
    }

    private void consumeOneTimeUnit(ArrayList<Process> processList, ArrayList<Integer> quantumWithID, int bestID)
    {
        Process current = processList.get(bestID);
        current.setBurstTime(current.getBurstTime() - 1); // Decrement burst time
        current.setQuantum(current.getQuantum() - 1);    // Decrement quantum

        // Update quantum immediately
        if (current.getQuantum() == 0) {updateQuantum(processList, quantumWithID, current);}
        //updated quantum
        quantumWithID.set(current.getPid(), current.getQuantum());
        processList.set(current.getPid(), current);
    }
    @Override
    ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        //intializing PIDs
        Map<Integer, Integer> originalToMappedPID = new HashMap<>();
        Map<Integer, Integer> mappedToOriginalPID = new HashMap<>();
        for (int i = 0; i < processList.size(); i++) {
            originalToMappedPID.put(processList.get(i).getPid(), i);
            mappedToOriginalPID.put(i, processList.get(i).getPid());
            processList.get(i).setPid(i); // Update PID in the process list
        }



        boolean flag = true,changed = false;

        int v1 = getV1(processList), v2 = getV2(processList), bestID = -1, cntr = 0, bestFactor = -1;

        ArrayList<Integer> quantumWithID = new ArrayList<>();
        ArrayList<Integer> schedule = new ArrayList<>();

        //initialize quantumWithID
        for (Process process : processList) {
            quantumWithID.add(process.getQuantum());
        }

        //fill Schedule
        while(flag)
        {
            bestFactor = -1;
            Process prevProcess = (bestID != -1) ? processList.get(bestID) : null;
            //picks up the best fitting process that arrived and still wants to work if any
            for (int j = 0; j< processList.size(); j++)
            {
                Process current = processList.get(j);
                if(processFactor(current,v1,v2) > bestFactor &&
                   current.getArrivalTime() <=cntr &&
                   current.getBurstTime() > 0)
                {
                    bestFactor = processFactor(current,v1,v2);
                    bestID = current.getPid();
                    //tells us that the current process was kicked out
                    changed = true;
                }
            }
            //makes sure that each process does its 40% of the quantum
            if(changed) {
                double q = Math.min(ceil(processList.get(bestID).getQuantum() * 0.4), processList.get(bestID).getBurstTime());
                //adds to schedule and updating both quantum and burst time
                for (int i = 1; i < q;i++) {
                    schedule.add(mappedToOriginalPID.get(bestID));
                    consumeOneTimeUnit(processList,quantumWithID,bestID);
                };
            }
            //checks if a new process entered
            if (changed) {
                //checks if a process was kicked out
                if (prevProcess != null) {
                    updateQuantum(processList, quantumWithID, prevProcess);
                }
                changed = false;
            }


            if (processList.get(bestID).getBurstTime() > 0 && processList.get(bestID).getQuantum() > 0) {
                consumeOneTimeUnit(processList, quantumWithID, bestID);
                schedule.add(mappedToOriginalPID.get(bestID));
            }
            cntr++;
            flag = processList.stream().anyMatch(process -> process.getBurstTime() > 0);
        }
        return schedule;
    }
}
