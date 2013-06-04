package org.bonitasoft.reporting.exception;

public class BonitaReportException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BonitaReportException() {
		super();
	}

	public BonitaReportException(String message) {
		super(message);
	}

	public BonitaReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public BonitaReportException(Throwable cause) {
		super(cause);
	}

}
