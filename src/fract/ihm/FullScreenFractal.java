package fract.ihm;

import javax.swing.*;
import fract.BiomorphLauncher;
import fract.ihm.MainWindow.Calcul;

import java.awt.*;
import java.awt.event.*;

/**
 * Fenêtre de plein écran du fractal qui reprends le panel de dessin dans un
 * JFrame sans composants.
 */
public class FullScreenFractal extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit la fenêtre. Enlève le scrollPane du panel de la fenêtre
	 * principale pour l'attribuer à la fenêtre de plein écran.
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public FullScreenFractal(final MainWindow mw) {
		// fenêtre
		this.mw = mw;
		// calcul nouvelle échelle adaptée
		// Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		// double ratio = d.getWidth() / mw.getPan().getWidth();
		// System.out.println("new scale=" + (mw.getFract().getScale() /
		// ratio));
		// this.mw.getFract().setCoordonnees(mw.getFract().getPosX(),
		// mw.getFract().getPosY(), mw.getFract().getScale() / ratio);
		// mw.launchCalculAndDisplay(true);
		// 
		this.setVisible(true);
		GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().setFullScreenWindow(this);
		mw.container.remove(mw.scroll);
		mw.scroll.getPreferredSize().setSize(0, 0);
		mw.repaint();
		this.add(mw.scroll);
		mw.scroll.setBorder(null);

		// écouteur en cas de ALT+F4 (tous cas de fermeture)
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				closeWindow();
			}
		});

		// écouteur sur la touche échappe
		mw.getPan().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				// mw.manageCalcul(Calcul.arret);
				if (ev.getKeyCode() == KeyEvent.VK_ESCAPE)
					closeWindow();
				// mw.getPan().removeKeyListener(this);
			}
		});
	}

	/**
	 * Ferme la fenêtre en réatribuant le panel à la fenêtre principale.
	 */
	private void closeWindow() {
		BiomorphLauncher.writeAction("Fermeture du plein écran");
		mw.container.add(mw.scroll);
		mw.validate();
		mw.repaint();
		dispose();
	}

	final private MainWindow mw;
}
