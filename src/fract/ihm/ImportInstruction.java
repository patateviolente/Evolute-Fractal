package fract.ihm;

import javax.swing.*;

import fract.BiomorphLauncher;
import fract.lang.PanneauExt;
import fract.opt.StructSaveVue;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * <p>
 * Classe graphique simple pour import un jeu d'instruction extérieur du même
 * type que celle trouvables dans le fichier config.
 * </p>
 * <p>
 * L'utilisateur doit pouvoir importer plusieurs lignes simultannément pour une
 * couleur et une vue et avoir la possibilité d'avoir un nombre illimité
 * d'instruction en cas d'ajout dans le fichier config en plus.
 * </p>
 */
public class ImportInstruction extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Dessine les composants de la fenêtre.
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public ImportInstruction(MainWindow mw) {
		// fenêtre
		this.mw = mw;
		this.setTitle(PanneauExt.getString("ImportInstruction.0")); //$NON-NLS-1$
		this.setBounds((int) mw.getLocationOnScreen().getX() + 20, (int) mw
				.getLocationOnScreen().getY() + 80, 400, 200);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(250, 100));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif")));
		
		// composants
		this.description = new JLabel(
				PanneauExt.getString("ImportInstruction.1")); //$NON-NLS-1$
		this.description
				.setToolTipText("<html>" //$NON-NLS-1$
						+ PanneauExt.getString("ImportInstruction.3") //$NON-NLS-1$
						+ PanneauExt.getString("ImportInstruction.4") //$NON-NLS-1$
						+ PanneauExt.getString("ImportInstruction.5") //$NON-NLS-1$
						+ PanneauExt.getString("ImportInstruction.6") //$NON-NLS-1$
						+ PanneauExt.getString("ImportInstruction.7") //$NON-NLS-1$
						+ PanneauExt.getString("ImportInstruction.8")); //$NON-NLS-1$
		this.area = new JTextArea();
		this.scroll = new JScrollPane(this.area);
		this.add = new JCheckBox(
				PanneauExt.getString("ImportInstruction.9")); //$NON-NLS-1$
		this.valider = new JButton(PanneauExt.getString("ImportInstruction.10")); //$NON-NLS-1$
		this.valider.addActionListener(this);

		// ajout
		Box box = Box.createVerticalBox();
		Component c[] = { this.description, this.scroll, this.add, this.valider };
		for (Component cc : c)
			box.add(cc);
		this.add(box);
	}

	/**
	 * Ecoute le bouton valider et demande l'import.
	 */
	public void actionPerformed(ActionEvent ev) {
		String line = null;
		int type, id = -1;
		BufferedReader br = new BufferedReader(new StringReader(this.area
				.getText()));
		do {
			try {
				line = br.readLine();
				if (line == null)
					break;
				type = BiomorphLauncher.gOpt.getType(line);
				if (type != -1) {
					BiomorphLauncher.gOpt.decryptAndSaveLine(line, type);
					id = BiomorphLauncher.tVue.getLastId();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (line != null);
		// chargement
		if (id != -1)
			this.mw.loadFractal(BiomorphLauncher.tVue.getVueAtId(id));
		this.dispose();
		// enregistrement si demandé
		if (this.add.isSelected())
			this.mw.saveFractal();
	}

	// composants
	final private MainWindow mw;
	final private JLabel description;
	final private JScrollPane scroll;
	final private JTextArea area;
	final private JCheckBox add;
	final private JButton valider;
}
