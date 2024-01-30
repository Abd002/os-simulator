package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Kernel {
	
	private Scheduler scheduler;
	//private Mutex mutex; 						/* not exist yet */
	//private Memory memory; 					/* not exist yet */
	//private Interpreter interpreter; 			/* not exist yet */
	//private MMU mmu; 							/* not exist yet */
	
	public String readFromscreen() {
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		scanner.close();						/* free up system resources */
		return userInput;
	}
	
	public void writeToScreen(String content) {
		System.out.println(content);
		return ;
	}
	
	public String readFromDisk(String fileName) {/* just the name without .txt */
		String content="";
		
		/* TODO : file path -> not handled yet  */
		String filePath ="C:\\Users\\AbdElRahman Khalifa\\git\\os-simulator\\resources\\files\\"+fileName; 
		
		File file =new File(filePath);
		try {										/* handle if file doesn't exist */
		
			Scanner scanner = new Scanner(file);
			
			while(scanner.hasNextLine()) {			/* reading process and concatenate all strings in one line */
				String line = scanner.nextLine();
				content = content+" "+line;
			}
			
			scanner.close();						/* free up system resources */
		}catch (FileNotFoundException e){
			System.out.println("File not found");
		}

		return content;
	}
	
	
	
}
