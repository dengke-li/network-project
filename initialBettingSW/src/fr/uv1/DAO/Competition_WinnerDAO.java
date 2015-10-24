package fr.uv1.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.uv1.Competition.Competition_Winner;
import fr.uv1.Competitor.PCompetitor;
import fr.uv1.bettingServices.BadParametersException;
import fr.uv1.bettingServices.DatabaseConnection;

/**
 * DAO class (<i>Data Access Object</i>) for the {@link Competition_Winner} class.
 * This class provides the CRUD functionalities :<br>
 * <ul>
 *   <li><b>C</b>: create a new Competition_Winner in the database.
 *   <li><b>R</b>: retrieve (or read) a (list of)Competition_Winner(s) in the database.
 *   <li><b>U</b>: update the values stored in the database for a Competition_Winner.
 *   <li><b>D</b>: delete a Competition_Winner in the database.
 * </ul>
 * 
 */

public class Competition_WinnerDAO {

  //-----------------------------------------------------------------------------
  /**
   * Store a Competition_Winner in the database. This Competition_Winner is not stored
   * yet, so his <code>id</code> value is <code>NULL</code>. Once the
   * Competition_Winner is stored, the method returns the Competition_Winner with the
   * <code>id</code> value setted.
   * 
   * @param Competition_Winner the Competition_Winner to be stored.
   * @return the Competition_Winner with the updated value for the id.
   * @throws SQLException
   */
  public Competition_Winner persist(Competition_Winner competition_winner) throws SQLException
  {
    // Two steps in this methods which must be managed in an atomic
    // (unique) transaction:
    //   1 - insert the new Competition_Winner;
    //   2 - once the insertion is OK, in order to set up the value
    //       of the id, a request is done to get this value by
    //       requesting the sequence (Competition_Winners_id_seq) in the
    //       database.
    Connection c = DatabaseConnection.getConnection();
    try
    {
      c.setAutoCommit(false);
      PreparedStatement psPersist = c.prepareStatement("insert into competitions_winner(name, c_type, close_date)  values ( ?, ?, ?)");

      psPersist.setString(1, competition_winner.getName());
      psPersist.setString(2, competition_winner.getType());
      psPersist.setDate(3, competition_winner.getClose_date());   
      psPersist.executeUpdate();
      
      psPersist.close();
      
      // Retrieving the value of the id with a request on the
      // sequence (Competition_Winners_id_seq).
      PreparedStatement psIdValue = c.prepareStatement("select currval('competitions_winner_id_seq') as value_id");
      ResultSet resultSet = psIdValue.executeQuery();
      Integer id  = null;
      while(resultSet.next())
      {
        id = resultSet.getInt("value_id");
      }
      resultSet.close();
      psIdValue.close();
      c.commit();
      competition_winner.setId(id);
    }
    catch (SQLException e)
    {
      try
      {
        c.rollback();
      }
      catch (SQLException e1)
      {
        e1.printStackTrace();
      }
      c.setAutoCommit(true);
      throw e;
    }
    
    c.setAutoCommit(true);
    c.close();
    
    return competition_winner;
  }
  //-----------------------------------------------------------------------------
  /**
   * Find a Competition_Winner by his id.
   * 
   * @param id the id of the Competition_Winner to retrieve.
   * @return the Competition_Winner or null if the id does not exist in the database.
   * @throws SQLException
   * @throws BadParametersException 
   */

public Competition_Winner findById(Integer id) throws SQLException, BadParametersException
  {
    // 1 - Get a database connection from the class 'DatabaseConnection' 
    Connection c = DatabaseConnection.getConnection();

    // 2 - Creating a Prepared Statement with the SQL instruction.
    //     The parameters are represented by question marks. 
    PreparedStatement psSelect = c.prepareStatement("select * from competitions_winner where id=?");
    
    // 3 - Supplying values for the prepared statement parameters (question marks).
    psSelect.setInt(1, id);

    // 4 - Executing Prepared Statement object among the database.
    //     The return value is a Result Set containing the data.
    ResultSet resultSet = psSelect.executeQuery();
    
    // 5 - Retrieving values from the Result Set.
    Competition_Winner Competition_Winner = null;
    Bet_WinnerDAO bet_winnerdao= new Bet_WinnerDAO();
    PCompetitorDAO competitordao= new PCompetitorDAO();
    while(resultSet.next())
    {
      Competition_Winner = new Competition_Winner(resultSet.getInt("id"),
                                  resultSet.getString("name"), 
                                  resultSet.getString("c_type"),
                                  resultSet.getDate("close_date"),
                                  bet_winnerdao.findbyidcompetition(resultSet.getInt("id")),
                                  competitordao.getWinnerCompetitionListCompetitors(resultSet.getInt("id")),
                                  null);
    }
    
    // 6 - Closing the Result Set
    resultSet.close();
    
    // 7 - Closing the Prepared Statement.
    psSelect.close();
    
    // 8 - Closing the database connection.
    c.close();
    
    return Competition_Winner;
  }


/**
 * Find a Competition_Winner by date.
 *
 * @param id the id of the Competition_Winner to retrieve.
 * @return the Competition_Winner or null if the id does not exist in the database.
 * @throws SQLException
 * @throws BadParametersException
 */
/**
 * 
 * @param date
 * @return
 * @throws SQLException
 * @throws BadParametersException
 */
	public  Collection<Competition_Winner> findByDate(Date date) throws SQLException, BadParametersException
	{
	          Connection c = DatabaseConnection.getConnection();
	          PreparedStatement psSelect = c.prepareStatement("select * from competitions_winner where close_date > ?");
	          psSelect.setDate(1, date);
	          ResultSet resultSet = psSelect.executeQuery();
	          Collection<Competition_Winner> Competition_Winners = new ArrayList<Competition_Winner>();
	          PCompetitorDAO competitordao=new PCompetitorDAO();
	          Bet_WinnerDAO bet_winnerdao=new Bet_WinnerDAO();
	          while(resultSet.next())
	          {
	            Competition_Winners.add(new Competition_Winner(resultSet.getInt("id"),
	            												resultSet.getString("name"),
	            												resultSet.getString("c_type"),
	            												 resultSet.getDate("close_date"),
	            												bet_winnerdao.findbyidcompetition(resultSet.getInt("id")),
	            												competitordao.getWinnerCompetitionListCompetitors(resultSet.getInt("id")),
	            												null));
	          }
	          resultSet.close();
	          psSelect.close();
	          c.close();
	          
	          return Competition_Winners;
	}



  //-----------------------------------------------------------------------------
  /**
   * Find all the Competition_Winners in the database.
   * 
   * @return
   * @throws SQLException
   * @throws BadParametersException 
   */
  

public Collection<Competition_Winner> findAll() throws SQLException, BadParametersException
  {
    Connection c = DatabaseConnection.getConnection();
    PreparedStatement psSelect = c.prepareStatement("select * from competitions_winner order by id");
    ResultSet resultSet = psSelect.executeQuery();
    Collection<Competition_Winner> Competition_Winners = new ArrayList<Competition_Winner>();
    PCompetitorDAO competitordao=new PCompetitorDAO();
    Bet_WinnerDAO bet_winnerdao=new Bet_WinnerDAO();
    while(resultSet.next())
    {
   
    	Competition_Winner newcompetition_winner=new Competition_Winner(	 resultSet.getInt("id"),
	    		  										 resultSet.getString("name"), 
	    		  										 resultSet.getString("c_type"),
	    		  										  resultSet.getDate("close_date"),
	      												 null,
	      												 competitordao.getWinnerCompetitionListCompetitors(resultSet.getInt("id")),
	      												 null);
      newcompetition_winner.setBet_Winners(bet_winnerdao.findbyidcompetition(resultSet.getInt("id")));
      Competition_Winners.add(newcompetition_winner);
    }
    resultSet.close();
    psSelect.close();
    c.close();
    
    return Competition_Winners;
  }
  //-----------------------------------------------------------------------------
  /**
   * Update on the database the values from a Competition_Winner.
   * 
   * @param Competition_Winner the Competition_Winner to be updated.
   * @throws SQLException
   */
  public void update(Competition_Winner competition_winner) throws SQLException
  {
    // 1 - Get a database connection from the class 'DatabaseConnection' 
    Connection c = DatabaseConnection.getConnection();

    // 2 - Creating a Prepared Statement with the SQL instruction.
    //     The parameters are represented by question marks. 
    PreparedStatement psUpdate = c.prepareStatement("update competitions_winner set name=?, c_type=?, close_date=?   where id=?");

    // 3 - Supplying values for the prepared statement parameters (question marks).
    psUpdate.setString(1, competition_winner.getName());
    psUpdate.setString(2, competition_winner.getType());
    psUpdate.setDate(3, competition_winner.getClose_date());
    psUpdate.setInt(4, competition_winner.getId());

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
  //-----------------------------------------------------------------------------
  /**
   * Delete from the database a specific Competition_Winner.<br>
   * <i>Be careful: the delete functionality does not operate a delete
   * cascade on bets belonging to the Competition_Winner.</i>
   * 
   * @param Competition_Winner the Competition_Winner to be deleted.
   * @throws SQLException
   */
  public void delete(Competition_Winner competition_winner) throws SQLException
  {
    Connection c = DatabaseConnection.getConnection();   
    PreparedStatement psUpdate = c.prepareStatement("delete from competitions_winner where id=?");
    psUpdate.setInt(1, competition_winner.getId());
    psUpdate.executeUpdate();
    psUpdate.close();
    c.close();
  }
  
  /**
   * add winner(result) to a competition
   * @param Competition_Winner
   * @param winner
   * @return void
   * @throws SQLException
   */
  public void  resulutpersist(Competition_Winner competition_winner, PCompetitor winner) throws SQLException
  {
  
	  Connection c = DatabaseConnection.getConnection();
	    try
	    {
	      c.setAutoCommit(false);
	      PreparedStatement psPersist = c.prepareStatement("insert into result_competitions_podium (competition_winner_id, result_id_competitor )  values ( ?, ?)");

	      psPersist.setInt(1, competition_winner.getId());
	      psPersist.setInt(2, competition_winner.getWinner().getId()); 
	      psPersist.executeUpdate();
	      
	      psPersist.close();
	      
	      // Retrieving the value of the id with a request on the
	      // sequence (Competition_Winners_id_seq).

	       c.commit();
	    }
	    catch (SQLException e)
	    {
	      try
	      {
	        c.rollback();
	      }
	      catch (SQLException e1)
	      {
	        e1.printStackTrace();
	      }
	      c.setAutoCommit(true);
	      throw e;
	    }
	    
	    c.setAutoCommit(true);
	    c.close();
	  }
  /**
   * find the result of a competition_winner
   * return the winner
   * @param Competition_Winner
   * @param winner
   * @return void
   * @throws SQLException
   */
	  public PCompetitor findresult(Competition_Winner competition_Winner ) throws SQLException, BadParametersException
	  {
	    Connection c = DatabaseConnection.getConnection();
	    PreparedStatement psSelect = c.prepareStatement("select * from result_competitions_winner where competition_podium_id=?");
	    psSelect.setInt(1, competition_Winner.getId());
	    ResultSet resultSet = psSelect.executeQuery();
	    PCompetitorDAO competitordao= new PCompetitorDAO();
	    PCompetitor winner=competitordao.findById(resultSet.getInt("result_id_competitor"));
	
	    // Closing the Result Set
	    resultSet.close();
	    
	    // Closing the Prepared Statement.
	    psSelect.close();
	    
	    //  Closing the database connection.
	    c.close();
	    
	    return winner;
	  }
  
	//-----------------------------------------------------------------------------
	/**
	 * Find a Competition_Winner by name type close_date.
	 *
	 * @param id the id of the Competition_Winner to retrieve.
	 * @return the Competition_Winner or null if the id does not exist in the database.
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	
	public Competition_Winner find(String name, String type, Date a_close_date) throws SQLException, BadParametersException
	{
	  // 1 - Get a database connection from the class 'DatabaseConnection'
	  Connection c = DatabaseConnection.getConnection();
	
	  // 2 - Creating a Prepared Statement with the SQL instruction.
	  //     The parameters are represented by question marks.
	  PreparedStatement psSelect = c.prepareStatement("select * from competitions_winner where name=? and type=? and close_date=?");
	  
	  // 3 - Supplying values for the prepared statement parameters (question marks).
	  psSelect.setString(1, name);
	  psSelect.setString(2, type);
	  psSelect.setDate(3, (java.sql.Date) a_close_date);
	
	  // 4 - Executing Prepared Statement object among the database.
	  //     The return value is a Result Set containing the data.
	  ResultSet resultSet = psSelect.executeQuery();
	  
	  // 5 - Retrieving values from the Result Set.
	  Competition_Winner competition_winner = null;
	  Bet_WinnerDAO bet_winnerdao= new Bet_WinnerDAO();
	  PCompetitorDAO competitordao= new PCompetitorDAO();
	  while(resultSet.next())
	  {
	    competition_winner = new Competition_Winner(resultSet.getInt("id"),
	                                resultSet.getString("name"),
	                                resultSet.getString("c_type"),
	                                resultSet.getDate("close_date"),
	                                bet_winnerdao.findbyidcompetition(resultSet.getInt("id")),
	                                competitordao.getWinnerCompetitionListCompetitors(resultSet.getInt("id")),
	                                null
	                                );
	  }
	  
	  // 6 - Closing the Result Set
	  resultSet.close();
	  
	  // 7 - Closing the Prepared Statement.
	  psSelect.close();
	  
	  // 8 - Closing the database connection.
	  c.close();
	  
	  return competition_winner;
	 }

		/**
		 * find a competition_winner by name of competition
		 * @param name
		 * @return the right competition
		 * @throws SQLException
		 * @throws BadParametersException
		 */
		public Competition_Winner findbyname(String name) throws SQLException, BadParametersException
		{
		  Connection c = DatabaseConnection.getConnection();
		  PreparedStatement psSelect = c.prepareStatement("select * from competitions_winner where name=?");	  
		  psSelect.setString(1, name);	
		  ResultSet resultSet = psSelect.executeQuery();	  
		  Competition_Winner competition_winner = null;
		  Bet_WinnerDAO bet_winnerdao= new Bet_WinnerDAO();
		  PCompetitorDAO competitordao= new PCompetitorDAO();
		  while(resultSet.next())
		  {
		    competition_winner = new Competition_Winner(resultSet.getInt("id"),
		                                				resultSet.getString("name"),
		                                				resultSet.getString("c_type"),
		                                				resultSet.getDate("close_date"),
		                                				bet_winnerdao.findbyidcompetition(resultSet.getInt("id")),
		                                				competitordao.getWinnerCompetitionListCompetitors(resultSet.getInt("id")));
		  }
		  
		  resultSet.close();
		  psSelect.close();	  
		  c.close();	  
		  return competition_winner;
		}
		  //-----------------------------------------------------------------------------
}

