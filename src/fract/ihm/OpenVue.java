package fract.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import fract.BiomorphLauncher;
import fract.opt.StructSaveVue;

//import java.awt.event.*;

/**
 * Fenêtre de sélection d'une vue pré-enregistrée. Ces vues sont importées au
 * lancement et en static depuis BiomorphLauncher. Cette fenêtre donne la
 * possibilité en un clic de choisir la position d'un fractal avec une image
 * temporaire génére si besoin et proposer des filtres selon le fractal choisi.
 */
public class OpenVue extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit la fenêtre.
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public OpenVue(MainWindow mw) {
		// paramétrage fenêtre
		this.mw = mw;
		this.setTitle("Ouverture d'une vue pré-enrigistrée");
		this.setBounds((int) mw.getLocationOnScreen().getX() + 20, (int) mw
				.getLocationOnScreen().getY() + 100, 750, 300);
		this.setVisible(true);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif")));
		
		this.ssv = BiomorphLauncher.tVue.getArrayOfStructSaveVue();
		this.descr = new JLabel[this.ssv.length];
		Container c = new JPanel();
		c.setLayout(new GridLayout(this.ssv.length, 1));
		for (int i = 0; i < ssv.length; i++) {
			this.descr[i] = new JLabel(this.ssv[i].toString());
			c.add(this.descr[i]);
			this.descr[i].addMouseListener(this);
		}
		scroll = new JScrollPane(c);
		this.add(scroll);
	}

	/**
	 * Ecoute le clic sur une vue.
	 */
	public void mouseClicked(MouseEvent ev) {
		for (int i = 0; i < this.ssv.length; i++) {
			if (ev.getSource() == this.descr[i]) {
				BiomorphLauncher.writeSubAction("Sélection preset " + i);
				this.dispose();
				this.mw.loadFractal(ssv[i]);
				return;
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	// variables
	private StructSaveVue[] ssv;
	private JLabel[] descr;
	private MainWindow mw;
	private JScrollPane scroll;
}
