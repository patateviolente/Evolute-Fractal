package fract.test.julia;

import java.awt.*;

import fract.algo.BaseFractale;
import fract.struct.*;

public class MandelbrotTmp extends Frame {
	private static final long serialVersionUID = 1L;

	// Programme principal
	public MandelbrotTmp() {
		this.setTitle("MandelbrotTmp");
		this.setBounds(10, 10, 600, 550);
		this.setVisible(true);
		this.setBackground(Color.white);
		this.addWindowListener(new FermerFenetre());
		this.setVisible(true);
		this.fdessinfractale(this.getGraphics());
	}

	// -Dessin des fractales
	public void fdessinfractale(Graphics g) {

		int width = 600, height = 550; // taille de l'image
		double scale = 0.008;
		// centre du graph
		double posX = -(width/(1/scale))/2, posY = -(height/(1/scale))/2;

		int iteration = 40;

		//double tmp = 0, z1 = 0, z2 = 0, p1 = 0, p2 = 0;

		final double angle=(0.5);
		
		double tmp = 0, z1 = 0, z2 = 0, x2 = 0, y2 = 0,x3=0, y3=0, rZx = 0, rZy = 0;

		int n = 0;
		double x1,y1;

		// couleurs (provisoire)
		Color c[] = new Color[iteration];
		c[0] = Color.black;
		for (int i = 1; i < iteration; i++)
			c[i] = new Color(i * 256 / (iteration + 1), 0, 0);
		c[iteration - 1] = Color.black;

		// algo
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				n = 0;
				
			    //normalization and scale coordinates
			    
				Complex point = new Complex (x * scale + posX, y * scale + posY);
				Complex z = point;
				
				for (int i = 1; i < iteration; i++) {
					
					/** Il suffit de changer la variable p pour la puissance Z = Z^p + C*/
					int p=2;
					z=z.puissance(p);
					
					rZx= z.re();
					rZy = z.im();
					
	            	// rotation
					z1= (rZx*Math.cos(angle) - rZy*Math.sin(angle));
					z2= (rZy*Math.cos(angle) + rZx*Math.sin(angle));
					
					// affection partie reel et imaginaire dans z
					z= new Complex(z1, z2);
					z=z.plus(point);
					
					if (!((z.abs()) < 4 && ++n < iteration))
						break;
				}

				g.setColor(c[n]);
				g.drawLine(x, y, x, y);
			}
		}
	}
}