package ecc;

import java.util.ArrayList;

public abstract class Code {
	
	public abstract ArrayList<Integer> encode(ArrayList<Integer> message) throws InvalidRowException;
	public abstract ArrayList<Integer> decode(ArrayList<Integer> recieved) throws InvalidRowException;
	public abstract int getMessageLen();
	public abstract int getCodeLen();
	
	
	//Function for adding the vectors
	public ArrayList<Integer> add(ArrayList<Integer> first, ArrayList<Integer> sec){
		ArrayList<Integer> result = new ArrayList<>();
		for(int i=0; i<first.size(); ++i) {
			result.add((first.get(i)+sec.get(i))%2);
		} return result;
	}	
}
