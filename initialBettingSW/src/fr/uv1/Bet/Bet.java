package fr.uv1.Bet;

import fr.uv1.Subscriber.Subscriber;

/**
 * 
 * @author G3B3 <br>
 *         This class represents a bet . <br>
 */
public class Bet {

	private Subscriber bet_subscriber;
	private long bet_number_tokens;
	private int id;

	public Bet(int id, Subscriber bet_subscriber, long bet_number_tokens) {

		this.id = id;
		this.bet_subscriber = bet_subscriber;
		this.bet_number_tokens = bet_number_tokens;
	}

	public Bet(Subscriber bet_subscriber, long bet_number_tokens) {

		this.bet_subscriber = bet_subscriber;
		this.bet_number_tokens = bet_number_tokens;
	}

	public void setBet_subscriber(Subscriber bet_subscriber) {
		this.bet_subscriber = bet_subscriber;
	}

	public Subscriber getBet_subscriber() {
		return bet_subscriber;
	}

	public void setBet_number_tokens(long bet_number_tokens) {
		this.bet_number_tokens = bet_number_tokens;
	}

	public long getBet_number_tokens() {
		return bet_number_tokens;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}