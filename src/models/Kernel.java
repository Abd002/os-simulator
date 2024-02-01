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
	
	private Scheduler scheduler;
	//private Mutex mutex; 							/* not exist yet */
	//private Memory memory; 						/* not exist yet */
	//private Interpreter interpreter; 				/* not exist yet */
	private MemoryManagementUnit mmu;
		
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
	
	
	/* TODO waiting interpreter(parse(Ins&variables)) and MMU(allocateMemory) and schedulerAdmitProcess */
	public void createProcess(String programName) {
		
		String program =readFromDisk(programName);
		
		
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
