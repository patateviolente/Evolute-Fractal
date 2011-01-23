package fract.struct;

/**
 * Représente l'objet mathématique des Complexe donne la possibilité de stocker
 * et faire des calculs complexes
 */
public class ComplexeCartesien extends BaseComplexe implements ModelComplexe {
	/**
	 * Constructeur pour déclarer un nouveau complexe dans un repère cartésien ;
	 * c'est à dire en entrant les deux parties réelle puis imaginaire.
	 * 
	 * @param re
	 *            Partie réelle
	 * @param img
	 *            Partie imaginaire
	 */
	public ComplexeCartesien(final double re, final double img) {
		this.re = re;
		this.img = img;
	}

	/**
	 * Constructeur simple qui initialise la partie réelle et imaginaire de
	 * l'objet à 0.
	 */
	public ComplexeCartesien() {
		this.re = 0d;
		this.img = 0d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fract.struct.ModelComplexe#getRe()
	 */
	public double getRe() {
		return this.re;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fract.struct.ModelComplexe#getImg()
	 */
	public double getImg() {
		return this.img;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "z = " + re + " + " + img + "i";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fract.struct.ModelComplexe#getMod()
	 */
	public double getMod() {
		return Math.sqrt(this.getModCarre());
	}

	/**
	 * Retourne le module mis au carré.
	 * 
	 * @return Module²
	 */
	public double getModCarre() {
		return re * re + img * img;
	}

	/**
	 * @return complexe au carré.
	 */
	public ComplexeCartesien carre() {
		return this.pow(2);
	}

	/**
	 * @return complexe au cube
	 */
	public ComplexeCartesien cube() {
		return this.pow(3);
	}

	// return a new Complex object whose value is (this * b)
	public ComplexeCartesien times(ComplexeCartesien b) {
		ComplexeCartesien a = this;
		double real = a.re * b.re - a.img * b.img;
		double imag = a.re * b.img + a.img * b.re;
		return new ComplexeCartesien(real, imag);
	}

	public ComplexeCartesien pow(int n) {
		ComplexeCartesien p = new ComplexeCartesien(1d, 0d);
		while(n-- > 0)
			p = times(p);
		return p;
	}

	/**
	 * @return complexe passé au sinus
	 */
	public ComplexeCartesien sin() {
		final double e1 = Math.exp(this.getImg()), e2 = 1 / e1;
		return new ComplexeCartesien(Math.sin(this.getRe()) * (e1 + e2) / 2,
				Math.cos(this.getRe()) * (e1 - e2) / 2);
	}

	/**
	 * @return complexe cosinus
	 */
	public ComplexeCartesien cos() {
		final double e1 = Math.exp(this.getImg()), e2 = 1 / e1;
		return new ComplexeCartesien(Math.cos(this.getRe()) * (e1 + e2) / 2,
				-Math.sin(this.getRe()) * (e1 - e2) / 2);
	}

	/**
	 * @return complexe tangante
	 */
	public ComplexeCartesien tan() {
		final double e1 = Math.exp(this.getImg()), e2 = 1 / e1;
		return new ComplexeCartesien(Math.tan(this.getRe()) * (e1 + e2) / 2,
				-Math.tan(this.getRe()) * (e1 - e2) / 2);
	}

	/**
	 * @return complexe exponentiel
	 */
	public ComplexeCartesien exp() {
		final double e1 = Math.exp(this.getRe());
		return new ComplexeCartesien(e1 * Math.cos(this.getImg()), e1
				* Math.sin(this.getImg()));
	}

	/**
	 * Multiplie la partie réelle et imaginaire par le facteur en entrée.
	 * 
	 * @param cst
	 *            Le facteur
	 * @return le nouveau coplexe.
	 */
	public ComplexeCartesien facteur(final double cst) {
		return new ComplexeCartesien(this.re * cst, this.img * cst);
	}

	/**
	 * Ajoute un complexe cartésien.
	 * 
	 * @param p
	 *            Complexe à ajouter
	 * @return nouveau complexe additionné
	 */
	public ComplexeCartesien add(final ComplexeCartesien p) {
		return new ComplexeCartesien(p.getRe() + re, p.getImg() + img);
	}

	/**
	 * Soustrait un complexe cartésien.
	 * 
	 * @param p
	 *            Complexe à soustraire
	 * @return nouveau complexe soustrait
	 */
	public ComplexeCartesien soustraire(final BaseComplexe p) {
		return new ComplexeCartesien(re - p.getRe(), img - p.getImg());
	}

}
