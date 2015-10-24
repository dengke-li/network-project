package fr.uv1.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.uv1.Bet.Bet_Winner;
import fr.uv1.Competition.Competition_Winner;
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
 * @author G3B3
 */
public class Bet_WinnerDAO {
	// -----------------------------------------------------------------------------
	/**
	 * Store a bet in the database for a specific bet of winner This bet is not
	 * stored yet, so his <code>id</code> value is <code>NULL</code>. Once the
	 * bet is stored, the method returns the bet with the <code>id</code> value
	 * setted.
	 * 
	 * @param bet
	 *            the bet to be stored.
	 * @return the bet with the updated value for the id.
	 * @throws SQLException
	 */
	public Bet_Winner persist(Bet_Winner bet_winner, Integer competition_ID)
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
					.prepareStatement("insert into bets_winner(id_competition,id_subscriber,number_of_tokens, result_id_competitor)  values (?,?,?,?)");
			psPersist.setInt(1, competition_ID);
			psPersist.setInt(2, bet_winner.getBet_subscriber().getId());
			psPersist.setLong(3, bet_winner.getBet_number_tokens());
			psPersist.setInt(4, bet_winner.getWinnerCompetitor().getId());
			psPersist.executeUpdate();
			psPersist.close();

			// Retrieving the value of the id with a request on the
			// sequence (bets_winner_id_seq).
			PreparedStatement psIdValue = c
					.prepareStatement("select currval('bets_winner_id_seq') as value_id");
			ResultSet resultSet = psIdValue.executeQuery();
			Integer id = null;
			while (resultSet.next()) {
				id = resultSet.getInt("value_id");
			}
			resultSet.close();
			psIdValue.close();
			c.commit();
			bet_winner.setId(id);
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
		return bet_winner;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find a bet by his id.
	 * 
	 * @param id
	 *            the id of the bet to retrieve.
	 * @return the bet or null if the id does not exist in the database.
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Bet_Winner findById(Integer id) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_winner where id=?");
		psSelect.setInt(1, id);
		ResultSet resultSet = psSelect.executeQuery();
		Bet_Winner bet_winner = null;
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		PCompetitorDAO competitorDAO = new PCompetitorDAO();
		while (resultSet.next()) {
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));
			PCompetitor winnercompetitor = competitorDAO.findById(resultSet
					.getInt("result_id_competitor"));
			bet_winner = new Bet_Winner(id, subscriber,
					resultSet.getInt("number_of_tokens"), winnercompetitor);
		}

		resultSet.close();
		psSelect.close();
		c.close();
		return bet_winner;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets for a collection of specific bets of winner in the
	 * database by Subscriber_id.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Winner> findBySubscriber_id(int Subscriber_id)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_winner where id_subscriber=? order by id");
		psSelect.setInt(1, Subscriber_id);
		ResultSet resultSet = psSelect.executeQuery();
		SubscriberDAO subscriberDAO = new SubscriberDAO();

		PCompetitorDAO competitorDAO = new PCompetitorDAO();

		Collection<Bet_Winner> bets_winner = new ArrayList<Bet_Winner>();
		while (resultSet.next()) {
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("result_id_competitor"));
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));
			// add a bet_winner with the right parametre
			bets_winner.add(new Bet_Winner(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_winner;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets of winner in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Winner> findAll() throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_winner");
		ResultSet resultSet = psSelect.executeQuery();
		Collection<Bet_Winner> bets_winner = new ArrayList<Bet_Winner>();
		while (resultSet.next()) {

			SubscriberDAO subscriberDAO = new SubscriberDAO();
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));

			PCompetitorDAO competitorDAO = new PCompetitorDAO();
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("result_id_competitor"));
			// add a bet_winner with the right parametre
			bets_winner.add(new Bet_Winner(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_winner;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Update on the database the values from a bet.
	 * 
	 * @param bet
	 *            the bet to be updated.
	 * @throws SQLException
	 */
	public void update(Bet_Winner bet_winner) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update bets_winner set number_of_tokens=?, id_subscriber=?,result_id_competitor=?  where id=?");
		psUpdate.setLong(1, bet_winner.getBet_number_tokens());
		psUpdate.setInt(2, bet_winner.getBet_subscriber().getId());
		psUpdate.setInt(3, bet_winner.getWinnerCompetitor().getId());
		psUpdate.setInt(4, bet_winner.getId());
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
	public void delete(Bet_Winner bet_winner) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("delete from bets_winner where id=?");
		psUpdate.setInt(1, bet_winner.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	public Collection<Bet_Winner> findbyidcompetition(int id_competition)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_winner where id_competition=?");
		psSelect.setInt(1, id_competition);
		ResultSet resultSet = psSelect.executeQuery();
		Collection<Bet_Winner> collectbet_winner = new ArrayList<Bet_Winner>();
		Bet_Winner bet_winner = null;
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		PCompetitorDAO competitorDAO = new PCompetitorDAO();
		while (resultSet.next()) {
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("result_id_competitor"));
			// add a bet_winner with the right parametre
			bet_winner = new Bet_Winner(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner);
			collectbet_winner.add(bet_winner);
		}

		resultSet.close();
		psSelect.close();
		c.close();

		return collectbet_winner;

	}

	/**
	 * find the bet By Subscriber_id and competition_id
	 * 
	 * @param Subscriber_id
	 * @param id_competition
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Winner> findBySubscriber_id_and_competition_id(
			int Subscriber_id, int id_competition) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_winner where id_subscriber=? and id_competition=? order by id");
		psSelect.setInt(1, Subscriber_id);
		psSelect.setInt(2, id_competition);
		ResultSet resultSet = psSelect.executeQuery();
		SubscriberDAO subscriberDAO = new SubscriberDAO();

		PCompetitorDAO competitorDAO = new PCompetitorDAO();

		Collection<Bet_Winner> bets_winner = new ArrayList<Bet_Winner>();
		while (resultSet.next()) {
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("result_id_competitor"));
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));
			// add a bet_winner with the right parametre
			bets_winner.add(new Bet_Winner(resultSet.getInt("id"), subscriber,
					resultSet.getInt("number_of_tokens"), competitorwinner));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_winner;
	}

	/**
	 * find the bet by result : the id of competitor
	 * 
	 * @param result_id_competitor
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Collection<Bet_Winner> findbyresult_id_competitor_winner(
			int result_id_competitor) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from bets_winner where result_id_competitor=? order by id");
		psSelect.setInt(1, result_id_competitor);
		ResultSet resultSet = psSelect.executeQuery();
		SubscriberDAO subscriberDAO = new SubscriberDAO();

		PCompetitorDAO competitorDAO = new PCompetitorDAO();

		Collection<Bet_Winner> bets_winner = new ArrayList<Bet_Winner>();
		while (resultSet.next()) {
			PCompetitor competitorwinner = competitorDAO.findById(resultSet
					.getInt("result_id_competitor"));
			Subscriber subscriber = subscriberDAO.findById(resultSet
					.getInt("id_subscriber"));
			// add a bet_winner with the right parametre
			bets_winner.add(new Bet_Winner(resultSet.getInt("id"),
					subscriber, resultSet.getInt("number_of_tokens"),
					competitorwinner));
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return bets_winner;
	}

	/**
	 * delete the relation between competition and its bets
	 * @param competition_winner
	 */
	public void deletebycompetition(Competition_Winner competition_winner){
		Connection c;
		try {
			c = DatabaseConnection.getConnection();

			PreparedStatement psUpdate = c
					.prepareStatement("delete from bets_winner where id_competition=?");
			psUpdate.setInt(1, competition_winner.getId());
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