package models;

import models.process.Process;
import java.util.HashSet;
import java.util.Set;

public final class Interpreter {
    private final Kernel kernel;

    public Interpreter(Kernel kernel){
        this.kernel = kernel;
    }

    /**
     * this method to return all variables names from program lines.
     * @param programLines
     * @return all variables name
     */
    public String[] parseVariables(String[] programLines){
    	Set<String> varNames = new HashSet<String> ();
        for(int index = 0;index < programLines.length;index++){
            String programLine = programLines[index];
            String[] words = programLine.split(" ");
            for(int i = 0;i < words.length;i++){
                String word = words[i];
                switch(word){
                    case "print":
                        varNames.add(words[++i]);
                        break;
                    case "assign":
                        varNames.add(words[++i]);
                        break;
                    case "printFromTo":
                        varNames.add(words[++i]);
                        varNames.add(words[++i]);
                        break;
                    default:
                        break;
                }
            }
        }

        return varNames.toArray(new String[varNames.size()]);
    }

    /**
     * execute the instruction
     * -------------------------------------------------------------
     * • print: to print the output on the screen. Example: print x.
     *
     * • assign: to initialize a new variable and assign a value to it.
     *
     * • writeFile: to write data to a file.
     *
     * •w readFile: to read data from a file.
     *
     * • printFromTo: to print all numbers between 2 numbers.
     *
     * • semWait: to acquire a resource.
     *
     * • semSignal: to release a resource.
     * @param process
     * @return
     */

    public boolean executeInstruction(Process process) {
        boolean done = false;
        String[] instructions = process.getInstructions();
        String instruction = instructions[process.pcb.getPC()];
        kernel.systemCalls.writeToScreen("INTERPRETER :: Executing instruction <" + instruction + "> from process #" + process.pcb.pid);
        String[] words = instruction.split(" ");
        String resourceName = "";
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                switch (word) {
                    case "print":
                        resourceName = words[++i];
                        kernel.systemCalls.writeToScreen(process.getVariable(resourceName).getValue());
                        done = true;
                        break;
                    case "assign":
                        String variableName = words[++i];
                        String value = words[++i];
                        String data = "";
                        if (value.equals("input")) {
                            kernel.systemCalls.writeToScreen("Please enter a value :");
                            data = kernel.systemCalls.readFromScreen();
                        }else if(value.equals("readFile")){
                            resourceName = words[++i];
                            String[] lines = kernel.systemCalls.readFromDisk(resourceName);
                            for(String line : lines){
                                data += line + "\n";
                            }
                        }
                        else{
                            data = value;
                        }
                        process.setVariableValue(variableName, data);
                        done = true;
                        break;
                    case "writeFile":
                        String fileName = words[++i];
                        String varName = words[++i];
                        String[] dataValue;
                        if(varName.equals("input")){
                            kernel.systemCalls.writeToScreen("Please enter a value :");
							resourceName = kernel.systemCalls.readFromScreen();
                            dataValue = resourceName.split(" ");
                            kernel.systemCalls.writeToDisk(fileName,dataValue);
                        }else{
                            String[] lines = process.getVariable(varName).getValue().split("\n");
                            kernel.systemCalls.writeToDisk(fileName, lines);
                        }
                        done = true;
                        break;
                    case "readFile":
                        String[] content = kernel.systemCalls.readFromDisk(words[++i]);
                        for (String line : content) {
                            kernel.systemCalls.writeToScreen(line);
                        }
                        done = true;
                        break;
                    case "printFromTo":
                        int firstNumber = Integer.parseInt(process.getVariable(words[++i]).getValue());
                        int secondNumber = Integer.parseInt(process.getVariable(words[++i]).getValue());
                        for (int counter = firstNumber; counter <= secondNumber; counter++) {
                            kernel.systemCalls.writeToScreen(String.valueOf(counter));
                        }
                        done = true;
                        break;
                    case "semWait":
                        resourceName = words[++i];
                        done = kernel.mutex.semWait(resourceName);
                        break;
                    case "semSignal":
                        kernel.mutex.semSignal(words[++i]);
                        done = true;
                        break;
                    default:
                        //input's error
                        break;
                }
            }
            //increment PC
            if(done){
                process.pcb.incrementPC();
            }else{
                //block the process
                kernel.scheduler.blockProcess(process, resourceName);
            }
            return done;
    }
}

