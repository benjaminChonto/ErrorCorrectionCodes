package ecc;

import java.util.ArrayList;

//A Matrix class for the BLS and Hamming codes
public class Matrix {
	//number of rows and columns
	private int row = 0;
	private int col = 0;
	private ArrayList<ArrayList<Integer>> elements = new ArrayList<>();
	
	public void add(ArrayList<Integer> row) throws InvalidRowException {
		if(col == 0)
			col = row.size();
		else if(row.size() != col)
			throw new InvalidRowException
			("The number of elements in the row differs from the number of columns in the matrix");
		elements.add(row); ++this.row;
	}
	//Getters
	//THE NUMBER OF ROWS (not the number of elements in a row)
	public int getRowSize() {
		return row;
	}
	//THE NUMBER OF COLUMNS (not the number of elements in a column)
	public int getColSize() {
		return col;
	}
	public ArrayList<Integer> getRow(int i){
		return elements.get(i);
	}
	public ArrayList<Integer> getCol(int i){
		ArrayList<Integer> result = new ArrayList<>();
		for(int j=0; j<row; ++j) {
			result.add(elements.get(j).get(i));
		} return result;
	}
	public int get(int i, int j) {
		return elements.get(i).get(j);
	}
	//Matrix Multiplication
	public ArrayList<Integer> multiplyByMx(ArrayList<Integer> vec){
		ArrayList<Integer> result = new ArrayList<>();
		for(int i=0; i<col; ++i) {
			int sum = 0;
			for(int j=0; j<row; ++j)
				sum = (sum + elements.get(j).get(i)*vec.get(j))%2;
			result.add(sum);
		} return result;
	}
	
	public ArrayList<Integer> multiplyMxbyVector(ArrayList<Integer> vec){
		ArrayList<Integer> result = new ArrayList<>();
		for(int i=0; i<row; ++i) {
			int sum = 0;
			for(int j=0; j<col; ++j)
				sum = (sum + elements.get(i).get(j)*vec.get(j))%2;
			result.add(sum);
		} return result;
	}

	//For debugging reasons
	public String toString() {
		String S = new String();
		for(int i=0; i<row; ++i)
			S +=elements.get(i) + "\n";
		return S;
	}

}
