package fract.ihm;

import javax.swing.*;

import fract.BiomorphLauncher;
import fract.opt.EnumOption.IntEnum;

import java.awt.*;
import java.awt.event.*;

/**
 * Fenêtre popup au lancement qui donne des conseils écrit dans le programme et
 * permet d'y revenir à chaque lancement au même endroit. Désactivable par
 * l'option IntEnum.infos
 */
public class DidYouKnow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit la fenêtre graphiquement.
	 */
	public DidYouKnow() {
		// fenêtre
		this.setVisible(true);
		this.setBounds(300, 200, 350, 220);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BiomorphLauncher.class
						.getResource("/icon/interfaces/icone.gif"))); //$NON-NLS-1$
		
		// composants
		this.n = BiomorphLauncher.tOpt.getIntValue(IntEnum.infos);
		JPanel pan = new JPanel();
		this.prec = new JButton("Précédent");
		this.prec.addActionListener(this);
		this.suiv = new JButton("Suivant");
		this.suiv.addActionListener(this);
		this.desactiv = new JCheckBox("Ne plus afficher au lancement");
		this.desactiv.addActionListener(this);
		this.text = new JLabel("chargement...");
		this.updateText();

		// ajout interne
		Box b = Box.createHorizontalBox();
		b.add(this.prec);
		b.add(this.suiv);
		pan.setLayout(new BorderLayout());
		pan.add(this.text);
		pan.add(b, BorderLayout.SOUTH);

		// image
		JLabel img = new JLabel(new ImageIcon(BiomorphLauncher.class
				.getResource("/icon/interfaces/didyouknow.png")));

		// ajouts
		this.updateTitle();
		this.add(this.desactiv, BorderLayout.SOUTH);
		this.add(img, BorderLayout.EAST);
		this.add(pan);
		this.repaint();
		this.validate();
	}

	/**
	 * Adapte le titre à l'information en cours. Donne un titre du type
	 * "Information X sur X"
	 */
	private void updateTitle() {
		this.setTitle("Informations " + this.n + " sur " + (this.s.length));
	}

	/**
	 * Adapte le texte au numéro en cours.
	 */
	private void updateText() {
		// vérif n pas trop grand ni petit
		if (this.n <= 0)
			this.n = 1;
		if (this.n > s.length + 1)
			this.n = 1;

		// ajout texte
		this.text.setText("<html>" + this.s[this.n - 1] + "</html>");

		// modification boutons
		this.prec.setEnabled((this.n >= 2));
		this.suiv.setEnabled((this.n < this.s.length));
		
		// dernière info
		if(this.n >= this.s.length){
			this.desactiv.setSelected(true);
			BiomorphLauncher.gOpt.changeOption("infos", ((this.desactiv
					.isSelected()) ? -1 : this.n));
		}
	}

	/**
	 * Ecoute les boutons suivant/ précédent et la case désactivation.
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		if (src == this.desactiv) {
			BiomorphLauncher.gOpt.changeOption("infos", ((this.desactiv
					.isSelected()) ? -1 : this.n));
			BiomorphLauncher.tOpt.modifierOption("infos", ((this.desactiv
					.isSelected()) ? -1 : this.n));
		} else {
			this.n += (src == this.suiv) ? 1 : -1;
			if (!this.desactiv.isSelected()) // sinon on reste à -1
				BiomorphLauncher.gOpt.changeOption("infos", this.n);
			this.updateText();
			this.updateTitle();
		}
	}

	// texte et composants
	private int n;
	private final JButton prec, suiv;
	private final JCheckBox desactiv;
	private final JLabel text;
	private final String[] s = {
			"<b>Bienvenue</b> dans Evolute Fractal !<br />Ce logiciel permet de représenter des fractales <i>(ou biomorphes)</i> et d'en faire des rendu.<br />Cette fenêtre est là pour vous aider à commencer rapidement.",
			"<b>[interface]</b> Pour <b>naviguer rapidement</b> dans un fractal vous pouver utiliser<br /> - </b>ESPACE + souris pour vous déplacer<br /> - <b>CTRL</b> + roulette pour zoomer<br /> - <b>SHIFT</b> + souris pour la rotation<br /> - Dessiner une zone de zoom (souris)<br />Il est aussi possible d'utiliser les curseurs de la barre d'icones <i>(invisible par défaut)</i>",
			"<b>[interface]</b> <b>L'interface</b> est personnaliable<br /> - Vous pouvez gérer la visibilité des composants par le <b>menu Affichage</b><br /> - Mais la <b>section affichage des préférences <i>(CTRL+P)</i> offre un résultat résident dans les options</b>.",
			"<b>[interface]</b> Utilisez les <b>raccourcis clavier pour agrandir la surface de travail</b> avec <b>SHIFT + AZERTY</b> (et M), un double clic sur ls panneau latéral droit et bas les feront aussi disparaîtres.",
			"<b>[rendu]</b> Faire un <b>rendu d'une composition de taille défini au pixel prêt ?</b><br />La taille du panneau est réglable depuis le panneau <b>Edition/ taille de la composition</b>. Vous avez aussi un mode plein écran juste en dessous <i>(ECHAP pour sortir)</i>.",
			"<b>[rendu]</b> Le <b>nombre de threads</b> (processus) est le nombre d'endroits calculés simultanément, il est réglable dans le panneau de <b>préférence/ moteur de rendu</b>.<br />N'oubliez pas d'augmenter cette valeur en cas de rendu réseau !",
			"<b>[export]</b> <b>Enregistrez vos fractales !</b><br />Allez dans <b>Fichier/ exporter vue</b>, les coordonnées de la fractale et ses couleurs seront enregistrées et chargeable depuis le menu fichier.",
			"<b>[export]</b> Réutilisez vos <b>couleurs</b> !<br />Un simple clic sur la disquette de la barre de couleurs et votre dégradé sera enregistré et accessible depuis la <b>bibliothèque de couleurs.</b>",
			"<b>[export]</b> <b>Partagez vos fractales sur internet !</b><br />Un simple copier/ coller de l'instruction dans <b>Fichier/ paratager vue</b> et d'autres possésseurs pourront revenir sur la vue enregistrée avec les même couleurs !<br />Vous pouvez aller sur le site officiel du projet <b>www.evolute-fractale.c.la</b>",
			"<b>[rendu]</b> Un <b>rendu multimachine</b> est possible pour les possésseurs UNIX <b>sur des machine ayant un serveur ssh d'installé</b>.<br />Il vous suffit d'entrer leur ip et le dossier bin/ absolu du logiciel.",
			"<b>[rendu]</b> La <b>fonction autodetect du rendu réseau</b> pour trouver le dossier lance en fait une fonction <b>locate</b>, elle <b>ne fonctionnera pas si votre copie du logiciel est récente</b> sauf si vous faites un <b>updatedb</b> en root pour mettre à jour la base des fichiers.",
			"<b>[rendu]</b> Un <b>rendu multimachine est appréciable sur une composition complexe</b> avec un nombre d'itération très élevé.<br />En revanche si la composition n'est pas complexe du tout <i>(moins de 3 secondes de rendu)</i>, le temps de réponse peut se présenter comme non négligeable.<br />",
			"<b>[options]</b> De façon générale : <b>les options modifiables dans les menus et barre d'outils sont provisoires</b>,<br />tandis que <b>celles dans les paramètres sont stockées dans le fichier config</b>.",
			"<b>[macro]</b> Un <b>rendu vidéo ?</b><br />Il est possible d'animer la position, le nombre d'itérations ainsi que deux autres constantes.<br />L'interface étant un peu rustre, un rapide coup d'oeil dans le manuel vous fera gagner du temps !",
			"<b>[macro]</b> <b>Les points d'entrée sorties accessibles par un <u>clic droit</u></b> sont situés :<br /> - Dans l'un des 3 champs de coordonnées<br /> - Dans la case itération de la barre d'outil<br /> - Dans les cases de variables du fractales<br />Les points d'entrée sortie définies, il faut finir le paramétrage dans l'administration.",
			"<b>[macro]</b> L'<b>administration des macros</b> permet de définir la variables d'animation, son pas, activer/ désactiver les variables additionelles, ...<br />Ce procédé répond à la demande de se concentrer sur principalement un paramètre.",
			"<b>[macro]</b> À <b>chaque option de macro modifié</b>, le <b>résumé et nombre d'image est mis à jour</b> dans le panneau de macro (<b>Affichage/ rendu macro</b>)",
			"<b>[fractale]</b> Certaines options sont disponibles pour les fractales (puissance, constantes, fonction).<br />N'importe quelle équation à deux modificateurs et une constante est possible pour les <b>biomorphes</b>.<br />En revanche pour les fractales il faudra utiliser l'éditeur d'équation pour en découvrir vraiment d'autres !", 
			"<b>[fractale]</b> Les fractales dans la liste sont tout simplement listés dans le dossier <b>fract/algo</b><br />Vérifiez sur le site officiel qu'il n'y ai pas de fichiers compatibles pour en ajouter <i>(vous pouvez aussi les partager)</i>", 
			"<b>[fractale]</b> Les <b>biomorphes</b> sont les dessins un peu plus dépouillés, ils sont en fait <b>calculés réccursivement</b> ce qui leur donnent ces formes nettes et étrangement proches des animaux marins.<br />Pour voir une évolution proche des cellules, utilisez l'éditeur de macro sur leur constantes !",
			"<b>[fractale]</b> L'éditeur d'quation n'est pas très abouti faute de temps, c'est en fait du code Java à éditer directement.<br />Nous vous conseillons d'utiliser un IDE adapté au Java pour écrire des plug-ins de fractales.",
			"<b>[ f i n ]</b> Nous vous remercions d'avoir lu cette aide, en espérant que vous aurez apprécié le logiciel.<br />Si vous êtes intéressé, vous pouvez contribuer au projet ou même le reprendre, informations sur le site officiel.<br /> A bientôt !"
	
	};
}
