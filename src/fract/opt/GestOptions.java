package fract.opt;

import java.awt.Color;
import java.io.*; // écriture/ lecture fichier de config
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.*; // analyse du fichier par expressions régulières

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fract.BiomorphLauncher;
import fract.color.ChooseRampColor;
import fract.color.ColorAdvanced;
import fract.lang.GestOption;
import fract.opt.EnumOption.*;

/**
 * <p>
 * Permet de charger le fichier config dans les structures de stockage des
 * différents éléments possibles à stocker [vues|couleurs|options|réseau]. Cette
 * classe utilise les expressions régulières, il est possible de charger une
 * ligne d'option individuelement.
 * </p>
 */
final public class GestOptions {

	/**
	 * <p>
	 * Première instance, on vérifie si le fichier de config existe. S'il
	 * n'existe pas le fichier est créé par défaut.
	 * </p>
	 */
	public GestOptions() {

		File f = new File(BiomorphLauncher.configPath);
		if (f == null || !f.exists()) {
			try {
				this.createInitConfigFile();
			} catch (IOException e1) {
				System.err
						.println(GestOption.getString("GestOption.0") //$NON-NLS-1$
								+ GestOption.getString("GestOption.1") //$NON-NLS-1$
								+ GestOption.getString("GestOption.2")); //$NON-NLS-1$
			}
		}

		// enfin, on charge les donnée depuis le fichier config
		try {
			this.loadOptions();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.checkOptionExistence();

	}

	/**
	 * Vérifie si les toutes les options existent dans le fichier config. Une
	 * liste des options non définies est créée pour être affichée à
	 * l'utilisateur.
	 */
	final private void checkOptionExistence() {
		StringEnum[] s = StringEnum.values();
		IntEnum[] i = IntEnum.values();
		BooleanEnum[] b = BooleanEnum.values();
		ArrayList al = new ArrayList<Object>();

		for (StringEnum ss : s) { // StringValues
			try {
				BiomorphLauncher.tOpt.getStringValue(ss);
			} catch (NullPointerException e) {
				BiomorphLauncher.writeError(GestOption.getString("GestOption.3") + ss + GestOption.getString("GestOption.4")); //$NON-NLS-1$ //$NON-NLS-2$
				al.add(ss);
			}
		}

		for (IntEnum ss : i) { // IntValues
			try {
				BiomorphLauncher.tOpt.getIntValue(ss);
			} catch (NullPointerException e) {
				BiomorphLauncher.writeError(GestOption.getString("GestOption.5") + ss + GestOption.getString("GestOption.6")); //$NON-NLS-1$ //$NON-NLS-2$
				al.add(ss);
			}
		}

		for (BooleanEnum ss : b) { // BooleanValues
			try {
				BiomorphLauncher.tOpt.getBooleanValue(ss);
			} catch (NullPointerException e) {
				BiomorphLauncher.writeError(GestOption.getString("GestOption.7") + ss + GestOption.getString("GestOption.8")); //$NON-NLS-1$ //$NON-NLS-2$
				al.add(ss);
			}
		}

		if (al.size() != 0) {
			Iterator<EnumOption> it;
			String manquant = new String("{"); //$NON-NLS-1$
			it = al.iterator();
			while (it.hasNext()) {

				manquant += it.next() + " "; //$NON-NLS-1$
			}
			if (JOptionPane
					.showConfirmDialog(
							new JFrame(),
							GestOption.getString("GestOption.11") //$NON-NLS-1$
									+ GestOption.getString("GestOption.12") //$NON-NLS-1$
									+ manquant
									+ GestOption.getString("GestOption.13") //$NON-NLS-1$
									+ GestOption.getString("GestOption.14") //$NON-NLS-1$
									+ GestOption.getString("GestOption.15") //$NON-NLS-1$
									+ GestOption.getString("GestOption.16"), //$NON-NLS-1$
							GestOption.getString("GestOption.17"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				File f = new File(BiomorphLauncher.configPath);
				if (f.exists() && f.delete()) {
					try {
						this.createInitConfigFile();
					} catch (IOException e1) {
						System.err
								.println(GestOption.getString("GestOption.18") //$NON-NLS-1$
										+ GestOption.getString("GestOption.19") //$NON-NLS-1$
										+ GestOption.getString("GestOption.20")); //$NON-NLS-1$
						System.exit(0);
					}
					try {
						this.loadOptions();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					JOptionPane
							.showMessageDialog(
									new JFrame(),
									GestOption.getString("GestOption.21"), //$NON-NLS-1$
									GestOption.getString("GestOption.22"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
					System.exit(5);
				}
			} else
				System.exit(5);
		}
	}

	/**
	 * <p>
	 * Charge les options rangées dans le fichier de configuration et les place
	 * dans les variables static de BiomorphLauncher.
	 * </p>
	 * <p>
	 * Cette classe fait en fait un tri du fichier en localisant les différentes
	 * parties, en supprimant les commentaires et espaces puise en faisant appel
	 * à la mathode de chargement.
	 * </p>
	 * 
	 * @throws IOException
	 */
	final private void loadOptions() throws IOException {
		BiomorphLauncher.writeProcedure(GestOption.getString("GestOption.23")); //$NON-NLS-1$
		BiomorphLauncher.writeAction(GestOption.getString("GestOption.24")); //$NON-NLS-1$
		// str = System.class.getResourceAsStream("/config");
		// fileIn = new InputStreamReader(str);
		// File entree = new File(BiomorphLauncher.configPath);
		BufferedReader entree = new BufferedReader(new FileReader(
				BiomorphLauncher.configPath));

		// BufferedReader entree = new BufferedReader(fileIn);
		byte mode = -1; // défini le mode de chargement de donnée
		String line;
		Pattern parseLine = Pattern.compile("\\s*([^#]*).*$"); //$NON-NLS-1$

		Matcher match;

		boucle: do // on parcourt toutes les lignes
		{
			line = entree.readLine();
			if (line == null) // fin du fichier, on arrête
				break;
			// si la ligne est commentée on passe
			match = parseLine.matcher(line);
			if (!(match.find() && !match.group(1).isEmpty()))
				continue;

			// Vérification d'un changement de modes
			if (line.length() > 0 && line.charAt(0) == '-') {
				for (int i = mode + 1; i < strToUpMode.length; i++) {
					if (line.equals(strToUpMode[i])) {
						mode = (byte) i;
						BiomorphLauncher
								.writeSubAction(GestOption.getString("GestOption.26") //$NON-NLS-1$
										+ strToUpMode[mode]);
						continue boucle;
					}
				}
			}

			/*
			 * si on a trouvé au moins un mode, on détecte la ligne et on
			 * enregistre les valeurs.
			 */
			if (mode == -1)
				continue;

			// Demande de chargement
			if (!this.decryptAndSaveLine(line, mode))
				BiomorphLauncher
						.writeError(GestOption.getString("GestOption.27") //$NON-NLS-1$
								+ line + GestOption.getString("GestOption.28") + mode); //$NON-NLS-1$

		} while (line != null);
		BiomorphLauncher.writeAction(GestOption.getString("GestOption.29")); //$NON-NLS-1$
	}

	/**
	 * <p>
	 * Charge une ligne de donnée de type [vue|couleur|option|connexion] (le
	 * type est à indiquer en argument) et l'ajout au tableau correspondant en
	 * static dans BiomorphLauncher.
	 * </p>
	 * <p>
	 * Renvoie vrai si le chargement à réussi, faux pour tout autre raison
	 * d'échec.
	 * </p>
	 * 
	 * @param ligne
	 *            Ligne à décrypter
	 * @param type
	 *            Type de la ligne (0=vue, 1=couleur, 2=option)
	 */
	public boolean decryptAndSaveLine(final String ligne, final int type) {
		match = detectInfo[type].matcher(ligne);

		if (!match.find()) {
			// la ligne ne correspond pas
			return false;
		}

		switch (type) {
		// 0 : Chargement d'un VUE
		case 0:
			// détection des arguments
			int nb;
			double[] tabArgs;
			matchSecond = detectInfo[4].matcher(match.group(15));
			nb = 0; // d'abord on compte pour connaître la T du tableau
			while (matchSecond.find())
				nb++;
			tabArgs = new double[nb];
			nb = 0; // puis on ajoute dans le nouveau tableau
			matchSecond = detectInfo[4].matcher(match.group(15));
			while (matchSecond.find())
				tabArgs[nb++] = Double.parseDouble(matchSecond.group(1));

			// ajout dans le tableau des vues
			try {
				int id = Integer.parseInt(match.group(1));
				int idCol = Integer.parseInt(match.group(4));
				if (id == -1 || idCol == -1) { // cas d'importation
					id = BiomorphLauncher.tVue.getLastId() + 1;
					idCol = BiomorphLauncher.tCol.getLastId() + 1;
					// System.out.println("id=" + id + " && idCol=" + idCol);
				}
				BiomorphLauncher.tVue.ajouter(id, new StructSaveVue(match
						.group(2), match.group(3), idCol, new Color(Integer
						.parseInt(match.group(5))), new Color(Integer
						.parseInt(match.group(6))), new Color(Integer
						.parseInt(match.group(7))),
						match.group(8).equals("ON"), Integer.parseInt(match //$NON-NLS-1$
								.group(9)),
						Double.parseDouble(match.group(10)), Double
								.parseDouble(match.group(11)), Double
								.parseDouble(match.group(12)), Integer
								.parseInt(match.group(13)), Integer
								.parseInt(match.group(14)), tabArgs));
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		case 1:
			int idCol;
			idCol = Integer.parseInt(match.group(1));
			if (idCol == -1) { // import
				idCol = BiomorphLauncher.tCol.getLastId() + 1;
				// System.out.println("idCol=" + idCol);
			}
			// ajout des données globales du preset
			BiomorphLauncher.tCol.ajouter(idCol, match.group(2), Integer
					.parseInt(match.group(3)));
			match = detectInfo[3].matcher(match.group(4));
			while (match.find()) {
				// Ajout d'une couleur au dégradé du preset
				BiomorphLauncher.tCol.addColorData(idCol, Integer
						.parseInt(match.group(1)), Float.parseFloat(match
						.group(2)), Float.parseFloat(match.group(3)));

			}
			break;
		case 2:
			// System.out.println(match.group(1)+" "+match.group(2));
			// valeur booléenne ?
			if (match.group(2).equals("on")) //$NON-NLS-1$
				BiomorphLauncher.tOpt.ajouter(match.group(1), true, (byte) 0);
			else if (match.group(2).equals("off")) //$NON-NLS-1$
				BiomorphLauncher.tOpt.ajouter(match.group(1), false, (byte) 0);
			else
				// valeur entière ?
				try {
					int b = Integer.parseInt(match.group(2));
					BiomorphLauncher.tOpt.ajouter(match.group(1), b, (byte) 1);
				} catch (NumberFormatException e) {
					// sinon c'est du texte
					if (match.group(3) != null)
						BiomorphLauncher.tOpt.ajouter(match.group(1), match
								.group(3), (byte) 2);
					else
						return false;
				}

			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * Retourne le type de donnée de la ligne selon sa syntaxe :
	 * <ul>
	 * <li>0 : une vue</li>
	 * <li>1 : un dégradé de couleurs</li>
	 * <li>2 : une option</li>
	 * <li>-1 : non reconnu</li>
	 * </ul>
	 * 
	 * @param line
	 *            Ligne lue
	 * @return type de la ligne
	 */
	public int getType(String line) {
		for (int i = 0; i <= 2; i++) {
			match = detectInfo[i].matcher(line);
			if (match.find())
				return i;
		}
		return -1;
	}

	/**
	 * Crée le fichier d'option par défaut au cas où il n'existe pas ou que la
	 * fonction est explicitement appelée : dans ce cas l'ancien fichier est
	 * remplacé.
	 * 
	 * @throws IOException
	 */
	public void createInitConfigFile() throws IOException {
		BiomorphLauncher
				.writeProcedure(GestOption.getString("GestOption.33")); //$NON-NLS-1$

		// on supprimer le fichier s'il existe déjà
		File f = new File(BiomorphLauncher.configPath);
		if (f.exists())
			f.delete();

		// String s =
		// BiomorphLauncher.class.getResource("/compil.sh").toString(); s =
		// s.substring(5, s.length() - 9);

		PrintWriter sortie = new PrintWriter(new FileWriter(
				BiomorphLauncher.configPath));
		sortie
				.println("\n" //$NON-NLS-1$
						+ "\t###########################################################################\n" //$NON-NLS-1$
						+ "\t###  Fichier de configuration du programme BiomorphesCalculator 0.1     ###\n" //$NON-NLS-1$
						+ "\t###      Programme écrit par Patrick Portal et Fabrice Antoine          ###\n" //$NON-NLS-1$
						+ "\t### Premier projet de Génie logiciel en Java License L2-S3-Informatique ###\n" //$NON-NLS-1$
						+ "\t###               Université de Cergy Pontoise 2009-2010                ###\n" //$NON-NLS-1$
						+ "\t###########################################################################\n" //$NON-NLS-1$
						+ "\n\n---------- Mémorisation coordonnées ----------\n" //$NON-NLS-1$
						+ "$>id=8 ~name=\"Julia_dftVue\" -fract=Julia £ColorRamp=1 -mainColors{bg:-1;-1_&_-16777216-alt=OFF} #iter=600 €coord={scl:0.002900763358778626-posX:-1.3-posY:-1.010142493638677__initSize=786x674} -_-extraFractArg={[2.0][-0.745][0.1]}\n" //$NON-NLS-1$
						+ "$>id=7 ~name=\"Julia_C=-1.26-0.023i\" -fract=Julia £ColorRamp=1 -mainColors{bg:-1;-1_&_-16777216-alt=OFF} #iter=100 €coord={scl:0.0038872992723788078-posX:-1.6652746242587915-posY:-1.4075470737913487__initSize=786x674} -_-extraFractArg={[2.0][-1.2625][-0.02325]}\n" //$NON-NLS-1$
						+ "$>id=6 ~name=\"Julia_C=0.32-0.033i\" -fract=Julia £ColorRamp=1 -mainColors{bg:-1;-1_&_-16777216-alt=OFF} #iter=150 €coord={scl:0.0038872992723788078-posX:-1.5214445511807757-posY:-1.3531248839780454__initSize=786x674} -_-extraFractArg={[2.0][0.321875][-0.0328125]}\n" //$NON-NLS-1$
						+ "$>id=5 ~name=\"Radiolaire12Pointes_Formation\" -fract=BiomorpheSimple £ColorRamp=5 -mainColors{bg:-1;-1_&_-16777216-alt=OFF} #iter=20 €coord={scl:0.0016899067876019235-posX:-0.30124861864462665-posY:-1.268297498482481__initSize=732x696} -_-extraFractArg={[0.6][1.0]}\n" //$NON-NLS-1$
						+ "$>id=4 ~name=\"Mandelbrot_spiralezoom\" -fract=Mandelbrot £ColorRamp=8 -mainColors{bg:-16777216;-1_&_-16777216-alt=OFF} #iter=960 €coord={scl:9.197590459234971E-7-posX:-0.7468017251262861-posY:-0.1846566074009809__initSize=932x696} -_-extraFractArg={[2.0]}\n" //$NON-NLS-1$
						+ "$>id=3 ~name=\"Mandelbrot_spirale\" -fract=Mandelbrot £ColorRamp=8 -mainColors{bg:-16777216;-1_&_-16777216-alt=OFF} #iter=760 €coord={scl:1.2712224901093377E-5-posX:-0.7965163404003772-posY:-0.16677894955167502__initSize=932x696} -_-extraFractArg={[2.0]}\n" //$NON-NLS-1$
						+ "$>id=2 ~name=\"Mandelbrot_sinusoideBW\" -fract=Mandelbrot £ColorRamp=1 -mainColors{bg:-16777216;-1_&_-16777216-alt=ON} #iter=50 €coord={scl:3.086016696590571E-5-posX:-1.5665743098958997-posY:-0.009476354035422784__initSize=706x627} -_-extraFractArg={[2.0]}\n" //$NON-NLS-1$
						+ "$>id=1 ~name=\"Mandelbrot_carre\" -fract=Mandelbrot £ColorRamp=8 -mainColors{bg:-16777216;-1_&_-16777216-alt=OFF} #iter=1981 €coord={scl:7.828871088489619E-14-posX:0.4434567033084793-posY:-0.3753515035436974__initSize=706x627} -_-extraFractArg={[2.0]}\n" //$NON-NLS-1$
						+ "\t\t#---> Chaque lignes retient la position d'un fractal pour y revenir facilement\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "#-----------------------------------------\n" //$NON-NLS-1$
						+ "---------- Dégradé de couleurs ----------\n" //$NON-NLS-1$
						+ "->id=9 ~name=\"VertRougeJaune\" *madeFor=0 £color={[-16751818, 0.0, 50.0][-1, 100.0, 50.0][-16024499, 23.945784, 50.0][-6061053, 17.168674, 50.0][-1861623, 48.192772, 50.0][-4008788, 62.198795, 50.0][-8389019, 92.77108, 50.0][-977304, 75.60241, 50.0][-701817, 58.13253, 50.0][-2143414, 44.42771, 50.0][-5119643, 35.542168, 50.0][-14722150, 8.584337, 50.0][-2916449, 70.481926, 50.0][-1012962, 83.885544, 50.0]}\n" //$NON-NLS-1$
						+ "->id=8 ~name=\"VertRougeBleu\" *madeFor=0 £color={[-1, 100.0, 50.0][-1495471, 13.343557, 50.0][-16224113, 54.754604, 50.0][-1314208, 0.0, 50.0][-16658961, 48.312885, 50.0][-6683838, 3.504043, 50.0][-1147363, 19.018818, 50.0][-5938864, 23.92473, 50.0][-9697760, 30.846775, 50.0][-5805880, 40.524193, 50.0][-5505683, 44.153225, 50.0][-5908196, 51.814514, 50.0][-1170933, 57.19086, 50.0][-12308993, 64.24731, 50.0][-3132863, 67.80914, 50.0][-3373824, 73.185486, 50.0][-13549138, 33.803764, 50.0][-3866339, 28.696236, 50.0][-135679, 19.69086, 50.0][-15512930, 7.9301076, 50.0][-2937591, 5.9811826, 50.0][-16681007, 1.8145161, 50.0][-14601769, 26.545698, 50.0][-5298652, 10.752688, 50.0][-1730, 11.626344, 50.0][-5758796, 2.6209676, 50.0][-8892727, 15.053764, 50.0][-1518719, 22.446236, 50.0][-12437361, 14.045699, 50.0][-4639865, 12.567204, 50.0][-9398519, 9.206989, 50.0][-11912808, 7.123656, 50.0][-356864, 6.5860214, 50.0][-16314416, 5.107527, 50.0][-12282727, 0.8736559, 50.0][-1013967, 37.701614, 50.0][-4787844, 35.61828, 50.0][-16213138, 39.448925, 50.0][-15129500, 42.069893, 50.0][-4058698, 45.295696, 50.0][-1665286, 46.70699, 50.0][-974239, 49.731182, 50.0][-9130841, 50.672043, 50.0][-2863750, 52.82258, 50.0][-15905720, 55.91398, 50.0][-1959375, 58.80376, 50.0][-336853, 60.8871, 50.0][-16341335, 62.701614, 50.0][-9878828, 65.860214, 50.0][-6702393, 66.66667, 50.0][-2117119, 70.96774, 50.0][-10003260, 69.42204, 50.0][-2243924, 78.76344, 50.0][-94137, 75.604836, 50.0][-2283953, 85.41667, 50.0][-11600399, 82.39247, 50.0][-2078580, 88.70968, 50.0][-8999405, 91.06183, 50.0][-6696890, 96.57258, 50.0][-13921207, 98.31989, 50.0][-7177009, 93.07796, 50.0][-16740919, 94.82527, 50.0][-10096498, 86.76075, 50.0][-12758361, 83.53494, 50.0][-5658900, 79.435486, 50.0][-1928966, 81.048386, 50.0][-1151375, 80.30914, 50.0][-14418957, 78.09139, 50.0][-1881188, 74.59677, 50.0][-5770370, 71.370964, 50.0][-4563990, 70.49731, 50.0][-10687615, 63.44086, 50.0][-793831, 61.89516, 50.0][-14849118, 59.81183, 50.0][-15690081, 53.69624, 50.0][-8076050, 32.594086, 50.0][-3773656, 20.497313, 50.0][-8473971, 18.145163, 50.0][-7817491, 16.330645, 50.0][-4031421, 17.002687, 50.0][-11610395, 17.540321, 50.0][-8441882, 26.075268, 50.0][-14976972, 24.663979, 50.0][-11120056, 91.86828, 50.0][-6238855, 93.61559, 50.0][-10327819, 95.900536, 50.0][-4609663, 97.24463, 50.0][-11346211, 90.053764, 50.0][-6199110, 89.314514, 50.0][-7346185, 87.56721, 50.0][-13750149, 84.27419, 50.0][-4238164, 84.81183, 50.0][-13215806, 76.6129, 50.0][-16697469, 81.58602, 50.0][-13470829, 77.35215, 50.0][-1236494, 64.91935, 50.0][-687903, 68.75, 50.0][-570112, 72.17742, 50.0][-4650850, 29.368279, 50.0][-7483987, 27.688173, 50.0][-240774, 21.57258, 50.0][-7601677, 23.185484, 50.0]}\n" //$NON-NLS-1$
						+ "->id=7 ~name=\"DégradéRouge\" *madeFor=0 £color={[-5242352, 0.0, 50.0][-1, 100.0, 50.0]}\n" //$NON-NLS-1$
						+ "->id=5 ~name=\"Couleurs+\" *madeFor=0 £color={[-1, 100.0, 50.0][-1495471, 13.343559, 50.0][-14859827, 0.0, 50.0][-16658961, 48.312885, 50.0][-8444208, 82.99731, 50.0][-4497375, 23.567707, 50.0][-12022985, 34.765625, 50.0][-10842467, 4.6875, 50.0][-7629941, 59.244793, 50.0][-3198618, 72.52605, 50.0]}\n" //$NON-NLS-1$
						+ "->id=4 ~name=\"Arc-en-ciel\" *madeFor=0 £color={[-5242352, 0.0, 50.0][-5376, 21.484375, 50.0][-14490107, 42.578125, 50.0][-16637736, 68.8802, 50.0][-9450242, 59.375, 50.0][-654932, 85.41667, 50.0][-65794, 100.0, 50.0]}\n" //$NON-NLS-1$
						+ "->id=3 ~name=\"Example2\" *madeFor=0 £color={[-1, 100.0, 50.0][-1495471, 13.343557, 50.0][-16224113, 54.754604, 50.0][-1314208, 0.0, 50.0][-16658961, 48.312885, 50.0][-8444208, 82.36196, 50.0]}\n" //$NON-NLS-1$
						+ "->id=2 ~name=\"Example1\" *madeFor=0 £color={[-1, 100.0, 50.0][-8969458, 2.3809524, 50.0][-5722847, 44.49405, 50.0][-6319163, 0.0, 50.0][-7800966, 78.422615, 50.0]}\n" //$NON-NLS-1$
						+ "->id=1 ~name=\"default\" *madeFor=0 £color={[-16777176, 0.0, 50.0][-9795136, 34.0, 50.0][-12164490, 56.0, 50.0][-1, 100.0, 50.0]}\n" //$NON-NLS-1$
						+ "\t\t#---> Chaque lignes retient la position d'un fractal pour y revenir facilement\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "#-----------------------------------------\n" //$NON-NLS-1$
						+ "---------- Options du logiciel ----------\n" //$NON-NLS-1$
						+ "#---> OPTIONS D'AFFICHAGE ET D'ACCESSIBILITÉ <-----\n" //$NON-NLS-1$
						+ "\t#-------> Affichage interface globale\n" //$NON-NLS-1$
						+ "menuBar on			# Fichier/ edition/...\n" //$NON-NLS-1$
						+ "toolBars on			# Afficher barres d'outils\n" //$NON-NLS-1$
						+ "planPan on			# Panneau d'infos à la navigation dans le plan (situration, angle, coordonnées)\n" //$NON-NLS-1$
						+ "equationEdit on  		# Panneau d'équation (choix du fractal)\n" //$NON-NLS-1$
						+ "colorPan on  			# Editeur de couleurs\n" //$NON-NLS-1$
						+ "stateBar on			# Affichage barre d'état et avancement\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\t#-------> Affichage barre d'outils\t # Barre d'outils individuelles\n" //$NON-NLS-1$
						+ "tbHisto on			# Navigation (prec., suiv., ...)\n" //$NON-NLS-1$
						+ "tbStatut on			# Statut du rendu (lecture, pause, actyaliser)\n" //$NON-NLS-1$
						+ "tbTools off			# Icônes pour manipuler le fractale à la souris sans raccourcis clavier\n" //$NON-NLS-1$
						+ "tbIter on  			# Nombre d'itération + auto\n" //$NON-NLS-1$
						+ "tbColor on  			# Sélection des couleurs\n" //$NON-NLS-1$
						+ "tbColorAdv off	  	# Outils avancés pour l'édition par dégradés de couleurs (fixer itération)\n" //$NON-NLS-1$
						+ "tbValid on			# Précision du calcul + bouton validaton\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\t#-------> Option chargement par défaut et langue\n" //$NON-NLS-1$
						+ "language \"fr\"				# fr ou en, ou sinon tout sera mis en fr\n" //$NON-NLS-1$
						+ "dftFractal \"Mandelbrot\"		# fractal par défaut à l'ouverture du logiciel\n" //$NON-NLS-1$
						+ "dftColRamp 0					# Dégradé de couleurs par défaut (<=0 pour par défaut) par son ID\n" //$NON-NLS-1$
						+ "dftCsteIt 100	  			# Constante d'itération fixe (pour ne pas toujours réadapter les couleurs)\n" //$NON-NLS-1$
						+ "infos 1		  			# Information DidYouKnow (<=0 pour désactiver)\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\t#-------> Fenêtre\n" //$NON-NLS-1$
						+ "fullScreen off			# Maximise par défaut la fenêtre\n" //$NON-NLS-1$
						+ "horizSize 850			# Longueur (en pixels) de la fenêtre (si fullScreen désactivé)\n" //$NON-NLS-1$
						+ "vertSize 680			# Hauteur ...........\n" //$NON-NLS-1$
						+ "boundX 200			# Décalage horizontal (en pixels) depuis le coin supérieur gauche de votre écran\n" //$NON-NLS-1$
						+ "boundY 100			# ........ vertical\n" //$NON-NLS-1$
						+ "windowStyle \"system\" 	# vous pouvez utiliser :\n" //$NON-NLS-1$
						+ "					# \"default\" pour style swing java, \n" //$NON-NLS-1$
						+ "					# \"system\" pour le lême style de fenêtres que votre système, \n" //$NON-NLS-1$
						+ "					# \"look&feel\" pour un thème de UIManager\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "#-----------------------------------------\n" //$NON-NLS-1$
						+ "\t#---> MOTEUR DE RENDU/ THREADS/ OPT FRACTALES <-----\n" //$NON-NLS-1$
						+ "nbThread 0		# Le nombre de partie sur l'image qui vont se calculer. 0 pour le mode 'automatique'\n" //$NON-NLS-1$
						+ "progressive off	# si activé : le rendu sera de plus en plus net sinon ce sera un calcul linéaire\n" //$NON-NLS-1$
						+ "verticalRender on	# Sur on, le rendu se fera de la gauche vers la droite, sinon du haut vers le bas\n" //$NON-NLS-1$
						+ "precision 1		# 1 = tous les pixels calculés, 2 pour 1/4, 3 pour 1/8, 4 pour 1/16 etc \n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "\n" //$NON-NLS-1$
						+ "#-----------------------------------------\n" //$NON-NLS-1$
						+ "\t#---> MACRO POUR RENDU DE SUITE D'IMAGES\n" //$NON-NLS-1$
						+ "\t#/!\\ configuration complexe, utiliser interface /!\\ <-----" //$NON-NLS-1$
						+ "\t# L'éditeur de macro permet de faire un rendu d'un point A à un point B en modifiant linéairement les valeurs\n" //$NON-NLS-1$
						+ "\t# Les options de macros commencent par M\n" //$NON-NLS-1$
						+ "MposXinit \"0.5\" 			# Pos X initiales coin sup gauche\n" //$NON-NLS-1$
						+ "MposXfin \"0.5\" 			#       finale (identique pour ne pas bouger)\n" //$NON-NLS-1$
						+ "MposYinit \"0.2\"			#     Y init\n" //$NON-NLS-1$
						+ "MposYfin \"0.2\"			#       finale\n" //$NON-NLS-1$
						+ "Mscaleinit \"0.1\"			#     echelle init\n" //$NON-NLS-1$
						+ "Mscalefin \"0.1\"			#             finale\n" //$NON-NLS-1$
						+ "MiterInit 560			# nb itér initiale\n" //$NON-NLS-1$
						+ "MiterFin 560				#         finale\n" //$NON-NLS-1$
						+ "Mvar1offs 1 				# position dans le tableau de variable fractale 1 à animer (négatif si inutile)\n" //$NON-NLS-1$
						+ "Mvar1init \"0.745\"		# valeur variable 1 initiale\n" //$NON-NLS-1$
						+ "Mvar1fin \"0.423\"		#                   finale\n" //$NON-NLS-1$
						+ "Mvar2offs -1 			# position dans le tableau de variable fractale 1 à animer (négatif si inutile)\n" //$NON-NLS-1$
						+ "Mvar2init \"0.745\"		# valeur variable 2 initiale\n" //$NON-NLS-1$
						+ "Mvar2fin \"0.423\"		#                   finale\n" //$NON-NLS-1$
						+ "MmainVar \"Mvar1\"		# Variable sur lequel se basera l'animation et le pas de rendu. Déterminera aussi le nombre d'images\n" //$NON-NLS-1$
						+ "MrenderPas \"0.02\"			# Pas d'animation pour la variable principale\n" //$NON-NLS-1$
						+ "MimgName \"image\"			# Préfixe des images de la suite \n" //$NON-NLS-1$

						+ "\n"); //$NON-NLS-1$
		sortie.close();
		BiomorphLauncher
				.writeAction(GestOption.getString("GestOption.9")); //$NON-NLS-1$
	}

	/**
	 * <p>
	 * Modifie une option par la valeur en argument dans le fichier de
	 * configuration. Il est impossible d'ajouter ou de détruire une option,
	 * elles ne sont pas faîtes pour ça.
	 * </p>
	 * <p>
	 * Si jamais l'utilisateur détruit des options, le moyen de réparer les
	 * dégâts est de remplacer le fichier de config par celui par défaut.
	 * </p>
	 * 
	 * @param nom
	 *            Nom de l'option
	 * @param val
	 *            Valeur
	 */
	public void changeOption(final String nom, final Object val) {
		String newLine = null;
		Pattern recupEnd = Pattern.compile("(\\s*[^\\\"]#.*)|()$"); //$NON-NLS-1$

		// si c'est un booleen on changee sur on/ off
		if (val.toString().equals("true") || val.toString().equals("false")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (val.toString().equals("true")) //$NON-NLS-1$
				newLine = nom + " on"; //$NON-NLS-1$
			else
				newLine = nom + " off"; //$NON-NLS-1$
		} else
			newLine = nom + " " + val; //$NON-NLS-1$
		// écriture du fichier
		try {
			// en terme de flux
			// fileIn = new InputStreamReader(str); BufferedReader br = new
			// BufferedReader(this.fileIn);

			// Paramétrage entrée
			File entree = new File(BiomorphLauncher.configPath);
			BufferedReader br = new BufferedReader(new FileReader(
					BiomorphLauncher.configPath));
			// Paramétrage sortie
			File sortie = new File("temp"); //$NON-NLS-1$
			BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));

			// variable de lecture
			String line = null;
			boolean modif = false;
			while ((line = br.readLine()) != null) {
				// ligne concernée -> modification
				if (line.startsWith(nom) && !modif) {
					Matcher m = recupEnd.matcher(line);
					if (m.find())
						bw.write(newLine + m.group(0) + "\n"); //$NON-NLS-1$
					else
						bw.write(newLine + "\n"); //$NON-NLS-1$
					bw.flush();
					modif = true;
				} else { // sinon on la recopie
					bw.write(line + "\n"); //$NON-NLS-1$
					bw.flush();
				}
			}

			// en terme de flux
			// URL u = System.class.getResource("/config");
			// File f = new File(u.getFile());
			// sortie.renameTo(f);

			// Traitement après écriture
			bw.close();
			br.close();
			entree.delete();
			sortie.renameTo(new File(BiomorphLauncher.configPath));

			// Affichage
			if (modif)
				;// BiomorphLauncher.writeAction("L'option " + nom
			// + " a été mise à jour dans le "
			// + "fichier de configuration");
			else
				BiomorphLauncher.writeError(GestOption.getString("GestOption.152") + nom //$NON-NLS-1$
						+ GestOption.getString("GestOption.153") //$NON-NLS-1$
						+ GestOption.getString("GestOption.154") //$NON-NLS-1$
						+ GestOption.getString("GestOption.155")); //$NON-NLS-1$

		} catch (FileNotFoundException e) {
			BiomorphLauncher
					.writeError(GestOption.getString("GestOption.156")); //$NON-NLS-1$
		} catch (IOException e) {
			BiomorphLauncher
					.writeError(GestOption.getString("GestOption.157")); //$NON-NLS-1$
		}
	}

	/**
	 * Ajoute un preset de couleur au fichier de configuration.A partir de ces
	 * deux arguments vous pouvez : </p>
	 * <ul>
	 * <li><b>Ajouter une nouvelle couleur</b> : il faut mettre un
	 * <u>identifiant négatif</u></li>
	 * <li><b>Modifier une couleur</b> : il faut mettre l'identifiant de la
	 * couleur à modifier.</li>
	 * </ul>
	 * 
	 * @param id
	 *            Identifiant numérique (id) du dégradé de couleur
	 * @param nom
	 *            Surnom du dégradé
	 * @param madeFor
	 *            Id (non précisé = -1) de la vue attribuée
	 * @param crc
	 *            Objet ChooseRampColor
	 */
	public void manageColor(final int id, final String nom, final int madeFor,
			final ChooseRampColor crc) {
		String newLine = null;
		Pattern recupEnd = Pattern.compile("(\\s*[^\\\"]#.*)|()$"); //$NON-NLS-1$
		int setId = id;
		ColorAdvanced[] ca = crc.getArrayOfColors();
		boolean ajout = false;

		if (id < 0) { // nouveau : on l'ajoute dans tCol
			setId = BiomorphLauncher.tCol.getLastId() + 1;
			ajout = true;
			BiomorphLauncher.tCol.ajouter(setId, nom, madeFor, crc);
		}
		newLine = "->id=" + setId + " ~name=\"" + nom + "\" *madeFor=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ madeFor + " £color={"; //$NON-NLS-1$

		for (ColorAdvanced c : ca)
			newLine += "[" + c.getColor().getRGB() + ", " + c.getPosition() //$NON-NLS-1$ //$NON-NLS-2$
					+ ", " + c.getPoid() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		newLine += "}"; //$NON-NLS-1$

		// réécriture du fichier
		try {
			// Paramétrage entrée
			File entree = new File(BiomorphLauncher.configPath);
			BufferedReader br = new BufferedReader(new FileReader(
					BiomorphLauncher.configPath));
			// Paramétrage sortie
			File sortie = new File("temp"); //$NON-NLS-1$
			BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));

			// variable de lecture
			String line = null;
			boolean modif = false;
			while ((line = br.readLine()) != null) {
				// ajout : on ajoute la ligne après repaire ------ def ------
				if (!modif && ajout && line.startsWith(strToUpMode[1])) {
					bw.write(line + "\n"); //$NON-NLS-1$
					bw.write(newLine + "\n"); //$NON-NLS-1$
					modif = true;
				} // modification couleur
				else if (!modif && ajout && line.startsWith("->id=" + id + " ")) { //$NON-NLS-1$ //$NON-NLS-2$
					Matcher m = recupEnd.matcher(line);
					bw.write(newLine + ((m.find()) ? m.group(0) : "") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					modif = true;
				} else
					// sinon on la recopie
					bw.write(line + "\n"); //$NON-NLS-1$
				bw.flush();
			}
			// Traitement après écriture
			bw.close();
			br.close();
			entree.delete();
			sortie.renameTo(new File(BiomorphLauncher.configPath));

			//  Notification
			if (modif)
				BiomorphLauncher.writeAction(GestOption.getString("GestOption.176") + nom //$NON-NLS-1$
						+ GestOption.getString("GestOption.177")); //$NON-NLS-1$
			else
				BiomorphLauncher
						.writeError(GestOption.getString("GestOption.178") //$NON-NLS-1$
								+ nom
								+ GestOption.getString("GestOption.179")); //$NON-NLS-1$

		} catch (FileNotFoundException e) {
			BiomorphLauncher
					.writeError(GestOption.getString("GestOption.180")); //$NON-NLS-1$
		} catch (IOException e) {
			BiomorphLauncher
					.writeError(GestOption.getString("GestOption.181")); //$NON-NLS-1$
		}
	}

	/**
	 * <p>
	 * Ajoute une vue dans le fichier de configuraton à partir d'un identifiant
	 * et d'un objet StructSaveVue. A partir de ces deux arguments vous pouvez :
	 * </p>
	 * <ul>
	 * <li><b>Ajouter une nouvelle vue</b> : il faut mettre un <u>identifiant
	 * négatif</u></li>
	 * <li><b>Modifier une vue</b> : il faut mettre un identifiant qui existe.</li>
	 * </ul>
	 * 
	 * @param id
	 *            Identifiant numérique de la vue (négatif pour nouveau)
	 * @param ssv
	 *            Structure de stockage de vue
	 */
	public void manageVue(final int id, final StructSaveVue ssv) {
		String newLine = null;
		Pattern recupEnd = Pattern.compile("(\\s*[^\\\"]#.*)|()$"); //$NON-NLS-1$
		int setId = id;
		boolean ajout = false;

		if (id < 0) { // nouveau : on l'ajoute dans tVue
			setId = BiomorphLauncher.tVue.getLastId() + 1;
			ajout = true;
			BiomorphLauncher.tVue.ajouter(setId, ssv);
		}
		newLine = ssv.getFormattedConfigLine(setId);

		// réécriture du fichier
		try {
			// Paramétrage entrée
			File entree = new File(BiomorphLauncher.configPath);
			BufferedReader br = new BufferedReader(new FileReader(
					BiomorphLauncher.configPath));
			// Paramétrage sortie
			File sortie = new File("temp"); //$NON-NLS-1$
			BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));

			// variable de lecture
			String line = null;
			boolean modif = false;
			while ((line = br.readLine()) != null) {
				// ajout : on ajoute la ligne après repaire ------ def ------
				if (!modif && ajout && line.startsWith(strToUpMode[0])) {
					bw.write(line + "\n"); //$NON-NLS-1$
					bw.write(newLine + "\n"); //$NON-NLS-1$
					modif = true;
				} // modification couleur
				else if (!modif && ajout && line.startsWith("->id=" + id + " ")) { //$NON-NLS-1$ //$NON-NLS-2$
					Matcher m = recupEnd.matcher(line);
					bw.write(newLine + ((m.find()) ? m.group(0) : "") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					modif = true;
				} else
					// sinon on la recopie
					bw.write(line + "\n"); //$NON-NLS-1$
				bw.flush();
			}
			// Traitement après écriture
			bw.close();
			br.close();
			entree.delete();
			sortie.renameTo(new File(BiomorphLauncher.configPath));

			//  Notification
			if (modif)
				BiomorphLauncher.writeAction(GestOption.getString("GestOption.191") + ssv.getName() //$NON-NLS-1$
						+ GestOption.getString("GestOption.192") //$NON-NLS-1$
						+ GestOption.getString("GestOption.193")); //$NON-NLS-1$
			else
				BiomorphLauncher
						.writeError(GestOption.getString("GestOption.194") //$NON-NLS-1$
								+ ssv.getName()
								+ GestOption.getString("GestOption.195")); //$NON-NLS-1$

		} catch (FileNotFoundException e) {
			BiomorphLauncher
					.writeError(GestOption.getString("GestOption.196")); //$NON-NLS-1$
		} catch (IOException e) {
			BiomorphLauncher
					.writeError(GestOption.getString("GestOption.197")); //$NON-NLS-1$
		}
	}

	// outils pour les regex
	private final String[] strToUpMode = {
			"---------- Mémorisation coordonnées ----------", //$NON-NLS-1$
			"---------- Dégradé de couleurs ----------", //$NON-NLS-1$
			"---------- Options du logiciel ----------" }; //$NON-NLS-1$
	final private Pattern[] detectInfo = {
			Pattern
					.compile("^\\s*\\$>id=([+-]?\\d+)\\s+~name=\"(.+)\"\\s*\\-fract=([a-zA-Z0-9]+)\\s*£ColorRamp=([+-]?\\d+) -mainColors\\{bg:([-+]?\\d+);([-+]?\\d+)_&_([-+]?\\d+)-alt=((?:OFF)|(?:ON))\\} #iter=(\\d+) €coord=\\{scl:([-+\\dE\\.]+)-posX:([-+\\dE\\.]+)-posY:([-+\\dE\\.]+)__initSize=(\\d+)x(\\d+)\\} \\-_\\-extraFractArg=\\{((?:\\[[-+\\dE\\.]+\\])*)\\}"), //$NON-NLS-1$
			Pattern
					.compile("^\\s*->id=([+-]?\\d+)\\s+~name=\"(.+)\"\\s*\\*madeFor=(\\-?\\d+)\\s*£color=\\{((?:\\[[+-]{0,1}\\d+,\\s*[\\d\\.]+,\\s*[\\d\\.]+\\])+)\\}"), //$NON-NLS-1$
			Pattern.compile("^([A-z0-9\\_]+)\\s+(on|off|\"(.+)\"|-?[0-9]+)"), //$NON-NLS-1$
			// Ce pattern est pour le formatage des couleurs
			Pattern
					.compile("\\[([+-]{0,1}\\d+),\\s*([\\d\\.]+),\\s*([\\d\\.]+)\\]"), //$NON-NLS-1$
			// ce dernier pr la reconnaissance des tab d'arguments des vues
			Pattern.compile("\\[([-+\\dE\\.]+)\\]") }; //$NON-NLS-1$
	private Matcher match, matchSecond;

	// pour retenir facilement les modes depuis l'extérieur
	final public static int VUE = 0, COULEUR = 1, OPTION = 2;
}
