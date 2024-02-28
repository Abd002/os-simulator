package models;

import models.Kernel;
import models.process.Process;
import models.process.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ArrayList<String> varNames = new ArrayList<>();
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
                    case "writeFile":
                        varNames.add(words[++i]);
                        break;
                    case "readFile":
                        varNames.add(words[++i]);
                        break;
                    case "printFromTo":
                        //no variables
                        break;
                    case "semWait":
                        varNames.add(words[++i]);
                        break;
                    case "semSignal":
                        varNames.add(words[++i]);
                    default:
                        break;
                }
            }
        }
        String[] variableNames = new String[varNames.size()];
        variableNames = varNames.toArray(variableNames);
        return variableNames;
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
     * readFile: to read data from a file.
     *
     * • printFromTo: to print all numbers between 2 numbers.
     *
     * • semWait: to acquire a resource.
     *
     * • semSignal: to release a resource.
     * @param process
     * @return
     */

    public boolean executeInstruction(Process process){
        boolean done = false;
        String[] instructions = process.getInstructions();
        Variable[] variables = process.getAllVariables();
        for(int i = 0;i < instructions.length;i++){
            String[] words = instructions[i].split(" ");
        }

        //TODO
        return false;
    }

}
