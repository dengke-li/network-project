package fr.uv1.Bet;

import fr.uv1.Competitor.PCompetitor;
import fr.uv1.Subscriber.Subscriber;

/**
 * 
 * @author G3B3<br>
 * 
 *         This class represents a bet of podium . <br>
 */
public class Bet_Podium extends Bet {
	private PCompetitor winner;
	private PCompetitor second;	
	private PCompetitor third;

	public Bet_Podium(Subscriber bet_subscriber, long bet_number_tokens,
			PCompetitor a_winner, PCompetitor a_second, PCompetitor a_third) {
		super(bet_subscriber, bet_number_tokens);
		this.setWinner(a_winner);
		this.setSecond(a_second);
		this.setThird(a_third);
	}

	public Bet_Podium(Integer id, Subscriber bet_subscriber,
			long bet_number_tokens, PCompetitor a_winner, PCompetitor a_second,
			PCompetitor a_third) {
		super(id,bet_subscriber, bet_number_tokens);
		this.setWinner(a_winner);
		this.setSecond(a_second);
		this.setThird(a_third);
	}

	public void setWinner(PCompetitor winner) {
		this.winner = winner;
	}

	public PCompetitor getWinner() {
		return winner;
	}

	public void setSecond(PCompetitor second) {
		this.second = second;
	}

	public PCompetitor getSecond() {
		return second;
	}

	public void setThird(PCompetitor third) {
		this.third = third;
	}

	public PCompetitor getThird() {
		return third;
	}

}