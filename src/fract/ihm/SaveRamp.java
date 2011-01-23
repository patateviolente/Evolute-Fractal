package fract.ihm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

import fract.BiomorphLauncher;
import fract.color.ChooseRampColor;

/**
 * <p>
 * Popup de validation et précision avant enregistrement du dégradé de couleur.
 * Le popup demande un nom et demande au gestionnaire d'options d'enregistrer
 * dans le fichier + en mémoire vive.
 * </p>
 */
final public class SaveRamp extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Construction de la fenêtre, placement des composant texte, validation,
	 * etc.
	 * </p>
	 * 
	 * @param crc
	 *            Dégradé de couleur à sauvegarder
	 */
	public SaveRamp(ChooseRampColor crc) {
		// paramètre fenêtre
		this.crc = crc;
		this.setTitle("Enregistrer dégradé");
		this.setBounds(300, 200, 250, 150);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setAlwaysOnTop(true);

		// composants
		this.attribuer = new JCheckBox("Attribuer spécialement au fractal");
		this.nom = new JTextField("noname");
		this.nom.setPreferredSize(new Dimension(200, 26));
		this.nom.setToolTipText("<html>"
				+ "Le nom est un plus de reconaissance, <br />"
				+ "mais ce n'est pas ce qui désigne le<br />"
				+ "dégradé, il est donc facultatif.</html>");
		this.annuler = new JButton("Annuler");
		this.annuler.addActionListener(this);
		this.enregistrer = new JButton("Energistrer");
		this.enregistrer.addActionListener(this);

		// placement layout
		Container c = this.getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("Nom du dégradé"));
		c.add(this.nom);
		c.add(this.attribuer);
		c.add(this.annuler);
		c.add(this.enregistrer);
	}

	/**
	 * <p>
	 * Boutons valider et annuler.<br />
	 * Détruit la fenêtre si nom est correct.
	 * </p>
	 */
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == this.annuler)
			this.dispose();
		else {
			if (this.nom.getText().length() > 0) {
				BiomorphLauncher.gOpt.manageColor(-1, this.nom.getText(), 0,
						this.crc);
				this.dispose();
			} else
				this.nom.setBackground(Color.yellow);
		}
	}

	final private ChooseRampColor crc;
	final private JTextField nom;
	final private JCheckBox attribuer;
	final private JButton annuler, enregistrer;
}
