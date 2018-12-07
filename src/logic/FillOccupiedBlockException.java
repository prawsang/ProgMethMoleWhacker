package logic;

public class FillOccupiedBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FillOccupiedBlockException() {}
	
	public FillOccupiedBlockException(String message) {
		super (message);
	}

	public FillOccupiedBlockException(Throwable cause) {
		super (cause);
	}

	public FillOccupiedBlockException(String message, Throwable cause) {
		super (message, cause);
	}	
}
