package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import models.process.*;
import models.process.Process;

public final class Scheduler {
	
	private final Kernel kernel;
	public final int TIME_SLICE;
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
		pcb.setState(ProcessState.READY);
		readyQueue.add(pcb);
	}
	
	private void dispatchProcess(PCB pcb) {
		kernel.systemCalls.writeToScreen("SCHEDULER :: Process #" + pcb.pid + " started EXECUTION at CLOCK CYCLE " + kernel.getClock());		

		int timeElapsed = 0;	
		Process process = kernel.restoreProcessState(pcb);
		pcb.setState(ProcessState.RUNNING);
		
		while (timeElapsed != TIME_SLICE) {
			boolean executionSucceeded = kernel.interpreter.executeInstruction(process);
			kernel.incrementClock();
			timeElapsed++;
			
			if (process.pcb.getPC() == process.getInstructions().length) {
				terminateProcess(pcb);
				return;
			}
			
			if (!executionSucceeded)
				return; // Interpreter should have blocked the process according to the resource
		}
		
		interruptProcess(process);
	}
	
	public void blockProcess(Process process, String resource) {
		process.pcb.setState(ProcessState.WAITING);
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
				break;
			if (!fileWaitingQueue.get(resource).isEmpty())
				admitProcess(fileWaitingQueue.get(resource).remove());
		}
	}
	
	private void interruptProcess(Process process) {
		kernel.systemCalls.writeToScreen("SCHEDULER :: Process #" + process.pcb.pid + " was INTERRUPTED at CLOCK CYCLE " + kernel.getClock());		
		kernel.saveProcessState(process);
		admitProcess(process.pcb);
	}
	
	private void terminateProcess(PCB pcb) {
		kernel.systemCalls.writeToScreen("SCHEDULER :: Process #" + pcb.pid + " was TERMINATED at CLOCK CYCLE " + kernel.getClock());		
		pcb.setState(ProcessState.TERMINATED);
		kernel.mmu.deallocateMemory(pcb.getMemoryTable());
		pcb.deleteProcess();
	}
	
	public Queue<PCB> getReadyQueue() {
		return new LinkedList<>(readyQueue);
	}
	
	public Queue<PCB> getInputWaitingQueue () {
		return new LinkedList<>(inputWaitingQueue);
	}
	
	public Queue<PCB> getOutputWaitingQueue() {
		return new LinkedList<>(outputWaitingQueue);		
	}
	
	public HashMap<String, Queue<PCB>> getFileWaitingQueue() {
		return new HashMap<String, Queue<PCB>>(fileWaitingQueue);
	}

}