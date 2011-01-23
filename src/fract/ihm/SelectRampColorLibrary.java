package fract.ihm;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import fract.BiomorphLauncher;
import fract.color.*;
import fract.opt.StructSaveColor;

/**
 * <p>
 * Fenêtre permettant d'afficher les presets de dégradés, et de renvoyer la
 * référence d'une sélection dans la fenêtre principale en argument
 * constructeur.
 * </p>
 */
final public class SelectRampColorLibrary extends JFrame implements
		MouseWheelListener, MouseListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Dessine la fenêtre. Dessines tous les composants et les ChooseRampColor
	 * verticalement.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 * @param bp
	 *            Panel inférieur (dégradé, statut)
	 */
	public SelectRampColorLibrary(MainWindow mw, BottomPanel bp) {
		// définition de la fenêtre
		this.setVisible(true);
		this.setBounds((int) mw.getLocationOnScreen().getX() + 80, (int) mw
				.getLocationOnScreen().getY() + 90, 400, 300);
		this.setTitle("Bibliothèque de dégradés de couleurs");
		this.setResizable(false);
		this.bp = bp;

		nb = BiomorphLauncher.tCol.getNbValues();
		this.crc1 = new ChooseRampColor[nb];
		ssc = BiomorphLauncher.tCol.getArrayOfStructSaveColor();

		// container des dégradé perosnnalisés
		this.c1 = new Container();
		this.c1.setLayout(new GridLayout(nb, 1));
		for (int i = 0; i < nb; i++) {
			this.crc1[i] = new ChooseRampColor(ssc[i].getArrayOfColorAdvanced());
			this.crc1[i].setEnabled(false);
			this.crc1[i].setBorder(BorderFactory.createTitledBorder(ssc[i]
					.getName()));
			this.crc1[i].setPreferredSize(new Dimension(350, 50));
			this.crc1[i].addMouseListener(this);
			this.c1.add(crc1[i]);
		}

		// on ajout au contenu
		this.js = new JScrollPane(this.c1);
		this.js.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.js.addMouseWheelListener(this);
		this.contenu = this.getContentPane();
		this.labPerso = new JLabel(
				"     Cliquez sur un dégradé pour l'appliquer :");
		this.labPerso.setPreferredSize(new Dimension(200, 22));
		this.labInfo = new JLabel(nb + " dégradés enregistrés");
		this.contenu.add(this.labPerso, BorderLayout.NORTH);
		this.contenu.add(this.labInfo, BorderLayout.SOUTH);
		this.contenu.add(this.js);
		this.validate();
	}

	/**
	 * <p>
	 * Accelère le scroll pour un défilement plus rapide.
	 * </p>
	 */
	public void mouseWheelMoved(MouseWheelEvent event) {
		final JScrollBar scrollBar = js.getVerticalScrollBar();
		final int rotation = event.getWheelRotation();
		if (scrollBar != null) {
			scrollBar.setValue(scrollBar.getValue()
					+ (scrollBar.getBlockIncrement(rotation) * rotation / 10));
		}
	}

	/**
	 * <p>
	 * Ecoute de la souris en vas de validation d'un dégradé par un clic.
	 * </p>
	 */
	public void mouseClicked(MouseEvent ev) {
		for (int i = 0; i < nb; i++) {
			if (ev.getSource() == this.crc1[i]) {
				BiomorphLauncher
						.writeAction("Sélection d'un dégradé pré-enregistré");
				this.bp.crc.removeAllColorAndSetThese(ssc[i]
						.getArrayOfColorAdvanced());
				this.bp.repaint();
				this.dispose();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
	}

	// variables
	final private BottomPanel bp;
	final private JScrollPane js;
	final private JLabel labPerso, labInfo;
	final private Container contenu, c1;
	final private ChooseRampColor crc1[];
	final private StructSaveColor[] ssc;
	final private int nb;
}
