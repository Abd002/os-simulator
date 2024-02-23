package models.memory;
public final class MemoryWord {
    /* Class Attributes:*/
    private  String data;
    private String variableName;
    private boolean isInstruction;
    private boolean isVariable;

    //constructor
    public MemoryWord(){
        data = "";
        variableName = "";
        isInstruction = false;
        isVariable = false;
    }
    public MemoryWord(String instruction){
        this.data = instruction;
        this.variableName = "";
        setInstruction(true);
    }

    public MemoryWord(String data,String variableName){
        this.data = data;
        this.variableName = variableName;
        setVariable(true);
    }

    /* Getters*/
    public String getData(){
        return  data;
    }

    public String getVariableName(){
        return variableName;
    }

    public boolean isInstruction() {
        return isInstruction;
    }

    public boolean isVariable() {
        return isVariable;
    }

    /*Setters*/
    public void setData(String data){
        this.data = data;
    }
    public void setVariableName(String  variableName){
        this.variableName = variableName;
    }
    public void setInstruction(boolean isInstruction){
        this.isInstruction = isInstruction;
        isVariable = !isInstruction;
    }

    public void setVariable(boolean isVariable){
        this.isVariable = isVariable;
        isInstruction = !isVariable;
    }

    public void setWord(MemoryWord word){
        this.data = word.data;
        this.variableName = word.variableName;
        this.isVariable = word.isVariable;
        this.isInstruction = word.isInstruction;
    }

}
