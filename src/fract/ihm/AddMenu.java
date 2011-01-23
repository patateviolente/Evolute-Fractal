package fract.ihm;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.*;
import fract.BiomorphLauncher;
import fract.ihm.admin.AdminIndex;
import fract.lang.Menu;
import fract.opt.EnumOption.BooleanEnum;

/**
 * <p>
 * Panel de dessin de la JToolbar, ajoute la JToolbar à l'élément MainWindow en
 * entrée. Les écouteurs du menu sont traités dans la classe même.
 * </p>
 */
final public class AddMenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Dessine le menu. Attribut les images, les séparateurs, les raccourcis
	 * clavier (Accelerators).
	 * </p>
	 * 
	 * @param mw
	 *            La fenêtre principale.
	 */
	public AddMenu(final MainWindow mw) {
		this.mw = mw;

		// //////////////////////////////////////
		// Affichage du MENU
		this.barre = new JMenuBar();
		mw.setJMenuBar(barre);
		for (int i = 0; i < menuTtxt.length; i++) {
			menuT[i] = new JMenu(menuTtxt[i]);
			// désactiv recréation de miniature pour éviter repaint intempestifs
			menuT[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent ev) {
					mw.getPan().onPanel = false;
				}
			});
			barre.add(menuT[i]);
			// on parcourt toutes les entrées
			for (int u = 0; u < menuItxt[i].length; u++) {
				if (u == 0) // initialisation du nombre d'entrées dans le menu
					menuI[i] = new JMenuItem[menuItxt[i].length];

				if (i == 2 && u < 8) { // menu spécial en JmenuButtonMenuItem
					if (u == 7) { // sous menu toolbars
						this.barreJtb = new JMenu(menuItxt[i][u]);
						for (int w = 0; w < menuCheckTB.length; w++) {
							menuCheckTB[w] = new JCheckBoxMenuItem(
									this.txtJtb[w]);
							menuCheckTB[w].setSelected(true);
							menuCheckTB[w].addActionListener(this);
							barreJtb.add(menuCheckTB[w]);
							barreJtb.setIcon(new ImageIcon(
									BiomorphLauncher.class
											.getResource("/icon/menu/" //$NON-NLS-1$
													+ this.imgMenu[i][u])));
						}
						barreJtb.addSeparator();
						barreJtb.add(this.displayAllTb);
						this.displayAllTb.addActionListener(this);
						this.displayAllTb.setSelected(BiomorphLauncher.tOpt
								.getBooleanValue(BooleanEnum.toolBars));
						this.displayAllTb.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_T,
										InputEvent.SHIFT_MASK));
						barre.add(menuT[i]);
						menuT[i].add(this.barreJtb);
					} else {
						// menu checkbox interface
						menuRBMI[u] = new JCheckBoxMenuItem(menuItxt[i][u]);
						menuRBMI[u].addActionListener(this);
						menuT[i].add(menuRBMI[u]);
						menuRBMI[u].setSelected(true);
						if (this.imgMenu[i][u] != null)
							this.menuRBMI[u].setIcon(new ImageIcon(
									BiomorphLauncher.class
											.getResource("/icon/menu/" //$NON-NLS-1$
													+ this.imgMenu[i][u])));
						if (this.raccMenu[i][u] != null)
							this.menuRBMI[u].setAccelerator(raccMenu[i][u]);
					}
				} else { // menu normal
					menuI[i][u] = new JMenuItem(menuItxt[i][u]);
					menuI[i][u].addActionListener(this);
					menuT[i].add(menuI[i][u]);
					// racourcis éventuel
					if (this.raccMenu[i][u] != null)
						this.menuI[i][u].setAccelerator(raccMenu[i][u]);
					// image décorative
					if (this.imgMenu[i][u] != null)
						this.menuI[i][u].setIcon(new ImageIcon(
								BiomorphLauncher.class
										.getResource("/icon/menu/" //$NON-NLS-1$
												+ this.imgMenu[i][u])));

				}
				// ajouter séparateur
				if (this.menuSeparator[i][u])
					this.menuT[i].addSeparator();
			}
		}
	}

	/**
	 * @return Barre de menu
	 */
	public JMenuBar getMenuBar() {
		return this.barre;
	}

	/**
	 * <p>
	 * Rend la barre de menu visible ou invisible selon l'argument en entrée.
	 * </p>
	 * 
	 * @param visible
	 *            Vrai pour visible, faux sinon.
	 */
	public void setMenuVisible(boolean visible) {
		System.out.println(Menu.getString("AddMenu.3") + visible); //$NON-NLS-1$
		this.barre.setVisible(visible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#isVisible()
	 */
	public boolean isVisible() {
		return this.barre.isVisible();
	}

	/**
	 * Ecoute tous les éléments du menu et exécute des appels simples.
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		// AFFICHAGE BARRES DOUTILS
		for (int i = 0; i < this.menuCheckTB.length; i++)
			if (src == this.menuCheckTB[i]) {
				mw.getToolBars().setTbVisible(i,
						this.menuCheckTB[i].isSelected());
				return;
			}
		// TOUT AFFICHER/ MASQUER BARRE D'OUTILS
		if (src == this.displayAllTb) {
			this.mw.getToolBars().setVisibleAll(this.displayAllTb.isSelected());
		}
		// OPEN WINDOW
		else if (src == menuI[0][0]) {
			BiomorphLauncher.writeProcedure(Menu.getString("AddMenu.4")); //$NON-NLS-1$
			new MainWindow();
		} // SAUVEGARDER VUE
		else if (src == menuI[0][1]) {
			BiomorphLauncher.writeProcedure(Menu.getString("AddMenu.5")); //$NON-NLS-1$
			this.mw.saveFractal();
		}// IMPORTER VUE
		else if (src == menuI[0][2]) {
			BiomorphLauncher.writeProcedure(Menu.getString("AddMenu.6")); //$NON-NLS-1$
			new OpenVue(this.mw);
		} // import instructions txt
		else if (src == menuI[0][3]) {
			BiomorphLauncher.writeProcedure(Menu.getString("AddMenu.7")); //$NON-NLS-1$
			new ImportInstruction(this.mw);
		} // export instructions txt
		else if (src == menuI[0][4]) {
			BiomorphLauncher.writeProcedure(Menu.getString("AddMenu.8")); //$NON-NLS-1$
			new ExportInstruction(this.mw);
		}// EXPORTER IMAGE
		else if (src == menuI[0][5]) {
			System.out.println(Menu.getString("AddMenu.9")); //$NON-NLS-1$
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showDialog(this, Menu.getString("AddMenu.10")); //$NON-NLS-1$
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String nomFichier = fc.getSelectedFile().toString();
				// System.out.println(nomFichier);
				if (!fc.getSelectedFile().toString().endsWith(".png")) //$NON-NLS-1$
					nomFichier += ".png"; //$NON-NLS-1$
				try {
					ImageIO.write(mw.getPan().buff, "png", //$NON-NLS-1$
							new File(nomFichier));
					System.out.println(Menu.getString("AddMenu.14")); //$NON-NLS-1$
				} catch (Exception e) {
					System.out.println(Menu.getString("AddMenu.15")); //$NON-NLS-1$
					e.printStackTrace();
				}
			}
		} // DISPOSER FENÊTRE
		else if (src == menuI[0][6]) {
			mw.dispose();
		} // ARRET COMPLET
		else if (src == menuI[0][7]) {
			BiomorphLauncher.writeProcedure(Menu.getString("AddMenu.16")); //$NON-NLS-1$
			System.exit(0);
		} // COPIER VERS PRESSEPAPIER
		else if (src == menuI[1][0]) { // ouverture de l'administration
			this.mw.getPan().copyPanToClipoard();
		}// ADMINISTRATION
		else if (src == menuI[1][1]) { // redim panneau
			new RedimPanneau(this.mw);
		} else if (src == menuI[1][2]) { // plein écran
			new FullScreenFractal(this.mw);
		} else if (src == menuI[1][3]) { // ouverture de l'administration
			new AdminIndex(this.mw);
		} // AFF PANNEAU COULEUR
		else if (src == menuRBMI[0] || src == menuRBMI[1]) {
			mw.getParams().setVisibleWindows(menuRBMI[0].isSelected(),
					menuRBMI[1].isSelected());
		} // DISPLAY NETWORK RENDER
		else if (src == menuRBMI[2]) {
			if (BiomorphLauncher.windows)
				JOptionPane
						.showMessageDialog(
								this.mw,
								"Désolé, le rendu réseau est impossible sous Windows.\nLe logiciel exploite des script bash seulement pour les systèmes UNIX.",
								"Rendu multimachine impossible sous Windows",
								JOptionPane.INFORMATION_MESSAGE);
			else
				mw.getRenderManager().setVisible(menuRBMI[2].isSelected());
		}// MACROS
		else if (src == menuRBMI[3]) {
			mw.getMacroEditor().setVisible(menuRBMI[3].isSelected());
		}// DISPLAY NETWORK RENDER
		else if (src == menuRBMI[4]) {
			this.setMenuVisible(!this.barre.isVisible());
		} // DISPLAY COLORRAMP
		else if (src == menuRBMI[5]) {
			mw.getBottomPanel().setRampVisible(menuRBMI[5].isSelected());
		} // DISPLAY STATUTBAR
		else if (src == menuRBMI[6]) {
			mw.getBottomPanel().setStatutVisible(menuRBMI[6].isSelected());
		} else if (src == menuI[3][0]) { // HELP
			try {
				Desktop.getDesktop().browse(
						new URI("file://localhost"
								+ System.getProperty("user.dir")
								+ "/help/index.htm"));
			} catch (IOException e) {
			} catch (URISyntaxException e) {
			}
			System.out.println(System.getProperty("user.dir"));
			JOptionPane
					.showMessageDialog(
							this.mw,
							"L'aide html a dût s'ouvrir dans votre navigateur internet.\nSi ce n'est pas le cas ouvrez manuelement le fichier help/index.html.");
		} else if (src == menuI[3][1]) { //  ABOUT
			new About();
		}
	}

	/**
	 * <p>
	 * Met à jour le statut des checkbox en fonction de l'apparence ou non de la
	 * visibilité des composants graphiques.
	 * </p>
	 */
	public void updateCheckBoxes() {
		menuRBMI[0].setSelected(mw.getParams().getParamNavig().isVisible());
		menuRBMI[1].setSelected(mw.getParams().getParamFractal().isVisible());
		menuRBMI[2].setSelected(mw.getRenderManager().isVisible());
		menuRBMI[3].setSelected(mw.getMacroEditor().isVisible());
		menuRBMI[4].setSelected(this.barre.isVisible());
		menuRBMI[5].setSelected(mw.getBottomPanel().crc.isVisible());
		menuRBMI[6].setSelected(mw.getBottomPanel().statut.isVisible());

		for (int i = 0; i < this.mw.getToolBars().jtb.length; i++)
			menuCheckTB[i]
					.setSelected(this.mw.getToolBars().jtb[i].isVisible());
	}

	// menu principal
	final private MainWindow mw;
	final private JMenuBar barre; // Menu
	private JMenu barreJtb; // Menu
	final private String[] menuTtxt = {
			Menu.getString("AddMenu.17"), Menu.getString("AddMenu.18"), Menu.getString("AddMenu.19"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Menu.getString("AddMenu.20") }; //$NON-NLS-1$
	final private JMenu[] menuT = new JMenu[menuTtxt.length];
	final private String[][] menuItxt = {
			{
					Menu.getString("AddMenu.21"), Menu.getString("AddMenu.22"), Menu.getString("AddMenu.23"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					Menu.getString("AddMenu.24"), Menu.getString("AddMenu.25"), //$NON-NLS-1$ //$NON-NLS-2$
					Menu.getString("AddMenu.26"), Menu.getString("AddMenu.27"), //$NON-NLS-1$ //$NON-NLS-2$
					Menu.getString("AddMenu.28") }, //$NON-NLS-1$
			{
					Menu.getString("AddMenu.29"), Menu.getString("AddMenu.30"), Menu.getString("AddMenu.31"), Menu.getString("AddMenu.32") }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			{
					Menu.getString("AddMenu.33"), Menu.getString("AddMenu.34"), Menu.getString("AddMenu.35"), Menu.getString("AddMenu.70"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					Menu.getString("AddMenu.36"), Menu.getString("AddMenu.37"), //$NON-NLS-1$ //$NON-NLS-2$
					Menu.getString("AddMenu.38"), Menu.getString("AddMenu.39") }, //$NON-NLS-1$ //$NON-NLS-2$
			{ Menu.getString("AddMenu.40"), Menu.getString("AddMenu.41") } }; //$NON-NLS-1$ //$NON-NLS-2$
	final private KeyStroke[][] raccMenu = {
			{
					KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK),
					null,
					null,
					KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK),
					KeyStroke
							.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK) },
			{
					KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK) },
			{
					KeyStroke
							.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK),
					KeyStroke
							.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK),
					null,
					null,
					KeyStroke
							.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK),
					KeyStroke
							.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK),
					KeyStroke
							.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_MASK) },
			{ KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK),
					KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK) } };
	final private String[][] imgMenu = {
			{ "windowadd.png", "vue_export.png", "vue.png", "openLine.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"world_edit.png", "imageexport.png", "windowdelete.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"quit.png" }, //$NON-NLS-1$
			{ "copier.png", "redim.png", "fullscreen.png", "pref.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			{
					"navig.png", "equation.png", "network.png", "macro.png", "menu.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"color2.png", "statut.png", "tb.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{ "help.png", "apropos.png" } }; //$NON-NLS-1$ //$NON-NLS-2$
	final private boolean[][] menuSeparator = {
			{ true, false, true, false, true, true, false, false },
			{ true, false, true, false },
			{ false, true, false, true, false, false, true, false },
			{ false, false } };

	// var spécifiques au CheckMenuItem
	final private JCheckBoxMenuItem[] menuRBMI = new JCheckBoxMenuItem[7];
	final private JMenuItem[][] menuI = new JMenuItem[menuItxt.length][];
	final private String txtJtb[] = {
			Menu.getString("AddMenu.63"), Menu.getString("AddMenu.64"), //$NON-NLS-1$ //$NON-NLS-2$
			Menu.getString("AddMenu.65"), Menu.getString("AddMenu.66"), Menu.getString("AddMenu.67"), Menu.getString("AddMenu.71"), Menu.getString("AddMenu.68") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	final public JCheckBoxMenuItem displayAllTb = new JCheckBoxMenuItem(Menu
			.getString("AddMenu.69")); //$NON-NLS-1$
	final public JCheckBoxMenuItem[] menuCheckTB = new JCheckBoxMenuItem[7];
}
