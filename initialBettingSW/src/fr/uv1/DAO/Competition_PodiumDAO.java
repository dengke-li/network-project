package fr.uv1.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.uv1.Competition.Competition_Podium;
import fr.uv1.Competitor.PCompetitor;
import fr.uv1.bettingServices.BadParametersException;
import fr.uv1.bettingServices.DatabaseConnection;

/**
 * DAO class (<i>Data Access Object</i>) for the {@link Competition_Podium}
 * class. This class provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new Competition_Podium in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)Competition_Podium(s) in the
 * database.
 * <li><b>U</b>: update the values stored in the database for a
 * Competition_Podium.
 * <li><b>D</b>: delete a Competition_Podium in the database.
 * </ul>
 * 
 */

public class Competition_PodiumDAO {

	// -----------------------------------------------------------------------------
	/**
	 * Store a Competition_Podium in the database. This Competition_Podium is
	 * not stored yet, so his <code>id</code> value is <code>NULL</code>. Once
	 * the Competition_Podium is stored, the method returns the
	 * Competition_Podium with the <code>id</code> value setted.
	 * 
	 * @param Competition_Podium
	 *            the Competition_Podium to be stored.
	 * @return the Competition_Podium with the updated value for the id.
	 * @throws SQLException
	 */
	public Competition_Podium persist(Competition_Podium Competition_Podium)
			throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist = c
					.prepareStatement("insert into competitions_podium(name, c_type, close_date)  values (?, ?, ?)");

			psPersist.setString(1, Competition_Podium.getName());
			psPersist.setString(2, Competition_Podium.getType());
			psPersist.setDate(3, Competition_Podium.getClose_date());
			psPersist.executeUpdate();
			psPersist.close();		

			// Retrieving the value of the id with a request on the
			// sequence (Competition_Podiums_id_seq).
			PreparedStatement psIdValue = c
					.prepareStatement("select currval('competitions_podium_id_seq') as value_id");
			ResultSet resultSet = psIdValue.executeQuery();
			Integer id = null;
			while (resultSet.next()) {
				id = resultSet.getInt("value_id");
			}
			resultSet.close();
			psIdValue.close();
			c.commit();
			Competition_Podium.setId(id);
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

		return Competition_Podium;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find a Competition_Podium by his id.
	 * 
	 * @param id
	 *            the id of the Competition_Podium to retrieve.
	 * @return the Competition_Podium or null if the id does not exist in the
	 *         database.
	 * @throws SQLException
	 * @throws BadParametersException
	 */

	public Competition_Podium findById(Integer id) throws SQLException,
			BadParametersException {

		Connection c = DatabaseConnection.getConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select * from competitions_podium where id=?");

		psSelect.setInt(1, id);
		ResultSet resultSet = psSelect.executeQuery();

		Competition_Podium competition_podium = null;
		Bet_PodiumDAO bet_podiumdao = new Bet_PodiumDAO();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		while (resultSet.next()) {
			competition_podium = new Competition_Podium(resultSet.getInt("id"),
					resultSet.getString("name"), resultSet.getString("c_type"),
					resultSet.getDate("close_date"),
					bet_podiumdao.findbyidcompetition(resultSet.getInt("id")),
					competitordao.getPodiumCompetitionListCompetitors(resultSet
							.getInt("id")));

		}

		resultSet.close();

		psSelect.close();

		c.close();
		
		return competition_podium;
	}

	public Collection<Competition_Podium> findByDate(Date date)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitions_podium where close_date > ?");
		
		psSelect.setDate(1, date);
		ResultSet resultSet = psSelect.executeQuery();
		Collection<Competition_Podium> Competition_Podiums = new ArrayList<Competition_Podium>();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		Bet_PodiumDAO bet_podiumdao = new Bet_PodiumDAO();
		while (resultSet.next()) {
			Competition_Podiums.add(new Competition_Podium(resultSet
					.getInt("id"), resultSet.getString("name"), resultSet
					.getString("c_type"), resultSet
					.getDate("close_date"), bet_podiumdao
					.findbyidcompetition(resultSet.getInt("id")),
					competitordao.getPodiumCompetitionListCompetitors(resultSet
							.getInt("id")), null, null, null));
		}
		resultSet.close();
		psSelect.close();
		c.close();
		
		return Competition_Podiums;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the Competition_Podiums in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */

	public Collection<Competition_Podium> findAll() throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitions_podium order by id");
		ResultSet resultSet = psSelect.executeQuery();
		Collection<Competition_Podium> Competition_Podiums = new ArrayList<Competition_Podium>();
		Bet_PodiumDAO bet_podiumdao = new Bet_PodiumDAO();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		while (resultSet.next()) {

			Competition_Podium newcompetition_podium = new Competition_Podium(
					resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getString("c_type"), resultSet
							.getDate("close_date"),
					bet_podiumdao.findbyidcompetition(resultSet.getInt("id")),
					competitordao.getPodiumCompetitionListCompetitors(resultSet
							.getInt("id")));

			Competition_Podiums.add(newcompetition_podium);

		}
		resultSet.close();
		psSelect.close();
		c.close();

		return Competition_Podiums;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Update on the database the values from a Competition_Podium.
	 * 
	 * @param Competition_Podium
	 *            the Competition_Podium to be updated.
	 * @throws SQLException
	 */
	public void update(Competition_Podium Competition_Podium)
			throws SQLException {
		// 1 - Get a database connection from the class 'DatabaseConnection'
		Connection c = DatabaseConnection.getConnection();

		// 2 - Creating a Prepared Statement with the SQL instruction.
		// The parameters are represented by question marks.
		PreparedStatement psUpdate = c
				.prepareStatement("update competitions_podium set name=?, c_type=?, close_date=?  where id=?");

		// 3 - Supplying values for the prepared statement parameters (question
		// marks).
		psUpdate.setString(1, Competition_Podium.getName());
		psUpdate.setString(2, Competition_Podium.getType());
		psUpdate.setDate(3, Competition_Podium.getClose_date());
		psUpdate.setInt(4, Competition_Podium.getId());

		// Executing the prepared statement object among the database.
		// If needed, a return value (int) can be obtained. It contains
		// how many rows of a table were updated.
		// int nbRows = psUpdate.executeUpdate();
		psUpdate.executeUpdate();

		// 6 - Closing the Prepared Statement.
		psUpdate.close();

		// 7 - Closing the database connection.
		c.close();
	}

	// -----------------------------------------------------------------------------
	/**
	 * Delete from the database a specific Competition_Podium.<br>
	 * <i>Be careful: the delete functionality does not operate a delete cascade
	 * on bets belonging to the Competition_Podium.</i>
	 * 
	 * @param Competition_Podium
	 *            the Competition_Podium to be deleted.
	 * @throws SQLException
	 */
	public void delete(Competition_Podium competition_podium)
			throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("delete from competitions_podium where id=?");

		psUpdate.setInt(1, competition_podium.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	/**
	 * 
	 * @param competition_podium
	 * @param winner
	 * @param second
	 * @param third
	 * @throws SQLException
	 */
	public void resulutpersist(Competition_Podium competition_podium,
			PCompetitor winner, PCompetitor second, PCompetitor third)
			throws SQLException {

		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			PreparedStatement psPersist = c
					.prepareStatement("insert into result_competitions_podium (competition_podium_id, result1_id_competitor, result2_id_competitor,result3_id_competitor)  values ( ?, ?, ?,?)");

			psPersist.setInt(1, competition_podium.getId());
			psPersist.setInt(2, competition_podium.getWinner_first().getId());
			psPersist.setInt(3, competition_podium.getWinner_second().getId());
			psPersist.setInt(4, competition_podium.getWinner_third().getId());
			psPersist.executeUpdate();

			psPersist.close();

			// Retrieving the value of the id with a request on the
			// sequence (Competition_Winners_id_seq).

			c.commit();
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
	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param competition_podium
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public PCompetitor findresultFirst(Competition_Podium competition_podium)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from result_competitions_podium where competition_podium_id=?");
		psSelect.setInt(1, competition_podium.getId());
		ResultSet resultSet = psSelect.executeQuery();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		PCompetitor winnerfirst = competitordao.findById(resultSet
				.getInt("result1_id_competitor"));

		// Closing the Result Set
		resultSet.close();

		// Closing the Prepared Statement.
		psSelect.close();

		// Closing the database connection.
		c.close();

		return winnerfirst;
	}

	/**
	 * 
	 * @param competition_podium
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public PCompetitor findresultSecond(Competition_Podium competition_podium)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from result_competitions_podium where competition_podium_id=?");
		psSelect.setInt(1, competition_podium.getId());
		ResultSet resultSet = psSelect.executeQuery();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		PCompetitor winnesecond = competitordao.findById(resultSet
				.getInt("result2_id_competitor"));

		// Closing the Result Set
		resultSet.close();

		// Closing the Prepared Statement.
		psSelect.close();

		// Closing the database connection.
		c.close();

		return winnesecond;
	}

	/**
	 * 
	 * @param competition_podium
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public PCompetitor findresultThird(Competition_Podium competition_podium)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from result_competitions_podium where competition_podium_id=?");
		psSelect.setInt(1, competition_podium.getId());
		ResultSet resultSet = psSelect.executeQuery();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		PCompetitor winnethird = competitordao.findById(resultSet
				.getInt("result3_id_competitor"));

		// Closing the Result Set
		resultSet.close();

		// Closing the Prepared Statement.
		psSelect.close();

		// Closing the database connection.
		c.close();

		return winnethird;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the Competition_Podiums in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Competition_Podium find(String name, String type, Date a_close_date)
			throws SQLException, BadParametersException {
		Connection c = DatabaseConnection.getConnection();

		// 2 - Creating a Prepared Statement with the SQL instruction.
		// The parameters are represented by question marks.
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitions_podiums where name=? and type=? and close_date=?");

		// 3 - Supplying values for the prepared statement parameters (question
		// marks).
		psSelect.setString(1, name);
		psSelect.setString(2, type);
		psSelect.setDate(3, (java.sql.Date) a_close_date);

		// 4 - Executing Prepared Statement object among the database.
		// The return value is a Result Set containing the data.
		ResultSet resultSet = psSelect.executeQuery();

		// 5 - Retrieving values from the Result Set.
		Competition_Podium competition_podium = null;
		Bet_PodiumDAO bet_podiumdao = new Bet_PodiumDAO();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		while (resultSet.next()) {
			competition_podium = new Competition_Podium(resultSet.getInt("id"),
					resultSet.getString("name"), resultSet.getString("c_type"),
					resultSet.getDate("close_date"),
					bet_podiumdao.findbyidcompetition(resultSet.getInt("id")),
					competitordao.getPodiumCompetitionListCompetitors(resultSet
							.getInt("id")));
		}

		// 6 - Closing the Result Set
		resultSet.close();

		// 7 - Closing the Prepared Statement.
		psSelect.close();

		// 8 - Closing the database connection.
		c.close();

		return competition_podium;
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @param a_close_date
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public Competition_Podium findbyname(String name) throws SQLException,
			BadParametersException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from competitions_podium where name=?");
		psSelect.setString(1, name);
		ResultSet resultSet = psSelect.executeQuery();
		Competition_Podium competition_podium = null;
		Bet_PodiumDAO bet_podiumdao = new Bet_PodiumDAO();
		PCompetitorDAO competitordao = new PCompetitorDAO();
		while (resultSet.next()) {
			competition_podium = new Competition_Podium(resultSet.getInt("id"),
					resultSet.getString("name"), resultSet.getString("c_type"),
					resultSet.getDate("close_date"),
					bet_podiumdao.findbyidcompetition(resultSet.getInt("id")),
					competitordao.getPodiumCompetitionListCompetitors(resultSet
							.getInt("id")));
		}

		resultSet.close();
		psSelect.close();
		c.close();
		return competition_podium;
	}
}
