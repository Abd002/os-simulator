package models.process;

import java.util.Arrays;

public final class PCB {


	public final int pid;
	private ProcessState state;
	private int programCounter;
	private int[] memoryTable;

	public PCB() {
		this.pid = UniqueIdGenerator.generateUniqueId();
		this.state = ProcessState.NEW;
		this.programCounter = 0;
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

	public void setMemTable(int[] memTable) {
		for (int i = 0; i < memTable.length; i++) {
			this.memoryTable[i] = memTable[i];
		}
	}
	
	public void deleteProcess() {
		UniqueIdGenerator.releaseId(pid);
		return ;
	}
	
	public void initializeMemTable(int[] table) {
		memoryTable=Arrays.copyOf(table, table.length);
	}
	
	
	
}