package fract.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fract.BiomorphLauncher;
import fract.exception.ThreadNotReadyException;
import fract.ihm.MainWindow;
import fract.renderEngine.CompressionRendu;
import fract.struct.*;
import fract.opt.EnumOption.BooleanEnum;

/**
 * <p>
 * Définie les points commun entre tous les algo existant sur les fractales. Les
 * dimensions, coordonnées minimales/maximales, les homotéties y sont définies.
 * </p>
 * <p>
 * De plus, cette classe contient toutes les fonctions nécessaires au calcul
 * pour définir des variables de type graphique, un traitement multithread et
 * les moteurs de rendu et d'affichage.
 * </p>
 */
public abstract class BaseFractale extends Thread implements ModelFractale {

	/**
	 * <p>
	 * Constructeur complexe pour donner d'office position, échelle, itérations,
	 * constantes[] du fractal.
	 * </p>
	 * 
	 * @param mw
	 *            Objet MainWindow
	 * @param posX
	 *            PosX du fractal
	 * @param posY
	 *            PosY du fractal
	 * @param scale
	 *            Echelle du fractam
	 * @param iterations
	 *            Le nombre d'itérations
	 */
	public BaseFractale(final MainWindow mw, final double posX,
			final double posY, final double scale, final int iterations) {
		// on récupère les éléments indispensables. Selon les différents
		// algorithmes certaines valeurs supplémentaires seront ajoutées
		this.mw = mw;
		this.posX = posX;
		this.posY = posY;
		this.scale = scale;
		this.iterations = iterations;
		this.recalculateAll = true;
		Largeur = this.mw.getPanelDimension().getWidth();
		Hauteur = this.mw.getPanelDimension().getHeight();
	}

	/**
	 * Adapte la position et l'échelle du fractal actuel selon la dimenison du
	 * panneau.
	 * 
	 * @param x
	 *            Longueur du panneau
	 * @param y
	 *            Hauteur du panneau
	 * @param CposX
	 *            Abcisse du fractal
	 * @param CposY
	 *            Ordonnée du fractal
	 * @param CscaleMin
	 *            Echelle du fractal
	 */
	public void adaptDefaultCoordToNewDim(final int x, final int y,
			final double CposX, final double CposY, final double CscaleMin) {
		double ratioX = baseX / (double) x, ratioY = baseY / (double) y;
		this.posX = CposX;
		this.posY = CposY;
		this.scale = CscaleMin;

		if (ratioX < ratioY) {
			double test = (x * ratioY) / (baseX);
			this.posY /= test;
			this.scale *= ratioX;
		} else {
			double test = (y * ratioX) / (baseY);
			this.posX /= test;
			this.scale *= ratioY;
		}
	}

	/**
	 * <p>
	 * Retourne une l'échelle suivant une échelle logarithmique. Cette échelle
	 * n'a pas vraiment de sens lorsque l'itération comme le fractal est censé
	 * être infini mais donne un appercu à l'utilisateur global d'où il est et
	 * si son nombre d'itération est correct :
	 * </p>
	 * <ul>
	 * <li>
	 * Lorsque l'échelle est en mode automatique, le pourcentage indiqué indique
	 * un pourcentage du point du plan large à l'échelle maximale avant
	 * imprécision.</li>
	 * <li>
	 * Lorsque l'itération est sur un mode manuel elle indique alors la
	 * progression du plan large jusque à un stade où le nombre d'itérations est
	 * insuffisant.</li>
	 * </ul>
	 * 
	 * @param CscaleMax
	 *            Echelle maximum
	 * @param CscaleMin
	 *            Echelle minimum
	 * @return Echelle logarithmique dépendant du nombre d'itérations en
	 *         pourcentage
	 */
	public int getpourcentZoom(final double CscaleMax, final double CscaleMin) {
		int tmp = (int) (1 / CscaleMax - 1 / (CscaleMin));
		return (int) (100 * Math.log10((1 / this.getScale())) / Math.log10(tmp
				* this.iterations));
	}

	/**
	 * <p>
	 * Lance la procédure de calcul. Avant de lancer la procédure de calcul, il
	 * faut facultativement avoir fait appel aux fonctions suivantes :
	 * </p>
	 * <ul>
	 * <li>setRecalculateAll(boolean) pour sauter ou non les pixels recopiés.</li>
	 * <li>setBufferAndGraphics(Graphics, BufferedImage) pour désigner
	 * l'affichage</li>
	 * </ul>
	 */
	public void run() {
		this.arretThread = false;
		this.enPause = false;
		next++;
		try {
			this.launchCalcul(this.departCalcul);
		} catch (ThreadNotReadyException e) {
			e.printStackTrace();
		} catch (NullPointerException e) { // en cas de redimensionnement
		} catch (ArrayIndexOutOfBoundsException e) { // de l'interface
		}
	}

	/**
	 * <p>
	 * Arrete net le calcul. Les threads en cours d'exécutions vont être
	 * stoppés, le pourcentage mis à 100%.
	 * </p>
	 */
	public void end() {
		this.arretThread = true;
	}

	/**
	 * <p>
	 * Met en pause le calcul si la valeur d'entrée est sur true, sinon le
	 * calcul sera remis en lecture.
	 * </p>
	 * 
	 * @param pause
	 *            Vrai pour mettre en pause le calcul
	 */
	public void setPause(final boolean pause) {
		this.enPause = pause;
	}

	/**
	 * @return le nombre d'arguments pour un fractal en particulier.
	 */
	public int getNbArgs() {
		return this.nbArgs;
	}

	/**
	 * @return tableau d'arguments de type double du fractal.
	 */
	public double[] getArgumentsArray() {
		return this.enterArgs;
	}

	/**
	 * Procédure d'initialisation avant lancement du calcul.
	 * 
	 * @param thread
	 *            Thread à préparer
	 */
	public void initialiserVariablesCalculGlobales(final int thread) {
		this.next = 0;
		this.prCentFini = new int[thread];
		nbEndedThread = 0;
	}

	/**
	 * Formate une chaîne de caractères du type 0.58657;67.97868 avec des
	 * nombres du type double pour envoyer les arguments du fractal aux machines
	 * distantes de rendu.
	 * 
	 * @return Chaine à envoyer à la machine distante.
	 */
	public String getFormatedArgs() {
		if (this.nbArgs == 0)
			return "";
		String s = this.enterArgs[0] + "";
		for (int i = 1; i < this.nbArgs; i++)
			s += ":" + this.enterArgs[i];
		return s;
	}

	/**
	 * <p>
	 * Applique les arguments en entrée pour le fracal. Pour rappel, les
	 * arguments sopnt un tableau de double et seront interprétés par la classe
	 * du fractal chargé.
	 * </p>
	 * <p>
	 * <font color="red">/!\ Attention, pour se servir de cette fonction, il est
	 * préférable de stopper l'exécution et de la relancer complètement après
	 * sinon différentes parties de l'image représentant un fractal avec
	 * arguments différents seront mélangées /!\</font>
	 * </p>
	 * 
	 * @param args
	 *            Tableau de type double
	 */
	public void setArguments(final double[] args) {
		this.enterArgs = args;
	}

	/**
	 * <p>
	 * Donne les informations sur les capacités des machines distantes.
	 * Initialise les variables de calcul à distance. Les champs requis sont :
	 * </p>
	 * <ul>
	 * <li>Un tableau d'adressage machine du type <br />
	 * PORT USER ADRESSE_IP DOSSIER_BIN</li>
	 * <li>Un tableau d'entier contenant respectivement le nombre de processus à
	 * lancer par la machine</li>
	 * </ul>
	 * <li>Respectivement la liste des mots de passe brut</li> </ul>
	 * 
	 * @param infoNetwork
	 *            Tableau de String contenant info serveur (voir description)
	 * @param nbProcessus
	 *            Tableau d'entier contenant nombre de processus
	 * @param mdp
	 *            Tableau de String contenant mots de passe
	 */
	public void setDistantProperties(final String[] infoNetwork,
			final int[] nbProcessus, final String[] mdp) {
		this.networkOffset = 0;
		this.networkTotal = 0;
		this.networkMdp = mdp;
		this.netWorkInfoNetwork = infoNetwork;
		this.netWorkNbProcessus = nbProcessus;
		for (int i : this.netWorkNbProcessus)
			this.networkTotal += i;
	}

	/**
	 * <p>
	 * Modifie l'argument d'un fractal à une position donnée. Attention, ne
	 * revoie pas d'exception en cas de dépassement de tableau.
	 * </p>
	 * 
	 * @param offset
	 *            Position de l'argument dans le tableau
	 * @param value
	 *            Valeur (type double)
	 */
	public void modifyFractArgument(final int offset, final double value) {
		this.enterArgs[offset] = value;
	}

	/**
	 * Initialise un nouveau thread afin de vérifier la synchronisation. Renvoie
	 * une exception pour avertir si un processus n'a pas eu le temps de se
	 * lancer précédement.
	 * <p>
	 * dec est le nombre de thread. DepartCalcul est le thread actuel.
	 * </p>
	 * 
	 * @param dec
	 *            Nombre total de threads
	 * @param departCalcul
	 *            Thread
	 * @param local
	 *            vrai = calcul local, besoin de synchronisation
	 * @throws ThreadNotReadyException
	 *             Au cas où le thread précédent n'aurait pas été lancé. Au tel
	 *             cas le thread ordonné n'est pas lancé, c'est à la fonction
	 *             supérieur de redemander le calcul.
	 */
	public void setDecalageCalcul(final int dec, final int departCalcul,
			final boolean local) throws ThreadNotReadyException {
		if (departCalcul != next) {
			throw new ThreadNotReadyException("Le calcul de l'étape "
					+ departCalcul + " ne peut pas se lancer car l'étape"
					+ next + " n'a pas encore été lancée");
		}
		this.decalageCalcul = dec;
		this.departCalcul = departCalcul;
		if (!local) // si le calcul n'est pas local, on ne synchronise pas
			next++;
	}

	/**
	 * @param dec
	 *            Le nombre total de Threads
	 * @param departCalcul
	 *            Le thread actuel
	 */
	public void setDecalageCalculUnsynchronized(final int dec,
			final int departCalcul) {
		this.decalageCalcul = dec;
		this.departCalcul = departCalcul;
	}

	/**
	 * <p>
	 * Dépose la précision de calcul (nombre entre 1 et l'infini) qui sera
	 * appliquée pour les calculs suivants.
	 * </p>
	 * <p>
	 * La précision est l'intervalle de pixels sur la suite de pixels calculés.
	 * Si ce nombre est 1, tous les pixels seront calculés, si 2 alors 1/2²
	 * pixels seronts calculés...
	 * </p>
	 * <p>
	 * <u>Exemples illustrés</u> (<b>X</b> pixel calculé, <b>-</b> pixel recopié
	 * par le pixel le plus proche supérieur gauche)
	 * </p>
	 * <ul>
	 * <li>Précision = 1 (soit 1/1)<br />
	 * X X X X X X X X<br />
	 * X X X X X X X X<br />
	 * X X X X X X X X<br />
	 * ...</li>
	 * <li>Précision = 2 (soit 1/4) :<br />
	 * X - X - X - X -<br />
	 * - - - - - - - -<br />
	 * X - X - X - X -<br />
	 * - - - - - - - -<br />
	 * </li>
	 * <li>Précision = 3 (soit 1/9) :<br />
	 * X - - X - - X -<br />
	 * - - - - - - - -<br />
	 * - - - - - - - -<br />
	 * X - - X - - X -<br />
	 * - - - - - - - -<br />
	 * - - - - - - - -<br />
	 * </li>
	 * <li>...</li>
	 * 
	 * </ul>
	 * 
	 * @param precision
	 *            Précision en pixels (1 meilleur)
	 */
	public void setPrecision(final int precision) {
		this.preci = precision;
	}

	public void setDirectDisplay(final boolean val) {
		this.directDisplay = val;
	}

	/**
	 * <p>
	 * Change les coordonnées principales : la position et l'échelle par celles
	 * enregistrées, les valeurs sont enregistrées sans traitement.
	 * </p>
	 * 
	 * @param x
	 *            Abcisse point supérieur gauche
	 * @param y
	 *            Ordonnée point supérieur gauche
	 * @param sc
	 *            Echelle du fractal
	 */
	public void changeCoordAndScale(final double x, final double y,
			final double sc) {
		this.posX = x;
		this.posY = y;
		this.scale = sc;
	}

	/**
	 * <p>
	 * Déplace le fractal du nombre de pixels en X et Y donnés dans le plan qui
	 * seront adaptés au changement de position dans le plan par multiplication
	 * du l'échelle.
	 * </p>
	 * 
	 * @param offsetX
	 *            Décalage X en pixels
	 * @param offsetY
	 *            Décalage Y en pixels
	 */
	public void deplacer(final int offsetX, final int offsetY) {
		this.posX -= scale * offsetX;
		this.posY -= scale * offsetY;
	}

	/**
	 * <p>
	 * Zoom sur la fractal à partir d'un décalage + un nouveau ratio (1 pour
	 * aucun changement)
	 * </p>
	 * <p>
	 * Le décalage doit être donné en pixels depuis la zone d'affichage, les
	 * variables de position dans le plan seront calculées automatiquement
	 * depuis ce décalage et l'échelle du fractal.
	 * </p>
	 * 
	 * @param offsetX
	 *            Décalage X en pixels
	 * @param offsetY
	 *            Décalage Y en pixels
	 * @param ratio
	 *            Ratio entre l'image d'origine et la zone de sélection
	 */
	public void zoomer(final int offsetX, final int offsetY, final double ratio) {
		this.posX += scale * offsetX;
		this.posY += scale * offsetY;
		this.scale *= ratio;
	}

	/**
	 * @return le nombre d'itérations
	 */
	public int getIterations() {
		return this.iterations;
	}

	/**
	 * <p>
	 * Change le nombre d'itérations par celui en argument.
	 * </p>
	 * 
	 * @param it
	 *            Nouveau nombre d'itérations
	 */
	public void setIterations(final int it) {
		this.iterations = it;
	}

	public int getAutoIteration() {
		int i = (int) ((100 * Math.log1p((1 / this.getScale() * 2))) - 400)
				* this.getpourcentZoom() / 100;
		return (i < 20) ? 20 : i;
	}

	/**
	 * Modifie la position et l'échelle par les valeurs en entrée. L'itération
	 * automatique est mise à joour si active.
	 * 
	 * @param posX
	 *            Abcisse du point supérieur gauche
	 * @param posY
	 *            Ordonnée du point supérieur gauche
	 * @param scale
	 *            Nouveau échelle
	 */
	public void setCoordonnees(final double posX, final double posY,
			final double scale) {
		this.posX = posX;
		this.posY = posY;
		this.scale = scale;
	}

	/**
	 * Change l'état de la variable recalculateAll. Si elle est sur vrai alors
	 * chaques pixels sera calculé un par un sinon seuls les points qui sont sur
	 * NULL dans la matrice de point seront recalculés.
	 * 
	 * @param recalculate
	 *            false pour ne pas recalculer les pixels contenant déjà une
	 *            couleur
	 */
	public void setRecalculateAll(final boolean recalculate) {
		this.recalculateAll = recalculate;
	}

	/**
	 * @param g
	 *            Objet Graphics (affichage instantanné écran)
	 * @param bi
	 *            Objet BufferedImage (tampon)
	 */
	public void setBufferAndGraphics(final Graphics g, final BufferedImage bi) {
		this.g = g;
		this.bi = bi;
	}

	/**
	 * Demande l'avancement de tous les threads et fait une moyenne pour rendre
	 * l'avancement global de 0 à 100.
	 * 
	 * @return L'avancement global en pourcentage.
	 */
	public int getAvancement() {
		int total = 0;
		if (this.prCentFini == null) // non commencé
			return 100;
		for (int i : this.prCentFini)
			total += i;
		return total / this.prCentFini.length;

	}

	/**
	 * Recalculer. Le calcul sera partiel ou complet selon l'état de la variable
	 * recalculateAll.
	 * 
	 * @throws ThreadNotReadyException
	 *             Au cas où le thread précédent n'aurait pas été lancé. Au tel
	 *             cas le thread ordonné n'est pas lancé, c'est à la fonctin
	 *             supérieur de redemander le calcul.
	 */
	public void calculer() throws ThreadNotReadyException {
		this.launchCalcul(this.departCalcul);
	}

	/**
	 * Appeler calculer en défnissant un ImageBuffer, un object Graphics pour
	 * compléter le cache image et dessiner en même temps l'image lors du
	 * calcul. Permet en plus de redéfinir un calcul partiel selon le cache de
	 * la matrice de point (RegPoint)
	 * 
	 * @param g
	 *            L'objet Graphics
	 * @param bi
	 *            L'objet BufferedImage
	 * @param recalculateAll
	 *            Vrai pour que tous les pixels non trouvés soient recalculés.
	 * @throws ThreadNotReadyException
	 *             Au cas où le thread précédent n'aurait pas été lancé. Au tel
	 *             cas le thread ordonné n'est pas lancé, c'est à la fonctin
	 *             supérieur de redemander le calcul.
	 */
	public void calculer(final Graphics g, final BufferedImage bi,
			final boolean recalculateAll) throws ThreadNotReadyException {
		this.recalculateAll = recalculateAll;
		this.g = g;
		this.bi = bi;
		this.launchCalcul(this.departCalcul);
	}

	/**
	 * Méthode calculer commune à tous les fractales, paramétrer cette fonction
	 * à partir des constructeurs et méthodes externe de préfixe
	 * 'CalculateStepxxx'
	 * 
	 * @throws ThreadNotReadyException
	 */
	public void calculer(final Graphics g, final BufferedImage bi)
			throws ThreadNotReadyException {
		this.g = g;
		this.bi = bi;
		this.launchCalcul(this.departCalcul);
	}

	/**
	 * Lancement du calcul. Cette procédure utilise les configurations
	 * enregistré par les procédure admettant un BufferImage, un type Graphics,
	 * une valeur recalculateAll, ainsi que le paramétrage des Thread. La
	 * fonction fait appel à d'autres moteurs de rendu si le moteur vertical
	 * linéaire n'est pas choisi.
	 * 
	 * @param thread
	 *            Position du thread depuis la gauche du dessin.
	 * @throws ThreadNotReadyException
	 */
	public void launchCalcul(final int thread) throws ThreadNotReadyException {
		int rows, columns, endX;

		// Mauvause configuration d'affichage avant calcul
		if (mw.pts == null || mw.pts.getColumns() <= 0) {
			mw.pts = new RegPoint((int) mw.getPanelDimension().getWidth(),
					(int) mw.getPanelDimension().getHeight());
			if (mw.getPanelDimension().getWidth() < 1) {
				throw new ThreadNotReadyException(
						"Dessin lancé sans connaître la taille du panel");
			}
		}

		// rendu progressif
		if (BiomorphLauncher.tOpt.getBooleanValue(BooleanEnum.progressive)) {
			this.aproxCalcul(thread);
			return;
		}
		// rendu linéaire horizontal
		if (!BiomorphLauncher.tOpt.getBooleanValue(BooleanEnum.verticalRender)) {
			this.horizRender(thread);
			return;
		}
		// sinon rendu linéaire vertical

		// On détermine colonnes et lignes
		if (bi == null) {
			rows = mw.pts.getRows();
			columns = mw.pts.getColumns();
		} else {
			rows = bi.getWidth();
			columns = bi.getHeight();
		}
		int inter = rows / this.decalageCalcul;
		endX = (thread == (this.decalageCalcul - 1)) ? rows : (thread + 1)
				* (inter);
		int initX = this.adaptIntervalToPrecision(thread * inter);

		// RENDU RÉSEAU
		if (this.networkTotal-- > 0) {
			while (true) {
				if (this.netWorkNbProcessus[offset]-- > 0) {
					// information console
					if (thread == 0)
						BiomorphLauncher.writeAction("Lancement calculs distants");
					// ///////////
					this.launchDistantCalcul(thread, inter, initX, endX,
							columns, offset);
					break;
				} else
					++offset;
			}
			return;
		}

		int[] result = new int[columns]; // stock résultats
		for (int x = initX; x < endX; x += preci) {
			for (int y = 0; y < columns; y += preci) {
				finTests: if (recalculateAll
						|| (this.mw.pts.getColor(x, y) == null)) {
					this.calculateStepZZero(thread, x, y); // REDEF étape0
					for (int i = 1; i < iterations; i++) // suite récurrence
					{
						this.calculateStepPowAndCst(thread, i, x, y); // REDEF
						// test sur l'élément de la suite calculé
						mw.pts.setPoint(x, y, mw.getBgFractColor());
						if (this.calculateStepGetCondition(thread, i)) //  REDEF
						{
							result[y] = i;
							break finTests;
						}
					}
					if (g != null) // coloration par défaut si tests ratés
						result[y] = -1;

				} else
					result[y] = -2;
				// fin condition
			}
			// on dessine la lignée de points
			for (int i = 0; i < result.length; i += this.preci)
				this.setSynchrPoint(x, i, result[i], this.preci);

			// on met à jour le pourcentage à chaques colonne effectuée
			if (this.prCentFini != null) {
				this.prCentFini[thread] = 100 - (int) ((float) ((endX - x))
						/ (float) (endX - (thread * inter)) * 100);
			}
			// vérification arrêt
			if (this.arretThread)
				break;
			// vérification pause
			while (this.enPause) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		// fin boucles et fonction
		if (this.prCentFini != null)
			this.prCentFini[thread] = 100;
		nbEndedThread++;
	}

	/**
	 * Lance le calcul distant et analyse les flux d'entrée.
	 * 
	 * @param thread
	 *            Le numéro du thread (de 0 au maximum)
	 * @param initX
	 *            Abcisse de départ du calcul
	 * @param endX
	 *            Abcisse de fin de calcul
	 */
	public void launchDistantCalcul(final int thread, final int inter,
			final int initX, final int endX, final int sizeY, final int offset) {
		String s;
		if (this.networkMdp[offset].length() != 0)
			s = BiomorphLauncher.class.getResource("/NetworkCallPass.sh")
					.toString();
		else
			s = BiomorphLauncher.class.getResource("/NetworkCall.sh")
					.toString();
		int ligne = 0, y; // nombre de lignes (et donc de colonnes calculées)
		String splited[];
		boolean debut = false; // indique si le rendu à commencé
		s = s.substring(5, s.length());

		String cmd = s
				+ ((this.networkMdp[offset].length() != 0) ? " "
						+ this.networkMdp[offset] + "" : "") + " "
				+ this.netWorkInfoNetwork[offset] + " " + mw.getFractalName()
				+ " " + this.iterations + " " + initX + " " + endX + " "
				+ sizeY + " " + this.posX + " " + this.posY + " " + this.scale
				+ " " + this.angle + " " + this.preci + " "
				+ this.getFormatedArgs();
		BiomorphLauncher.writeSubAction(cmd);

		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			s = null;

			while (!BiomorphLauncher.procDone(p)) {
				while ((s = stdInput.readLine()) != null) {
					y = 0;
					if (!debut && s.equals("DEBUT"))
						debut = true;
					else if (s.equals("FIN")) {

					} else if (debut) {
						++ligne;
						s = CompressionRendu.uncompress(s);

						splited = s.split(";");
						for (int i = 0; i < splited.length - 1; i++) {
							this.setSynchrPoint(initX + (ligne - 1) * preci, y
									* preci, Integer.parseInt(splited[i]),
									this.preci);
							y++;
						}

						// on met à jour le pourcentage à chaques colonne
						if (this.prCentFini != null)
							this.prCentFini[thread] = Math
									.min(
											((int) ((float) (ligne)
													/ (float) (endX - (thread * inter)) * 100))
													* this.preci, 100);

						// vérification arrêt
						if (this.arretThread)
							break;
						// vérification pause
						while (this.enPause) {
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					} else if (!s.equals("FIN")) {
						// sinon la ligne est une information avant rendu, on
						// peut l'afficher
					}

				}
			}
			stdInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// fin boucles et fonction
		if (this.prCentFini != null)
			this.prCentFini[thread] = 100;
		nbEndedThread++;
	}

	/**
	 * Mettre un point dans la grille de pixel, dans le buffer ou à l'écran,
	 * synchronisé pour que les couleurs ne se mélangent pas.
	 * 
	 * @param x
	 *            Abcisse point supérieur gauche.
	 * @param y
	 *            Ordonnée point supérieur gauche.
	 * @param i
	 *            Itérations <i>(correspond à une couleur)</i>
	 * @param largeur
	 *            La précision <i>(1 meilleur, 2 = carré 2 sur 2, ...).</i>
	 */
	public synchronized void setSynchrPoint(final int x, final int y,
			final int i, final int largeur) {
		if (i < -1)
			return;
		// choix couleurs
		if (i <= 0) { // couleur par dft
			this.couleur = mw.getBgFractColor();
			bi.setRGB(x, y, g.getColor().getRGB());
		} else {
			this.couleur = mw.getColorAtIteration(i - 1);
		}
		// remplissage écran
		if (this.directDisplay) {
			g.setColor(this.couleur);
			g.fillRect(x, y, largeur, largeur);
		}

		// remplissage grille de points
		if (largeur > 1)
			for (int u = 0; u < largeur; u++)
				for (int w = 0; w < largeur; w++)
					mw.pts.setPoint(x + u, y + w, this.couleur);
		else
			mw.pts.setPoint(x, y, this.couleur);

		// remplissable buffer
		if (largeur > 1) {
			for (int u = x; u < ((bi.getWidth() > x + largeur) ? x + largeur
					: bi.getWidth()); u++)
				for (int w = y; w < ((bi.getHeight() > y + largeur) ? y
						+ largeur : bi.getHeight()); w++)
					bi.setRGB(u, w, this.couleur.getRGB());
		} else
			bi.setRGB(x, y, this.couleur.getRGB());

	}

	/**
	 * Algorithme de calcul en affichage par approche.
	 * 
	 * @param thread
	 *            Le thread à calculer
	 */
	private void aproxCalcul(final int thread) {
		int rows, columns, endX;
		int decX, xx; // xx = vrai valeur de x

		// On détermine colonnes et lignes
		if (bi == null) {
			rows = mw.pts.getRows();
			columns = mw.pts.getColumns();
		} else {
			rows = bi.getWidth();
			columns = bi.getHeight();
		}
		int inter = rows / this.decalageCalcul;

		endX = (thread == (this.decalageCalcul - 1)) ? rows : (thread + 1)
				* (inter);

		// repères calculs
		decX = this.adaptIntervalToPrecision(thread * inter);

		if (thread == 0) {
			// initialisation
			this.tabOffset = new int[inter];
			this.tabDist = new int[inter];
			this.offset = 0;
			this.genTable(0, inter);

			// on agrandit la grille pour le dernier processus plus grand
			int reste = rows - inter * this.decalageCalcul;
			int[] to = new int[this.tabOffset.length + reste];
			int[] td = new int[this.tabDist.length + reste];
			for (int i = 0; i < this.tabOffset.length; i++) {
				to[i] = this.tabOffset[i];
				td[i] = this.tabDist[i];
			}
			for (int i = this.tabOffset.length; i < this.tabOffset.length
					+ reste; i++) {
				to[i] = i;
				td[i] = this.tabOffset.length + reste - i;
			}
			this.tabOffset = to;
			this.tabDist = td;
		} else if (thread + 1 == this.decalageCalcul)
			inter += (rows - (decX + inter));

		for (int x = 0; x < inter; x++) {
			xx = this.tabOffset[x];
			for (int y = 0; y < columns; y++) {
				fin: if (true) { // faudra trouver 1truc pour pas tt recalculer
					this.calculateStepZZero(thread, xx + decX, y); // REDEF
					// étape0
					for (int i = 1; i < iterations; i++) // suite récurrence
					{
						this.calculateStepPowAndCst(thread, i, xx + decX, y); // REDEF
						// test sur l'élément de la suite calculé
						mw.pts.setPoint(this.tabOffset[x] + decX, y, mw
								.getBgFractColor());
						if (this.calculateStepGetCondition(thread, i)) //  REDEF
						{
							this.setSynchrPoint(xx + decX, y, i,
									this.tabDist[x]);
							break fin;
						}
					}
					if (g != null) // coloration par défaut si tests ratés
						this.setSynchrPoint(xx + decX, y, -1, this.tabDist[x]);
				}

				// on met à jour le pourcentage à chaques colonne effectuée
				if (this.prCentFini != null) {
					this.prCentFini[thread] = 100 - (int) ((float) ((endX - (x + decX)))
							/ (float) (endX - (thread * inter)) * 100);
				}
				// vérification arrêt
				if (this.arretThread)
					break;
				// vérification pause
				while (this.enPause) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		// fin boucles et fonction
		if (this.prCentFini != null)
			this.prCentFini[thread] = 100;
		nbEndedThread++;
	}

	/**
	 * Rendu linéaire horizontal (de haut en bas)
	 * 
	 * @param thread
	 *            Thread à calculer
	 */
	private void horizRender(final int thread) {
		int rows, columns, endY;

		// On détermine colonnes et lignes
		if (bi == null) {
			rows = mw.pts.getRows();
			columns = mw.pts.getColumns();
		} else {
			rows = bi.getWidth();
			columns = bi.getHeight();
		}
		int inter = rows / this.decalageCalcul;
		endY = (thread == (this.decalageCalcul - 1)) ? columns : (thread + 1)
				* (inter);

		int[] result = new int[rows]; // stock résultats
		for (int y = this.adaptIntervalToPrecision(thread * inter); y < this.bi
				.getHeight(); y += preci) {
			for (int x = 0; x < rows; x += preci) {
				finTests: if (recalculateAll
						|| (this.mw.pts.getColor(x, y) == null)) {
					this.calculateStepZZero(thread, x, y); // REDEF étape0
					for (int i = 1; i < iterations; i++) // suite récurrence
					{
						this.calculateStepPowAndCst(thread, i, x, y); // REDEF
						// test sur l'élément de la suite calculé
						mw.pts.setPoint(x, y, mw.getBgFractColor());
						if (this.calculateStepGetCondition(thread, i)) //  REDEF
						{
							result[x] = i;
							break finTests;
						}
					}
					if (g != null) // coloration par défaut si tests ratés
						result[x] = -1;

				} else
					result[x] = -2;
			}
			// on dessine la lignée de points
			for (int i = 0; i < result.length; i += this.preci)
				this.setSynchrPoint(i, y, result[i], this.preci);

			// on met à jour le pourcentage à chaques colonne effectuée
			if (this.prCentFini != null) {
				this.prCentFini[thread] = 100 - (int) ((float) ((endY - y))
						/ (float) (endY - (thread * inter)) * 100);
			}
			// vérification arrêt
			if (this.arretThread)
				break;
			// vérification pause
			while (this.enPause) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		// fin boucles et fonction
		if (this.prCentFini != null)
			this.prCentFini[thread] = 100;
		nbEndedThread++;
	}

	/**
	 * Méthode utile pour le rendu progressif, calcul les position de façon
	 * dichonomique du rendu.
	 * 
	 * @param debut
	 *            Position minimale du tableau
	 * @param fin
	 *            Position maximale du tableau
	 */
	private void genTable(final int debut, final int fin) {
		// System.out.println("appel de "+debut+" à "+fin);
		this.tabOffset[offset] = debut;
		this.tabDist[offset] = fin - debut;
		// System.out.println("d=" + debut + " ; " + fin + "  (" + offset +
		// ")");

		if (this.tabDist[offset++] == 1)
			return;

		if (debut + (fin - debut) / 2 != fin)
			genTable(debut + (fin - debut) / 2, fin);
		if (debut + 1 < debut + (fin - debut) / 2)
			genTable(debut + 1, debut + (fin - debut) / 2);

	}

	/**
	 * Utilise le modulo pour donne run chiffre inférieur rond pour les étapes
	 * de calculs en cas de précision supérieur à 1
	 * 
	 * @param valeur
	 * @return valeur <= à celle d'entrée utilisable pour la précision
	 */
	public int adaptIntervalToPrecision(int valeur) {
		while (valeur % this.preci != 0)
			valeur++;
		return valeur;
	}

	/**
	 * @return vrai si tous les threads de calculs sont arrivés à leur fin de
	 *         vie.
	 */
	public boolean isCalculFinished() {
		return (this.nbEndedThread >= this.decalageCalcul);
	}

	/**
	 * Impose une constante d'angle qui sera effective dans les prochains
	 * calculs.
	 * 
	 * @param angle
	 *            Nouvel angle
	 */
	public void setAngle(final double angle) {
		this.angle = angle / 2;
	}

	/**
	 * @return angle
	 */
	public double getAngle() {
		return this.angle;
	}

	/**
	 * Retourne le zoom dans un créneau de 0 à 100 selon une échelle
	 * logarithmique et d'après les données entrée individuelement dans chaques
	 * fractales CscaleMin ET CscaleMax.
	 * 
	 * @return pourcentageZoom
	 */
	public int getpourcentZoom() {
		return 0;
	}

	/**
	 * @return échelle
	 */
	public double getScale() {
		return this.scale;
	}

	/**
	 * @return la position en X
	 */
	public double getPosX() {
		return this.posX;
	}

	/**
	 * @return la position en Y
	 */
	public double getPosY() {
		return this.posY;
	}

	/**
	 * @return la couleur par défaut
	 */
	public Color getBackground() {
		return this.bg;
	}

	public double Largeur, Hauteur;
	// variables globales
	protected double posX, posY, scale, angle = 0;
	protected MainWindow mw;
	private boolean arretThread, enPause;
	protected int nbArgs = 0;
	protected double[] enterArgs;
	private int[] tabOffset;
	private int[] tabDist;
	private int offset;

	// variables de calcul
	final protected int baseX = 190, baseY = 200;
	protected double decX = 0, decY = 0;
	protected boolean recalculateAll, directDisplay = false;
	protected int decalageCalcul = 1, nbEndedThread = 1;
	private int departCalcul = 0;
	private int[] prCentFini;
	private int preci = 1;
	private int next = 0;
	private Color couleur;

	// variables pour le rendu réseau
	String[] netWorkInfoNetwork, networkMdp;
	int[] netWorkNbProcessus;
	int networkOffset, networkTotal;

	// variables pour le calcul
	protected int iterations = 10;
	protected Color bg = Color.black;

	// affichage
	protected Graphics g;
	protected BufferedImage bi;
}
