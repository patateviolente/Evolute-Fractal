package fract.algo;

import fract.ihm.MainWindow;
import fract.struct.Complex;

public class BlackHole extends BaseFractale {
	final static double CposX = -2.20, CposY = -3.20, CscaleMin = 0.028222,
			CscaleMax = 0.000001;
	final static int Citerations = 40;

	/**
	 * Constructeur avec valeurs par défaut
	 * 
	 * @param mw
	 */
	public BlackHole(MainWindow mw) {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(mw, CposX, CposY, CscaleMin, Citerations);
		this.initializeVariablesForXThreads(1); // au moins 1 thread
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
		//_d = new double[n];
		//_z1 = new double[n];
		//_z2 = new double[n];
		//_tanx = new double[n];
		//_tanhy = new double[n];
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
		//_z1[t] = -1;
		//_z2[t] = 0;
	}

	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		//_tanx[t] = Math.tan(_z1[t]);
		//_tanhy[t] = Math.tanh(-_z2[t]);
		//_d[t]= Math.pow(_tanx[t]-1, 2)+ Math.pow(_tanhy[t]+_tanhy[t]*_tanx[t], 2);
		
		//_z1[t] = (_tanx[t]*_tanx[t] -1 -((-_tanhy[t]+ _tanhy[t]*_tanx[t])*(_tanhy[t]+ _tanhy[t]*_tanx[t])))/_d[t] + (this.posX + x * scale);
		//_z2[t] = (((_tanx[t]+1)*(_tanhy[t]+_tanhy[t]*_tanx[t]))+((_tanx[t]-1)*(-_tanhy[t]+_tanhy[t]*_tanx[t])))/_d[t] + (this.posY + y * scale);
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
		//return (!((_z1[t]*_z1[t] + _z2[t]*_z2[t]) < 20 && ++i < this.iterations));
		return (!((z1[t]*z1[t] + z2[t]*z2[t]) < 20 && ++i < this.iterations));
	}

	//private double[] _z1, _z2, _tanx, _tanhy, _d;
	private double[] z1, z2,z1Tmp, z2Tmp;
	private Complex[] z;
	private Complex[] b;
}
