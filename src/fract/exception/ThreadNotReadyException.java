package fract.exception;

/**
 * <p>
 * Exception lancée lorsque des threads n'ont pas pu être lancés sur une cause
 * quelconque (dans le message).
 * </p>
 */
final public class ThreadNotReadyException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Constructeur héritant de Exception, message personnalisé en argument
	 * (assessible avec la méthode getMessage()).
	 * </p>
	 * 
	 * @param msg
	 *            Message d'erreur
	 */
	public ThreadNotReadyException(String msg) {
		super(msg);
	}
}
