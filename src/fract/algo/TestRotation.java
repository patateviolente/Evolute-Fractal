package fract.algo;

import fract.ihm.MainWindow;
import fract.struct.Complex;

public class TestRotation extends BaseFractale {
	final static double CposX = -1.8, CposY = -1.24, CscaleMin = 0.012,
			CscaleMax = 0.000001;
	final static int Citerations = 45;
	final int CNbArgs = 1;

	public TestRotation(MainWindow mw) {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(mw, CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
		this.nbArgs = this.CNbArgs;
		this.enterArgs = new double[this.nbArgs];
		this.enterArgs[0] = 2;
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

	/*
	 * (non-Javadoc)
	 * @see fract.algo.ModelFractale#initializeVariablesForXThreads(int)
	 */
	public void initializeVariablesForXThreads(int n) {
		x = new double[n];
		y = new double[n];
		z1 = new double[n];
		z2 = new double[n];
		z = new Complex[n];
		
		px = new double[n];
		py = new double[n];
		rx = new double[n];
		ry = new double[n];
		
        // rotation d'un point dans un plan complexe
        // suffit de changer mx et my pour modifier le repère de rotation
		mx = posX - (-(Largeur * scale)/2);
		my = posY - (-(Hauteur * scale)/2);
		
		for(int i=0; i<n; i++){
			z[i] = new Complex(0d, 0d);
		}
	}

	/*
	 * @see fract.algo.BaseFractale#calculateStepZZero(int, int, int)
	 */
	public void calculateStepZZero(int t, int x, int y) {
		this.z1[t] = this.z2[t] = 0;
		this.x[t] = x;
		this.y[t] = y;
		
		z[t] = new Complex(0d, 0d);
		
        // now define the pixel to be examined...Defining that pixel was done inside the iteration loop. Quite confusing...
        px[t] = (posX + this.x[t] * scale);
        py[t] = (posY + this.y[t] * scale);
        

		if(this.angle != 0){
			
        // and now rotate (px/py) by angle around (mx/my)
        // for this to do, subtract mx/my (so center is at 0/0),
        // then rotate, then add mx/my again

        px[t] -= mx;
        py[t] -= my;
        
            // Ok, centered at 0/0, now rotate px/py by angle!

        
             rx[t] = (px[t] * Math.cos(angle) - py[t] * Math.sin(angle));
             ry[t] = (py[t] * Math.cos(angle) + px[t] * Math.sin(angle));
             
	    
        px[t] = rx[t] + mx;
        py[t] = ry[t] + my;
		}
        
	}

	/*
	 * @see fract.algo.BaseFractale#calculateStepPowAndCst(int, int, int, int)
	 */
	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		z[t] = z[t].puissance((int)(this.enterArgs[0]));
		
		z1[t]=z[t].re() + px[t] ;
		z2[t]=z[t].im() + py[t] ;
		
		z[t]= new Complex(z1[t], z2[t]);
	}

	/*
	 * @see fract.algo.BaseFractale#calculateStepGetCondition(int, int)
	 */
	public boolean calculateStepGetCondition(int t, int i) {
		return (!((z[t].abs()) < 4 && i < iterations));
	}

	// variables de calcul
	private double[] z1, z2, x, y, px, py, rx, ry;
	private Complex[] z; 
	private double mx, my;
}
