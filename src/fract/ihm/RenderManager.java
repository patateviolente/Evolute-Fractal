package fract.ihm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fract.BiomorphLauncher;
import fract.lang.RendManager;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * <p>
 * Classe graphique pour répartir le taux de calcul sur les différentes
 * machines. Permet d'ajouter/ supprimer/ enregistrer les machines.
 * </p>
 */
final public class RenderManager extends JFrame implements ChangeListener,
		ActionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Dessine la fenêtre par défaut. Ajoue le bloc spécial par défaut
	 * représentant la machine locale avec bouton ajout et CheckBox pour activer
	 * rendu réseau.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public RenderManager(final MainWindow mw) {
		// params fenêtre
		this.mw = mw;
		this.setBounds((int) (mw.getLocationOnScreen().getX() + mw.getSize()
				.getWidth()), (int) mw.getLocationOnScreen().getY(), 210, 600);
		this.setTitle(RendManager.getString("RendManager.0")); //$NON-NLS-1$
		this.setResizable(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				setVisible(false);
				mw.getMenu().updateCheckBoxes();
			}
		});
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif"))); //$NON-NLS-1$

		// composants principaux
		this.map = new TreeMap<String, StructRenderValues>();
		this.activNetwork = new JCheckBox(RendManager
				.getString("RendManager.2"), true); //$NON-NLS-1$
		this.save = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/interface_enregistrer.png"))); //$NON-NLS-1$
		this.save.addActionListener(this);
		this.save.setToolTipText(RendManager.getString("RendManager.4")); //$NON-NLS-1$
		this.stop = new JButton(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/menu/quit.png"))); //$NON-NLS-1$
		this.stop.addActionListener(this);
		this.stop.setToolTipText(RendManager.getString("RendManager.6")); //$NON-NLS-1$
		this.mainCapacity = new JSlider(JSlider.HORIZONTAL);
		this.mainCapacity.setMinimum(0);
		this.mainCapacity.addChangeListener(this);
		this.mainCapacity.setPreferredSize(new Dimension(160,
				(int) this.mainCapacity.getPreferredSize().getHeight()));
		this.totalMachines = new JLabel();
		this.updateLabelNbMachines();
		this.add = new JButton(RendManager.getString("RendManager.7")); //$NON-NLS-1$
		this.add.addActionListener(this);

		// organisation panel propriétaire
		JPanel owner = new JPanel();
		owner.setPreferredSize(new Dimension(180, 120));
		owner.setLayout(new FlowLayout());
		owner.add(this.mainCapacity);
		owner.add(this.add);
		owner.setBorder(BorderFactory.createTitledBorder(RendManager
				.getString("RendManager.8"))); //$NON-NLS-1$

		// organisation panel principal
		this.main = new JPanel();
		this.main.setLayout(new FlowLayout());
		this.main.add(owner);

		// panels, scoll, container
		this.mainPan = new JPanel();
		this.mainPan.setPreferredSize(new Dimension(180, 300));
		this.mainPan.setLayout(new BorderLayout());
		Box b = Box.createHorizontalBox();
		b.add(this.activNetwork);
		b.add(this.save);
		b.add(this.stop);
		this.mainPan.add(b, BorderLayout.NORTH);
		this.mainPan.add(this.totalMachines, BorderLayout.SOUTH);
		this.mainPan.add(main);
		this.mainPan.validate();
		this.scroll = new JScrollPane(this.mainPan);
		this.scroll.addMouseWheelListener(this);
		this.getContentPane().add(this.scroll);
		this.validate();
		this.loadRenderers();
	}

	/**
	 * <p>
	 * Ajoute une nouvelle machine. Cette fonction ne met pas à jour le panel
	 * graphique, pour celà appelez la fonction updatePanel().
	 * </p>
	 * 
	 * @param surnom
	 *            Surnom de la machine
	 * @param addr
	 *            Adresse de la machine
	 * @param tache
	 *            Nombre de processus à lancer
	 */
	private void addValue(final String surnom, final String addr,
			final int tache) {
		// création nouveau tableau plus grands
		JPanel pan = new JPanel();
		map
				.put(
						surnom,
						new StructRenderValues(
								this,
								pan,
								surnom,
								new JSlider(JSlider.HORIZONTAL, tache),
								new JTextField(addr),
								new JButton(RendManager
										.getString("RendManager.9")), new JButton(RendManager.getString("RendManager.10")))); //$NON-NLS-1$ //$NON-NLS-2$
		this.main.add(pan);
		this.updateSliders(null);
		pan.validate();
		this.updateLabelNbMachines();
	}

	/**
	 * <p>
	 * Charge le plus récent préset existant en lisant le fichier
	 * .distantRenderersList et en écrasant la map de d'adresse des ordinateurs
	 * distants.
	 * </p>
	 */
	private void loadRenderers() {
		String s = BiomorphLauncher.class.getResource("/compil.sh").toString(); //$NON-NLS-1$
		s = s.substring(5, s.length() - 9);
		try {
			try {
				// réinit
				this.toutSupprimer();
				this.updatePanel();

				// chargement
				File f = new File(s + ".distantRenderersList"); //$NON-NLS-1$
				StructSaveOption struct;
				ois = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(f)));
				do {
					struct = (StructSaveOption) ois.readObject();
					this.addValue(struct.nom, struct.ssh, struct.valSlider);

				} while (struct.nom != null);

				ois.close();
			} catch (FileNotFoundException e) {
				return; // pas grave, rien à charger c'est tout
			} catch (EOFException e) {
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.updateSliders(null);
		this.updatePanel();
	}

	/**
	 * <p>
	 * Ecrit le fichier .distantRenderersList en racine contenant l'objet map
	 * (TreeMap) contenant tous les ordinateurs distants.
	 * </p>
	 */
	private void saveRenderers() {
		String s = BiomorphLauncher.class.getResource("/compil.sh").toString(); //$NON-NLS-1$
		s = s.substring(5, s.length() - 9);

		try {
			// on écrit tous les objets un par un
			oos = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(new File(s
							+ ".distantRenderersList")))); //$NON-NLS-1$
			try {
				this.it = this.map.keySet().iterator();
				while (this.it.hasNext()) {
					String clef = it.next();
					StructRenderValues struct = this.map.get(clef);
					oos.writeObject(new StructSaveOption(clef, struct
							.getTextField().getText(), struct.getSlider()
							.getValue()));
				}

				oos.close();
			} catch (NotSerializableException e) {
				BiomorphLauncher.writeError(RendManager
						.getString("RendManager.15") //$NON-NLS-1$
						+ ", " + RendManager.getString("RendManager.17")); //$NON-NLS-1$ //$NON-NLS-2$
				e.printStackTrace();
				return;
			} catch (NullPointerException e) {
				BiomorphLauncher.writeError(RendManager
						.getString("RendManager.18")); //$NON-NLS-1$
				return;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BiomorphLauncher
				.writeSubAction(RendManager.getString("RendManager.19")); //$NON-NLS-1$
	}

	/**
	 * <p>
	 * Formulaire graphique pour ajouter une nouvelle machine ; demande un
	 * surnom et l'adresse.
	 * </p>
	 */
	final private class FormNewMachine extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;

		public FormNewMachine() {
			// fenêtre
			this.setAlwaysOnTop(true);
			this.setBounds(200, 180, 400, 200);
			this.setTitle(RendManager.getString("RendManager.20")); //$NON-NLS-1$
			this.setVisible(true);
			this.setMinimumSize(new Dimension(300, 180));

			// composants
			this.Lnom = new JLabel(RendManager.getString("RendManager.21")); //$NON-NLS-1$
			this.Lnom.setToolTipText("<html>" //$NON-NLS-1$
					+ RendManager.getString("RendManager.23")); //$NON-NLS-1$
			this.Luser = new JLabel(RendManager.getString("RendManager.24")); //$NON-NLS-1$
			this.user = new JTextField(RendManager.getString("RendManager.1")); //$NON-NLS-1$
			this.Ladresse = new JLabel(RendManager.getString("RendManager.26")); //$NON-NLS-1$
			this.Ladresse.setToolTipText("<html>" //$NON-NLS-1$
					+ RendManager.getString("RendManager.28")); //$NON-NLS-1$
			this.Ldossier = new JLabel(RendManager.getString("RendManager.29")); //$NON-NLS-1$
			this.dossier = new JTextField("/usr/FractalGen/bin"); //$NON-NLS-1$
			this.nom = new JTextField(RendManager.getString("RendManager.31")); //$NON-NLS-1$
			this.nom.setText(RendManager.getString("RendManager.32")); //$NON-NLS-1$
			this.adresse = new JTextField(RendManager
					.getString("RendManager.33")); //$NON-NLS-1$
			this.Lport = new JLabel(RendManager.getString("RendManager.34")); //$NON-NLS-1$
			this.port = new JTextField(RendManager.getString("RendManager.35")); //$NON-NLS-1$
			this.valid = new JButton(RendManager.getString("RendManager.36")); //$NON-NLS-1$
			this.valid.addActionListener(this);
			this.autodetect = new JButton(RendManager
					.getString("RendManager.37")); //$NON-NLS-1$
			this.autodetect.addActionListener(this);

			// bouton autodetect
			JPanel pan = new JPanel();
			pan.setLayout(new BorderLayout());
			pan.add(this.dossier);
			pan.add(this.autodetect, BorderLayout.EAST);

			JPanel form = new JPanel();
			form.setLayout(new GridLayout(5, 2));
			Component[] c = { Lnom, nom, Luser, user, Ladresse, adresse, Lport,
					port, Ldossier, pan };
			for (Component cc : c)
				form.add(cc);

			this.getContentPane().add(form);
			this.getContentPane().add(this.valid, BorderLayout.SOUTH);
		}

		/**
		 * <p>
		 * Ecoute le bouton de validation ou de recherche du dossier du
		 * programme distant.
		 * </p>
		 */
		public void actionPerformed(ActionEvent ev) {
			String rep;
			// détection automatique
			if (ev.getSource() == this.autodetect) {
				rep = pingDistantMachine(this.port.getText() + " " //$NON-NLS-1$
						+ this.user.getText(), this.adresse.getText(), "", 10); //$NON-NLS-1$
				if (rep != null && rep.length() != 0) {
					JOptionPane
							.showMessageDialog(
									new JPanel(),
									RendManager.getString("RendManager.40") + rep + "\"", //$NON-NLS-1$ //$NON-NLS-2$
									RendManager.getString("RendManager.42"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
					this.dossier.setText(rep);
				} else {
					JOptionPane
							.showMessageDialog(
									new JPanel(),
									RendManager.getString("RendManager.43"), //$NON-NLS-1$
									RendManager.getString("RendManager.44"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
			}
			if (ev.getSource() == this.valid) {
				// ajout / final obligatoire auto
				if (!(!this.dossier.getText().endsWith("/") || !this.dossier //$NON-NLS-1$
						.getText().endsWith("/ "))) //$NON-NLS-1$
					this.dossier.setText(this.dossier.getText() + "/"); //$NON-NLS-1$

				// nom déjà existant
				if (map.containsKey(this.nom.getText())) {
					JOptionPane
							.showMessageDialog(
									new JPanel(),
									RendManager.getString("RendManager.48"), //$NON-NLS-1$
									RendManager.getString("RendManager.49"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}

				// sinon on regarde la réponse
				rep = pingDistantMachine(this.port.getText() + " " //$NON-NLS-1$
						+ this.user.getText(), this.adresse.getText(),
						this.dossier.getText(), 10);

				if (rep != null && rep.length() != 0) {
					boolean ping = false;
					if (rep.contains(RendManager.getString("RendManager.51"))) //$NON-NLS-1$
						ping = true;
					// message d'information
					JOptionPane.showMessageDialog(new JPanel(), RendManager
							.getString("RendManager.52") //$NON-NLS-1$
							+ rep
							+ "\"" //$NON-NLS-1$
							+ ((!ping) ? RendManager
									.getString("RendManager.54") //$NON-NLS-1$
									: RendManager.getString("RendManager.55")), //$NON-NLS-1$
							RendManager.getString("RendManager.56"), //$NON-NLS-1$
							(ping) ? JOptionPane.INFORMATION_MESSAGE
									: JOptionPane.ERROR_MESSAGE);
					// ajout
					if (ping) {
						addValue(this.nom.getText(), this.port.getText() + " " //$NON-NLS-1$
								+ this.user.getText() + " " //$NON-NLS-1$
								+ this.adresse.getText() + " " //$NON-NLS-1$
								+ this.dossier.getText(), 0);
						updatePanel();
						this.dispose();
					}
				} else {
					JOptionPane
							.showMessageDialog(
									new JPanel(),
									RendManager.getString("RendManager.60") //$NON-NLS-1$
											+ RendManager
													.getString("RendManager.61"), //$NON-NLS-1$
									RendManager.getString("RendManager.62"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
			}

		}

		final private JTextField nom, user, adresse, port, dossier;
		final private JLabel Lnom, Luser, Ladresse, Lport, Ldossier;
		final private JButton autodetect, valid;
	}

	/**
	 * <p>
	 * Met à jour le maximum de chaques sliders et fait un équilibrage des
	 * données.
	 * </p>
	 * 
	 * @param js
	 *            La référence du JSlider à ne pas toucher (mettre null sinon)
	 */
	public void updateSliders(JSlider js) {
		JSlider s;
		int total = 0;
		int nbThreads = this.mw.nbThread;
		// réglage mainCapacity
		this.mainCapacity.setMaximum(nbThreads);
		this.mainCapacity.setPaintTicks(true);
		this.mainCapacity.setMajorTickSpacing(nbThreads / 4 + 1);
		this.mainCapacity.setMinorTickSpacing(nbThreads / 4 + 1);

		// s'il n'y a aucune machine réseau on met tout en local
		if (this.map.size() == 0)
			this.mainCapacity.setValue(nbThreads);
		total += this.mainCapacity.getValue();

		// autres sliders
		this.it = this.map.keySet().iterator();
		while (it.hasNext()) {
			s = this.map.get(it.next()).getSlider();
			s.setMaximum(nbThreads);
			this.mainCapacity.setPaintTicks(true);
			s.setMajorTickSpacing(nbThreads / 4 + 1);
			s.setMinorTickSpacing(nbThreads / 4 + 1);
			total += s.getValue();
		}
		total -= nbThreads;

		// si il n'y a qu'un slider on le désactive
		this.mainCapacity.setEnabled(this.map.size() != 0);

		// adaptation
		int extrem; // plus grand ou plus petit slider
		int[] tab;
		JSlider slide;
		while (total != 0) {
			// on trouve le plus petit ou plus grand
			extrem = -1;
			tab = this.getArrayNbTasks();
			if (total > 0) { // c'est qu'il y a plus de threads que demandés
				for (int i = 0; i < tab.length; i++) {
					if (extrem != -1 && tab[extrem] < tab[i]
							&& this.getRenderVAlueAtOffset(i).getSlider() != js)
						extrem = i;
					else if (this.getRenderVAlueAtOffset(i).getSlider() != js)
						extrem = i;
				}
				if (extrem == -1)
					extrem = 0;
				slide = this.getRenderVAlueAtOffset(extrem).getSlider();
				slide.setValue(slide.getValue() - 1);
				total--;

			} else { // c'est qu'il y a un manque dans la répartition
				for (int i = 0; i < tab.length; i++) {
					if (extrem != -1 && tab[extrem] > tab[i]
							&& this.getRenderVAlueAtOffset(i).getSlider() != js)
						extrem = i;
					else if (this.getRenderVAlueAtOffset(i).getSlider() != js)
						extrem = i;
				}
				if (extrem == -1)
					extrem = 0;
				slide = this.getRenderVAlueAtOffset(extrem).getSlider();
				slide.setValue(slide.getValue() + 1);
				total++;

			}
		}

		// on regarde si le total des sliders na pas débordé sur le JS principal
		/*
		 * int nb[] = this.getArrayNbTasks(); total = 0; for (int i : nb) total
		 * += i; int reste = nbThreads - total;
		 * this.mainCapacity.setValue(reste);
		 */
	}

	/**
	 * <p>
	 * Envoie une requète de test à la machine distante indiquée. Si la machine
	 * met plus de n secondes à répondre alors la réponse sera fausse.
	 * </p>
	 * 
	 * @param user
	 *            Utilisateur
	 * @param addr
	 *            Adresse IP
	 * @param folder
	 *            Dossier absolu vers bin/
	 * @param sec
	 *            Timeout
	 * @return réponse
	 */
	private String pingDistantMachine(final String user, final String addr,
			final String folder, int sec) {
		sec *= 1000; // passage en millisecondes
		String rep = ""; //$NON-NLS-1$
		Process p;

		try {
			String s = BiomorphLauncher.class.getResource("/NetworkCall.sh") //$NON-NLS-1$
					.toString();
			s = s.substring(5, s.length()) + " " + user + " " + addr + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ folder;
			BiomorphLauncher.writeSubAction(RendManager
					.getString("RendManager.68") + s); //$NON-NLS-1$
			if (sec > 0) {
				Thread t = new EndProcess(p = Runtime.getRuntime().exec(s), sec);
				t.start();
			} else
				p = Runtime.getRuntime().exec(s);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			while (!procDone(p)) {
				while ((s = stdInput.readLine()) != null) {
					rep += s + "\n"; //$NON-NLS-1$
				}
			}
		} catch (IOException e1) {
			System.err.println(RendManager.getString("RendManager.70")); //$NON-NLS-1$
			return null;
		}

		return rep;
	}

	/**
	 * Compteur qui prends en argument le processus à arrêter et le temps limite
	 * de validité.
	 */
	class EndProcess extends Thread {
		/**
		 * @param p
		 *            Le processus à limiter en temps
		 * @param timeout
		 *            Le temps maximal autorisé
		 */
		public EndProcess(Process p, int timeout) {
			this.p = p;
			this.timeout = timeout;
		}

		/**
		 * Attend le timeout et détruit le processus.
		 */
		public void run() {
			int t = 0;
			while (true) {
				try {
					Thread.sleep(this.inter);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t += this.inter;
				if (this.cancel)
					return;
				else if (t > this.timeout) {
					BiomorphLauncher.writeSubAction(RendManager
							.getString("RendManager.71") //$NON-NLS-1$
							+ (timeout / 1000)
							+ RendManager.getString("RendManager.72")); //$NON-NLS-1$
					p.destroy();
					// si ssh (ps enfant) on supprime le dernier ps ssh
					stopSshProcess();
					return;
				}
			}
		}

		/**
		 * Annule la procédure de décompte.
		 */
		public void annuler() {
			this.cancel = true;
		}

		// variables
		Process p;
		int timeout;
		private boolean cancel = false;
		final private int inter = 500;
	}

	/**
	 * Lance le script NetworkCancelSsh.sh qui arrête le dernier processus ssh.
	 */
	private void stopSshProcess() {
		try {
			String s = BiomorphLauncher.class.getResource(
					"/NetworkCancelSsh.sh").toString(); //$NON-NLS-1$
			s = s.substring(5, s.length());
			Runtime.getRuntime().exec(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Retourne un tableau de String des différents arguments désignant les
	 * machine du type "utilisateur adresseIP dossier".
	 * </p>
	 * 
	 * @return tableau d'arguments machines distantes.
	 */
	public String[] getArrayComputers() {
		if (!this.activNetwork.isSelected())
			return new String[0];
		String[] tab = new String[this.map.size()];
		it = this.map.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			tab[i++] = this.map.get(it.next()).getTextField().getText();
		}
		return tab;
	}

	/**
	 * <p>
	 * Retourne un tableau de nombres de zones que chaques ordinateurs doivent
	 * respectivement faire selon la fonction getArrayComputers (qui donne
	 * seulement les infos nécessaires à la connexion).
	 * </p>
	 * <p>
	 * Le tableau est du nombre de machines ajoutés. S'il n'y a aucune machine
	 * la longueur du tableau est 0, etc. S'il y a N zones à calculer à distance
	 * et U machines sont désignés pour faire chacun L taches alors le reste (N
	 * - U*L) sera donné à l'ordinateur courrant.
	 * </p>
	 * 
	 * @return tableau d'arguments machines distantes.
	 */
	public int[] getArrayNbTasks() {
		if (!this.activNetwork.isSelected())
			return new int[0];
		int[] tab = new int[this.map.size()];
		it = this.map.keySet().iterator();
		int i = 0;
		while (it.hasNext())
			tab[i++] = this.map.get(it.next()).getSlider().getValue();
		return tab;
	}

	/**
	 * <p>
	 * Retourne un tableau de mot de passe correspondant respectivement à
	 * chaques lignes de connection.
	 * </p>
	 * 
	 * @return tableau d'arguments machines distantes.
	 */
	public String[] getArrayMdp() {
		if (!this.activNetwork.isSelected())
			return new String[0];
		String[] tab = new String[this.map.size()];
		it = this.map.keySet().iterator();
		int i = 0;
		while (it.hasNext())
			tab[i++] = this.map.get(it.next()).getPswField().getText();
		return tab;
	}

	/**
	 * Retourne vrai si le processus est terminé.
	 * 
	 * @param p
	 *            Objet Process
	 * @return Vrai si processus terminé.
	 */
	private static boolean procDone(final Process p) {
		try {
			p.exitValue();
			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

	/**
	 * <p>
	 * Met à jour les composants graphiques de tous les panel des machines et
	 * actualise le panel principal contenant ces panneaux.
	 * </p>
	 */
	private void updatePanel() {
		for (int i = 0; i < this.map.size(); i++)
			this.getRenderVAlueAtOffset(i).getPanel().validate();
		this.mainPan.validate();

		int hauteur = this.mainCapacity.getHeight() + 100;
		for (int i = 0; i < this.map.size(); i++)
			if (this.getRenderVAlueAtOffset(i).getPanel() != null)
				hauteur += this.getRenderVAlueAtOffset(i).getPanel()
						.getHeight() + 30;
		this.mainPan.setPreferredSize(new Dimension(180, hauteur));
		this.main.repaint();
		this.scroll.validate();
		this.scroll.repaint();
	}

	/**
	 * Ecoute boutons (nouveau, ...)
	 */
	public void actionPerformed(final ActionEvent ev) {
		Object src = ev.getSource();
		// ajout d'une machine
		if (src == this.add) {
			new FormNewMachine();
		} else if (src == this.save) {
			this.saveRenderers();
		} else if (src == this.stop) {
			this.stopSshProcess();
		} else {
			for (int i = 0; i < this.map.size(); i++) {
				if (src == this.getRenderVAlueAtOffset(i).getButtonSuppr()) {
					this.supprimerMachine(i);
					return;
				}
			}
		}
	}

	/**
	 * Écoute les changements de valeur des JSliders
	 */
	public void stateChanged(final ChangeEvent ev) {
		this.updateSliders((((JSlider) ev.getSource())));
	}

	/**
	 * <p>
	 * Retourne un objet de stockage au rang donnée. L'outil de stockage étant
	 * un TreeMap, les éléments sont ordonnées dans l'ordre où ils ont été
	 * ajoutés.
	 * </p>
	 * 
	 * @param offset
	 * @return
	 */
	private StructRenderValues getRenderVAlueAtOffset(final int offset) {
		int i = 0;
		String s;
		this.it = map.keySet().iterator();
		while (it.hasNext()) {
			s = it.next();
			if (i++ == offset) {
				return this.map.get(s);
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Supprime une machine distante de la map.
	 * </p>
	 * 
	 * @param rang
	 */
	private void supprimerMachine(int rang) {
		int i = 0;
		this.it = map.keySet().iterator();
		while (it.hasNext()) {
			String clef = it.next();
			if (i++ == rang) {
				StructRenderValues ssv = this.map.get(clef);
				this.main.remove(ssv.getPanel());
				this.map.remove(clef);
				BiomorphLauncher.writeSubAction(RendManager
						.getString("RendManager.74") //$NON-NLS-1$
						+ ssv.getTextField().getText());

				this.updateSliders(null);
				this.updatePanel();
				return;
			}
		}
		this.updateLabelNbMachines();
	}

	/**
	 * Met à jour le label information nombre de machines.
	 */
	private void updateLabelNbMachines() {
		this.nbActiv = this.map.size();
		this.totalMachines
				.setText(RendManager.getString("RendManager.75") + this.nbActiv); //$NON-NLS-1$
	}

	/**
	 * <p>
	 * Vide tous les ordinateurs distants enregistrés
	 * </p>
	 */
	private void toutSupprimer() {
		this.it = map.keySet().iterator();

		while (it.hasNext()) {
			String clef = it.next();
			StructRenderValues ssv = this.map.get(clef);
			this.mainPan.remove(ssv.getPanel());
			this.map.remove(clef);
			this.updatePanel();
		}
		this.updateSliders(null);
	}

	/**
	 * <p>
	 * Accelère le scroll pour un défilement plus rapide.
	 * </p>
	 */
	public void mouseWheelMoved(MouseWheelEvent event) {
		final JScrollBar scrollBar = scroll.getVerticalScrollBar();
		final int rotation = event.getWheelRotation();
		if (scrollBar != null) {
			scrollBar.setValue(scrollBar.getValue()
					+ (scrollBar.getBlockIncrement(rotation) * rotation / 10));
		}
	}

	/**
	 * <p>
	 * Stockage des données individuelles d'une machine de rendu réseau avec
	 * nom, valeur, adresse, activation...
	 * </p>
	 */
	class StructRenderValues implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * @param rm
		 * @param pan
		 * @param nom
		 * @param StacheMachine
		 * @param TsshMachine
		 * @param BdelMachine
		 * @param Bactu
		 */
		public StructRenderValues(RenderManager rm, JPanel pan, String nom,
				JSlider StacheMachine, JTextField TsshMachine,
				JButton BdelMachine, JButton Bactu) {
			// création composants
			this.extensions = pan;
			this.tacheMachine = StacheMachine;
			this.tacheMachine.addChangeListener(rm);
			this.tacheMachine.setPreferredSize(new Dimension(160,
					(int) rm.mainCapacity.getPreferredSize().getHeight() + 20));
			this.sshMachine = TsshMachine;
			this.sshMachine.setPreferredSize(new Dimension(170, 22));
			this.Lmdp = new JLabel(RendManager.getString("RendManager.76")); //$NON-NLS-1$
			this.mdp = new JPasswordField();
			this.mdp.setToolTipText("<html>" //$NON-NLS-1$
					+ RendManager.getString("RendManager.78") //$NON-NLS-1$
					+ RendManager.getString("RendManager.79") //$NON-NLS-1$
					+ RendManager.getString("RendManager.80") //$NON-NLS-1$
					+ RendManager.getString("RendManager.81") //$NON-NLS-1$
					+ RendManager.getString("RendManager.82") //$NON-NLS-1$
					+ RendManager.getString("RendManager.83") //$NON-NLS-1$
					+ RendManager.getString("RendManager.84")); //$NON-NLS-1$
			this.mdp.setPreferredSize(new Dimension(90, 22));
			this.delMachine = BdelMachine;
			this.delMachine.addActionListener(rm);
			this.actualizeMachine = Bactu;
			this.actualizeMachine.addActionListener(rm);

			// ajout composants
			pan.setPreferredSize(new Dimension(180, 195));
			pan.setBackground(colorOk);
			pan.setLayout(new FlowLayout());
			pan.setBorder(BorderFactory.createTitledBorder(nom));
			pan.add(this.sshMachine);
			pan.add(this.Lmdp);
			pan.add(this.mdp);
			pan.add(this.tacheMachine);
			pan.add(this.delMachine);
			pan.add(this.actualizeMachine);
		}

		public JPanel getPanel() {
			return this.extensions;
		}

		public JSlider getSlider() {
			return this.tacheMachine;
		}

		public JTextField getTextField() {
			return this.sshMachine;
		}

		public JTextField getPswField() {
			return this.mdp;
		}

		public JButton getButtonSuppr() {
			return this.delMachine;
		}

		private JPanel extensions;
		private JSlider tacheMachine;
		private JTextField sshMachine;
		private JLabel Lmdp;
		private JPasswordField mdp;
		private JButton delMachine, actualizeMachine;
	}

	// composants par défaut
	final private JPanel main;
	final private JScrollPane scroll;
	final private JPanel mainPan;
	final private MainWindow mw;
	final private JLabel totalMachines;
	final private JSlider mainCapacity;
	final private JButton add;
	private int nbActiv = 1;
	final private Color colorOk = new Color(175, 201, 180);
	final private Color colorNOk = new Color(201, 178, 175);
	final private JCheckBox activNetwork;
	final private JButton save, stop;

	// composants d'extensions
	private transient TreeMap<String, StructRenderValues> map;
	private Iterator<String> it;

	// composants sauvegarde
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
}

/**
 * Classe de stockage simple faîte pour être enregistrée dans un fichier en tant
 * qu'objet sérialisable.
 */
class StructSaveOption implements Serializable {
	private static final long serialVersionUID = 1L;

	public StructSaveOption(String nom, String ssh, int valSlider) {
		this.nom = nom;
		this.ssh = ssh;
		this.valSlider = valSlider;
	}

	public String nom, ssh;
	public int valSlider;
}
