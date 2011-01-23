package fract.ihm.admin;

import javax.swing.*;

import fract.BiomorphLauncher;
import fract.ihm.MainWindow;
import fract.lang.AdminMsg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 * L'accueil de l'administration va appeler toutes les parties de
 * l'administration dans des onglets, elle dispose d'un bouton valider pour
 * fermer la fenêtre, les paramètres se sauvegardant instantannément lors des
 * modifications.
 * </p>
 */
public class AdminIndex extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Construit la fenêtre, les onglets.
	 * </p>
	 * 
	 * @param mw
	 *            La fenêtre principale.
	 */
	public AdminIndex(MainWindow mw) {
		// construction fenêtre
		setMw(mw);
		this.setTitle(AdminMsg.getString("AdminIndex.0")); //$NON-NLS-1$
		this.setBounds((int) mw.getLocationOnScreen().getX() + 80, (int) mw
				.getLocationOnScreen().getY() + 90, 360, 410);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"icon/interface/icon_taskbar.png")); //$NON-NLS-1$
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif"))); //$NON-NLS-1$

		// Layout
		contenu = this.getContentPane();
		contenu.setLayout(new BorderLayout());
		infoAdmin = new JLabel();
		contenu.add(infoAdmin, "South"); //$NON-NLS-1$

		onglet = new JTabbedPane();
		try {
			// création et ajouts des onglets
			AdminGeneral tGen = new AdminGeneral();
			AdminAffichage tAff = new AdminAffichage();
			AdminThread tRendu = new AdminThread();
			AdminMacro tMacro = new AdminMacro();
			AdminOption tOpt = new AdminOption();

			onglet.add(AdminMsg.getString("AdminIndex.4"), tGen); //$NON-NLS-1$
			onglet.add(AdminMsg.getString("AdminIndex.5"), tAff); //$NON-NLS-1$
			onglet.add(AdminMsg.getString("AdminIndex.6"), tRendu); //$NON-NLS-1$
			onglet.add(AdminMsg.getString("AdminIndex.7"), tMacro); //$NON-NLS-1$
			onglet.add(AdminMsg.getString("AdminIndex.8"), tOpt); //$NON-NLS-1$
			this.getContentPane().add(onglet);
			this.validate();
			this.setVisible(true);
			BiomorphLauncher.writeAction("Panneau d'administration dessiné");
		} catch (NullPointerException e) {
			// au cas où les composants ne seraient pas prêt
			this.dispose();
			BiomorphLauncher
					.writeSubAction("L'administration a été demandée alors que les composants n'étaient pas tous prêts, réessai dans 200ms");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
			}
			new AdminIndex(mw);
		}
	}

	/**
	 * Ecoute le bouton Valider qui ferme la fenêtre.
	 */
	public void actionPerformed(ActionEvent ev) {
		this.dispose();
	}

	/**
	 * Ouvre un onglet à la position.
	 * 
	 * @param pos
	 *            Position de l'onglet
	 */
	public void setSelectedIndex(int pos) {
		this.onglet.setSelectedIndex(pos);
	}

	/**
	 * <p>
	 * Rempli la variable mw. En static pour la laisser accessible aux onglets.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public static void setMw(MainWindow mw) {
		AdminIndex.mw = mw;
	}

	/**
	 * @return fenêtre Principale
	 */
	public static MainWindow getMw() {
		return mw;
	}

	// composants
	private static MainWindow mw;
	final private JTabbedPane onglet;
	final private Container contenu;
	public static JLabel infoAdmin;
}
