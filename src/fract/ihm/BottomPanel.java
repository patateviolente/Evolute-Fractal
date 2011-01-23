package fract.ihm;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

import fract.BiomorphLauncher;
import fract.color.*;
import fract.lang.BottomPan;

/**
 * <p>
 * Cette classe est un morceau de l'ihm principale composée de :
 * </p>
 * <ul>
 * <li>La barre de statut et d'avancement</li>
 * <li>le sélecteur de couleur et des boutons nécessaires à son écoute.</li>
 * </ul>
 */
final public class BottomPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Le constructeur instantancie tous les composants et les place dans un
	 * GridLayout.
	 * </p>
	 * 
	 * @param mw Objet MainWindow
	 */
	public BottomPanel(final MainWindow mw) {
		// instanciation composants
		this.mw = mw;
		this.statut = new JLabel(BottomPan.getString("BottomPanel.0")); //$NON-NLS-1$
		this.crc = new ChooseRampColor();
		this.appliquer = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_ok.png"))); //$NON-NLS-1$
		this.appliquer.setPreferredSize(new Dimension(18, 18));
		this.appliquer.setToolTipText("<html>" //$NON-NLS-1$
				+ BottomPan.getString("BottomPanel.3") //$NON-NLS-1$
				+ BottomPan.getString("BottomPanel.4") //$NON-NLS-1$
				+ BottomPan.getString("BottomPanel.5")); //$NON-NLS-1$
		this.openLibrary = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_openlibrary.png"))); //$NON-NLS-1$
		this.openLibrary.setToolTipText("<html>" //$NON-NLS-1$
				+ BottomPan.getString("BottomPanel.8") //$NON-NLS-1$
				+ BottomPan.getString("BottomPanel.9") //$NON-NLS-1$
				+ BottomPan.getString("BottomPanel.10")); //$NON-NLS-1$
		this.switchColors = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_switch.png"))); //$NON-NLS-1$
		this.switchColors.addActionListener(this);
		this.switchColors
				.setToolTipText(BottomPan.getString("BottomPanel.12")); //$NON-NLS-1$
		this.regRamp = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_enregistrer.png"))); //$NON-NLS-1$
		this.regRamp
				.setToolTipText("<html>" //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.15") //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.16")); //$NON-NLS-1$
		this.regRamp.addActionListener(this);
		this.fermer = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_arreter.png"))); //$NON-NLS-1$
		this.fermer
				.setToolTipText("<html>" //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.19") //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.20") //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.21")); //$NON-NLS-1$
		this.fermer.setPreferredSize(new Dimension(18, 18));
		this.affichLignes = new JCheckBox(BottomPan.getString("BottomPanel.22")); //$NON-NLS-1$
		this.affichLignes.setSelected(false);
		this.crc.setDisplayLines(false);
		this.affichLignes
				.setToolTipText("<html>" //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.24") //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.25") //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.26") //$NON-NLS-1$
						+ BottomPan.getString("BottomPanel.27")); //$NON-NLS-1$
		this.progress = new JProgressBar();
		// réduction des marges des icones
		Component a[] = { this.appliquer, this.openLibrary, this.regRamp,
				this.fermer };
		for (Component c : a)
			((AbstractButton) c).setMargin(new Insets(1, 1, 1, 1));

		// écouteurs
		this.statut.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2)
					setRampVisible(!fermer.isVisible());
				mw.getMenu().updateCheckBoxes();
			}
		});
		this.appliquer.addActionListener(this);
		this.openLibrary.addActionListener(this);
		this.fermer.addActionListener(this);
		this.affichLignes.addActionListener(this);

		// statut et progressBar
		final JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.add(this.statut, BorderLayout.WEST);
		pan.add(this.progress);
		
		// Dessin du GridBagLayout avec contraintes :
		this.gbl = new GridBagLayout();
		this.setLayout(gbl);
		this.setPreferredSize(new Dimension(200, 350));
		this.gbc = new GridBagConstraints();
		this.gbc.fill = GridBagConstraints.BOTH;
		Component c[] = { this.crc, this.appliquer, this.openLibrary,
				this.regRamp, this.switchColors, this.fermer,
				this.affichLignes, pan };

		for (int i = 0; i < c.length; i++) {
			this.gbc.gridx = this.x[i];
			this.gbc.gridy = this.y[i];
			this.gbc.gridwidth = this.larg[i];
			this.gbc.gridheight = this.haut[i];
			this.gbc.weightx = this.px[i];
			this.gbc.weighty = this.py[i];
			this.add(c[i], this.gbc);
		}
		this
				.setBorder(BorderFactory.createLineBorder(new Color(200, 200,
						200)));
		this.setPreferredSize(new Dimension(400, 70));
	}

	/**
	 * <p>
	 * Switch la visibilité du panneau de sélection de dégradé de couleur. Si ce
	 * composant est invisible, la hauteur du panneau est aussi réduite et il y
	 * a un gain de place.
	 * </p>
	 * 
	 * @param visible Rendre visible
	 */
	public void setRampVisible(final boolean visible) {
		Component c[] = { this.crc, this.fermer, this.appliquer, this.fermer,
				this.switchColors, this.regRamp, this.openLibrary,
				this.affichLignes};
		for (Component cc : c)
			cc.setVisible(visible);
		this.setPreferredSize(new Dimension(400, (visible) ? ((this.statut
				.isVisible()) ? 70 : 50) : (this.statut.isVisible()) ? 16 : 0));
		this.validate();
	}

	/**
	 * <p>
	 * Switch la visibilitée de la barre de statut. Si ce composant est
	 * invisible, la hauteur du panneau est réduite.
	 * </p>
	 * 
	 * @param visible Rendre visible
	 */
	public void setStatutVisible(final boolean visible) {
		this.statut.setVisible(visible);
		this.progress.setVisible(visible);
		this.setPreferredSize(new Dimension(400, (visible) ? (this.fermer
				.isVisible() ? 70 : 15) : (this.fermer.isVisible() ? 50 : 0)));
		this.validate();
	}

	/**
	 * <p>
	 * Met à jour le ChooseColorRamp dans le cas où il faut mettre à jour
	 * l'affichage des lignes et génère aussi un nouveau tableau de couleurs
	 * pour les itérations à l'avance.
	 * </p>
	 */
	public void updateCrcIterationRepaint() {
		// on vérifie si le nombre d'itération est fixé d'abord sinon normal
		if(this.mw.getToolBars().activFixIter.isSelected()){
			try{
				this.crc.setNumberOfLines(Integer.parseInt(this.mw.getToolBars().fixedIter.getText()));
			} catch(NumberFormatException e){
				this.crc.setNumberOfLines(this.mw.getFract().getIterations());
			}
		}
		else
			this.crc.setNumberOfLines(this.mw.getFract().getIterations());
		this.crc.generateLines(null);
		this.crc.repaint();
	}

	/**
	 * <p>
	 * Rendre accessible ou non les composants liés aux dagradés de couleurs. Si
	 * les composants sont désactivés, tous les boutons de gestion du dégradé
	 * dont innacessibles et le dégradé désactivé (dépends de sa réaction en
	 * désactivé).
	 * </p>
	 * 
	 * @param enable
	 */
	public void setEnableColorComponents(final boolean enable) {
		Component[] c = { this.crc, this.appliquer, this.openLibrary,
				this.regRamp };
		for (Component cc : c)
			cc.setEnabled(enable);
	}

	/**
	 * <p>
	 * Ecoute les boutons :
	 * </p>
	 * <ul>
	 * <li>Masquage du panneau de couleurs <i>(appel setRampVisible)</i></li>
	 * <li>Options sur le dégradé : Affichage lignes repaire, inverser les
	 * couleurs, ouverture da la bibliothèque de couleurs et enregistrement.</li>
	 * <li>Validation du dégradé (appel un recalcul couleur)</li>
	 * <li></li>
	 * valider, masquer/afficher lignes et masquer fenêtre.
	 * </ul>
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		// on masque le panneau des couleurs
		if (src == this.fermer)
			this.setRampVisible(!this.fermer.isVisible());
		// afficher/ faire disparaître les lignes repaires
		else if (src == this.affichLignes) {
			this.crc.setDisplayLines(this.affichLignes.isSelected());
			this.updateCrcIterationRepaint();
		}
		// on valide la couleur = actualiser
		else if (src == this.appliquer) {
			this.crc.generateLines(null);
			this.mw.launchCalculAndDisplay(true);
		} else if (src == this.openLibrary) {
			new SelectRampColorLibrary(mw, this);
		} else if (src == this.switchColors) {
			this.crc.switchColors();
		}
		// enregistrement dégradé
		else if (src == this.regRamp) {
			new SaveRamp(this.crc);
		}
	}

	// layout
	final private GridBagLayout gbl;
	final private GridBagConstraints gbc;
	final private int x[] = { 0, 15, 16, 17, 18, 19, 15, 0 };
	final private int y[] = { 0, 0, 0, 0, 0, 0, 1, 2 };
	final private int larg[] = { 15, 1, 1, 1, 1, 1, 5, 20 };
	final private int haut[] = { 2, 1, 1, 1, 1, 1, 1, 1 };
	final private int px[] = { 25, 0, 0, 0, 0, 0, 0, 1 };
	final private int py[] = { 0, 0, 0, 0, 0, 0, 0, 0 };

	// composants
	final public JLabel statut;
	final public ChooseRampColor crc;
	final private JButton appliquer, regRamp, switchColors, openLibrary,
			fermer;
	final private JCheckBox affichLignes;
	final private MainWindow mw;
	final public JProgressBar progress;
}
