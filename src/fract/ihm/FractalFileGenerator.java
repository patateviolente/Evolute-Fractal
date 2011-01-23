package fract.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import fract.BiomorphLauncher;
import fract.gen.GenFractalFile;
import fract.struct.ComplexeCartesien;

/**
 * Class qui s'occupe de générer et compiler les classes d'extension pour les
 * fractales depuis un éditeur d'équation.
 */
public class FractalFileGenerator extends JFrame implements ActionListener,
		MouseWheelListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur : dessine les composants graphiques de la fenêtre
	 * 
	 * @param mw
	 *            Objet MainWindow
	 */
	public FractalFileGenerator(final MainWindow mw) {
		// définition fenêtre
		this.mw = mw;
		this.setBounds(250, 180, 880, 650);
		this.setTitle("Editeur d'équations");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		gen = new GenFractalFile(mw);

		// composants supérieurs
		this.nom = new JTextField();
		this.nom.setPreferredSize(new Dimension(200, 20));
		this.Lnom = new JLabel("Entrez le nom de votre class : ");
		JPanel panHaut = new JPanel();
		panHaut.setLayout(new FlowLayout());
		panHaut.add(this.Lnom);
		panHaut.add(this.nom);
		this.compiler = new JButton("Valider");
		this.compiler.addActionListener(this);
		RepAlgo rep = new RepAlgo();
		rep.setPreferredSize(new Dimension(274, 480));

		// création et ajouts des onglets
		PanelSimpleForm formSimple = new PanelSimpleForm();
		this.scroll1 = new JScrollPane(formSimple);
		this.scroll1.addMouseWheelListener(this);
		onglet = new JTabbedPane();
		onglet.add("Formulaire pour Programmeurs", scroll1);
		this.getContentPane().add(onglet);

		// ajout dans le container principale
		this.add(panHaut, BorderLayout.NORTH);
		this.add(onglet);
		this.add(rep, BorderLayout.WEST);
		this.add(this.compiler, BorderLayout.SOUTH);
		this.validate();
	}

	/**
	 * Le panel de formulaire simple à base d'aires de texte, moins évolué mais
	 * pratique pour le programmeur.
	 */
	private class PanelSimpleForm extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		public PanelSimpleForm() {
			// panel supérieur choix preset
			JPanel psup = new JPanel();
			psup.setLayout(new FlowLayout());
			psup.add(new JLabel("Partir depuis le preset : "));
			String[] s = { "Mandelbrot (fractal) carré",
					"Radiolaire simple (Biomorphe) carré réccursif" };
			BoxPreset = new JComboBox(s);
			psup.add(BoxPreset);
			validPreset = new JButton("Valide");
			validPreset.addActionListener(this);
			psup.add(validPreset);

			// panel principal avec aires de texte
			Container c = new Container();
			c.setLayout(new GridLayout(6, 1));
			txtArea = new JTextArea[6];
			scrollArea = new JScrollPane[6];
			descrChamps = new JLabel[6];
			for (int i = 0; i < txtArea.length; i++) {
				JPanel pan = new JPanel();
				pan.setLayout(new BorderLayout());
				descrChamps[i] = new JLabel(descrChmpTxt[i]);
				txtArea[i] = new JTextArea();
				txtArea[i].setText(tmpChampTxt[i][preset]);
				scrollArea[i] = new JScrollPane(txtArea[i]);
				scrollArea[i].setPreferredSize(new Dimension(100, 100));
				pan.add(descrChamps[i], BorderLayout.NORTH);
				pan.add(scrollArea[i]);
				c.add(pan);
			}
			this.setLayout(new BorderLayout());
			this.add(psup, BorderLayout.NORTH);
			this.add(c);
		}

		/**
		 * Rempli les aires de texte par les constantes de texte.
		 */
		private void appliPreset() {
			for (int i = 0; i < txtArea.length; i++) {
				txtArea[i].setText(tmpChampTxt[i][preset]);
			}
		}

		/**
		 * Ecoute validation de preset
		 */
		public void actionPerformed(ActionEvent arg0) {
			this.preset = BoxPreset.getSelectedIndex();
			this.appliPreset();
			JOptionPane
					.showMessageDialog(
							this,
							"Les champs ont été réinitialisés par ceux\npar défaut du fractal sélectionné.",
							"Champs réinitialisés",
							JOptionPane.INFORMATION_MESSAGE);
		}

		int preset = 0;

	}

	/**
	 * Dessin de position dans l'algorithme.
	 */
	private class RepAlgo extends JPanel {
		public RepAlgo() {
			kit = Toolkit.getDefaultToolkit();
			Image img;
		}

		public void paintComponent(final Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			img = kit.getImage(BiomorphLauncher.class
					.getResource("/icon/interfaces/structure_algo.png"));
			g.drawImage(img, 0, 0, this);
		}

		private Toolkit kit;
		private Image img;
	}

	/**
	 * Ecoute la validation d'un des deux formulaires.
	 */
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == this.compiler) {
			// formulaire direct Java
			if (this.onglet.getSelectedIndex() == 0) {
				gen.GenSimpleForm(this);
			}
		}
	}

	/**
	 * Accélérer le scroll.
	 */
	public void mouseWheelMoved(MouseWheelEvent ev) {
		this.scroll1.getVerticalScrollBar().setValue(
				this.scroll1.getVerticalScrollBar().getValue()
						+ ((ev.getWheelRotation() > 0) ? 15 : -15));
	}

	// éléments globaux
	final private MainWindow mw;
	final private JTabbedPane onglet;
	final private JLabel Lnom;
	final public JTextField nom;
	final private JButton compiler;
	final private GenFractalFile gen;

	// éléments de la partie aire de texte
	private JComboBox BoxPreset;
	public JTextArea[] txtArea;
	private JButton validPreset;
	private JScrollPane[] scrollArea;
	private JScrollPane scroll1, scroll2;
	private JLabel[] descrChamps;
	private String[] descrChmpTxt = {
			"Constantes du fractal (éditez les [type_xxx] par le type correspondant)",
			"Entrez les variables (SOUS FORME DE TABLEAU) globales à la classe de calcul",
			"Entrez la procédure d'initialisation des tableau de variables",
			"Entrez la procédure à faire avant d'entrez dans la boucle d'itération (facultatif)",
			"Entrez la procédure à faire pendant la partie itération",
			"Entrez la condition d'arrêt (en cas de réussite = couleur)" };
	private String[][] tmpChampTxt = {
			{
					"final static double CposX = -1.8, CposY = -1.24, CscaleMin = 0.012, \n\tCscaleMax = 0.000001;\nfinal static int Citerations = 45;",
					"final static double CposX = -1.8, CposY = -1.24, CscaleMin = 0.012, \n\tCscaleMax = 0.000001;\nfinal static int Citerations = 15;" },
			{
					"private double[] z1Tmp, z2Tmp, z1, z2, x, y;\nprivate Complex[] z;",
					"protected ComplexeCartesien z[][];" },
			{
					"z1Tmp = new double[n];\nz2Tmp = new double[n];\nx = new double[n];\ny = new double[n];\nz1 = new double[n];\nz2 = new double[n];\nz = new Complex[n];\n\nfor(int i=0; i<n; i++)\nz[i] = new Complex(0d, 0d);",
					"z = new ComplexeCartesien[n][];\nfor (int i = 0; i < n; i++)\nz[i] = new ComplexeCartesien[iterations];" },
			{
					"this.z1[t] = this.z2[t] = 0;\nthis.x[t] = x;\nthis.y[t] = y;\n\nz[t] = new Complex(0d, 0d);",
					"z[t][0] = new ComplexeCartesien(posX + scale * x, posY + scale * y);" },
			{
					"z[t] = z[t].puissance((int)(2));\nz1[t]=z[t].re()+(posX + this.x[t] * scale);\nz2[t]=z[t].im()+(posY + this.y[t] * scale);\nz[t]= new Complex(z1[t], z2[t]);\n",
					"z[t][i] = z[t][i - 1].carre();\nz[t][i].setRe(z[t][i].getRe() + 10);	// 10 = CONSTANTE" },
			{
					"return (!((z1[t]*z1[t] + z2[t]*z2[t]) < 4 && i < iterations));",
					"return (z[t][i].isReImgOrModUpTo(this.iterations) && z[t][i].isReOrImgDownTo(this.iterations));" } };

	// éléments du formulaire complexe
}
