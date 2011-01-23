package fract.algo;

import fract.ihm.MainWindow;
import fract.struct.*;

public class BiomorpheCarreTanCube extends BaseFractale {
final static double CposX = -1.8, CposY = -1.24, CscaleMin = 0.012, 
	CscaleMax = 0.000001;
final static int Citerations = 15;
	public BiomorpheCarreTanCube(MainWindow mw) {
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
		z = new ComplexeCartesien[n][];
for (int i = 0; i < n; i++)
z[i] = new ComplexeCartesien[iterations];
	}
	
	public void calculateStepZZero(int t, int x, int y) {
		z[t][0] = new ComplexeCartesien(posX + scale * x, posY + scale * y);
	}
	
	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		z[t][i] = z[t][i - 1].carre().sin().cube();
z[t][i].setRe(z[t][i].getRe() + 10);	// 10 = CONSTANTE
	}
	
	public boolean calculateStepGetCondition(int t, int i) {
		return (z[t][i].isReImgOrModUpTo(this.iterations) && z[t][i].isReOrImgDownTo(this.iterations));
	}
	
protected ComplexeCartesien z[][];	// variables de calcul
}

