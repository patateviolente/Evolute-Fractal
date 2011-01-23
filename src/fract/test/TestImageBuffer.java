package fract.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class TestImageBuffer extends JFrame {
	public static void main(String[] args) {
		new FenBI();
	}
}

class FenBI extends JFrame {
	public FenBI() {
		setBounds(100, 100, 200, 200);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Pan p = new Pan();
		this.add(p);
	}

	class Pan extends JPanel {
		public Pan() {
			bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
			bi.setRGB(10, 10, Color.red.getRGB());
		}
		public void paintComponent(Graphics g){
			Graphics2D g2d = (Graphics2D)g;
			g2d.rotate(0.5, 50, 50);
			g2d.drawImage(bi, 0, 0, null);
			//bi.setRGB(0, 0, null);
			System.out.println(bi.getRGB(0, 0));
		}
		BufferedImage bi;
	}
}