package fract.test.julia;


import java.awt.event.*;  //WindowAdapter

public class FermerFenetre extends WindowAdapter {
	public void windowClosing (WindowEvent evt) {
		System.out.println (evt.getWindow().getName());
		if (evt.getWindow().getName().equals("frame0")) {
			System.exit(0);
		} else {
			evt.getWindow().dispose();
		}
	}
}
