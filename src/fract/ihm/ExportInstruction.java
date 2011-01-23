package fract.ihm;

import javax.swing.*;

import fract.BiomorphLauncher;
import fract.color.ColorAdvanced;
import fract.lang.PanneauExt;
import fract.opt.StructSaveVue;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

/**
 * <p>
 * Cette classe graphique simple donne la possibilité de copier dans le presse
 * papier les données qui identifie la session courante du dessin : coordonnées
 * complètes du fractal (et/ou exclusif) couleurs.
 * </p>
 */
final public class ExportInstruction extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Construit la fenêtre avec l'aire de texte, label d'aide, bouton de copie.
	 * </p>
	 * 
	 * @param mw Objet MainWindow
	 */
	public ExportInstruction(MainWindow mw) {
		// fenêtre
		this.mw = mw;
		this.setTitle(PanneauExt.getString("ExportInstruction.0")); //$NON-NLS-1$
		this.setBounds((int) mw.getLocationOnScreen().getX() + 20, (int) mw
				.getLocationOnScreen().getY() + 80, 800, 140);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(250, 100));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif")));
		
		// texte
		vue = new StructSaveVue("mavue", this.mw.getFractalName(), -1, mw //$NON-NLS-1$
				.getBgFractColor(), mw.getAlternateColor(0), mw
				.getAlternateColor(1), mw.getToolBars().alternateColors
				.isSelected(), mw.getFract().getIterations(), mw.getFract()
				.getScale(), mw.getFract().getPosX(), mw.getFract().getPosY(),
				mw.getPan().getWidth(), mw.getPan().getHeight(), mw.getFract()
						.getArgumentsArray()).getFormattedConfigLine(-1)
				+ "\n"; //$NON-NLS-1$
		// Génération champ couleur
		colorLine = "->id=-1 ~name=\"import\" *madeFor=-1 £color={"; //$NON-NLS-1$

		for (ColorAdvanced c : this.mw.getBottomPanel().crc.getArrayOfColors())
			colorLine += "[" + c.getColor().getRGB() + ", " + c.getPosition() //$NON-NLS-1$ //$NON-NLS-2$
					+ ", " + c.getPoid() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		colorLine += "}"; //$NON-NLS-1$

		// composants
		this.description = new JLabel(PanneauExt.getString("ExportInstruction.9")); //$NON-NLS-1$
		this.description
				.setToolTipText("<html>" //$NON-NLS-1$
						+ PanneauExt.getString("ExportInstruction.11") //$NON-NLS-1$
						+ PanneauExt.getString("ExportInstruction.12") //$NON-NLS-1$
						+ PanneauExt.getString("ExportInstruction.13")); //$NON-NLS-1$
		this.area = new JTextArea(vue + colorLine);

		// sélection automatique du texte
		this.area.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				selectText();
			}
		});
		this.area.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				selectText();
			}
		});

		this.scroll = new JScrollPane(this.area);
		this.valider = new JButton(PanneauExt.getString("ExportInstruction.14")); //$NON-NLS-1$
		this.valider.addActionListener(this);

		// ajout
		Box box = Box.createVerticalBox();
		Component c[] = { this.description, this.scroll, this.valider };
		for (Component cc : c)
			box.add(cc);
		this.add(box);
	}

	/**
	 * <p>
	 * Sélectionne le texte en entier dans l'aire de texte.
	 * </p>
	 */
	private void selectText() {
		this.area.setSelectionStart(0);
		this.area.setSelectionEnd(this.area.getText().length());
	}

	/**
	 * <p>
	 * Ecoute le bouton copier dans le presse papier et copie les informations
	 * dans le presse papier système directement dans cette fonction en
	 * instanciant un objet ClipBoard.
	 * </p>
	 */
	public void actionPerformed(ActionEvent ev) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(this.area.getText()), null);
		this.dispose();
	}

	// composants
	final private MainWindow mw;
	final private JLabel description;
	final private JScrollPane scroll;
	final private JTextArea area;
	final private JButton valider;

	// texte
	final private String vue;
	private String colorLine;
}
