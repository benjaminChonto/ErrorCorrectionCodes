package ecc;

import java.util.ArrayList;
import java.util.Collections;

public class Polynom {
	
	private int deg = 0;
	private ArrayList<Integer> coefs;
	
	//Constructors
	public Polynom() {
		deg = 0;
		coefs = new ArrayList<>();
	}
	public Polynom(ArrayList<Integer> arr){
		coefs = new ArrayList<>(arr);
		deg = arr.size()-1;
	}
	public Polynom(int[] arr) {
		ArrayList<Integer> l = new ArrayList<>();
		for(int i=0; i<arr.length; ++i)
			l.add(arr[i]);
		coefs = new ArrayList<>(l);
		deg = coefs.size()-1;
	}
	//Getters
	public int getDeg() { return deg; }
	public ArrayList<Integer> getCoef() { return coefs; }
	public int getCoef(int idx) { return coefs.get(idx); }
	//Setter
	public void set(int idx, int n) {
		coefs.set(idx,n);
	}
	public void clone(Polynom p) {
		coefs.clear();
		deg = p.getDeg();
		for(int i=0;i <= p.getDeg(); ++i)
			coefs.add(p.getCoef(i));
	}
	
	//Left and right shift
	public Polynom shiftLeft() {
		int temp = coefs.get(0);
		for(int i=0; i<deg; ++i) {
			coefs.set(i, coefs.get(i+1));
		} coefs.set(deg, temp);
		return this;
	}
	public Polynom shiftRight() {
		int temp = coefs.get(deg);
		for(int i=deg; i>0; --i) {
			coefs.set(i, coefs.get(i-1));
		} coefs.set(0, temp);
		return this;
	}
	//Polinomial substraction
	public Polynom substract(Polynom b) {
		ArrayList<Integer> sub = this.coefs;
		for(int i=0; i<=this.deg; ++i) {
			if(b.getDeg()<i) break;
			sub.set(i,coefs.get(i)-b.getCoef(i));
		}
		if(b.getDeg()>deg) {
			for(int i=deg+1; i<=b.getDeg();++i)
				sub.add(b.getCoef(i)*(-1));
		}
		Polynom result = new Polynom(sub);
		return result;
	}
	//Polynomial addition
	public Polynom add(Polynom b) {
		ArrayList<Integer> res = this.coefs;
		for(int i=0; i<=this.deg; ++i) {
			if(b.getDeg()<i) break;
			res.set(i,coefs.get(i)+b.getCoef(i));
		}
		if(b.getDeg()>deg) {
			for(int i=deg+1; i<=b.getDeg();++i)
				res.add(b.getCoef(i));
		}
		Polynom result = new Polynom(res);
		return result;
	}
	
	//Polynomial multiplication
	public Polynom multiply(Polynom b) {
		ArrayList<Integer> c = new ArrayList<>();
		int maxdeg = this.deg+b.getDeg();
		for(int i=0; i<=maxdeg; ++i)
			c.add(0);
		for(int i=0; i<=this.deg; ++i) {
			for(int j=0; j<=b.getDeg(); ++j)
				c.set(i+j, c.get(i+j)+(this.getCoef(i)*b.getCoef(j)));
		}
		Polynom result = new Polynom(c);
		result.reduce();
		return result;
	}
	//Polynomial division
	public Polynom[] division(Polynom b) {
		//Checking if the division is possible
		boolean notnull = false;
		for(int i=0; i<=b.getDeg();++i) {
			if(b.getCoef(i) != 0)
				notnull = true;
		} if(notnull == false) {throw new ArithmeticException("Divison by zero");}
		Polynom quotient = new Polynom(new ArrayList<Integer>
							(Collections.nCopies(Integer.max(deg, b.getDeg()), 0)));
		//This was cut out
		//Polynom remainder = new Polynom(new ArrayList<Integer>(Collections.nCopies(1, 0)));

		Polynom remainder = new Polynom(this.getCoef());
		reduce(); b.reduce();
		Polynom divident = new Polynom(this.getCoef());
		Polynom divisor = new Polynom(b.getCoef());
		int n=0; int m=0;
		while(divident.getDeg()>=divisor.getDeg()) {
			n = divident.getDeg(); m = divisor.getDeg();
			Polynom leading = new Polynom(new ArrayList<Integer>
							(Collections.nCopies(Integer.max(deg, b.getDeg()), 0)));
			leading.set(n-m, divident.getCoef(n)/divisor.getCoef(m));
			quotient.set(n-m, divident.getCoef(n)/divisor.getCoef(m));
			remainder.clone(divident.substract(divisor.multiply(leading)));
			remainder.reduce();
			divident.clone(remainder);
		}
		quotient.reduce();
		return new Polynom[] {quotient, remainder};
	}
	//Quotient of polynomial division
	public Polynom divide(Polynom b) {
		Polynom[] res = this.division(b);
		return res[0];
	}
	//Remainder of polynomial division
	public Polynom divideWithRemainder(Polynom b) {
		Polynom[] res = this.division(b);
		return res[1];
	}
	
	//Removes the highest zero coefficients
	public void reduce() {
		while(coefs.get(deg) == 0) {
			if(deg == 0) return;
			coefs.remove(deg); --deg;
		}
	}

	
	public String toString(){
		String result = new String();
		for(int i=0;i<=deg; ++i) {
			if(i==0)
				result += coefs.get(i) + " ";
			else if(i==1)
				result += "+ " + coefs.get(i) + "x ";
			else if (i>0)
				result += "+ " +coefs.get(i) + "x^" + i + " ";
		}
		return result;
	}
}
