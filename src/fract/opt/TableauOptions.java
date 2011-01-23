package fract.opt;

import java.util.HashMap;
import java.util.Iterator;

import fract.opt.EnumOption.BooleanEnum;
import fract.opt.EnumOption.IntEnum;
import fract.opt.EnumOption.StringEnum;

/**
 * <p>
 * Classe de stockage des options dans une hashmap, qui enregistre des éléments
 * StructOption et les récupère par leur noms. Les clés de la hashmap sont les
 * noms des options.
 * </p>
 * <p>
 * Cette structure permet un large champs de possibilités pour tout post
 * traitement :
 * </p>
 * <ul>
 * <li>Modifier une option.</li>
 * <li>Afficher les options dans un double tableau.</li>
 * <li>Obtenir les options dans un tableau.</li>
 * <li>Obtenir des options par leurs type.</li>
 * <li>...</li>
 * </ul>
 */
final public class TableauOptions {
	public TableauOptions() {
		hm = new HashMap<String, StructOption>();
	}

	/**
	 * <p>
	 * Ajouter un élément avec le nom de l'option, sa valeur <i>(Object)</i> et
	 * son type <i>(un entier prédéfini)</i>.
	 * </p>
	 * 
	 * @param nom
	 *            Nom de l'option
	 * @param val
	 *            Valeur
	 * @param type
	 *            Type
	 */
	public void ajouter(final String nom, final Object val, final byte type) {
		hm.put(nom, new StructOption(val, type));
	}

	/**
	 * <p>
	 * Modifier la valeur d'une option par la valeur en entrée.
	 * </p>
	 * 
	 * @param nom
	 *            Nom de l'option
	 * @param val
	 *            Nouvelle valeur
	 * @return faux si l'option n'existe pas.
	 */
	public boolean modifierOption(final String nom, final Object val) {
		Object o = hm.get(nom);
		StructOption so = this.hm.get(nom);
		if (o != null) {
			so.setValue(val);
			// System.out.println(nom+" vaut maintenant "+so.getValue());
			return true;
		} else
			System.out.println("Erreur changement option : "
					+ "elle n'existait pass dans la map");
		return false;
	}

	/**
	 * @param val
	 *            Position option entière (enum)
	 * @return int
	 */
	public int getIntValue(final IntEnum val) {
		return (Integer) (hm.get(val.toString()).getValue());
	}

	/**
	 * @param val
	 *            Position option booléenne (enum)
	 * @return option du type booléen.
	 */
	public boolean getBooleanValue(final BooleanEnum val) {
		return (Boolean) (hm.get(val.toString()).getValue());
	}

	/**
	 * @param name
	 *            Nom de l'option
	 * @return option en Object.
	 */
	public Object getValue(final String name) {
		return (hm.get(name).getValue());
	}

	/**
	 * @param name
	 *            Nom de l'option
	 * @return type de l'objet.
	 */
	public byte getType(final String name) {
		return hm.get(name).getType();
	}

	/**
	 * @param val
	 *            Position option String (enum)
	 * @return option du type String.
	 */
	public String getStringValue(final StringEnum val) {
		return hm.get(val.toString()).getValue().toString();
	}

	/**
	 * <p>
	 * Rends sous forme de tableau les options pour le mode CONSOLE de la façon
	 * suivant à double colonne :
	 * </p>
	 * <span style="font-family: 'Courier New', Courier, monospace;"><br />
	 * ' ' ' ' ' ' ' ' ' T A B L E A U ' 'D E S ' 'O P T I O N S<br />
	 * ' ' ' ' ' ' ╔════════════════╤═════════╦════════════════╤══════╗<br />
	 * ' ' ' ' ' ' ║ Option ' ' ' ' │ Valeur '║ Option ' ' ' ' │Valeur║<br />
	 * ' ' ' ' ' ' ╠════════════════╪═════════╬════════════════╪══════╣<br />
	 * ' ' ' ' ' ' ║ nomOption1 ' ' │ xxx' ' '║ nomOption2 ' ' │true '║<br />
	 * ' ' ' ' ' ' ║ ... ' ' ' ' ' '│ xxx' ' '║ ' ' ' ' ' ' ' '│ ' ' '║<br />
	 * ' ' ' ' ' ' ╚════════════════╧═════════╩════════════════╧══════╝<br />
	 * </span>
	 */
	public void afficher() {
		String clef = null, esp, esp2;
		Object valeur = false;
		int c = 0;

		System.out
				.println("                    T A B L E A U    D E S    O P T I O N S      \n"
						+ "             ╔════════════════╤═════════╦════════════════╤══════╗\n"
						+ "             ║ Option         │ Valeur  ║ Option         │Valeur║\n"
						+ "             ╠════════════════╪═════════╬════════════════╪══════╣");
		it = hm.keySet().iterator();

		while (it.hasNext()) {
			clef = it.next();
			valeur = hm.get(clef).getValue();
			esp = " ";
			esp2 = " ";
			for (int i = clef.length(); i < 11; i++)
				esp += " ";
			for (int i = clef.length(); i < 14; i++)
				esp2 += " ";
			if (c++ % 2 == 0)
				System.out.print("             ║ " + clef + esp + "   │ "
						+ valeur + "\t║");
			else
				System.out.println(" " + clef + esp2 + "│" + valeur + "\t║");
		}
		if (c % 2 == 1)
			System.out.println("                │      ║");
		System.out
				.println("             ╚════════════════╧═════════╩════════════════╧══════╝");
	}

	/**
	 * <p>
	 * Supprimer tous les éléments de la map.
	 * </p>
	 */
	public void vider() {
		this.hm = new HashMap<String, StructOption>();
	}

	/**
	 * <p>
	 * supprime une option à partir de son nom.
	 * </p>
	 * 
	 * @param opt
	 *            Nom de l'option à supprimer
	 */
	public void remove(final String opt) {
		this.hm.remove(opt);
	}

	/**
	 * @param nom
	 *            Nom de l'option
	 * @return existance du nom de l'option dans le tableau en mémoire.
	 */
	public boolean exists(final String nom) {
		return ((this.hm.get(nom) == null) ? false : true);
	}

	/**
	 * <p>
	 * Retour un tableau de String, idéal pour afficher dans une JList
	 * rapidement par exemple. Rendu de la forme 'nomOption valeur'.
	 * </p>
	 * 
	 * @return tableau de données de la forme 'nomOoption valeur'.
	 */
	public String[] toArrayString() {
		String ret[] = new String[this.hm.size() - this.dontDisplay.length+1];
		Object valeur;
		String clef, space;
		int i = 0;

		it = hm.keySet().iterator();
		while (it.hasNext()) {
			space = "";
			clef = it.next();
			if (this.isDisplayable(clef)) {
				valeur = hm.get(clef).getValue();
				for (int u = clef.length(); u < 25; u++)
					space += " ";

				ret[i++] = clef + space + valeur.toString();
			}
		}
		return ret;
	}

	/**
	 * <p>
	 * Retourne un tableau "parallèle" à toArrayString mais en spécifiant
	 * uniquement les noms.
	 * </p>
	 * 
	 * @return tableau de données de la forme 'nomOoption'.
	 */
	public String[] toArrayStringNames() {
		String ret[] = new String[this.hm.size() - this.dontDisplay.length+1];
		String clef;
		int i = 0;

		it = hm.keySet().iterator();
		while (it.hasNext()) {
			clef = it.next();
			if (this.isDisplayable(clef))
				ret[i++] = clef;
		}
		return ret;
	}

	/**
	 * @param nom
	 * @return vrai si le nom de l'option n'est pas dans le tableau de String à
	 *         ne pas afficher.
	 */
	private boolean isDisplayable(final String nom) {
		for (String s : dontDisplay)
			if (s.equals(nom))
				return false;
		return true;
	}

	// variables pour la hashMap
	private HashMap<String, StructOption> hm;
	private Iterator<String> it;
	final private String[] dontDisplay = { "precision", "verticalRender",
			"progressive", "nbThread", "Mfract", "MposXinit", "MposXfin", "Mscaleinit", "Mscalefin",
			"MposYinit", "MposYfin", "MiterInit", "MiterFin", "Mvar1offs",
			"Mvar1init", "Mvar1fin", "Mvar2offs", "Mvar2init", "Mvar2fin",
			"MmainVar", "MrenderPas", "MimgName" };
}
