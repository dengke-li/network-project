package fr.uv1.bettingServices;

public class ExistingCompetitionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExistingCompetitionException() {
		super();
	}

	public ExistingCompetitionException(String reason) {
		super(reason);
	}
}