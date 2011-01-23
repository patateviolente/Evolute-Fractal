package fract.opt;

import java.util.HashMap;
import java.util.Iterator;

/**
 * <p>
 * HashMap contenant tous les presets de couleurs. Les clés de la map son de
 * type entier et son l'identifiant unique d'une vue. Cette structure propose
 * les actions suivantes :
 * <ul>
 * <li><b>Ajouter</b> une vue (nom et Structure de vue exigés)</li>
 * <li><b>Obtenir une vue</b> à un identifiant (unique) donné</li>
 * <li>Obtenir le <b>nombre de vues</b></li>
 * <li>Obtenir le <b>plus grand id</b>entifiant (id)</li>
 * <li>Obtenir un <b>tableau de structures</b> de vues</li>
 * <li>...</li>
 * </ul>
 * </p>
 */
final public class TableauSaveVue {
	/**
	 * <p>
	 * Le constructeur initialise la hashmap.
	 * </p>
	 */
	public TableauSaveVue() {
		this.hm = new HashMap<Integer, StructSaveVue>();
	}

	/**
	 * <p>
	 * Ajouter un élément par son numéro d'identification, son nom et l'id de la
	 * fonction pour laquelle elle a été créé.
	 * </p>
	 * 
	 * @param id
	 *            Id de la vue
	 * @param struct
	 *            Structure de stockage de la vue
	 */
	public void ajouter(final int id, final StructSaveVue struct) {
		this.hm.put(id, struct);
	}

	/**
	 * @return le nombre d'éléments dans la map.
	 */
	public int getNbValues() {
		return this.hm.size();
	}

	/**
	 * @return le plus grand identifiant des vues enregistré.
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
	 * @param id
	 *            Id de la vue
	 * @return la structure de la vue à l'id donné
	 */
	public StructSaveVue getVueAtId(final int id) {
		return this.hm.get(id);
	}

	/**
	 * @return un tableau de tous les éléments de stockage de dégradé.
	 */
	public StructSaveVue[] getArrayOfStructSaveVue() {
		StructSaveVue ssc[] = new StructSaveVue[this.hm.size()];
		it = this.hm.keySet().iterator();
		int i = 0;
		while (it.hasNext())
			ssc[i++] = this.hm.get(it.next());
		return ssc;
	}

	/**
	 * <p>
	 * Affiche un tableau des options enregistrées du type :
	 * </p>
	 * <span style="font-family: 'Courier New', Courier, monospace;"><br />
	 * ' ' ' ' ' ' V U E S ' ' E N R E G I S T R E E S<br />
	 * ╔═══════╤═════════════════════════════════════════════════════════<br />
	 * ║ Id ' '│ Description <br />
	 * ╠═══════╪═════════════════════════════════════════════════════════<br />
	 * ║ 2 ' ' │ affichage par défaut d'une vue (toString)<br />
	 * ╚═════════════════════════════════════════════════════════════════
	 * </span>
	 */
	public void afficher() {
		final byte[] tailleCase = { 6 }; // taille des cases
		byte spc[] = new byte[1];
		String esp[] = new String[1];
		StructSaveVue ssv;
		Integer clef;
		System.out
				.println("                    V U E S   E N R E G I S T R E E S      \n"
						+ "        ╔═══════╤═════════════════════════════════════════════════════════\n"
						+ "        ║ Id    │ Description  \n"
						+ "        ╠═══════╪═════════════════════════════════════════════════════════");
		this.it = hm.keySet().iterator();

		while (it.hasNext()) {
			clef = it.next();
			ssv = this.hm.get(clef);
			// calcul des espaces
			spc[0] = (byte) (tailleCase[0] - clef.toString().length());
			esp[0] = "";
			for (int u = 0; u < spc[0]; u++)
				esp[0] += " ";
			// on peut afficher
			System.out.println("        ║ " + clef + esp[0] + "│ " + ssv);
		}
		System.out
				.println("        ╚═════════════════════════════════════════════════════════════════");
	}

	// variables
	private Iterator<Integer> it;
	private HashMap<Integer, StructSaveVue> hm;
}
