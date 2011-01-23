package fract.ihm.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fract.BiomorphLauncher;
import fract.lang.AdminSrc;
import fract.opt.EnumOption.StringEnum;

/**
 * <p>
 * Panel d'option de macro pour éditer une macro interpolations.
 * </p>
 */
public class AdminMacro extends JPanel implements CaretListener, ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Instancie les éléments de base.
	 */
	public AdminMacro() {
		this.setLayout(new BorderLayout());
		this
				.add(
						new JLabel(
								"<html><body style=\"200px; text-align:justify;\">" //$NON-NLS-1$
										+ AdminSrc.getString("AdminMacro.1") //$NON-NLS-1$
										+ AdminSrc.getString("AdminMacro.2") //$NON-NLS-1$
										+ "</body></html>"), BorderLayout.NORTH); //$NON-NLS-1$
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(5, 2));

		// composants
		this.openM = new JButton(AdminSrc.getString("AdminMacro.4")); //$NON-NLS-1$
		this.openM.addActionListener(this);
		this.mainVar = new JComboBox(this.Sdescription);
		this.mainVar.addActionListener(this);
		this.prefix = new JTextField(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.MimgName));
		this.prefix.addCaretListener(this);
		this.pas = new JTextField(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.MrenderPas));
		this.pas.addCaretListener(this);
		this.activ1 = new JButton(AdminSrc.getString("AdminMacro.5")); //$NON-NLS-1$
		this.activ1.addActionListener(this);
		this.activ2 = new JButton(AdminSrc.getString("AdminMacro.6")); //$NON-NLS-1$
		this.activ2.addActionListener(this);

		// selected item de mainVar
		for (int i = 0; i < this.Svar.length; i++)
			if (this.Svar[i].equals(BiomorphLauncher.tOpt
					.getStringValue(StringEnum.MmainVar)))
				this.mainVar.setSelectedIndex(i);

		// ajout
		Box centre = Box.createVerticalBox();
		pan.add(new JLabel(AdminSrc.getString("AdminMacro.7"))); //$NON-NLS-1$
		pan.add(this.mainVar);
		pan.add(new JLabel(AdminSrc.getString("AdminMacro.8"))); //$NON-NLS-1$
		pan.add(this.pas);
		pan.add(new JLabel(AdminSrc.getString("AdminMacro.9"))); //$NON-NLS-1$
		pan.add(this.prefix);
		pan.add(new JLabel(AdminSrc.getString("AdminMacro.10"))); //$NON-NLS-1$
		pan.add(this.activ1);
		pan.add(new JLabel(AdminSrc.getString("AdminMacro.11"))); //$NON-NLS-1$
		pan.add(this.activ2);
		centre.add(pan);
		centre.add(Box.createVerticalGlue());

		this.add(centre);
		this.add(this.openM, BorderLayout.SOUTH);
	}

	/**
	 * Surveille activité de la liste de variable d'animation, des boutons de
	 * désactivation d'animation de variable secondaires.
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		if (src == this.mainVar) {
			String s = this.Svar[this.mainVar.getSelectedIndex()];
			BiomorphLauncher.tOpt.modifierOption("MmainVar", s); //$NON-NLS-1$
			BiomorphLauncher.gOpt.changeOption("MmainVar", "\"" + s + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminIndex.infoAdmin.setText(AdminSrc.getString("AdminMacro.16") + s); //$NON-NLS-1$
		} else if(src == this.openM){
			AdminIndex.getMw().getMacroEditor().setVisible(true);
			AdminIndex.getMw().getMenu().updateCheckBoxes();
		}
		else {
			BiomorphLauncher.tOpt.modifierOption("Mvar" //$NON-NLS-1$
					+ ((src == this.activ1) ? "1" : "2") + "offs", -1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			BiomorphLauncher.gOpt.changeOption("Mvar" //$NON-NLS-1$
					+ ((src == this.activ1) ? "1" : "2") + "offs", -1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminIndex.infoAdmin.setText(AdminSrc.getString("AdminMacro.25") //$NON-NLS-1$
					+ ((src == this.activ1) ? AdminSrc.getString("AdminMacro.26") : AdminSrc.getString("AdminMacro.27")) + AdminSrc.getString("AdminMacro.28")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		AdminIndex.getMw().getMacroEditor().updateLabelsAndValues();
	}

	/**
	 * Surveille l'activité du changement de nom du préfixe d'image, du pas de
	 * rendu de la variables principale.
	 */
	public void caretUpdate(CaretEvent ev) {
		Object src = ev.getSource();
		if (src == this.prefix) {
			String n = this.prefix.getText().replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
			BiomorphLauncher.tOpt.modifierOption("MimgName", n); //$NON-NLS-1$
			BiomorphLauncher.gOpt.changeOption("MimgName", "\"" + n + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminIndex.infoAdmin.setText(AdminSrc.getString("AdminMacro.35") + n); //$NON-NLS-1$
		} else if (src == this.pas) {
			try {
				Double.parseDouble(this.pas.getText());
				String n = this.pas.getText();
				BiomorphLauncher.tOpt.modifierOption("MrenderPas", n); //$NON-NLS-1$
				BiomorphLauncher.gOpt.changeOption("MrenderPas", "\"" + n //$NON-NLS-1$ //$NON-NLS-2$
						+ "\""); //$NON-NLS-1$
				AdminIndex.infoAdmin.setText(AdminSrc.getString("AdminMacro.40") + n); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				AdminIndex.infoAdmin
						.setText(AdminSrc.getString("AdminMacro.41")); //$NON-NLS-1$
			}
		}
		AdminIndex.getMw().getMacroEditor().updateLabelsAndValues();
	}

	// Composants
	final private JComboBox mainVar;
	final private String[] Sdescription = { AdminSrc.getString("AdminMacro.42"), AdminSrc.getString("AdminMacro.43"), //$NON-NLS-1$ //$NON-NLS-2$
			AdminSrc.getString("AdminMacro.44"), AdminSrc.getString("AdminMacro.45"), AdminSrc.getString("AdminMacro.46"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminSrc.getString("AdminMacro.47") }; //$NON-NLS-1$
	final private String[] Svar = { "MposX", "MposY", "Mscale", "Miter", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"Mvar1", "Mvar2" }; //$NON-NLS-1$ //$NON-NLS-2$
	final private JTextField prefix, pas;
	final private JButton activ1, activ2, openM;
}
