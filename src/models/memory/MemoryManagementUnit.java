package models.memory;
import models.Kernel;
import models.process.Process;
import models.process.PCB;
import java.util.Arrays;
import java.io.File;

public final class MemoryManagementUnit {
    private final Kernel kernel;
    private boolean[] isAlLocated;
    private int[] processIDs;

    public  MemoryManagementUnit (Kernel kernel){
        this.kernel = kernel;
        int size = this.kernel.memory.maxNumberOfWords;
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

    public void swapToDisk(Process process){

    }
    public void swapFromDisk(PCB pcb){

    }

}
