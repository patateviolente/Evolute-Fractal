package fract.opt;

import java.util.ArrayList;
import java.util.Iterator;
import fract.color.ColorAdvanced;

/**
 * <p>
 * Stock dans un ArrayList les éléments de couleurs avancées (ColorAdvanced,
 * utilisées par ChooseRampColor) pour <b>enregistrer un dégradé ou un ensemble
 * de couleurs quelconques</b> et propose notemment de les <b>retourner dans un
 * tableau simple</b>, en plus de :
 * </p>
 * <ul>
 * <li>Retourner le <b>nombre de couleurs</b>.</li>
 * <li>Ajouter une couleurs (une à la fois).</li>
 * <li><b>Comparer un ensemble de couleurs</b> à celle enregistrées dans
 * l'ensemble.</li>
 * <li>...</li>
 * </ul>
 */
final public class StructSaveColor {

	/**
	 * <p>
	 * Constructeur initialisant l'arraylist avec un nom (de dégradé,
	 * <i>facultatif</i>) et un identifiant de vue attribué <i>facultatif</i>.
	 * </p>
	 * 
	 * @param nom
	 *            Surnom du dégradé
	 * @param madeFor
	 *            id saveVue
	 */
	public StructSaveColor(final String nom, final int madeFor) {
		this.nom = nom;
		this.madeFor = madeFor;
		this.al = new ArrayList<ColorAdvanced>();
	}

	/**
	 * <p>
	 * Ajoute un élément de type ColorAdvanced dans l'ArrayList.
	 * </p>
	 * 
	 * @param ca
	 *            Type ColorAdvanced à ajouter
	 */
	public void addItem(final ColorAdvanced ca) {
		this.al.add(ca);
	}

	/**
	 * @return le nombre de couleurs enregistrées.
	 */
	public int getNbColor() {
		return this.al.size();
	}

	/**
	 * @return un tableau de type ColorAdvanced : toutes les couleurs du
	 *         dégradé.
	 */
	public ColorAdvanced[] getArrayOfColorAdvanced() {
		ColorAdvanced[] ca = new ColorAdvanced[this.al.size()];
		it = this.al.iterator();
		int i = 0;
		while (it.hasNext())
			ca[i++] = it.next();
		return ca;
	}

	/**
	 * @param col
	 *            Tableau de couleurs à comparer
	 * @return vrai si le tableau de couleurs avancé en argument est exactement
	 *         le même que celui de la structure de l'objet.
	 */
	public boolean isSameColors(final ColorAdvanced[] col) {
		int i = 0;
		if (col.length < this.al.size())
			return false;
		it = this.al.iterator();
		while (it.hasNext()) {
			if (!it.next().equals(col[i++]))
				return false;
		}
		return true;
	}

	/**
	 * @return Nom donné au dégradé.
	 */
	public String getName() {
		return this.nom;
	}

	/**
	 * @return L'id du fractal auquel le dégradé à été de préférence enregistré.
	 */
	public int getMadeFor() {
		return this.madeFor;
	}

	// variables de stockage
	private String nom;
	private int madeFor;
	private Iterator<ColorAdvanced> it;
	private ArrayList<ColorAdvanced> al;
}
