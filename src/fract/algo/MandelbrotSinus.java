package fract.algo;
import fract.ihm.MainWindow;
import fract.struct.Complex;

public class MandelbrotSinus extends BaseFractale {
final static double CposX = -1.8, CposY = -1.24, CscaleMin = 0.012, 
	CscaleMax = 0.000001;
final static int Citerations = 45;
	public MandelbrotSinus(MainWindow mw) {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(mw, CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
	}
	
	public void adaptDefaultCoordToNewDim(int x, int y) {
		this.adaptDefaultCoordToNewDim(x, y, CposX, CposY, CscaleMin);
	}
	
	public int getpourcentZoom() {
		return this.getpourcentZoom(CscaleMax, CscaleMin);
	}
	
	/*
	 * #######################################################################
	 * ------------------------Méthodes de calculs----------------------------
	 * #######################################################################
	 */
	
	public void initializeVariablesForXThreads(int n) {
		z1Tmp = new double[n];
z2Tmp = new double[n];
x = new double[n];
y = new double[n];
z1 = new double[n];
z2 = new double[n];
z = new Complex[n];

for(int i=0; i<n; i++)
z[i] = new Complex(0d, 0d);
	}
	
	public void calculateStepZZero(int t, int x, int y) {
		this.z1[t] = this.z2[t] = 0;
this.x[t] = x;
this.y[t] = y;

z[t] = new Complex(0d, 0d);
	}
	
	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		z[t] = z[t].puissance((int)(2)).sin();
z1[t]=z[t].re()+(posX + this.x[t] * scale);
z2[t]=z[t].im()+(posY + this.y[t] * scale);
z[t]= new Complex(z1[t], z2[t]);

	}
	
	public boolean calculateStepGetCondition(int t, int i) {
		return (!((z1[t]*z1[t] + z2[t]*z2[t]) < 4 && i < iterations));
	}
	
private double[] z1Tmp, z2Tmp, z1, z2, x, y;
private Complex[] z;	// variables de calcul
}

