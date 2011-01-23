package fract.ihm.admin;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import fract.BiomorphLauncher;
import fract.lang.AdminMsg;

/**
 * <p>
 * Panel d'options, permet de changer les valeurs de chaques options par le type
 * qui convient : entier/ texte/ booléen.
 * </p>
 */
public class AdminOption extends JPanel implements ActionListener,
		ListSelectionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Instancie les éléments de base.
	 */
	public AdminOption() {
		this.setLayout(new BorderLayout());
		this.val = BiomorphLauncher.tOpt.toArrayString(); // toute la ligne
		this.valName = BiomorphLauncher.tOpt.toArrayStringNames();
		this.list = new JList(this.val);
		this.scroll = new JScrollPane(list);
		this.scroll.setPreferredSize(new Dimension(340, 180));
		this.modifier = new JButton(AdminMsg.getString("AdminOption.0")); //$NON-NLS-1$
		this.modifier.addActionListener(this);
		this.modifier.setEnabled(false);

		this.add(scroll);
		this.add(modifier, BorderLayout.SOUTH);

		list.addListSelectionListener(this);
	}

	/**
	 * Si changement de valeur alors une ligne est sélectionnée On peut activer
	 * les boutons
	 */
	public void valueChanged(final ListSelectionEvent ev) {
		this.modifier.setEnabled(true);
	}

	/**
	 * en cas de modification/ ajout d'extension vous pouvez par cette fonction
	 * actualiser la liste
	 */
	public void actualiserListe() {
		System.out.println(AdminMsg.getString("AdminOption.1")); //$NON-NLS-1$
		int offset = this.list.getSelectedIndex();
		this.val = BiomorphLauncher.tOpt.toArrayString();
		this.valName = BiomorphLauncher.tOpt.toArrayStringNames();
		this.list.setListData(this.val);
		this.list.setSelectedIndex(offset);
		this.validate(); // on rafraichit la vue de la liste
	}

	/**
	 * Boutons : Modifier
	 */
	public void actionPerformed(final ActionEvent ev) {
		if (this.list.getSelectedIndex() >= 0 && ev.getSource() == modifier)
			new AdminOptForm(this, this.valName[list.getSelectedIndex()]);
		else {
			BiomorphLauncher.writeError(AdminMsg.getString("AdminOption.2") //$NON-NLS-1$
					+ this.list.getSelectedIndex()
					+ AdminMsg.getString("AdminOption.3")); //$NON-NLS-1$
		}
	}

	/**
	 * Classe interne pour la modification d'une option
	 */
	class AdminOptForm extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;

		public AdminOptForm(final AdminOption fen, final String nom) {
			this.setVisible(true);
			this.setTitle(AdminMsg.getString("AdminOption.4")); //$NON-NLS-1$
			this.setBounds(245 + fen.getWidth(), 170, 350, 100);
			this.setAlwaysOnTop(true);
			this.toFront();

			// declaring labels
			this.nom = nom;
			type = BiomorphLauncher.tOpt.getType(nom);
			descr = new JLabel();
			o = BiomorphLauncher.tOpt.getValue(nom);
			switch (type) {
			case 0:
				descr.setText(AdminMsg.getString("AdminOption.5")); //$NON-NLS-1$
				break;
			case 1:
				descr.setText(AdminMsg.getString("AdminOption.6")); //$NON-NLS-1$
				break;
			default:
				descr.setText(AdminMsg.getString("AdminOption.7")); //$NON-NLS-1$
				break;
			}

			valeur = new JTextField(o.toString());
			valeur.setPreferredSize(new Dimension(150, 20));

			c = this.getContentPane();
			c.setLayout(new FlowLayout());
			c.add(descr);
			c.add(valeur);

			// BOUTON VALIDER
			valider = new JButton(AdminMsg.getString("AdminOption.8")); //$NON-NLS-1$
			valider.addActionListener(this);
			c.add(valider);
		}

		/**
		 * Traitement des chaînes si valider.
		 */
		public void actionPerformed(ActionEvent ev) {
			boolean ok = true;
			if (type == 0
					&& !(valeur.getText().equals("true") || valeur.getText() //$NON-NLS-1$
							.equals("false"))) //$NON-NLS-1$
				ok = false;
			else if (type == 1) {
				try {
					Integer.parseInt(valeur.getText());
				} catch (NumberFormatException e) {
					ok = false;
				}
			}

			if (!ok)
				JOptionPane.showMessageDialog(this,
						AdminMsg.getString("AdminOption.11"), //$NON-NLS-1$
						AdminMsg.getString("AdminOption.12"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			else {

				switch (this.type) {
				case 0:
					BiomorphLauncher.gOpt.changeOption(nom, Boolean
							.parseBoolean(valeur.getText()));
					BiomorphLauncher.tOpt.modifierOption(nom, Boolean
							.parseBoolean(valeur.getText()));
					break;
				case 1:
					BiomorphLauncher.gOpt.changeOption(nom, Integer
							.parseInt(valeur.getText()));
					BiomorphLauncher.tOpt.modifierOption(nom, Integer
							.parseInt(valeur.getText()));
					break;
				default:
					BiomorphLauncher.gOpt.changeOption(nom, "\"" //$NON-NLS-1$
							+ valeur.getText() + "\""); //$NON-NLS-1$
					BiomorphLauncher.tOpt.modifierOption(nom, valeur.getText());
					break;
				}
				actualiserListe();
				this.dispose();
			}
		}

		final private int type;
		final private String nom;
		final private Container c;
		final private Object o;
		final private JTextField valeur;
		final private JLabel descr;
		final private JButton valider;

	}

	final private JButton modifier;
	private String[] val, valName;
	final private JList list;
	final private JScrollPane scroll;

}
