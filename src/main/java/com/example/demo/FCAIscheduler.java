package com.example.demo;

import java.util.*;
import static java.lang.Math.ceil;

public class FCAIscheduler extends Scheduler {
    FCAIscheduler(int contextSwitchOverhead) {
        schedulerName = "FCAI Scheduler";
        this.contextSwitchOverhead = contextSwitchOverhead;
    }

    int calcFactor(int Priority, int Arrival, double V1, int RemBurst, double V2){
        return (int)ceil((10 - Priority) + ceil(Arrival / V1) + ceil(RemBurst / V2));
    }

    private double getV1(ArrayList<Process> processes){
        int last = Integer.MIN_VALUE;
        for (Process process : processes)
        {
            if (process.getArrivalTime() > last)
                last = process.getArrivalTime();
        }
        return last/(double)10;
    }
    private double getV2(ArrayList<Process> processes){
        int max = Integer.MIN_VALUE;
        for (Process process : processes)
        {
            if (process.getBurstTime() > max)
                max = process.getBurstTime();
        }
        return max /(double)10;
    }
    private int processFactor(Process p, double v1, double v2) {
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

    private void consumeOneTimeUnit(ArrayList<Process> processList, ArrayList<Integer> quantumWithID, int bestID) {
        Process current = processList.get(bestID);
        current.setBurstTime(current.getBurstTime() - 1); // Decrement burst time

        //updated quantum
        quantumWithID.set(current.getPid(), quantumWithID.get(current.getPid()) - 1);
    }

    private void mapLinks(ArrayList<Process> processList , Map<Integer, Integer> originalToMappedPID, Map<Integer, Integer> mappedToOriginalPID)
    {
        for (int i = 0; i < processList.size(); i++) {
            originalToMappedPID.put(processList.get(i).getPid(), i);
            mappedToOriginalPID.put(i, processList.get(i).getPid());
            processList.get(i).setPid(i); // Update PID in the process list
        }
    }
    @Override
    ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        //Initialization of used variables
        boolean flag = true, newArrival = false,first = true, repeatedLoop = false;
        int bestID = -1, cntr = 0, bestFactor = Integer.MIN_VALUE;
        double v1 = getV1(processList), v2 = getV2(processList);
        ArrayList<Integer> quantumWithID = new ArrayList<>();//for keeping trace of current quantum of each process
        ArrayList<Integer> schedule = new ArrayList<>();//for return
        Map<Integer, Integer> originalToMappedPID = new HashMap<>();//original to 0..n-1
        Map<Integer, Integer> mappedToOriginalPID = new HashMap<>();//0..n-1 to original
        Queue<Integer> queue = new LinkedList<>();
        Process prevProcess = null;

        //linking the PIDs to start from 0 to n-1 and to return original ids
        mapLinks(processList, originalToMappedPID, mappedToOriginalPID);

        //initialize quantumWithID wth initial IDS
        for (Process process : processList) quantumWithID.add(process.getQuantum());

        //fill Schedule
        while(flag)
        {
            //checking if the counter was modified
            if(!repeatedLoop)
            {
                //getting the processes arriving
                for (int i = 0; i < processList.size(); i++) {
                    if (processList.get(i).getArrivalTime() == cntr) {
                        queue.add(i); // Add process ID to the queue
                        if (first) {
                            bestFactor = processFactor(processList.get(i), v1, v2);
                            bestID = i;
                            first = false;
                            queue.poll();
                            newArrival = true;
                        }
                    }
                }
            }
            else {
                repeatedLoop = false;
                for (int i = 0; i < contextSwitchOverhead;i++) {
                    cntr++;
                    schedule.add(-1);
                    //checks for any values entered during that time
                    for(int x = 0; x < processList.size(); x++)
                        if(processList.get(x).getArrivalTime() == cntr) queue.add(x);
                }
            }
            //checks if a new process entered
            if(newArrival)
            {
                //if a previous process was kicked, we update its quantum here
                if (prevProcess != null) {
                    updateQuantum(processList, quantumWithID, prevProcess);
                }
                //then we consume all the initial 40%
                //get the 40% value
                double q = Math.min(ceil(processList.get(bestID).getQuantum() * 0.4), processList.get(bestID).getBurstTime());
                //adds them to schedule and updates both quantum and burst time
                for (int i = 0; i < q && processList.get(bestID).getBurstTime() > 0;i++) {
                    schedule.add(mappedToOriginalPID.get(bestID));
                    consumeOneTimeUnit(processList,quantumWithID,bestID);
                    //updates the counter
                    cntr++;
                    //checks for any values entered during that time
                    for(int x = 0; x < processList.size(); x++)
                        if(processList.get(x).getArrivalTime() == cntr) queue.add(x);
                }
                newArrival = false;
            }
            //normal processing and factor comparison
            else if(bestID != -1){
                int bestIndex = bestID;
                int size = queue.size();
                //checks if there are any processes waiting
                if(queue.size()>0)
                    //know the best factor
                    for (int i = 0; i < size; i++) {
                        int currentID = queue.poll();
                        int currentFactor = processFactor(processList.get(currentID), v1, v2);
                        //updating best
                        if (currentFactor < bestFactor && processList.get(currentID).getBurstTime()!=0) {
                            bestFactor = currentFactor;
                            bestIndex = currentID;
                        }
                        if(processList.get(currentID).getBurstTime()!=0)
                            queue.add(currentID); // Re-enqueue
                    }

                //making sure the process was swapped
                if(bestIndex != bestID){
                    //removing the best
                    for (int i = 0; i < size; i++) {
                        int currentID = queue.poll();
                        if (currentID != bestIndex && processList.get(currentID).getBurstTime()!=0) {
                            queue.add(currentID);
                        }
                    }
                    //updating the prevProcess to contain the value of the process we are about to remove and adds it back to the queue
                    prevProcess = processList.get(bestID);
                    if(processList.get(bestID).getBurstTime()!=0)
                        queue.add(bestID);
                    //updates the current process working
                    bestID = bestIndex;
                    bestFactor = processFactor(processList.get(bestID), v1, v2);
                    newArrival = true;
                    //makes sure that the process consumes its deserved time
                    cntr--;
                    repeatedLoop = true;
                }
                else
                {
                    //checks if the process have time left
                    if(processList.get(bestID).getBurstTime() != 0)
                    {
                        //checks if the process has quantum left
                        if(quantumWithID.get(bestID) != 0) {
                            consumeOneTimeUnit(processList, quantumWithID, bestID);
                            schedule.add(mappedToOriginalPID.get(bestID));
                        }
                        else
                        {
                            //since the quantum is done we put the working function back to the end of the queue,
                            // and we make the first of the queue to be the current process
                            prevProcess = processList.get(bestID);
                            if(processList.get(bestID).getBurstTime()!=0)
                                queue.add(bestID);
                            bestID = queue.poll();
                            bestFactor = processFactor(processList.get(bestID), v1, v2);
                            newArrival = true;
                            cntr--;
                            repeatedLoop = true;
                        }
                    }
                    //checks if the queue has values
                    else if(!queue.isEmpty())
                    {
                        //makes the first queue element work and deducts the counter, so it can work
                        prevProcess = processList.get(bestID);
                        if(processList.get(bestID).getBurstTime()!=0)
                            queue.add(bestID);
                        bestID = queue.poll();
                        bestFactor = processFactor(processList.get(bestID), v1, v2);
                        newArrival = true;
                        cntr--;
                        repeatedLoop = true;
                    }
                    //checks if there is no process that still needs to work or is arriving in the feature
                    else if(processList.stream().noneMatch(process -> process.getBurstTime() > 0))
                    {
                        //ends the loop
                        flag = false;
                    }
                    else
                    {
                        //if a process is arriving in the feature and no current process needs to work, we add a -1 to the schedule
                        schedule.add(-1);
                    }
                }
                //time keeper
                cntr++;
            }
            else {
                schedule.add(-1);
                cntr++;
            }
        }
        return schedule;
    }
}