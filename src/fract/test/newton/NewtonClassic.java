package fract.test.newton;

import java.awt.*;

import fract.struct.*;
import fract.test.julia.FermerFenetre;

public class NewtonClassic extends Frame {
	private static final long serialVersionUID = 1L;

	// Programme principal
	public NewtonClassic() {
		this.setTitle("NewtonClassic");
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
		int iteration = 40;


		//final double angle=(Math.PI);
		
		Complex zold;
		Complex r = new Complex(1.0, 0.0);

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

				for (int i = 1; i < iteration; i++) {
					
					/** Il suffit de changer la variable p 
					 * si on modifie p, il faut aussi modifier les couleurs*/
					int p=5;
					if (p==3){
						z=(Complex.plus(z.puissance(p).times(2.0), r)).divides(z.puissance(p-1).times(3.0));
					}
					else{
						z= z.minus((z.puissance(p).plus(r)).divides(z.puissance(p-1).times(p)));
					}
					if (!(z.minus(z).abs() < 0.00001 && ++n < iteration))
						break;
				}
				int j;
				j= (int) (Math.random()*100)+150;
				j = 150;
				if (z.re()>0.0){
					Color blue = new Color(0, j, 0);
					g.setColor(blue);
					g.drawLine(x, y, x, y);
				}
				else
				{
					if ((z.re()<-0.3) && (z.im()>0.0)){
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
				//g.setColor(c[n]);
				//g.drawLine(x, y, x, y);
			}
		}
	}
}