package fract.test;

import java.io.*;


public class TestSaveObject {

	public static void main(String[] args) {
		try {
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File("distantRenderersList"))));

				oos.writeObject(new StructSaveOption("", "", 1));
				oos.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			return;
		}
		System.out.println("fin");
	}

}

class StructSaveOption implements Serializable {
	private static final long serialVersionUID = 1L;

	public StructSaveOption(String nom, String ssh, int valSlider) {
		this.nom = nom;
		this.ssh = ssh;
		this.valSlider = valSlider;
	}

	private String nom, ssh;
	private int valSlider;
}
