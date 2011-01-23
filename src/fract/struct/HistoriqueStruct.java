package fract.struct;

/**
 * Class enregistrant les données nécessaire à l'identification d'une étape
 * passé : position, échelle, couleurs, ...
 */
public class HistoriqueStruct {
	/**
	 * Le constructeur enregistre les données nécessaires.
	 * 
	 * @param posX
	 *            Abcisse point supérieur gauche
	 * @param posY
	 *            Ordonnée point supérieur gauche
	 * @param scale
	 *            Echelle
	 */
	public HistoriqueStruct(double posX, double posY, double scale) {
		this.posX = posX;
		this.posY = posY;
		this.scale = scale;
	}

	/**
	 * @return Abcisse du point supérieur gauche.
	 */
	public double getPosX() {
		return this.posX;
	}

	/**
	 * @return Ordonnée du point supérieur gauche.
	 */
	public double getPosY() {
		return this.posY;
	}

	/**
	 * @return Echelle.
	 */
	public double getScale() {
		return this.scale;
	}

	// données
	private double posX, posY, scale;
}
