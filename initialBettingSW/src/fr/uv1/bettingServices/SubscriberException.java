package fr.uv1.bettingServices;

public class SubscriberException extends Exception {
	private static final long serialVersionUID = 1L;

	public SubscriberException() {
		super();
	}

	public SubscriberException(String reason) {
		super(reason);
	}

}
