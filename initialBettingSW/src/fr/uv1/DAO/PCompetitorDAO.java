package fr.uv1.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.uv1.Competition.Competition;
import fr.uv1.Competitor.PCompetitor;
import fr.uv1.bettingServices.BadParametersException;
import fr.uv1.bettingServices.DatabaseConnection;
import fr.uv1.bettingServices.ExistingCompetitorException;

/**
 * DAO class (<i>Data Access Object</i>) for the{@link PCompetitor} class.This
 * class provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new competitor in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)competitor(s) from the database.
 * <li><b>U</b>: update the values stored in the database for a bet.
 * <li><b>D</b>: delete a bet in the database.
 * </ul>
 */

public class PCompetitorDAO {
	/**
	 * set up the relation(play) between competitor and competition in the table
	 * of playcompetition. This play is not stored yet, so his <code>id</code>
	 * value is <code>NULL</code>. Once the id of competitor and id of
	 * competition are stored, the method returns the the <code>id</code>of play
	 * value setted.
	 * 
	 * 
	 * @throws SQLException
	 */

	public void playpersist(PCompetitor competitor, Competition competition)
			throws SQLException {
		// Two steps in this methods which must be managed in an atomic
		// (unique) transaction:
		// 1 - insert the id of competitor and id of competition in play;

		String type = competition.getType();
		Connection c = DatabaseConnection.getConnection();
		int id_competition = competition.getId();
		try {
			c.setAutoCommit(false);

			// Retrieving the value of the id with a request on the

			Integer id = null;
			id = competitor.getId();
			if (type.equals("Winner")) {
				PreparedStatement psPersist1 = c
						.prepareStatement("insert into play_competitions_winner(competition_winner_id, id_competitor) values (?, ?)");
				psPersist1.setInt(1, id_competition);
				psPersist1.setInt(2, id);
				psPersist1.executeUpdate();
				psPersist1.close();
			}
			if (type.equals("Podium")) {
				PreparedStatement psPersist2 = c
						.prepareStatement("insert into play_competitions_podium(competition_podium_id, id_competitor) values (?, ?)");
				psPersist2.setInt(1, id_competition);
				psPersist2.setInt(2, id);
				psPersist2.executeUpdate();
				psPersist2.close();
			}

			c.commit();
			competitor.setId(id);
		}

		catch (SQLException e) {
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
	}

	public void playdelete(PCompetitor competitor, Competition competition)
			throws SQLException {
		// Two steps in this methods which must be managed in an atomic
		// (unique) transaction:
		// 1 - delete the relation of competitor and competition in the table
		// playcompetition;

		String type = competition.getType();
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);

			// judge the type of competition, and delete the relation in the
			// corresponding table of play

			if (type.equals("Winner")) {
				PreparedStatement psPersist = c
						.prepareStatement("delete from play_competitions_winner where competition_winner_id=? and id_competitor=? ");
				psPersist.setInt(1, competition.getId());
				psPersist.setInt(2, competitor.getId());
				psPersist.executeUpdate();
				psPersist.close();
			}
			if (type.equals("Podium")) {
				PreparedStatement psPersist = c
						.prepareStatement("delete from play_competitions_podium where competition_podium_id=? and id_competitor=? ");
				psPersist.setInt(1, competition.getId());
				psPersist.setInt(2, competitor.getId());
				psPersist.executeUpdate();
				psPersist.close();
			}

			c.commit();
		}

		catch (SQLException e) {
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
	}

	
	
	public void isplay(PCompetitor competitor, Competition competition)
			throws SQLException, ExistingCompetitorException {
		// Two steps in this methods which must be managed in an atomic
		// (unique) transaction:
		// 1 - delete the relation of competitor and competition in the table
		// playcompetition;
		
		String type = competition.getType();
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);

			// judge the type of competition, and delete the relation in the
			// corresponding table of play
			boolean a=false;
			boolean b=false;
			if (type.equals("Winner")) {


				PreparedStatement psSelect = c
						.prepareStatement("select * from play_competitions_winner where competition_winner_id=? and id_competitor=? ");
				psSelect.setInt(1, competition.getId());
				psSelect.setInt(2, competitor.getId());
				
				ResultSet resultSet1 = psSelect.executeQuery();
				
				if(!resultSet1.next()){
					a=true;
				}
				resultSet1.close();
				psSelect.close();
			}
			
			if (type.equals("Podium")) {
				PreparedStatement psSelect = c
						.prepareStatement("select * from play_competitions_podium where competition_podium_id=? and id_competitor=? ");
				psSelect.setInt(1, competition.getId());
				psSelect.setInt(2, competitor.getId());			
				ResultSet resultSet2 = psSelect.executeQuery();
				if(resultSet2.next()){
					b=true;
				}
				resultSet2.close();
				psSelect.close();
				
			}
			if(!a&&!b){
//				
//			}
//			else{
				throw new ExistingCompetitorException("the competitor is not registered for the competition");
			}
			


			c.close();
		}

		catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			c.setAutoCommit(true);
			throw e;
		}

		c.close();
	}

	
	/**
	 * delete all the relation of a competitor in the two table play_competition
	 * 
	 * @param competitor
	 * @throws SQLException
	 */
	public void playlistdelete(PCompetitor competitor) throws SQLException {
		// Two steps in this methods which must be managed in an atomic
		// (unique) transaction:

		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist1 = c
					.prepareStatement("delete from play_competitions_winner where id_competitor=? ");
			psPersist1.setInt(1, competitor.getId());
			psPersist1.executeUpdate();
			psPersist1.close();

			PreparedStatement psPersist2 = c
					.prepareStatement("delete from play_competitions_podium where id_competitor=? ");

			psPersist2.setInt(1, competitor.getId());
			psPersist2.executeUpdate();
			psPersist2.close();

			c.commit();
		}

		catch (SQLException e) {
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
	}

	/**
	 * 
	 * 
	 * insert new competitor in the database.
	 * 
	 * 
	 * @author dengke
	 */
	public PCompetitor persist(PCompetitor competitor) throws SQLException {
		// Two steps in this methods which must be managed in an atomic
		// (unique) transaction:
		// 1 - insert the new competitor;
		// 2 - once the insertion is OK, in order to set up the value
		// of the id, a request is done to get this value by
		// requesting the sequence (competitors_id_seq) in the
		// database.
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist = c
					.prepareStatement("insert into competitors (firstname, lastname, borndate) values (?, ?, ?)");
			psPersist.setString(1, competitor.getFirstname());
			psPersist.setString(2, competitor.getLastname());
			psPersist.setString(3, competitor.getBorndate());
			psPersist.executeUpdate();
			psPersist.close();

			// Retrieving the value of the id with a request on the
			// sequence (competitors_id_seq).

			PreparedStatement psIdValue = c
					.prepareStatement("select currval('competitors_id_seq') as value_id");
			ResultSet resultSet = psIdValue.executeQuery();
			Integer id = null;
			while (resultSet.next()) {
				id = resultSet.getInt("value_id");
			}
			resultSet.close();
			psIdValue.close();

			c.commit();
			competitor.setId(id);
		}

		catch (SQLException e) {
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
		return competitor;
	}

	// -----------------------------------------------------------------------------

	/**
	 * Find a competitor by his id.
	 * 
	 * @param id
	 *            the id of the competitor to retrieve.
	 * @return the competitor or null if the id does not exist in the database.
	 * @throws SQLException
	 */

	public PCompetitor findById(Integer id) throws SQLException {

		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitors where id=?");
		psSelect.setInt(1, id);
		ResultSet resultSet = psSelect.executeQuery();
		PCompetitor competitor = null;
		while (resultSet.next()) {
			try {
				competitor = new PCompetitor(resultSet.getInt("id"),
						resultSet.getString("firstname"),
						resultSet.getString("lastname"),
						resultSet.getString("borndate"));
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		resultSet.close();
		psSelect.close();
		c.close();
		return competitor;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the competitors in the database.
	 * 
	 * @return
	 * @throws SQLException
	 */

	public Collection<PCompetitor> findAll() throws SQLException

	{
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitors order by id");
		ResultSet resultSet = psSelect.executeQuery();
		Collection<PCompetitor> competitors = new ArrayList<PCompetitor>();
		while (resultSet.next()) {
			try {
				competitors.add(new PCompetitor(resultSet.getInt("id"), resultSet
						.getString("firstname"), resultSet.getString("lastname"),resultSet.getString("borndate")));
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultSet.close();
		psSelect.close();
		c.close();
		return competitors;
	}

	// -----------------------------------------------------------------------------

	/**
	 * Update on the database the values from a competitor.
	 * 
	 * @param competitor
	 *            the competitor to be updated.
	 * @throws SQLException
	 */

	public void update(PCompetitor competitor) throws SQLException

	{
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update competitors set firstname=?, lastname=? ,borndate=? where id=?");
		psUpdate.setString(1, competitor.getFirstname());
		psUpdate.setString(2, competitor.getLastname());
		psUpdate.setString(3, competitor.getBorndate());
		psUpdate.setInt(4, competitor.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();

	}

	/**
	 * Delete from the database a specific competitor.<br>
	 * <i>Be careful: the delete functionality does not operate a delete cascade
	 * on competiton belonging to the competitor.</i>
	 * 
	 * @param competitor
	 *            the competitor to be deleted.
	 * @throws SQLException
	 */

	public void delete(PCompetitor competitor) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("delete from competitors where id=?");
		psUpdate.setInt(1, competitor.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	/**
	 * Select from the database a series of specific competitor whose
	 * competition_winner_id is given.<br>
	 * 
	 * @param competitor
	 *            the competitor to be deleted.
	 * @throws SQLException
	 */

	public Collection<PCompetitor> getWinnerCompetitionListCompetitors(
			int competition_id) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from play_competitions_winner where competition_winner_id=?");
		psSelect.setInt(1, competition_id);
		ResultSet resultSet = psSelect.executeQuery();
		Collection<PCompetitor> competitors = new ArrayList<PCompetitor>();
		while (resultSet.next()) {
			competitors.add(this.findById(resultSet.getInt("id_competitor")));
		}
		resultSet.close();
		psSelect.close();
		c.close();
		return competitors;
	}

	/**
	 * Select from the database a series of specific competitor whose
	 * competition_podium_id is given.<br>
	 * 
	 * @param competitor
	 *            the competitor to be deleted.
	 * @throws SQLException
	 */
	public Collection<PCompetitor> getPodiumCompetitionListCompetitors(
			int competition_id) throws SQLException {
		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from play_competitions_podium where competition_podium_id=?");
		psSelect.setInt(1, competition_id);
		ResultSet resultSet = psSelect.executeQuery();

		Collection<PCompetitor> competitors = new ArrayList<PCompetitor>();
		while (resultSet.next()) {
			competitors.add(this.findById(resultSet.getInt("id_competitor")));
		}
		resultSet.close();
		psSelect.close();
		c.close();
		return competitors;
	}

	/**
	 * 
	 * Find a competitor by his name.
	 * 
	 * 
	 * 
	 * @param id
	 *            the id of the competitor to retrieve.
	 * 
	 * @return the competitor or null if the id does not exist in the database.
	 * 
	 * @throws SQLException
	 */

	public PCompetitor findByName(String firstname, String lastname)
			throws SQLException {

		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitors where firstname=? and lastname=?");
		psSelect.setString(1, firstname);
		psSelect.setString(2, lastname);
		ResultSet resultSet = psSelect.executeQuery();
		PCompetitor competitor = null;
		while (resultSet.next()) {
			try {
				competitor = new PCompetitor(resultSet.getInt("id"),
						resultSet.getString("firstname"),
						resultSet.getString("lastname"),
						resultSet.getString("borndate")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultSet.close();
		psSelect.close();
		c.close();
		return competitor;
	}
	
	public PCompetitor findByNameAndBornDate(String firstname, String lastname,String borndate)
			throws SQLException {

		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitors where firstname=?  and lastname=? and borndate=?");
		psSelect.setString(1, firstname);
		psSelect.setString(2, lastname);
		psSelect.setString(3, borndate);
		ResultSet resultSet = psSelect.executeQuery();
		PCompetitor competitor = null;
		while (resultSet.next()) {
			try {
				competitor = new PCompetitor(resultSet.getInt("id"),
						resultSet.getString("firstname"),
						resultSet.getString("lastname"),
						resultSet.getString("borndate")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultSet.close();
		psSelect.close();
		c.close();
		return competitor;
	}

}
