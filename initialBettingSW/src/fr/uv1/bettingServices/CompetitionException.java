package fr.uv1.bettingServices;

public class CompetitionException extends Exception {
	private static final long serialVersionUID = 1L;

	public CompetitionException() {
		super();
	}

	public CompetitionException(String reason) {
		super(reason);
	}

}
