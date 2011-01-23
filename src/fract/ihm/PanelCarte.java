package fract.ihm;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;

import fract.BiomorphLauncher;
import fract.algo.*;
import fract.exception.ThreadNotReadyException;
import fract.ihm.MainWindow.Calcul;
import fract.opt.EnumOption.BooleanEnum;

/**
 * Déssine la carte de visualisation. Calcul une fois l'algo et trace un carré
 * de position qui représente la position du dessin principal.
 */
public class PanelCarte extends JPanel implements MouseMotionListener,
		MouseListener, Runnable {
	private static final long serialVersionUID = 1L;

	/**
	 * Le constructeur initialise les valeurs nécessaire au fonctionnement des
	 * écouteur. Une instance à la fenête principale est obligatoire.
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public PanelCarte(final MainWindow mw) {
		this.mw = mw;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		startX = startY = decX = decY = 0;
		this.draging = false;
		this.premierCalcul = true;
		try {
			this.rob = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.addMouseListener(new MouseAdapter() {
			/**
			 * Un double clic sur la carte met le zoom par défaut
			 */
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					mw.manageCalcul(Calcul.arret);
					mw.setFractalDefaultView();
				}
			}
		});
	}

	public void run() {
		this.fromRun = true;
		this.repaint();
	}

	/**
	 * Dessine le Panel dans l'ordre {image, carré}
	 * 
	 * @param g
	 *            Objet Graphics
	 */
	public void paintComponent(Graphics g) {
		if (this.getWidth() <= 0 && this.getHeight() <= 0 || this.fract == null)
			return;

		// si c'est le premier appel on calcul le fractal par défaut
		if (premierCalcul || image == null) {
			// si le rendu est intégrale, on demande à le faire dans un thread
			if (!this.fromRun) {
				Thread t = new Thread(this);
				t.start();
				return;
			}
			this.fromRun = false;

			this.premierCalcul = false;
			bi = new BufferedImage(this.getWidth(), this.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			this.generateFractalIntoBuffer();

			xi = fractCarte.getPosX();
			yi = fractCarte.getPosY();
			xf = fract.getPosX() + fractCarte.getScale() * this.getWidth();
			yf = fract.getPosY() + fractCarte.getScale() * this.getHeight();
		} else { // sinon on redessine l'image : elle ne change jamais
			g.drawImage(image, 0, 0, image.getWidth(null), image
					.getHeight(null), this);
		}

		// calcul du rapport d'échelle
		double rapX = 1 - Math.abs((fract.getPosX() - this.xf)
				/ (this.xf - this.xi));
		double rapY = 1 - Math.abs((fract.getPosY() - this.yf)
				/ (this.yf - this.yi));

		// point de départ xxi;yyi + calcul cotés
		xxi = (int) (rapX * this.getWidth()) + this.decX;
		yyi = (int) (rapY * this.getHeight()) + this.decY;
		w = (int) (mw.getPanelDimension().getWidth() * fract.getScale() / fractCarte
				.getScale());
		h = (int) (mw.getPanelDimension().getHeight() * fract.getScale() / fractCarte
				.getScale());
		// minimums
		if (w < 5 || h < 5) {
			w = h = 5;
			g.setColor(Color.red);
		} else
			g.setColor(Color.yellow);

		Graphics2D g2d = (Graphics2D) g;

		AffineTransform origXform = g2d.getTransform();
		AffineTransform newXform1 = (AffineTransform) (origXform.clone());
		newXform1.rotate(this.fract.getAngle() * 2, 150, 80);
		g2d.setTransform(newXform1);

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.3f));
		g2d.fillRect(xxi, yyi, w, h);
		g2d.drawRect(xxi, yyi, w, h);
		g2d.drawLine(xxi, yyi, xxi + w, yyi + h);
		g2d.drawLine(xxi + w, yyi, xxi, yyi + h);
	}

	/**
	 * Défini un nouveau fractal. A l'appel de cette fonction, la génération du
	 * fractal et son affichage se font automatiquement.
	 * 
	 * @param fractCarte
	 *            Fractal pour la carte
	 * @param fract
	 *            Fractal pour le pannel de dessin principal
	 */
	public void setFractals(final BaseFractale fractCarte,
			final BaseFractale fract) {
		startX = startY = decX = decY = 0;
		this.fractCarte = fractCarte;
		this.fract = fract;
		if (!this.premierCalcul) {
			this.generateFractalIntoBuffer();
		}
		this.premierCalcul = true;
		this.repaint();
	}

	/**
	 * Relance le calcul du fractal.
	 */
	public void recalculate() {
		premierCalcul = true;
		this.repaint();
	}

	/**
	 * Renvoie vrai si le curseur se trouve dans le carré de posionnement
	 * 
	 * @param x
	 *            Abcisse
	 * @param y
	 *            Ordonnée
	 * @return Vrai si curseur dans le rectangle
	 */
	private boolean inRect(final int x, final int y) {
		return (x >= xxi - tolerance && x <= (xxi + w + tolerance)
				&& y >= yyi - tolerance && y <= (yyi + h + tolerance)) ? true
				: false;
	}

	/**
	 * Détecte si le curseur est dans le carré de sélection
	 */
	public void mouseMoved(MouseEvent ev) {
		if (this.inRect(ev.getX(), ev.getY()))
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		else
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Génère le fractal, le met dans une image et actualise le Panel.
	 */
	private void generateFractalIntoBuffer() {
		while (true) {
			try {
				this.fractCarte.calculer(this.getGraphics(), bi, true);
				mw.getParams().setVisibleWindows(BiomorphLauncher.tOpt
						.getBooleanValue(BooleanEnum.planPan), BiomorphLauncher.tOpt
						.getBooleanValue(BooleanEnum.equationEdit));
				break;
			} catch (ThreadNotReadyException e) {
				BiomorphLauncher.writeError("Le panel n'était pas dimensionné "
						+ "lors du calcul -> réessai dans 100ms.");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
			}
		}
		this.image = Toolkit.getDefaultToolkit().createImage(
				this.bi.getSource());
		this.repaint();
	}

	/**
	 * <p>
	 * Aide au déplacement de la souris : plutôt que d'être bloqué par les bords
	 * de l'écran, une classe Robot vient ajouter aux coordonnées de départ
	 * (startX/ startY) un nouvelle espace d'écran en revenant à l'opposé.
	 * </p>
	 * 
	 * @param posX
	 *            Position X de la souris
	 * @param posY
	 *            Position Y de la souris
	 */
	private void appliInifiniteMouseArea(final int posX, final int posY) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		if (posX == 0) {
			this.addX -= d.getWidth();
			rob.mouseMove((int) d.getWidth() - 2, posY);
		} else if (posY == 0) {
			this.addY -= d.getHeight();
			rob.mouseMove(posX, (int) (d.getHeight() - 2));
		} else if (posX == d.getWidth() - 1) {
			this.addX += d.getWidth();
			rob.mouseMove(1, posY);
		} else if (posY == d.getHeight() - 1) {
			this.addY += d.getHeight();
			rob.mouseMove(posX, 1);
		}
	}

	/**
	 * Détecte le glisser/ déposer
	 */
	public void mouseDragged(MouseEvent ev) {
		if (this.draging) {
			this.appliInifiniteMouseArea(ev.getXOnScreen(), ev.getYOnScreen());
			decX = (int) ((ev.getX() + this.addX - this.startX)
					* fract.getScale() / fractCarte.getScale());
			decY = (int) ((ev.getY() + this.addY - this.startY)
					* fract.getScale() / fractCarte.getScale());
			int decX2 = ev.getX() + this.addX - this.startX;
			int decY2 = ev.getY() + this.addY - this.startY;
			this.mw.deplacementFigure(-decX2, -decY2);
			this.mw.miseAJourCoordTF(-decX2, -decY2);
			this.repaint();
		}
	}

	/**
	 * Début du déplacement si curseur dans la zone
	 */
	public void mousePressed(MouseEvent ev) {
		if (this.inRect(ev.getX(), ev.getY())) {
			this.addX = this.addY = 0;
			this.draging = true;
			this.startX = ev.getX();
			this.startY = ev.getY();
			this.repaint();
			this.mw.manageCalcul(Calcul.arret);
		}
	}

	/**
	 * fin du déplacement
	 */
	public void mouseReleased(MouseEvent ev) {
		if (this.draging) {
			this.draging = false;
			decX = ev.getX() + this.addX - this.startX;
			decY = ev.getY() + this.addY - this.startY;
			mw.deplacerFigure(-decX, -decY, (int) this.mw.getPanelDimension()
					.getWidth(), (int) this.mw.getPanelDimension().getHeight());
			decX = decY = this.mw.getPan().decX = this.mw.getPan().decY = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent ev) {
		this.repaint();
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

	// à instancier
	private MainWindow mw;
	private Robot rob;
	private Image image;
	private BaseFractale fractCarte, fract;
	private BufferedImage bi;

	// aide au dessin
	private double xi, yi, xf, yf; // coordonnées points carte
	private int xxi, yyi, w, h; // ....... points carré sélection
	private boolean premierCalcul;
	final private int tolerance = 8;
	private boolean fromRun = false; // retient si le calcul provient d'1 thread

	// le traitement des coordonnées souris
	private boolean draging;
	private int startX, startY, decX, decY; // pour le déplacement
	private int addX, addY; // ajout du modulo

}
