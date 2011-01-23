package fract.color;

import javax.swing.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * <p>
 * ChooseRampColor est un outil de sélection de couleurs en dégradé façon
 * interface Gimp/ Photoshop... Fait pour aller d'une couleur à l'autre et
 * rendre les couleurs choisis dans un intervalle donné ou celles choisis.
 * </p>
 * <p>
 * Cette classe est finale, si vous voulez l'améliorer reprennez le code source
 * brut et non le package.
 * </p>
 * <span style="font-family: 'Courier New', Courier, monospace;"><br />
 * ''''''''''''''''''''''↑''''''''''''''''''''''''''''''''''''<br />
 * ''padLeft'''''''''''''|padTop'''''''''''''''''''''padRight'<br />
 * ←--→'_________________↓_______________________________←--→<br />
 * ''''▕▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒'''''<br />
 * ''''▕▒▒▒▒▒◢◣▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒◢◣↕enfoncCurseur▒▒▒▒'''''<br />
 * '''''▔▔▔▔▔██▔▔▔▔▔▔↑▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔██▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔<br />
 * ''''''''''''''''''|'padBottom''''''''''''''''''''''''''''''<br />
 * ''''''''''''''''''↓''''''''''''''''''''''''''''''''''''''''<br />
 * </span>
 * 
 * @author Patrick Portal
 */
final public class ChooseRampColor extends JPanel implements
		MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Constructeur sans couleurs : on met celles par défaut :<br />
	 * ([-16777176, 0.0, 50.0][-1, 100.0, 50.0][-9795136, 34.355827,
	 * 50.0][-12164490, 56.288345, 50.0])
	 * </p>
	 */
	public ChooseRampColor() {
		this.map = new CRCMap();
		// on ajoute les couleurs par défaut : un dégradé de gris
		this.map.ajouter(new ColorAdvanced(new Color(0, 0, 40), 0));
		this.map.ajouter(new ColorAdvanced(new Color(-9795136), 34));
		this.map.ajouter(new ColorAdvanced(new Color(-12164490), 56));
		this.map.ajouter(new ColorAdvanced(Color.white, 100));
		this.constructorInitializeValues();
	}

	/**
	 * <p>
	 * Constructeur avec tableau de couleurs prédéfinies ajoutées par défaut.
	 * </p>
	 * 
	 * @param c
	 *            Tableau d'objet ColorAdvanced à insérer
	 */
	public ChooseRampColor(final ColorAdvanced[] c) {
		// ajout des couleurs en argument
		map = new CRCMap();
		for (ColorAdvanced cc : c)
			map.ajouter(cc);

		this.constructorInitializeValues();
	}

	/**
	 * <p>
	 * Appel réservé pour les constructeurs seulement.<br />
	 * Lance le calcul d'interpolation et ajoute les écouteurs, le premier
	 * affichage.
	 * </p>
	 */
	private void constructorInitializeValues() {
		// définition des variables par défaut
		this.displayLines = false;

		// écouteurs
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.repaint();

		// génération d'un échantillon de 10 couleurs par défaut
		this.setNumberOfLines(10);
		this.generateLines(null);
	}

	/**
	 * <p>
	 * Dessin complet dans l'ordre :
	 * </p>
	 * <ul>
	 * <li>Fond</li>
	 * <li>Dégradés</li>
	 * <li>Bordures</li>
	 * <li>Curseurs de sélection</li>
	 * <li>Bordures curseur</li>
	 * </ul>
	 * 
	 * @param g
	 *            Objet Graphics
	 */
	public void paintComponent(final Graphics g) {
		this.calculateDimensions();
		if (this.longBar < 1)
			return; // panel trop petit
		// fond de tous le panel
		g.setColor(new JFrame().getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		bi = new BufferedImage(this.longBar, 1, BufferedImage.TYPE_INT_RGB);

		// dessin des dégradés
		this.generateLines(g);

		// dessin des lignes
		float intervalle = this.longBar / (float) this.nbLines;
		for (int i = 0; i < this.nbLines; i++) {
			// détermination de la couleur opposée
			g.setColor(new Color(
					(bi.getRGB((int) (i * intervalle), 0) ^ 0x80) & 0xff));
			if (this.displayLines) {
				g.drawLine(this.padLeft + (int) (i * intervalle), this.padTop,
						this.padLeft + (int) (i * intervalle), this.padTop
								+ this.hautBar / 2);
			}
		}

		// bordure autour du dégradé
		g.setColor(Color.black);
		g.drawRect(padLeft, padTop, longBar, hautBar);

		// dessin des selecteurs
		ColorAdvanced[] tab = this.map.getAllObjects();
		pol = new Polygon[tab.length];
		col = new ColorAdvanced[tab.length];
		this.dr = 0;
		for (ColorAdvanced cc : tab) {
			this.debut = (int) (this.padLeft + this.longBar / 100d
					* cc.getPosition() - this.largCurseur / 2);
			// détermination coordonnées du curseur polygonale
			/*
			 * '''''1'⋀'''''''''''''''''''''''''''
			 * ''''''√#\''''''''###############'''
			 * '''0'√###\''2''''#'''Indices'''#'''
			 * '''''┃####┃''''''#'Coordonnées'#'''
			 * '''''┃####┃''''''#'du.curseur.'#'''
			 * '''''┗━━━━┛''''''###############'''
			 * ''''4''''''3'''''''''''''''''''''''
			 */
			int[] x = new int[5], y = new int[5];
			x[0] = x[4] = this.debut;
			x[2] = x[3] = this.debut + this.largCurseur;
			x[1] = this.debut + this.largCurseur / 2;
			y[1] = this.padTop + this.hautBar - this.enfoncementCuseur;
			y[0] = y[2] = this.padTop + this.hautBar;
			y[3] = y[4] = y[1] + this.hautCurseur;

			pol[dr] = new Polygon(x, y, x.length);
			col[dr] = cc;
			// contenu du polygone
			g.setColor(cc.getColor());
			g.fillPolygon(pol[dr]);
			// bordure polygone
			g.setColor(Color.black);
			g.drawPolygon(pol[dr++]);
		}

	}

	/**
	 * <p>
	 * Recalcule les dimensions pour faciliter le dessin du panel.
	 * </p>
	 */
	private void calculateDimensions() {
		/*
		 * ''''''''''''''''''''''↑''''''''''''''''''''''''''''''''''''
		 * ''padLeft'''''''''''''|padTop'''''''''''''''''''''padRight'
		 * ←--→'_________________↓_______________________________←--→
		 * ''''▕▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒'''''
		 * ''''▕▒▒▒▒▒◢◣▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒◢◣↕enfoncCurseur▒▒▒▒'''''
		 * '''''▔▔▔▔▔██▔▔▔▔▔▔↑▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔██▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔
		 * ''''''''''''''''''|'padBottom''''''''''''''''''''''''''''''
		 * ''''''''''''''''''↓''''''''''''''''''''''''''''''''''''''''
		 */
		// dimensions du rectangle de couleurs
		this.hautBar = this.getHeight() - this.padTop - padBottom;
		this.longBar = this.getWidth() - this.padLeft - padRight;
		// dimensions des sélecteurs de couleurs
	}

	/**
	 * <p>
	 * Génère les échantillons de couleurs en fonction des couleurs insérées
	 * (soit par le constructeur, soit par la fonction
	 * <i>removeAllColorAndSetThese</i>).
	 * </p>
	 * <p>
	 * Fonction 2 en 1 :
	 * <ul>
	 * <li>si g != NULL alors mode dessin : tout sera desinné sur le panel.</li>
	 * <li>Si g est NULL alors repLignes sera regénéré et prêt à être utilisé.</li>
	 * </ul>
	 * 
	 * @param g
	 *            Objet Graphics
	 */
	public void generateLines(final Graphics g) {
		boolean dessin = false; // dessin = true SO g != NULL
		this.lastColor = false;
		// si on est pas en mode dessin on crée un tableau de repères
		if (g == null)
			this.repLignes = new int[this.nbLines];
		else
			dessin = true;

		if (dessin) {
			g.setColor(Color.black); // couleur par défaut du fond si aucune
			// couleur
			g.fillRect(padLeft, padTop, longBar, hautBar);
		}

		c0 = this.map.getNextColor(-1);

		while (c0 != null) {
			this.c1 = this.map.getNextColor(c0.getPosition());
			// si c'est la dernière couleur on calcul avec la même jusqu'au bout
			if (this.c1 == null) {
				this.lastColor = true;
				c1 = new ColorAdvanced(c0.getColor(), 100);
			}

			// calculs des coordonnées
			if (dessin) {
				this.debut = (int) (this.padLeft + this.c0.getPosition() / 100
						* this.longBar);
				this.fin = (int) (this.padLeft + this.c1.getPosition() / 100
						* this.longBar);
			} else {
				this.debut = (int) (this.c0.getPosition() / 100 * this.repLignes.length);
				this.fin = (int) (this.c1.getPosition() / 100 * this.repLignes.length);
			}

			// calcul des différences de chaques nuances R-G-B
			this.dr = c1.getColor().getRed() - c0.getColor().getRed();
			this.dg = c1.getColor().getGreen() - c0.getColor().getGreen();
			this.db = c1.getColor().getBlue() - c0.getColor().getBlue();
			double steps = fin - debut;

			// boucle dessin des couleurs traits par traits
			for (int i = debut; i < fin; i++) {
				cTmp = new Color(
						// sélection nouvelle couleur
						(int) (c0.getColor().getRed() + dr / steps
								* (i - debut)),
						(int) (c0.getColor().getGreen() + dg / steps
								* (i - debut)),
						(int) (c0.getColor().getBlue() + db / steps
								* (i - debut)));
				if (dessin) {
					g.setColor(cTmp);
					// mise à jour de la ligne du buffer et affichage
					bi.setRGB(i - debut, 0, g.getColor().getRGB());
					g.drawLine(i, this.padTop, i, this.padTop + this.hautBar);
				} else
					this.repLignes[i] = cTmp.getRGB();
			}
			if (this.lastColor)
				break; // fin
			this.c0 = this.c1;
		}

	}

	/**
	 * <p>
	 * Défini le nombre de lignes qui vont être affichés et donc le nombre
	 * d'échantillons de couleurs qui vont être rendu à la demande.
	 * </p>
	 * 
	 * @param iterations
	 *            Nombre d'itérations
	 */
	public void setNumberOfLines(final int iterations) {
		this.nbLines = iterations - 1;
		if (this.nbLines < 1) // 1 minimum
			this.nbLines = 1;
		this.generateLines(null);
	}

	/**
	 * @return la longueur du tableau de couleur à remplir.
	 */
	public int getNumberOfLines() {
		return this.nbLines;
	}

	/**
	 * @param offset
	 *            Position dans le tableau de couleur
	 * @return la couleur générée à la position du dégradé à la position donnée.
	 */
	public Color getColorAtOffset(final int offset) {
		return new Color(this.repLignes[offset]);
	}

	/**
	 * <p>
	 * Définie si le nombre d'échantillons choisis doit être représenté par des
	 * traits à intervalle réguliers ou non lors du dessin du JPanel.
	 * </p>
	 * 
	 * @param display
	 *            true = afficher les intervalles
	 */
	public void setDisplayLines(final boolean display) {
		this.displayLines = display;
	}

	/**
	 * <p>
	 * Echanger les couleurs contre celles en arguments. Eqiuvaut presque à une
	 * réinitialisation complète de l'objet.
	 * </p>
	 * 
	 * @param c
	 *            Tableau de couleurs à insérer
	 */
	public void removeAllColorAndSetThese(final ColorAdvanced[] c) {
		this.map.viderArrayList();
		for (ColorAdvanced cc : c)
			map.ajouter(cc);
	}

	/**
	 * <p>
	 * Inverse toutes les positions des couleurs selon un mirroir vertical sur
	 * le centre de l'axe. Les couleurs à gauche se retrouveront à droite et
	 * inversement.
	 * </p>
	 */
	public void switchColors() {
		for (ColorAdvanced c : col)
			c.setPosition(100 - c.getPosition());
		this.repaint();
	}

	/**
	 * @return tableau de couleurs (avancées) contenues dans le dégradé.
	 */
	public ColorAdvanced[] getArrayOfColors() {
		return this.map.getAllObjects();
	}

	/*
	 * ECOUTEURS
	 */

	/**
	 * <p>
	 * Si un curseur est en mouvement (drag&drop) il y a deux possibilités :
	 * <ul>
	 * <li>Soit le curseur n'est pas hors-zone alors le dégradé est
	 * immédiatement recalculé pour donne run appercu.</li>
	 * <li>Soit le curseur est hors zone, dans ce cas il est supprimé.</li>
	 * </p>
	 */
	public void mouseDragged(final MouseEvent ev) {
		if (this.pol == null || !this.isEnabled())
			return;
		if (this.dragging) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			// suppresion d'une couleur (souris déviée)
			if (Math.abs(initY - ev.getY()) > 150
					|| ev.getX() > this.longBar + 200 || ev.getX() < -200) {
				System.out.println("---> Suppression d'une couleur");
				this.map.supprimer(col[this.offsetColorDrag]);
				this.dragging = false;
			}
			// ou déplacement de la couleur
			else {
				this.decal = this.initX + (ev.getX() - this.initX);
				col[this.offsetColorDrag].setPosition((decal - this.padLeft)
						/ (float) this.longBar * 100);
			}
			this.repaint();
		}
	}

	/**
	 * <p>
	 * Détecte si la souris positionnée sur un curseur, dans ce cas le curseur
	 * est en forme de main <i>(pour inciter à cliquer et déplacer le
	 * curseur)</i>
	 * </p>
	 */
	public void mouseMoved(final MouseEvent ev) {
		if (this.pol == null || !this.isEnabled())
			return;
		for (Polygon p : this.pol) {
			if (p.contains(ev.getPoint())) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				return;
			}
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * <p>
	 * Si on maintient le clic sur un curseur alors on commence le déplacement.
	 * </p>
	 */
	public void mousePressed(final MouseEvent ev) {
		if (!this.isEnabled())
			return;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		if (this.pol == null)
			return;
		for (int i = 0; i < pol.length; i++)
			if (pol[i].contains(ev.getPoint())) {
				this.offsetColorDrag = i;
				this.dragging = true;
				this.initX = ev.getX();
				this.initY = ev.getY();
			}
	}

	/**
	 * Ecoute les clics :
	 * <ul>
	 * <li>Un double clic sur un curseur ouvre un sélecteur de couleur.</li>
	 * <li>Un simple clic sur la barre de couleur (+ bordure inférieure) cré un
	 * nouveau sélecteur aléatoire.</li>
	 * </ul>
	 */
	public void mouseClicked(final MouseEvent ev) {
		if (!this.isEnabled())
			return;
		if (this.pol == null)
			return;
		// double clic = changement de couleur
		if (ev.getClickCount() == 1) {
			for (int i = 0; i < pol.length; i++)
				if (pol[i].contains(ev.getPoint()))
					new ColorSelector(this, col[i]);
		}
		// un clic sur la partie du bas sans sélecteur = nouvelle couleur
		if (ev.getClickCount() == 1 && ev.getX() > this.padLeft
				&& ev.getX() < this.padLeft + this.longBar
				&& ev.getY() > this.padTop) {
			for (int i = 0; i < pol.length; i++)
				if (pol[i].contains(ev.getPoint()))
					return;
			this.map.ajouter(new ColorAdvanced(new Color(
					(int) (Math.random() * 10000) % 255,
					(int) (Math.random() * 10000) % 255,
					(int) (Math.random() * 10000) % 255),
					(ev.getX() - this.padLeft) / (float) this.longBar * 100));
			this.repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent ev) {
		this.dragging = false;
	}

	// composants
	final private CRCMap map;
	private BufferedImage bi;

	// constantes de dessin
	final private int hautCurseur = 20, largCurseur = 10,
			enfoncementCuseur = 7;
	final private int padTop = 7, padLeft = 13, padRight = 13, padBottom = 20;
	private int longBar, hautBar;
	private Color cTmp;

	// variables pour le déplacement
	private boolean dragging;
	private int offsetColorDrag;
	private int initX, initY, decal;

	// valeur temporaires de dessin
	private int debut, fin, dr, dg, db;
	private boolean lastColor;
	private ColorAdvanced c0, c1;
	private Polygon pol[];
	private ColorAdvanced col[];

	// repaires lignes
	private boolean displayLines;
	private int nbLines;
	private int[] repLignes;

	/**
	 * Cette classe interne stock les couleurs dans une hashmap. Elle permet :
	 * <ul>
	 * <li>D'ajouter une couleur (type ColorAdvanced).</li>
	 * <li>De supprimer une couleur (depuis son instance).</li>
	 * <li>D'être vidée.</li>
	 * <li>D'obtenir la couleur directement plus à droite (champ position).</li>
	 * <li>De rendre les couleurs sous forme de tableau.</li>
	 * <li>Un affichage pour voir ce que contient l'arraylist.</li>
	 * </ul>
	 * 
	 * @author Patrick Portal
	 */
	final class CRCMap {
		/**
		 * Le constructeur instancie une nouvelle ArrayList.
		 */
		public CRCMap() {
			al = new ArrayList<ColorAdvanced>();
		}

		/**
		 * Ajouter une CouleurAdvanced dans l'ArayList.
		 * 
		 * @param c Couleur à ajouter
		 */
		public void ajouter(ColorAdvanced c) {
			this.al.add(c);
		}

		/**
		 * Supprimer un élément de la liste, il faut que l'élénent provienne de
		 * la même instance que l'objet en argument.
		 * 
		 * @param c Couleur a supprimer
		 */
		public void supprimer(ColorAdvanced c) {
			this.al.remove(c);
		}

		/**
		 * Vider l'arrayList
		 */
		public void viderArrayList() {
			this.al.clear();
		}

		/**
		 * Retourne la couleur qui à la position directement la plus proche de
		 * la position en argument. S'il n'y a pas de couleur alors la fonction
		 * renvoi un pointeur NULL attention !
		 * 
		 * @param pos Position de la couleur actuelle
		 * @return couleur la plus proche en pourcentage supérieur
		 */
		public ColorAdvanced getNextColor(double pos) {
			ColorAdvanced csuivant = null; // couleur qu'on renvoi
			Iterator<ColorAdvanced> li = al.iterator();
			while (li.hasNext()) {
				ctmp = li.next();
				if (csuivant != null
						&& ctmp.getPosition() < csuivant.getPosition()
						&& ctmp.getPosition() > pos)
					csuivant = ctmp;
				else if (csuivant == null && ctmp.getPosition() > pos)
					csuivant = ctmp;
			}
			return csuivant;
		}

		/**
		 * @return tableau de tous les éléments dans l'ordre où ils ont été
		 *         ajoutés.
		 */
		public ColorAdvanced[] getAllObjects() {
			ColorAdvanced[] tab = new ColorAdvanced[this.al.size()];
			it = al.iterator();
			int i = 0;
			while (it.hasNext())
				tab[i++] = it.next();
			return tab;
		}

		/**
		 * Affiche l'objet avec un System.out.print sur le toString existant.
		 */
		public void afficher() {
			System.out.print(this.toString());
		}

		/**
		 * <p>
		 * Affiche le tableau de la forme<br />
		 * ################# Contenu Array ColorAdvanced #################<br />
		 * # > 0 = ColorAdvanced [r=0;g=0;b=0] ; position=0.0% ; poid = 50.0\n
		 * [...ETC...]<br />
		 * ###############################################################
		 * </p>
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer(
					" ################# Contenu Array ColorAdvanced #################\n");
			for (int i = 0; i < al.size(); i++)
				sb.append(" # > " + i + " = " + al.get(i));
			sb
					.append(" ###############################################################\n");
			return sb.toString();
		}

		// variables
		private ArrayList<ColorAdvanced> al;
		private Iterator<ColorAdvanced> it;
		private ColorAdvanced ctmp;
	}

}
