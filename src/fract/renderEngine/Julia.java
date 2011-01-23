package fract.renderEngine;

import fract.struct.Complex;

public class Julia extends BaseFractale {
	final static double CposX = -1.3, CposY = -1.24, CscaleMin = 0.012,
			CscaleMax = 0.000001;
	final static int Citerations = 100;
	final int CNbArgs = 3;

	public Julia() {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
		this.nbArgs = this.CNbArgs;
		this.enterArgs = new double[this.nbArgs];
		this.enterArgs[0] = 2; // puissance
		this.enterArgs[1] = -0.745; // réel constante
		this.enterArgs[2] = 0.1; // img constante
	}

	/*
	 * @see fract.algo.ModelFractale#adaptDefaultCoordToNewDim(int, int)
	 */
	public void adaptDefaultCoordToNewDim(int x, int y) {
	}


	/*
	 * #######################################################################
	 * ------------------------Méthodes de calculs----------------------------
	 * #######################################################################
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see fract.algo.ModelFractale#initializeVariablesForXThreads(int)
	 */
	public void initializeVariablesForXThreads(int n) {
		z1Tmp = new double[n];
		z2Tmp = new double[n];
		x = new double[n];
		y = new double[n];
		z1 = new double[n];
		z2 = new double[n];
		z = new Complex[n];

		for (int i = 0; i < n; i++)
			z[i] = new Complex(0d, 0d);
	}

	/*
	 * @see fract.algo.BaseFractale#calculateStepZZero(int, int, int)
	 */
	public void calculateStepZZero(int t, int x, int y) {
		this.z1[t] = this.z2[t] = 0;
		this.x[t] = x;
		this.y[t] = y;
		z1[t] = (posX + this.x[t] * scale);
		z2[t] = (posY + this.y[t] * scale);

		if (this.angle != 0) {
			z1Tmp[t] = z1[t] + this.decX;
			z2Tmp[t] = z2[t] + this.decY;
			z1[t] = (z1Tmp[t] * Math.cos(angle*2) - z2Tmp[t] * Math.sin(angle*2));
			z2[t] = (z2Tmp[t] * Math.cos(angle*2) + z1Tmp[t] * Math.sin(angle*2));
			// pixel = centre + z1[t] + flip(z2[t]
			// flip echange partie re partie img
			// flip(2) = (0,2);
			// flip(1+3i) = (3+1i);
		}

		z[t] = new Complex(z1[t], z2[t]);
		C = new Complex(this.enterArgs[1], this.enterArgs[2]);
	}

	/*
	 * @see fract.algo.BaseFractale#calculateStepPowAndCst(int, int, int, int)
	 */
	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		z[t] = z[t].puissance((int) (this.enterArgs[0])).plus(C);
	}

	/*
	 * @see fract.algo.BaseFractale#calculateStepGetCondition(int, int)
	 */
	public boolean calculateStepGetCondition(int t, int i) {
		return (!((z[t].abs()) < 4 && i < iterations));
	}

	// variables de calcul
	private double[] z1Tmp, z2Tmp, z1, z2, x, y;
	private Complex[] z;
	private Complex C;
}
