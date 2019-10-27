package execption;

public class ParkingException extends Exception {

	private static final long serialVersionUID = -3552275262672621625L;

	private String errorCode = null; 
	private Object[] errorParameters = null;

	public ParkingException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ParkingException(String message) {
		super(message);
	}

	 
	public ParkingException(Throwable throwable) {
		super(throwable);
	}

	
	public ParkingException(String errorCode, String message, Object[] errorParameters) {
		super(message);
		this.setErrorCode(errorCode);
		this.setErrorParameters(errorParameters);
	}

	/**
	 * @param errorCode
	 * @param message
	 * @param throwable
	 */
	public ParkingException(String errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.setErrorCode(errorCode);
	}

	/**
	 * @param errorCode
	 * @param message
	 * @param errorParameters
	 * @param throwable
	 */
	public ParkingException(String errorCode, String message, Object[] errorParameters, Throwable throwable) {
		super(message, throwable);
		this.setErrorCode(errorCode);
		this.setErrorParameters(errorParameters);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Object[] getErrorParameters() {
		return errorParameters;
	}

	public void setErrorParameters(Object[] errorParameters) {
		this.errorParameters = errorParameters;
	}

}
