package models;

import controllers.Driver;
import models.memory.*;
import models.process.PCB;
import models.process.Process;

public class Kernel {

	private int clock;
	public final int MAX_CLOCK;
	public final SystemCalls systemCalls;
	public final Scheduler scheduler;
	public final Mutex mutex;
	public final Memory memory;
	// public final Interpreter interpreter; /* not exist yet */
	public final MemoryManagementUnit mmu;

	public Kernel(int maxClock) {
		clock = 0;
		MAX_CLOCK = maxClock;

		this.scheduler = new Scheduler(this, 2);
		this.systemCalls = new SystemCalls();
		this.mutex = new Mutex(this);
		this.memory = new Memory(this, 8);
		this.mmu = new MemoryManagementUnit(this);
	}

	public void incrementClock() {
		clock++;
		Driver.checkProcessArrival(clock);
		return;
	}

	public int getClock() {
		return clock;
	}

	public void printMessage(String msg) {
		System.out.println(msg);
		return;
	}

	public void run() {
		scheduler.schedule();
		System.out.println("END OFSIMULATION");
		return;
	}

	public void createProcess(String programName) {

	}

	public void saveProcessState(Process process) {
	}
	
	public Process restoreProcessState(PCB pcb) {
		
		return null;
	}


}
