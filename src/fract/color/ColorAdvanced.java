package fract.color;

import java.awt.Color;

/**
 * <p>
 * Structure de couleur destinée à être instanciée par fract.ChooseColorRamp
 * pour retenir la position dans un dégradé, une couleur, son 'poid'...
 * </p>
 */
final public class ColorAdvanced {

	/**
	 * <p>
	 * <b>Constructeur complet</b> pour demander couleur, position, poid. La
	 * couleur est un type java.awt.Color, la position est un float de 0 à 100,
	 * le poid est la position en pourcentage da la valeur de couleur centrale
	 * entre la couleur et celle de droite.
	 * </p>
	 * 
	 * @param c
	 *            Objet Color
	 * @param pos
	 *            Position dans la règle
	 * @param poid
	 *            Poid de la couleur
	 */
	public ColorAdvanced(final Color c, final float pos, final float poid) {
		this.c = c;
		this.position = this.formatPourcentage(pos);
		this.poid = this.formatPourcentage(poid);
	}

	/**
	 * <p>
	 * Constructeur identique au constructeur complet avec un poid par défaut au
	 * centre (50).
	 * </p>
	 * 
	 * @param c
	 *            Objet Color
	 * @param pos
	 *            Position de la couleur sur la règle
	 */
	public ColorAdvanced(final Color c, final float pos) {
		this.c = c;
		this.position = pos;
		this.poid = 50f;
	}

	/**
	 * Affiche l'objet de la façon : <br />
	 * <p style="font-family:courier;"> ColorAdvanced [r=dd;g=dd;b=dd] ;
	 * position=double% ; poid = double </p>
	 */
	public String toString() {
		return "ColorAdvanced [r=" + this.c.getRed() + ";g="
				+ this.c.getGreen() + ";b=" + this.c.getBlue()
				+ "] ; position=" + this.getPosition() + "% ; poid = "
				+ this.getPoid() + "\n";
	}

	/**
	 * <p>
	 * Affiche avec un System.out.print du toString() de l'objet.
	 * </p>
	 */
	public void afficher() {
		System.out.print(this);
	}

	/**
	 * <p>
	 * Formate le pourcentage en entrée pour qu'il doit bien compris entre 0 et
	 * 100 grace au modulo et en prenant la valeur absolue. Ce qui permet
	 * d'éviter les valeurs innatendues.
	 * </p>
	 * 
	 * @param valeur
	 *            Valeur (poid, position) quelconque à formater
	 */
	private float formatPourcentage(final float valeur) {
		if (valeur < 0)
			return 0f;
		else if (valeur > 100)
			return 100f;
		return valeur;
	}

	/*
	 * Toutes les méthode SET/GET
	 */

	/**
	 * @return couleur Mémorisée
	 */
	public Color getColor() {
		return this.c;
	}

	/**
	 * Modifie la couleur.
	 * 
	 * @param c
	 *            Objet Color
	 */
	public void setColor(final Color c) {
		this.c = c;
	}

	/**
	 * @return position en pourcentage
	 */
	public float getPosition() {
		return this.position;
	}

	/**
	 * <p>
	 * Modifie la position de la structure par celle en argument (donner valeur
	 * entre 0 et 100). La valeur sera vérifiée.
	 * </p>
	 * 
	 * @param position
	 *            Nouvelle position
	 */
	public void setPosition(final float position) {
		this.position = this.formatPourcentage(position);
	}

	/**
	 * @return poid de l'élément depuis son centre située sur sa droite.
	 */
	public float getPoid() {
		return this.poid;
	}

	/**
	 * <p>
	 * Modifie le poid de la structure par celui en argument (donner valeur
	 * entre 0 et 100, la valeur sera vérifiée).
	 * </p>
	 * 
	 * @param poid
	 *            Nouveau poid
	 */
	public void setPoid(final float poid) {
		this.poid = this.formatPourcentage(poid);
	}

	// données de stockage couleur
	private Color c;
	private float position, poid;
}
