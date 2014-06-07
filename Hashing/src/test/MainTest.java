package test;
import implementation.*;

public class MainTest {

	
	public static void main(String[] args) {
		 System.out.println("test 1 = "+test1());
		 System.out.println("test 2 = "+test2());
		 System.out.println("test 3 = "+test3());
		 System.out.println("test 4 = "+test4());
		 System.out.println("test 5 = "+test5());
		 System.out.println("test 6 = "+test6());
	}

	

	//on seperate chaining.
	private static boolean test1() {
		SeparateChaining<Integer, String> map=new SeparateChaining<Integer,String>();
		
		for (int i = 0; i < 100000; i++) {
			map.put(i, "as");
		}
		map.put(0, "one");
		map.put(1, "two");
		
		map.put(3, "three");
		
		//add duplicate then test it.
		map.put(3, "Three3");
		
		if (!map.get(3).equals("Three3"))
			return false;
		
		map.put(11, "eleven");
		//delete 11 then try to find it
		map.delete(11);
		if(map.contains(11))
			return false;
			
		System.out.println("Size"+map.size());
		System.out.println("coll"+map.getCollisions());
		return true;
	}
	
	private static boolean test2() {
		Bucketing<Integer, String> map=new Bucketing<Integer,String>();
		
		for (int i = 0; i < 100000; i++) {
			map.put(i, "as");
		}
		
		map.put(0, "one");
		map.put(1, "two");
		

		//delete 11 then try to find it
		map.delete(11);
		map.delete(0);
		map.delete(1);
		map.delete(3);
		if(map.contains(11))
			return false;
		System.out.println("Size"+map.size());
		System.out.println("coll"+map.getCollisions());
		return true;
	}
	private static boolean test3() {
		LinearProbing<Integer, String> map=new LinearProbing<Integer,String>();
		for (int i = 0; i < 100000; i++) {
			map.put(i, "as");
		}
		map.put(0, "one");
		map.put(1, "two");
		
		map.put(-3, "three");
		
		//add duplicate then test it.
		map.put(-3, "Three3");
		
		if (!map.get(-3).equals("Three3"))
			return false;
		
		map.put(11, "eleven");
		//delete 11 then try to find it
		map.delete(11);
		map.delete(0);
		map.delete(1);
		map.delete(3);
		if(map.contains(11))
			return false;
		System.out.println("Size"+map.size());
		System.out.println("coll"+map.getCollisions());
		return true;
	}
	private static boolean test4() {
		PseudoRandomProbing<Integer, String> map=new PseudoRandomProbing<Integer,String>();
		for (int i = 0; i < 100000; i++) {
			map.put(i, "as");
		}
		map.put(0, "one");
		map.put(1, "two");
		
		map.put(3, "three");
		
		//add duplicate then test it.
		map.put(3, "Three3");
		
		if (!map.get(3).equals("Three3"))
			return false;
		
		map.put(11, "eleven");
		//delete 11 then try to find it
		map.delete(11);
		if(map.contains(11))
			return false;
			
		System.out.println("Size"+map.size());
		System.out.println("coll"+map.getCollisions());
		return true;
	}
	
	private static boolean test5() {
		DoubleHashing<Integer, String> map=new DoubleHashing<Integer,String>();
		for (int i = 0; i < 100000; i++) {
			map.put(i, "as");
		}
		map.put(12, "one");
		
		
	
		map.put(1, "two");
		
		
		map.put(11, "three");
		
		
		//add duplicate then test it.
		map.put(11, "Three3");
		
		if (!map.get(11).equals("Three3"))
			return false;
		
		map.put(5, "as");
		map.put(2, "asd");
		map.put(9, "asd");
		map.put(10, "asdasd");
		
		map.put(11, "eleven");
		
		//delete 11 then try to find it
		
		map.delete(11);
		if(map.contains(11))
			return false;
		
		if (!map.contains(15))
			return false;	
	
		
		System.out.println("Size"+map.size());
		System.out.println("coll"+map.getCollisions());
		
		return true;
	}
	
	private static boolean test6() {
		QuadraticProbing<Integer, String> map=new QuadraticProbing<Integer,String>();
		for (int i = 0; i < 100000; i++) {
			map.put(i, "as");
		}
		map.put(12, "one");
		
		
	
		map.put(1, "two");
		
		
		map.put(11, "three");
		
		
		//add duplicate then test it.
		map.put(11, "Three3");
		
		if (!map.get(11).equals("Three3"))
			return false;
		
		map.put(5, "as");
		map.put(2, "asd");
		map.put(9, "asd");
		map.put(10, "asdasd");
		
		map.put(11, "eleven");
		
		//delete 11 then try to find it
		
		map.delete(11);
		if(map.contains(11))
			return false;
		
		if (!map.contains(15))
			return false;	
	
		
		System.out.println("Size"+map.size());
		System.out.println("coll"+map.getCollisions());
		
		return true;
	}


}

