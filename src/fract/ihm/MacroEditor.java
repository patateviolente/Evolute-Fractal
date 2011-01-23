package fract.ihm;

import javax.imageio.ImageIO;
import javax.swing.*;

import fract.BiomorphLauncher;
import fract.ihm.MainWindow.Calcul;
import fract.ihm.admin.AdminIndex;
import fract.lang.PanneauExt;
import fract.opt.EnumOption.IntEnum;
import fract.opt.EnumOption.StringEnum;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Fenêtre de lancement de calcul de la macro.
 */
public class MacroEditor extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit la fenêtre graphiquement.
	 * 
	 * @param mw
	 *            Objet MainWindow.
	 */
	public MacroEditor(final MainWindow mw) {
		// fenêtre
		this.mw = mw;
		this.setBounds(200, 300, 320, 420);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				setVisible(false);
				mw.getMenu().updateCheckBoxes();
			}
		});
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif")));
		

		// bouton modifier options
		JPanel panInfos = new JPanel();
		panInfos.setLayout(new FlowLayout());
		this.BopenAdmin = new JButton(PanneauExt.getString("MacroEditor.0")); //$NON-NLS-1$
		this.BopenAdmin.addActionListener(this);
		this.infos = new JLabel();
		panInfos.add(this.BopenAdmin, BorderLayout.NORTH);
		panInfos.add(this.infos);
		panInfos.setPreferredSize(new Dimension(290, 235));
		panInfos.setBorder(BorderFactory
				.createTitledBorder(PanneauExt.getString("MacroEditor.1"))); //$NON-NLS-1$

		// rendu en cours
		JPanel panRendu = new JPanel();
		panRendu.setBorder(BorderFactory.createTitledBorder(PanneauExt.getString("MacroEditor.2"))); //$NON-NLS-1$
		panRendu.setLayout(new GridLayout(5, 1));

		this.Bdemarrer = new JButton(PanneauExt.getString("MacroEditor.3")); //$NON-NLS-1$
		this.Bdemarrer.addActionListener(this);
		this.Bpause = new JButton(PanneauExt.getString("MacroEditor.4")); //$NON-NLS-1$
		this.Bpause.addActionListener(this);
		this.Bpause.setEnabled(false);
		JPanel panButton = new JPanel();
		panButton.setLayout(new GridLayout(1, 2));
		panButton.add(this.Bdemarrer);
		panButton.add(this.Bpause);

		this.LinfoImg = new JLabel(PanneauExt.getString("MacroEditor.5")); //$NON-NLS-1$
		this.LinfoTotal = new JLabel(PanneauExt.getString("MacroEditor.6")); //$NON-NLS-1$
		this.Pimg = new JProgressBar();
		this.Ptotal = new JProgressBar();

		panRendu.add(panButton);
		panRendu.add(this.LinfoImg);
		panRendu.add(this.Pimg);
		panRendu.add(this.LinfoTotal);
		panRendu.add(this.Ptotal);

		// ajouts finaux
		this.add(panInfos, BorderLayout.NORTH);
		this.add(panRendu);
	}

	/**
	 * Met à jour le contenu des informations dans les étiquettes du panneau de
	 * rendu sur la partie récapitulatif. Les données sont prisent dans tOpt en
	 * static.
	 */
	public void updateLabelsAndValues() {
		// vérification param offset ne dépasse pas sinon désactivation
		double[] d = this.mw.getFract().getArgumentsArray();
		if (d == null
				|| BiomorphLauncher.tOpt.getIntValue(IntEnum.Mvar1offs) > d.length)
			BiomorphLauncher.tOpt.modifierOption("Mvar1offs", -1); //$NON-NLS-1$
		if (d == null
				|| BiomorphLauncher.tOpt.getIntValue(IntEnum.Mvar2offs) > d.length)
			BiomorphLauncher.tOpt.modifierOption("Mvar2offs", -1); //$NON-NLS-1$

		// ///////// mise à jour variables de calcul ////////////
		// détermintation variable d'animation et nombre d'images
		if (BiomorphLauncher.tOpt.getStringValue(StringEnum.MmainVar).equals(
				"Miter")) { //$NON-NLS-1$
			this.nbImg = (int) Math.abs(Math.floor((BiomorphLauncher.tOpt
					.getIntValue(IntEnum.MiterFin) - BiomorphLauncher.tOpt
					.getIntValue(IntEnum.MiterInit))
					/ Double.parseDouble(BiomorphLauncher.tOpt
							.getStringValue(StringEnum.MrenderPas))));
		} else {
			StringEnum sei = StringEnum.valueOf(BiomorphLauncher.tOpt
					.getStringValue(StringEnum.MmainVar)
					+ "init"); //$NON-NLS-1$
			StringEnum sef = StringEnum.valueOf(BiomorphLauncher.tOpt
					.getStringValue(StringEnum.MmainVar)
					+ "fin"); //$NON-NLS-1$
			this.nbImg = (int) Math.abs(Math
					.floor((Double.parseDouble(BiomorphLauncher.tOpt
							.getStringValue(sef)) - Double
							.parseDouble(BiomorphLauncher.tOpt
									.getStringValue(sei)))
							/ Double.parseDouble(BiomorphLauncher.tOpt
									.getStringValue(StringEnum.MrenderPas))));
		}
		if (this.nbImg == 0)
			this.nbImg = 1;

		// états initiaux
		this.IdecX = Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.MposXinit));
		this.IdecY = Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.MposYinit));
		this.Iscale = Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.Mscaleinit));
		this.Ivar1 = Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.Mvar1init));
		this.Ivar2 = Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.Mvar2init));
		this.Iiter = BiomorphLauncher.tOpt.getIntValue(IntEnum.MiterInit);

		// états finaux
		this.OdecX = (Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.MposXfin)) - this.IdecX)
				/ this.nbImg;
		this.OdecY = (Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.MposYfin)) - this.IdecY)
				/ this.nbImg;
		this.Oscale = (Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.Mscalefin)) - this.Iscale)
				/ this.nbImg;
		this.Ovar1 = (Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.Mvar1fin)) - this.Ivar1)
				/ this.nbImg;
		this.Ovar2 = (Double.parseDouble(BiomorphLauncher.tOpt
				.getStringValue(StringEnum.Mvar2fin)) - this.Ivar2)
				/ this.nbImg;
		this.Oiter = (BiomorphLauncher.tOpt.getIntValue(IntEnum.MiterFin) - this.Iiter)
				/ this.nbImg;

		// /////// Màj labels //////////////////
		final int offsetVar1 = BiomorphLauncher.tOpt
				.getIntValue(IntEnum.Mvar1offs);
		final int offsetVar2 = BiomorphLauncher.tOpt
				.getIntValue(IntEnum.Mvar2offs);
		String recap = PanneauExt.getString("MacroEditor.7") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MposXinit)
				+ "</font>   à::<font color=\"#1543AA\">" //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MposXfin)
				+ PanneauExt.getString("MacroEditor.8") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MposYinit)
				+ "</font>   à::<font color=\"#1543AA\">" //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MposYfin)
				+ "</font><br />" //$NON-NLS-1$
				+ PanneauExt.getString("MacroEditor.17") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.Mscaleinit)
				+ PanneauExt.getString("MacroEditor.18") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.Mscaleinit)
				+ PanneauExt.getString("MacroEditor.19") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.MiterInit)
				+ PanneauExt.getString("MacroEditor.20") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getIntValue(IntEnum.MiterFin)
				+ PanneauExt.getString("MacroEditor.21") //$NON-NLS-1$
				+ ((offsetVar1 < 0 || offsetVar1 > 2) ? PanneauExt.getString("MacroEditor.22") //$NON-NLS-1$
						: PanneauExt.getString("MacroEditor.23") //$NON-NLS-1$
								+ offsetVar1
								+ PanneauExt.getString("MacroEditor.24") //$NON-NLS-1$
								+ BiomorphLauncher.tOpt
										.getStringValue(StringEnum.Mvar1init)
								+ PanneauExt.getString("MacroEditor.25") //$NON-NLS-1$
								+ BiomorphLauncher.tOpt
										.getStringValue(StringEnum.Mvar1fin))
				+ "</font><br /> - <font color=\"#448B4D\"><b>Var2</b></font> " //$NON-NLS-1$
				+ ((offsetVar2 < 0 || offsetVar2 > 2) ? PanneauExt.getString("MacroEditor.27") //$NON-NLS-1$
						: PanneauExt.getString("MacroEditor.28") //$NON-NLS-1$
								+ offsetVar2
								+ PanneauExt.getString("MacroEditor.29") //$NON-NLS-1$
								+ BiomorphLauncher.tOpt
										.getStringValue(StringEnum.Mvar2init)
								+ PanneauExt.getString("MacroEditor.30") //$NON-NLS-1$
								+ BiomorphLauncher.tOpt
										.getStringValue(StringEnum.Mvar2fin))
				+ PanneauExt.getString("MacroEditor.31") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MmainVar)
				+ "</font>'</b> = <font color=\"#AA2A1D\"><b>" //$NON-NLS-1$
				+ this.nbImg
				+ PanneauExt.getString("MacroEditor.33") //$NON-NLS-1$
				+ PanneauExt.getString("MacroEditor.34") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MrenderPas)
				+ "</font><br />" //$NON-NLS-1$
				+ PanneauExt.getString("MacroEditor.36") //$NON-NLS-1$
				+ BiomorphLauncher.tOpt.getStringValue(StringEnum.MimgName)
				+ "'<br />" //$NON-NLS-1$
				+ "<i><font color=\"grey\">" //$NON-NLS-1$
				+ PanneauExt.getString("MacroEditor.39") //$NON-NLS-1$
				+ "</font></i></html>"; //$NON-NLS-1$
		this.infos.setText(recap);

	}

	/**
	 * Classe de calcul étendue de Threads. Permer de lancer la suite d'image
	 * d'après les options enregistrées.
	 */
	final class ImgSeqCalcul extends Thread {

		/**
		 * 
		 */
		public void run() {
			String nom;
			int Mv1os = BiomorphLauncher.tOpt.getIntValue(IntEnum.Mvar1offs);
			int Mv2os = BiomorphLauncher.tOpt.getIntValue(IntEnum.Mvar2offs);
			Bpause.setEnabled(true);
			BiomorphLauncher.writeProcedure(PanneauExt.getString("MacroEditor.41")); //$NON-NLS-1$
			mw.avancement = 0;

			// changement dégradé de couleurs et taille panneau
			/*
			 * mw.getBottomPanel().crc
			 * .removeAllColorAndSetThese(BiomorphLauncher.tCol
			 * .getStructSaveColorAtId(5) .getArrayOfColorAdvanced());
			 * mw.getBottomPanel().updateCrcIterationRepaint(); // redim panneau
			 * mw.getPan().setPreferredSize(new Dimension(4000, 4000));
			 * mw.getPan().repaint(); mw.container.remove(mw.scroll);
			 * mw.container.add(mw.scroll); mw.validate();
			 */

			// démarrage processus
			boucle: for (int i = 1; i <= nbImg; i++) {

				// passage arguments
				mw.getFract().setCoordonnees(IdecX, IdecY, Iscale);
				mw.getFract().setIterations(Iiter);
				if (Oiter != 0) // modification itérations
					;// mw.getBottomPanel().updateCrcIterationRepaint();
				mw.manageCalcul(Calcul.arret);
				if (Mv1os >= 0) {
					Ivar1 += Ovar1;
					mw.getFract().modifyFractArgument(Mv1os, Ivar1);
				}
				if (Mv2os >= 0) {
					Ivar2 += Ovar2;
					mw.getFract().modifyFractArgument(Mv2os, Ivar2);
				}

				// Avancement rendu
				mw.launchCalculAndDisplay(true);
				while (mw.avancement != 100) {
					Pimg.setValue(mw.avancement);
					LinfoImg.setText(PanneauExt.getString("MacroEditor.42") + i + PanneauExt.getString("MacroEditor.43") //$NON-NLS-1$ //$NON-NLS-2$
							+ mw.avancement + "%"); //$NON-NLS-1$
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				}
				Pimg.setValue(mw.avancement);
				mw.avancement = 0;

				// Écriture d l'image
				nom = BiomorphLauncher.tOpt.getStringValue(StringEnum.MimgName)
						+ i + ".png"; //$NON-NLS-1$
				try {
					ImageIO.write(mw.getPan().buff, "png", new File(nom)); //$NON-NLS-1$
				} catch (Exception e) {
					e.printStackTrace();
				}
				BiomorphLauncher.writeSubAction(nom + PanneauExt.getString("MacroEditor.47")); //$NON-NLS-1$

				// avancement global
				Ptotal.setValue((int) (((double) i / nbImg) * 100));
				LinfoTotal.setText(i + PanneauExt.getString("MacroEditor.48") + nbImg); //$NON-NLS-1$

				//  Pause ou arrêt du rendu :
				if (!active)
					break boucle;
				while (enPause) {
					try {
						Thread.sleep(50);
						if (!active)
							break boucle;
					} catch (InterruptedException e) {
					}
				}

				// incrémentation variables
				IdecX += OdecX;
				IdecY += OdecY;
				Iscale += Oscale;
				Iiter += Oiter;
			}

			if (active) {
				JOptionPane
						.showMessageDialog(
								mw,
								PanneauExt.getString("MacroEditor.49") //$NON-NLS-1$
										+ PanneauExt.getString("MacroEditor.50"), //$NON-NLS-1$
								PanneauExt.getString("MacroEditor.51"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane
						.showMessageDialog(
								mw,
								PanneauExt.getString("MacroEditor.52") //$NON-NLS-1$
										+ PanneauExt.getString("MacroEditor.53"), //$NON-NLS-1$
								PanneauExt.getString("MacroEditor.54"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
			}
			BiomorphLauncher.writeAction(PanneauExt.getString("MacroEditor.55")); //$NON-NLS-1$
			Bdemarrer.setText(PanneauExt.getString("MacroEditor.56")); //$NON-NLS-1$
			enPause = active = false;
			Bpause.setEnabled(false);
			updateLabelsAndValues();
		}
	}

	/**
	 * Ecoute bouton d'ouvertur d'admin, lancement calcul
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		// Ouvrir admin
		if (src == this.BopenAdmin) {
			AdminIndex a = new AdminIndex(mw);
			a.setSelectedIndex(3);
		} //  Démarrer/ arrêter
		else if (src == this.Bdemarrer) {
			if (!this.active) {
				t = new ImgSeqCalcul();
				t.start();
				this.active = true;
				this.Bdemarrer.setText(PanneauExt.getString("MacroEditor.57")); //$NON-NLS-1$
			} else if (this.active) {
				if (JOptionPane
						.showConfirmDialog(
								this,
								PanneauExt.getString("MacroEditor.58") //$NON-NLS-1$
										+ PanneauExt.getString("MacroEditor.59")) == JOptionPane.OK_OPTION) { //$NON-NLS-1$
					this.active = false;
					this.Bdemarrer.setText(PanneauExt.getString("MacroEditor.60")); //$NON-NLS-1$
				}
			}
		} // bouton PAUSE
		else if (src == this.Bpause) {
			this.enPause = !this.enPause;
			this.Bpause.setText((this.enPause) ? PanneauExt.getString("MacroEditor.61") : PanneauExt.getString("MacroEditor.62")); //$NON-NLS-1$ //$NON-NLS-2$
			this.Bdemarrer.setEnabled(this.enPause);
		}
	}

	final private JLabel infos;
	private JButton BopenAdmin, Bdemarrer, Bpause;
	final private MainWindow mw;
	private JProgressBar Pimg, Ptotal;
	private JLabel LinfoImg, LinfoTotal;
	private Thread t;
	private boolean enPause = false, active = false;

	double IdecX, IdecY, Iscale, Ivar1, Ivar2;
	double FdecX, FdecY, Fscale, Fvar1, Fvar2;
	double OdecX, OdecY, Oscale, Ovar1, Ovar2, Oiter;
	double Cinit, Cend, Coffset;
	int nbImg, Iiter, Fiter;
}
