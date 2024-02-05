package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import models.process.*;
import models.process.Process;

public final class Scheduler {
	
	private final Kernel kernel;
	private final int TIME_SLICE;
	private final Queue<PCB> readyQueue;
	private final Queue<PCB> inputWaitingQueue;
	private final Queue<PCB> outputWaitingQueue;
	private final HashMap<String, Queue<PCB>> fileWaitingQueue;

	public Scheduler(Kernel kernel, int timeSlice) {
		this.kernel = kernel;
		this.TIME_SLICE = timeSlice;
		readyQueue = new LinkedList<>();
		inputWaitingQueue = new LinkedList<>();
		outputWaitingQueue = new LinkedList<>();
		fileWaitingQueue = new HashMap<>();
	}
	
	public void schedule() {
		while (kernel.getClock() < kernel.MAX_CLOCK) {
			if (readyQueue.isEmpty()) {
				kernel.incrementClock();
				continue;
			}
			dispatchProcess(readyQueue.remove());
		}
	}
	
	public void admitProcess(PCB pcb) {
		// TODO: pcb.setState(ProcessState.READY);
		readyQueue.add(pcb);
	}
	
	private void dispatchProcess(PCB pcb) {
		// TODO: pcb.setState(ProcessState.RUNNING);
		int timeElapsed = 0;
		Process process = kernel.restoreProcessState(pcb);
		
		// TODO: Implement the rest of the method
	}
	
	public void blockProcess(Process process, String resource) {
		// TODO: process.pcb.setState(ProcessState.WAITING);
		kernel.saveProcessState(process);
		
		switch(resource) {
		case "input":
			inputWaitingQueue.add(process.pcb);
			break;
		case "output":
			outputWaitingQueue.add(process.pcb);
			break;
		default:
			if (!fileWaitingQueue.containsKey(resource))
				fileWaitingQueue.put(resource,  new LinkedList<>());
			fileWaitingQueue.get(resource).add(process.pcb);
		}
	}
	
	public void unblockProcess(String resource) {
		switch(resource) {
		case "input":
			if (!inputWaitingQueue.isEmpty())
				admitProcess(inputWaitingQueue.remove());
			break;
		case "output":
			if (!outputWaitingQueue.isEmpty())
				admitProcess(outputWaitingQueue.remove());
			break;
		default:
			if (!fileWaitingQueue.containsKey(resource))
				return; // TODO: Throw an error
			if (!fileWaitingQueue.get(resource).isEmpty())
				admitProcess(fileWaitingQueue.get(resource).remove());
		}
	}
	
	private void interruptProcess(Process process) {
		kernel.saveProcessState(process);
		admitProcess(process.pcb);
	}
	
	private void terminateProcess(PCB pcb) {
		// TODO: pcb.setState(ProcessState.TERMINATED);
		kernel.mmu.deallocateMemory(pcb.getMemoryTable());
		pcb.deleteProcess();
	}

}