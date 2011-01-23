package fract.opt;

import java.awt.Color;

/**
 * <p>
 * Stock toutes les informations nécessaires d'une vue telle qu'elle a été
 * enregistrée avec <i>[id, nom, fractal, id du dégradé de couleurs, couleurs
 * (fond et couleurs alternées), itérations, coordonnées, taille originale,
 * arguments du fractal]</i> et dans un tableau de double les arguments
 * supplémentaires au fractal qui sont plus ou moins nombreux selon le fractal.
 * </p>
 */
final public class StructSaveVue {
	/**
	 * Constructeur, instancie toutes les valeurs nécessaires en arguments.
	 * 
	 * @param nom
	 *            Surnom de la vue
	 * @param fract
	 *            Nom du fractal
	 * @param ramp
	 *            Id du dégradé de couleurs
	 * @param bg
	 *            Couleur de fond
	 * @param alt1
	 *            Couleur alt1
	 * @param alt2
	 *            Couleur alt2
	 * @param iter
	 *            Nombre d'itérations
	 * @param scale
	 *            Echelle
	 * @param posX
	 *            Abcisse point supérieur gauche
	 * @param posY
	 *            Ordonnée point supérieur gauche
	 * @param width
	 *            longueur de la composition
	 * @param height
	 *            hauteur de la composition
	 * @param args
	 *            Tableau d'arguments du fractal
	 */
	public StructSaveVue(final String nom, final String fract, final int ramp,
			final Color bg, final Color alt1, final Color alt2,
			final boolean altActiv, final int iter, final double scale,
			final double posX, final double posY, final int width,
			final int height, final double[] args) {
		this.nom = nom;
		this.nomFract = fract;
		this.intVal[0] = ramp;
		this.colorVal[0] = bg;
		this.colorVal[1] = alt1;
		this.colorVal[2] = alt2;
		this.altActiv = altActiv;
		this.intVal[2] = iter;
		this.doubleVal[0] = scale;
		this.doubleVal[1] = posX;
		this.doubleVal[2] = posY;
		this.intVal[3] = width;
		this.intVal[4] = height;
		this.args = args;
	}

	/**
	 * <p>
	 * Retourne une valeur entière <i>[idcol, fract, iter, width, height]</i>.
	 * </p>
	 * 
	 * @param i
	 *            Indice valeur
	 * @return valeur entière concernée dans l'énumaration.
	 */
	public int getIntValue(final EnumVue i) {
		return this.intVal[i.ordinal()];
	}

	/**
	 * <p>
	 * Retourne une valeur double <i>[scale, posX, posY]</i>.
	 * </p>
	 * 
	 * @param i
	 *            Indice valeur
	 * @return valeur à précision réelle concernée dans l'énumaration.
	 */
	public double getDoubleValue(final EnumVue i) {
		return this.doubleVal[i.ordinal() - 5];
	}

	/**
	 * <p>
	 * Retourne un objet de type Color <i>[bg, alt1, alt2]</i>.
	 * </p>
	 * 
	 * @param i
	 *            Indice valeur
	 * @return couleur concerne dans l'énumération.
	 */
	public Color getColorValue(final EnumVue i) {
		return this.colorVal[i.ordinal() - 8];
	}

	/**
	 * @return nom donné à la vue.
	 */
	public String getName() {
		return this.nom;
	}

	/**
	 * @param tabOffset
	 *            Position dans le tableau de l'argument
	 * @return l'argument supplémentaire à la position donnée.
	 */
	public double getArg(final int tabOffset) {
		return this.args[tabOffset];
	}

	/**
	 * @return vrai si l'alternance des couleurs est activée sur la vue.
	 */
	public boolean isColorAltern() {
		return this.altActiv;
	}

	/**
	 * <p>
	 * Retourne une <b>description rapide</b> du type :
	 * </p>
	 * <span style="font-family: 'Courier New', Courier, monospace;">à
	 * compléter</span>
	 */
	public String toString() {
		return "VUE \""
				+ this.getName()
				+ "\" fract[("
				+ this.intVal[1]
				+ ") - "
				+ this.intVal[2]
				+ "itér.] - Couleur["
				+ ((!this.altActiv) ? "fond=(" + this.colorVal[0].getRGB()
						+ ") + dégradé(" + this.intVal[0] + ")"
						: " Couleurs alt. (" + this.colorVal[1].getRGB()
								+ ")&(" + this.colorVal[2].getRGB() + ")")
				+ "] - Coord[(sc=" + this.doubleVal[0] + ";posX="
				+ this.doubleVal[1] + ";posY=" + this.doubleVal[2]
				+ ") - compo[" + this.intVal[3] + "x" + this.intVal[4] + "]]";
	}

	/**
	 * @return le tableau d'argument sous la forme '{[778.9][56.8E89][...]}'
	 */
	private String getFormattedArguments() {
		StringBuffer sb = new StringBuffer("{");
		if (this.args != null)
			for (int i = 0; i < this.args.length; i++)
				sb.append("[" + this.args[i] + "]");
		return sb.toString() + "}";
	}

	/**
	 * <p>
	 * Converti l'objet au format exacte pour correspondre au fichier de
	 * configuration du type:<br />
	 * <span style="font-family: 'Courier New', Courier, monospace;"> $>id=INT
	 * ~name="STRING" -fract=STRING £ColorRamp=INT
	 * -mainColors{bg:DOUBLE;-1_&_DOUBLE-alt=OFF} #iter=INT €coord={scl:DOUBLE
	 * -posX:DOUBLE-posY:DOUBLE__initSize=INTxINT}
	 * -_-extraFractArg={[DOUBLE]}</span>
	 * </p>
	 * <p>
	 * L'argument en entrée est inséré en tant qu'identifiant dans la ligne
	 * générée.
	 * </p>
	 * 
	 * @param id
	 *            Identifiant donner
	 * @return ligne formatée
	 */
	public String getFormattedConfigLine(final int id) {
		return "$>id=" + id + " ~name=\"" + this.getName() + "\" -fract="
				+ this.nomFract + " £ColorRamp="
				+ this.getIntValue(EnumVue.idramp) + " -mainColors{bg:"
				+ this.getColorValue(EnumVue.background).getRGB() + ";"
				+ this.getColorValue(EnumVue.alt1).getRGB() + "_&_"
				+ this.getColorValue(EnumVue.alt2).getRGB() + "-alt="
				+ (this.isColorAltern() ? "ON" : "OFF") + "} #iter="
				+ this.getIntValue(EnumVue.iter) + " €coord={scl:"
				+ this.getDoubleValue(EnumVue.scale) + "-posX:"
				+ this.getDoubleValue(EnumVue.posX) + "-posY:"
				+ this.getDoubleValue(EnumVue.posY) + "__initSize="
				+ this.getIntValue(EnumVue.width) + "x"
				+ this.getIntValue(EnumVue.height) + "} -_-extraFractArg="
				+ getFormattedArguments();
	}

	/**
	 * <p>
	 * Retourne le nom du fractal <i>(ex : Mandelbrot)</i> qui est le nom de la
	 * classe sans l'extension.
	 * </p>
	 * 
	 * @return nom du fractal
	 */
	public String getFractalName() {
		return this.nomFract;
	}

	/**
	 * @return tableau d'arguments de doubles
	 */
	public double[] getArrayArgs() {
		return this.args;
	}

	// variables stockage
	final private int[] intVal = new int[5];// idcol, fract, iter, width,
	// height;
	final private String nom, nomFract;
	final private Color[] colorVal = new Color[3]; // bg, alt1, alt2;
	final private boolean altActiv;
	final private double[] doubleVal = new double[3]; // scale, posX, posY,
	final private double args[];
}
