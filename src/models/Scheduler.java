package models;

import java.util.LinkedList;
import java.util.Queue;

import models.process.*;
import models.process.Process;
import controllers.Driver;

public class Scheduler {
	
	public Kernel kernel;
	public int timeSlice;
	public Queue<PCB> readyQueue;
	public Queue<PCB> waitingQueue;
	
	public Scheduler(Kernel kernel, int timeSlice) {
		this.kernel = kernel;
		this.timeSlice = timeSlice;
		readyQueue = new LinkedList<>();
		waitingQueue = new LinkedList<>();
	}
	
	public void schedule() {
		while(true) {
			if (readyQueue.isEmpty()) /* Should add an exit command or smth */ {
				Driver.tick();
				continue;
			}
			
			PCB pcb = readyQueue.remove();
			dispatchProcess(pcb);
		}
	}
	
	public void admitProcess(PCB pcb ){
		readyQueue.add(pcb);
	}
	
	public PCB dispatchProcess(PCB pcb) {
		return new PCB();
	}
	
	public PCB terminateProcess(Process process) {
		return new PCB();
	}
	
	public void interruptProcess() {
		
	}
	
	public void blockProcess() {
		
	}
	
}