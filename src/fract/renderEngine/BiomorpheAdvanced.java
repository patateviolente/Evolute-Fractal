package fract.renderEngine;

import fract.struct.*;

/**
 * Biomorphes avancé : permet une équation complexe avec un nombre de paramètres
 * infinis.
 */
public class BiomorpheAdvanced extends BaseFractale {
	final static double CposX = -9.4, CposY = -10.5, CscaleMin = 0.1,
			CscaleMax = 0.000001;
	final static int Citerations = 20;
	final int CNbArgs = 4;

	/**
	 * Constructeur avec valeurs par défaut
	 */
	public BiomorpheAdvanced() {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
		this.nbArgs = this.CNbArgs;
		this.enterArgs = new double[this.nbArgs];
		this.enterArgs[0] = 1.5; // constante
		this.enterArgs[1] = 0; // fonction 1
		this.enterArgs[2] = 0; // Ajouter, soustraire [...]
		this.enterArgs[3] = 7; // fonction 2
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

	private ComplexeCartesien callFonction(ComplexeCartesien zin,
			ComplexeCartesien zfrom, int arg) {
		switch (arg) {
		case 0: // carré
			zin = zfrom.carre();
			break;
		case 1:
			zin = zfrom.cube();
			break;
		case 8:
			zin = zfrom.cos();
			break;
		case 9:
			zin = zfrom.sin();
			break;
		case 10:
			zin = zfrom.tan();
			break;
		case 11:
			zin = zfrom.exp();
			break;
		default:
			int p = (int) this.enterArgs[1] + 2;
			zin = zfrom.pow(p);
			break;
		}
		return zin;
	}

	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		// dépend du choix de la liste, paar défault carré
		z[t][i] = this.callFonction(z[t][i], z[t][i - 1],
				(int) this.enterArgs[1]);
		
		switch ((int) this.enterArgs[2]) {
		case 0:
			z[t][i] = z[t][i].add(this.callFonction(z[t][i], z[t][i - 1],
					(int) this.enterArgs[3]));
			break;
		default:
			z[t][i] = z[t][i].soustraire(this.callFonction(z[t][i], z[t][i - 1],
					(int) this.enterArgs[3]));
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
