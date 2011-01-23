
package fract.test;
import javax.swing.*;
import fract.ihm.*;

public class TestSelectAngle {
	public static void main(String[] args) {
		new FenTest();
	}
}

class FenTest extends JFrame{
	private static final long serialVersionUID = 1L;

	public FenTest(){
		this.setVisible(true);
		this.setSize(300, 300);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		SelectAngle sa = new SelectAngle();
		this.getContentPane().add(sa);
	}
}