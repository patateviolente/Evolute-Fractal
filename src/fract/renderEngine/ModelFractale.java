package fract.renderEngine;

/**
 * Interface Fractale simplifiée pour le moteur de rendu. N'exige que les
 * méthodes de calculs.
 */
public interface ModelFractale {

	/**
	 * <b>Etape de calcul</b> qui se situe juste avant la boucle d'itération. En
	 * général cette étape sert à initialiser la valeur par défaut des variables
	 * avant calcul.
	 * 
	 * @param thread
	 *            Numéro de processus
	 * @param x
	 *            Abcisse dans la grille
	 * @param y
	 *            Ordonnée dans la grille
	 */
	public void calculateStepZZero(int thread, int x, int y);

	/**
	 * <b>Etape de calcul</b> qui se situe dans la boucle d'itération. Cette
	 * étape sert à faire le calcu sur les variables.
	 * 
	 * @param thread
	 *            Numéro de processus
	 * @param i
	 *            Itération actuelle
	 * @param x
	 *            Abcisse dans la grille
	 * @param y
	 *            Ordonnée dans la grille
	 */
	public void calculateStepPowAndCst(int thread, int i, int x, int y);

	/**
	 * <b>Etape de calcul</b> : condition d'arrêt du calcul. Si la condition est
	 * respectée alors le calcul s'arrête et la couleur peut être déterminée.
	 * 
	 * @param thread
	 *            Numéro de processus
	 * @param i
	 *            Itération actuelle
	 * @return condition respectée
	 */
	public boolean calculateStepGetCondition(int thread, int i);
}
