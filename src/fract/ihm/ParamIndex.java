package fract.ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import fract.BiomorphLauncher;
import fract.ihm.MainWindow.Calcul;
import fract.lang.Params;
import fract.opt.EnumOption.StringEnum;

/**
 * <p>
 * Panneau d'administration graphique avec deux classes internes :
 * </p>
 * <ul>
 * <li>Panneau d'affichage du plan [getParamNavig()]</li>
 * <li>Editeur d'équation [getParamFractal()]</li>
 * </ul>
 * <span style="font-family: 'Courier New', Courier, monospace;"><br />
 * --------> Repère Fenêtre :<br />
 * ' | ° ° ° ° ° ° ° ° ° ° ° ° ° ° ° ° °|<br />
 * ' |---------------------------|------|<br />
 * ' | ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ |######|<br />
 * ' | ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ |######|<br />
 * ' | ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ |##ICI#|<br />
 * ' | ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ |######|<br />
 * ' | ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ ¨ |######|<br />
 * ' |---------------------------|------|<br />
 * ' | ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~|<br />
 * </span>
 */
final public class ParamIndex extends JPanel {
	private static final long serialVersionUID = 1L;
	public PanelCarte carte;
	private MainWindow mw;
	private ParamNavig navig;
	private ParamFractal paramFract;

	/**
	 * <p>
	 * Desine le panneau de paramétrage principal appel des TitledBorder pour
	 * entourer les deux panels d'administration : la navigation dans le plan et
	 * le paramétrage du fractal.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public ParamIndex(final MainWindow mw) {
		// création des composant et récup de MW
		this.mw = mw;
		this.navig = new ParamNavig();
		this.paramFract = new ParamFractal();
		this.addMouseListener(new MouseAdapter() {
			/**
			 * Si tout est invisible, un double clic sur la partie restante
			 * réouvre le panneau complet. S'il est visible il devient au
			 * contraire invisible.
			 */
			public void mouseClicked(MouseEvent ev) {
				if (!navig.isVisible() && !paramFract.isVisible()
						&& ev.getClickCount() == 2)
					setVisibleWindows(true, true);
				else if (ev.getClickCount() == 2)
					setVisibleWindows(false, false);
				mw.getMenu().updateCheckBoxes();
			}
		});

		// Création des bordures, ajout de titres
		TitledBorder titleNavig, titleFract;
		titleNavig = BorderFactory.createTitledBorder(Params
				.getString("ParamIndex.0")); //$NON-NLS-1$
		titleFract = BorderFactory.createTitledBorder(Params
				.getString("ParamIndex.1")); //$NON-NLS-1$
		this.navig.setBorder(titleNavig);
		this.paramFract.setBorder(titleFract);

		// gestion du layout et ajout
		Box lay = Box.createVerticalBox();
		this.add(lay);
		lay.add(this.navig);
		lay.add(this.paramFract);
		lay.add(Box.createGlue()); // rempli ce qu'il reste
	}

	/**
	 * <p>
	 * Switch la visibilité des panneaux de paramètrage.
	 * </p>
	 * <ul>
	 * <li>Premier argument pour le panneau de coordonnées.</li>
	 * <li>Deuxième argument pour le sélecteur d'équation.</li>
	 * </ul>
	 * 
	 * @param v1
	 *            Visibilité panneau plan
	 * @param v2
	 *            Visibilité panneau équation
	 */
	public void setVisibleWindows(final boolean v1, final boolean v2) {
		this.navig.setVisible(v1);
		this.paramFract.setVisible(v2);
	}

	/**
	 * <p>
	 * Retourne le panneau de navigation <i>(position/ échelle, preview,
	 * angle...)</i>
	 * </p>
	 */
	public ParamNavig getParamNavig() {
		return this.navig;
	}

	/**
	 * <p>
	 * Retourne le panneau de configuration du fractal <i>(changer fractal,
	 * ouvrir éditeur...)</i>.
	 * </p>
	 */
	public ParamFractal getParamFractal() {
		return this.paramFract;
	}

	/**
	 * <p>
	 * Ajoute ou retire un fractal de la liste déroulante.
	 * </p>
	 * 
	 * @param ajout
	 *            Ajouter = true
	 * @param nom
	 *            Nom du fractal
	 */
	public void addFractalToList(final boolean ajout, final String nom) {
		this.paramFract.addFractalToList(ajout, nom);
	}

	/**
	 * <p>
	 * Met à jour le panneau graphique d'arguments selon le fractal chargé.
	 * </p>
	 */
	public void updatePanelArguments() {
		this.paramFract.updatePanelArguments();
	}

	/**
	 * <p>
	 * Classe (interne) regroupant tous les composants de navigation (slider
	 * échelle, champs de positions, carte globale, utilitaire de rotation) pour
	 * le panneau de navigation dans le plan.
	 * </p>
	 */
	final class ParamNavig extends JPanel implements KeyListener,
			ActionListener {
		private static final long serialVersionUID = 1L;

		/**
		 * <p>
		 * Construit tous les composants du panneau de navigation, les
		 * positionnent dans un GridBagLayout pour plus de souplesse.
		 * </p>
		 */
		public ParamNavig() {
			/* Préparation des composants */
			// slider d'échelle
			this.scale = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
			this.scale.setMinorTickSpacing(5);
			this.scale.setMajorTickSpacing(50);
			this.scale.setPaintTicks(true);
			this.scale.setPaintLabels(true);

			// carte
			carte = new PanelCarte(mw);

			// labels et aire de texte :
			final JPopupMenu jpm = new JPopupMenu();
			this.jmi_copyI = new JMenuItem(Params.getString("ParamIndex.2")); //$NON-NLS-1$
			this.jmi_copyI.setIcon(new ImageIcon(BiomorphLauncher.class
					.getResource("/icon/interfaces/first.png"))); //$NON-NLS-1$
			this.jmi_copyF = new JMenuItem(Params.getString("ParamIndex.4")); //$NON-NLS-1$
			this.jmi_copyF.setIcon(new ImageIcon(BiomorphLauncher.class
					.getResource("/icon/interfaces/last.png"))); //$NON-NLS-1$
			this.jmi_copyI.addActionListener(this);
			this.jmi_copyF.addActionListener(this);
			jpm.add(this.jmi_copyI);
			jpm.add(this.jmi_copyF);

			this.ech = new JTextField();
			this.ech.setToolTipText("<html>" //$NON-NLS-1$
					+ Params.getString("ParamIndex.7") //$NON-NLS-1$
					+ Params.getString("ParamIndex.8") //$NON-NLS-1$
					+ Params.getString("ParamIndex.9") //$NON-NLS-1$
					+ Params.getString("ParamIndex.10")); //$NON-NLS-1$
			this.ech.setComponentPopupMenu(jpm);

			this.posX = new JTextField();
			this.posX.setToolTipText("<html>" //$NON-NLS-1$
					+ Params.getString("ParamIndex.12") //$NON-NLS-1$
					+ Params.getString("ParamIndex.13")); //$NON-NLS-1$
			this.posX.setComponentPopupMenu(jpm);
			this.posY = new JTextField();
			this.posY.setToolTipText("<html>" //$NON-NLS-1$
					+ Params.getString("ParamIndex.15") //$NON-NLS-1$
					+ Params.getString("ParamIndex.16")); //$NON-NLS-1$
			this.posY.setComponentPopupMenu(jpm);

			this.ech.addKeyListener(this);
			this.posX.addKeyListener(this);
			this.posY.addKeyListener(this);
			this.Lech = new JLabel(Params.getString("ParamIndex.17")); //$NON-NLS-1$
			this.Lech.setToolTipText(this.ech.getToolTipText());
			this.LposX = new JLabel(Params.getString("ParamIndex.18")); //$NON-NLS-1$
			this.LposX.setToolTipText(this.posX.getToolTipText());
			this.LposY = new JLabel(Params.getString("ParamIndex.19")); //$NON-NLS-1$
			this.LposY.setToolTipText(this.posY.getToolTipText());

			// Sélecteur d'angle
			this.selAngle = new SelectAngle();
			this.selAngle.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent ev) {
					mw.setRotation(selAngle.getAngleEnRadian(), false, false);
				}
			});
			this.selAngle.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent ev) {
					mw.getPan().rotationExterieur = true;
				}

				public void mouseReleased(MouseEvent ev) {
					mw.getPan().rotationExterieur = false;
					mw.setRotation(selAngle.getAngleEnRadian(), true, false);
				}
			});
			this.activRotation = new JCheckBox(Params
					.getString("ParamIndex.20"), false); //$NON-NLS-1$
			this.activRotation.addActionListener(this);
			this.activRotation.setToolTipText("<html>" //$NON-NLS-1$
					+ Params.getString("ParamIndex.22") //$NON-NLS-1$
					+ Params.getString("ParamIndex.23")); //$NON-NLS-1$
			this.selAngle.setEnable(this.activRotation.isSelected());

			// Dessin du GridBagLayout avec contraintes :
			this.gbl = new GridBagLayout();
			this.setLayout(gbl);
			this.setPreferredSize(new Dimension(200, 350));
			this.gbc = new GridBagConstraints();
			this.gbc.fill = GridBagConstraints.BOTH;
			Component c[] = { carte, scale, Lech, LposX, LposY, ech, posX,
					posY, selAngle, activRotation };

			for (int i = 0; i < this.x.length; i++) {
				this.gbc.gridx = this.x[i];
				this.gbc.gridy = this.y[i];
				this.gbc.gridwidth = this.larg[i];
				this.gbc.gridheight = this.haut[i];
				this.gbc.weightx = this.px[i];
				this.gbc.weighty = this.py[i];
				this.add(c[i], this.gbc);
			}
		}

		/**
		 * <p>
		 * Update des coordonnées depuis l'objet fractal d'après ses coordonnées
		 * [échelle, positionX-Y]. La carte sera mise à jour.
		 * </p>
		 */
		public void updateCoord() {
			// position et échelle
			this.ech.setText("" + arrondir(mw.getFract().getScale())); //$NON-NLS-1$
			this.posX.setText("" + arrondir(mw.getFract().getPosX())); //$NON-NLS-1$
			this.posY.setText("" + arrondir(mw.getFract().getPosY())); //$NON-NLS-1$

			this.scale.setValue(mw.getFract().getpourcentZoom());
			carte.repaint();
		}

		/**
		 * <p>
		 * Coche l'option rotation si angle non nul + demande l'adaptation à
		 * l'objet SelectAngle (angle et repaint).
		 * </p>
		 * 
		 * @param angle
		 *            Angle en radians
		 */
		public void updateRotationTools(final double angle) {
			this.activRotation.setSelected(angle != 0);
			this.selAngle.setEnable(angle != 0);
			this.selAngle.setAngle(angle);
			this.selAngle.repaint();
		}

		/**
		 * <p>
		 * Fait un update de tous les composants affichés : en cas de changement
		 * de fractal : coordonnées, rotation.
		 * </p>
		 */
		public void updateAllComponents() {
			// recalcul des coordonnées échelle et position
			this.updateCoord();
		}

		/**
		 * <p>
		 * Update des coordonnées d'après les arguments en entrée.
		 * </p>
		 * 
		 * @param posX
		 *            Abcisse point supérieur gauche
		 * @param posY
		 *            Ordonnée point supérieur gauche
		 */
		public void updateCoord(double posX, double posY) {
			// position et échelle
			this.ech.setText("" + arrondir(mw.getFract().getScale())); //$NON-NLS-1$
			this.posX.setText("" + arrondir(posX)); //$NON-NLS-1$
			this.posY.setText("" + arrondir(posY)); //$NON-NLS-1$
		}

		/**
		 * <p>
		 * Arrondit nombre en entrée précision 100000000.
		 * </p>
		 * 
		 * @param nb
		 *            Nombre à arrondir
		 * @return nombre arrondit
		 */
		private double arrondir(final double nb) {
			return Math.ceil(nb * 100000000) / 100000000;
		}

		/**
		 * <p>
		 * Détecte <b>touche entrée</b> pour les coordonnées.
		 * </p>
		 */
		public void keyPressed(KeyEvent ev) {
			if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
				mw.getFract().changeCoordAndScale(
						Double.parseDouble(this.posX.getText()),
						Double.parseDouble(this.posY.getText()),
						Double.parseDouble(this.ech.getText()));
				mw.launchCalculAndDisplay(true);
			}
		}

		/**
		 * Controle les actions de la checkbox de rotation, écoute menu
		 * contextuels copie coordonnées points initiaux/ finaux.
		 */
		public void actionPerformed(ActionEvent ev) {
			Object src = ev.getSource();
			if (src == this.activRotation) {
				this.selAngle.setEnable(this.activRotation.isSelected());
				mw.setRotation(selAngle.getAngleEnRadian(), true, false);
			} else if (src == this.jmi_copyI || src == this.jmi_copyF) {
				final double d[] = { Double.parseDouble(this.posX.getText()),
						Double.parseDouble(this.posY.getText()),
						Double.parseDouble(this.ech.getText()) };
				StringEnum[] se = { StringEnum.MposXinit, StringEnum.MposYinit,
						StringEnum.Mscaleinit };
				if (src == this.jmi_copyF) {
					se[0] = StringEnum.MposXfin;
					se[1] = StringEnum.MposYfin;
					se[2] = StringEnum.Mscalefin;
				}
				for (int i = 0; i < d.length; i++) {
					BiomorphLauncher.tOpt
							.modifierOption(se[i].toString(), d[i]);
					BiomorphLauncher.gOpt.changeOption(se[i].toString(), "\"" //$NON-NLS-1$
							+ d[i] + "\""); //$NON-NLS-1$
				}
				mw.getMacroEditor().updateLabelsAndValues();
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

		// coordonées GridBagLayout
		final private int x[] = { 0, 0, 0, 0, 0, 1, 1, 1, 4, 0 };
		final private int y[] = { 0, 3, 4, 5, 6, 4, 5, 6, 4, 7 };
		final private int larg[] = { 8, 8, 1, 1, 1, 3, 3, 3, 4, 8 };
		final private int haut[] = { 3, 1, 1, 1, 1, 1, 1, 1, 3, 1 };
		final private int px[] = { 10, 10, 0, 0, 0, 3, 3, 3, 2, 10 };
		final private int py[] = { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		// composants
		final private GridBagLayout gbl;
		final private GridBagConstraints gbc;
		final private SelectAngle selAngle;
		final private JSlider scale;
		final private JCheckBox activRotation;
		final private JTextField ech, posX, posY;
		final private JLabel Lech, LposX, LposY;
		final JMenuItem jmi_copyI, jmi_copyF;
	}

	/**
	 * <p>
	 * Classe (interne) regroupant tous les composants gestion des fractales
	 * (sélection, ouverture éditeur, arguments pour chaque fractales).
	 * </p>
	 */
	final class ParamFractal extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		/**
		 * <p>
		 * Construit les composants, les positionnent dans un GridBagLayout pour
		 * plus de souplesse
		 * </p>
		 */
		public ParamFractal() {
			/*
			 * Préparation des composants
			 */
			// labels et boutons
			this.Lchoix = new JLabel(Params.getString("ParamIndex.32")); //$NON-NLS-1$
			this.Badd = new JButton(Params.getString("ParamIndex.33")); //$NON-NLS-1$
			this.Bsuppr = new JButton(Params.getString("ParamIndex.34")); //$NON-NLS-1$
			this.Bok = new JButton(Params.getString("ParamIndex.35")); //$NON-NLS-1$
			this.Badd.setMargin(new Insets(1, 1, 1, 1));
			this.Bsuppr.setMargin(new Insets(1, 1, 1, 1));
			this.Bok.setMargin(new Insets(1, 1, 1, 1));
			this.Badd.addActionListener(this);
			this.Bsuppr.addActionListener(this);
			this.Bok.addActionListener(this);

			this.panArgs = new JPanel();
			this.panArgs.setLayout(new FlowLayout());

			this.setPreferredSize(new Dimension(200, 230));
			// liste
			this.listEq = new JComboBox(lstEq);
			this.listEq.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent ev) {
					if (ev.getKeyChar() == '\n')
						validerFractal();
				}
			});
			this.getFractalFiles();

			// Dessin du GridBagLayout avec contraintes :
			gbl = new GridBagLayout();
			this.setLayout(gbl);
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			Component c[] = { Lchoix, this.listEq, Badd, Bsuppr, Bok,
					new JLabel(""), panArgs }; //$NON-NLS-1$
			int x[] = { 0, 0, 0, 3, 5, 7, 0 };
			int y[] = { 0, 1, 2, 2, 2, 2, 3 };
			int larg[] = { 9, 9, 3, 2, 2, 2, 9 };
			int haut[] = { 1, 1, 1, 1, 1, 1, 3 };
			int px[] = { 10, 10, 3, 3, 3, 3, 10 };
			int py[] = { 0, 0, 0, 0, 0, 0, 1 };
			for (int i = 0; i < c.length; i++) {
				gbc.gridx = x[i];
				gbc.gridy = y[i];
				gbc.gridwidth = larg[i];
				gbc.gridheight = haut[i];
				gbc.weightx = px[i];
				gbc.weighty = py[i];
				this.add(c[i], gbc);
			}
		}

		/**
		 * <p>
		 * Remplit la liste de fractales disponibles depuis un listing des
		 * fichiers. Détecte l'absence d gestionnaire de fichiers pour lister
		 * les fichiers dans une archive jar.
		 * </p>
		 */
		private void getFractalFiles() {
			String[] listefichiers = null;
			String tmp;
			final boolean dos = (System.getProperty(Params
					.getString("ParamIndex.37")) //$NON-NLS-1$
					.startsWith(Params.getString("ParamIndex.38"))); //$NON-NLS-1$
			int nb = 0;

			URL u = System.class.getResource("/fract/algo/"); //$NON-NLS-1$
			if (u == null) {
				// = programme lancé depuis une archive JAR
				String s = System.class.getResource("/.classpath").toString(); //$NON-NLS-1$
				s = s.substring(4 + 5, s.length());
				s = s.substring(0, s.length() - 12);

				JarFile jarFile;
				try {
					// on liste les fichiers dans l'archive jar
					jarFile = new JarFile(s);
					jarFile.entries();
					Enumeration<JarEntry> en = jarFile.entries();
					JarEntry entry;
					ArrayList<String> al = new ArrayList<String>();
					while (en.hasMoreElements()) {
						entry = (JarEntry) en.nextElement();
						String name = entry.getName();
						if (name.startsWith("fract/algo/")) { //$NON-NLS-1$
							name = name.substring(11, name.length());
							al.add(name);
						}
					}
					// on remplit un tableau en itérant sur l'AL
					listefichiers = new String[al.size()];
					this.nomFract = new String[al.size()];
					Iterator<String> it = al.iterator();
					int i = 0;
					while (it.hasNext()) {
						listefichiers[i] = it.next();
						++i;
					}
				} catch (IOException e) {
					BiomorphLauncher.writeError(Params
							.getString("ParamIndex.42") + s //$NON-NLS-1$
							+ Params.getString("ParamIndex.43")); //$NON-NLS-1$
					System.exit(59);
				}
			} else {
				// système de fichier normal (pg pas dans un jar)
				algoPath = u.toString();
				algoPath = algoPath.substring((dos) ? 6 : 5, algoPath.length());
				if (dos) { // si windows, traitement spécial sur le lien :
					algoPath = algoPath.replaceAll("%20", " "); //$NON-NLS-1$ //$NON-NLS-2$
				}
				File repertoire = new File(algoPath);

				// System.out.println(algoPath);
				listefichiers = repertoire.list();
				this.nomFract = new String[listefichiers.length];
			}
			this.listEq.addItem("--------------"); //$NON-NLS-1$
			for (String s : listefichiers) {
				if (!this.isDefaultFractal(s) && s.endsWith(".class")) { //$NON-NLS-1$
					tmp = s.substring(0, s.length() - 6);
					this.nomFract[nb++] = tmp;
					this.listEq.addItem(tmp);
				}
			}
			this.listEq.setSelectedItem(BiomorphLauncher.tOpt
					.getStringValue(StringEnum.dftFractal));
		}

		/**
		 * <p>
		 * Met à jour le panneau des arguments en cas de changement de fractal.
		 * </p>
		 */
		public void updatePanelArguments() {
			// composants globaux
			this.validArguments = new JButton(Params.getString("ParamIndex.48")); //$NON-NLS-1$
			this.validArguments.addActionListener(this);
			this.panArgs.removeAll();

			// layout pour les composants individuels
			String f = this.listEq.getSelectedItem().toString();
			if (f != null) {
				if (f.equals("Mandelbrot") || f.equals("Julia")) { //$NON-NLS-1$ //$NON-NLS-2$
					this.puissance = new JComboBox(new String[] { "1", "2", //$NON-NLS-1$ //$NON-NLS-2$
							"3", "4", "5", "6", "7", "8", "9", "10", "11", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
							"12", "13", "14", "15" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					this.puissance.setSelectedIndex((int) mw.getFract()
							.getArgumentsArray()[0] - 1);
					this.Lpuissance = new JLabel(Params
							.getString("ParamIndex.3")); //$NON-NLS-1$
					this.Lpuissance.setToolTipText(this.puissance
							.getToolTipText());
					this.panArgs.add(this.Lpuissance);
					this.panArgs.add(this.puissance);
				}
				if (f.equals(Params.getString("ParamIndex.5"))) { //$NON-NLS-1$
					this.Lpuissance.setText(Params.getString("ParamIndex.68")); //$NON-NLS-1$
					this.nbRe = new JTextField(mw.getFract()
							.getArgumentsArray()[1]
							+ ""); //$NON-NLS-1$
					this.nbRe.setComponentPopupMenu(new CreateMenuCopyKey(1,
							this.nbRe).getPopupMenu());
					this.nbRe.addActionListener(this);
					this.nbImg = new JTextField(mw.getFract()
							.getArgumentsArray()[2]
							+ ""); //$NON-NLS-1$
					this.nbImg.addActionListener(this);
					this.nbImg.setComponentPopupMenu(new CreateMenuCopyKey(2,
							this.nbImg).getPopupMenu());
					this.Lplus = new JLabel("+"); //$NON-NLS-1$
					this.Li = new JLabel("i"); //$NON-NLS-1$
					this.panArgs.add(this.nbRe);
					this.panArgs.add(this.Lplus);
					this.panArgs.add(this.nbImg);
					this.panArgs.add(this.Li);
				}
				if (f.equals("BiomorpheSimple") //$NON-NLS-1$
						|| f.equals("BiomorpheAdvanced")) { //$NON-NLS-1$
					this.nbRe = new JTextField(mw.getFract()
							.getArgumentsArray()[0]
							+ ""); //$NON-NLS-1$
					this.nbRe.addActionListener(this);
					this.nbRe.setPreferredSize(new Dimension(70, 20));
					this.nbRe.setComponentPopupMenu(new CreateMenuCopyKey(0,
							this.nbRe).getPopupMenu());
					this.puissance = new JComboBox(
							new String[] {
									Params.getString("ParamIndex.76"), //$NON-NLS-1$
									Params.getString("ParamIndex.77"), "^4", "^5", "^6", "^7", "^8", "^9", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
									Params.getString("ParamIndex.84"), Params.getString("ParamIndex.85"), Params.getString("ParamIndex.86"), Params.getString("ParamIndex.87") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					this.puissance.setSelectedIndex((int) mw.getFract()
							.getArgumentsArray()[1]);
					this.panArgs.add(new JLabel(Params
							.getString("ParamIndex.88"))); //$NON-NLS-1$
					this.panArgs.add(this.nbRe);
					this.panArgs.add(new JLabel(Params
							.getString("ParamIndex.89"))); //$NON-NLS-1$
					this.panArgs.add(this.puissance);
				}
				if (f.equals("BiomorpheAdvanced")) { //$NON-NLS-1$
					this.puissance2 = new JComboBox(
							new String[] {
									Params.getString("ParamIndex.91"), //$NON-NLS-1$
									Params.getString("ParamIndex.92"), "^4", "^5", "^6", "^7", "^8", "^9", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
									Params.getString("ParamIndex.6"), Params.getString("ParamIndex.100"), Params.getString("ParamIndex.101"), Params.getString("ParamIndex.102") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					this.choixAdd = new JComboBox(new String[] { " + ", " - " }); //$NON-NLS-1$ //$NON-NLS-2$
					JPanel panAdd = new JPanel();
					panAdd.setLayout(new FlowLayout());
					panAdd.add(this.choixAdd);
					panAdd.add(this.puissance2);
					this.panArgs.add(panAdd);
				}
			}

			panArgs.add(this.validArguments);
			this.panArgs.validate();
		}

		/**
		 * <p>
		 * Ajoute ou retire un fractal à la liste.
		 * </p>
		 * 
		 * @param ajout
		 * @param nom
		 */
		public void addFractalToList(final boolean ajout, final String nom) {
			if (ajout) {
				String[] s2 = new String[this.nomFract.length + 1];
				for (int i = 0; i < this.nomFract.length; i++) {
					if (this.nomFract[i] == null) {
						s2[i] = nom;
						break;
					}
					s2[i] = this.nomFract[i];
				}
				this.nomFract = s2;
				this.listEq.addItem(nom);
			}
		}

		/**
		 * <p>
		 * Ecoute la validation du fractal sélectionné dans la JList.
		 * </p>
		 */
		private void validerFractal() {
			if (this.listEq.getSelectedIndex() < this.lstEq.length)
				mw.changeFractal(this.lstEq[listEq.getSelectedIndex()]);
			else if (this.listEq.getSelectedIndex() > this.lstEq.length) {
				mw.changeFractal(this.nomFract[this.listEq.getSelectedIndex()
						- this.lstEq.length - 1]);
			}
		}

		/**
		 * @param name
		 * @return si l'argument est dans la liste des classes fractales par
		 *         défaut incluent dans le logiciel.
		 */
		private boolean isDefaultFractal(final String name) {
			for (String s : this.lstEq)
				if ((name).equals(s + ".class") //$NON-NLS-1$
						|| name.equals("BaseFractale.class") //$NON-NLS-1$
						|| name.equals("ModelFractale.class")) //$NON-NLS-1$
					return true;
			return false;
		}

		public String[] getFractalsList() {
			String[] s = new String[this.listEq.getItemCount()];
			for (int i = 0; i < s.length; i++)
				s[i] = this.listEq.getItemAt(i).toString();
			return s;
		}

		/**
		 * <p>
		 * Ecoute Bouton éditer/ supprimer...
		 * </p>
		 */
		public void actionPerformed(final ActionEvent ev) {
			Object src = ev.getSource();
			if (src == Bok) { // changement de fractal
				this.validerFractal();
			} // ouverture éditeur d'équations
			else if (src == this.Badd) {
				if (BiomorphLauncher.windows)
					JOptionPane.showMessageDialog(mw, Params
							.getString("ParamIndex.14"), //$NON-NLS-1$
							Params.getString("ParamIndex.21"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				else
					new FractalFileGenerator(mw);

			} //  SUPPRESSION
			else if (src == this.Bsuppr) {
				// on prévient si le fichier est précieux
				if (this.listEq.getSelectedIndex() == this.lstEq.length) {
					JOptionPane
							.showMessageDialog(
									mw,
									Params.getString("ParamIndex.11"), //$NON-NLS-1$
									Params.getString("ParamIndex.109"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
					return;
				} else if (this.listEq.getSelectedIndex() < this.lstEq.length) {
					JOptionPane.showMessageDialog(mw, Params
							.getString("ParamIndex.110"), //$NON-NLS-1$
							Params.getString("ParamIndex.111"), //$NON-NLS-1$
							JOptionPane.WARNING_MESSAGE);
				}
				// cas de confirmation
				if (JOptionPane.showConfirmDialog(mw, Params
						.getString("ParamIndex.112") //$NON-NLS-1$
						+ this.listEq.getSelectedItem()
						+ Params.getString("ParamIndex.113")) == JOptionPane.OK_OPTION) { //$NON-NLS-1$

					File f = new File(this.algoPath
							+ this.listEq.getSelectedItem() + ".class"); //$NON-NLS-1$
					if (f.exists()) {
						if (f.delete()) { // suppression réussie
							JOptionPane
									.showMessageDialog(
											mw,
											this.listEq.getSelectedItem()
													+ Params
															.getString("ParamIndex.115"), //$NON-NLS-1$
											this.listEq.getSelectedItem()
													+ Params
															.getString("ParamIndex.116"), //$NON-NLS-1$
											JOptionPane.INFORMATION_MESSAGE);
							this.listEq.removeItemAt(this.listEq
									.getSelectedIndex());
						} else
							// le fichier n'a pas pu être supprimé
							JOptionPane
									.showMessageDialog(
											mw,
											Params.getString("ParamIndex.117") //$NON-NLS-1$
													+ this.listEq
															.getSelectedItem()
													+ Params
															.getString("ParamIndex.118"), //$NON-NLS-1$
											this.listEq.getSelectedItem()
													+ Params
															.getString("ParamIndex.119"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
					} else { // le fichier n'existe pas
						JOptionPane.showMessageDialog(mw, Params
								.getString("ParamIndex.120") //$NON-NLS-1$
								+ this.listEq.getSelectedItem()
								+ Params.getString("ParamIndex.121"), //$NON-NLS-1$
								this.listEq.getSelectedItem()
										+ Params.getString("ParamIndex.122"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						this.listEq
								.removeItemAt(this.listEq.getSelectedIndex());
					}

				}
			} else {
				// on arrête l'affichage pour changer les arguments
				String f = mw.getFractalName();
				if (f.equals("Mandelbrot") || f.equals("Julia")) { //$NON-NLS-1$ //$NON-NLS-2$
					// modification puissance
					mw.getFract().modifyFractArgument(0,
							this.puissance.getSelectedIndex() + 1);
					mw.getFractCarte().modifyFractArgument(0,
							this.puissance.getSelectedIndex() + 1);
				}
				if (f.equals("Julia")) { //$NON-NLS-1$
					// modification partie RE et IMG
					try {
						mw.getFract().modifyFractArgument(1,
								Double.parseDouble(this.nbRe.getText()));
						mw.getFractCarte().modifyFractArgument(1,
								Double.parseDouble(this.nbRe.getText()));
					} catch (NumberFormatException e) {
						this.nbRe.setForeground(Color.red);
					}
					try {
						mw.getFract().modifyFractArgument(2,
								Double.parseDouble(this.nbImg.getText()));
						mw.getFractCarte().modifyFractArgument(2,
								Double.parseDouble(this.nbImg.getText()));
					} catch (NumberFormatException e) {
						this.nbImg.setForeground(Color.red);
					}
				}
				if (f.equals("BiomorpheSimple") //$NON-NLS-1$
						|| f.equals("BiomorpheAdvanced")) { //$NON-NLS-1$
					// modification partie RE et IMG
					try {
						mw.getFract().modifyFractArgument(0,
								Double.parseDouble(this.nbRe.getText()));
						mw.getFractCarte().modifyFractArgument(0,
								Double.parseDouble(this.nbRe.getText()));
					} catch (NumberFormatException e) {
						this.nbRe.setForeground(Color.red);
					}
					mw.getFract().modifyFractArgument(1,
							this.puissance.getSelectedIndex());
					mw.getFractCarte().modifyFractArgument(1,
							this.puissance.getSelectedIndex());
				}
				if (f.equals("BiomorpheAdvanced")) { //$NON-NLS-1$
					mw.getFract().modifyFractArgument(2,
							this.choixAdd.getSelectedIndex());
					mw.getFract().modifyFractArgument(3,
							this.puissance2.getSelectedIndex());
					mw.getFractCarte().modifyFractArgument(2,
							this.choixAdd.getSelectedIndex());
					mw.getFractCarte().modifyFractArgument(3,
							this.puissance2.getSelectedIndex());
				}
				mw.getParams().carte.recalculate();
				mw.manageCalcul(Calcul.arret);
				mw.launchCalculAndDisplay(true);
			}
		}

		/**
		 * Instantie les outils nécessaires pour créer un menu d'animation de
		 * variables de macro complet.
		 */
		class CreateMenuCopyKey implements ActionListener {
			public CreateMenuCopyKey(int position, JTextField tf) {
				this.position = position;
				this.tf = tf;
				jpm = new JPopupMenu();
				this.jmi_def1 = new JMenuItem(Params
						.getString("ParamIndex.129")); //$NON-NLS-1$
				this.jmi_def1.setIcon(new ImageIcon(BiomorphLauncher.class
						.getResource("/icon/interfaces/pill_add.png"))); //$NON-NLS-1$
				this.jmi_def2 = new JMenuItem(Params
						.getString("ParamIndex.131")); //$NON-NLS-1$
				this.jmi_def2.setIcon(new ImageIcon(BiomorphLauncher.class
						.getResource("/icon/interfaces/pill_add.png"))); //$NON-NLS-1$
				this.jmi_copyI1 = new JMenuItem(Params
						.getString("ParamIndex.133")); //$NON-NLS-1$
				this.jmi_copyI1.setIcon(new ImageIcon(BiomorphLauncher.class
						.getResource("/icon/interfaces/first.png"))); //$NON-NLS-1$
				this.jmi_copyF1 = new JMenuItem(Params
						.getString("ParamIndex.135")); //$NON-NLS-1$
				this.jmi_copyF1.setIcon(new ImageIcon(BiomorphLauncher.class
						.getResource("/icon/interfaces/last.png"))); //$NON-NLS-1$
				this.jmi_copyI2 = new JMenuItem(Params
						.getString("ParamIndex.137")); //$NON-NLS-1$
				this.jmi_copyI2.setIcon(new ImageIcon(BiomorphLauncher.class
						.getResource("/icon/interfaces/first.png"))); //$NON-NLS-1$
				this.jmi_copyF2 = new JMenuItem(Params
						.getString("ParamIndex.139")); //$NON-NLS-1$
				this.jmi_copyF2.setIcon(new ImageIcon(BiomorphLauncher.class
						.getResource("/icon/interfaces/last.png"))); //$NON-NLS-1$
				this.jmi_def1.addActionListener(this);
				this.jmi_def2.addActionListener(this);
				this.jmi_copyI1.addActionListener(this);
				this.jmi_copyF1.addActionListener(this);
				this.jmi_copyI2.addActionListener(this);
				this.jmi_copyF2.addActionListener(this);
				jpm.add(this.jmi_def1);
				jpm.add(this.jmi_def2);
				jpm.add(this.jmi_copyI1);
				jpm.add(this.jmi_copyF1);
				jpm.add(this.jmi_copyI2);
				jpm.add(this.jmi_copyF2);
			}

			/**
			 * @return JPopupMenu contenant les options de macro sur les valeur
			 *         en argument constructeur.
			 */
			public JPopupMenu getPopupMenu() {
				return this.jpm;
			}

			public void actionPerformed(ActionEvent ev) {
				Object src = ev.getSource();
				String opt = null;
				double val;
				if (src == this.jmi_def1) {
					opt = "Mvar1offs"; //$NON-NLS-1$
					val = this.position;
					BiomorphLauncher.gOpt.changeOption(opt, (int) val);
					BiomorphLauncher.tOpt.modifierOption(opt, (int) val);
				} else if (src == this.jmi_def2) {
					opt = "Mvar2offs"; //$NON-NLS-1$
					val = this.position;
					BiomorphLauncher.gOpt.changeOption(opt, (int) val);
					BiomorphLauncher.tOpt.modifierOption(opt, (int) val);
				} else {
					val = Double.parseDouble(tf.getText());
					Component c[] = { jmi_copyI1, jmi_copyF1, jmi_copyI2,
							jmi_copyF2 };
					String s[] = { "Mvar1init", "Mvar1fin", "Mvar2init", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							"Mvar2fin" }; //$NON-NLS-1$
					for (int i = 0; i < c.length; i++)
						if (src == c[i])
							opt = s[i];
					BiomorphLauncher.gOpt.changeOption(opt, "\"" + val + "\""); //$NON-NLS-1$ //$NON-NLS-2$
					BiomorphLauncher.tOpt.modifierOption(opt, val);
				}
				mw.getMacroEditor().updateLabelsAndValues();
			}

			final private JPopupMenu jpm;
			final private JMenuItem jmi_def1, jmi_def2, jmi_copyI1, jmi_copyF1,
					jmi_copyI2, jmi_copyF2;
			final private int position;
			final private JTextField tf;
		}

		// composants
		final private GridBagLayout gbl;
		final private GridBagConstraints gbc;
		final private JLabel Lchoix;
		final private String[] lstEq = { "Mandelbrot", "BlackHole", "Julia", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"BiomorpheSimple", "BiomorpheAdvanced" }; // liste fract par //$NON-NLS-1$ //$NON-NLS-2$
		// défaut
		private String[] nomFract;
		final private JComboBox listEq;
		final private JButton Badd, Bsuppr, Bok;

		// composants individuels globaux
		final private JPanel panArgs;
		private JComboBox puissance, puissance2, choixAdd;
		private JLabel Lpuissance;
		private JButton validArguments;
		private JTextField nbRe, nbImg;
		private JLabel Lplus, Li;

		String algoPath; // chemin vers les plug-ins compilés
	}

}
