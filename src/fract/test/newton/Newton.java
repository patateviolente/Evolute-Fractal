package fract.test.newton;

/* Newton of z = z^3 -1*/

import java.awt.*;

import fract.algo.BaseFractale;
import fract.test.julia.FermerFenetre;
import fract.struct.*;

public class Newton extends Frame {
	private static final long serialVersionUID = 1L;

	// Programme principal
	public Newton() {
		this.setTitle("Newton");
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
		double posX = -1.33, posY = -1.5;
		double scale = 0.0053;
		int iteration = 100;

		double tmp = 0, zx = 0, zy = 0; 
		int n = 0;

		double d;
		// couleurs (provisoire)
		
		

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				zx = posX + x * scale;
				zy = posY + y * scale;
				n = 0;

				for (int i = 1; i < iteration; i++) {

					d = 3.0*((zx*zx - zy*zy)*(zx*zx - zy*zy) + 4.0*zx*zx*zy*zy);
					if (d == 0.0){
						d = 0.00001;
					}
					/* Decomposition of z = z^3 -1*/
					
					tmp=zx;
					zx = (2.0/3.0)*zx + (zx*zx - zy*zy)/d; //+ (posX + x * scale);
					zy = (2.0/3.0)*zy - (2.0*tmp*zy)/d; //+ (posY + y * scale);

				}
				int j;
				j= (int) (Math.random()*100)+150;
				//System.out.println("couleur= "+j);
				if (zx>0.0){
					Color blue = new Color(0, j, 0);
					g.setColor(blue);
					g.drawLine(x, y, x, y);
				}
				else
				{
					if ((zx<-0.3) && (zy>0.0)){
						
						Color green = new Color(0, 0, j);
						g.setColor(green);
						g.drawLine(x, y, x, y);
						}
					else{
						Color red = new Color(j, 0, 0);
						g.setColor(red);
						g.drawLine(x, y, x, y);
					}
				}
			}
		}
	}
}