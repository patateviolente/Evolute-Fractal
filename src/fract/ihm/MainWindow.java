package fract.ihm;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import fract.BiomorphLauncher;
import fract.lang.OtherInterface;
import fract.opt.EnumVue;
import fract.opt.StructSaveVue;
import fract.struct.HistoriqueTab;
import fract.struct.RegPoint;
import fract.algo.*;
import fract.exception.ThreadNotReadyException;
import fract.opt.EnumOption.IntEnum;
import fract.opt.EnumOption.BooleanEnum;
import fract.opt.EnumOption.StringEnum;

/**
 * Classe de l'interface graphique principale, déclare tous les composants
 * graphiques et ceux indispensables au programme : Panel de dessin (Panel),
 * Tableau de Point (RegPoint), et un Algorithme quelconque.
 */
final public class MainWindow extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit la fenêtre [menu, éléments] et instanci les pincipaux
	 * composantstre en fond
	 */
	public MainWindow() {

		// //////////////////////////////////////
		// Définition des propriétés de la fenêtre
		this.setBounds(BiomorphLauncher.tOpt.getIntValue(IntEnum.boundX),
				BiomorphLauncher.tOpt.getIntValue(IntEnum.boundY),
				BiomorphLauncher.tOpt.getIntValue(IntEnum.horizSize),
				BiomorphLauncher.tOpt.getIntValue(IntEnum.vertSize));
		this.setTitle(OtherInterface.getString("MainWindow.0")); //$NON-NLS-1$
		this.setVisible(true);
		if(BiomorphLauncher.nbActivWindow == 0 && BiomorphLauncher.tOpt.getIntValue(IntEnum.infos) >= 1)
			new DidYouKnow();
		BiomorphLauncher.nbActivWindow++;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif"))); //$NON-NLS-1$
		this.setMinimumSize(new Dimension(300, 180));
		this.addWindowListener(new WindowAdapter() {
			/**
			 * On vérifit le nombre de fenêtre ouvertes, s'il n'y en a plus on
			 * arrête le programme.
			 */
			public void windowClosed(WindowEvent ev) {
				BiomorphLauncher.nbActivWindow--;
				if (BiomorphLauncher.nbActivWindow == 0) {
					BiomorphLauncher
							.writeProcedure(OtherInterface.getString("MainWindow.2")); //$NON-NLS-1$
					System.exit(0);
				} else
					BiomorphLauncher.writeAction(OtherInterface.getString("MainWindow.3") //$NON-NLS-1$
							+ BiomorphLauncher.nbActivWindow);
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ev) {
			}
		});

		if (BiomorphLauncher.tOpt.getBooleanValue(BooleanEnum.fullScreen)) { //  PLEIN ECRAN
			this.pack();
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
		}

		// curseur personnalisé de rotation
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("icon/cursor/rotation.png"); //$NON-NLS-1$
		rotateCursor = toolkit.createCustomCursor(image, new Point(0, 0),
				OtherInterface.getString("MainWindow.5")); //$NON-NLS-1$

		// vérification existance fractal par défaut
		String fr = BiomorphLauncher.tOpt.getStringValue(StringEnum.dftFractal);
		if (BiomorphLauncher.class.getResource("/fract/algo/" + fr + ".class") == null) { //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane
					.showMessageDialog(this, OtherInterface.getString("MainWindow.8") //$NON-NLS-1$
							+ fr + OtherInterface.getString("MainWindow.9") //$NON-NLS-1$
							+ OtherInterface.getString("MainWindow.10"), //$NON-NLS-1$
							OtherInterface.getString("MainWindow.11"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
			BiomorphLauncher.gOpt.changeOption("dftFractal", "\"Mandelbrot\""); //$NON-NLS-1$ //$NON-NLS-2$
			BiomorphLauncher.tOpt.modifierOption("dftFractal", OtherInterface.getString("MainWindow.15")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// //////////////////////////////////////
		// UNITE DE CALCUL
		this.nbCalculUnit = Runtime.getRuntime().availableProcessors();
		this.calculerNbThread();

		// //////////////////////////////////////
		// Instanciation des composants de base
		this.menu = new AddMenu(this);
		this.menu.setVisible(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.menuBar));
		this.mng = new RenderManager(this);
		this.macro = new MacroEditor(this);
		this.mng.setVisible(false);
		this.tb = new AddToolbars(this);
		this.container = this.getContentPane();
		this.bas = new BottomPanel(this);
		this.bas.setRampVisible(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.colorPan));
		this.bas.setStatutVisible(BiomorphLauncher.tOpt
				.getBooleanValue(BooleanEnum.stateBar));
		this.panelParametre = new ParamIndex(this);
		this.container.add(this.panelParametre, BorderLayout.EAST);
		this.container.add(this.bas, BorderLayout.SOUTH);
		this.tabHisto = new HistoriqueTab();
		this.updateHistoriqueButton();

		this.pan = new Panel(this);
		pan.addKeyListener(this);
		pan.addMouseListener(new MouseAdapter() {
			/**
			 * Focus au panel quand la souris passe à l'intérieur.
			 */
			public void mouseEntered(MouseEvent ev) {
				pan.requestFocus();
				pan.onPanel = true;
				pan.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		});

		scroll = new JScrollPane(pan);
		container.add(scroll);
		pan.requestFocus();
		this.menu.updateCheckBoxes();
		
		// //////////////////////////////////////
		// Choix du premier algorithme
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
		}
		this.changeFractal(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.dftFractal));
		this.validate(); // recalcul dimensions
		fract.adaptDefaultCoordToNewDim(pan.getWidth(), pan.getHeight());
		this.addHistoryView();
		this.menu.updateCheckBoxes();

		this.updateStatutBar();
		this.pan.setPreferredSize(null);
		pan.repaint();
		this.mng.updateSliders(null);

		// //////////////////////////////////////
		// dégradé de couleur par défaut ?
		int idcol = BiomorphLauncher.tOpt.getIntValue(IntEnum.dftColRamp);
		try {
			if (idcol > 0) {
				this.bas.crc.removeAllColorAndSetThese(BiomorphLauncher.tCol
						.getStructSaveColorAtId(idcol)
						.getArrayOfColorAdvanced());
				this.bas.updateCrcIterationRepaint();
			}
		} catch (NullPointerException e) { // structure nulle = id inexistant
			JOptionPane
					.showMessageDialog(
							this,
							OtherInterface.getString("MainWindow.16"), //$NON-NLS-1$
							OtherInterface.getString("MainWindow.17"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
			BiomorphLauncher.gOpt.changeOption("dftColRamp", 0); //$NON-NLS-1$
			BiomorphLauncher.tOpt.modifierOption("dftColRamp", 0); // en cas de //$NON-NLS-1$
			// nouvelle
			// fenêtre
		}
	}

	/**
	 * Instancie les fractales d'après leur nom et fait les opérations
	 * nécessaires au recalcul et au bon affichage.
	 * 
	 * @param nomClasse
	 *            Nom de fractal (voir fichier .class)
	 */
	@SuppressWarnings("unchecked")
	public void changeFractal(String nomClasse) {
		// choix du fractal
		if (!this.premierFractal)
			manageCalcul(Calcul.arret);
		BiomorphLauncher.writeProcedure(OtherInterface.getString("MainWindow.20") + nomClasse); //$NON-NLS-1$
		this.nameFractal = nomClasse;

		try {
			Class cl = Class.forName("fract.algo." + nomClasse); //$NON-NLS-1$
			Class[] types = new Class[] { MainWindow.class };
			Constructor ct = cl.getConstructor(types);

			fract = (BaseFractale) ct.newInstance(new Object[] { this });
			fractCarte = (BaseFractale) ct.newInstance(new Object[] { this });
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			JOptionPane
					.showMessageDialog(
							this,
							OtherInterface.getString("MainWindow.22") //$NON-NLS-1$
									+ nomClasse
									+ OtherInterface.getString("MainWindow.23"), //$NON-NLS-1$
							OtherInterface.getString("MainWindow.24"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			BiomorphLauncher.writeError(OtherInterface.getString("MainWindow.25") + nomClasse); //$NON-NLS-1$
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		// assignation des threads
		tb.TfIt.setText(this.fract.getIterations() + ""); //$NON-NLS-1$
		this.fract.setDecalageCalculUnsynchronized(this.nbCalculUnit, 0);

		// mise à jour des composants graphiques
		this.bg = this.fract.getBackground();
		if (this.pts != null)
			this.pts.resetValues();
		this.bas.crc.setNumberOfLines(fract.getIterations());
		this.panelParametre.carte.setFractals(fractCarte, fract);
		this.panelParametre.getParamNavig().updateAllComponents();
		this.panelParametre.updatePanelArguments();
		this.tb.panColor.setBackground(this.bg);

		// si ce n'est pas le 1er calcul on met à jour les dimensisns
		if (!this.premierFractal) {
			this.fract.adaptDefaultCoordToNewDim(pan.getWidth(), pan
					.getHeight());
			this.panelParametre.carte.setFractals(this.fractCarte, this.fract);
			this.launchCalculAndDisplay(true);
			// mise à jour historique et première vue
			this.tabHisto.resetHistorique();
			this.addHistoryView();
			this.updateHistoriqueButton();
		}
		this.premierFractal = false;
		this.macro.updateLabelsAndValues();
	}

	/**
	 * Procédure d'application des données de la barre d'outils sur le fractal
	 */
	public void appliFractOption() {
		int ite;
		try {
			ite = Integer.parseInt(this.tb.TfIt.getText());
			this.fract.setIterations(ite);
			this.bas.updateCrcIterationRepaint();
			BiomorphLauncher
					.writeAction(OtherInterface.getString("MainWindow.27")); //$NON-NLS-1$
			if (this.tb.repaintAfterValid.isSelected())
				this.launchCalculAndDisplay(true);
			return;

		} catch (NumberFormatException e) {
		}
		this.tb.TfIt.setBackground(new Color(255, 222, 222));
	}

	/**
	 * Met à jour la barre de statut.
	 */
	public void updateStatutBar() {
		this.bas.statut.setText(this.avancement + " % - " //$NON-NLS-1$
				+ pan.getWidth() + "x" + pan.getHeight() + " - " //$NON-NLS-1$ //$NON-NLS-2$
				+ this.nbThread + " threads - " + formatTime(this.ms) //$NON-NLS-1$
				+ (this.redim ? OtherInterface.getString("MainWindow.1") : "")); //$NON-NLS-1$ //$NON-NLS-2$
		this.bas.progress.setValue(this.avancement);
	}

	/**
	 * Retourne un argument en millisecondes de la forme XXmn, XXsec, XXms
	 * 
	 * @param n
	 *            Durée en millisecondes
	 * @return Donnée temporelle formatée séparée mn sec ms
	 */
	private String formatTime(int n) {
		String s = ""; //$NON-NLS-1$
		if (n > 60 * 1000) {
			s += ((int) n / 60000) + OtherInterface.getString("MainWindow.35"); //$NON-NLS-1$
			n %= 60000;
		}
		if (n > 1000) {
			s += ((int) n / 1000) + OtherInterface.getString("MainWindow.36"); //$NON-NLS-1$
			n %= 1000;
		}
		s += n + OtherInterface.getString("MainWindow.37"); //$NON-NLS-1$
		return s;
	}

	/**
	 * Relai pour retourner la couleur du dégradé.
	 */
	public synchronized Color getColorAtIteration(int i) {
		if (i < 0)
			System.out.println(OtherInterface.getString("MainWindow.38")); //$NON-NLS-1$
		if (this.tb.alternateColors.isSelected())
			return this.getAlternateColor(i);
		if (this.bas.crc.getNumberOfLines() > i) {
			return this.bas.crc.getColorAtOffset(i);
		} else {
			return Color.red;
			/*
			 * BiomorphLauncher.writeError("Les itérations de dégradés n'ont pas "
			 * + "été calculé avant la demande, " +
			 * "vérifier l'ordre des procédure."); System.exit(-1); // erreur
			 * grave à corriger
			 */
		}
		// return null;
	}

	/**
	 * <p>
	 * Procédure pour un déplacement [déplacement des coordonnées, déplacement
	 * de la matrice, recalcul partiel des point, repaint du panel puis
	 * regénération du buffer].
	 * </p>
	 * <p>
	 * En cas de figure ébauchée, la fonction demande à la matrice de point
	 * d'éffacer les lignes déjà enregistrées en bord d'image pour éviter des
	 * zones non calculées <i>(en effet le moteur de rendu fait un saut de n
	 * pixels et n'ira pas chercher entre s'il y a du vide)</i>
	 * </p>
	 * <p>
	 * La taille du panneau de dessin (important seulement si le dessin est
	 * ébauché) est demandée explicitement pour pouvoir tricher sur l'écrasement
	 * des lignes, entrez simplement panneau.getWidth() ou panneau.getHeight()
	 * pour appliquer un déplacement normal.
	 * </p>
	 * 
	 * @param decX
	 *            Abcisse de déplacement (négatif = dessin vers la gauche)
	 * @param decY
	 *            Ordonnée de déplacement (négatif = dessin vers le haut)
	 * @param panWidth
	 *            La longueur du panneau de dessin.
	 * @param panHeight
	 *            La hauteur du panneau de dessin.
	 */
	public void deplacerFigure(final int decX, final int decY,
			final int panWidth, final int panHeight) {
		int preci = this.tb.getPrecision();
		int x = panWidth + decX - preci;
		int y = panHeight + decY - preci;
		this.fract.deplacer(decX, decY);
		this.pts = this.pts.decalerPoints(decX, decY);

		// System.out.println("x=" + x + "    y=" + y);
		if (decX < 0 && x > 0)
			this.pts.eraseLines(true, x - x % preci, preci * 2);

		if (decY < 0 && y > 0)
			this.pts.eraseLines(false, y - y % preci, preci * 2);

		this.launchCalculAndDisplay(false);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		this.pan.bufferToImage();
	}

	/**
	 * Procédure de déplacement de la fenêtre : quand l'image se déplace sans
	 * recalculer l'image.
	 * 
	 * @param decX
	 *            Décalage X en pixels
	 * @param decY
	 *            Décalage Y en pixels
	 */
	public void deplacementFigure(int decX, int decY) {
		// si la précision st diminuée, on décale selon les carrés de précision
		int preci = this.tb.getPrecision();
		if (preci > 1) {
			while (decX % (preci - 1) != 0)
				decX++;
			while (decY % (preci - 1) != 0)
				decY++;
		}
		// pan.buff.
		// System.out.println("déplacement " + decX + ";" + decY);

		this.pan.decX = decX;
		this.pan.decY = decY;
		this.pan.repaint();
	}

	/**
	 * Met à jours les composants de la rotation et lance un calcul si besoin.
	 * 
	 * @param angle
	 *            Angle en radians
	 * @param calculer
	 *            Tous recalculer
	 * @param fromPanelDessin
	 *            vrai si provient du panneau de dessin principal
	 */
	public void setRotation(double angle, final boolean calculer,
			boolean fromPanelDessin) {
		angle = (int) (angle * 10) / 10d;
		// mise à jour des composants
		if (fromPanelDessin)
			this.panelParametre.getParamNavig().updateRotationTools(angle);
		else {
			this.pan.angleActuel = angle;
			this.pan.repaint();
		}
		// recalcul si action terminée
		if (calculer && this.fract.getAngle() != angle) {
			this.fract.setAngle(angle);
			this.launchCalculAndDisplay(true);
		}
	}

	/**
	 * Lance le calcul complet. Analyse le nombre de threads, initialise les
	 * variables de calculs avant.
	 * 
	 * @param recalculateAll
	 *            Tout recalculer
	 */
	public void launchCalculAndDisplay(boolean recalculateAll) {
		// si le calcul a été interrompu, on le relance en entier
		if (this.avancement < 100) {
			// détection calcul relancée par repaint redimensionnement du panel
			if (!this.fract.isCalculFinished()) {
				this.redim = true;
				return;
			} else
				recalculateAll = true;
		}

		// itérations automatiques
		if (this.getToolBars().isIterAuto()) {
			this.fract.setIterations(this.fract.getAutoIteration());
			this.tb.TfIt.setText(this.fract.getIterations() + ""); //$NON-NLS-1$
			this.getBottomPanel().updateCrcIterationRepaint();
		}

		// GESTION DES THREADS
		this.redim = false; // savoir si panel à été redimensionné pendant rendu
		this.fract.initializeVariablesForXThreads(nbThread);
		this.fract.initialiserVariablesCalculGlobales(nbThread);
		this.fract.setDirectDisplay(true);

		// GESTION RENDU MULTIMACHINES
		this.fract.setDistantProperties(this.mng.getArrayComputers(), this.mng
				.getArrayNbTasks(), this.mng.getArrayMdp());

		// gestion calcul
		this.fract.setPrecision(this.tb.getPrecision());
		this.fract.setBufferAndGraphics(this.pan.getGraphics(), this.pan.buff);
		this.fract.setRecalculateAll(recalculateAll);

		// gestion fenêtre et variables
		this.tb.jb_lectpause.setEnabled(true);
		this.tb.jb_stop.setEnabled(true);
		this.tb.setEnableGrids(false);
		this.ms = 0;

		// recopie des pixels déjà calculés dans le buffer.
		if (!recalculateAll && this.tb.getPrecision() > 0) {
			for (int u = 0; u < this.pts.getRows(); u++) {
				for (int w = 0; w < this.pts.getColumns(); w++) {
					if (pts.getColor(u, w) != null)
						this.pan.buff.setRGB(u, w, pts.getColor(u, w).getRGB());
				}
			}
		}

		// Lancement des threads
		boolean ok;
		this.dstart = new Date();
		for (int i = 0; i < nbThread; i++) {
			ok = false;
			// on vérifit que les threads se lancent un a un
			while (!ok) {
				try {
					this.fract.setDecalageCalcul(nbThread, i, true);
					this.threadCalcul[i] = new Thread(this.fract);
					this.threadCalcul[i].start();
					ok = true;

				} catch (ThreadNotReadyException e1) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
					ok = false;
				}
			}
		}

		ObtainProgress progress = new ObtainProgress();
		Thread t1 = new Thread(progress);
		t1.start();
	}

	/**
	 * <p>
	 * Manager le calcul en cours. Pour l'argument d'entrée, utilisez les
	 * valeurs de l'énumération Calcul :
	 * </p>
	 * <ul>
	 * <li>Calcul.pause = mettre en pause le calcul</li>
	 * <li>Calcul.lecture = relancer le calcul depuis sa pause</li>
	 * <li>Calcul.arret = arrêter totalement le calcul, reprise impossible</li>
	 * <li>Calcul.playPause</li>
	 * </ul>
	 * 
	 * @param choix
	 *            Enum Calcul [pause, lecture, arret...]
	 */
	public void manageCalcul(final Calcul choix) {
		switch (choix) {
		case pause: // pause
			for (int i = 0; i < nbThread; i++)
				if (this.threadCalcul[i].isAlive())
					this.fract.setPause(true);
			this.enPause = true;
			this.tb.jb_lectpause.setIcon(new ImageIcon(BiomorphLauncher.class
					.getResource("/icon/interfaces/interface_lecture.png"))); //$NON-NLS-1$
			break;
		case lecture: // lecture
			for (int i = 0; i < nbThread; i++)
				if (this.threadCalcul[i].isAlive())
					this.fract.setPause(false);
			this.enPause = false;
			this.tb.jb_lectpause.setIcon(new ImageIcon(BiomorphLauncher.class
					.getResource("/icon/interfaces/interface_pause.png"))); //$NON-NLS-1$
			break;
		case arret: // arrêt
			for (int i = 0; i < nbThread; i++)
				if (this.threadCalcul != null && this.threadCalcul[i] != null
						&& this.threadCalcul[i].isAlive())
					this.fract.end();
			this.tb.jb_lectpause.setEnabled(false);
			this.tb.jb_stop.setEnabled(false);
			while (!this.fract.isCalculFinished()) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
			}
			break;
		case playPause: // lecture si pause et inverse
			if(this.enPause)
				this.manageCalcul(Calcul.lecture);
			else
				this.manageCalcul(Calcul.pause);
			break;
		}
	}

	/**
	 * Classe qui vérifie périodiquement l'état du calcul.
	 */
	class ObtainProgress extends Thread {
		/**
		 * <p>
		 * Méthode d'évaluation du temps de calcul. Attention le comptage du
		 * temps comporte quelques subtilités : <b>le temps affiché du début à
		 * la fin est suffisement précis</b> puisqu'il prends la différence de
		 * deux objets Date. En revanche le temps calculé intermédiaire est fait
		 * en partie par des addition de millisecondes entre plusiqueurs
		 * commandes sleep, ce qui est totalement imprécis. Le vrai temps est
		 * calculé environ toutes les 200ms dans un calcul en plus de la toute
		 * fin, mais il se peut qu'il y ai des aléas à l'intérieur de ce
		 * créneau.
		 * </p>
		 * <p>
		 * <b>En cas de pause</b>, le temps total devient légèrement plus
		 * instable puisque une variable est incrémenté toutes les 50ms puis son
		 * total soustrait au nombre final. Donc s'il y a beaucoup de pause, le
		 * résultat peut varier... Le processeur n'étant pas en surcharge en
		 * pause, le temps est normalement bien incrémenté et le tout
		 * satisfaisant.
		 * </p>
		 */
		public void run() {
			this.tmpPause = 0;
			while (true) {
				try {
					avancement = fract.getAvancement();
					if (avancement >= 100) {
						// on calcul le "vrai" temps au cas où il aurait été
						// erroné par la surcharge du processeur
						dend = new Date();
						ms = (int) (dend.getTime() - dstart.getTime())
								- this.tmpPause;
						updateStatutBar();

						tb.jb_lectpause.setEnabled(false);
						tb.jb_stop.setEnabled(false);
						tb.setEnableGrids(true);
						pan.bufferToImage(); // on peut mettre à l'affichage le
						// buffer complet
						pan.repaint();

						// on prévient en cas de redimensionnement
						if (redim) {
							launchCalculAndDisplay(false);
							JOptionPane
									.showMessageDialog(
											getParent(),
											OtherInterface.getString("MainWindow.42") //$NON-NLS-1$
													+ OtherInterface.getString("MainWindow.43") //$NON-NLS-1$
													+ OtherInterface.getString("MainWindow.44"), //$NON-NLS-1$
											OtherInterface.getString("MainWindow.45"), //$NON-NLS-1$
											JOptionPane.WARNING_MESSAGE);
							repaint(); // en cas de pixels sur l'interface
						}
						return;
					} else if (ms % 20 == 0) {
						// toutes les 200ms on vérifie le vrai temps
						dend = new Date();
						ms = (int) (dend.getTime() - dstart.getTime()) / 10;
						ms *= 10;
						ms -= this.tmpPause;
					}
					updateStatutBar();
					Thread.sleep(50);
					if (!enPause)
						ms += 50;
					else
						// compte seulement le temps en calcul
						this.tmpPause += 50;
					// normalement le temps en pause est bien comptabilisé
					// puisque le processeur n'est pas surchargé

				} catch (InterruptedException e) {
				}
			}
		}

		int tmpPause;
	}

	/**
	 * Charger un fractal depuis l'identification de la vue. Modifie tous les
	 * paramètres, les couleurs, itérations, coordonnées etc avec mise à jour de
	 * l'interface.
	 * 
	 * @param ssv
	 *            Structure de stockage de vue
	 */
	public void loadFractal(final StructSaveVue ssv) {
		// changement du fractal
		manageCalcul(Calcul.arret);
		this.premierFractal = true;
		this.changeFractal(ssv.getFractalName());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// mise à jour dégradé couleurs/ itérations
		this.fract.setIterations(ssv.getIntValue(EnumVue.iter));
		// System.out.println("enumvue=" + ssv.getIntValue(EnumVue.idramp));
		this.bas.crc.removeAllColorAndSetThese(BiomorphLauncher.tCol
				.getStructSaveColorAtId(ssv.getIntValue(EnumVue.idramp))
				.getArrayOfColorAdvanced());
		this.bas.updateCrcIterationRepaint();
		// composants de la JToolBar
		this.tb.updateComponents(ssv);
		this.tb.repaint();
		// coordonnées dans le fractal
		this.fract.changeCoordAndScale(ssv.getDoubleValue(EnumVue.posX), ssv
				.getDoubleValue(EnumVue.posY), ssv
				.getDoubleValue(EnumVue.scale));
		// transmition d'arguments
		this.fractCarte.setArguments(ssv.getArrayArgs());
		this.fract.setArguments(ssv.getArrayArgs());
		// coordonnées dans la boîte de navigation
		this.panelParametre.getParamNavig().updateAllComponents();
		this.panelParametre.getParamFractal().updatePanelArguments();
		this.launchCalculAndDisplay(true);
	}

	/**
	 * Rassemble les éléments actuels nécessaires à la création d'une nouvelle
	 * vue et demande au gestionnaire d'ajouter une nouvelle ligne
	 * d'identification.
	 */
	public void saveFractal() {
		int exists = BiomorphLauncher.tCol
				.areTheseColorsLooksToAny(this.bas.crc.getArrayOfColors());
		// si le dégradé de couleurs n'existe pas on l'enregistre en plus
		if (exists == -1) {
			BiomorphLauncher.gOpt.manageColor(-1, "reservedForVue", -1, //$NON-NLS-1$
					this.bas.crc);
			exists = BiomorphLauncher.tCol.getLastId();
		}
		String nom = JOptionPane.showInputDialog(this,
				OtherInterface.getString("MainWindow.47"), OtherInterface.getString("MainWindow.48")); //$NON-NLS-1$ //$NON-NLS-2$
		if (nom == null)
			return;
		BiomorphLauncher.gOpt.manageVue(-1, new StructSaveVue(nom,
				this.nameFractal, exists, this.bg, this.altCol1, this.altCol2,
				this.tb.alternateColors.isSelected(), this.fract
						.getIterations(), this.fract.getScale(), this.fract
						.getPosX(), this.fract.getPosY(), this.pan.getWidth(),
				this.pan.getHeight(), this.fract.getArgumentsArray()));
	}

	public String getFractalName() {
		return this.nameFractal;
	}

	/**
	 * Remet la vue par défaut du fractal en relancant le calcul complet.
	 */
	public void setFractalDefaultView() {
		this.fract.adaptDefaultCoordToNewDim(pan.getWidth(), pan.getHeight());
		this.launchCalculAndDisplay(true);
	}

	/**
	 * Procédure pour modifier en direct les coordonnées de position x, Y dans
	 * le panneau latéral d'information du plan (textfields) et zoom selon un
	 * décalage en cours.
	 * 
	 * @param decX
	 *            Décalage X en pixels
	 * @param decY
	 *            Décalage Y en pixels
	 */
	public void miseAJourCoordTF(final int decX, final int decY) {
		this.panelParametre.getParamNavig().updateCoord(
				this.pan.mw.fract.getPosX() - decX * this.fract.getScale(),
				this.pan.mw.fract.getPosY() - decY * this.fract.getScale());
	}

	/**
	 * Met à jour les icônes de l'historique.
	 */
	public void updateHistoriqueButton() {
		this.tb.jb_suiv.setEnabled(!this.tabHisto.isLastView());
		this.tb.jb_prec.setEnabled(!this.tabHisto.isFirstView());
	}

	/**
	 * Ajoute une vue dans l'historique.
	 * 
	 * @return valeur émise par ajouterVue()
	 */
	public boolean addHistoryView() {
		boolean val = this.tabHisto.ajouterVue(fract.getPosX(),
				fract.getPosY(), fract.getScale());
		this.updateHistoriqueButton();
		return val;
	}

	/**
	 * Naviguer dans l'ihstorique. Argument positif pour un +1, argument négatif
	 * pour un -1.
	 * 
	 * @param i
	 *            Différence (négatif pour avant, position pour après)
	 */
	public void goToHistoryView(final int i) {
		int offset;
		this.manageCalcul(Calcul.arret);
		if (i >= 0)
			offset = this.tabHisto.getOffset() - 1;
		else
			offset = this.tabHisto.getOffset() + 1;
		this.fract.setCoordonnees(this.tabHisto.getDouble(offset, 0),
				this.tabHisto.getDouble(offset, 1), this.tabHisto.getDouble(
						offset, 2));
		this.updateHistoriqueButton();
		this.launchCalculAndDisplay(true);
	}

	/**
	 * @return dimension du panel
	 */
	public Dimension getPanelDimension() {
		return new Dimension(this.pan.getWidth(), this.pan.getHeight());
	}

	/**
	 * @return le fractal principal du panel
	 */
	public BaseFractale getFract() {
		return this.fract;
	}

	/**
	 * @return fenêtre de rendu réseau.
	 */
	public RenderManager getRenderManager() {
		return this.mng;
	}

	/**
	 * @return fenêtre de macros.
	 */
	public MacroEditor getMacroEditor() {
		return this.macro;
	}

	/**
	 * @return le fractal de la minicarte.
	 */
	public BaseFractale getFractCarte() {
		return this.fractCarte;
	}

	/**
	 * @return panneau de paramètres
	 */
	public ParamIndex getParams() {
		return this.panelParametre;
	}

	/**
	 * @return panel du bas (couleurs et statut)
	 */
	public BottomPanel getBottomPanel() {
		return this.bas;
	}

	/**
	 * @return barre d'outils
	 */
	public AddToolbars getToolBars() {
		return this.tb;
	}

	/**
	 * @return le panel de dessin.
	 */
	public Panel getPan() {
		return this.pan;
	}

	/**
	 * @return menu
	 */
	public AddMenu getMenu() {
		return this.menu;
	}

	/**
	 * @return la couleur par défaut du fractal.
	 */
	public Color getBgFractColor() {
		return this.bg;
	}

	public String[] getFractalsName() {
		return this.panelParametre.getParamFractal().getFractalsList();
	}

	/**
	 * Obtenir une couleur alternative. Un nombre pair donne la couleur
	 * alternative 1. Une nombre impair donne la couleur alternative 2.
	 * 
	 * @param i
	 *            entier pair ou impair
	 * @return couleur alternative 1 ou 2.
	 */
	public Color getAlternateColor(final int i) {
		return (i % 2 == 0) ? this.altCol1 : this.altCol2;
	}

	/**
	 * Crée un tableau de la taille du nombre de threads à utiliser.
	 */
	public void calculerNbThread() {
		if (BiomorphLauncher.tOpt.getIntValue(IntEnum.nbThread) <= 0)
			nbThread = this.nbCalculUnit * 2;
		else
			nbThread = BiomorphLauncher.tOpt.getIntValue(IntEnum.nbThread);
		this.threadCalcul = new Thread[this.nbThread];
	}

	/**
	 * Donner les couleurs spéciales du fractal : la couluer par défaut, et les
	 * couleurs alternatives. Pour ne pas modifier une couleur, entrer une
	 * valeur null.
	 * 
	 * @param bg
	 *            Couleur de fond
	 * @param alt1
	 *            Couleur alternative 1
	 * @param alt2
	 *            Couleur alternative 2
	 */
	public void setFractColors(Color bg, Color alt1, Color alt2) {
		if (bg != null) {
			this.bg = bg;
			this.tb.panColor.setBackground(this.bg);
		}
		if (alt1 != null) {
			this.altCol1 = alt1;
			this.tb.panAltColor1.setBackground(this.altCol1);
		}
		if (alt2 != null) {
			this.altCol2 = alt2;
			this.tb.panAltColor2.setBackground(this.altCol2);
		}
	}

	/**
	 * Détecte les touches enfoncées et retient si c'est une touche raccourci ;
	 * pour l'instant : {'ESPACE'} sont des touches raccourcis. Cette méthode
	 * est indispensable pour détecter un raccourcis clavier+souris
	 */
	public void keyPressed(KeyEvent ev) {
		int key = ev.getKeyCode();
		if(key == 116){	// F5
			this.manageCalcul(Calcul.arret);
			this.launchCalculAndDisplay(true);
		} else if(key == 115 || key == 20){	// F4 ou capsLock
			this.manageCalcul(Calcul.playPause);
		} else if(key == 27) // Echap
			this.manageCalcul(Calcul.arret);
		
		if (this.shiftDown && key == KeyEvent.VK_M) {
			this.getMenu().setMenuVisible(!this.getMenu().isVisible());
			this.menu.updateCheckBoxes();
		}
		switch (key) {
		case KeyEvent.VK_SPACE:
			this.spaceDown = true;
			break;
		case KeyEvent.VK_CONTROL:
			this.ctrlDown = true;
			break;
		case KeyEvent.VK_SHIFT:
			this.shiftDown = true;
			break;
		}
		this.appliCursor();
	}

	/**
	 * Oublie la touche de raccourci si s'en est une
	 */
	public void keyReleased(KeyEvent ev) {
		switch (ev.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			spaceDown = false;
			break;
		case KeyEvent.VK_CONTROL:
			this.ctrlDown = false;
			pan.endOfZoom();
			break;
		case KeyEvent.VK_SHIFT:
			this.shiftDown = false;
			pan.endOfZoom();
			break;
		}
		this.appliCursor();
	}

	/**
	 * Applique le curseur sur le panel de représentation conformément à la
	 * touche appuyée (ctrolDown, spaceDown, shiftDown)
	 */
	public void appliCursor() {
		this.pan.setCursor(Cursor.getDefaultCursor());
		if (this.spaceDown)
			this.pan.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		else if (this.ctrlDown)
			this.pan.setCursor(Cursor
					.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		else if (this.shiftDown)
			this.pan.setCursor(rotateCursor);
	}

	public void keyTyped(KeyEvent ev) {
	} // fonction impl inutile

	// THREADS
	private int nbCalculUnit;
	private Thread threadCalcul[];
	private boolean premierFractal = true;
	public int nbThread;
	public int avancement = 100, ms = 0;
	public boolean enPause = false;
	private Date dstart, dend;
	private boolean redim;

	// énumérations
	public enum Calcul {
		pause, lecture, arret, playPause
	}

	// APPELS DE COMPOSANTS
	final private AddToolbars tb;
	private Cursor rotateCursor;
	private BaseFractale fract, fractCarte;
	public boolean spaceDown, ctrlDown, shiftDown;
	private String nameFractal;
	final private RenderManager mng;
	final private MacroEditor macro;
	final private AddMenu menu;

	public RegPoint pts; // idem, panel y a accès
	public HistoriqueTab tabHisto;
	private Panel pan;
	public Container container;
	private ParamIndex panelParametre;
	public JScrollPane scroll;
	private BottomPanel bas;
	public Color bg, altCol1, altCol2;
	public static final int menuWidth = 230, mapWidth = 200, mapHeight = 160;
}
