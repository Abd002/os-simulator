package Memory;
public class MemoryWord {
    /* Class Attributes:*/
    private  String data;
    private String variableName;
    private boolean isInstruction;
    private boolean isVariable;

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
}

public void setVariable(boolean isVariable){
        this.isVariable = isVariable;
}


}
