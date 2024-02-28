package models.process;

import java.util.Arrays;

public final class PCB {

	public final int pid;
	private ProcessState state;
	private int programCounter;
	private int[] memoryTable;

	public PCB(int processSize) {
		this.pid = UniqueIdGenerator.generateUniqueId();
		this.state = ProcessState.NEW;
		this.programCounter = 0;
		this.memoryTable = new int[processSize];
	}

	public ProcessState getState() {
		return state;
	}

	public void setState(ProcessState state) {
		this.state = state;
	}

	public int getPC() {
		return programCounter;
	}

	public void incrementPC() {
		programCounter++;
		return;
	}

	public int[] getMemoryTable() {
		return Arrays.copyOf(memoryTable, memoryTable.length);
	}

	public void setMemoryTable(int[] table) {
		memoryTable = Arrays.copyOf(table, table.length);
	}

	public void deleteProcess() {
		UniqueIdGenerator.releaseId(pid);
		return;
	}

}