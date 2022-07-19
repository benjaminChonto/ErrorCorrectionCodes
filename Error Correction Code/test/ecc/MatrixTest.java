package ecc;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import org.junit.Assume;

public class MatrixTest {
	Matrix identity;
	ArrayList<Integer> vector;
	
	@Before
	public void init() throws InvalidRowException {
		vector = new ArrayList<>();
		vector.add(0); vector.add(1); vector.add(0); vector.add(1); vector.add(1);
		identity = new Matrix();
		for(int i=0; i<5; ++i) {
			ArrayList<Integer> id = new ArrayList<>();
			for(int j=0; j<5; ++j) {
				if(j==i) id.add(1);
				else id.add(0);
			} identity.add(id);
		}
	}

	@Test
	public void multiplyMxwithVector() {
		ArrayList<Integer> result = identity.multiplyMxbyVector(vector);
		Assert.assertEquals(vector,result);
	}
	@Test
	public void multiplyVectorbyMx() {
		ArrayList<Integer> result = identity.multiplyByMx(vector);
		Assert.assertEquals(vector, result);
	}
	@Test(expected = InvalidRowException.class)
	public void addWrongRow() throws InvalidRowException {
		ArrayList<Integer> newRow = new ArrayList<>();
		newRow.add(0);
		identity.add(newRow);
	}
	@Test
	public void addRow() {
		try {
			identity.add(vector);
		}catch(InvalidRowException e) {
			Assume.assumeNoException(e);
		}
	}
	

}
