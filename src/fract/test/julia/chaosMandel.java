package fract.test.julia;

import java.awt.*;

import fract.algo.BaseFractale;
import fract.struct.*;

public class chaosMandel extends Frame {
	private static final long serialVersionUID = 1L;

	// Programme principal
	public chaosMandel() {
		this.setTitle("choasMandel");
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
        //double posX = -2.1, posY = -1.35;
        double scale = 0.008;
        // center of Frame (0,0)
		double posX = -(width*scale)/2, posY = -(height*scale)/2 ;
		System.out.println("posX :"+posX+" ; posY :"+posY);
        int iteration = 40;

        // double tmp = 0, z1 = 0, z2 = 0, p1 = 0, p2 = 0;

        // removed final keyword for my demonstration loop
        double angle = 0; // Math.PI / 3;

        /** rotation point in complex plane, i.e. at (-2.1/0)
         ** suffit de changer mx et my pour modifier le rep�re de rotation
         ** mx et my est le milieu du cadre de zoom.
         */
        double mx = -0.0, my = -0.0;

        double z1 = 0, z2 = 0;

        int n = 0;

        // couleurs (provisoire)
        Color c[] = new Color[iteration];
        c[0] = Color.black;
        for (int i = 1; i < iteration; i++)
            c[i] = new Color(i * 256 / (iteration + 1), 0, 0);
        c[iteration - 1] = Color.black;

        // Just for demonstration purposes to show that it works...
        for (angle = 0; angle < Math.PI; angle += Math.PI / 10)
        {
            // algo
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    n = 0;
                    Complex z = new Complex(0.0, 0.0);
                    // now define the pixel to be examined...Defining that pixel was done inside the iteration loop. Quite confusing...
                    double px = posX + x * scale;
                    double py = posY + y * scale;

                    // and now rotate (px/py) by angle around (mx/my)
                    // for this to do, subtract mx/my (so center is at 0/0),
                    // then rotate, then add mx/my again

                    px -= mx;
                    py -= my;

                    // Ok, centered at 0/0, now rotate px/py by angle!
                    double rx = (px * Math.cos(angle) - py * Math.sin(angle));
                    double ry = (py * Math.cos(angle) + px * Math.sin(angle));

                    px = rx + mx;
                    py = ry + my;

                    for (int i = 1; i < iteration; i++) {
                        /**
                         * Il suffit de changer la variable p pour la puissance
                         * Z = Z^p + c
                         */
                        int p = 2;
                        z = z.puissance(p);

                        z1 = z.re() + px;
                        z2 = z.im() + py;

                        // affection partie reel et imaginaire dans z
                        z = new Complex(z1, z2);

                        if (!((z1 * z1 + z2 * z2) < 4 && ++n < iteration))
                            break;
                    }

                    g.setColor(c[n]);
                    g.drawLine(x, y, x, y);
                    
                }
                g.setColor(Color.yellow);
                g.drawLine((int) (-(posX-mx)/scale), 0,(int) (-(posX-mx)/scale), height); //axe y
                g.drawLine(0, (int)(-(posY-my)/scale), width , (int) (-(posY-my)/scale)); //axe x
            }
        }
    }
}
  