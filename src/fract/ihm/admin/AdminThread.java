package fract.ihm.admin;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fract.BiomorphLauncher;
import fract.lang.AdminMsg;
import fract.opt.EnumOption.IntEnum;
import fract.opt.EnumOption.BooleanEnum;

import java.awt.event.*;
import java.awt.*;

/**
 * <p>
 * Onglet de gestion du moteur de rendu. Donne des options graphiquement
 * uniquement concernées par le rendu.
 * </p>
 */
final public class AdminThread extends JPanel implements ActionListener,
		ChangeListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Construit le panel graphique avec composants et pose les écouteurs. Les
	 * composants concernent :
	 * </p>
	 * <ul>
	 * <li>Le nombre de threads à lancer</li>
	 * <li>Le type de rendu</li>
	 * </ul>
	 */
	public AdminThread() {
		nbCore = Runtime.getRuntime().availableProcessors();
		// composants threads
		this.infosTh = new JLabel(AdminMsg.getString("AdminThread.0") //$NON-NLS-1$
				+ nbCore);
		this.infosTh.setBackground(Color.yellow);
		this.infosTh
				.setToolTipText("<html>" //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.2") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.3") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.4") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.5")); //$NON-NLS-1$
		this.thAuto = new JCheckBox(AdminMsg.getString("AdminThread.6") + nbCore //$NON-NLS-1$
				+ "x2)"); //$NON-NLS-1$
		this.thAuto.addActionListener(this);
		this.thAuto.setToolTipText("<html>" //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.9") //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.10") //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.11") //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.12") //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.13") //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.14")); //$NON-NLS-1$
		this.perso = new JLabel(AdminMsg.getString("AdminThread.15")); //$NON-NLS-1$
		this.perso
				.setToolTipText("<html>" //$NON-NLS-1$
						+ "<font color=\"red\">" //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.18") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.19") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.20") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.21") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.22") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.23") //$NON-NLS-1$
						+ AdminMsg.getString("AdminThread.24")); //$NON-NLS-1$
		this.nbThreads = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		this.nbThreads.setToolTipText(this.perso.getToolTipText());
		this.nbThreads.setMinorTickSpacing(1);
		this.nbThreads.setMajorTickSpacing(10);
		this.nbThreads.setPaintTicks(true);
		this.nbThreads.setPaintLabels(true);
		this.nbThreads.addChangeListener(this);
		this.nbThreads.setValue(BiomorphLauncher.tOpt
				.getIntValue(IntEnum.nbThread));
		if (BiomorphLauncher.tOpt.getIntValue(IntEnum.nbThread) == 0) {
			this.thAuto.setSelected(true);
			this.nbThreads.setEnabled(false);
		}

		// composant sens de rendu
		this.sens = new ButtonGroup();
		this.vert = new JRadioButton(AdminMsg.getString("AdminThread.25")); //$NON-NLS-1$
		this.vert.setSelected(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.verticalRender));
		this.hor = new JRadioButton(AdminMsg.getString("AdminThread.26")); //$NON-NLS-1$
		this.hor.setSelected(!(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.verticalRender)));
		this.vert.addActionListener(this);
		this.hor.addActionListener(this);
		this.sens.add(this.vert);
		this.sens.add(this.hor);

		// composant moteur de rendu
		this.engine = new ButtonGroup();
		this.prog = new JRadioButton(AdminMsg.getString("AdminThread.27")); //$NON-NLS-1$
		this.prog.setSelected(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.progressive));
		this.lin = new JRadioButton(AdminMsg.getString("AdminThread.28")); //$NON-NLS-1$
		this.lin.setSelected(!(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.progressive)));
		this.prog.addActionListener(this);
		this.lin.addActionListener(this);
		this.engine.add(this.prog);
		this.engine.add(this.lin);

		// panneau de précision
		final JPanel panPreci = new JPanel();
		final String tttpreci = "<html><div style=\"width:300px;\">" //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.30") //$NON-NLS-1$
				+ AdminMsg.getString("AdminThread.31") //$NON-NLS-1$
				+ "</div></html>"; //$NON-NLS-1$
		panPreci.setLayout(new BorderLayout());
		final JLabel Lpreci = new JLabel(AdminMsg.getString("AdminThread.33")); //$NON-NLS-1$
		Lpreci.setToolTipText(tttpreci);
		this.preci = new JSlider(JSlider.HORIZONTAL, 1, 25, 1);
		this.preci.setValue(BiomorphLauncher.tOpt
				.getIntValue(IntEnum.precision));
		this.preci.setToolTipText(""); //$NON-NLS-1$
		this.preci.setMinorTickSpacing(1);
		this.preci.setToolTipText(tttpreci);
		this.preci.addChangeListener(this);
		panPreci.add(Lpreci, BorderLayout.WEST);
		panPreci.add(preci);
		this.testRender = new JButton(AdminMsg.getString("AdminThread.35")); //$NON-NLS-1$
		this.testRender.addActionListener(this);

		// pane threads
		Box box1 = Box.createVerticalBox();
		box1.setBorder(BorderFactory.createTitledBorder(AdminMsg.getString("AdminThread.36"))); //$NON-NLS-1$
		Component c[] = { this.infosTh, this.thAuto, this.perso, this.nbThreads };
		for (Component cc : c)
			box1.add(cc);

		// panel sélection moteur
		JPanel jp1 = new JPanel();
		jp1.setBorder(BorderFactory.createTitledBorder(AdminMsg.getString("AdminThread.37"))); //$NON-NLS-1$
		jp1.setLayout(new GridLayout(1, 2));
		jp1.add(this.prog);
		jp1.add(this.lin);

		// panel de sens de rendu
		JPanel jp2 = new JPanel();
		jp2.setBorder(BorderFactory.createTitledBorder(AdminMsg.getString("AdminThread.38"))); //$NON-NLS-1$
		jp2.setLayout(new GridLayout(1, 2));
		jp2.add(this.hor);
		jp2.add(this.vert);
		this.activeOptionRender();

		// ajout au panneau
		Box box = Box.createVerticalBox();
		box.setPreferredSize(new Dimension(320, 310));
		box.add(box1);
		box.add(jp1);
		box.add(jp2);
		box.add(panPreci);
		box.add(this.testRender);
		this.add(box);
	}

	/**
	 * <p>
	 * S'occupe de griser/ dégriser les options de rendu innactifs si un autre
	 * de moteur de rendu est sélectionné.
	 * </p>
	 */
	private void activeOptionRender() {
		this.hor.setEnabled(!BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.progressive));
		this.vert.setEnabled(!BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.progressive));
	}

	/**
	 * <p>
	 * Ecoute les composants du type JRadioButton, JSlider.
	 * </p>
	 */
	public void actionPerformed(final ActionEvent ev) {
		Object src = ev.getSource();
		// horizontale / vertical
		if (src == this.vert || src == this.hor) {
			BiomorphLauncher.gOpt.changeOption("verticalRender", (this.vert //$NON-NLS-1$
					.isSelected()) ? "on" : "off"); //$NON-NLS-1$ //$NON-NLS-2$
			BiomorphLauncher.tOpt.modifierOption("verticalRender", (this.vert //$NON-NLS-1$
					.isSelected()) ? true : false);
			AdminIndex.getMw().getRenderManager().updateSliders(null);
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminThread.43") //$NON-NLS-1$
					+ ((this.vert.isSelected()) ? AdminMsg.getString("AdminThread.7") : AdminMsg.getString("AdminThread.1")) //$NON-NLS-1$ //$NON-NLS-2$
					+ AdminMsg.getString("AdminThread.46")); //$NON-NLS-1$
		} // nombre de threads
		else if (src == this.thAuto) {
			this.nbThreads.setEnabled(!this.thAuto.isSelected());
			if (this.thAuto.isSelected()) {
				BiomorphLauncher.gOpt.changeOption("nbThread", 0); //$NON-NLS-1$
				BiomorphLauncher.tOpt.modifierOption("nbThread", 0); //$NON-NLS-1$
			} else {
				BiomorphLauncher.gOpt.changeOption("nbThread", this.nbThreads //$NON-NLS-1$
						.getValue());
				BiomorphLauncher.tOpt.modifierOption("nbThread", this.nbThreads //$NON-NLS-1$
						.getValue());
			}
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminThread.51") //$NON-NLS-1$
					+ ((this.thAuto.isSelected()) ? AdminMsg.getString("AdminThread.52") : AdminMsg.getString("AdminThread.53") //$NON-NLS-1$ //$NON-NLS-2$
							+ this.nbThreads.getValue()));

		} // rendu progressif / linéaire
		else if (src == this.lin || src == this.prog) {
			BiomorphLauncher.gOpt.changeOption(AdminMsg.getString("AdminThread.54"), (this.prog //$NON-NLS-1$
					.isSelected()) ? "on" : "off"); //$NON-NLS-1$ //$NON-NLS-2$
			BiomorphLauncher.tOpt.modifierOption(AdminMsg.getString("AdminThread.57"), (this.prog //$NON-NLS-1$
					.isSelected()) ? true : false);
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminThread.58") //$NON-NLS-1$
					+ ((this.prog.isSelected()) ? AdminMsg.getString("AdminThread.59") : AdminMsg.getString("AdminThread.60")) //$NON-NLS-1$ //$NON-NLS-2$
					+ AdminMsg.getString("AdminThread.61")); //$NON-NLS-1$
		} else if (src == this.testRender) {
			AdminIndex.getMw().launchCalculAndDisplay(true);
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminThread.62")); //$NON-NLS-1$
		}
		this.activeOptionRender();
		AdminIndex.getMw().calculerNbThread();
	}

	/**
	 * <p>
	 * S'occupe du composant JSlider (nombre de threads)
	 * </p>
	 */
	public void stateChanged(ChangeEvent ev) {
		if (ev.getSource() == this.nbThreads) {
			BiomorphLauncher.gOpt.changeOption("nbThread", this.nbThreads //$NON-NLS-1$
					.getValue());
			BiomorphLauncher.tOpt.modifierOption("nbThread", this.nbThreads //$NON-NLS-1$
					.getValue());
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminThread.65") //$NON-NLS-1$
					+ this.nbThreads.getValue());
			AdminIndex.getMw().calculerNbThread();
		} else if (ev.getSource() == this.preci) {
			BiomorphLauncher.gOpt.changeOption("precision", this.preci //$NON-NLS-1$
					.getValue());
			BiomorphLauncher.tOpt.modifierOption("precision", this.preci //$NON-NLS-1$
					.getValue());
			AdminIndex.infoAdmin.setText(AdminMsg.getString("AdminThread.68") //$NON-NLS-1$
					+ this.preci.getValue());
			AdminIndex.getMw().getToolBars().selectNvRender
					.setSelectedIndex(this.preci.getValue() - 1);
		}
	}

	// sélection nombre de thread
	final private JLabel infosTh, perso;
	final private JCheckBox thAuto;
	final private JSlider nbThreads, preci;
	final private int nbCore;

	// rendu horizontal ou vertical
	final private JRadioButton hor, vert, prog, lin;
	final private ButtonGroup sens, engine;
	final private JButton testRender;
}
