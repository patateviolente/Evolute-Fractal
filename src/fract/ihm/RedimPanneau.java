package fract.ihm;

import javax.swing.*;

import fract.BiomorphLauncher;

import java.awt.*;
import java.awt.event.*;

/**
 * <p>
 * Boîte de dialogue simple pour redimensionner la taille du panneau de dessin
 * du fractal. Permet de donner des dimensions en pixels personnaliées, de
 * choisir les dimensions par défaut et de visualiser avant de fermer la
 * fenêtre.
 * </p>
 */
public class RedimPanneau extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Construit la fenêtre, les composants graphiques. La fenêtre principale
	 * est requise en argument pour le placement de la fenêtre et accéder au
	 * panel de dessin.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public RedimPanneau(final MainWindow mw) {
		// fenêtre
		this.mw = mw;
		this.setVisible(true);
		this.setTitle("Modifier taille du panneau de dessin");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds((int) mw.getLocationOnScreen().getX() + 100, (int) mw
				.getLocationOnScreen().getY() + 80, 200, 120);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif")));
		
		// composants
		this.width = new JTextField((int) mw.getPanelDimension().getWidth()
				+ "");
		this.height = new JTextField((int) mw.getPanelDimension().getHeight()
				+ "");
		this.appli = new JButton("Appliquer");
		this.valid = new JButton("Valider");
		this.Lwidth = new JLabel("Longueur:");
		this.Lheight = new JLabel("Hauteur:");
		this.appli.addActionListener(this);
		this.valid.addActionListener(this);

		// focus sur toute la ligne pour écraser les données
		this.width.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent ev) {
				width.setSelectionStart(0);
				width.setSelectionEnd(width.getText().length());
			}
		});
		this.height.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent ev) {
				height.setSelectionStart(0);
				height.setSelectionEnd(height.getText().length());
			}
		});

		// détection touche entrer
		this.width.addKeyListener(this);
		this.height.addKeyListener(this);

		// diposition
		Container c = this.getContentPane();
		c.setLayout(new GridLayout(3, 2));
		Component cmp[] = { this.Lwidth, this.width, this.Lheight, this.height,
				this.appli, this.valid };
		for (Component cc : cmp)
			this.add(cc);

	}

	/**
	 * <p>
	 * Ecoute les boutons de validation et d'application et appelle la fonction
	 * de fermeture si validation des valeurs.
	 * </p>
	 */
	public void actionPerformed(final ActionEvent ev) {
		this.appliValues();
		if (ev.getSource() == this.valid)
			this.dispose();
	}

	/**
	 * <p>
	 * Applique les valeurs en actualisant le ScrollPane : insère les nouvelles
	 * dimensions au panel de dessin, retire le panel, le rajoute, puise valide
	 * le scrolpane.
	 * </p>
	 */
	private void appliValues() {
		this.mw.getPan().setPreferredSize(
				new Dimension(Integer.parseInt(this.width.getText()), Integer
						.parseInt(this.height.getText())));
		this.mw.getPan().repaint();
		this.mw.container.remove(mw.scroll);
		this.mw.container.add(mw.scroll);
		this.mw.validate();
	}

	/**
	 * <p>
	 * Si la <b>touche entrer</b> est pressée, les données sont validées et la
	 * fenêtre fermée.
	 * </p>
	 */
	public void keyPressed(KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
			appliValues();
			dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0) {
	}

	// variables
	final private JTextField width, height;
	final private JLabel Lwidth, Lheight;
	final private JButton appli, valid;
	private MainWindow mw;
}
