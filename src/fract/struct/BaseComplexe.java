package fract.struct;

/**
 * Cette class abstraite définie quelques fonctions similaires aux différentes
 * représentation complexes (polaire, cartésienne).
 */
public abstract class BaseComplexe implements ModelComplexe {

	/**
	 * Opération pour la série de test N°1
	 * 
	 * @param n
	 *            Nombre à comparer (itérations)
	 */
	public boolean isReImgOrModUpTo(int n) {
		return (Math.abs(this.getRe()) > n || (Math.abs(this.getImg())) > n || this
				.getMod() > n) ? true : false;
	}

	/**
	 * Opération pour la série de test N°2
	 * 
	 * @param n
	 *            Nombre à comparer (itérations)
	 */
	public boolean isReOrImgDownTo(int n) {
		return (Math.abs(this.getRe()) < n || (Math.abs(this.getImg())) < n) ? true
				: false;
	}

	/**
	 * @return l'angle en radians
	 */
	public double getAngle() {
		return this.angle;
	}

	/**
	 * A surdéfinir
	 * 
	 * @param angle
	 *            Angle en radians
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/*
	 * Description dans l'interface
	 */
	public void setImg(double img) {
		this.img = img;
	}

	/*
	 * Description dans l'interface
	 */
	public void setRe(double re) {
		this.re = re;
	}

	double re, img, angle;

}
