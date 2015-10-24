package fr.uv1.Competition;
import java.sql.Date;
import java.util.Collection;

import fr.uv1.Competitor.PCompetitor;
import fr.uv1.bettingServices.BadParametersException;

import fr.uv1.Bet.Bet_Podium;

public class Competition_Podium extends Competition{


	private PCompetitor winner_first;
	private PCompetitor winner_second;
	private PCompetitor winner_third;
	private Collection<Bet_Podium> Bets_Podium;
	
	public Competition_Podium (String name, String type, Date close_date ) throws BadParametersException {
		super(name, type, close_date);	
		this.setList_competitor(null);
		this.winner_first=null;
		this.winner_second=null;
		this.winner_third=null;
	
	}
	
	public Competition_Podium (Integer id,String name, String type, Date close_date ) throws BadParametersException {
		super(id,name, type, close_date,null);	
		this.winner_first=null;
		this.winner_second=null;
		this.winner_third=null;
	
	}
	
	public Competition_Podium(Integer id,String name, String type, Date close_date,Collection<Bet_Podium> bets_podium,Collection<PCompetitor> competitors ) throws BadParametersException {
		super(id,name, type, close_date,competitors);	
		this.Bets_Podium=bets_podium;
		this.winner_first=null;
		this.winner_second=null;
		this.winner_third=null;
		// TODO Auto-generated constructor stub
	}
	
	public Competition_Podium(Integer id,String name, String type, Date close_date,Collection<Bet_Podium> bets_podium,Collection<PCompetitor> competitors , PCompetitor winner_first,PCompetitor winner_second,PCompetitor winner_third) throws BadParametersException {
		super(id,name, type, close_date,competitors);	
		this.Bets_Podium=bets_podium;
		this.winner_first=winner_first;
		this.winner_second=winner_second;
		this.winner_third=winner_third;
		// TODO Auto-generated constructor stub
	}
	public Competition_Podium(String name, String type, Date close_date,Collection<Bet_Podium> bets_podium,Collection<PCompetitor> competitors , PCompetitor winner_first,PCompetitor winner_second,PCompetitor winner_third) throws BadParametersException {
		super(name, type, close_date);	
	
		this.Bets_Podium=bets_podium;
		this.winner_first=winner_first;
		this.winner_second=winner_second;
		this.winner_third=winner_third;
		// TODO Auto-generated constructor stub
	}
	
	
	public PCompetitor getWinner_first() {
		return winner_first;
	}

	public void setWinner_first(PCompetitor winner_first) {
		this.winner_first = winner_first;
	}

	public PCompetitor getWinner_second() {
		return winner_second;
	}

	public void setWinner_second(PCompetitor winner_second) {
		this.winner_second = winner_second;
	}

	public PCompetitor getWinner_third() {
		return winner_third;
	}

	public void setWinner_third(PCompetitor winner_third) {
		this.winner_third = winner_third;
	}

	public Collection<Bet_Podium> getBet_Podium() {
		return Bets_Podium;
	}

	public void setBet_Podium(Collection<Bet_Podium> bet_Podium) {
		Bets_Podium = bet_Podium;
	}
	
	public String toString() {
		return "name_competition_podium:"+this.getName()+" id:"+getId()+" winners:"+getWinner_first()+";"+getWinner_second()+";"+getWinner_third()+";";
	}
}
