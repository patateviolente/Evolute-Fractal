package fract.algo;

/**
 * Interface à inclure pour un objet Fractal, comporte le minimum de classe
 * nécessaire à la manipulation d'un Fractal de tout type
 */
public interface ModelFractale {

	/**
	 * Adapter l'échelle à la taille du panneau désirée pour faire correspondre
	 * l'image à celle par défaut ().
	 * 
	 * @param x
	 * @param y
	 */
	public void adaptDefaultCoordToNewDim(final int x, final int y);

	/**
	 * Méthode qui prépare les tableau de variables de calculs selon le nombre
	 * de Threads en argument. Si le calcul se lance en 4 Threads alors un
	 * nombre de 4 en argument est un minimum pour ne pas avoir un
	 * OutOfBoundException.
	 */
	public void initializeVariablesForXThreads(int n);

	/**
	 * <b>Etape de calcul</b> située juste avant la boucle d'itération. En
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
	 * <b>Etape de calcul</b> située dans la boucle d'itération. Cette étape
	 * sert à faire le calcul sur les variables.
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
