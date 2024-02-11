package models.process;

import java.util.HashMap;
import java.util.Map;

public class Process {
	
	public  PCB pcb;
	private  String[] instructions;
	private  Map<String, Integer> variables = new HashMap<>(); 	/* mp.put() & mp.get() */


	public String[] getInstructions() {
		return instructions;
	}
}
