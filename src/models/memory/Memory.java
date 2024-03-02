package models.memory;
import models.Kernel;

public final class Memory {
    public final int MAX_SIZE;
    private final Kernel kernel;
    private final MemoryWord[] memory;
    public Memory(Kernel kernel,int maxNumberOfWords){
        this.MAX_SIZE = maxNumberOfWords;
        this.memory = new MemoryWord[maxNumberOfWords];
        this.kernel = kernel;
    }
    public MemoryWord getMemoryWord(int address){
        return memory[address];
    }

    public void setMemoryWord(int address,MemoryWord word){
        this.memory[address] = word;
    }

    public MemoryWord[] getMemory(){
        return this.memory.clone();
    }

}
