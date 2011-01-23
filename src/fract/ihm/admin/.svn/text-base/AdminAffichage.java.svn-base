package fract.ihm.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import fract.BiomorphLauncher;
import fract.lang.AdminMsg;
import fract.opt.EnumOption.BooleanEnum;

/**
 * <p>
 * Panel d'option de macro pour éditer une macro interpolations, .
 * </p>
 */
public class AdminAffichage extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Instancie les éléments de base.
	 */
	public AdminAffichage() {
		this.setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(this.nomOpt.length, 1));

		// création composant
		this.aff = new JCheckBox[this.nomOpt.length];
		for (int i = 0; i < this.nomOpt.length; i++) {
			this.aff[i] = new JCheckBox(AdminMsg.getString("AdminMsg.0") + this.description[i]); //$NON-NLS-1$
			this.aff[i].setSelected(BiomorphLauncher.tOpt
					.getBooleanValue(this.benum[i]));
			this.aff[i].addActionListener(this);
			Box b = Box.createHorizontalBox();
			b.add(new JLabel(new ImageIcon(BiomorphLauncher.class
					.getResource("/icon/menu/" + img[i])))); //$NON-NLS-1$
			b.add(this.aff[i]);
			grid.add(b);
		}

		// ajout panel final
		this.add(new JLabel(AdminMsg.getString("AdminMsg.2")), //$NON-NLS-1$
				BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(grid);
		this.add(jsp);
	}

	/**
	 * Ecoute les JCheckbox d'affichage.
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		for (int i = 0; i < this.nomOpt.length; i++) {
			if (src == this.aff[i]) {
				BiomorphLauncher.gOpt.changeOption(this.nomOpt[i], this.aff[i]
						.isSelected());
				BiomorphLauncher.tOpt.modifierOption(this.nomOpt[i],
						this.aff[i].isSelected());
				AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminMsg.3") + this.description[i] //$NON-NLS-1$
						+ AdminMsg.getString("AdminMsg.4") //$NON-NLS-1$
						+ ((this.aff[i].isSelected()) ? AdminMsg.getString("AdminMsg.5") : AdminMsg.getString("AdminMsg.6")) //$NON-NLS-1$ //$NON-NLS-2$
						+ AdminMsg.getString("AdminMsg.7")); //$NON-NLS-1$

				// affichage instantanné
				if (i >= 6) // barre d'outils
					AdminIndex.getMw().getToolBars().setTbVisible(i - 6,
							this.aff[i].isSelected());
				else {
					switch (i) {
					case 0:
						AdminIndex.getMw().getMenu().setMenuVisible(
								this.aff[0].isSelected());
						break;
					case 1:
						AdminIndex.getMw().getToolBars().setVisibleAll(
								this.aff[1].isSelected());
						break;
					case 2:
					case 3:
						AdminIndex.getMw().getParams().setVisibleWindows(
								this.aff[2].isSelected(),
								this.aff[3].isSelected());
						break;
					case 4:
						AdminIndex.getMw().getBottomPanel().setRampVisible(
								this.aff[4].isSelected());
						break;
					case 5:
						AdminIndex.getMw().getBottomPanel().setStatutVisible(
								this.aff[5].isSelected());
						break;

					}
				}
				return;
			}
		}
	}

	final private JCheckBox[] aff;
	final private String[] nomOpt = { "menuBar", "toolBars", "planPan", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"equationEdit", "colorPan", "stateBar", "tbHisto", "tbStatut", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"tbTools", "tbIter", "tbColor", "tbColorAdv", "tbValid" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	final private String[] description = { AdminMsg.getString("AdminMsg.21"), AdminMsg.getString("AdminMsg.22"), //$NON-NLS-1$ //$NON-NLS-2$
			AdminMsg.getString("AdminMsg.23"), AdminMsg.getString("AdminMsg.24"), AdminMsg.getString("AdminMsg.25"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminMsg.getString("AdminMsg.26"), AdminMsg.getString("AdminMsg.27"), AdminMsg.getString("AdminMsg.28"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminMsg.getString("AdminMsg.29"), AdminMsg.getString("AdminMsg.30"), AdminMsg.getString("AdminMsg.31"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AdminMsg.getString("AdminMsg.32"), AdminMsg.getString("AdminMsg.33") }; //$NON-NLS-1$ //$NON-NLS-2$
	final private String[] img = { "menu.png", "tb.png", "navig.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"equation.png", "color2.png", "statut.png", "tb.png", "tb.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"tb.png", "tb.png", "tb.png", "tb.png", "tb.png" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	final private BooleanEnum[] benum = { BooleanEnum.menuBar,
			BooleanEnum.toolBars, BooleanEnum.planPan,
			BooleanEnum.equationEdit, BooleanEnum.colorPan,
			BooleanEnum.stateBar, BooleanEnum.tbHisto, BooleanEnum.tbStatut,
			BooleanEnum.tbTools, BooleanEnum.tbIter, BooleanEnum.tbColor,
			BooleanEnum.tbColorAdv, BooleanEnum.tbValid };

}
