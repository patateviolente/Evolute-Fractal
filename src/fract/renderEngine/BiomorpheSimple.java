package fract.renderEngine;

import fract.struct.*;

/**
 * Algo de fractal spécifique au radiolaire 12 pointes dans la théorie
 */
public class BiomorpheSimple extends BaseFractale {
	final static double CposX = -9.4, CposY = -10.5, CscaleMin = 0.1,
			CscaleMax = 0.000001;
	final static int Citerations = 20;
	final int CNbArgs = 2;

	/**
	 * Constructeur avec valeurs par défaut
	 */
	public BiomorpheSimple() {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
		this.nbArgs = this.CNbArgs;
		this.enterArgs = new double[this.nbArgs];
		this.enterArgs[0] = 15;
		this.enterArgs[1] = 0;
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
		z = new ComplexeCartesien[n][];
		for (int i = 0; i < n; i++)
			z[i] = new ComplexeCartesien[iterations];
	}

	public void calculateStepZZero(int t, int x, int y) {
		z[t][0] = new ComplexeCartesien(posX + scale * x, posY + scale * y);
	}

	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		// dépend du choix de la liste, paar défault carré
		switch ((int)this.enterArgs[1]) {
		case 0:	// carré
			z[t][i] = z[t][i - 1].carre();
			break;
		case 1:
			z[t][i] = z[t][i - 1].cube();
			break;
		case 2:
			z[t][i] = z[t][i - 1].cos();
			break;
		case 3:
			z[t][i] = z[t][i - 1].sin();
			break;
		default:
			z[t][i] = z[t][i - 1].exp();
			break;
		}
		z[t][i].setRe(z[t][i].getRe() + this.enterArgs[0]);
	}

	public boolean calculateStepGetCondition(int t, int i) {
		return (z[t][i].isReImgOrModUpTo(this.iterations) && z[t][i]
				.isReOrImgDownTo(this.iterations));
	}

	protected ComplexeCartesien z[][];
}
