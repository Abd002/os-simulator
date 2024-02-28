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

	public SystemCalls(Kernel kernel) {
		this.kernel = kernel;
	}

	public String[] readFromDisk(String fileName) { /* just the name without .txt */
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

		String[] stringArray = new String[stringList.size()];
		stringArray = stringList.toArray(stringArray);

		return stringArray;
	}

	public void writeToDisk(String fileName, String lines[]) {
		String filePath = "resources/files/" + fileName;
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
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		scanner.close(); /* free up system resources */
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
