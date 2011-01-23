package fract.struct;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Cette classe stock les précédentes configuration liés à un nouveau calcul.
 */
public class HistoriqueTab {
	/**
	 * Le constructeur initialise la HashMap.
	 */
	public HistoriqueTab() {
		this.hm = new TreeMap<Integer, HistoriqueStruct>();
		this.offset = this.taille = -1;
	}

	/**
	 * <p>
	 * Ajouter une vue par son numéro dans l'historique et par toutes ses
	 * données. La fonction retourne faux si jamais la vue précédente est
	 * exactement la même que la vue à ajouter pour éviter les doublons.
	 * </p>
	 * 
	 * @param posX
	 *            Abcisse point supérieur gauche
	 * @param posY
	 *            Ordonnée point supérieur gauche
	 * @param scale
	 *            Echelle
	 */
	public boolean ajouterVue(double posX, double posY, double scale) {
		// on éfface les position suivantes si la nouvelle entrée existe
		if (this.offset != this.taille) {
			this.deleteAllEntriesAfterThisPosition(this.offset + 1);
		}

		// on ajoute si la vue est différente
		if (this.offset >= 0
				&& posX == this.getDouble(this.offset, HistoriqueTab.POSX)
				&& posY == this.getDouble(this.offset, HistoriqueTab.POSY)
				&& scale == this.getDouble(this.offset, HistoriqueTab.SCALE))
			return false;

		hm.put(this.offset + 1, new HistoriqueStruct(posX, posY, scale));
		this.taille = ++this.offset;
		return true;
	}

	/**
	 * Fait un reset de l'historique : position sur -1 et map vidée.
	 */
	public void resetHistorique() {
		this.deleteAllEntriesAfterThisPosition(0);
		this.taille = this.offset = -1;
	}

	/**
	 * Supprime toutes les historiques dont l'indice est supérieur ou égal à
	 * l'argument.
	 * 
	 * @param pos
	 *            Position
	 */
	public void deleteAllEntriesAfterThisPosition(int pos) {
		this.offset = this.taille = pos - 1;
		for (int i = pos; i < Integer.MAX_VALUE; i++) {
			if (this.hm.get(i) != null)
				this.hm.remove(i);
			else
				return;
		}
	}

	/**
	 * @return retourne vrai si il n'y a pas de vue suivante.
	 */
	public boolean isLastView() {
		return (this.offset == this.taille);
	}

	/**
	 * @return retourne vrai si la position de historique est sur la première
	 *         vue (ou sur aucun -> offset = -1).
	 */
	public boolean isFirstView() {
		return (this.offset <= 0);
	}

	/**
	 * <p>
	 * Retourne les infos demandés à telle position et quelle valeur double.
	 * Pour savoir quoi choisir en valeur, utilisez les constantes accesibles en
	 * static.
	 * </p>
	 * <ul>
	 * <li>0 : abcisse</li>
	 * <li>1 : ordonnée</li>
	 * <li>2 : échelle</li>
	 * </ul>
	 * 
	 * @param position
	 *            Position
	 * @param valeur
	 *            Valeur à obtenir (0=posX, 1=posY, 2=l'échelle)
	 * @return valeur demandé en paramètre (0 pour posX - 1 pour posY - 2 pour
	 *         l'échelle)
	 */
	public double getDouble(int position, int valeur) {
		HistoriqueStruct hs = this.hm.get(position);
		this.offset = position;
		switch (valeur) {
		case 0:
			return hs.getPosX();
		case 1:
			return hs.getPosY();
		default:
			return hs.getScale();
		}
	}

	/**
	 * @return la position du curseur dans la map.
	 */
	public int getOffset() {
		return this.offset;
	}

	/**
	 * @return la taille de l'historique.
	 */
	public int getTaille() {
		return this.taille;
	}

	/**
	 * <p>
	 * Affiche un tableau des vus de l'historique pour visualiser le contenu de
	 * la Map de la façon suivante :
	 * </p>
	 * <p>
	 *' ' ' ' ' ' ' ' H I S T O R I Q U E D E S V U E S<br />
	 *' ' ' ' ╔═══════╤═════════════════╤═════════════════╤═════════════════╗<br />
	 *' ' ' ' ║ Id ' '│ Abcisse (X) ' ' │ Ordonnée (Y) ' '│ Echelle ' ' ' ' ║<br />
	 *' ' ' ' ╠═══════╪═════════════════╪═════════════════╪═════════════════╣<br />
	 *' ' ' ' ║.......│.................│.................│.................║
	 * </p>
	 */
	public void afficher() {
		final byte[] tailleCase = { 6, 15, 15, 15 }; // taille des cases
		byte spc[] = new byte[4];
		String esp[] = new String[4];
		HistoriqueStruct hs;
		Integer clef;
		System.out
				.println("                      H I S T O R I Q U E   D E S   V U E S      \n"
						+ "        ╔═══════╤═════════════════╤═════════════════╤═════════════════╗\n"
						+ "        ║  Id   │   Abcisse (X)   │   Ordonnée (Y)  │     Echelle     ║\n"
						+ "        ╠═══════╪═════════════════╪═════════════════╪═════════════════╣");
		this.it = hm.keySet().iterator();

		while (it.hasNext()) {
			clef = it.next();
			hs = this.hm.get(clef);
			// calcul des espaces
			spc[0] = (byte) (tailleCase[0] - clef.toString().length());
			spc[1] = (byte) (tailleCase[1] - new String(hs.getPosX() + "")
					.length());
			spc[2] = (byte) (tailleCase[2] - new String(hs.getPosY() + "")
					.length());
			spc[3] = (byte) (tailleCase[3] - new String(hs.getScale() + "")
					.length());
			for (int i = 0; i < 4; i++) {
				esp[i] = "";
				for (int u = 0; u < spc[i]; u++)
					esp[i] += " ";
			}
			// on peut afficher
			System.out.println("        ║ " + clef + esp[0] + "│ "
					+ hs.getPosX() + esp[1] + " │ " + hs.getPosY() + esp[2]
					+ " │ " + hs.getScale() + esp[3] + " ║");
		}
		System.out
				.println("        ╚═══════╧═════════════════╧═════════════════╧═════════════════╝");
	}

	// variables
	private TreeMap<Integer, HistoriqueStruct> hm;
	private Iterator<Integer> it;
	private int offset, taille;

	// constantes
	final static int POSX = 0, POSY = 1, SCALE = 2;
}
