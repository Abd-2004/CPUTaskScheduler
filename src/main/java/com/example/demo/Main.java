package com.example.demo;

import java.util.ArrayList;

public class Main {
        public static void main(String[] args) {
            // Create an ArrayList to hold Process objects
            ArrayList<Process> processList = new ArrayList<>();

            // Add processes to the list using the provided data
            processList.add(new Process(1, "P1", "Red", 0, 17, 4, 4));  // P1: Burst time = 17, Arrival time = 0, Priority = 4, Quantum = 4
            processList.add(new Process(2, "P2", "Blue", 3, 6, 9, 3));   // P2: Burst time = 6, Arrival time = 3, Priority = 9, Quantum = 3
            processList.add(new Process(3, "P3", "Green", 4, 10, 3, 5)); // P3: Burst time = 10, Arrival time = 4, Priority = 3, Quantum = 5
            processList.add(new Process(4, "P4", "Yellow", 29, 4, 8, 2)); // P4: Burst time = 4, Arrival time = 29, Priority = 8, Quantum = 2
            FCAIschedueler fcaIschedueler = new FCAIschedueler();
            ArrayList<Integer> schedule = fcaIschedueler.createSchedule(processList);
            for (int a:schedule)
                System.out.print(a+" ");
    }

}
