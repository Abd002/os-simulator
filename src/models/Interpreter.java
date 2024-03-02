package models;

import models.process.Process;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
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
        String[] words = instruction.split(" ");
        String resourceName = "";
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                switch (word) {
                    case "print":
                        resourceName = words[++i];
                        kernel.systemCalls.writeToScreen(resourceName);
                        done = true;
                        break;
                    case "assign":
                        String variableName = words[++i];
                        String value = words[++i];
                        String data = "";
                        if (value.equals("input")) {
                            kernel.systemCalls.writeToScreen("Please enter a value :");
                            try (Scanner scan = new Scanner(System.in)) {
								data = scan.nextLine();
							}
                        }else if(value.equals("readFile")){
                            resourceName = words[++i];
                            String[] lines = kernel.systemCalls.readFromDisk(resourceName);
                            for(String line : lines){
                                data += line;
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
                            try (Scanner scan = new Scanner(System.in)) {
								resourceName = scan.nextLine();
							}
                            dataValue = resourceName.split(" ");
                            kernel.systemCalls.writeToDisk(fileName,dataValue);
                        }else{
                            int size = words.length;
                            String[] lines = new String[size - 2];
                            int index = 0;
                            while(i < words.length){
                                lines[index++] = words[++i];
                            }
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
                        double firstNumber = Double.parseDouble(words[++i]);
                        double secondNumber = Double.parseDouble(words[++i]);
                        for (double counter = firstNumber; counter <= secondNumber; counter++) {
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

