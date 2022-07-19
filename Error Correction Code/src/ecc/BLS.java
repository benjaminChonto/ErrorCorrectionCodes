package ecc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import java.util.Iterator;

import java.io.File;
import java.io.FileNotFoundException;

//Class for Binary Linear Systematic codes,
//using matrices and look up tables.

public class BLS extends Code{
	private int t;
	private Matrix G = new Matrix();
	private Matrix H = new Matrix();
	private Matrix codes = new Matrix();
	private HashMap<ArrayList<Integer>,ArrayList<Integer>> SyndromeDecoder = new HashMap<>();

	
	
	//Constructor
	public BLS(int tt) throws FileNotFoundException, InvalidRowException {
		t = tt;
		//Initializinig the G matrix from a file
		Scanner input = new Scanner(new File("Gmx"+t+".txt"));
		while(input.hasNextLine()) {
			String line = input.nextLine();
			ArrayList<Integer> row = new ArrayList<>();
			String[] arr = line.split(" ");
			for(String s: arr) 
				row.add(s.equals("1")? 1 : 0);
			G.add(row);
		}

		//Initializing the H matrix using the G matrix
		//first the identity matrix part
		Matrix I = new Matrix();
		for(int i=0; i<G.getColSize()-G.getRowSize(); ++i) {
			ArrayList<Integer> idRow = new ArrayList<>();
			for(int j=0; j<G.getColSize()-G.getRowSize();++j)
				idRow.add(j==i?1:0);
			I.add(idRow);
		}
		//then the transposition
		for(int j =G.getColSize()-G.getRowSize()-1; j<G.getColSize();++j) {
			ArrayList<Integer> row = new ArrayList<>();
			for(int i=0; i<G.getRowSize(); ++i) {
				row.add(G.get(i,j));
			}
			row.addAll(I.getRow(j-(G.getColSize()-G.getRowSize()-1)));
			H.add(row);
		}
		//Initializing the codewords
		initCodes();
		//Using the codewords, the Syndrome Decoding table can be constructed
		LUT();
		
	}
	public void initCodes() {
		for(int i=0; i<Math.pow(2,G.getRowSize());++i) {
			ArrayList<Integer> codeword = new ArrayList<>();
			codeword = binaryRepr(i,G.getRowSize());
			try {
				codes.add(G.multiplyByMx(codeword));
			} catch (InvalidRowException e) {
				e.printStackTrace();
			}
		}
	}
	//Constructing the syndrome decoding table
	public void LUT() throws InvalidRowException {
		//first collecting all the syndromes in an arraylist
		ArrayList<Integer> syndrome = new ArrayList<>();
		for(int i=0; i<Math.pow(2,H.getRowSize());++i) {
			syndrome = binaryRepr(i,H.getRowSize());
			
			//for each syndrome, we have to find a corresponding error
			ArrayList<Integer> error = new ArrayList<>();
			for(int j = 0; j<Math.pow(2,H.getColSize());++j) {
				error = binaryRepr(j,H.getColSize());
				if(H.multiplyMxbyVector(error).equals(syndrome))
					break;
			}
			ArrayList<Integer> leading = findLeandingError(error);
			SyndromeDecoder.put(syndrome, leading);
		}
	}
//Helping functions for making the LUT
	//Finding the error with the most probability in a group
	public ArrayList<Integer> findLeandingError(ArrayList<Integer> error){
		//Constructing the error group
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		result.add(error);
		for(int i=1; i<codes.getRowSize(); ++i) {
			ArrayList<Integer> temp = new ArrayList<>();
			for(int j=0; j<error.size();++j)
				temp.add((codes.get(i, j)+error.get(j))%2);
			result.add(temp);
		}
		//Then finding the leader (the one with the lowest weight)
		Iterator<ArrayList<Integer>> it = result.iterator();
		int idx = 0; int i=0;
		while(it.hasNext()) {
			if(weight(it.next())<weight(result.get(idx))) {
				idx = i;
			}
		++i;
		} return result.get(idx);
	}
	
	//Calculates the weight of a given vector
	public int weight(ArrayList<Integer> vector) {
		int i=0;
		for(int j=0;j<vector.size();++j) {
			i+=vector.get(j);
		} return i;
	}
	//Returns an array list with the binary representation of a given integer
	public ArrayList<Integer> binaryRepr(int n, int length){
		ArrayList<Integer> result = new ArrayList<>();
		String size = Integer.toString(length);
		String str = String.format("%"+size+"s", Integer.toBinaryString(n)).replaceAll(" ", "0");
		for(int i=0; i<str.length(); ++i)
			result.add(Character.getNumericValue(str.charAt(i)));
		return result;
	}
	//Getters
	public int getMessageLen() {
		return G.getRowSize();
	}
	public int getCodeLen() {
		return G.getColSize();
	}

	@Override
	public ArrayList<Integer> encode(ArrayList<Integer> message) throws InvalidRowException {
		if(message.size() != G.getRowSize())
			throw new InvalidRowException("The size of the vector is invalid!");
		//Matrix multiplication
		return G.multiplyByMx(message);
	}

	@Override
	public ArrayList<Integer> decode(ArrayList<Integer> recieved) throws InvalidRowException {
		if(recieved.size() != H.getColSize())
			throw new InvalidRowException("The size of the vector is invalid!");
		ArrayList<Integer> syndrome = H.multiplyMxbyVector(recieved);
		//Finding the corresponding error vector
		ArrayList<Integer> e = SyndromeDecoder.get(syndrome);
		ArrayList<Integer> codeword = add(e,recieved);
		ArrayList<Integer> message = new ArrayList<>();
		//Cutting the first k bits
		for(int j=0; j<G.getRowSize();++j)
			message.add(codeword.get(j));
		return message;
	}	
}