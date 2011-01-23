package fract.color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>
 * Cette classe <b>instancie un JColorChooser</b> pour <b>modifier la couleur
 * d'un sélecteur de couleur</b> de l'éditeur de dégradé et modifit lui même la
 * couleur.
 * </p>
 */
final public class ColorSelector extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Le constructeur définit la fenêtre, ajoute un écouteur au bouton valider.
	 * En argument :
	 * </p>
	 * <ul>
	 * <li>Le sélecteur de dégradé (ChooseRampColor)</li>
	 * <li>Le type ColorAdvanced dont il faut modifier la couleur</li>
	 * </ul>
	 * 
	 * @param crc
	 *            Objet ChooseRampColor
	 * @param c
	 *            Objet ColorAdvanced <i>(appartient normalement à crc)</i>
	 */
	public ColorSelector(final ChooseRampColor crc, final ColorAdvanced c) {
		// construction de la fenêtre
		this.setBounds(400, 100, 580, 430);
		this.setTitle("Sélecteur de couleurs");
		this.setLayout(new FlowLayout());
		this.setAlwaysOnTop(true);
		this.toFront();
		this.setVisible(true);

		// composants et écouteur
		this.c = c;
		this.crc = crc;
		cc = new JColorChooser(c.getColor());
		valid = new JButton("valider la couleur");
		valid.addActionListener(this);

		// ajout et dessin
		this.add(cc);
		this.add(valid);
		this.validate();
	}

	/**
	 * <p>
	 * Action de validation : modifie la couleur du type ColorAdvanced (du
	 * ChooseRampColor), fait un repaint du défgradé, et dispose la fenêtre.
	 * </p>
	 */
	public void actionPerformed(final ActionEvent ev) {
		this.c.setColor(this.cc.getColor());
		this.crc.repaint();
		this.dispose();
	}

	// composants
	final private ChooseRampColor crc;
	final private ColorAdvanced c;
	final private JColorChooser cc;
	final private JButton valid;
}
