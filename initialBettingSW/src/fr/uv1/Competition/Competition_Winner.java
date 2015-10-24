package fr.uv1.Competition;

/**
 * the class represent the competition using form of winner as the result
 */
import java.sql.Date;
import java.util.Collection;

import fr.uv1.Bet.Bet_Winner;
import fr.uv1.Competitor.PCompetitor;
import fr.uv1.bettingServices.BadParametersException;

public class Competition_Winner extends Competition {

	private PCompetitor winner;
	private Collection<Bet_Winner> Bets_Winners;

	public Competition_Winner(String name, String type, Date close_date) throws BadParametersException {
		super(name, type, close_date);
		this.setList_competitor(null);
		this.setWinner(null);

	}

	public Competition_Winner(Integer id, String name, String type, Date close_date) throws BadParametersException {
		super(id, name, type, close_date, null);
		this.setWinner(null);
	}

	public Competition_Winner(Integer id, String name, String type,
			Date close_date, PCompetitor winner) throws BadParametersException {
		super(id, name, type, close_date, null);
		this.setWinner(winner);
	}

	public Competition_Winner(String name, String type, Date close_date,
			PCompetitor winner) throws BadParametersException {
		super(name, type, close_date);
		this.setWinner(winner);
	}

	public Competition_Winner(Integer id, String name, String type,
			Date close_date, Collection<Bet_Winner> bets_winner,
			Collection<PCompetitor> competitors, PCompetitor winner) throws BadParametersException {
		super(id, name, type, close_date, competitors);
		this.Bets_Winners = bets_winner;

		this.setWinner(winner);
		// TODO Auto-generated constructor stub
	}

	public Competition_Winner(Integer id, String name, String type,
			Date close_date, Collection<Bet_Winner> bets_winner,
			Collection<PCompetitor> competitors) throws BadParametersException {
		super(id, name, type, close_date, competitors);
		this.Bets_Winners = bets_winner;

		this.setWinner(null);
		// TODO Auto-generated constructor stub
	}

	public PCompetitor getWinner() {
		return winner;
	}

	public void setWinner(PCompetitor winner) {
		this.winner = winner;
	}

	public Collection<Bet_Winner> getBet_Winners() {
		return Bets_Winners;
	}

	public void setBet_Winners(Collection<Bet_Winner> bet_Winners) {
		Bets_Winners = bet_Winners;
	}

	@Override
	public String toString() {
		return "name_competition_winner" + this.getName() + " id:" + getId()
				+ " winner" + getWinner();
	}

}
