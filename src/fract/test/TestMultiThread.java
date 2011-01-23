package fract.test;
import java.util.*;

public class TestMultiThread {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		// lancement calcul
		System.out.println("availableProcessors : " + Runtime.getRuntime().availableProcessors());
		System.exit(0);
		c = new CalculPourris[8];
		t = new Thread[8];
		
		for(int i=0; i<c.length; i++){
			c[i] = new CalculPourris();
			t[i] = new Thread(c[i]);
		}
		for(int i=0; i<c.length; i++){
			t[i].start();
		}
		
		scan.nextLine();
		
		for(int i=0; i<c.length; i++)
			new Thread(c[i]).start();
	}
	public static CalculPourris[] c;
	public static Thread[] t;
}

class CalculPourris implements Runnable{
	public void run(){
		calcul();
	}
	public synchronized void calcul(){
		double c = 0;
		for(int i=1; i<100; i++){
			for(int u=1; u<400000; u++)
				c = Math.atan(i/u);
			System.out.println(i+"%______"+c);
		}
	}
	
}