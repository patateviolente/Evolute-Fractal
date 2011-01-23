package fract.color;

import javax.swing.*;

import fract.ihm.MainWindow;

import java.awt.*;
import java.awt.event.*;

/**
 * <p>
 * Cette classe <b>instancie un JColorChooser pour choisir les couleurs par
 * défaut du fractal (fond et couleurs alternatives) qui sont situées dans la
 * barre d'outils de couleurs.
 * </p>
 */
final public class DefaultColorSelector extends JFrame implements
		ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Le constructeur définit la fenêtre, ajoute un écouteur au bouton valider.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 * @param select
	 *            Indice de couleur (0 = fond, 1 = alt1, 2 = alt2)
	 */
	public DefaultColorSelector(final MainWindow mw, final int select) {
		// construction de la fenêtre
		this.mw = mw;
		this.select = select;
		this.setBounds(400, 100, 560, 430);
		this.setTitle("Sélecteur de couleurs");
		this.setLayout(new FlowLayout());
		this.setAlwaysOnTop(true);
		this.toFront();
		this.setVisible(true);

		Color colorInit;
		if (select == 0)
			colorInit = mw.getBgFractColor();
		else
			colorInit = mw.getAlternateColor(select + 1);

		// composants et écouteur
		cc = new JColorChooser(colorInit);
		valid = new JButton("valider la couleur");
		valid.addActionListener(this);

		// ajout et dessin
		this.add(cc);
		this.add(valid);
		this.validate();
	}

	/**
	 * <p>
	 * Action de validation : donne la couleur au fond, couleur alternative
	 * selon le choix et dispose la fenêtre.
	 * </p>
	 */
	public void actionPerformed(final ActionEvent ev) {
		// selon le mode de modification de couleurs
		switch (select) {
		case DefaultColorSelector.FOND:
			this.mw.setFractColors(cc.getColor(), null, null);
			break;
		case DefaultColorSelector.ALT1:
			this.mw.setFractColors(null, cc.getColor(), null);
			break;
		case DefaultColorSelector.ALT2:
			this.mw.setFractColors(null, null, cc.getColor());
			break;
		}
		this.dispose();
	}

	// composants
	final private int select;
	final private MainWindow mw;
	final private JColorChooser cc;
	final private JButton valid;

	// aide à la désignation d'une couleur
	final private static int FOND = 0, ALT1 = 1, ALT2 = 2;
}
