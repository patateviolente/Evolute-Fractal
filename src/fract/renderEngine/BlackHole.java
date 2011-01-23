package fract.renderEngine;

import fract.struct.Complex;

public class BlackHole extends BaseFractale {
	final static double CposX = -2.20, CposY = -3.20, CscaleMin = 0.028222,
			CscaleMax = 0.000001;
	final static int Citerations = 40;
	
	public BlackHole() {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(CposX, CposY, CscaleMin, Citerations);
		this.initializeVariablesForXThreads(1); // au moins 1 thread
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

	public void initializeVariablesForXThreads(int n) {
		z1Tmp = new double[n];
		z2Tmp = new double[n];
		z1 = new double[n];
		z2 = new double[n];
		z = new Complex[n];
		b = new Complex[n];
		for(int i=0; i<n; i++){
			z[i] = new Complex(0d, 0d);
			b[i] = new Complex(0d, 0d);
		}
	}

	public void calculateStepZZero(int t, int x, int y) {
		z[t] = new Complex(0d, 0d);
		b[t] = new Complex(1d, 0d);
	}

	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		z[t]=((z[t].cos()).plus(b[t])).divides((z[t].tan()).minus(b[t]));
		
		z1[t]=z[t].re()+(posX + x * scale);
		z2[t]=z[t].im()+(posY + y * scale);
		
		if(this.angle != 0){
			z1Tmp[t] = z1[t];
			z2Tmp[t] = z2[t];
			z1[t] = (z1Tmp[t] * Math.cos(angle) - z2Tmp[t] * Math.sin(angle));
			z2[t] = (z2Tmp[t] * Math.cos(angle) + z1Tmp[t] * Math.sin(angle));
		}
		z[t]= new Complex(z1[t], z2[t]);
	}

	public boolean calculateStepGetCondition(int t, int i) {
		return (!((z1[t]*z1[t] + z2[t]*z2[t]) < 20 && ++i < this.iterations));
	}

	private double[] z1, z2,z1Tmp, z2Tmp;
	private Complex[] z;
	private Complex[] b;
}
