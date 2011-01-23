package fract.algo;

import java.awt.Color;

import fract.ihm.MainWindow;
import fract.struct.*;

public class Newton extends BaseFractale {
	final static double CposX = -1.8, CposY = -1.8, CscaleMin = 0.008,
			CscaleMax = 0.000001;
	final static int Citerations = 35;

	/**
	 * Constructeur avec valeurs par défaut
	 * 
	 * @param mw
	 */
	public Newton(MainWindow mw) {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(mw, CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
		this.bg = Color.white;
	}

	/*
	 * @see fract.algo.ModelFractale#adaptDefaultCoordToNewDim(int, int)
	 */
	public void adaptDefaultCoordToNewDim(int x, int y) {
		this.adaptDefaultCoordToNewDim(x, y, CposX, CposY, CscaleMin);
	}

	/*
	 * @see fract.algo.BaseFractale#getpourcentZoom()
	 */
	public int getpourcentZoom() {
		return this.getpourcentZoom(CscaleMax, CscaleMin);
	}

	/*
	 * #######################################################################
	 * ------------------------Méthodes de calculs----------------------------
	 * #######################################################################
	 */

	public void initializeVariablesForXThreads(int n) {
		//z3 = new Cmplx[n];
		//z = new Cmplx[n];
		//z2 = new Cmplx[n];
		//test = new Cmplx[n];
		
		tmp = new double[n];
		zx = new double[n];
		zy = new double[n]; 
		for(int i=0; i<n; i++){
			tmp[i] = 0;
			zx[i] = 0;
			zy[i] = 0;
		}
	}

	public void calculateStepZZero(int t, int x, int y) {
		//z[t] = new Cmplx(posX + scale * x, posY + scale * y);
		zx[t] = posX + x * scale;
		zy[t] = posY + y * scale;
	}

	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		/*z2[t].sqr(z[t]);			// carre
		z2[t].mult(3);			// facteur
		test[t].cube(z[t]);		// cube
		z3[t].set(test[t].real(), test[t].imag());
		z3[t].mult(2);			// facteur
		z3[t].add(one);		// add
		z[t].divid(z3[t], z2[t]);	// diviser
		test[t].subtr(one);	// soustraire
		*/
		d = 3.0*((zx[t]*zx[t] - zy[t]*zy[t])*(zx[t]*zx[t] - zy[t]*zy[t]) + 4.0*zx[t]*zx[t]*zy[t]*zy[t]);
		if (d == 0.0){
			d = 0.00001;
		}
		/* Decomposition of z = z^3 -1*/
		
		tmp[t]=zx[t];
		zx[t] = (2.0/3.0)*zx[t] + (zx[t]*zx[t] - zy[t]*zy[t])/d; 
		zy[t] = (2.0/3.0)*zy[t] - (2.0*tmp[t]*zy[t])/d;
	}

	public boolean calculateStepGetCondition(int t, int i) {
		return ((zx[t] >= 0.04));
	}

	final Complex one = new Complex(1.0, 0.0);
	Complex[] z, z3, z2, test;
	double tmp[], zx[], zy[], d;
}

