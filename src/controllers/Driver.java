package controllers;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;

public final class Driver {
	
	private static Kernel kernel;
	private static HashMap<Integer, ArrayList<String>> processes;
	
	private Driver() {}
	
	public static void main(String[] args) {
		kernel = new Kernel(100);
		processes = new HashMap<>();
				
		/* TODO: Read program names and times from args*/
		processes.put(0, new ArrayList<String>());
		processes.get(0).add("program0");
		processes.put(1, new ArrayList<String>());
		processes.get(1).add("program1");
		processes.put(4, new ArrayList<String>());
		processes.get(4).add("program2");
		processes.get(4).add("program3");
		
		kernel.run();
	}
	
	public static void checkProcessArrival(int clock) {
		if (processes.containsKey(clock)) {
			processes.get(clock).forEach(process -> kernel.createProcess(process));
		}
	}
	
}
