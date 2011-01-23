package fract.opt;

import java.util.HashMap;
import java.util.Iterator;

import fract.color.ChooseRampColor;
import fract.color.ColorAdvanced;
import java.awt.Color;

/**
 * <p>
 * HashMap contenant tous les presets de couleurs. Le nom clé est le nom du
 * preset.
 * </p>
 */
public class TableauSaveColor {
	/**
	 * Le constructeur initialise la hashmap et rempli les champs simples.
	 */
	public TableauSaveColor() {
		this.hm = new HashMap<Integer, StructSaveColor>();
	}

	/**
	 * Ajouter un élément par son numéro d'identification, son nom et l'id de la
	 * fonction pour laquelle elle a été créé.
	 * 
	 * @param id
	 *            Identifiant numérique
	 * @param nom
	 *            Surnom du dégradé
	 * @param madeFor
	 *            id de la structure vue attribuée
	 */
	public void ajouter(final int id, final String nom, final int madeFor) {
		this.hm.put(id, new StructSaveColor(nom, madeFor));
	}

	/**
	 * Ajouter un élément complet, le dégradé de couleurs sera converti.
	 * 
	 * @param id
	 *            Identifiant numérique
	 * @param nom
	 *            Surnom du dégradé
	 * @param madeFor
	 *            id de la structure vue attribuée
	 * @param crc
	 *            Dégradé de couleur
	 */
	public void ajouter(final int id, String nom, final int madeFor,
			final ChooseRampColor crc) {
		ColorAdvanced[] ca = crc.getArrayOfColors();
		this.hm.put(id, new StructSaveColor(nom, madeFor));
		for (ColorAdvanced c : ca)
			// ajouts couleurs
			this.addColorData(id, c.getColor().getRGB(), c.getPosition(), c
					.getPoid());

	}

	/**
	 * Ajouter une couleur à un dégradé
	 * 
	 * @param id
	 *            Identifiant numérique du dégradé de couleur
	 * @param couleur
	 *            Couleur (rgb) à ajouter
	 * @param pos
	 *            Sa position
	 * @param poid
	 *            Son poid
	 */
	public void addColorData(final int id, final int couleur, final float pos,
			final float poid) {
		this.hm.get(id).addItem(
				new ColorAdvanced(new Color(couleur), pos, poid));
	}

	/**
	 * @return le nombre d'élément dans la map.
	 */
	public int getNbValues() {
		return this.hm.size();
	}

	/**
	 * @return l'identifiant le plus grand dand la map.
	 */
	public int getLastId() {
		it = this.hm.keySet().iterator();
		int i = 0, u;
		while (it.hasNext()) {
			u = it.next();
			if (u > i)
				i = u;
		}
		return i;
	}

	/**
	 * @return un tableau de tous les élément de stockage de dégradé.
	 */
	public StructSaveColor[] getArrayOfStructSaveColor() {
		StructSaveColor ssc[] = new StructSaveColor[this.hm.size()];
		it = this.hm.keySet().iterator();
		int i = 0;
		while (it.hasNext())
			ssc[i++] = this.hm.get(it.next());
		return ssc;
	}

	/**
	 * @return un tableau de tous les éléments de stockage des couleurs d'un
	 *         dégradé à l'id donné dans la hahmap.
	 * @param id
	 *            Id du dégradé de couleur
	 */
	public StructSaveColor getStructSaveColorAtId(final int id) {
		return this.hm.get(id);
	}

	/**
	 * Retourne l'identifiant du dégradé de couleur ayant exatcement les même
	 * couleurs que le tableau de couleurs en argument. Retourne -1 si aucun
	 * dégradé ne porte ces couleurs.
	 * 
	 * @param ca
	 *            Tableau de couleurs
	 * @return identifiant du dégradé qui est exactement le même que celui
	 *         composé par les couleurs en argument.
	 */
	public int areTheseColorsLooksToAny(final ColorAdvanced[] ca) {
		it = this.hm.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			i = it.next();
			if (this.hm.get(i).isSameColors(ca))
				return i;
		}
		return -1;
	}

	/**
	 * <p>
	 * Affiche un tableau des options enregistrées du type :
	 * </p>
	 * <span style="font-family: 'Courier New', Courier, monospace;"><br />
	 * ' ' ' ' ' ' ' ' ' T A B L E A U ' 'D E S ' 'C O U L E U R S<br />
	 * ' ' ' '
	 * ╔═══════╤══════════════════════╤════════════════╤════════════════╗<br />
	 * ' ' ' ' ║ Id ' '│ Nom du preset ' ' ' '│ De pref. pour '│ Nb de couleurs
	 * ║<br />
	 * ' ' ' '
	 * ╠═══════╪══════════════════════╪════════════════╪════════════════╣<br />
	 * </span>
	 */
	public void afficher() {
		final byte[] tailleCase = { 6, 20, 14, 14 }; // taille des cases
		byte spc[] = new byte[4];
		String esp[] = new String[4];
		StructSaveColor ssc;
		Integer clef;
		System.out
				.println("                    T A B L E A U    D E S    C O U L E U R S      \n"
						+ "        ╔═══════╤══════════════════════╤════════════════╤════════════════╗\n"
						+ "        ║ Id    │ Nom du preset        │  De pref. pour │ Nb de couleurs ║\n"
						+ "        ╠═══════╪══════════════════════╪════════════════╪════════════════╣");
		this.it = hm.keySet().iterator();

		while (it.hasNext()) {
			clef = it.next();
			ssc = this.hm.get(clef);
			// calcul des espaces
			spc[0] = (byte) (tailleCase[0] - clef.toString().length());
			spc[1] = (byte) (tailleCase[1] - ssc.getName().length());
			spc[2] = (byte) (tailleCase[2] - new String(ssc.getMadeFor() + "")
					.length());
			spc[3] = (byte) (tailleCase[3] - new String(ssc.getNbColor() + "")
					.length());
			for (int i = 0; i < 4; i++) {
				esp[i] = "";
				for (int u = 0; u < spc[i]; u++)
					esp[i] += " ";
			}
			// on peut afficher
			System.out.println("        ║ " + clef + esp[0] + "│ "
					+ ssc.getName() + esp[1] + " │ " + ssc.getMadeFor()
					+ esp[2] + " │ " + ssc.getNbColor() + esp[3] + " ║");
		}
		System.out
				.println("        ╚═══════╧══════════════════════╧════════════════╧════════════════╝");
	}

	// variables

	private Iterator<Integer> it;
	private HashMap<Integer, StructSaveColor> hm;
}
