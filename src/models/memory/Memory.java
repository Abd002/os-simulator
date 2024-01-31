package Memory;

public class Memory {
    private final int maxNumberOfWords;
    private final MemoryWord[] memory;
    public Memory(int maxNumberOfWords){
        this.maxNumberOfWords = maxNumberOfWords;
        this.memory = new MemoryWord[maxNumberOfWords];
    }

    public int getMaxNumberOfWords() {
        return maxNumberOfWords;
    }

   public MemoryWord getMemmory(int address){
       return memory[address];
   }
}
