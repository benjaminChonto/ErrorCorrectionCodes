package ecc;

import java.util.ArrayList;

public class ReedSolomon extends Code{
	private int t;
	private int messageLen;
	private int codeLen;
	private int GF;
	private Polynom generator;
	private int prim;
	
	//Constructor
	public ReedSolomon(int err) {
		t = err;
		switch(t){
		case 1:
			GF = 5; messageLen = 2; codeLen = 4; break;
		case 2:
			GF = 7; messageLen = 2; codeLen = 6; break;
		case 3:
			GF = 11; messageLen = 4; codeLen = 10; break;
		default: GF = 5; messageLen = 2; codeLen = 4;
		} 
		//The primitive element in the GF
		int alpha = primitive();
		prim = alpha;
		generator = new Polynom(gen(alpha).getCoef());
	}

	//Algorithm for finding the primitive element
	public int primitive() {
		int alpha;
		for(alpha = 2;  alpha<GF; ++alpha) {
			int[] powers = new int[GF];
			for(int i=1; i<GF; ++i)
				powers[i-1] = ((int)Math.pow(alpha, i))%GF;
			boolean found = true;
			for(int i=0; i<GF-2; ++i) {
				if(powers[i] == 1)
					found = false;
			} if(found == true) break;
		} return alpha;
	}
	public Polynom gen(int alpha) {
		//First we start with the polynomial: 1
		int[] one = {1};
		Polynom gen = new Polynom(one);
		//Multiplying by each factor
		for(int i=1; i<=codeLen-messageLen; ++i) {
			int n = inGF((int)(Math.pow(alpha, i)));
			int[] arr = {(-1)*n,1};
			Polynom factor = new Polynom(arr);
			gen = factor.multiply(gen);
		}
		intoField(gen);
		return gen;
	}
	//Taking the corresponding element for each coefficient in the GF
	public void intoField(Polynom poly) {
		for(int i=0; i<=poly.getDeg(); ++i)
			poly.set(i, inGF(poly.getCoef(i)));
	}
	//Takes modulo GF of given integer
	public int inGF(int n) {
		int result = n%GF;
		if(result<0)
			result += GF;
		return result;
	}
	//Getters
	public int getMessageLen() {
		return messageLen;
	}
	public int getCodeLen() {
		return codeLen;
	}
	public int getGF() { return GF; }

	@Override
	public ArrayList<Integer> encode(ArrayList<Integer> message) {
		Polynom mess = new Polynom(message);
		Polynom code = new Polynom(mess.multiply(generator).getCoef());
		intoField(code);
		return code.getCoef();
	}

	@Override
	public ArrayList<Integer> decode(ArrayList<Integer> recieved) throws InvalidRowException {
		int numberOfRotations = 0; 
		boolean found = false;
		Polynom remainder = new Polynom();
		//Error Trapping Algorithm
		while(found != true) {
			Polynom rec = new Polynom(recieved);
			for(int i=numberOfRotations; i>0; i--)
				rec.shiftRight();
			remainder.clone(rec.divideWithRemainder(generator));
			intoField(remainder); remainder.reduce();
			//n is the weight of the remainder polynomial
			//(the number of non zero elements)
			int n = 0;
			for(int i = 0; i< remainder.getDeg(); ++i) {
				if(remainder.getCoef(i)!=0) ++n;
			}
			if(n<t+1)
				found = true;
			else numberOfRotations++;
			if(numberOfRotations>recieved.size())
				throw new InvalidRowException("The error can't be solved!");
		}
		for(int i=numberOfRotations; i>0; i--) 
			remainder.shiftLeft();

		//Adding the error to the received polynomial
		Polynom code = new Polynom(remainder.add(new Polynom(recieved)).getCoef());
		intoField(code);
		//At last, dividing the code with the generator polynomial to get the message
		Polynom message = new Polynom(code.divide(generator).getCoef());
		intoField(message);
		if(message.getDeg()+1<messageLen) {
			for(int i=message.getDeg()+1; i<messageLen; ++i)
				message.getCoef().add(0);
		}
		return message.getCoef();
	}
	
	public ArrayList<Integer> add(ArrayList<Integer> a, ArrayList<Integer> b) {
		//ArrayList<Integer> result = new ArrayList<>();
		Polynom x = new Polynom(a);
		Polynom y = new Polynom(b);
		ArrayList<Integer> result = x.add(y).getCoef();
		for(int i=0; i<result.size(); ++i)
			result.set(i, result.get(i)%GF);
		return result;
	}
	
	//To see the output during testing
	public String toString() {
		String result = new String();
		result = "The primitive element is: " + prim + "\n" + 
				"The generator is: " + generator;
		
		return result;
	}

}
