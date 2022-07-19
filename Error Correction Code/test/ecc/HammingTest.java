package ecc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HammingTest {
	Hamming h;
	ArrayList<Integer> m;
	ArrayList<Integer> e;
	
	public HammingTest(ArrayList<Integer> a, ArrayList<Integer> b) {
		m = a; e = b;
		}
	@Before
	public void init() throws FileNotFoundException, InvalidRowException {
		h = new Hamming();
	}

	@Test
	public void test() throws InvalidRowException {
		ArrayList<Integer> code = h.encode(m);
		ArrayList<Integer> recieved = h.add(code, e);
		
		Assert.assertEquals(m,h.decode(recieved));
	}
	@Parameters
	public static ArrayList<Object[]> parameters(){
		ArrayList<Object[]> param = new ArrayList<>();
		ArrayList<Integer> m1 = new ArrayList<>(List.of(0,0,0,0));
		ArrayList<Integer> m2 = new ArrayList<>(List.of(0,1,1,0));
		ArrayList<Integer> m3 = new ArrayList<>(List.of(1,1,0,1));
		
		ArrayList<Integer> e1 = new ArrayList<>(List.of(1,0,0,0,0,0,0));
		ArrayList<Integer> e2 = new ArrayList<>(List.of(0,1,0,0,0,0,0));
		ArrayList<Integer> e3 = new ArrayList<>(List.of(0,0,0,0,1,0,0));

		param.add(new Object[] {m1,e1});
		param.add(new Object[] {m2,e2});
		param.add(new Object[] {m3,e3});
		
		
		return param;
	}
		

}
