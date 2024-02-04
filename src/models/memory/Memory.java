package models.memory;
import models.Kernel;

public final class Memory {
    private final int maxNumberOfWords;
    private final Kernel kernel;
    private  MemoryWord[] memory;
    public Memory(Kernel kernel,int maxNumberOfWords){
        this.maxNumberOfWords = maxNumberOfWords;
        this.memory = new MemoryWord[maxNumberOfWords];
        this.kernel = kernel;
    }

    public int getMaxNumberOfWords() {
        return maxNumberOfWords;
    }

    public MemoryWord getMemmoryWord(int address){
        return memory[address];
    }

    public void setMemoryWord(int address,MemoryWord word){
        this.memory[address] = word;
    }

}
