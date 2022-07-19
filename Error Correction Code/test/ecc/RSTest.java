package ecc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
@RunWith(Parameterized.class)
public class RSTest {
	ReedSolomon rs;
	ArrayList<Integer> m;
	ArrayList<Integer> e;
	int t;
	
	public RSTest(ArrayList<Integer> a, ArrayList<Integer> b,
						int t) {
		m = a; e = b;
		rs = new ReedSolomon(t);
		}

	@Test
	public void test() throws InvalidRowException {
		ArrayList<Integer> code = rs.encode(m);
		ArrayList<Integer> recieved = rs.add(code, e);
		Assert.assertEquals(m,rs.decode(recieved));
	}
	
	@Parameters
	public static ArrayList<Object[]> parameters(){
		ArrayList<Object[]> param = new ArrayList<>();
		
		ArrayList<Integer> rs1m1 = new ArrayList<>(List.of(3,4));
		ArrayList<Integer> rs1m2 = new ArrayList<>(List.of(0,2));
		ArrayList<Integer> rs1e1 = new ArrayList<>(List.of(0,4,0,0));
		ArrayList<Integer> rs1e2 = new ArrayList<>(List.of(2,0,0,0));
		
		ArrayList<Integer> rs2m1 = new ArrayList<>(List.of(5,4));
		ArrayList<Integer> rs2m2 = new ArrayList<>(List.of(3,6));
		ArrayList<Integer> rs2e1 = new ArrayList<>(List.of(1,0,0,5,0,0));
		ArrayList<Integer> rs2e2 = new ArrayList<>(List.of(0,4,4,0,0,0));
		
		ArrayList<Integer> rs3m1 = new ArrayList<>(List.of(1,2,3,8));
		ArrayList<Integer> rs3m2 = new ArrayList<>(List.of(6,3,7,4));
		ArrayList<Integer> rs3e1 = new ArrayList<>(List.of(5,0,9,0,0,3,0,0,0,0));
		ArrayList<Integer> rs3e2 = new ArrayList<>(List.of(0,8,8,0,2,0,0,0,0,0));

		param.add(new Object[] {rs1m1,rs1e1,1});
		param.add(new Object[] {rs1m2,rs1e2,1});
		param.add(new Object[] {rs2m1,rs2e1,2});
		param.add(new Object[] {rs2m2,rs2e2,2});
		param.add(new Object[] {rs3m1,rs3e1,3});
		param.add(new Object[] {rs3m2,rs3e2,3});
		
		
		
		return param;
	}
		

}
