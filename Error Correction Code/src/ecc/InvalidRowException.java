package ecc;

//Exception when inserting an invalid row into a matrix
public class InvalidRowException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRowException(String str) {
		super(str);
	}

}
