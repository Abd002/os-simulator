package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import models.process.*;
import models.process.Process;
import models.memory.*;

public class Kernel {
	
	private int clock;
	public final int MAX_CLOCK;
	public Scheduler scheduler;
	public MemoryManagementUnit mmu;
	
	//public Mutex mutex; 							/* not exist yet */
	//public Memory memory; 						/* not exist yet */
	//public Interpreter interpreter; 				/* not exist yet */
	//public final SystemCalls systemCalls;			/* not exist yet */
	//public final Scheduler scheduler 				/* not exist yet */
	
	//TODO ->>>>>
	public Kernel(int maxClock) {
		clock=0;
		MAX_CLOCK=maxClock;
	}
	
	public void incrementClock() {
		return ;
	}
	
	public int getClock() {
		return getClock();
	}
	
	public void printMessage(String msg) {
		
	}
	public void run() {
		
	}
	
	/* TODO waiting interpreter(parse(Ins&variables)) and MMU(allocateMemory) and schedulerAdmitProcess */
	public void createProcess(String programName) {
		
		String program =readFromDisk(programName);
		
		
		return ;
	}
	
	public void saveProcessState(Process process) {
		
	}
	
	public Process restoreProcessState(PCB pcb) {
		return restoreProcessState(pcb);
	}


	

	
	// <<<<<<<<-
		
	public String readFromScreen() {
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		scanner.close();							/* free up system resources */
		return userInput;
	}
	
	public void writeToScreen(String content) {
		System.out.println(content);
		return ;
	}
	
	public String readFromDisk(String fileName) {	/* just the name without .txt */
		String content="";
		
		String filePath ="resources/files/"+fileName; 
		
		File file =new File(filePath);
		try {										/* handle if file doesn't exist */
		
			Scanner scanner = new Scanner(file);
			
			while(scanner.hasNextLine()) {			/* reading process and separate strings by \n */
				String line = scanner.nextLine();
				content = content+"\n"+line;
			}
			
			scanner.close();						/* free up system resources */
		}catch (FileNotFoundException e){
			System.out.println("File not found");
		}

		return content;
	}
	
	public static void writeToDisk(String fileName, String content) {
		String filePath ="resources/files/"+fileName; 
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(content);
			writer.close();
		}catch (IOException e) {
            e.printStackTrace();
        }
		return ;
	}
	
	
	
	
	public Process restoreState(PCB pcb) {
		/*
		 * if (pcb.isLoaded == false)
		 * {
		 * 		return mmu.swapFromDisk(pcb);
		 * }
		 * 
		 * ArrayList<String> instructions = new ArrayList<String>();
		 * Map<string, Variable> variables = new HashMap<>();
		 * 
		 * for (int i = 0; i < pcb.memoryBoundaries.length; i++)
		 * {
		 * 		MemoryWord word = readFromMemory(pcb.memoryBoundaries[i]);
		 * 		if (word.isInstruction)
		 * 		{
		 * 			instructions.add(word.getData())
		 * 		}
		 * 		if (word.isVariable)
		 * 		{
		 * 			variables.put(word.getVariableName, new Variable(word.getVariableName, word.getData, pcb.memoryBoundaries[i]);
		 * 		}
		 * }
		 * 
		 * return new Process(pcb, instructions.ToArray(), variables);
		 */
		
		return new Process();
	}
	
}
