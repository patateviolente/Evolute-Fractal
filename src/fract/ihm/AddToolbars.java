package fract.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fract.BiomorphLauncher;
import fract.color.DefaultColorSelector;
import fract.ihm.MainWindow.Calcul;
import fract.lang.Toolbars;
import fract.opt.EnumOption;
import fract.opt.EnumVue;
import fract.opt.StructSaveVue;
import fract.opt.EnumOption.BooleanEnum;
import fract.opt.EnumOption.IntEnum;

/**
 * <p>
 * Dessine les barres d'outils et les ajoute à la fenêtre principale. Les
 * écouteurs des barre d'outils sont traités ici.
 * </p>
 */
final public class AddToolbars extends JPanel implements ActionListener,
		KeyListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Dessine les barres d'outils, attribue les raccourcis clavier,
	 * toolTipTexts, icones etc...
	 * </p>
	 * 
	 * @param mw
	 *            La fenêtre principale.
	 */
	public AddToolbars(final MainWindow mw) {
		this.mw = mw;
		// boutons précédent/ suivant
		this.LabHisto = new JLabel(Toolbars.getString("AddToolbars.0")); //$NON-NLS-1$
		this.LabHisto
				.setToolTipText("<html><div width=\"250\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.2") //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.3") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.jb_prec = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_precedent.png"))); //$NON-NLS-1$
		this.jb_prec.addActionListener(this);
		this.jb_suiv = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_suivant.png"))); //$NON-NLS-1$
		this.jb_suiv.addActionListener(this);
		this.jb_addHV = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_addhistoryview.png"))); //$NON-NLS-1$
		this.jb_addHV
				.setToolTipText("<html><div width=\"230\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.9") //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.10") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.jb_addHV.addActionListener(this);

		// Pause/ lecture
		this.jb_actualize = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_actualize.png"))); //$NON-NLS-1$
		this.jb_actualize.addActionListener(this);
		this.jb_actualize.setToolTipText("<html>" //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.14") //$NON-NLS-1$
				+ "</html>"); //$NON-NLS-1$
		this.jb_lectpause = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_pause.png"))); //$NON-NLS-1$
		this.jb_lectpause
				.setToolTipText("<html><div width=\"230\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.18") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.jb_lectpause.addActionListener(this);
		this.jb_lectpause.setEnabled(false);
		this.jb_stop = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_arreter.png"))); //$NON-NLS-1$
		this.jb_stop.addActionListener(this);
		this.jb_stop.setToolTipText("<html>" //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.22") //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.23") + "</html>"); //$NON-NLS-1$ //$NON-NLS-2$

		// curseurs
		this.c_normal = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/cursor/normal.png"))); //$NON-NLS-1$
		this.c_normal.addActionListener(this);
		this.c_normal.setSelected(true);
		this.c_normal.setToolTipText("<html>" //$NON-NLS-1$
				+ "<div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.28") //$NON-NLS-1$
				+ "</div></html>"); //$NON-NLS-1$
		this.c_select = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/cursor/select.png"))); //$NON-NLS-1$
		this.c_select.addActionListener(this);
		this.c_select
				.setToolTipText("<html><div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.32") //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.33") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.c_move = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/cursor/move.png"))); //$NON-NLS-1$
		this.c_move.addActionListener(this);
		this.c_move
				.setToolTipText("<html><div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.37") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.c_rotate = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/cursor/rotation.png"))); //$NON-NLS-1$
		this.c_rotate.addActionListener(this);
		this.c_rotate
				.setToolTipText("<html><div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.41") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.show_axes = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_axes.png"))); //$NON-NLS-1$
		this.show_axes.addActionListener(this);
		this.show_axes
				.setToolTipText("<html><div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.45") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$

		// label itérations
		this.LabIt = new JLabel(Toolbars.getString("AddToolbars.47")); //$NON-NLS-1$
		this.LabIt.setToolTipText("" //$NON-NLS-1$
				+ "<html><div width=\"240\" style=\"text-align:justify;\">" //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.50") //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.51") //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.52") //$NON-NLS-1$
				+ "</div></html>"); //$NON-NLS-1$
		// texfield itérations
		this.TfIt = new JTextField();
		this.TfIt.setMargin(new Insets(0, 0, 0, 0));
		this.TfIt.addKeyListener(this);
		this.TfIt.setToolTipText(this.LabIt.getToolTipText());
		this.TfIt.setPreferredSize(new Dimension(55, 25));
		this.TfIt.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent ev) {
				try {
					Integer.parseInt(TfIt.getText());
					TfIt.setForeground(Color.black);
					TfIt.setBackground(Color.white);
				} catch (NumberFormatException e) {
					TfIt.setForeground(Color.red);
				}
			}
		});
		this.iterAuto = new JCheckBox(Toolbars.getString("AddToolbars.54")); //$NON-NLS-1$
		this.iterAuto.setSelected(false);
		this.iterAuto
				.setToolTipText("<html><div width=\"250\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.56") + //$NON-NLS-1$
						Toolbars.getString("AddToolbars.57") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		final JPopupMenu jpm = new JPopupMenu();
		this.jmi_copyI = new JMenuItem("[macro] Définir itération initiale");
		this.jmi_copyI.setIcon(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/first.png")));
		this.jmi_copyF = new JMenuItem("[macro] Définir itération finale");
		this.jmi_copyI.addActionListener(this);
		this.jmi_copyF.addActionListener(this);
		this.jmi_copyF.setIcon(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/last.png")));
		jpm.add(this.jmi_copyI);
		jpm.add(this.jmi_copyF);
		this.TfIt.setComponentPopupMenu(jpm);

		// checkbox tout recalculer
		this.repaintAfterValid = new JCheckBox(""); //$NON-NLS-1$
		this.repaintAfterValid.setSelected(true);
		this.repaintAfterValid.setToolTipText("" //$NON-NLS-1$
				+ "<html><div width=\"230\" style=\"text-align:justify;\">" //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.62") //$NON-NLS-1$
				+ Toolbars.getString("AddToolbars.63") //$NON-NLS-1$
				+ "</div></html>"); //$NON-NLS-1$
		// calcul partiel
		this.selectNvRender = new JComboBox(this.txtNvRender);
		this.selectNvRender.setSelectedIndex(BiomorphLauncher.tOpt
				.getIntValue(IntEnum.precision) - 1);
		this.selectNvRender
				.setToolTipText("<html><div width=\"240\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.66") //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.67") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		// label couleur
		this.LabColor = new JLabel(Toolbars.getString("AddToolbars.69")); //$NON-NLS-1$
		this.LabColor
				.setToolTipText("<html><div width=\"240\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.71") //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.72") //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.73") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$

		// carré couleur
		this.panColor = new JPanel();
		this.panColor.setPreferredSize(new Dimension(40, 15));
		this.panColor.setBackground(mw.getBgFractColor());
		this.panColor.setToolTipText(this.LabColor.getToolTipText());
		this.panColor.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new DefaultColorSelector(mw, 0);
			}
		});

		// alternate colors
		this.alternateColors = new JCheckBox(Toolbars
				.getString("AddToolbars.75")); //$NON-NLS-1$
		this.alternateColors.addActionListener(this);
		this.alternateColors
				.setToolTipText("<html><div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.77") + //$NON-NLS-1$
						Toolbars.getString("AddToolbars.78") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$

		mw.altCol1 = Color.white;
		mw.altCol2 = Color.black;
		this.panAltColor1 = new JPanel();
		this.panAltColor1.setBackground(mw.altCol1);
		this.panAltColor1.setPreferredSize(new Dimension(20, 15));
		this.panAltColor1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new DefaultColorSelector(mw, 1);
			}
		});
		this.panAltColor2 = new JPanel();
		this.panAltColor2.setBackground(mw.altCol2);
		this.panAltColor2.setPreferredSize(new Dimension(20, 15));
		this.panAltColor2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new DefaultColorSelector(mw, 2);
			}
		});

		// barre d'itération fixe
		this.activFixIter = new JCheckBox("It. fix");
		this.fixedIter = new JTextField(""
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.dftCsteIt));

		// valider
		this.butValider = new JButton(Toolbars.getString("AddToolbars.80")); //$NON-NLS-1$
		this.butValider
				.setToolTipText("<html><div width=\"200\" style=\"text-align:justify;\">" //$NON-NLS-1$
						+ Toolbars.getString("AddToolbars.82") //$NON-NLS-1$
						+ "</div></html>"); //$NON-NLS-1$
		this.butValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mw.manageCalcul(Calcul.arret);
				mw.appliFractOption();
			}
		});

		curs[0] = this.c_normal;
		curs[1] = this.c_select;
		curs[2] = this.c_move;
		curs[3] = this.c_rotate;

		// insertion jtb
		Box panelToolbars = Box.createHorizontalBox();
		Component[][] cJtb = {
				{ this.LabHisto, this.jb_prec, this.jb_suiv, this.jb_addHV },
				{ this.jb_stop, this.jb_actualize, this.jb_lectpause },
				{ this.c_normal, this.c_select, this.c_move, this.c_rotate,
						this.show_axes },
				{ this.LabIt, this.TfIt, this.iterAuto },
				{ this.LabColor, this.panColor, this.alternateColors,
						this.panAltColor1, this.panAltColor2 },
				{ this.fixedIter, this.activFixIter },
				{ this.selectNvRender, this.repaintAfterValid, this.butValider } };
		jtb = new JToolBar[this.jtbWidth.length];
		for (int i = 0; i < jtb.length; i++) {
			jtb[i] = new JToolBar();
			jtb[i].setPreferredSize(new Dimension(jtbWidth[i], 25));
			for (int u = 0; u < cJtb[i].length; u++) {
				this.jtb[i].add(cJtb[i][u]);
				if (i == 2 && u == 4)
					this.jtb[i].addSeparator();
			}
			panelToolbars.add(jtb[i]);
		}

		// visibilité par option des barre d'outils
		for (int i = 0; i < this.tbOpt.length; i++)
			this.setTbVisible(i, BiomorphLauncher.tOpt
					.getBooleanValue(this.tbOpt[i]));

		for (int i = 0; i < 10; i++)
			panelToolbars.add(Box.createHorizontalGlue());
		mw.add(panelToolbars, BorderLayout.NORTH);

	}

	/**
	 * <p>
	 * Met à jour le contenu de tous les composants graphiques depuis une
	 * structure d'enregistrement [nombre d'itérations, couleurs de fond,
	 * alternées].
	 * </p>
	 * 
	 * @param ssv
	 *            Structure de la vue à charger.
	 */
	public void updateComponents(final StructSaveVue ssv) {
		// mise à jour composants
		this.TfIt.setText(ssv.getIntValue(EnumVue.iter) + ""); //$NON-NLS-1$
		this.panColor.setBackground(ssv.getColorValue(EnumVue.background));
		this.panAltColor1.setBackground(ssv.getColorValue(EnumVue.alt1));
		this.panAltColor2.setBackground(ssv.getColorValue(EnumVue.alt2));
		// System.out.println(ssv.isColorAltern());
		this.alternateColors.setSelected(ssv.isColorAltern());
		this.setAlternateColors();

		// mise à jour valeurs dans mw
		this.mw.bg = ssv.getColorValue(EnumVue.background);
		this.mw.altCol1 = ssv.getColorValue(EnumVue.alt1);
		this.mw.altCol2 = ssv.getColorValue(EnumVue.alt2);
	}

	/**
	 * Rend toutes les barres d'outils visibles ou invisibleselon la checkbox du
	 * menu. Si toutes les barre sont invisibles, la zone de barre d'outils sera
	 * réduite.
	 */
	public void setVisibleAll(final boolean visible) {
		AddMenu menu = this.mw.getMenu();
		for (int i = 0; i < menu.menuCheckTB.length; i++) {
			if (BiomorphLauncher.tOpt.getBooleanValue(this.tbOpt[i])
					|| !visible) {
				menu.menuCheckTB[i].setSelected(menu.displayAllTb.isSelected());
				this.setTbVisible(i, visible);
			}
		}
	}

	/**
	 * <p>
	 * Met à jour les composants lors d'une activation/ désactivation de
	 * l'alternance des couleurs [ChooseRampColor disable, JCheckBox coché].
	 * </p>
	 */
	private void setAlternateColors() {
		if (this.alternateColors.isSelected()) {
			this.alternateColors.setBackground(Color.yellow);
			mw.getBottomPanel().setEnableColorComponents(false);
		} else {
			this.alternateColors.setBackground(null);
			mw.getBottomPanel().setEnableColorComponents(true);
		}
	}

	/**
	 * <p>
	 * Rend la barre d'outil au rang i visible ou invisible selon l'argument.
	 * Pour plus de facilités pour trouver le bon numéro, utilisez les
	 * constantes statiques commencant par TB_.
	 * </p>
	 * 
	 * @param i
	 *            La position de la barre d'outils (0 pour navigation, etc...)
	 * @param visible
	 *            True pour visible
	 */
	public void setTbVisible(final int i, final boolean visible) {
		this.jtb[i].setVisible(visible);
	}

	/**
	 * <p>
	 * Retourne la précision [1 pour un calcul complet, 2 pour un 1 pixel sur 4,
	 * 3 pour un pixel sur 8 etc...]
	 * </p>
	 * 
	 * @return precision en décalage de pixel.
	 */
	public int getPrecision() {
		return this.selectNvRender.getSelectedIndex() + 1;
	}

	/**
	 * @return la valeur du JCheckBox d'itération automatique.
	 */
	public boolean isIterAuto() {
		return this.iterAuto.isSelected();
	}

	/**
	 * @return la valeur du bouton.
	 */
	public boolean isGridActiv() {
		return this.show_axes.isSelected();
	}

	/**
	 * <p>
	 * Change la possibilité d'activer le bouton grille selon l'argument en
	 * entrée.
	 * </p>
	 * 
	 * @param val
	 *            true pour activer (appuyer) bouton grille
	 */
	public void setEnableGrids(final boolean val) {
		this.show_axes.setEnabled(val);
	}

	/**
	 * <p>
	 * Enlève la sélection à tous les boutons de sélection de curseur.
	 * </p>
	 */
	private void unselectAllCursorButton() {
		for (int i = 0; i < this.curs.length; i++)
			this.curs[i].setSelected(false);
	}

	/**
	 * <p>
	 * Ecoute les boutons des barres d'outils et fait appel à des actions
	 * simples.
	 * </p>
	 */
	public void actionPerformed(final ActionEvent ev) {
		Object src = ev.getSource();
		// PRECÉDENT SUIVANT / CLICHÉ
		if (src == this.jb_prec) {
			mw.goToHistoryView(1);
		} else if (src == this.jb_suiv) {
			mw.goToHistoryView(-1);
		} else if (src == this.jb_addHV) {
			if (mw.addHistoryView())
				BiomorphLauncher.writeSubAction(Toolbars
						.getString("AddToolbars.85")); //$NON-NLS-1$
		}

		// LECTURE PAUSE ARRET ACTUALISER
		else if (src == this.jb_actualize) {
			mw.manageCalcul(Calcul.arret);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mw.launchCalculAndDisplay(true);
		} else if (src == this.jb_lectpause) {
			if (mw.avancement < 100) {
				if (mw.enPause)
					mw.manageCalcul(Calcul.lecture);
				else
					mw.manageCalcul(Calcul.pause);
			}
		} else if (src == this.jb_stop)
			mw.manageCalcul(Calcul.arret);

		// ALTERNER COULEURS
		else if (src == this.alternateColors)
			this.setAlternateColors();

		// ACTIVER GRILLE
		else if (src == this.show_axes) {
			this.show_axes.setSelected(!this.show_axes.isSelected());
			this.mw.getPan().repaint();
		}

		// copie macro itération
		else if (src == jmi_copyI || src == jmi_copyF) {
			BiomorphLauncher.tOpt.modifierOption(
					(src == jmi_copyI) ? "MiterInit" : "MiterFin", Integer
							.parseInt(this.TfIt.getText()));
			BiomorphLauncher.gOpt.changeOption((src == jmi_copyI) ? "MiterInit"
					: "MiterFin", Integer.parseInt(this.TfIt.getText()));
			this.mw.getMacroEditor().updateLabelsAndValues();
		}

		// CURSEURS
		else {
			for (int i = 0; i < curs.length; i++) {
				if (src == curs[i]) {
					this.unselectAllCursorButton();
					this.curs[i].setSelected(true);
				}
			}
			this.mw.ctrlDown = curs[1].isSelected();
			this.mw.spaceDown = curs[2].isSelected();
			this.mw.shiftDown = curs[3].isSelected();
			this.mw.appliCursor();
		}
	}

	/**
	 * <p>
	 * Ecoute la validation du champs de texte de l'itération et lance un
	 * racalcul.
	 * </p>
	 */
	public void keyPressed(final KeyEvent ev) {
		if (ev.getSource() == this.TfIt && ev.getKeyCode() == KeyEvent.VK_ENTER) {
			mw.manageCalcul(Calcul.arret);
			mw.appliFractOption();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent ev) {
	}

	// toolbar curseurs
	final public JButton c_normal, c_select, c_move, c_rotate;
	final private JButton[] curs = new JButton[4];
	final private JButton show_axes;

	// itération fixe
	public JCheckBox activFixIter;
	public JTextField fixedIter;

	// correspondance énumération option tb
	final BooleanEnum[] tbOpt = { BooleanEnum.tbHisto, BooleanEnum.tbStatut,
			BooleanEnum.tbTools, BooleanEnum.tbIter, BooleanEnum.tbColor,
			BooleanEnum.tbColorAdv, BooleanEnum.tbValid };

	// TOOLBAR (=+Options fractales)
	final private MainWindow mw;
	final public JButton jb_addHV, jb_prec, jb_suiv, jb_actualize, jb_stop,
			jb_lectpause;
	final private JMenuItem jmi_copyI, jmi_copyF;

	final public JToolBar[] jtb;
	final private int[] jtbWidth = { 145, 110, 160, 190, 190, 150, 135 };
	final private JLabel LabHisto, LabIt, LabColor;
	final String[] txtNvRender = { "1/1", "1/4", "1/9", "1/16", "1/25", " *6", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			" *7", " *8", "1/9²", "1/100", " *11", " *12", " *13", " *14", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			"1/15²", " *16", " *17", " *18", " *19", "1/20²", " *21", " *22", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			" *23", " *24", "1/25²" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	final public JComboBox selectNvRender;
	final public JTextField TfIt;
	final public JCheckBox repaintAfterValid, alternateColors, iterAuto;
	final public JPanel panColor, panAltColor1, panAltColor2;
	final private JButton butValider;
	final public static int TB_NAVIG = 0, TB_RENDU = 1, TB_CURSOR = 2,
			TB_ITER = 3, TB_COLORS = 4, TB_ITER_ADV = 5, TB_VALID = 6;
}
