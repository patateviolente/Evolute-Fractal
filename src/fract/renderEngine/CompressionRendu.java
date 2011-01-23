package fract.renderEngine;

/**
 * <p>
 * Faible compression par caractères spécialement taillée pour réduire et
 * restauréer une chaîne du type "n;n;n;n;n;k;k;k;k;k;k;l;l;l;k;k;k;..." où n,
 * k, l sont des nombres et les réexpédient en texte de la façon
 * "nx5;k;5;lx3;kx3;...".
 * </p>
 * <p>
 * Plus la répétition est forte et plus la compression sera bonne, donc valable
 * pour des chaînes longues et répétitives...
 * </p>
 * <p>
 * <p>
 * Les fonctions accessibles en static peuvent compresser et décompresser tout
 * simplement les chaînes correpondantes, avec en entrée et sortie une chaîne de
 * caractère.
 * </p>
 */
public class CompressionRendu {
	/**
	 * Compresse la chaîne en entrée <i>(voir description classe pour plus
	 * d'infos sur le type de compressison).</i>
	 * 
	 * @param in
	 *            Chaîne brut
	 * @return chaîne compactée
	 */
	public static String compress(String in) {
		StringBuffer out = new StringBuffer("");
		String[] cut = in.split(";");

		int val = Integer.parseInt(cut[0]); // valeur
		int repet = 1; // retient le nombre de rpétitions

		for (int i = 1; i < cut.length; i++) {
			if (Integer.parseInt(cut[i]) == val)
				repet++;
			else {
				out.append(val + "x" + repet + ";");
				val = Integer.parseInt(cut[i]);
				repet = 1;
			}
		}
		out.append(val + "x" + repet + ";");
		return out.toString();
	}

	/**
	 * Décompresse la chaîne en entrée <i>(voir description classe pour plus
	 * d'infos sur le type de compressison).</i>
	 * 
	 * @param in
	 *            Chaîne compressé
	 * @return chaîne brut
	 */
	public static String uncompress(String in) {
		StringBuffer out = new StringBuffer("");
		String[] cut = in.split(";"), cut2;

		for (int i = 0; i < cut.length; i++) {
			cut2 = cut[i].split("x");
			for (int u = 0; u < Integer.parseInt(cut2[1]); u++)
				out.append(cut2[0] + ";");
		}

		return out.toString();
	}

}
