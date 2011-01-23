package fract.test.blackhole;

import java.awt.*;
import fract.test.julia.*;
import fract.struct.Complex;

public class BlackHole extends Frame {
	private static final long serialVersionUID = 1L;

	// Programme principal
	public BlackHole() {
		this.setTitle("BlackHole");
		this.setBounds(10, 10, 500, 500);
		this.setVisible(true);
		this.setBackground(Color.white);
		this.addWindowListener(new FermerFenetre());
		this.setVisible(true);
		this.fdessinfractale(this.getGraphics());
	}

	// -Dessin des fractales
	public void fdessinfractale(Graphics g) {
		final int width = 500, height = 500; // taille de l'image
		final double posX = -1.60, posY = -1.80;
		final double scale = 0.0072222;
		final int iteration = 40;
		final double deg = (Math.PI)/180;				//convertion de radians en degres

		double z1, z2, tanx, tanhy, d;
		int n = 0;
		
		
		System.out.println("degre tan 60="+Math.tan(deg*60)+", radian tan 60 :"+Math.tan(60));
		// couleurs (provisoire)
		Color c[] = new Color[iteration];
		c[0] = Color.black;
		for (int i = 1; i < iteration; i++){
			c[i] = new Color(i * 256 / (iteration + 1), 0, 0);
		}
		c[iteration - 1] = Color.black;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				z1 = 0;
				z2 = 0;
				n = 0;
				Complex z= new Complex(0d, 0d);
				Complex b= new Complex(1d, 0d);

				for (int i = 1; i < iteration; i++) {
					/*tanx = Math.tan(z1);
					tanhy = Math.tanh(-z2);
					d= Math.pow(tanx-1, 2)+ Math.pow(tanhy+tanhy*tanx, 2);
					
					/** Decomposition of z = (tan(z)+1)/tan(z)-1)
					 * valeur complexe du tan(x+iy) = (tan(x) - itanh(x))/(1+itanh(-y)*tanx)
					 *

					z1 = (tanx*tanx -1 -((-tanhy+ tanhy*tanx)*(tanhy+ tanhy*tanx)))/d + (posX + x * scale);
					z2 = (((tanx+1)*(tanhy+tanhy*tanx))+((tanx-1)*(-tanhy+tanhy*tanx)))/d + (posY + y * scale);
					*/
					z=((z.cos()).plus(b)).divides((z.tan()).minus(b));
					
					z1=z.re()+(posX + x * scale);
					z2=z.im()+(posY + y * scale);
					
					z= new Complex(z1, z2);
					
					if (!((z1*z1 + z2*z2) < 20 && ++n < iteration)) {
						break;
					}
				}

				g.setColor(c[n]);
				g.drawLine(x, y, x, y);
			}
		}
	}
}