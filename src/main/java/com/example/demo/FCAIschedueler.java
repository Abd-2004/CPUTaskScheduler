package com.example.demo;

import java.util.ArrayList;

import static java.lang.Math.ceil;


import java.util.ArrayList;

public class FCAIschedueler extends Scheduler {

    int calcFactor(int Priority, int Arrival, int V1, int RemBurst, int V2)
    {
        return (int)ceil((10 - Priority) + (Arrival / (float)V1) + (RemBurst / (float)V2));
    }

        int getV1(ArrayList<Process> processes)
        {
            int last = Integer.MIN_VALUE;
            for (Process process : processes)
            {
                if (process.getArrivalTime() > last)
                    last = process.getArrivalTime();
            }
            return last / 10;
        }
        int getV2(ArrayList<Process> processes)
        {
            int max = Integer.MIN_VALUE;
            for (Process process : processes)
            {
                if (process.getBurstTime() > max)
                    max = process.getBurstTime();
            }
            return max / 10;
        }

    @Override
    ArrayList<Integer> createSchedule(ArrayList<Process> processList) {
        return null;
    }
}
