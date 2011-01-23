package fract.struct;

public interface ModelComplexe {
	public String toString();
	
	/**
	 * Retourne la partie réelle actuelle
	 * 
	 * @return partie réelle
	 */
	public double getRe();

	/**
	 * Retourne la partie imaginaire actuelle
	 * 
	 * @return partie imaginaire
	 */
	public double getImg();

	/**
	 * Modifie la partie réelle de l'objet
	 * 
	 * @param re Nouvelle partie réelle.
	 */
	public void setRe(double re);

	/**
	 * Modifie la partie imaginaire de l'objet
	 * 
	 * @param img Nouvelle partie imaginaire.
	 */
	public void setImg(double img);

	/**
	 * Retourne le module de l'objet complexe en cours. Le calcul se fait par
	 * racine(re²+img²)
	 * 
	 * @return module de type double
	 */
	public double getMod();
	
	/**
	 * Retourne la puissance de l'objet complexe en cours. Cette fonction
	 * n'écrase pas le complexe en cours ! (non void) Enlever un complexe à la
	 * puissance n ne revient pas à élever la partie réelle et imaginaire à la
	 * puissance n car il faut prendre en compte i.
	 * 
	 * @param n
	 * @return nouveauComplexe
	 */
	public BaseComplexe pow(int n);
}
