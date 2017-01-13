package es.me.parser.javaparser.utils;

/**
 * @author igayolfernan
 *
 * Defines the starting substring for getter and setter methods
 */
public enum GetSet {
	
	GET("get"), SET("set"), IS("is");
	
	private String value;
	
	private GetSet(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
