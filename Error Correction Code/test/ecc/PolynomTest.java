package ecc;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PolynomTest {
	
	Polynom p1;
	Polynom p2;
	Polynom p3;
	
	@Before
	public void init() {
		ArrayList<Integer> arr1 = new ArrayList<>();
		ArrayList<Integer> arr2 = new ArrayList<>();
		ArrayList<Integer> arr3 = new ArrayList<>();
		arr1.add(-1); arr1.add(0); arr1.add(1); //x^2-1
		arr2.add(1); arr2.add(1); 				//x+1
		arr3.add(-1); arr3.add(1); 				//x-1
		p1 = new Polynom(arr1);
		p2 = new Polynom(arr2);
		p3 = new Polynom(arr3);
	}

	@Test
	public void addition() {
		ArrayList<Integer> res1 = new ArrayList<>();
		res1.add(0); res1.add(2);
		ArrayList<Integer> res2 = new ArrayList<>();
		res2.add(-2); res2.add(1); res2.add(1);
		
		Assert.assertEquals(res1,p2.add(p3).getCoef());
		Assert.assertEquals(res2, p3.add(p1).getCoef());
	}
	@Test
	public void substraction(){
		ArrayList<Integer> res1 = new ArrayList<>();
		ArrayList<Integer> res2 = new ArrayList<>();
		res1.add(2); res1.add(0);
		res2.add(0); res2.add(-1); res2.add(1);
		
		Assert.assertEquals(res1, p2.substract(p3).getCoef());
		Assert.assertEquals(res2, p1.substract(p3).getCoef());
	}
	@Test
	public void multiplication() {
		Assert.assertEquals(p1.getCoef(), p2.multiply(p3).getCoef());
	}
	@Test
	public void division() {
		Assert.assertEquals(p2.getCoef(), p1.divide(p3).getCoef());
	}
	@Test
	public void divisionWithRemainder() {
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(0); arr.add(0); arr.add(1);
		Polynom ex = new Polynom(arr);
		ArrayList<Integer> res = new ArrayList<>();
		res.add(-1);
		
		Assert.assertEquals(p2.getCoef(), p2.divideWithRemainder(p1).getCoef());
		Assert.assertEquals(res, p1.divideWithRemainder(ex).getCoef());
	}
	@Test
	public void shiftRTest() {
		ArrayList<Integer> res = new ArrayList<>();
		res.add(1); res.add(-1); res.add(0);
		Assert.assertEquals(res, p1.shiftRight().getCoef());
	}
	@Test
	public void ShiftLTest() {
		ArrayList<Integer> res = new ArrayList<>();
		res.add(0); res.add(1); res.add(-1);
		Assert.assertEquals(res, p1.shiftLeft().getCoef());
	}

}
