package fract.ihm;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

/**
 * <p>
 * Composant de forme trigonométrique qui permet de choisir un angle et de le
 * demander en radians ou en degrés.
 * </p>
 * <span style="font-family: 'Courier New', Courier, monospace;"><br />
 * . . ┌----------------┐<br />
 * . . │ . . .# . # . . │<br />
 * . . │ . #. . . . # . │<br />
 * . . │ .#. . _____ #. │<br />
 * . . │ .#. . . . . #. │<br />
 * . . │ . # α=0rad # . │<br />
 * . . │ . . .# . # . . │<br />
 * . . └----------------┘</span>
 */
public class SelectAngle extends JPanel implements MouseListener,
		MouseMotionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Le constructeur ne demande pas d'argument : la taille attribué à ce
	 * composant doit être défini extérieurement.
	 * </p>
	 */
	public SelectAngle() {
		// taille et écouteurs
		this.setPreferredSize(new Dimension(180, 180));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		// par défaut le point impact est situé en haut du cercle
		this.setAngle(0d);
		this.enable = true;

		// option par défaut modifiables
		this.decalage = false;
		this.ecrire = true;

		// définition des couleurs
		this.FondPanel = new JFrame().getBackground();
		this.FondCercleEnabled = Color.yellow;
		this.FondCercleParcouruEnabled = Color.orange;
		this.FondCercleDisabled = Color.lightGray;
		this.tmpDisable = this.FondCercleDisabled; // copie pour l'anim'
		this.FondCercleParcouruDisabled = Color.gray;

	}

	/**
	 * <p>
	 * Dessine le composant dans l'ordre suivant en se servant des données
	 * géométriques calculées par majCoordonnees() :
	 * </p>
	 * <ul>
	 * <li>Couleur de fond</li>
	 * <li>Fond du cercle</li>
	 * <li>Contours</li>
	 * <li>Texte</li>
	 * </ul>
	 * 
	 * @param g
	 *            Objet Graphics
	 */
	public void paintComponent(Graphics g) {
		this.majCoordonnees();

		// fond
		g.setColor(this.FondPanel);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// fond cercle
		g.setColor((this.enable) ? FondCercleEnabled : FondCercleDisabled);
		g.fillOval(0, this.offset, diametre, diametre);

		// camembert
		g.setColor((this.enable) ? FondCercleParcouruEnabled
				: FondCercleParcouruDisabled);
		g.fillArc(0, this.offset, diametre, diametre, 0, (int) Math
				.toDegrees(angle));

		// figures
		g.setColor(Color.black);
		g.drawOval(0, this.offset, diametre, diametre);
		g.drawLine(departX, departY + offset, impactX, impactY + offset);

		// écriture
		g.setFont(this.police);
		if (this.ecrire && this.decalage)
			g.drawString(ecrit, this.getWidth() / 2 - 3 * this.offset,
					(int) (this.offset * 0.9));
		else if (this.ecrire) {
			g.drawString(ecrit, this.getWidth() / 2 - 4 * this.getWidth() / 10,
					this.diametre * 4 / 5);
		}
	}

	/**
	 * <p>
	 * Calcul de la plupart des données mathématiques et de dessin :
	 * </p>
	 * <ul>
	 * <li>Décalage supérieur.</li>
	 * <li>Angle pointé (si manuel).</li>
	 * <li>Taille de police.</li>
	 * <li>Point d'impact pour le deuxième point du segment.</li>
	 * </ul>
	 */
	private void majCoordonnees() {
		// diamètre et point de départ
		if (decalage)
			this.offset = this.getHeight() / 9;
		else
			this.offset = 0;
		if (this.getWidth() > this.getHeight() - this.offset)
			this.diametre = this.getHeight() - this.offset;
		else
			this.diametre = this.getWidth() - this.offset;
		this.rayon = this.diametre / 2;
		this.departX = this.diametre / 2;
		this.departY = this.diametre / 2;

		// angle
		if (this.clicY != Integer.MAX_VALUE) {
			try {
				angle = Math.atan((double) Math.abs(clicY - departY)
						/ (double) Math.abs(clicX - departX));
				if (clicX < departX)
					angle = Math.PI / 2 + Math.PI / 2 - Math.abs(angle);
				if (clicY > departY)
					angle *= -1;
				if (angle < 0)
					angle = 2 * Math.PI + angle;
			} catch (ArithmeticException e) {
				angle = 0;
			}
		} // sinon on a imposé un angle

		// police
		police = new Font(Font.SERIF, Font.PLAIN, (int) (this.getHeight() / 5));
		ecrit = "α=" + Math.ceil(angle * 1000) / 1000 + "rad";

		// impact
		this.impactX = (int) (this.rayon * Math.cos(angle)) + rayon;
		this.impactY = -(int) (this.rayon * Math.sin(angle)) + rayon;
	}

	/**
	 * <p>
	 * Impose un angle en radians. Attention, il faut demander un repaint pour
	 * que le composant soit mis à jour graphiquement.
	 * </p>
	 * 
	 * @param angle
	 *            Angle en radians
	 */
	public void setAngle(final double angle) {
		this.clicY = Integer.MAX_VALUE; // repère pour une future condition
		this.angle = angle;
	}

	/**
	 * Retourne l'angle comme si il était trigonométrique modulo [-PI;PI].
	 * 
	 * @return angle en radians
	 */
	public double getAngleEnRadian() {
		return (this.enable) ? this.angle : 0;
	}

	/**
	 * <p>
	 * Change le statut du composant : enable = actif ; !enable = désactivé (pas
	 * d'écouteurs actif et dessin modifié). Le dessin prend une alure
	 * <b>grisé</b> automatiquement lorsqu'il est désactivé.
	 * </p>
	 * 
	 * @param enable
	 *            Rendre actif
	 */
	public void setEnable(final boolean enable) {
		this.enable = enable;
		this.repaint();
	}

	/**
	 * <p>
	 * Affiche les principales variables du composant de la façon :
	 * </p>
	 * <span style="font-family: 'Courier New', Courier, monospace;"> Sélecteur
	 * angulaire {rayon=ENTIER(pix) ; angle=DOUBLE(rad)}</span>
	 * 
	 * @return Description de l'objet
	 */
	public String toString() {
		return "Sélecteur angulaire {rayon=" + this.rayon + "(pix) ; angle="
				+ this.angle + "(rad)}";
	}

	/**
	 * <p>
	 * Conterti en degré l'angle du cercle trigonométrique. Le point 0 ne se
	 * trouve plus à droite mais en haut du cercle (pi/2 = 0°)
	 * </p>
	 * 
	 * @return angle en degrés
	 */
	public int getAngleEnDegre() {
		int tmp = (int) Math.abs((angle * 180 / Math.PI) - 90);
		if (this.clicX < this.departX && this.clicY <= this.departY)
			tmp = 360 - tmp;
		return (this.enable) ? tmp : 0;
	}

	/**
	 * <p>
	 * Procédure qui attribut les nouvelles coordonnées au point de contact en
	 * prenant comùpte de la marge supérieure.
	 * </p>
	 */
	private void newPosition(final MouseEvent ev) {
		if (this.enable && (ev.getButton() == 0 || ev.getButton() == 1)) {
			clicX = ev.getX();
			clicY = ev.getY() - this.offset;
			this.repaint();
		}
	}

	/**
	 * @return abcisse du centre
	 */
	public int getCenterX() {
		return this.rayon;
	}

	/**
	 * @return ordonnée du centre
	 */
	public int getCenterY() {
		return this.rayon + this.offset;
	}

	/**
	 * <p>
	 * Quand la souris se déplace avec le clic gauche enfoncé, un appel d'un
	 * nouvel angle à recalculer est lancé.
	 * </p>
	 */
	public void mouseDragged(MouseEvent ev) {
		if (this.enable)
			this.newPosition(ev);
	}

	/**
	 * <p>
	 * Un clic appel un recalcul d'angle. Si le composant est désactivé alors on
	 * change momentanément la couleur pour prévenir.
	 * </p>
	 */
	public void mousePressed(MouseEvent ev) {
		if (this.enable)
			this.newPosition(ev);
		else {
			this.FondCercleDisabled = this.FondCercleParcouruDisabled;
			this.repaint();
		}
	}

	/**
	 * <p>
	 * Clic relaché : si le composant est désactivé on rend la bonne couleur au
	 * composant.
	 * </p>
	 */
	public void mouseReleased(MouseEvent arg0) {
		if (!this.enable) {
			this.FondCercleDisabled = this.tmpDisable;
			this.repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	final private Color FondPanel, FondCercleEnabled,
			FondCercleParcouruEnabled;
	private Color tmpDisable, FondCercleDisabled, FondCercleParcouruDisabled;
	private boolean enable, decalage, ecrire;
	private String ecrit;
	private Font police;
	private double angle;
	private int diametre, rayon, offset;
	private int departX, departY, impactX, impactY;
	private int clicX, clicY;
}
