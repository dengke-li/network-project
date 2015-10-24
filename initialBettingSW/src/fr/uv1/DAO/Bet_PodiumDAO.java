package fr.uv1.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.uv1.Bet.Bet;
import fr.uv1.Bet.Bet_Podium;
import fr.uv1.Competition.Competition_Podium;
import fr.uv1.Competitor.PCompetitor;
import fr.uv1.Subscriber.Subscriber;
import fr.uv1.bettingServices.BadParametersException;
import fr.uv1.bettingServices.DatabaseConnection;

/**
 * DAO class (<i>Data Access Object</i>) for the {@link Bet} class. This class
 * provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new bet in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)bet(s) from the database.
 * <li><b>U</b>: update the values stored in the database for a bet.
 * <li><b>D</b>: delete a bet in the database.
 * </ul>
 * 
 * @author Dengke Li
 */
public class Bet_PodiumDAO {
	// -----------------------------------------------------------------------------
	/**
	 * Store a bet of podium in the database for a specific competition This bet
	 * is not stored yet, so his <code>id</code> value is <code>NULL</code>.
	 * Once the bet is stored, the method returns the bet with the
	 * <code>id</code> value setted.
	 * 
	 * @param bet
	 *            the bet to be stored.
	 * @return the bet with the updated value for the id.
	 * @throws SQLException
	 */
	public Bet_Podium persist(Bet_Podium bet_podium, Integer competition_ID)
			throws SQLException {
		// Two steps in this method which must be managed in an atomic
		// (unique) transaction:
		// 1 - insert the new bet;
		// 2 - once the insertion is OK, in order to set up the value
		// of the id, a request is done to get this value by
		// requesting the sequence (bets_id_seq) in the
		// database.
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist = c
					.prepareStatement("insert into bets_podium(id_competition,id_subscriber,number_of_tokens, id_winner,id_second,id_third)  values (?,?,?,?,?,?)");

			psPersist.setInt(1, competition_ID);
			psPersist.setInt(2, bet_podium.getBet_subscriber().getId());
			psPersist.setLong(3, bet_podium.getBet_number_tokens());
			psPersist.setInt(4, bet_podium.getWinner().getId());
			psPersist.setInt(5, bet_podium.getSecond().getId());
			psPersist.setInt(6, bet_podium.getThird().getId());

			psPersist.executeUpdate();

			psPersist.close();

			// Retrieving the value of the id with a request on the
			// sequence (bets_podium_id_seq).
			PreparedStatement psIdValue = c
					.prepareStatement("select currval('bets_podium_id_seq') as value_id");
			ResultSet resultSet = psIdValue.executeQuery();
			Integer id = null;
			while (resultSet.next()) {
				id = resultSet.getInt("value_id");
			}
			resultSet.close();
			psIdValue.close();
			c.commit();
			bet_podium.setId(id);
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			c.setAutoCommit(true);
			throw e;
		}

		c.setAutoCommit(true);
		c.close();

		return bet_podium;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find a bet of podium by his id.
	 * 
	 * @param id
	 *            the id of the bet to retrieve.
	 * @return the bet or null if the id does not exist in the database.
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Bet_Podium findById(Integer id) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_podium where id=?");
		psSelect.setInt(1, id);
		ResultSet resultSet = psSelect.executeQuery();

		Bet_Podium bet_podium = null;
		SubscriberDAO subscriberdao = new SubscriberDAO();
		while (resultSet.next()) {
			// find the subscriber by id
			Subscriber subscriber = subscriberdao.findById(resultSet
					.getInt("id_subscriber"));
			// find the result by id
			PCompetitorDAO competitorDAO = new PCompetitorDAO();
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("id_winner"));
			PCompetitor competitorsecond = competitorDAO.findById(resultSet
					.getInt("id_second"));
			PCompetitor competitorthird = competitorDAO.findById(resultSet
					.getInt("id_third"));

			/*
			 * psSelectcompetition.setInt(1,
			 * resultSet.getInt("id_competition")); ResultSet
			 * resultSetcompetition = psSelectcompetition.executeQuery();
			 * Competition_Podium competition_podium=new
			 * Competition_Podium(resultSetcompetition
			 * .getInt("id_competition"),resultSetcompetition
			 * .getInt("Result_competitor_ID_first"),
			 * resultSetcompetition.getInt
			 * ("Result_competitor_ID_second"),resultSetcompetition
			 * .getInt("Result_competitor_ID_third"
			 * ),resultSetcompetition.getInt(
			 * "competition_name"),resultSetcompetition
			 * .getInt("competition_name"),
			 * resultSetcompetition.getInt("competition_type"
			 * ),resultSetcompetition.getInt("competition_date"));
			 * resultSetcompetition.close(); psSelectcompetition.close();
			 */

			bet_podium = new Bet_Podium(subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner,
					competitorsecond, competitorthird);
		}

		resultSet.close();
		psSelect.close();
		c.close();

		return bet_podium;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets of podium for a specific subscriber in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Podium> findBySubscriber_id(int Subscriber_id)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_podium where id_subscriber=? order by id");
		psSelect.setInt(1, Subscriber_id);
		ResultSet resultSet = psSelect.executeQuery();

		Collection<Bet_Podium> bets_podium = new ArrayList<Bet_Podium>();
		SubscriberDAO subscriberdao = new SubscriberDAO();
		while (resultSet.next()) {
			// find the subscriber by id
			Subscriber subscriber = subscriberdao.findById(resultSet
					.getInt("id_subscriber"));

			// find the result by id
			PCompetitorDAO competitorDAO = new PCompetitorDAO();
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("id_winner"));
			PCompetitor competitorsecond = competitorDAO.findById(resultSet
					.getInt("id_second"));
			PCompetitor competitorthird = competitorDAO.findById(resultSet
					.getInt("id_third"));

			bets_podium.add(new Bet_Podium(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner,
					competitorsecond, competitorthird));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_podium;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Podium> findAll() throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_podium");
		ResultSet resultSet = psSelect.executeQuery();
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		PCompetitorDAO competitorDAO = new PCompetitorDAO();
		Collection<Bet_Podium> bets_podium = new ArrayList<Bet_Podium>();
		while (resultSet.next()) {
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));

			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("id_winner"));
			PCompetitor competitorsecond = competitorDAO.findById(resultSet
					.getInt("id_second"));
			PCompetitor competitorthird = competitorDAO.findById(resultSet
					.getInt("id_third"));
			// add a bet_podium with the right parametre
			bets_podium.add(new Bet_Podium(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner,
					competitorsecond, competitorthird));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_podium;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Update on the database the values from a bet.
	 * 
	 * @param bet
	 *            the bet to be updated.
	 * @throws SQLException
	 */
	public void update(Bet_Podium bet_podium) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update bets_podium set number_of_tokens=?, id_subscriber=?,id_winner=? ,id_second=? ,id_third=?  where id=?");
		psUpdate.setLong(1, bet_podium.getBet_number_tokens());
		psUpdate.setInt(2, bet_podium.getBet_subscriber().getId());
		psUpdate.setInt(3, bet_podium.getWinner().getId());
		psUpdate.setInt(4, bet_podium.getSecond().getId());
		psUpdate.setInt(5, bet_podium.getThird().getId());
		psUpdate.setInt(6, bet_podium.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	// -----------------------------------------------------------------------------
	/**
	 * Delete from the database a specific bet.
	 * 
	 * @param bet
	 *            the bet to be deleted.
	 * @throws SQLException
	 */
	public void delete(Bet_Podium bet_podium) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("delete from bets_podium where id=?");
		psUpdate.setInt(1, bet_podium.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	/**
	 * Search from the database a collection of specific bet by id_competition.
	 * 
	 * @param id_competition
	 *            the bet to be deleted.
	 * @throws SQLException
	 */
	public Collection<Bet_Podium> findbyidcompetition(int id_competition)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_podium where id_competition=?");
		psSelect.setInt(1, id_competition);
		ResultSet resultSet = psSelect.executeQuery();
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		PCompetitorDAO competitorDAO = new PCompetitorDAO();
		Collection<Bet_Podium> collectbet_podium = new ArrayList<Bet_Podium>();
		Bet_Podium bet_podium = null;
		while (resultSet.next()) {

			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("id_winner"));
			PCompetitor competitorsecond = competitorDAO.findById(resultSet
					.getInt("id_second"));
			PCompetitor competitorthird = competitorDAO.findById(resultSet
					.getInt("id_third"));
			// add a bet_podium with the right parametre
			bet_podium = new Bet_Podium(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner,
					competitorsecond, competitorthird);
			collectbet_podium.add(bet_podium);
		}

		resultSet.close();
		psSelect.close();
		c.close();

		return collectbet_podium;

	}

	/**
	 * find the bet By Subscriber_id and competition_id
	 * 
	 * @param Subscriber_id
	 * @param competition_id
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Podium> findBySubscriber_id_and_competition_id(
			int Subscriber_id, int competition_id) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_podium where id_subscriber=? and id_competition=? order by id");
		psSelect.setInt(1, Subscriber_id);
		psSelect.setInt(2, competition_id);
		ResultSet resultSet = psSelect.executeQuery();

		Collection<Bet_Podium> bets_podium = new ArrayList<Bet_Podium>();
		SubscriberDAO subscriberdao = new SubscriberDAO();
		while (resultSet.next()) {
			// find the subscriber by id
			Subscriber subscriber = subscriberdao.findById(resultSet
					.getInt("id_subscriber"));

			// find the result by id
			PCompetitorDAO competitorDAO = new PCompetitorDAO();
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("id_winner"));
			PCompetitor competitorsecond = competitorDAO.findById(resultSet
					.getInt("id_second"));
			PCompetitor competitorthird = competitorDAO.findById(resultSet
					.getInt("id_third"));

			bets_podium.add(new Bet_Podium(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner,
					competitorsecond, competitorthird));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_podium;
	}

	/**
	 * find the bets by result : the id of competitor_podium
	 * 
	 * @param id_winner
	 * @param id_second
	 * @param id_third
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Podium> findbyresult_id_competitor_podium(
			int id_winner, int id_second, int id_third) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_podium where id_winner=? and  id_second=? and  id_third=? order by id");
		psSelect.setInt(1, id_winner);
		psSelect.setInt(2, id_second);
		psSelect.setInt(3, id_third);
		ResultSet resultSet = psSelect.executeQuery();

		Collection<Bet_Podium> bets_podium = new ArrayList<Bet_Podium>();
		SubscriberDAO subscriberdao = new SubscriberDAO();
		while (resultSet.next()) {
			// find the subscriber by id
			Subscriber subscriber = subscriberdao.findById(resultSet
					.getInt("id_subscriber"));

			// find the result by id
			PCompetitorDAO competitorDAO = new PCompetitorDAO();
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("id_winner"));
			PCompetitor competitorsecond = competitorDAO.findById(resultSet
					.getInt("id_second"));
			PCompetitor competitorthird = competitorDAO.findById(resultSet
					.getInt("id_third"));

			bets_podium.add(new Bet_Podium(resultSet.getInt("id"),
					subscriber, resultSet.getInt("number_of_tokens"),
					competitorwinner, competitorsecond, competitorthird));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_podium;
	}

	/**
	 * delete the relation between competition and its bets
	 * 
	 * @param competition_podium
	 */
	public void deletebycompetition(Competition_Podium competition_podium) {
		Connection c;
		try {
			c = DatabaseConnection.getConnection();

			PreparedStatement psUpdate = c
					.prepareStatement("delete from bets_podium where id_competition=?");
			psUpdate.setInt(1, competition_podium.getId());
			psUpdate.executeUpdate();
			psUpdate.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// -----------------------------------------------------------------------------
}