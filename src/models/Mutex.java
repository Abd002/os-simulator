package models;

import java.util.ArrayList;
import java.util.HashMap;

public final class Mutex {
	
	private final Kernel kernel;
	private int inputSem;
	private int outputSem;
	private final HashMap<String, Integer> fileSem;
	
	public Mutex(Kernel kernel) {
		this.kernel = kernel;
		inputSem = 1;
		outputSem = 1;
		fileSem = new HashMap<>();
	}
	
	public boolean semWait(String resource) {
		switch (resource) {
		case "input":
			if (inputSem <= 0)
				return false;
			inputSem--;
			break;
		case "output":
			if (outputSem <= 0)
				return false;
			outputSem--;
			break;
		default:
			if (!fileSem.containsKey(resource))
				fileSem.put(resource, 1);
			if (fileSem.get(resource) <= 0)
				return false;
			fileSem.put(resource, fileSem.get(resource) - 1);
		}
		
		return true;
	}
	
	public void semSignal(String resource) {
		switch (resource) {
		case "input":
			inputSem++;
			break;
		case "output":
			outputSem++;
			break;
		default:
			if (!fileSem.containsKey(resource))
				return; // TODO: Throw an error
			fileSem.put(resource, fileSem.get(resource) + 1);
		}
		
		kernel.scheduler.unblockProcess(resource);
	}
	
	public boolean isInputMutexFree() {
		return inputSem == 1;
	}
	
	public boolean inOutputMutexFree() {
		return outputSem == 1;
	}
	
	public String[] getMutexLockedFiles() {
		ArrayList<String> files = new ArrayList<>();
		fileSem.forEach((k, v) -> {
			if (v == 0) files.add(k);
		});
		return files.toArray(new String[0]);
	}
	
}
