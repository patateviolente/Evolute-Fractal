package fract.gen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import fract.BiomorphLauncher;
import fract.ihm.FractalFileGenerator;
import fract.ihm.MainWindow;

import javax.swing.*;

/**
 * Classe de procédures de génération de fichiers.
 */
final public class GenFractalFile {
	public GenFractalFile(MainWindow mw) {
		this.mw = mw;
	}

	/**
	 * @param ffg
	 */
	public void GenSimpleForm(FractalFileGenerator ffg) {
		if (ffg.nom.getText().length() < 3) {
			JOptionPane
					.showMessageDialog(
							ffg,
							"Vous devez donner un  de trois caractères minimum au fichier",
							"Pas de nom", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ffg.nom.setText(ffg.nom.getText().substring(0, 1).toUpperCase()
				+ ffg.nom.getText().substring(1, ffg.nom.getText().length()));

		PrintWriter sortie;
		try {
			sortie = new PrintWriter(new FileWriter("src/fract/algo/"
					+ ffg.nom.getText() + ".java"));

			sortie
					.println("package fract.algo;\n\n"
							+ "import fract.ihm.MainWindow;\n"
							+ "import fract.struct.*;\n" + "\n"
							+ "public class "
							+ ffg.nom.getText()
							+ " extends BaseFractale {\n"
							+ ffg.txtArea[0].getText()
							+ "\n"
							+ "\tpublic "
							+ ffg.nom.getText()
							+ "(MainWindow mw) {\n"
							+ "\t\t// coordonnées par défaut du fractal (le plus loin mais pas trop)\n"
							+ "\t\tsuper(mw, CposX, CposY, CscaleMin, Citerations);\n"
							+ "\t\tinitializeVariablesForXThreads(1); // au moins 1 thread\n"
							+ "\t}\n"
							+ "\t\n"
							+ "\tpublic void adaptDefaultCoordToNewDim(int x, int y) {\n"
							+ "\t\tthis.adaptDefaultCoordToNewDim(x, y, CposX, CposY, CscaleMin);\n"
							+ "\t}\n"
							+ "\t\n"
							+ "\tpublic int getpourcentZoom() {\n"
							+ "\t\treturn this.getpourcentZoom(CscaleMax, CscaleMin);\n"
							+ "\t}\n"
							+ "\t\n"
							+ "\t/*\n"
							+ "\t * #######################################################################\n"
							+ "\t * ------------------------Méthodes de calculs----------------------------\n"
							+ "\t * #######################################################################\n"
							+ "\t */\n"
							+ "\t\n"
							+ "\tpublic void initializeVariablesForXThreads(int n) {\n"
							+ "\t\t"
							+ ffg.txtArea[2].getText()
							+ "\n"
							+ "\t}\n"
							+ "\t\n"
							+ "\tpublic void calculateStepZZero(int t, int x, int y) {\n"
							+ "\t\t"
							+ ffg.txtArea[3].getText()
							+ "\n"
							+ "\t}\n"
							+ "\t\n"
							+ "\tpublic void calculateStepPowAndCst(int t, int i, int x, int y) {\n"
							+ "\t\t"
							+ ffg.txtArea[4].getText()
							+ "\n"
							+ "\t}\n"
							+ "\t\n"
							+ "\tpublic boolean calculateStepGetCondition(int t, int i) {\n"
							+ "\t\t"
							+ ffg.txtArea[5].getText()
							+ "\n"
							+ "\t}\n"
							+ "\t\n"
							+ ffg.txtArea[1].getText()
							+ "\t// variables de calcul\n" + "}\n");
			sortie.close();
			BiomorphLauncher
					.writeAction("Ecriture du premier fichier de configuration (par défaut) réussi");
			this.runCompilation(ffg.nom.getText());
			this.mw.getParams().addFractalToList(true, ffg.nom.getText());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compile la classe
	 */
	private void runCompilation(String className) {
		try {
			Runtime.getRuntime().exec(
					System.getProperty("user.dir") + "/" + script + " "
							+ className);
			System.out.println("run " + System.getProperty("user.dir") + "/"
					+ script + " " + className);
		} catch (IOException e) {
			BiomorphLauncher
					.writeError("Erreur en essayant de compiler le fichier "
							+ (System.getProperty("user.dir") + "/" + script
									+ " " + className));
			e.printStackTrace();
		}
		JOptionPane
				.showMessageDialog(
						mw,
						"La compilation a été lancée.\nS'il n'y avait pas d'erreur de syntaxe, le fichier devrait être chargeable depuis\nla liste.\nNe fermez pas l'éditeur pour pouvoir rééditer le fichier.",
						"Compilation terminée", JOptionPane.INFORMATION_MESSAGE);
	}

	final private MainWindow mw;
	final private String script = "compil.sh";
}
