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
		this.memory = new Memory(100);
		this.mmu = new MemoryManagementUnit(this);
		this.interpreter = new Interpreter(this);
	}
	
	private void printState() {
		systemCalls.writeToScreen("\nKERNEL :: CLOCK CYCLE " + clock + " #####################");
		
		systemCalls.writeToScreen("\nMEMORY :: CONTENT (" + memory.MAX_SIZE + " WORDS) ----------------------");
		MemoryWord[] physicalMemory = memory.getMemory();
		int[] processIds = mmu.getProcessIDs();
		for (int i = 0; i < memory.MAX_SIZE; i++) {
			MemoryWord word = physicalMemory[i];
								
			if (processIds[i] != -1) {
				systemCalls.writeToScreen("MEM BLOCK #" + (i+1));
				systemCalls.writeToScreen("\tSTATUS: Allocated for process #" + processIds[i]);
				systemCalls.writeToScreen("\tTYPE: " + (word.isInstruction() ? "Instruction" : "Variable"));
				
				if (word.isInstruction()) {
					systemCalls.writeToScreen("\tINSTRUCTION: " + word.getData());
				}
				if (word.isVariable()) {
					systemCalls.writeToScreen("\tVARIABLE NAME: " + word.getVariableName());
					systemCalls.writeToScreen("\tVARIABLE VALUE: " + word.getData());					
				}
			}
		}
		
		systemCalls.writeToScreen("\nSCHEDULER :: QUEUES -----------------------------");
		systemCalls.writeToScreen("READY QUEUE");
		scheduler.getReadyQueue().forEach(pcb -> systemCalls.writeToScreen("\tProcess #" + pcb.pid));
		systemCalls.writeToScreen("INPUT WAITING QUEUE");	
		scheduler.getInputWaitingQueue().forEach(pcb -> systemCalls.writeToScreen("\tProcess #" + pcb.pid));
		systemCalls.writeToScreen("OUTPUT WAITING QUEUE");
		scheduler.getOutputWaitingQueue().forEach(pcb -> systemCalls.writeToScreen("\tProcess #" + pcb.pid));
		systemCalls.writeToScreen("FILE WAITING QUEUE");
		scheduler.getFileWaitingQueue().forEach((f, q) -> {
			if (!q.isEmpty()) {
				systemCalls.writeToScreen("\tFile Name: " + f);
				q.forEach(pcb -> systemCalls.writeToScreen("\t\tProcess #" + pcb.pid));
			}
		});
		
		systemCalls.writeToScreen("\nMUTEX :: LOCKS -----------------------------");
		systemCalls.writeToScreen("Input Mutex: " + (mutex.isInputMutexFree() ? "AVAILABLE" : "LOCKED"));
		systemCalls.writeToScreen("Output Mutex: " + (mutex.isOutputMutexFree() ? "AVAILABLE" : "LOCKED"));
		String[] lockedFiles = mutex.getMutexLockedFiles();
		if (lockedFiles.length != 0) {
			systemCalls.writeToScreen("Locked Files: ");
			for (int i = 0; i < lockedFiles.length; i++) {
				systemCalls.writeToScreen("\tFile Name: " + lockedFiles[i]);
			}
		}
	}

	public void incrementClock() {
		clock++;
		printState(); // COMMENT ME OUT TO ONLY SEE THE PROGRAMS OUTPUT 
		Driver.checkProcessArrival(clock);
		return;
	}
	
	public int getClock() {
		return clock;
	}

	public void run() {
		systemCalls.writeToScreen("START OF SIMULATION #############################################");
		scheduler.schedule();
		systemCalls.writeToScreen("END OF SIMULATION #############################################");
		systemCalls.scanner.close();
		return;
	}

	public void createProcess(String programName) {
		String[] programInstructions = systemCalls.readFromDisk(programName);
		String[] PasrsedVariables = interpreter.parseVariables(programInstructions);

		PCB pcb = new PCB(programInstructions.length + PasrsedVariables.length);
		Process process = new Process(pcb, programInstructions, PasrsedVariables);

		int[] allocatedAddress = mmu.allocateMemory(programInstructions.length + PasrsedVariables.length, pcb.pid);
		pcb.setMemoryTable(allocatedAddress);
		if (allocatedAddress[0] == -1) {
			mmu.swapToDisk(process);
		}
		if (allocatedAddress[0] != -1) {
			Variable[] variables = process.getAllVariables();
			for (int i = 0; i < variables.length; i++) {
				systemCalls.writeToMemory(pcb.getMemoryTable()[variables[i].address],
						new MemoryWord(variables[i].getValue(), variables[i].name));
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
			MemoryWord word = new MemoryWord(variableArray[i].getValue(), variableArray[i].name);
			systemCalls.writeToMemory(process.pcb.getMemoryTable()[variableArray[i].address], word);
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
