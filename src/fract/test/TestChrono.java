package fract.test;

import java.util.Date;

public class TestChrono {

	public static void main(String[] args) {
		Date d1 = new Date();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		
		Date d2 = new Date();
		
		long diff = d2.getTime() - d1.getTime();
		System.out.println("durée = "+diff);
	}

}
