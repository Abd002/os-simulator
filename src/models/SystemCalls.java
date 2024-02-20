package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import models.memory.MemoryWord;

public final class SystemCalls {

	private final Kernel kernel;

	public SystemCalls() {
		this.kernel = new Kernel(10);
	}

	public String readFromScreen() {
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		scanner.close(); /* free up system resources */
		return userInput;
	}

	public void writeToScreen(String content) {
		System.out.println(content);
		return;
	}

	public ArrayList<String> readFromDisk(String fileName) { /* just the name without .txt */
		ArrayList<String> stringList = new ArrayList<>();
		String filePath = "resources/files/" + fileName;

		File file = new File(filePath);
		try { /* handle if file doesn't exist */

			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				stringList.add(line);
			}

			scanner.close(); /* free up system resources */
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		return stringList;
	}

	public static void writeToDisk(String fileName, String content) {
		String filePath = "resources/files/" + fileName;
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public MemoryWord readFromMemory(int address) {
		return kernel.memory.getMemmoryWord(address);
	}

	public void writeToMemory(int address, MemoryWord word) {
		kernel.memory.setMemoryWord(address, word);
		return;
	}

}
