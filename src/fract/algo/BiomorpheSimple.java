package fract.algo;

import java.awt.Color;

import fract.ihm.MainWindow;
import fract.algo.BaseFractale;
import fract.struct.*;

/**
 * Biomorphes (réccursivitée dans les complexe) avec possibilités d'option :
 * fonction et constante paramétrable.
 */
public class BiomorpheSimple extends BaseFractale {
	final static double CposX = -9.4, CposY = -10.5, CscaleMin = 0.1,
			CscaleMax = 0.000001;
	final static int Citerations = 20;
	final int CNbArgs = 2;

	/**
	 * Constructeur avec valeurs par défaut
	 * 
	 * @param mw
	 */
	public BiomorpheSimple(MainWindow mw) {
		// coordonnées par défaut du fractal (le plus loin mais pas trop)
		super(mw, CposX, CposY, CscaleMin, Citerations);
		initializeVariablesForXThreads(1); // au moins 1 thread
		this.bg = Color.white;
		this.nbArgs = this.CNbArgs;
		this.enterArgs = new double[this.nbArgs];
		this.enterArgs[0] = 1.5;
		this.enterArgs[1] = 0;
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
		z = new ComplexeCartesien[n][];
		for (int i = 0; i < n; i++)
			z[i] = new ComplexeCartesien[iterations];
	}

	public void calculateStepZZero(int t, int x, int y) {
		z[t][0] = new ComplexeCartesien(posX + scale * x, posY + scale * y);
	}

	public void calculateStepPowAndCst(int t, int i, int x, int y) {
		// dépend du choix de la liste, paar défault carré
		switch ((int) this.enterArgs[1]) {
		case 0: // carré
			z[t][i] = z[t][i - 1].carre();
			break;
		case 1:
			z[t][i] = z[t][i - 1].cube();
			break;
		case 8:
			z[t][i] = z[t][i - 1].cos();
			break;
		case 9:
			z[t][i] = z[t][i - 1].sin();
			break;
		case 10:
			z[t][i] = z[t][i - 1].tan();
			break;
		case 11:
			z[t][i] = z[t][i - 1].exp();
			break;
		default:
			int p = (int) this.enterArgs[1] + 2;
			z[t][i] = z[t][i - 1].pow(p);
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
