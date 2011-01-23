package fract.ihm.admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.event.*;
import java.io.IOException;

import fract.BiomorphLauncher;
import fract.ihm.MainWindow.Calcul;
import fract.lang.AdminMsg;
import fract.opt.EnumOption.BooleanEnum;
import fract.opt.EnumOption.IntEnum;
import fract.opt.EnumOption.StringEnum;

/**
 * <p>
 * Panel d'option de macro pour éditer une macro interpolations, .
 * </p>
 */
public class AdminGeneral extends JPanel implements ActionListener,
		CaretListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Instancie les éléments de base.
	 */
	public AdminGeneral() {
		this.setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(8, 2));

		// option langue
		this.Glang = new ButtonGroup();
		this.fr = new JRadioButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/fr_flag.gif"))); //$NON-NLS-1$
		this.fr.setText(AdminMsg.getString("AdminGeneral.1")); //$NON-NLS-1$
		this.fr.addActionListener(this);
		this.en = new JRadioButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/us_flag.jpg"))); //$NON-NLS-1$
		this.en.setText(AdminMsg.getString("AdminGeneral.3")); //$NON-NLS-1$
		this.en.addActionListener(this);
		this.Glang.add(this.fr);
		this.Glang.add(this.en);
		JPanel panLang = new JPanel();
		panLang.setLayout(new FlowLayout());
		panLang.add(this.fr);
		panLang.add(this.en, BorderLayout.EAST);

		// option fractal par défaut
		this.lstFract = new JComboBox(AdminIndex.getMw().getFractalsName());
		this.lstFract.setPreferredSize(new Dimension(220, 20));
		this.lstFract.addActionListener(this);
		this.lstFract.setSelectedItem(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.dftFractal));

		// bouton reset
		Breset = new JButton(AdminMsg.getString("AdminGeneral.4")); //$NON-NLS-1$
		Breset.addActionListener(this);

		// taille fenêtre
		Box panSize = Box.createHorizontalBox();
		this.width = new JTextField(AdminMsg.getString("AdminGeneral.5") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.horizSize));
		this.width.addCaretListener(this);
		this.height = new JTextField("" //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.vertSize));
		this.height.addCaretListener(this);
		panSize.add(this.width);
		panSize.add(new JLabel(" x ")); //$NON-NLS-1$
		panSize.add(this.height);

		// bounds
		Box panBounds = Box.createHorizontalBox();
		this.boundX = new JTextField("" //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.boundX));
		this.boundX.addCaretListener(this);
		this.boundY = new JTextField("" //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.boundY));
		this.boundY.addCaretListener(this);
		panBounds.add(new JLabel(" X=")); //$NON-NLS-1$
		panBounds.add(this.boundX);
		panBounds.add(new JLabel(" ; Y=")); //$NON-NLS-1$
		panBounds.add(this.boundY);

		// fullscreen
		this.fullscreen = new JCheckBox(AdminMsg.getString("AdminGeneral.12")); //$NON-NLS-1$
		this.fullscreen.setSelected(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.fullScreen));
		this.fullscreen.addActionListener(this);

		// infos
		this.infos = new JCheckBox("Activer à l'ouverture");
		this.infos
				.setSelected(BiomorphLauncher.tOpt.getIntValue(IntEnum.infos) > 0);
		this.infos.addActionListener(this);
		
		// ajout panel final
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.13"))); //$NON-NLS-1$
		grid.add(panLang);
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.14"))); //$NON-NLS-1$
		grid.add(this.lstFract);
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.15"))); //$NON-NLS-1$
		grid.add(this.Breset);
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.16"))); //$NON-NLS-1$
		grid.add(panSize);
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.17"))); //$NON-NLS-1$
		grid.add(panBounds);
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.18"))); //$NON-NLS-1$
		grid.add(this.fullscreen);
		grid.add(new JLabel(AdminMsg.getString("AdminGeneral.66"))); //$NON-NLS-1$
		grid.add(this.infos);

		// glue
		Box b = Box.createVerticalBox();
		b.add(grid);
		b.add(Box.createRigidArea(new Dimension(20, 120)));

		grid.setPreferredSize(new Dimension(330, 180));
		this.add(new JLabel(AdminMsg.getString("AdminGeneral.19")), //$NON-NLS-1$
				BorderLayout.NORTH);
		this.add(b);
	}

	/**
	 * Ecoute le bouton reset, Option de langue, checkbox de plein écran, le
	 * ComboBox de sélection de fractal.
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		// reset config
		if (src == this.Breset) {
			if (JOptionPane.showConfirmDialog(this, AdminMsg
					.getString("AdminGeneral.20")) == JOptionPane.OK_OPTION) { //$NON-NLS-1$
				try {
					BiomorphLauncher.gOpt.createInitConfigFile();
					JOptionPane.showMessageDialog(this, AdminMsg
							.getString("AdminGeneral.21"), //$NON-NLS-1$
							AdminMsg.getString("AdminGeneral.22"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
					AdminIndex.infoAdmin.setText(AdminMsg
							.getString("AdminGeneral.23")); //$NON-NLS-1$
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, AdminMsg
							.getString("AdminGeneral.24"), //$NON-NLS-1$
							AdminMsg.getString("AdminGeneral.25"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		// changement de langue
		else if (src == this.fr || src == this.en) {
			BiomorphLauncher.gOpt.changeOption("language", //$NON-NLS-1$
					(src == this.fr) ? "\"fr\"" : "\"en\""); //$NON-NLS-1$ //$NON-NLS-2$
			BiomorphLauncher.tOpt.modifierOption("language", //$NON-NLS-1$
					(src == this.fr) ? "fr" : "en"); //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane.showMessageDialog(this, AdminMsg
					.getString("AdminGeneral.32"), //$NON-NLS-1$
					AdminMsg.getString("AdminGeneral.33"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
		}
		// fullscreen
		else if (src == this.fullscreen) {
			BiomorphLauncher.gOpt.changeOption("fullScreen", this.fullscreen //$NON-NLS-1$
					.isSelected());
			BiomorphLauncher.tOpt.modifierOption("fullScreen", this.fullscreen //$NON-NLS-1$
					.isSelected());
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminGeneral.36") //$NON-NLS-1$
					+ ((this.fullscreen.isSelected()) ? AdminMsg
							.getString("AdminGeneral.37") //$NON-NLS-1$
							: AdminMsg.getString("AdminGeneral.38")) //$NON-NLS-1$
					+ AdminMsg.getString("AdminGeneral.39")); //$NON-NLS-1$
		}
		// sélection fractal par défaut
		else if (src == this.lstFract) {
			String s = this.lstFract.getSelectedItem().toString();
			if (!s.startsWith("----")) { //$NON-NLS-1$
				BiomorphLauncher.gOpt.changeOption("dftFractal", "\"" + s //$NON-NLS-1$ //$NON-NLS-2$
						+ "\""); //$NON-NLS-1$
				BiomorphLauncher.tOpt.modifierOption("dftFractal", s); //$NON-NLS-1$
				AdminIndex.infoAdmin.setText(AdminMsg
						.getString("AdminGeneral.0") + s); //$NON-NLS-1$
			} else
				AdminIndex.infoAdmin.setText(AdminMsg
						.getString("AdminGeneral.46") + s //$NON-NLS-1$
						+ ")"); //$NON-NLS-1$
		}
		// informations à l'ouverture
		else if (src == this.infos) {
			BiomorphLauncher.gOpt.changeOption(
					"infos", this.infos.isSelected() ? 1 : -1); //$NON-NLS-1$
			BiomorphLauncher.tOpt.modifierOption(
					"infos", this.infos.isSelected() ? 1 : -1); //$NON-NLS-1$
			AdminIndex.infoAdmin
					.setText(AdminMsg.getString("AdminGeneral.67") + this.infos.isSelected()); //$NON-NLS-1$
		}
	}

	/**
	 * Ecoute caractères dans les champs des cases taille de la fenêtre et
	 * position de la fenêtre.
	 */
	public void caretUpdate(CaretEvent ev) {
		int val = 0;
		String champ = ""; //$NON-NLS-1$s
		Object src = ev.getSource();
		AdminIndex.getMw().manageCalcul(Calcul.arret);
		try {
			if (src == this.width) {
				champ = AdminMsg.getString("AdminGeneral.2"); //$NON-NLS-1$
				val = Integer.parseInt(this.width.getText());
				AdminIndex.getMw().setSize(
						new Dimension(val, AdminIndex.getMw().getHeight()));
				BiomorphLauncher.gOpt.changeOption("horizSize", val); //$NON-NLS-1$
				BiomorphLauncher.tOpt.modifierOption("horizSize", val); //$NON-NLS-1$
			} else if (src == this.height) {
				champ = AdminMsg.getString("AdminGeneral.52"); //$NON-NLS-1$
				val = Integer.parseInt(this.height.getText());
				AdminIndex.getMw().setSize(
						new Dimension(AdminIndex.getMw().getWidth(), val));
				BiomorphLauncher.gOpt.changeOption("vertSize", val); //$NON-NLS-1$
				BiomorphLauncher.tOpt.modifierOption("vertSize", val); //$NON-NLS-1$
			} else {
				if (src == this.boundX) {
					champ = AdminMsg.getString("AdminGeneral.6"); //$NON-NLS-1$
					val = Integer.parseInt(this.boundX.getText());
					AdminIndex.getMw().setLocation(val,
							(int) AdminIndex.getMw().getLocation().getY());
					BiomorphLauncher.gOpt.changeOption("boundX", val); //$NON-NLS-1$
					BiomorphLauncher.tOpt.modifierOption("boundX", val); //$NON-NLS-1$
				} else if (src == this.boundY) {
					champ = AdminMsg.getString("AdminGeneral.58"); //$NON-NLS-1$
					val = Integer.parseInt(this.boundY.getText());
					AdminIndex.getMw().setLocation(
							(int) AdminIndex.getMw().getLocation().getX(), val);
					BiomorphLauncher.gOpt.changeOption("boundY", val); //$NON-NLS-1$
					BiomorphLauncher.tOpt.modifierOption("boundY", val); //$NON-NLS-1$
				}
			}
			AdminIndex.infoAdmin
					.setText(AdminMsg.getString("AdminGeneral.61") + champ //$NON-NLS-1$
							+ AdminMsg.getString("AdminGeneral.62") + val + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (NumberFormatException e) {
			AdminIndex.infoAdmin
					.setText(AdminMsg.getString("AdminGeneral.64") + champ //$NON-NLS-1$
							+ AdminMsg.getString("AdminGeneral.65")); //$NON-NLS-1$
		}
	}

	final private JCheckBox fullscreen, infos;
	final private JButton Breset;
	final private JRadioButton fr, en;
	final private ButtonGroup Glang;
	final private JComboBox lstFract;
	final private JTextField width, height, boundX, boundY;
}
