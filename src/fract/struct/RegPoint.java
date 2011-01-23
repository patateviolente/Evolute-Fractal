package fract.struct;

import java.awt.Color;

/**
 * Structure de donnée des points en cours de calcul retient la position, et la
 * couleur dans de simple tableau
 */
public class RegPoint {
	/**
	 * Le constructeur initialise le tableau de type Color à deux dimension
	 * d'après les dimensions fournis
	 * 
	 * @param x
	 *            Nombre de colonnes
	 * @param y
	 *            Nombre de lignes
	 */
	public RegPoint(final int x, final int y) {
		this.couleur = new Color[x][y];

		// on initialise les points
		this.setThisValueToAll(null);
	}

	/**
	 * Donne la couleur en arguments à tous les éléments de la grille. Pour
	 * faire un reset, faire un setThisValueToAll(null) et la couleur null
	 * d'affichera ensuite par la couleur par défaut.
	 * 
	 * @param c
	 *            Couleur (Color)
	 */
	public void setThisValueToAll(final Color c) {
		for (int i = 0; i < this.getRows(); i++)
			for (int u = 0; u < this.getColumns(); u++)
				couleur[i][u] = c;
	}

	/**
	 * Redimensionne la grille (recopie la grille dan sune nouvelle). Si la
	 * grille est plus grande, les nouveaux points seront des objets nuls. Si la
	 * grille est plus petite alors des objets seront supprimés.
	 * 
	 * @param width
	 *            Nouvelle longueur
	 * @param height
	 *            Nouvelle hauteur
	 */
	public RegPoint redimensionnerGrille(final int width, final int height) {
		RegPoint rp = new RegPoint(width, height);
		for (int i = 0; i < ((width < this.getRows()) ? width : this.getRows()); i++)
			for (int u = 0; u < ((height < this.getColumns()) ? height : this
					.getColumns()); u++)
				rp.setPoint(i, u, this.getColor(i, u));
		return rp;
	}

	/**
	 * Pour décaler la matrice de point. cette fonction crée une nouvelle
	 * instance à RegPoint plutôt que de les déplacer un par un les points selon
	 * les quatre cas possible. Deux boucles imbriquées sont nécessaires.
	 * 
	 * @param decX
	 *            Décalage en X
	 * @param decY
	 *            Décalage en Y
	 */
	public RegPoint decalerPoints(final int decX, final int decY) {
		int ri, rf, ci, cf; // Row Init, Row Final, Col init, Col Final
		RegPoint rp = new RegPoint(this.getRows(), this.getColumns());

		// si ça dépasse évidement la zone alors on passe à la suite
		if (Math.abs(decX) >= this.getColumns()
				|| Math.abs(decY) >= this.getRows())
			return rp;

		// abcisses
		if (decX >= 0) {
			ri = decX;
			rf = this.getRows();
		} else {
			ri = 0;
			rf = this.getRows() + decX;
		}

		// ordonnées
		if (decY >= 0) {
			ci = decY;
			cf = this.getColumns();
		} else {
			ci = 0;
			cf = this.getColumns() + decY;
		}
		// on recopie les pixels dans le tableau
		for (; ri < rf; ri++) {
			for (int y = ci; y < cf; y++) {
				rp.couleur[ri][y] = this.couleur[ri - decX][y - decY];
			}
		}
		return rp;
	}

	/**
	 * Déposer un point avec sa couleur (type Color)
	 * 
	 * @param x
	 *            Position en X
	 * @param y
	 *            Position en Y
	 * @param c
	 *            Couleur
	 */
	public void setPoint(final int x, final int y, final Color c) {
		if (x < this.couleur.length && y < this.couleur[0].length)
			this.couleur[x][y] = c;
	}

	/**
	 * Retourne la couleur du point entré en argument. Bien s'assurer que le
	 * point existe dans la matrice car aucune exception n'est traitée pour un
	 * gain de temps évident.
	 * 
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 * @return couleur du point demandé
	 */
	public Color getColor(final int x, final int y) {
		return this.couleur[x][y];
	}

	/**
	 * Retourne la taille d'une colonne (dimension 2)
	 * 
	 * @return taille des colonnes
	 */
	public int getColumns() {
		if (this.getRows() != 0)
			return this.couleur[0].length;
		return 0;
	}

	/**
	 * Retourne la taille d'une ligne (dimension 1)
	 * 
	 * @return taille des lignes
	 */
	public int getRows() {
		return this.couleur.length;
	}

	/**
	 * Met à null toutes les cases.
	 */
	public void resetValues() {
		this.setThisValueToAll(null);
	}

	/**
	 * <p>
	 * Affiche l'image en mode console avec des carré et des blancs sur format
	 * carré. L'afffichage est réduit à une ligne de 80 caractères maximum pour
	 * éviter les retours à la ligne sur les moyens écrans. Il est préférable
	 * d'afficher l'image sur un composant graphique traitant le pixel...
	 * </p>
	 * <p>
	 * Exemple<br />
	 * ██████████████████████████████████████''████████████████████████<br />
	 * ███████████████████████████████████''███████████████████████████<br />
	 * ████████████████████████████████████████''█████████████████████'<br />
	 * ███████████████████████████████████████''''''███████''████████''<br />
	 * █████████████████████████████████████'''''''██████'''''''████''█<br />
	 * ████████████████████████████████████'█'''''████'''''''''''''███'<br />
	 * ██████████████████████████████████████'''''██''''''''''''''''''█<br />
	 * ████████████████████████████████████████''''█''████'█''''''''''█<br />
	 * ██████████████████████████████████████████████''█████'█'''''████<br />
	 * ████████████████████████'███████'███████████''''███████'████'███<br />
	 * ████████████████████████''█████''███'██''██''''''███████████████<br />
	 * ████████████████████████'█''''''''''''''''''''''████████████████<br />
	 * ███████████████████████''██''''''''''''''''''''''█'█████████████<br />
	 * ███████████████████████████'''''''''''''''''''''████████████████<br />
	 * ██████████████████████████''''''''''''''''''''██████████████████<br />
	 * ████████████████████████''''''''''''''''''''''█'''██████████████<br />
	 * █████████████████████████''''''''''''''''''''''█'███████████████<br />
	 * ███████████████████████'''''''██''██''█'''█████''███████████████<br />
	 * ████████████████''███████''''███████████'███████████████████████<br />
	 * █████████████'''''███████'''████████████████████████████████████<br />
	 * █████'█████''''''''''██████'█''''███████████████████████████████<br />
	 * █'█''''███''''''''''''''''''███''''█████████████████████████████<br />
	 * ██'██'''█'████''''''''''''████'''''█'███████████████████████████<br />
	 * ████''████''████''''''█'██████''''''████████████████████████████<br />
	 * █████████''████████''█████████''''██████████████████████████████<br />
	 * █████████'█████████████████████'''██████████████████████████████<br />
	 * ████████████████████████████████████'███████████████████████████<br />
	 * </p>
	 */
	public void afficher() {
		int ech = (int) Math.ceil(this.getRows() / 60);
		StringBuffer sb = new StringBuffer("		ZONNE ACTUELLE CALCUEE : \n");
		for (int i = 0; i < this.getColumns(); i += ech * 2) {
			for (int u = 0; u < this.getRows(); u += ech) {
				sb
						.append((this.couleur[u][i] == null
								|| this.couleur[u][i].equals(Color.white) || this.couleur[u][i]
								.equals(Color.black)) ? "'" : "█");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());

	}

	/**
	 * <p>
	 * Ecrase les données ( -> null) dans une ligne ou une colonne avec pour
	 * argument un départ et une largeur.
	 * </p>
	 * <p>
	 * <u>Exemple :</u> Pour éffacer une colonne de la colonne 352 à 358 choisir
	 * les argument true, 352, 6.
	 * </p>
	 * 
	 * @param vertical
	 *            vrai si supression colonne
	 * @param debut
	 *            Position début
	 * @param largeur
	 *            nombre de colonnes ou rangés à effacer depuis début
	 */
	public void eraseLines(final boolean vertical, final int debut,
			final int largeur) {
		if (vertical) {
			for (int i = debut; i < debut + largeur; i++)
				for (int u = 0; u < this.getColumns(); u++)
					this.setPoint(i, u, null);
		} else {
			for (int i = debut; i < debut + largeur; i++)
				for (int u = 0; u < this.getRows(); u++)
					this.setPoint(u, i, null);
		}
	}

	/**
	 * <p>
	 * Affiche un descriptif de la grille de point du type 'Grille de points de
	 * taille [LIGNESxCOLONNES]'
	 * </p>
	 */
	public String toString() {
		return "Grille de points de taille [" + this.getRows() + "x"
				+ this.getColumns() + "]";
	}

	private Color couleur[][];
}
