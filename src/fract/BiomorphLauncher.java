package fract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fract.ihm.*;
import fract.lang.OtherInterface;
import fract.opt.GestOptions;
import fract.opt.TableauOptions;
import fract.opt.TableauSaveColor;
import fract.opt.TableauSaveVue;
import fract.opt.EnumOption.StringEnum;

/**
 * <p>
 * Classe de lancement de l'application, fait appel à la fenêtre principale et
 * controle les arguments, les options, déclare des options de type Utils en
 * static...
 * </p>
 * 
 * @author Patrick Portal - Fabrice Antoine
 */
final public class BiomorphLauncher {

	/**
	 * <p>
	 * Méthode principale du programme, appel dans l'ordre : {Le tableau des
	 * options, de dégradés, de vues, le gestionnaire des options, le décrypteur
	 * d'arguments, et surtout la fenêtre.
	 * </p>
	 * Les arguments sont ceux décrits dans la notice.
	 * 
	 * @param args
	 *            Arguments en entrée (options)
	 */
	public static void main(String[] args) {
		// déclaration tableaux de données, et chargement du fichier config
		tOpt = new TableauOptions();
		tVue = new TableauSaveVue();
		tCol = new TableauSaveColor();
		// recherche d'un emplacement pour fichier config (ne peut être
		// enregistré dans un .jar, trop compliqué)
		BiomorphLauncher.windows = (System.getProperty("os.name").contains("Windows"));
		configPath = System.getProperty("user.dir") + "/config"; //$NON-NLS-1$ //$NON-NLS-2$
		gOpt = new GestOptions();

		// les otions sont chargées, on regarde si l'utilisateur à choisis une
		// langue différentes de celle par défaut du système
		try {
			Locale.setDefault(new Locale(BiomorphLauncher.tOpt
					.getStringValue(StringEnum.language), "")); //$NON-NLS-1$
		} catch (MissingResourceException e) {
			Locale.setDefault(new Locale("fr", "")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// écrase les argument par ceux surdéfinis enentrée
		appliArguments(args);

		// affichage des données chargées
		tOpt.afficher();
		tCol.afficher();
		// tVue.afficher();

		// style de la fenêtre (LookAndFeel)
		selectWindowStyle();
		// enfin la fenêtre
		writeProcedure(OtherInterface.getString("BiomorphLauncher.5")); //$NON-NLS-1$
		new MainWindow();
	}

	/**
	 * Fonction qui charge le style de fenêtre choisis dans les options par
	 * défaut le style de fenêtre est celui des JFrames dans Java mais il est
	 * possible d'assimiler les fenêtre à l'aide du système en utilisant
	 * LookAndFeel.
	 */
	public static void selectWindowStyle() {
		String n = tOpt.getStringValue(StringEnum.windowStyle);
		try {
			if (n.equals("system")) { //$NON-NLS-1$
				writeAction(OtherInterface.getString("BiomorphLauncher.7")); //$NON-NLS-1$
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e) {
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				} catch (UnsupportedLookAndFeelException e) {
					writeError(OtherInterface.getString("BiomorphLauncher.8")); //$NON-NLS-1$
				}
			} else if (n.equals("look&feel")) { //$NON-NLS-1$
				writeAction(OtherInterface.getString("BiomorphLauncher.10")); //$NON-NLS-1$
				JFrame.setDefaultLookAndFeelDecorated(true);
				UIManager.setLookAndFeel(UIManager.getLookAndFeel());
			} else {
				writeAction(OtherInterface.getString("BiomorphLauncher.11")); //$NON-NLS-1$
				// fenêtre par défaut swing
			}
		} catch (UnsupportedLookAndFeelException e) {
		}
	}

	/**
	 * Fonction qui va analyser les arguments en tableau de chaînes de
	 * caractères à l'appel du programme et les remplacer dans les options déjà
	 * chargées.
	 * 
	 * @param args
	 *            Les arguments tels quel dans le main
	 */
	public static void appliArguments(String args[]) {
		if (args.length > 0)
			writeProcedure(OtherInterface.getString("BiomorphLauncher.12") //$NON-NLS-1$
					+ ((args.length == 1) ? "'" : "es " + args.length + " ") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ "argument" + ((args.length == 1) ? "" : "s")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		for (int i = 0; i < args.length; i++) {
			// on récupère le nom de l'option jusque =
			int longueur = 0;
			for (int u = 0; u < args[i].length(); u++) {
				if (args[i].charAt(u) == '=')
					break;
				else
					longueur++;
			}
			String nom = args[i].substring(0, longueur);

			// si c'est pas cohérent on passe au prochain argument
			if (longueur == args[i].length() || longueur == 0) {
				writeError(OtherInterface.getString("BiomorphLauncher.0") //$NON-NLS-1$
						+ args[i]
						+ OtherInterface.getString("BiomorphLauncher.20")); //$NON-NLS-1$
				continue;
			} else if (!tOpt.exists(nom)) {
				writeError(OtherInterface.getString("BiomorphLauncher.21") //$NON-NLS-1$
						+ args[i]
						+ OtherInterface.getString("BiomorphLauncher.22")); //$NON-NLS-1$
				continue;
			}

			// puis la valeur
			String valeur = args[i].substring(longueur + 1, args[i].length());
			if (valeur.equals("true")) //$NON-NLS-1$
				tOpt.modifierOption(nom, true);
			else if (valeur.equals("false")) //$NON-NLS-1$
				tOpt.modifierOption(nom, false);
			else {
				try {
					int b = Integer.parseInt(valeur);
					tOpt.modifierOption(nom, b);
				} catch (NumberFormatException e) {
					tOpt.modifierOption(nom, valeur);
				}
			}
			writeAction(OtherInterface.getString("BiomorphLauncher.25") + args[i] //$NON-NLS-1$
					+ OtherInterface.getString("BiomorphLauncher.26")); //$NON-NLS-1$
		}
	}

	/**
	 * Rend l'angle en radians à partir de deux points.
	 * 
	 * @param xi
	 *            Abcissee point supérieur gauche
	 * @param yi
	 *            Ordonnée point supérieur gauche
	 * @param xf
	 *            Abcisse point inférieur droit
	 * @param yf
	 *            Ordonnée point inférieur droit
	 * @return angle en radians
	 */
	public static double getCornerFromThesePoints(int xi, int yi, int xf, int yf) {
		double angle = 0d;
		if (xi == xf && yi == yf)
			return 0;
		try {
			angle = Math.atan((double) Math.abs(yf - yi)
					/ (double) Math.abs(xf - xi));
			if (xf < xi)
				angle = Math.PI / 2 + Math.PI / 2 - Math.abs(angle);
			if (yf > yi)
				angle *= -1;
		} catch (ArithmeticException e) {
			angle = 0;
		}
		if (angle < 0)
			return 2 * Math.PI + angle;
		return angle;
	}

	/**
	 * Fait un appel extérieur à une commande (Process) et affiche son flux se
	 * sortie.
	 * 
	 * @param p
	 *            Objet Process
	 * @param cmd
	 *            La ligne de commande
	 */
	public static void runAndDisplayProcess(Process p, String cmd) {
		String rep = OtherInterface.getString("BiomorphLauncher.27"); //$NON-NLS-1$
		// System.out.println("--- La commande \""+cmd+"\" vaut:");

		try {
			String s;
			p = Runtime.getRuntime().exec(cmd);
			System.out.println(OtherInterface.getString("BiomorphLauncher.28") + cmd); //$NON-NLS-1$
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			while (!procDone(p)) {
				while ((s = stdInput.readLine()) != null) {
					rep += s + "\n"; //$NON-NLS-1$
					System.out.println(s);
				}

			}
		} catch (IOException e1) {
			return;
		}

	}

	/**
	 * Essai de trouver une valeur de sortie du processus, retourne vrai si
	 * réussite sinon faux.
	 * 
	 * @param p
	 *            L'objet Process
	 * @return Vrai si processus terminé.
	 */
	public static boolean procDone(Process p) {
		try {
			p.exitValue();
			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

	/**
	 * Formate une nouvelle étape importante du programme ayant pour syntaxe :
	 * ## texte
	 * 
	 * @param str
	 *            Ligne d'information de procédure
	 */
	public static void writeProcedure(String str) {
		System.out.println(" ## " + str); //$NON-NLS-1$
	}

	/**
	 * Formate une action racine de la procédure en cours ayant pour syntaxe :
	 * -> texte
	 * 
	 * @param str
	 *            Ligne d'information action
	 */
	public static void writeAction(String str) {
		System.out.println(" -> " + str); //$NON-NLS-1$
	}

	/**
	 * Formate une sous action de la procédure en cours avec pour syntaxe :
	 * ----> texte
	 * 
	 * @param str
	 *            Ligne d'information sous-action
	 */
	public static void writeSubAction(String str) {
		System.out.println(" ----> " + str); //$NON-NLS-1$
	}

	/**
	 * Formate une erreure du programme de la forme /!\ ERREUR /!\ en rouge
	 * 
	 * @param str
	 *            Ligne d'erreur
	 */
	public static void writeError(String str) {
		System.err.println(" /!\\ " + str.toUpperCase() + " /!\\"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static int nbActivWindow = 0;
	public static TableauSaveColor tCol;
	public static TableauSaveVue tVue;
	public static TableauOptions tOpt;
	public static GestOptions gOpt;
	public static String configPath;
	public static boolean windows;
}
