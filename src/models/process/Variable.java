package models.process;

public final class Variable {
	public final String name;
	private String value;
	public final int address;
	
	public Variable(String name,String value,int address) {
		this.name=name;
		this.value=value;
		this.address=address;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value=value;
	}
	
}
