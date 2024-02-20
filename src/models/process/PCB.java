package models.process;

import java.util.Arrays;

public final class PCB {

	private enum ProcessState {
		NEW, READY, WAITING, RUNNING, TERMINATED
	};

	public final int pid;
	private ProcessState state;
	private int programCounter;
	private final int[] memoryTable;

	public PCB(int[] addresses) {
		this.pid = UniqueIdGenerator.generateUniqueId();
		this.state = ProcessState.NEW;
		this.programCounter = 0;
		this.memoryTable = addresses;
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
	
	
	
}