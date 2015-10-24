package fr.uv1.Bet;

import fr.uv1.Competitor.PCompetitor;
import fr.uv1.Subscriber.Subscriber;

/**
 * 
 * @author G3B3 <br>
 *         This class represents a bet of podium . <br>
 */
public class Bet_Winner extends Bet {
	private PCompetitor winnerCompetitor;

	public Bet_Winner(Integer Id, Subscriber bet_subscriber,
			long bet_number_tokens, PCompetitor winnerCompetitor) {
		super(Id,bet_subscriber, bet_number_tokens);
		this.winnerCompetitor = winnerCompetitor;
	}

	public Bet_Winner(Subscriber bet_subscriber, long bet_number_tokens,
			PCompetitor winnerCompetitor) {
		super(bet_subscriber, bet_number_tokens);
		this.winnerCompetitor = winnerCompetitor;
	}

	public void setWinnerCompetitor(PCompetitor winnerCompetitor) {
		this.winnerCompetitor = winnerCompetitor;
	}

	public PCompetitor getWinnerCompetitor() {
		return winnerCompetitor;
	}

}