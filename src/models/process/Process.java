package models.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Process {

	public final PCB pcb;
	private final String[] instructions;
	private final Map<String, Variable> variables = new HashMap<>(); /* mp.put() & mp.get() */

	public Process(PCB pcb, String[] instructions, String[] varNames, String[] varValues) {
		this.pcb = pcb;
		this.instructions = instructions;
		for (int i = 0; i < varNames.length; i++) {
			variables.put(varNames[i], new Variable("", "", instructions.length + i));
		}
	}

	public String[] getInstructions() {
		return instructions;
	}

	public Variable getVariable(String name) {
		return variables.get(name);
	}

	public void setVariableValue(String name, String value) {
		Variable temp = variables.get(name);
		temp.setValue(value);
		variables.put(name, temp);
		return;
	}

	public Variable[] getAllVariables() {
		ArrayList<Variable> variableList = new ArrayList<>();
		for (String key : variables.keySet()) {
			variableList.add(variables.get(key));
		}
		Variable[] variableArray = new Variable[variableList.size()];
		variableArray = variableList.toArray(variableArray);
		return variableArray;

	}
}
