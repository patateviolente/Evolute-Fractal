package fract.opt;

/**
 * <p>
 * Classe contenant les différentes énumérations désignant les différentes
 * options en les regroupant par leur type d'options :
 * </p>
 * <ul>
 * <li>Entier <i>(IntEnum)</i></li>
 * <li>Booléen <i>(BooleanEnum)</i></li>
 * <li>Chaîne de caractères <i>(StringEnum)</i></li>
 * </ul>
 */
final public class EnumOption {

	/**
	 * <p>MimgName
	 * Enumération désignant une option renvoyant un entier.
	 * </p>
	 */
	public enum IntEnum {
		infos, horizSize, vertSize, boundX, boundY, nbThread, dftCsteIt, precision, dftColRamp, MiterInit, MiterFin, Mvar1offs, Mvar2offs;
	}

	/**
	 * <p>
	 * Enumération désignant une option renvoyant un type booléen.
	 * </p>
	 */
	public enum BooleanEnum {
		stateBar, menuBar, planPan, equationEdit, colorPan, toolBars, fullScreen, tbHisto, tbStatut, tbTools, tbIter,tbColorAdv, tbColor, tbValid, progressive, verticalRender;
	}

	/**
	 * <p>
	 * Enumération désignant une option renvoyant une chaîne de caractères.
	 * </p>
	 */
	public enum StringEnum {
		windowStyle, language, dftFractal, MimgName, MmainVar, MposXinit, MposXfin, MposYinit, MposYfin, Mscaleinit, Mscalefin, Mvar1init, Mvar1fin, Mvar2init, Mvar2fin, MrenderPas;
	}
}
