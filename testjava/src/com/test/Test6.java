package com.test;

public class Test6 {
	private static String name="test";
	private static int t1 =10;
	

	

	public static void main(String[] args) {
		/*User u = new User();
		u.setName("nnnn");
		String str = "test1";
		testu(u,str);
		System.out.println(u.getName());
		System.out.println(str);
		*/
		/*changeName(name,t1);
		System.out.println(name+"========"+t1);*/
		
		try{
			System.out.println("===========11");
			return;
		}
		finally{
			System.out.println("===========222");
		}
		
	}
	
	public static void changeName(String name1,int t){
		name = "hahhhhahahah";
		t=100;
	}
	
	public static void testu(User u ,String tempStr){
		u.setName("name");
		tempStr="test";
	}
	
	
}
