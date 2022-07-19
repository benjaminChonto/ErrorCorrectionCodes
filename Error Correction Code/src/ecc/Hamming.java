package ecc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Hamming extends Code{
	private Matrix G = new Matrix();
	private Matrix H = new Matrix();
	
	public Hamming() throws FileNotFoundException, InvalidRowException {
		//Initializinig the H matrix from a file
		Scanner input = new Scanner(new File("Hmx1.txt"));
		while(input.hasNextLine()) {
			String line = input.nextLine();
			ArrayList<Integer> row = new ArrayList<>();
			String[] arr = line.split(" ");
			for(String s: arr) 
				row.add(s.equals("1")? 1 : 0);
			H.add(row);
		}
		//Initializing the G matrix, using the H
		//first the identity matrix
		int kLength = (H.getRowSize()-H.getColSize())*(-1);
		Matrix I = new Matrix();
		for(int i=0; i<kLength;++i) {
			ArrayList<Integer> idrow = new ArrayList<>();
			for(int j=0; j<kLength;++j)
				idrow.add(i==j?1:0);
			I.add(idrow);
		}
		//then the transposition
		for(int n=0; n<kLength; ++n){
			ArrayList<Integer> row = new ArrayList<>();
			row.addAll(I.getRow(n));
			for(int m=0; m<H.getRowSize();++m)
				row.add(H.get(m, n));
			G.add(row);
		}
	}
	//Getters
	public int getMessageLen() {
		return G.getRowSize();
	}
	public int getCodeLen() {
		return G.getColSize();
	}

	@Override
	public ArrayList<Integer> encode(ArrayList<Integer> message)throws InvalidRowException {
		if(message.size() != G.getRowSize())
			throw new InvalidRowException("The size of the vector is invalid!");
		return G.multiplyByMx(message);
	}

	@Override
	public ArrayList<Integer> decode(ArrayList<Integer> recieved) throws InvalidRowException{
		if(recieved.size() != H.getColSize())
			throw new InvalidRowException("The size of the vector is invalid!");
		//Evaluating the syndrome vector
		ArrayList<Integer> syndrome = H.multiplyMxbyVector(recieved);
		//Finding the row which corresponds to the syndrome
		int idx = 0;
		for(int i=0; i<H.getColSize(); ++i) {
			if(H.getCol(i).equals(syndrome))
				idx = i;
		}
		ArrayList<Integer> code = recieved;
		code.set(idx, (recieved.get(idx)+1)%2);
		//Cutting the first k bits
		ArrayList<Integer> message = new ArrayList<>();
		for(int i=0; i<G.getRowSize(); ++i) {
			message.add(code.get(i));
		}
		return message;
	}
}
