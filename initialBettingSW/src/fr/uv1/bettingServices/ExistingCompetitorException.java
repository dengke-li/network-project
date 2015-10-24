package fr.uv1.bettingServices;

public class ExistingCompetitorException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExistingCompetitorException() {
		super();
	}

	public ExistingCompetitorException(String reason) {
		super(reason);
	}
}
