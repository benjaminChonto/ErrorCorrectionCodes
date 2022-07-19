package ecc;

import java.io.Serializable;

public class CodeData implements Serializable{


	private static final long serialVersionUID = 1L;
	private String name;
	private boolean correct;
	private double time;
	
	//Constructor
	public CodeData(String n, boolean c, double t) {
		name = n;
		correct = c;
		time = t;
	}
	//Getters
	public String getName() { return name; }
	public boolean getCorrect() { return correct; }
	public double getTime() { return time; }
	
	//Setters
	public void setName(String n) { name = n; }
	public void setCorrect(boolean c) { correct = c; }
	public void setTime(double t) { time = t; }

}
