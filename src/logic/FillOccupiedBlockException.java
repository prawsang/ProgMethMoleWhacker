package logic;

public class FillOccupiedBlockException extends Exception {

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
