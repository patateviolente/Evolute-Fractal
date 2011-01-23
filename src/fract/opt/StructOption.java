package fract.opt;

/**
 * <p>
 * Classe de stockage d'une option. Cette structure est unique à une seule
 * option. La donnée est enregistrée sous la classe Object mais le type est
 * retenue par un entier <i>(fixé par le logiciel, dépend de l'utilisation)</i>.
 * </p>
 */
final public class StructOption {
	/**
	 * <p>
	 * Crée une nouvelle structure de rangement d'option de tout types de class
	 * héritant d'Object (tous...).
	 * </p>
	 * 
	 * @param value
	 *            Valeur (tout types)
	 * @param type
	 *            Type de la valeur (0=boolean, 1=entier, 2=String)
	 */
	public StructOption(final Object value, final byte type) {
		this.value = value;
		this.type = type;
	}

	/**
	 * @return un entier définissant le type de l'Objet.
	 */
	public byte getType() {
		return this.type;
	}

	/**
	 * Change la valeur de l'objet par le paramètre.
	 * 
	 * @param newValue
	 *            Nouvelle valeur
	 */
	public void setValue(final Object newValue) {
		this.value = newValue;
	}

	/**
	 * @return la valeur stockée (Object).
	 */
	public Object getValue() {
		return this.value;
	}

	// variables de stockage
	private Object value;
	private byte type;
}
