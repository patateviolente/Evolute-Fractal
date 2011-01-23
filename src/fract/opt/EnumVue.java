package fract.opt;

/**
 * <p>
 * Enumérations pour accéder au champs d'une vue. Pour s'en servir, il faut les
 * entrer dans une fonction du type :
 * </p>
 * <ul>
 * <li><b>getColorValue(EnumVue)</b> -> retourne un type color, valeures
 * possibles <i>[background, alt1, alt2]</i></li>
 * <li><b>getDoubleValue(EnumVue)</b> -> retourne un type double, valeures
 * possibles <i>[scale, posX, posY]</i></li>
 * <li><b>getIntValue(EnumVue)</b> -> retourne un type entier, valeures
 * possibles <i>[idramp, annul, iter, width, height]</i></li>
 * </ul>
 */
public enum EnumVue {
	idramp, annul, iter, width, height, scale, posX, posY, background, alt1, alt2;
}
