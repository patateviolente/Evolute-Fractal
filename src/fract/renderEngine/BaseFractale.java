package fract.renderEngine;

import fract.algo.ModelFractale;

/**
 * Classe de calcul minimaliste pour rendre les calculs demandés.<br >
 * Il n'y a pas de gestion des threads ici, le calcul est lancé et c'est à la
 * machine appelante de gérer les flux.
 * 
 */
public abstract class BaseFractale extends Thread implements ModelFractale {

	public BaseFractale(double posX, double posY, double scale, int iterations) {
		this.posX = posX;
		this.posY = posY;
		this.scale = scale;
		this.iterations = iterations;
	}

	/**
	 * Renvoie une longue chaîne sur le flux standard du type :<br />
	 * 4;7;2;X;X;X;67;34...\n<br />
	 * 3X;2;1;12;X;1;4...\n<br />
	 * ...<br />
	 * <p>
	 * Ce flux est en fait le <u>nombre d'itérations trouvés par colonnes
	 * séparés d'un point virgule</u>. Un X signifie aucun résultat (le maximum
	 * et donc la couleur par défaut). Une nouvelle colonne calculée est séparée
	 * par un saut de ligne.
	 * </p>
	 * 
	 * @param debut
	 *            Abcisse départ X
	 * @param fin
	 *            Abcisse fin X
	 * @param col
	 *            Hauteur en pixels
	 * @param preci
	 *            Précision (saut) en pixels
	 */
	public void launchCalcul(int debut, int fin, int col, int preci) {
		StringBuffer sb;
		for (int x = debut; x < fin; x += preci) {
			sb = new StringBuffer();
			for (int y = 0; y < col; y += preci) {
				finTests: if (true) {
					this.calculateStepZZero(0, x, y);
					for (int i = 1; i < iterations; i++) // suite récurrence
					{
						this.calculateStepPowAndCst(0, i, x, y);
						// test sur l'élément de la suite calculé
						if (this.calculateStepGetCondition(0, i)) {
							sb.append(i + ";");
							break finTests;
						}

					}
					sb.append("-1;"); // fin de chiffre
				}
			}
			System.out.println(CompressionRendu.compress(sb.toString()));

		}
	}

	/**
	 * Change le nombre d'itérations par celui en argument.
	 * 
	 * @param i
	 *            nombre d'iterations
	 */
	public void setIterations(int i) {
		this.iterations = i;
	}

	/**
	 * Change les coordonnées principales : la position et l'échelle.
	 * 
	 * @param posX
	 *            Abcisse point supérieur gauche
	 * @param posY
	 *            Ordonnée point supérieur gauche
	 * @param scale
	 *            Echelle
	 */
	public void changeCoordAndScale(double posX, double posY, double scale) {
		this.posX = posX;
		this.posY = posY;
		this.scale = scale;
	}

	/**
	 * Insère dans le fractal les arguments (toujours du type double) formatés
	 * de la façon suivant DOUBLE;DOUBLE:... dans le fractal
	 * 
	 * @param arg
	 *            L'argument formatés
	 */
	public void decryptFormatedArgs(String arg) {
		if (arg == null || arg.length() == 0)
			return;
		System.out.println("#######" + arg);
		String[] s = arg.split(":");
		for (int i = 0; i < s.length; i++) {
			if (s[i].length() == 0)
				break;
			else {
				this.modifyFractArgument(i, Double.parseDouble(s[i]));
				System.out.println(s[i]);
			}
		}
		System.out.println("######");
	}

	/**
	 * <p>
	 * Modifie l'argument d'un fractal à une position donnée. Attention, ne
	 * revoie pas d'exception en cas de dépassement de tableau.
	 * </p>
	 * 
	 * @param offset
	 *            Position dans le tableau d'arguments
	 * @param value
	 *            Nouvelle valeur
	 */
	private void modifyFractArgument(int offset, double value) {
		this.enterArgs[offset] = value;
	}

	/**
	 * Impose une constante d'angle qui sera effective dans les prochains
	 * calculs.
	 * 
	 * @param angle
	 *            Angle en radians
	 */
	public void setAngle(double angle) {
		this.angle = angle / 2;
	}

	// variables globales
	protected double posX, posY, scale, angle = 0;
	protected int nbArgs = 0;
	protected double[] enterArgs;
	protected double decX = 0, decY = 0;

	// variables de calcul
	protected boolean recalculateAll;

	// variables pour le calcul
	protected int iterations = 10;
}
