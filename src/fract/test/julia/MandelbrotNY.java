package fract.test.julia;

import java.awt.*;

import javax.swing.JFrame;

public class MandelbrotNY extends Frame{

	//Déclaration des variables
	//-Affichage SDL
	int largeur=500,hauteur=500,bits=32;
	//-Programme
	int pauseprogramme=0;
	//-Calcul des pixels
	int px=0, py=0;

	//-Calcul de la Fractale
	int zoom=1, Xrepereinit=0, Yrepereinit=0;
	int Xinit, Yinit;
	int n;
	double Xrepere, Yrepere, Zx, Zy, Cx, Cy, copie;
	//-Fractale de Julia pré-enregistrée
	int nfractale=0;
	int nfractalemax=4;
	//double parametreC[4*2]={0,0,-0.7927,0.1609,0.32,0.043,-0.0986,-0.65186};
	double paramCx, paramCy;
	//-Apparence de la fractale (Ces variables peuvent être modifiées)
	int pas=10;
	int borderonly=0;
	int facteur=1;


	//Programme principal
	public MandelbrotNY(){
		setTitle ("MandelbrotNY");
		setBounds ( 10, 10, 500 , 500 );
		setVisible (true);
		setBackground ( Color.cyan );
		addWindowListener (new FermerFenetre()); 
		
		setVisible (true);
		this.fdessinfractale(getGraphics());
	}


	//Fonctions

	//-Dessin des fractales
	public void fdessinfractale(Graphics g){
		for (py=250; py<=hauteur; py++){
			
			for (px=0; px<largeur; px++){
	    		Xinit=0;Yinit=0;
	            Xinit=(int)(Xrepereinit-(largeur/2));
	            Yinit=(int)(Yrepereinit-(hauteur/2));
	            Xrepere=4*((px+Xinit)/((double)(zoom*largeur)));
	            Yrepere=4*((py+Yinit)/((double)(zoom*hauteur)));
	            Zx=Xrepere;
	            Zy=Yrepere;
	            if(paramCx!=0&&paramCy!=0){Cx=paramCx;Cy=paramCy;}
	            else{Cx=Xrepere;Cy=Yrepere;};
	            n=0;
	            while ( n<255 && (Zx*Zx+Zy*Zy)<=4 ){copie=Zx;
	                                              Zx=Zx*Zx-Zy*Zy+Cx;
	                                              Zy=2*copie*Zy+Cy;
	                                              n++;
	                                             }
	            if(n==255){
					g.setColor(Color.black);
					g.drawLine(px, py, px, py);
					g.drawLine(px, py-(2*(py-250)), px, py-(2*(py-250)));
	            }
	            else{
					Color col = new Color(n, n, 0);
					g.setColor(col);
					g.drawLine(px, py, px, py);
					g.drawLine(px, py-(2*(py-250)), px, py-(2*(py-250)));
	            }
			}
		}
	}
}