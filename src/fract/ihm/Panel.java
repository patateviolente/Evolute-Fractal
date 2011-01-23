package fract.ihm;

import javax.swing.*;

import fract.BiomorphLauncher;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

final public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Le panel est déclaré avec la référence vers l'enregistrement des points.
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public Panel(final MainWindow mw) {
		// on récupère la fenêtre principale qui dispose des points et algos
		this.mw = mw;
		kit = Toolkit.getDefaultToolkit();

		// on ajoute les écouteurs pour le zoom, la rotation, le déplacement
		ecout = new PanelEcouteurs(this);
		this.addMouseMotionListener(ecout);
		this.addMouseListener(ecout);
		this.addMouseWheelListener(ecout);

		// menu contextuel
		this.jpm = new JPopupMenu();
		this.jmi_copier = new JMenuItem("Copier");
		this.jmi_copier.addActionListener(ecout);
		this.jmi_copier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		this.jmi_copier.setIcon(new ImageIcon("icon/menu/copier.png"));
		this.jpm.add(this.jmi_copier);

		// var pour le déplacement
		decX = decY = 0;
		image = null;
		onPanel = true;
		initZoomX = initZoomY = endZoomX = endZoomY = initRotX = initRotY = -1;
	}

	/**
	 * Par l'appel d'un repaint() : Dessine le fractal qui a été calculé et
	 * enregistré dans RegPoint. On ne s'occupe que de lister point par point et
	 * d'interpréter la couleur enregistrée.
	 * 
	 * @param g
	 *            Objet Graphics
	 */
	public void paintComponent(final Graphics g) {
		// déclaration d'un nouveau Buffer si nécessaire
		if (mw.pts == null)
			return;
		if ((mw.pts.getRows() > 0 && (buff == null
				|| mw.pts.getRows() != buff.getWidth() || mw.pts.getColumns() != buff
				.getHeight()))
				&& mw.pts.getRows() > 0) {
			buff = new BufferedImage(mw.pts.getRows(), mw.pts.getColumns(),
					BufferedImage.TYPE_INT_RGB);
		}

		// au cas où la surface s'agrandi
		if (this.getWidth() != this.mw.pts.getRows()
				|| this.getHeight() != this.mw.pts.getColumns()) {
			// on donne les ancienne dimension pour effacer des les pixels
			// exédentaire en cas de rendu ébauché (oldX oldY)
			int oldX = this.mw.pts.getRows();
			int oldY = this.mw.pts.getColumns();
			this.mw.pts = this.mw.pts.redimensionnerGrille(this.getWidth(),
					this.getHeight());
			this.updateImageBuffer();
			this.mw.deplacerFigure(0, 0, oldX, oldY);
			// this.mw.launchCalculAndDisplay(false);
			this.bufferToImage();
		}

		// si icone est null alors il n'y a pas d'homotétie en cours
		if (image == null) {
			buff = new BufferedImage(mw.pts.getRows(), mw.pts.getColumns(),
					BufferedImage.TYPE_INT_RGB);
			this.mw.launchCalculAndDisplay(true);
			this.bufferToImage();
			this.sizeAxes = this.getWidth() / 4;
			this.sizeCycle = this.sizeAxes - this.sizeAxes / 6;
		}
		// sinon (homotétie) on affiche l'image
		else {
			// image
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			// pas de zoom
			if (this.zoomEnCours == 1) {
				Graphics2D g2d = (Graphics2D) g;
				// rotation
				if (rotationExterieur || this.initRotX >= 0
						|| this.initRotY >= 0) {
					this.drawRotationElements(g, g2d);
				} // normal
				else {
					g.drawImage(image, decX, decY, image.getWidth(null), image
							.getHeight(null), this);
					// appel normal seulement
					this.drawAxes(g);
				}

			} else {
				g.drawImage(this.image,
						(this.image.getWidth(null) - (int) (this.image
								.getWidth(null) * this.zoomEnCours)) / 2

						, (this.image.getHeight(null) - (int) (this.image
								.getHeight(null) * this.zoomEnCours)) / 2,
						(int) (this.image.getWidth(null) * this.zoomEnCours),
						(int) (this.image.getHeight(null) * this.zoomEnCours),
						this);
			}
			// zone de sélection (zoom)
			g.setColor(Color.red);
			g.drawRect(Math.min(endZoomX, initZoomX), Math.min(endZoomY,
					initZoomY), Math.abs(endZoomX - initZoomX), Math
					.abs(endZoomY - initZoomY));
		}
	}

	/**
	 * Dessine les axes.
	 * 
	 * @param g
	 *            Objet Graphics
	 */
	private void drawAxes(final Graphics g) {
		if (!this.mw.getToolBars().isGridActiv())
			return;
		// dessin des axes
		double xi = mw.getFract().getPosX(), yi = mw.getFract().getPosY(), xf = mw
				.getFract().getPosX()
				+ mw.getFract().getScale() * this.getWidth(), yf = mw
				.getFract().getPosY()
				+ mw.getFract().getScale() * this.getHeight();
		int tmp;
		if (!(xi < 0 && xf < 0 || xi > 0 && xf > 0)) {
			tmp = (int) ((-xi) / (xf - xi) * this.getWidth());
			g.setColor(Color.red);
			g.drawLine(tmp + this.decX, this.decY, tmp + this.decX, this
					.getHeight()
					+ this.decY);
		}
		if (!(yi < 0 && yf < 0 || yi > 0 && yf > 0)) {
			tmp = (int) ((-yi) / (yf - yi) * this.getHeight());
			g.setColor(Color.red);
			g.drawLine(this.decX, tmp + this.decY, this.getWidth() + this.decX,
					tmp + this.decY);
		}
	}

	/**
	 * Dessine le cerle trigonométrique de rotation.
	 * 
	 * @param g
	 *            Objet Graphics
	 * @param g2d
	 *            Objet Graphics2D
	 */
	private void drawRotationElements(final Graphics g, final Graphics2D g2d) {
		// transformation pour la rotation
		AffineTransform origXform = g2d.getTransform();
		AffineTransform newXform1 = (AffineTransform) (origXform.clone());
		newXform1.rotate(-this.angle, this.initRotX, this.initRotY);
		g2d.setTransform(newXform1);
		g2d.drawImage(image, 0, 0, null);
		// on passe à la transformation actuelle pour le repère
		g2d.setTransform(origXform);

		// axes
		g
				.drawLine(initRotX - sizeAxes, initRotY, initRotX + sizeAxes,
						initRotY);
		g
				.drawLine(initRotX, initRotY - sizeAxes, initRotX, initRotY
						+ sizeAxes);
		// cercle
		angleTmp = (this.angleActuel + angle) % (Math.PI * 2);
		g.setColor(Color.yellow);
		g.drawArc(initRotX - sizeCycle, initRotY - sizeCycle, sizeCycle * 2,
				sizeCycle * 2, 0, (int) Math.toDegrees(angleTmp));
		g.drawLine(initRotX, initRotY, (int) (initRotX + sizeCycle
				* Math.cos(angleTmp)), (int) (initRotY - sizeCycle
				* Math.sin(angleTmp)));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.3f));
		g.fillArc(initRotX - sizeCycle, initRotY - sizeCycle, sizeCycle * 2,
				sizeCycle * 2, 0, (int) Math.toDegrees(angleTmp));
		// texte
		g.setFont(new Font("Arial", 40, 50));
		g.drawString(((int) ((angleActuel + angle) * 100) / 100d) + " rad",
				this.initRotX, this.initRotY - this.sizeCycle / 2);

		// axes avec rotation
		g2d.setTransform(newXform1);
		this.drawAxes(g2d);
		g2d.setTransform(origXform);
	}

	/**
	 * Met à jour l'image temporaire à partir du BufferImage qui a été complété
	 * lors du calcul ou de la fonction updateImageBuffer().
	 */
	public void bufferToImage() {
		if (this.buff != null) {
			this.image = Toolkit.getDefaultToolkit().createImage(
					this.buff.getSource());
		}
	}

	/**
	 * Mise à jour du buffer, utile après le renouvelement d'un calcul.
	 */
	public void updateImageBuffer() {
		if (buff != null && mw.pts.getRows() > 0 && mw.pts.getColumns() > 0) {
			buff = new BufferedImage(mw.pts.getRows(), mw.pts.getColumns(),
					BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < mw.pts.getRows(); i++)
				for (int u = 0; u < mw.pts.getColumns(); u++)
					if (mw.pts.getColor(i, u) != null)
						buff.setRGB(i, u, mw.pts.getColor(i, u).getRGB());
		}
	}

	/**
	 * Signale la fin du zoom (redessin de l'image et calcul des nouvelle
	 * coordonnées)
	 */
	public void endOfZoom() {
		if (zoomEnCours != 1) {
			BiomorphLauncher.writeSubAction("Zoom x" + this.zoomEnCours);
			mw
					.getFract()
					.zoomer(
							(this.getWidth() - (int) (this.getWidth() / this.zoomEnCours)) / 2,
							(this.getHeight() - (int) (this.getHeight() / this.zoomEnCours)) / 2,
							1 / this.zoomEnCours);
			this.zoomEnCours = 1;
			mw.launchCalculAndDisplay(true);
			this.bufferToImage();
			mw.getParams().getParamNavig().updateCoord();
			mw.addHistoryView();
		}
	}

	/**
	 * Copie le contenu du buffer du panel dans le presse papier.
	 */
	public void copyPanToClipoard() {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new Transferable() {
					public DataFlavor[] getTransferDataFlavors() {
						return new DataFlavor[] { DataFlavor.imageFlavor };
					}

					public boolean isDataFlavorSupported(DataFlavor flavor) {
						return DataFlavor.imageFlavor
								.equals(DataFlavor.imageFlavor);
					}

					public Object getTransferData(DataFlavor flavor)
							throws UnsupportedFlavorException, IOException {
						if (!DataFlavor.imageFlavor.equals(flavor)) {
							throw new UnsupportedFlavorException(flavor);
						}
						return buff;
					}
				}, null);
		BiomorphLauncher.writeAction("copie du dessin dans le presse papier");
	}

	// Objets
	public MainWindow mw;
	public Image image;
	public Toolkit kit;
	public PanelEcouteurs ecout;
	public BufferedImage buff;
	private int sizeAxes = 100, sizeCycle;
	final public JPopupMenu jpm;
	public JMenuItem jmi_copier;

	// valeurs de passage
	public int initDecX = Integer.MAX_VALUE, // variables pour les homotéties
			initDecY, decX, decY, initZoomX, initZoomY, endZoomX,
			endZoomY,
			initRotX, initRotY;
	public double angle, angleActuel, angleTmp;
	public boolean rotationExterieur;
	public double zoomEnCours = 1;
	final public float factZmIn = 0.95f, factZmOut = 1.05f;
	public boolean onPanel;
}
