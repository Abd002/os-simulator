package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import models.memory.MemoryWord;

public final class SystemCalls {

	private final Kernel kernel;
	public final Scanner scanner;
	
	public SystemCalls(Kernel kernel) {
		this.kernel = kernel;
		scanner = new Scanner(System.in);
	}

	public String[] readFromDisk(String fileName) { /* just the name without .txt */
		ArrayList<String> stringList = new ArrayList<>();
		String filePath = "resources/files/" + fileName + ".txt";

		File file = new File(filePath);
        
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
	        String line;
	        try {
				while ((line = br.readLine()) != null)
					stringList.add(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return stringList.toArray(new String[0]);
	}

	public void writeToDisk(String fileName, String lines[]) {
		String filePath = "resources/files/" + fileName + ".txt";
		try {
			FileWriter writer = new FileWriter(filePath);
			for (int i = 0; i < lines.length; i++) {
				writer.write(lines[i]);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public String readFromScreen() {
		String userInput = scanner.nextLine();
		return userInput;
	}

	public void writeToScreen(String content) {
		System.out.println(content);
		return;
	}

	public MemoryWord readFromMemory(int address) {
		return kernel.memory.getMemoryWord(address);
	}

	public void writeToMemory(int address, MemoryWord word) {
		kernel.memory.setMemoryWord(address, word);
		return;
	}

}
