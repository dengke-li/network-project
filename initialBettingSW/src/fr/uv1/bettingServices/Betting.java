package fr.uv1.bettingServices;

import java.sql.SQLException;
import java.util.*;

import fr.uv1.Competition.Competition;
import fr.uv1.Competitor.*;

/**
 * 
 * @author prou + mallet <br>
 * <br>
 *         This interface declares all methods that should be provided by a
 *         betting software. <br>
 * 
 */
public interface Betting {

	/**
	 * Subscribe.
	 * 
	 * @param a_name
	 *            the last name of the subscriber.
	 * @param a_firstName
	 *            the first name of the subscriber.
	 * @param a_username
	 *            the username of the subscriber.
	 * @param a_managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthenticationException
	 *             raised if the manager's password is incorrect.
	 * @throws ExistingSubscriberException
	 *             raised if a subscriber exists with the same username.
	 * @throws BadParametersException
	 *             raised if last name, first name or username are invalid.
	 * 
	 * @return password for the new subscriber
	 */
	String subscribe(String a_name,
			         String a_firstName, 			        
			         String a_borndate,
			         String a_username,
			         String a_managerPwd )
			throws AuthenticationException, ExistingSubscriberException,
			SubscriberException, BadParametersException;

	/**
	 * delete a subscriber.
	 * 
	 * @param a_username
	 *            the username of the subscriber.
	 * @param a_managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthenticationException
	 *             raised if the manager's password is incorrect.
	 * @throws ExistingSubscriberException
	 *             raised if username is not registered.
	 * @throws BadParametersException
	 * 
	 */
	long unsubscribe(String a_username, String a_managerPwd)
			throws AuthenticationException, ExistingSubscriberException;

	/**
	 * list subscribers.
	 * 
	 * @param a_managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthenticationException
	 *             raised if the manager's password is incorrect.
	 * 
	 * @return a list of list of strings:
	 *         <ul>
	 *         <li>subscriber's lastname</li>
	 *         <li>subscriber's firstname</li>
	 *         <li>subscriber's username</li>
	 *         </ul>
	 * @throws BadParametersException
	 * @throws SQLException
	 */
	ArrayList<ArrayList<String>> listSubscribers(String a_managerPwd)
			throws AuthenticationException;

	/**
	 * authenticate manager.
	 * 
	 * @param a_managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthenticationException
	 *             raised if the manager's password is incorrect.
	 */
	void authenticateMngr(String a_managerPwd) throws AuthenticationException;

	/**
	 * change manager's password.
	 * 
	 * @param newPwd
	 *            the new manager's password.
	 * @param currentPwd
	 *            the manager's password.
	 * 
	 * @throws AuthenticationException
	 *             raised if the current manager's password is incorrect.
	 * 
	 * @throws BadParametersException
	 *             raised if the new manager's password is invalid.
	 */

	/*
	 * i don't konw
	 */
	void changeMngrPwd(String newPwd, String currentPwd)
			throws AuthenticationException, BadParametersException;

	/**
	 * add a competition.
	 * 
	 * @param a_competition
	 * 
	 * @param a_competitorCalendar
	 * 
	 * @param competitors
	 * 
	 * @param a_managerPwd
	 * 
	 * @throws AuthenticationException
	 * 
	 * @throws BadParametersException
	 * 
	 * @return the found subscriber or null
	 */
	/* management of competition */
	void addCompetition(String a_competition, Calendar a_closingDate,
			Collection<Competitor> competitors, String a_managerPwd)
			throws AuthenticationException, BadParametersException,
			ExistingCompetitionException, CompetitionException;

	/**
	 * 
	 * @param a_competition
	 * @param a_managerPwd
	 * @throws AuthenticationException
	 * @throws ExistingCompetitionException
	 * @throws CompetitionException
	 */
	void cancelCompetition(String a_competition, String a_managerPwd)
			throws AuthenticationException, ExistingCompetitionException,
			CompetitionException;

	Collection<Competition> listCompetitions();
/**
 * 
 * @param a_competition
 * @param a_competitor
 * @param a_managerPwd
 * @throws AuthenticationException
 * @throws BadParametersException
 * @throws ExistingCompetitionException
 * @throws CompetitionException
 * @throws ExistingCompetitorException
 */
	void addCompetitor(String a_competition, Competitor a_competitor,
			String a_managerPwd) throws AuthenticationException,
			BadParametersException, ExistingCompetitionException,
			CompetitionException, ExistingCompetitorException;

	void deleteCompetitor(String a_competition, Competitor a_competitor,
			String a_managerPwd) throws AuthenticationException,
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException;

	Collection<Competitor> listCompetitors(String a_competition)
			throws ExistingCompetitionException, CompetitionException;

	/**
	 * creat a competitor person
	 * 
	 * @param lastName
	 * @param firstName
	 * @param borndate
	 * @param managerPwd
	 * @return
	 * @throws AuthenticationException
	 * @throws ExistingCompetitorException
	 * @throws BadParametersException
	 */
	Competitor createCompetitor(String lastName, String firstName,
			String borndate, String managerPwd) throws AuthenticationException,
			ExistingCompetitorException, BadParametersException;

	/**
	 * creat a competitor team
	 * @param lastName
	 * @param firstName
	 * @param borndate
	 * @param managerPwd
	 * @return
	 * @throws AuthenticationException
	 * @throws ExistingCompetitorException
	 * @throws BadParametersException
	 */
	Competitor createCompetitor(String name ,String managerPwd) throws AuthenticationException,
			ExistingCompetitorException, BadParametersException;


	/**
	 * 
	 * @param number_tokens
	 * @param a_competition
	 * @param a_winner
	 * @param a_second
	 * @param a_third
	 * @param a_username
	 * @param a_pwdSubs
	 * @throws AuthenticationException
	 * @throws BadParametersException
	 * @throws CompetitionException
	 * @throws ExistingCompetitionException
	 * @throws SubscriberException
	 */
	public void betOnPodium(long number_tokens, String a_competition,
			Competitor a_winner, Competitor a_second, Competitor a_third,
			String a_username, String a_pwdSubs)
			throws AuthenticationException,
            CompetitionException,
            ExistingCompetitionException,
            SubscriberException,
            BadParametersException;

	/**
	 * @param number_tokens
	 * @param a_competition
	 * @param a_winner
	 * @param a_username
	 * @param a_pwdSubs
	 * @throws AuthenticationException
	 * @throws BadParametersException
	 * @throws CompetitionException
	 * @throws ExistingCompetitionException
	 * @throws SubscriberException
	 */
	void betOnWinner(long number_tokens, String a_competition,
			Competitor a_winner, String a_username, String a_pwdSubs)
			throws AuthenticationException, BadParametersException,
			CompetitionException, ExistingCompetitionException,
			SubscriberException;
	/**
	 * 
	 * @param a_competition
	 * @param a_username
	 * @param a_pwdSubs
	 * @throws AuthenticationException
	 * @throws CompetitionException
	 * @throws ExistingCompetitionException
	 */
	void deleteBetsCompetition(String a_competition, String a_username,
			String a_pwdSubs) throws AuthenticationException,
			CompetitionException, ExistingCompetitionException;
/**
 * 
 * @param a_competition
 * @param a_winner
 * @param a_second
 * @param a_third
 * @param a_managerPwd
 * @throws AuthenticationException
 * @throws ExistingCompetitionException
 * @throws CompetitionException
 */
	void settlePodium(String a_competition, Competitor a_winner,
			Competitor a_second, Competitor a_third, String a_managerPwd)
			throws AuthenticationException, ExistingCompetitionException,
			CompetitionException;
/**
 * 
 * @param a_competition
 * @param a_winner
 * @param a_managerPwd
 * @throws AuthenticationException
 * @throws ExistingCompetitionException
 * @throws CompetitionException
 */
	void settleWinner(String a_competition, Competitor a_winner,
			String a_managerPwd) throws AuthenticationException,
			ExistingCompetitionException, CompetitionException;

	/* management of subscriber */
	/**
	 * 
	 * @param a_username
	 * @param newPwd
	 * @param currentPwd
	 * @throws AuthenticationException
	 * @throws BadParametersException
	 */
	void changeSubsPwd(String a_username, String newPwd, String currentPwd)
			throws AuthenticationException, BadParametersException;
/**
 * 
 * @param a_username
 * @param number_tokens
 * @param a_managerPwd
 * @throws AuthenticationException
 * @throws BadParametersException
 * @throws ExistingSubscriberException
 */
	void creditSubscriber(String a_username, long number_tokens,
			String a_managerPwd) throws AuthenticationException,
			BadParametersException, ExistingSubscriberException;
/**
 * 
 * @param a_username
 * @param number_tokens
 * @param a_managerPwd
 * @throws AuthenticationException
 * @throws BadParametersException
 * @throws ExistingSubscriberException
 * @throws SubscriberException
 */
	void debitSubscriber(String a_username, long number_tokens,
			String a_managerPwd) throws AuthenticationException,
			BadParametersException, ExistingSubscriberException,
			SubscriberException;
/**
 * 
 * @param a_username
 * @param a_pwdSubs
 * @return
 * @throws AuthenticationException
 */
	ArrayList<String> infosSubscriber(String a_username, String a_pwdSubs)
			throws AuthenticationException;

	/* consulter */
	/**
	 * 
	 * @param a_competition
	 * @return
	 * @throws ExistingCompetitionException
	 */
	ArrayList<String> consultBetsCompetition(String a_competition)
			throws ExistingCompetitionException;

}
