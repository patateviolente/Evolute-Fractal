package fract.test.julia;

import java.awt.*;

import fract.algo.BaseFractale;
import fract.struct.*;

public class Julia extends Frame {
	private static final long serialVersionUID = 1L;

	// Programme principal
	public Julia() {
		this.setTitle("JuliaSet");
		this.setBounds(10, 10, 500, 500);
		this.setVisible(true);
		this.setBackground(Color.white);
		this.addWindowListener(new FermerFenetre());
		this.setVisible(true);
		this.fdessinfractale(this.getGraphics());
	}

	// -Dessin des fractales
	public void fdessinfractale(Graphics g) {

		int width = 500, height = 500; // taille de l'image
		double posX = -2, posY = -2;
		double scale = 0.008;
		int iteration = 50;

		//double tmp = 0, z1 = 0, z2 = 0, p1 = 0, p2 = 0;

		final double angle=(Math.PI/2/8);
		
		double tmp = 0, z1 = 0, z2 = 0, x2 = 0, y2 = 0,x3=0, y3=0, rZx = 0, rZy = 0;

		int n = 0;

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
				
				Complex z= new Complex(posX + x * scale, posY + y * scale);
				
				/**
				 * Different julia
				 * a) C = -0.5 + 0.5i		d) C = -0.2 + 0.75i
				 * b) C = -1 + 0.05i		e) C = 0.25 + 0.52i
				 * e) C = -0.5 + 0.55i		f) C = 0.66i			h) C = -i
				 * 			etc...
				 */
				Complex C= new Complex(-0.74543, 0.11301);


				for (int i = 1; i < iteration; i++) {
					
					/** Il suffit de changer la variable p pour la puissance Z = Z^p + C*/
					int p=2;
					z=z.puissance(p).plus(C);
					
	            	// rotation
					//z1= (rZx*Math.cos(angle) - rZy*Math.sin(angle));
					//z2= (rZy*Math.cos(angle) + rZx*Math.sin(angle));
							
					if (!((z.abs()) < 4 && ++n < iteration))
						break;
				}

				g.setColor(c[n]);
				g.drawLine(x, y, x, y);
			}
		}
	}
}