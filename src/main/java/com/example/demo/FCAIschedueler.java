package com.example.demo;

import static java.lang.Math.ceil;

public class FCAIschedueler {

    int calcFactor(int Priority, int Arrival, int V1, int RemBurst, int V2)
    {
        return (int)ceil((10 - Priority) + (Arrival / (float)V1) + (RemBurst / (float)V2));
    }

    int getV1(Process[] processes, int n)
    {
        int last = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++)
        {
            if (processes[i].getArrivalTime() > last)
                last = processes[i].getArrivalTime();
        }
        return last / 10;
    }

    int getV2(Process[] processes, int n)
    {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++)
        {
            if (processes[i].getBurstTime() > max)
                max = processes[i].getBurstTime();
        }
        return max / 10;
    }
}
