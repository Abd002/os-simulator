package models;

import controllers.Driver;
import models.memory.*;
import models.process.PCB;
import models.process.Process;
import models.process.Variable;

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

		String[] program = systemCalls.readFromDisk(programName);
		int[] addresses = mmu.allocateMemory(program.length, 0);
		if (addresses.length == 0 || addresses[0] == -1) {
			
		}
	}

	public void saveProcessState(Process process) {
		Variable[] variableArray = process.getAllVariables();
		for (int i = 0; i < variableArray.length; i++) {

			MemoryWord word = new MemoryWord();
			word.setData(variableArray[i].getValue());
			word.setVariableName(variableArray[i].name);

			systemCalls.writeToMemory(variableArray[i].address, word);
		}
	}

	public Process restoreProcessState(PCB pcb) {

		return null;
	}

}
