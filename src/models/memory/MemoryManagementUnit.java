package models.memory;
import models.Kernel;
import models.process.Process;
import models.process.PCB;
import models.SystemCalls;
import models.process.Variable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

public final class MemoryManagementUnit {
    private final Kernel kernel;
    private boolean[] isAlLocated;
    private int[] processIDs;

    public  MemoryManagementUnit (Kernel kernel){
        this.kernel = kernel;
        int size = this.kernel.memory.MAX_SIZE;
        isAlLocated = new boolean[size];
        processIDs = new int[size];
        for(int i = 0;i < size;i++){
            isAlLocated[i] = false;
            processIDs[i] = -1;
        }

    }

    public int[] allocateMemory(int size,int pid){
        int[] result = new int[size];
        int index = 0;
        for(int i = 0;i < isAlLocated.length;i++){
            if(!isAlLocated[i]){
                result[index++] = i;
                isAlLocated[i] = true;
                processIDs[i] = pid;
            }
            if(size <= index){
                break;
            }
        }
        if(size > index){
            Arrays.fill(result, -1);
        }
        return result;
    }

    public void deallocateMemory(int [] addresses){
        for (int address : addresses) {
            isAlLocated[address] = false;
            processIDs[address] = -1;
        }
    }

    /**
     * This method is to swap process from memory to disk in resources files
     * @param process that we need to swap.
     */
    public void swapToDisk(Process process){
        String[] instructions  = process.getInstructions();
        Variable[] variables = process.getAllVariables();
        ArrayList<String> namesOfVariables = new ArrayList<>();
        ArrayList<String> valuesOfVariables = new ArrayList<>();
        for(Variable var: variables){
            namesOfVariables.add(var.name);
            valuesOfVariables.add(var.getValue());
        }
        String[] varNames = new String[namesOfVariables.size()];
        varNames = namesOfVariables.toArray(varNames);
        String[] values = new String[valuesOfVariables.size()];
        values = valuesOfVariables.toArray(values);
        //Content
        String[] programLines = new String[instructions.length + varNames.length + values.length + 3];
        //save all instruction first.
        int index = 0;
        programLines[index++] = "Instructions";
        for(int j = 0;j < instructions.length;j++){
            programLines[index++] = instructions[j];
        }
        //save all variable's name.
        programLines[index++] = "Variables";
        for(int j = 0;j < varNames.length;j++){
            programLines[index++] = varNames[j];
        }
        //save all variable's values.
        programLines[index++] = "Values";
        for(int j = 0;j < values.length;j++){
            programLines[index++] = values[j];
        }

        //Write to file
        kernel.systemCalls.writeToDisk(String.valueOf(process.pcb.pid),programLines);
        //Check the memory table has -1 addresses.
        int [] memoryTable = process.pcb.getMemoryTable();
        for(int i = 0;i < memoryTable.length;i++){
            if(memoryTable[i] != -1){
                deallocateMemory(memoryTable);
                break;
            }
        }
    }

    /**
     * this method allocate memory for process that already exist in disk.
     * if there isn't enough memory will free some locations by swap other process to disk.
     * in this method I free first process in memory.
     * @param pcb for the process
     */
    public void swapFromDisk(PCB pcb){
        String processNameID = String.valueOf(pcb.pid);
        String[] programLines = kernel.systemCalls.readFromDisk(processNameID);
        int sizeNeeded = pcb.getMemoryTable().length;
        int[] addresses = allocateMemory(sizeNeeded,pcb.pid);
        int[] memoryTable = pcb.getMemoryTable();
        //check if addresses -1
        if(addresses[0] == -1){
            //we need to free memory.
            HashMap<Integer,Integer> numberOfProcess = new HashMap<Integer, Integer>(); // PIDs -> counter
            for(int i = 0;i < processIDs.length;i++){
                numberOfProcess.put(processIDs[i], 0);
            }
            for(int i = 0;i < processIDs.length;i++){
                if(processIDs[i] != -1)
                {
                    int counter = numberOfProcess.get(processIDs[i]);
                    counter++;
                    numberOfProcess.put(processIDs[i],counter);
                }

            }
            //sort the map according to value
            //using list contains all values then sort it.
            //sort from low value -> high value.
            ArrayList<Integer> list = new ArrayList<>(numberOfProcess.values());
            Collections.sort(list, (o1, o2) -> o1.compareTo(o2));
            //copy sorted values to new sorted hashmap.
            HashMap<Integer,Integer> sortedMap = new LinkedHashMap<Integer,Integer>(); // (sorted) [PIDs -> numberOfLocations].
            for(int i : numberOfProcess.keySet()){
                for(int j : list){
                    if(numberOfProcess.get(i) == j){
                        sortedMap.put(i,j);
                    }
                }
            }
            //now we need to choose addresses we need to free.
            //I choose first fit algorithm.
            int counter = 0;
            ArrayList<Integer> freeProcesses = new ArrayList<Integer>(); // PIDs of process to deallocate.
            for(int i : sortedMap.keySet()){
                if(sortedMap.get(i) < counter){
                    counter += sortedMap.get(i);
                    freeProcesses.add(i);
                }else{
                    break;
                }
            }
            //free this processes.
            Map<Integer,ArrayList<Integer>> processLocations = new HashMap<Integer,ArrayList<Integer>>();
            if(counter >= sizeNeeded){
                for(int i = 0;i < freeProcesses.size();i++){
                    int size = sortedMap.get(freeProcesses.get(i)); //number of used location in memory.
                    ArrayList<Integer> locations = new ArrayList<>();
                    //get locations of process of (PID).
                    for(int j = 0;j < processIDs.length;j++){
                        if(processIDs[j] == freeProcesses.get(i)){
                            locations.add(j);
                            if(locations.size() == size){
                                break;
                            }
                        }
                        //save the locations of process with pid.
                        processLocations.put(freeProcesses.get(i),locations);
                    }
                    //swap this process to disk and deallocate from memory.
                   swapProcess(freeProcesses.get(i),locations);
                }
            }
            //after free some processes, we become ready to swap this pcb from disk
            addresses = allocateMemory(sizeNeeded, pcb.pid);
        }
        //swap the process from memory.
        ArrayList<String> instuctionList = new ArrayList<>();
        ArrayList<String> varNamesList = new ArrayList<>();
        ArrayList<String> valuesList = new ArrayList<>();
        //copy from disk
        int index = 0;
        for(int i = 0;i < programLines.length;i++){
            if(programLines[i] == "Instructions"){
                while(programLines[i+1] != "Variables"){
                    instuctionList.add(programLines[++i]);
                }
            }
            if(programLines[i] == "Variables"){
                while(programLines[i+1] != "Values"){
                    varNamesList.add(programLines[++i]);
                }
            }
            if(programLines[i] == "Values"){
                while(i < programLines.length){
                    valuesList.add(programLines[++i]);
                }
            }
        }
        //write to memory
        //instructions
        int counter = 0;
        for (int i = 0;i < instuctionList.size();i++){
            MemoryWord memoryWord = new MemoryWord(instuctionList.get(i));
            kernel.systemCalls.writeToMemory(addresses[counter++],memoryWord);
        }
        //variables
        //set all values of all variables from last address to the start.
        for(int i = 0;i < varNamesList.size();i++){
            MemoryWord memoryWord  = new MemoryWord(valuesList.get(i), varNamesList.get(i));
            kernel.systemCalls.writeToMemory(addresses[counter++],memoryWord);
        }
    }

    /**
     * this method to swap proccess from memory to disk using ID and locations only.
     * @param pid ID of process.
     * @param locations locations of memory words.
     */
    private void swapProcess(int pid,ArrayList<Integer> locations){
        String name = String.valueOf(pid);
        Integer [] locationsArray = new Integer[locations.size()];
        locationsArray = locations.toArray(locationsArray);
        //save the instructions and the variables with their values.
        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> varName = new ArrayList<>();
        ArrayList<String> instruction = new ArrayList<>();

        for(int i = 0;i < locations.size();i++){
            MemoryWord memoryWord = kernel.memory.getMemoryWord(locationsArray[i]);
            if(memoryWord.isVariable()){
                String tempVarName = memoryWord.getVariableName();
                String tempVarData = memoryWord.getData();
                data.add(tempVarData);
                varName.add(tempVarName);
            }else {
                String tempInstruction = memoryWord.getData();
                instruction.add(tempInstruction);
            }
        }
        //save in files.
        String[] lines = new String[instruction.size() + data.size() + varName.size() + 3];
        int index = 0;
        lines[index++] = "Instructions";
        for(String line : instruction){
            lines[index++] = line;
        }
        //save all variable's name.
        lines[index++] = "Variables";
        for(String line : varName){
            lines[index++] = line;
        }
        //save all variable's values.
        lines[index++] = "Values";
        for(String line : data){
            lines[index++] = line;
        }

        //Write to file
        kernel.systemCalls.writeToDisk(name,lines);
    }
    public int[] getProcessIDs()
    {
        return this.processIDs.clone();
    }

}
