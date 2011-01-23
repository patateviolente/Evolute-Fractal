package fract.ihm;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;
import fract.BiomorphLauncher;
import fract.ihm.MainWindow.Calcul;

/**
 * <p>
 * Les écouteurs de type MouseListener et MouseMotionListener du panel de dessin
 * principal sont implémentés ici pour plus de lisibilité.
 * </p>
 */
public class PanelEcouteurs implements MouseListener, MouseMotionListener,
		MouseWheelListener, ActionListener {
	public PanelEcouteurs(Panel pan) {
		this.pan = pan;
	}

	/**
	 * <p>
	 * Signalisation de rotation, déplacement ou zoom :
	 * </p>
	 * <ul>
	 * <li>Si la <b>touche espace</b> est maintenue, on active le déplacement.</li>
	 * <li>Si la <b>touche shift</b> est maintenue, on active la rotation.</li>
	 * <li>Si la <b>touche control</b> est maintenue, on active le zoom.</li>
	 * </ul>
	 **/
	public void mousePressed(final MouseEvent e) {
		this.pan.mw.getFract().setDirectDisplay(false);
		if (pan.mw.spaceDown) { // c'est un déplacement
			pan.initDecX = e.getX();
			pan.initDecY = e.getY();
		} else if (!pan.mw.shiftDown && e.getButton() == 1) { // c'est une
			// sélection du zoom
			pan.initZoomX = e.getX();
			pan.initZoomY = e.getY();
		} else if (pan.mw.shiftDown) { // rotation
			pan.initRotX = e.getX();
			pan.initRotY = e.getY();
			// pan.mw.getFract().setOffset(pan.initRotX, pan.initRotY);
			try { // on bouge le curseur pour aider l'utilisateur
				Robot rob = new Robot();
				Component comp = e.getComponent();
				rob.mouseMove((int) (comp.getLocationOnScreen().getX()) + 100
						+ e.getX(), (int) comp.getLocationOnScreen().getY()
						+ e.getY());

			} catch (AWTException e1) {
			}
		}
		pan.bufferToImage();
	}

	/**
	 * <p>
	 * Si la souris fait un glisser et que :
	 * </p>
	 * <ul>
	 * <li><b>Touche shift</b> enfoncée : rotation en cours</li>
	 * <li><b>Touche espace</b> enfoncée : déplacement en cours</li>
	 * <li><b>Touche control</b> enfoncée : zone de redimensionnement en cours</li>
	 * </ul>
	 * <p>
	 * Les actions effectués ne montre qu'un caractère provisoire avant d'être
	 * calculés <i>(en attente de relachement de touche dans tous les cas)</i>.
	 * </p>
	 */
	public void mouseDragged(final MouseEvent e) {

		if (pan.mw.spaceDown && SwingUtilities.isLeftMouseButton(e)) {
			// calcul décalage des pixels
			this.pan.mw.deplacementFigure(e.getX() - pan.initDecX, e.getY()
					- pan.initDecY);
			this.pan.mw.miseAJourCoordTF(e.getX() - pan.initDecX, e.getY()
					- pan.initDecY);
		} else if (pan.mw.shiftDown && pan.initRotX >= 0 && pan.initRotY >= 0) {
			// rotation
			pan.angle = BiomorphLauncher.getCornerFromThesePoints(pan.initRotX,
					pan.initRotY, e.getX(), e.getY());
			pan.mw.setRotation((pan.angleActuel + pan.angle) % (Math.PI * 2),
					false, true);
			pan.repaint();
		} else if (!pan.mw.shiftDown && pan.initZoomX >= 0
				&& pan.initZoomY >= 0) { // un zoom ?
			pan.endZoomX = e.getX();
			pan.endZoomY = e.getY();
		}
		pan.repaint();
	}

	/**
	 * <p>
	 * Si la souris est relachée et que :
	 * </p>
	 * <ul>
	 * <li><b>Touche shift</b> enfoncée : rotation terminée</li>
	 * <li><b>Touche espace</b> enfoncée : déplacement terminée</li>
	 * <li><b>Touche control</b> enfoncée : zone terminée</li>
	 * </ul>
	 * <p>
	 * Un calcul sera demandé <i>(complet sauf dans le cas d'un
	 * déplacement)</i>.
	 * </p>
	 */
	@SuppressWarnings("deprecation")
	public void mouseReleased(final MouseEvent e) {
		int tmp;

		this.pan.mw.manageCalcul(Calcul.arret);
		if (pan.mw.spaceDown) { // déplacement
			pan.mw.deplacerFigure(pan.decX, pan.decY, pan.getWidth(), pan
					.getHeight());
			pan.decX = pan.decY = 0;
		} else if (pan.mw.shiftDown) { // rotation
			pan.initRotX = pan.initRotY = -1;
			pan.repaint();
			pan.angleActuel += (pan.angle);
			pan.angleActuel %= (Math.PI * 2);
			pan.mw.setRotation(pan.angleActuel, true, true);
		} else if (!pan.mw.shiftDown && e.getButton() == 1) { // redimensionnement
			// échange du coin gauche avec le coin bas-droit s'il le faut
			pan.endZoomX = e.getX();
			pan.endZoomY = e.getY();

			if (pan.initZoomX > pan.endZoomX) {
				tmp = pan.initZoomX;
				pan.initZoomX = pan.endZoomX;
				pan.endZoomX = tmp;
			}
			if (pan.initZoomY > pan.endZoomY) {
				tmp = pan.initZoomY;
				pan.initZoomY = pan.endZoomY;
				pan.endZoomY = tmp;
			}
			// annulation d'un menu contextuel
			if (this.pan.jpm.isShowing()) {
				this.pan.jpm.hide();
				return;
			}
			// zoom simple vers le point désigné, 5 px de marge en cas de
			// mauvais clic
			if (Math.abs(pan.initZoomX - pan.endZoomX) < 5
					&& Math.abs(pan.initZoomY - pan.endZoomY) < 5) {
				pan.initZoomX = pan.initZoomY = pan.endZoomX = pan.endZoomY = -1;
				return;
			}
			// zone de sélection
			// on se base sur la partie la+grande pour ne pas couper la zone
			else if (Math.abs(pan.endZoomX - pan.initZoomX)
					/ (double) pan.getWidth() > Math.abs(pan.endZoomY
					- pan.initZoomY)
					/ (double) pan.getHeight()) {
				pan.mw.getFract().zoomer(
						pan.initZoomX,
						pan.initZoomY,
						(double) (pan.endZoomX - pan.initZoomX)
								/ (double) pan.getWidth());
			} else {
				pan.mw.getFract().zoomer(
						pan.initZoomX,
						pan.initZoomY,
						(double) (pan.endZoomY - pan.initZoomY)
								/ (double) pan.getHeight());
			}

			pan.mw.addHistoryView();
			pan.mw.launchCalculAndDisplay(true);
			pan.initZoomX = pan.initZoomY = pan.endZoomX = pan.endZoomY = -1;
		}
		pan.bufferToImage();
		this.pan.mw.getParams().getParamNavig().updateCoord();
	}

	/**
	 * <p>
	 * Si la <b>touche control</b> est maintenant, la roulette de la souris
	 * demande un zoom du dessin pour montrer l'échelle à l'utilisateur.
	 * </p>
	 * <p>
	 * Le fractal ne sera recalculé uniquement qu'au moment où control est
	 * relaché.
	 * </p>
	 */
	public void mouseWheelMoved(final MouseWheelEvent ev) {
		if (ev.isControlDown()) {
			pan.mw.manageCalcul(Calcul.arret);
			if (ev.getWheelRotation() > 0)
				pan.zoomEnCours /= pan.factZmOut;
			else
				pan.zoomEnCours /= pan.factZmIn;
			pan.repaint();
		}
	}

	public void mouseClicked(final MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			this.pan.jpm.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/**
	 * <p>
	 * Clic du <b>menuItem copier image</b>, l'utiliateur demande la copie dans
	 * le presse papier.
	 * </p>
	 */
	public void actionPerformed(final ActionEvent ev) {
		if (ev.getSource() == this.pan.jmi_copier) {
			this.pan.copyPanToClipoard();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}

	final private Panel pan;

}
