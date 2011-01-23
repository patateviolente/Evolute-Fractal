package fract.renderEngine;

/**
 * Classe à appeler en réseau pour un rendu multitâche.<br />
 * Les arguments doivent respecter strictement le modèle d'arguments et la
 * classe pourra faire appel à un type Fractal puis procéder à son calcul.
 */
public class Analyzer {

	/**
	 * La méthode principale décrypte les arguments et fait appel à un fractal
	 * <ul>
	 * <li>Nom du fractal <i>(un String sans espace attendu)</i></li>
	 * <li>Nombre d'itérations <i>(Entier attendu)</i></li>
	 * <li>Début de calcul en X <i>(Entier attendu)</i></li>
	 * <li>Fin de calcul en X <i>(Entier attendu)</i></li>
	 * <li>Hauteur de la grille <i>(Entier attendu)</i></li>
	 * <li>Position en X coin supérieur gauche <i>(Réel attendu)</i></li>
	 * <li>Position en Y coin supérieur gauche <i>(Réel attendu)</i></li>
	 * <li>Échelle <i>(Réel attendu)</i></li>
	 * <li>Angle de rotation <i>(Réel attendu)</i></li>
	 * </ul>
	 * Au cas contraire, le programme s'arrêtera avec l'erreur 666.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String nom;
		int it, initX, endX, sizeY, preci;
		double posX, posY, scale, angle;
		// c'est un test de réponse
		if (args.length == 1 && args[0].equals("ping") || args.length == 0)
			System.out.println("pong");
		else if (args.length != 11 && args.length != 10) {
			// erreure, on informe (tous les flux)
			for (String s : args)
				System.out.println(s);
			System.err.println("ERREUR (Nombre d'arguments=" + args.length
					+ " au lieu de 10 ou 1 ou 0 attendus)");
			System.out.println("ERREUR (Nombre d'arguments=" + args.length
					+ " au lieu de 11, 10, 1 ou 0 attendus)");
		} // sinon on demande un vrai calcul
		else {
			try {
				nom = args[0];
				it = Integer.parseInt(args[1]);
				initX = Integer.parseInt(args[2]);
				endX = Integer.parseInt(args[3]);
				sizeY = Integer.parseInt(args[4]);
				posX = Double.parseDouble(args[5]);
				posY = Double.parseDouble(args[6]);
				scale = Double.parseDouble(args[7]);
				angle = Double.parseDouble(args[8]);
				preci = Integer.parseInt(args[9]);

				/*
				 * System.out.println("Calcul \"" + nom + "\" X=[" + initX + ";"
				 * + endX + "] iter=" + it + " " + "h=" + sizeY + " " + "{x=" +
				 * posX + " y=" + posY + " sc=" + scale + " angle=" + angle +
				 * "} ");
				 */
				try {
					Class cl = Class.forName("fract.renderEngine." + nom);

					BaseFractale fract;
					fract = (BaseFractale) cl.newInstance();
					fract.setAngle(angle);
					fract.changeCoordAndScale(posX, posY, scale);
					fract.setIterations(it);
					if(args.length > 10)
						fract.decryptFormatedArgs(args[10]);
					System.out.println("DEBUT");
					fract.launchCalcul(initX, endX, sizeY, preci);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			} catch (NumberFormatException e) {
				for (String s : args)
					System.out.println(s);
				System.err
						.println("ERREUR (type d'arguments invalides, la conversion ne peut être faîte)");
				System.out.println("ERREUR type args");
			}

		}

	}
}
