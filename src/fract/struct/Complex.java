package fract.struct;


/**
 * Calcul les oprérations mathématique des nombre complexes
 * 
 * @author Patrick Portal - Fabrice Antoine
 *
 */
public class Complex {

    private final double re;   // the real part
    private final double im;   // the imaginary part
	
    /**
	 * Le constructeur créer et initiale un objet de type complexe donné
	 * @param re 
	 * 				Nombre réelle
	 * @param im
	 * 				Nombre Imaginaire
	 */
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }
    
    /**
     * Retroune le nombre complex en représentation de caractères
     * @return partie réelle + partie imaginaire
     */
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }
    /**
     * Retourne le module et l'argument du nombre complex
     * 
     * @return module / phase
     */

    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    public double phase() { return Math.atan2(im, re); }  // entre -pi et pi

    /**
     * Retourne la somme de deux objets complexes
     * @param b
     * 				Complexe
     * @return Complexe
     */
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }
    
    /**
     * Retourne la différence de deux objets complexes
     * @param b
     * 				Complexe
     * @return Complexe
     */
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }
    
    /**
     * Retourne la multiplication de deux objets complexes
     * @param b
     * 				Complexe
     * @return Complexe
     */
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }
    
    /**
     * Retourne le scalaire d'un objet complexe et d'un constant
     * @param alpha
     * 					Constant double
     * @return Complexe
     */
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }
    
    /**
     * Retourne la conjuguée d'un objet complexe
     * @return Complexe
     */
    public Complex conjugate() {  return new Complex(re, -im); }
    
    /**
     * Retourne la reciproque d'un objet complexe
     * @return Complexe
     */
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }
    
    /**
     * Retourne la partie réelle et imaginaire d'un complexe
     * @return Partie réelle & Partie imaginaire
     */
    public double re() { return re; }
    public double im() { return im; }

    /**
     * Retourne la division de deux objets complexes
     * @param b
     * 				Complexe
     * @return Complexe
     */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }
    /**
     * Retourne l'objet complexe en exponentielle
     * @return Complexe
     */
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }
    
    /**
     * Retourne l'objet complexe en sinus
     * @return Complexe
     */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }
    
    /**
     * Retourne l'objet complexe en cosinus
     * @return Complexe
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }
    
    /**
     * Retourne l'objet complexe en tangente
     * @return Complexe
     */
    public Complex tan() {
        return sin().divides(cos());
    }
    
    /**
     * Retourne la somme de deux nouveaux objets complexes
     * Version statique
     * @param a
     * 				Complexe
     * @param b
     * 				Complexe
     * @return Complexe
     */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }
    
    /**
     * Retourne l'objet complexe à sa puissance nième
     * @param n
     * 				Constant entier >= 0
     * @return
     */
	public Complex puissance (int n){
		Complex p = new Complex (1.0, 0.0);
		for (int i=1; i<=n; i++){
			p = times(p);
		}
		return p;		
	}
	
	/**
	 * Methode principale pour test
	 * @param args
	 * 					Arguments en entrée (options)
	 */
    public static void main(String[] args) {
        Complex a = new Complex(5.0, 6.0);
        Complex b = new Complex(-3.0, 4.0);

        System.out.println("a            = " + a);
        System.out.println("b            = " + b);
        System.out.println("Re(a)        = " + a.re());
        System.out.println("Im(a)        = " + a.im());
        System.out.println("b + a        = " + b.plus(a));
        System.out.println("a - b        = " + a.minus(b));
        System.out.println("a * b        = " + a.times(b));
        System.out.println("b * a        = " + b.times(a));
        System.out.println("a / b        = " + a.divides(b));
        System.out.println("(a / b) * b  = " + a.divides(b).times(b));
        System.out.println("conj(a)      = " + a.conjugate());
        System.out.println("|a|          = " + a.abs());
        System.out.println("tan(a)       = " + a.tan());
        System.out.println("b*b*b        = " + b.puissance(3));
    }

}
