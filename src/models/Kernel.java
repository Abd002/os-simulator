package models;

import java.util.ArrayList;

import controllers.Driver;
import models.memory.*;
import models.process.PCB;
import models.process.Process;
import models.process.Variable;

public final class Kernel {

	private int clock;
	public final int MAX_CLOCK;
	public final SystemCalls systemCalls;
	public final Scheduler scheduler;
	public final Mutex mutex;
	public final Memory memory;
	public final Interpreter interpreter;
	public final MemoryManagementUnit mmu;

	public Kernel(int maxClock) {
		clock = 0;
		MAX_CLOCK = maxClock;

		this.scheduler = new Scheduler(this, 2);
		this.systemCalls = new SystemCalls(this);
		this.mutex = new Mutex(this);
		this.memory = new Memory(this, 8);
		this.mmu = new MemoryManagementUnit(this);
		this.interpreter = new Interpreter(this);
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
		String[] programInstructions = systemCalls.readFromDisk(programName);
		String[] PasrsedVariables = interpreter.parseVariables(programInstructions);

		PCB pcb = new PCB(programInstructions.length + PasrsedVariables.length);
		Process process = new Process(pcb, programInstructions, PasrsedVariables);

		int[] allocatedAddress = mmu.allocateMemory(programInstructions.length, pcb.pid);
		if (allocatedAddress[0] == -1) {
			mmu.swapToDisk(process);
		}
		if (allocatedAddress[0] != -1) {
			Variable[] variables = process.getAllVariables();
			for (int i = 0; i < variables.length; i++) {
				systemCalls.writeToMemory(pcb.getMemoryTable()[variables[i].address],
						new MemoryWord(variables[i].name, variables[i].getValue()));
			}
			String[] instructions = process.getInstructions();
			for (int i = 0; i < instructions.length; i++) {
				systemCalls.writeToMemory(pcb.getMemoryTable()[i], new MemoryWord(instructions[i]));
			}
		}
		scheduler.admitProcess(pcb);
	}

	public void saveProcessState(Process process) {
		Variable[] variableArray = process.getAllVariables();
		for (int i = 0; i < variableArray.length; i++) {
			MemoryWord word = new MemoryWord(variableArray[i].name, variableArray[i].getValue());
			systemCalls.writeToMemory(variableArray[i].address, word);
		}
	}

	public Process restoreProcessState(PCB pcb) {
		if (pcb.getMemoryTable()[0] == -1) {
			mmu.swapFromDisk(pcb);
		}
		int[] memoryTable = pcb.getMemoryTable();

		ArrayList<String> instructions = new ArrayList<>();
		ArrayList<String> variablesName = new ArrayList<>();
		ArrayList<String> variablesValue = new ArrayList<>();

		for (int i = 0; i < memoryTable.length; i++) {
			MemoryWord word = systemCalls.readFromMemory(memoryTable[i]);
			if (word.isInstruction()) {
				instructions.add(word.getData());
			}
			if (word.isVariable()) {
				variablesName.add(word.getVariableName());
				variablesValue.add(word.getData());
			}
		}

		String[] instructionsArray = instructions.toArray(new String[instructions.size()]);
		String[] variablesNameArray = variablesName.toArray(new String[variablesName.size()]);
		String[] variablesValueArray = variablesValue.toArray(new String[variablesValue.size()]);

		Process process = new Process(pcb, instructionsArray, variablesNameArray, variablesValueArray);

		return process;
	}

}
