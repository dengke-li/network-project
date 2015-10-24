package fr.uv1.bettingServices;

import java.sql.Date;
import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.*;

import fr.uv1.Bet.*;
import fr.uv1.Competition.*;
import fr.uv1.Competitor.*;
import fr.uv1.DAO.*;
import fr.uv1.Subscriber.*;
import fr.uv1.utils.Utils;

;

/**
 * @author G3B3 <br>
 * <br>
 *         This class implements methods of the interface Betting. <br>
 * <br>
 *         <ul>
 *         <li>manager password validity:
 *         <ul>
 *         <li>only letters and digits are allowed</li>
 *         <li>password size should be at least 8 characters</li>
 *         </ul>
 *         </li>
 *         </ul>
 */

public class BettingSoft implements Betting {

	/*
	 * Manager password
	 */
	private String managerPassword;

	/*
	 * Password verifier
	 */
	private BettingPasswordsVerifier pv;

	/*
	 * Subscribers of the betting software
	 */
	private Collection<Subscriber> subscribers;

	private SubscriberDAO subscriberDAO;
	private Competition_PodiumDAO competition_podiumDAO;
	private Competition_WinnerDAO competition_winnerDAO;
	private Bet_PodiumDAO bet_podiumDAO;
	private Bet_WinnerDAO bet_winnerDAO;
	private PCompetitorDAO competitorDAO;
	private Date now;

	/**
	 * constructor of BettingSoft
	 * 
	 * @param a_managerPwd
	 *            manager password.
	 * 
	 * @throws BadParametersException
	 *             raised if a_managerPwd is incorrect.
	 */
	@SuppressWarnings("deprecation")
	public BettingSoft(String a_managerPwd) throws BadParametersException {
		// Create the verifier
		pv = new BettingPasswordsVerifier();

		// The password should be valid
		setManagerPassword(a_managerPwd);
		this.subscribers = new ArrayList<Subscriber>();
		// Create DAOs
		subscriberDAO = new SubscriberDAO();
		competition_podiumDAO = new Competition_PodiumDAO();
		competition_winnerDAO = new Competition_WinnerDAO();
		bet_winnerDAO = new Bet_WinnerDAO();
		bet_podiumDAO = new Bet_PodiumDAO();
		competitorDAO = new PCompetitorDAO();
		Calendar cal = Calendar.getInstance();
		this.now = new Date(cal.get(Calendar.YEAR) - 1900,
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}

	// **************************magagement password***********************
	/**
	 * Set the manager's password
	 * 
	 * @param managerPassword
	 * @throws BadParametersException
	 */
	private void setManagerPassword(String managerPassword)
			throws BadParametersException {
		if (managerPassword == null)
			throw new BadParametersException("manager's password not valid");
		// The password should be valid
		if (!pv.verify(managerPassword.toCharArray()))
			throw new BadParametersException("manager's password not valid");
		this.managerPassword = managerPassword;
	}

	/**
	 * From Betting interface
	 */
	@Override
	public void authenticateMngr(String a_managerPwd)
			throws AuthenticationException {
		if (a_managerPwd == null)
			throw new AuthenticationException("invalid manager's password");

		if (!this.managerPassword.equals(a_managerPwd))
			throw new AuthenticationException("incorrect manager's password");
	}

	/**
	 * From Betting interface
	 */

	@Override
	public void changeMngrPwd(String newPwd, String currentPwd)
			throws AuthenticationException, BadParametersException {
		// Authenticate manager
		authenticateMngr(currentPwd);
		// Change password if valid
		setManagerPassword(newPwd);
	}

	/**
	 * authenticate the subscriber
	 * 
	 * @param a_pwdSubs
	 * @param a_username
	 *            @ * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	public void authenticateSubs(String a_pwdSubs, String a_username)
			throws BadParametersException, AuthenticationException {
		Subscriber subscriber = null;
		try {
			subscriber = subscriberDAO.findByusernamepwd(a_pwdSubs, a_username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (subscriber == null) {
			throw new AuthenticationException(
					"incorrect subscriber's username ou password");
		}
	}

	// ********************management
	// subscriber*******************************************
	/**
	 * From Betting interface
	 */

	@Override
	public String subscribe(String a_lastname, String a_firstName,
			String a_borndate, String a_username, String a_managerPwd)
			throws AuthenticationException, ExistingSubscriberException,
			BadParametersException, SubscriberException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Look if a subscriber with the same username already exists
		try {
			Subscriber s = null;
			s = searchSubscriberByUsername(a_username);
			if (Utils.getage(Utils.stringToDate(a_borndate)) < 18)
				throw new SubscriberException("A subscriber is minor");
			// the subscriber exists already (judge by username)
			if (s != null)
				throw new ExistingSubscriberException(
						"A subscriber with the same username already exists");
			// the subscriber is minor
			/* The a_borndate must be in format (dd/MM/yyyy) */
			// int age = cal.get(Calendar.YEAR)
			// - Utils.stringToDate(a_borndate).getYear() - 1900;

			// System.out.println("cal.get(Calendar.YEAR):"+cal.get(Calendar.YEAR));
			// System.out.println("Date.valueOf(a_borndate).getYear():"+Date.valueOf(a_borndate).getYear());
			// System.out.println("age:"+age);

			s = new Subscriber(a_lastname, a_firstName, a_username, a_borndate,
					0);
			subscriberDAO.persist(s);
			System.out.println(s);
			return searchSubscriberByUsername(a_username).getPassword();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * this method is used for deleting un subscriber.
	 * 
	 * @throws BadParametersException
	 */
	@Override
	public long unsubscribe(String a_username, String a_managerPwd)
			throws AuthenticationException, ExistingSubscriberException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Look if a subscriber with the username exists
		try {
			Subscriber s = searchSubscriberByUsername(a_username);

			if (s != null) {

				subscriberDAO.delete(s);
				// remove it
				return s.getAccount();
			}

			else {
				throw new ExistingSubscriberException(
						"Subscriber does not exist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * List all the subscribers
	 * 
	 * @throws BadParametersException
	 */
	@Override
	public ArrayList<ArrayList<String>> listSubscribers(String a_managerPwd)
			throws AuthenticationException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Calculate the list of subscribers

		// ArrayList<String> subsData = new ArrayList<String>();
		List<Subscriber> subscribers = null;
		try {
			subscribers = subscriberDAO.findAll();
		} catch (SQLException | BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for (Subscriber s : subscribers) {
			ArrayList<String> subsData = new ArrayList<String>();
			subsData.add(s.getLastName());
			subsData.add(s.getFirstName());
			subsData.add(s.getUsername());
			result.add(subsData);
		}
		return result;
	}

	/**
	 * search a subscriber by username
	 * 
	 * @param a_username
	 *            the username of the subscriber.
	 * 
	 * @return the found subscriber or null
	 * @throws BadParametersException
	 */
	private Subscriber searchSubscriberByUsername(String a_username)
			throws BadParametersException {
		if (a_username == null)
			return null;
		Subscriber s = null;
		try {
			s = subscriberDAO.findByUsername(a_username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;
	}

	/**
	 * From Betting interface
	 */
	@Override
	public ArrayList<String> infosSubscriber(String a_username, String a_pwdSubs)
			throws AuthenticationException {
		try {
			// Authenticate subscriber
			authenticateSubs(a_pwdSubs, a_username);
			ArrayList<String> infoSubscriber = new ArrayList<String>();
			Integer Subscriber_id = null;

			Subscriber s = subscriberDAO.findByUsername(a_username);
			Subscriber_id = s.getId();

			infoSubscriber.add("[" + s.getLastName() + "]");
			infoSubscriber.add("[" + s.getFirstName() + "]");
			infoSubscriber.add("[" + s.getBorndate() + "]");
			infoSubscriber.add("[" + s.getUsername() + "]");
			infoSubscriber.add("[" + Long.toString(s.getAccount()) + "]");

			ArrayList<String> info_bets = new ArrayList<String>();
			long sum_tokens = 0;
			int number_tokens = 0;
			Collection<Bet_Podium> current_bets_podium;
			Collection<Bet_Winner> current_bets_winner;
			current_bets_winner = new ArrayList<Bet_Winner>();
			current_bets_podium = new ArrayList<Bet_Podium>();

			current_bets_podium = bet_podiumDAO
					.findBySubscriber_id(Subscriber_id);
			current_bets_winner = bet_winnerDAO
					.findBySubscriber_id(Subscriber_id);

			for (Bet_Podium b : current_bets_podium) {

				info_bets.add("[" + "podium N°" + Integer.toString(b.getId())
						+ ": " + Long.toString(b.getBet_number_tokens()) + "]");
				sum_tokens = sum_tokens + b.getBet_number_tokens();
				number_tokens++;
			}

			for (Bet_Winner b : current_bets_winner) {
				info_bets.add("[" + "winner N°" + Integer.toString(b.getId())
						+ ": " + Long.toString(b.getBet_number_tokens()) + "]");
				sum_tokens = sum_tokens + b.getBet_number_tokens();
				number_tokens++;
			}
			infoSubscriber.add("[" + number_tokens + "]");
			infoSubscriber.add("[" + Long.toString(sum_tokens) + "]");
			infoSubscriber.addAll(info_bets);
			System.out.println(infoSubscriber);
			return infoSubscriber;
		} catch (SQLException | BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// ********************************management
	// competition***********************************************************
	/**
	 * according the type of the competition,add a competition to the
	 * corresponding table
	 */
	@Override
	public void addCompetition(String a_competition, Calendar a_closingDate,
			Collection<Competitor> competitors, String a_managerPwd)
			throws AuthenticationException, BadParametersException,
			ExistingCompetitionException, CompetitionException {
		System.out.println("you have win89");
		if (a_competition == null) {
			throw new BadParametersException("name of competition not valide");
		}
		if (a_competition.length() < 1)
			throw new BadParametersException(
					"name length less than 1 character");
		// First character a letter
		// char cn = a_competition.charAt(0);
		// for (int i = 1; i < a_competition.length(); i++) {
		// cn = a_competition.charAt(i);
		// if (!Character.isLetter(cn) && (cn != '_'))
		// throw new BadParametersException(
		// "the name "
		// + a_competition
		// + " contains other characters than letters, dashes and spaces");
		// }
		if (competitors == null) {
			throw new BadParametersException(
					"liste of competitors is not valide");
		}

		if (a_closingDate.before(Calendar.getInstance())) {
			throw new CompetitionException(
					"the closing date is in the past (competition closed)");
		}
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Look if a subscriber with the same username already exists
		// DateFormat formatDeDate = new SimpleDateFormat("dd-MM-yyyy");

		// String year=Integer.toString(a_closingDate.get(Calendar.YEAR)-1900);
		// String month=Integer.toString(a_closingDate.get(Calendar.MONTH));
		// String
		// day=Integer.toString(a_closingDate.get(Calendar.DAY_OF_MONTH));
		// Date a_close_date = formatDeDate.parse(day+"/"+month+"/"+year);

		try {
			@SuppressWarnings({ "deprecation", "unused" })
			Date a_close_date = new Date(
					a_closingDate.get(Calendar.YEAR) - 1900,
					a_closingDate.get(Calendar.MONTH),
					a_closingDate.get(Calendar.DAY_OF_MONTH));

		} catch (Exception e) {
			throw new BadParametersException(
					"BornDate of competitor does not valide");
		}
		@SuppressWarnings("deprecation")
		Date a_close_date = new Date(a_closingDate.get(Calendar.YEAR) - 1900,
				a_closingDate.get(Calendar.MONTH),
				a_closingDate.get(Calendar.DAY_OF_MONTH));
		String type = null;
		if (competitors.size() < 2) {
			throw new CompetitionException(
					" there are less than two competitors");
		} else if (competitors.size() == 2)
			type = "Winner";
		else
			type = "Podium";
		Competition c;
		try {
			c = searchCompetition(a_competition);

			if (c != null)
				throw new ExistingCompetitionException(
						"A competition with the same name already exists");

			for (Competitor com : competitors) {
				int number_of_same_com = 0;
				for (Competitor com2 : competitors) {

					if ((((PCompetitor) com).getFirstname()
							.equals(((PCompetitor) com2).getFirstname()))
							&& (((PCompetitor) com).getLastname()
									.equals(((PCompetitor) com2).getLastname()))
							&& (((PCompetitor) com).getBorndate()
									.equals(((PCompetitor) com2).getBorndate()))) {
						number_of_same_com++;

					}
				}
				if (number_of_same_com >= 2) {
					throw new CompetitionException(
							"two or more competitors are the same (firstname, lastname, borndate)");
				}

			}

			for (Competitor com : competitors) {
				// for(Competitor com2 : competitors){

				if (!((PCompetitor) com).hasValidName()) {
					throw new BadParametersException();

				}
				Utils.stringToDate(((PCompetitor) com).getBorndate());
				// for validation of the Date

			}

			// add the competition by the type
			if (type.equals("Winner")) {

				c = new Competition_Winner(a_competition, type, a_close_date);
				competition_winnerDAO.persist((Competition_Winner) c);
				// *******************************//

			}

			else if (type.equals("Podium")) {
				c = new Competition_Podium(a_competition, type, a_close_date);
				competition_podiumDAO.persist((Competition_Podium) c);
			}
			// if (number_of_same_com>=2) {
			// throw new CompetitionException(
			// "two or more competitors are the same (firstname, lastname, borndate)");
			// }
			for (Competitor com : competitors) {
				try {
					this.addCompetitor(a_competition, com, managerPassword);
				} catch (ExistingCompetitorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("new competition =" + c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * according the type of the competition,delete a competition to the
	 * corresponding table
	 */

	@Override
	public void cancelCompetition(String a_competition, String a_managerPwd)
			throws AuthenticationException, ExistingCompetitionException,
			CompetitionException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Look if a subscriber with the username exists
		try {
			Competition c = searchCompetition(a_competition);
			System.out.println(c);

			if (c != null) {

				if (c.getClose_date().before(now)) {
					throw new CompetitionException(
							"the closing date is in the past (competition closed)");
				}
				Collection<PCompetitor> competitors = c.getList_competitor();

				// delete the relation between competitions and
				// bet****************************
				if (c.getType().equals("Winner")) {
					bet_winnerDAO.deletebycompetition((Competition_Winner) c);
				}

				if (c.getType().equals("Podium")) {

					bet_podiumDAO.deletebycompetition((Competition_Podium) c);
				}
				// delete the relation between competitions and competitors
				for (PCompetitor competitor : competitors) {
					// System.out.println(competitor);
					competitorDAO.playdelete(competitor, c);
				}

				// delete the competition by the type
				if (c.getType().equals("Winner")) {
					competition_winnerDAO.delete((Competition_Winner) c);
				}

				if (c.getType().equals("Podium")) {

					competition_podiumDAO.delete((Competition_Podium) c);
				}

			} else {
				throw new ExistingCompetitionException(
						"Competition does not exist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * list competitions in progress(non-Javadoc)
	 * 
	 * @see fr.uv1.bettingServices.Betting#listCompetitions()
	 */
	@Override
	public Collection<Competition> listCompetitions() {

		ArrayList<Competition_Winner> competition_winners;
		ArrayList<Competition_Podium> competition_podiums;
		Collection<Competition> c_total;

		competition_winners = new ArrayList<Competition_Winner>();
		competition_podiums = new ArrayList<Competition_Podium>();
		c_total = new ArrayList<Competition>();

		try {
			competition_winners = (ArrayList<Competition_Winner>) competition_winnerDAO
					.findAll();
			competition_podiums = (ArrayList<Competition_Podium>) competition_podiumDAO
					.findAll();
		} catch (SQLException | BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < competition_winners.size(); i++) {
			c_total.add(competition_winners.get(i));
		}
		for (int i = 0; i < competition_podiums.size(); i++) {
			c_total.add(competition_podiums.get(i));
		}

		return c_total;

	}

	/**
	 * find competition by name
	 * 
	 * @param a_name
	 * @return Competition
	 * @throws BadParametersException
	 */
	private Competition searchCompetition(String a_name)
			throws BadParametersException {
		if (a_name == null)
			return null;

		Competition_WinnerDAO competition_winnerDAO = new Competition_WinnerDAO();
		Competition_PodiumDAO competition_podiumDAO = new Competition_PodiumDAO();
		Competition_Winner cw = null;
		Competition_Podium cp = null;
		try {
			cw = competition_winnerDAO.findbyname(a_name);

			cp = competition_podiumDAO.findbyname(a_name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cw != null) {
			return cw;
		} else {
			return cp;
		}
	}

	/**
	 * search All Competition_Winners By Date
	 * 
	 * @param date
	 * @param a_managerPwd
	 * @return Collection<Competition_Winner>
	 * @throws AuthenticationException
	 * @throws ExistingCompetitorException
	 * @throws BadParametersException
	 */
	public Collection<Competition_Winner> searchAllCompetition_WinnersByDate(
			Date date, String a_managerPwd) throws AuthenticationException,
			ExistingCompetitorException, BadParametersException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);

		Competition_WinnerDAO competition_winnerDAO = new Competition_WinnerDAO();
		Collection<Competition_Winner> c1 = null;
		try {
			c1 = competition_winnerDAO.findByDate((java.sql.Date) date);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c1;
	}

	/**
	 * search All Competition_Podiums By Date
	 * 
	 * @param date
	 * @param a_managerPwd
	 * @return Collection<Competition_Podium>
	 * @throws AuthenticationException
	 * @throws ExistingCompetitorException
	 * @throws BadParametersException
	 */
	public Collection<Competition_Podium> searchAllCompetition_PodiumsByDate(
			Date date, String a_managerPwd) throws AuthenticationException,
			ExistingCompetitorException, BadParametersException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);

		Competition_PodiumDAO competition_podiumDAO = new Competition_PodiumDAO();
		Collection<Competition_Podium> c2 = null;
		try {
			c2 = competition_podiumDAO.findByDate((java.sql.Date) date);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c2;
	}

	// ***************************management
	// competitior**************************************************
	/**
	 * add the relation of a competitor and a competition
	 */
	@Override
	public void addCompetitor(String a_competition, Competitor a_competitor,
			String a_managerPwd) throws AuthenticationException,
			BadParametersException, ExistingCompetitionException,
			CompetitionException, ExistingCompetitorException {

		authenticateMngr(a_managerPwd);

		Utils.stringToDate(((PCompetitor) a_competitor).getBorndate());
		// for validation of the Date
		try {
			Competition_Winner c1;
			c1 = competition_winnerDAO.findbyname(a_competition);
			Competition_Podium c2;
			c2 = competition_podiumDAO.findbyname(a_competition);
			// System.out.println(c2);
			if (!a_competitor.hasValidName()) {
				throw new BadParametersException(
						"firstname, lastname are not valide");
			}
			// if (competitorDAO.findByName(
			// ((PCompetitor) a_competitor).getFirstname(),
			// ((PCompetitor) a_competitor).getLastname()) == null) {
			// throw new ExistingCompetitorException(
			// "don't find this competitor");
			// }

			if (c1 != null) {
				if (c1.getClose_date().before(now)) {
					throw new CompetitionException(
							" the closing date of the competition is in the past (competition closed)");
				}
				for (PCompetitor com : competitorDAO
						.getWinnerCompetitionListCompetitors(c1.getId())) {

					if (com.getBorndate().equals(
							((PCompetitor) a_competitor).getBorndate())
							&& com.getFirstname()
									.equals(((PCompetitor) a_competitor)
											.getFirstname())
							&& com.getLastname().equals(
									((PCompetitor) a_competitor).getLastname())) {
						throw new ExistingCompetitorException(
								" competitor is already registered for the competition");
					}
				}
				PCompetitor pc = competitorDAO.findByNameAndBornDate(
						((PCompetitor) a_competitor).getFirstname(),
						((PCompetitor) a_competitor).getLastname(),
						((PCompetitor) a_competitor).getBorndate());
				if (pc == null) {
					pc = competitorDAO.persist((PCompetitor) a_competitor);
				}
				competitorDAO.playpersist(pc, c1);
			} else if (c2 != null) {
				if (c2.getClose_date().before(now)) {
					throw new CompetitionException(
							" the closing date of the competition is in the past (competition closed)");
				}
				for (PCompetitor com : competitorDAO
						.getPodiumCompetitionListCompetitors(c2.getId())) {
					if (com.getBorndate() == ((PCompetitor) a_competitor)
							.getBorndate()
							&& com.getFirstname() == ((PCompetitor) a_competitor)
									.getFirstname()
							&& com.getLastname() == ((PCompetitor) a_competitor)
									.getLastname()) {
						throw new ExistingCompetitorException(
								" competitor is already registered for the competition");
					}
				}
				PCompetitor pc = competitorDAO.findByNameAndBornDate(
						((PCompetitor) a_competitor).getFirstname(),
						((PCompetitor) a_competitor).getLastname(),
						((PCompetitor) a_competitor).getBorndate());
				if (pc == null) {
					pc = competitorDAO.persist((PCompetitor) a_competitor);
				}
				competitorDAO.playpersist((PCompetitor) a_competitor, c2);
			} else
				throw new CompetitionException("can't find the competition");
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * delete a competitor of a competition
	 */
	@Override
	public void deleteCompetitor(String a_competition, Competitor a_competitor,
			String a_managerPwd) throws AuthenticationException,
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		authenticateMngr(a_managerPwd);
		try {
			try {

				Competition_Winner c1;
				c1 = competition_winnerDAO.findbyname(a_competition);

				Competition_Podium c2;
				c2 = competition_podiumDAO.findbyname(a_competition);

				if (c1 != null) {

					if (c1.getClose_date().before(now)) {
						throw new CompetitionException(
								" the closing date of the competition is in the past (competition closed)");
					}
					if (competitorDAO.getPodiumCompetitionListCompetitors(
							c1.getId()).size() <= 2) {
						throw new CompetitionException(
								" the number of remaining competitors is 2 before deleting");
					}
					competitorDAO.isplay((PCompetitor) a_competitor, c1);
					competitorDAO.playdelete((PCompetitor) a_competitor, c1);
				} else if (c2 != null) {
					if (c2.getClose_date().before(now)) {
						throw new CompetitionException(
								" the closing date of the competition is in the past (competition closed)");
					}
					if (competitorDAO.getPodiumCompetitionListCompetitors(
							c2.getId()).size() <= 2) {
						throw new CompetitionException(
								" the number of remaining competitors is 2 before deleting");
					}
					competitorDAO.isplay((PCompetitor) a_competitor, c2);
					competitorDAO.playdelete((PCompetitor) a_competitor, c2);
				} else
					throw new CompetitionException("can't find the competition");
			} catch (BadParametersException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * delete competitor from database
	 * 
	 * @param firstname
	 * @param lastname
	 * @param a_managerPwd
	 * @throws AuthenticationException
	 * @throws ExistingCompetitionException
	 * @throws CompetitionException
	 * @throws ExistingCompetitorException
	 */
	public void deleteCompetitorSystem(String firstname, String lastname,
			String a_managerPwd) throws AuthenticationException,
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		authenticateMngr(a_managerPwd);
		PCompetitorDAO competitordao = new PCompetitorDAO();
		try {
			PCompetitor c = competitordao.findByName(firstname, lastname);
			competitorDAO.playlistdelete(c);
			competitorDAO.delete(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * list competitor of a competition given
	 */
	@Override
	public Collection<Competitor> listCompetitors(String a_competition)
			throws ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		Collection<Competitor> result = new ArrayList<Competitor>();

		Competition c1, c2;
		try {
			c1 = (Competition) competition_winnerDAO.findbyname(a_competition);
			c2 = (Competition) competition_podiumDAO.findbyname(a_competition);

			Competition competition = null;
			if (c1 != null) {
				if (c1.getClose_date().before(now)) {
					throw new CompetitionException(
							" the closing date of the competition is in the past (competition closed)");
				}
				competition = c1;
			} else if (c2 != null) {
				if (c2.getClose_date().before(now)) {
					throw new CompetitionException(
							" the closing date of the competition is in the past (competition closed)");
				}
				competition = c2;
			} else {
				throw new ExistingCompetitionException(
						"the competition does not exist");
			}
			if (competition.getType().equals("Winner")) {

				for (PCompetitor com : competitorDAO
						.getWinnerCompetitionListCompetitors(competition
								.getId())) {

					result.add(com);
				}
				return result;
			} else if (competition.getType().equals("Podium")) {
				for (PCompetitor com : competitorDAO
						.getPodiumCompetitionListCompetitors(competition
								.getId())) {

					result.add(com);
				}
				return result;
			} else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Create person competitor
	@Override
	public Competitor createCompetitor(String a_lastname, String a_firstname,
			String borndate, String a_managerPwd)
			throws AuthenticationException, ExistingCompetitorException,
			BadParametersException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Look if a subscriber with the same username already exists
		PCompetitor c;

		Utils.stringToDate(borndate); // for validationg of the format of date
//		try {
//			c = competitorDAO.findByNameAndBornDate(a_firstname, a_lastname,
//					borndate);
//
//			if (c != null)
//				throw new ExistingCompetitorException(
//						"A person competitor with the same firstname ,lastname and borndate already exists");
			// Creates the new subscriber
			c = new PCompetitor(a_firstname, a_lastname, borndate);
			// competitorDAO.persist(c);

			return c;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}

	}

	// create team competitor
	@Override
	public Competitor createCompetitor(String name, String a_managerPwd)
			throws AuthenticationException, ExistingCompetitorException,
			BadParametersException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// Look if a subscriber with the same username already exists
		PCompetitor c=null;

//		try {
//			c = competitorDAO.findByName(name, "team");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		if (c != null)
//			throw new ExistingCompetitorException(
//					"A team competitor with the same name already exists");
		// Creates the new subscriber

		c = new PCompetitor(name);

		return c;

	}

	/**
	 * search Competitor By Name
	 * 
	 * @param a_firstname
	 * @param a_lastname
	 * @return Competitor
	 */
	private Competitor searchCompetitorByName(String a_firstname,
			String a_lastname) {
		if (a_firstname == null && a_lastname == null)
			return null;
		PCompetitorDAO competitorDAO = new PCompetitorDAO();
		PCompetitor c = null;
		try {
			c = competitorDAO.findByName(a_firstname, a_lastname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	// ********************************management
	// bet**************************************************
	/*
	 * generer un bet on podium on a competition The number of tokens of the
	 * subscriber is debited. (non-Javadoc)
	 * 
	 * @see fr.uv1.bettingServices.Betting#betOnPodium(long, java.lang.String,
	 * fr.uv1.Competitor.PCompetitor, fr.uv1.Competitor.PCompetitor,
	 * fr.uv1.Competitor.PCompetitor, java.lang.String, java.lang.String)
	 */
	@Override
	public void betOnPodium(long number_tokens, String a_competition,
			Competitor a_winner, Competitor a_second, Competitor a_third,
			String a_username, String a_pwdSubs)
			throws AuthenticationException, CompetitionException,
			ExistingCompetitionException, SubscriberException,
			BadParametersException {

		try {
			authenticateSubs(a_pwdSubs, a_username);
			System.out.println("you have win10");
			Competition_Podium competition_podium = competition_podiumDAO
					.findbyname(a_competition);
			if (competition_podium != null) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(competition_podium.getClose_date());
				if (cal.before(Calendar.getInstance())) {
					throw new CompetitionException("the competition is closed ");
				}
				Subscriber bet_subscriber = subscriberDAO
						.findByUsername(a_username);
				Collection<PCompetitor> m = competition_podium
						.getList_competitor();// hai you Tcompetitor mei
												// nong
				int flag = 0;

				String firstname1 = ((PCompetitor) a_winner).getFirstname();
				String lastname1 = ((PCompetitor) a_winner).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname1)
							&& p.getLastname().equals(lastname1)) {
						flag++;
					}
				}
				String firstname2 = ((PCompetitor) a_second).getFirstname();
				String lastname2 = ((PCompetitor) a_second).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname2)
							&& p.getLastname().equals(lastname2)) {
						flag++;
					}
				}
				String firstname3 = ((PCompetitor) a_third).getFirstname();
				String lastname3 = ((PCompetitor) a_third).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname3)
							&& p.getLastname().equals(lastname3)) {
						flag++;
					}
				}
				System.out.println("you have win");
				if (flag < 3) {
					throw new CompetitionException(
							"there is no competitor with name a_winner, a_second or a_third for the competition");
				}
				PCompetitor winner1 = competitorDAO.findByName(
						((PCompetitor) a_winner).getFirstname(),
						((PCompetitor) a_winner).getLastname());
				PCompetitor winner2 = competitorDAO.findByName(
						((PCompetitor) a_second).getFirstname(),
						((PCompetitor) a_second).getLastname());
				PCompetitor winner3 = competitorDAO.findByName(
						((PCompetitor) a_third).getFirstname(),
						((PCompetitor) a_third).getLastname());
				System.out.println("you have win5");
				Bet_Podium bet_podium = new Bet_Podium(bet_subscriber,
						number_tokens, winner1, winner2, winner3);

				try {
					debitSubscriber(a_username, number_tokens, managerPassword);
				} catch (ExistingSubscriberException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bet_podiumDAO.persist(bet_podium, competition_podium.getId());

			} else {
				throw new ExistingCompetitionException(
						"the competition does not exist");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * bet a winner for a competition The number of tokens of the subscriber is
	 * debited. (non-Javadoc)
	 * 
	 * @see fr.uv1.bettingServices.Betting#betOnWinner(long, java.lang.String,
	 * fr.uv1.Competitor.PCompetitor, java.lang.String, java.lang.String)
	 */
	@Override
	public void betOnWinner(long number_tokens, String a_competition,
			Competitor a_winner, String a_username, String a_pwdSubs)
			throws AuthenticationException, BadParametersException,
			CompetitionException, ExistingCompetitionException,
			SubscriberException {
		// TODO Auto-generated method stub
		try {
			authenticateSubs(a_pwdSubs, a_username);

			Competition_Winner competition_winner = competition_winnerDAO
					.findbyname(a_competition);
			Competition_Podium competition_podium = competition_podiumDAO
					.findbyname(a_competition);

			if (competition_winner != null) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(competition_winner.getClose_date());
				if (cal.before(Calendar.getInstance())) {
					throw new CompetitionException("the competition is closed ");
				}
				Subscriber bet_subscriber = subscriberDAO
						.findByUsername(a_username);
				Collection<PCompetitor> m = competition_winner
						.getList_competitor();
				int flag = 0;

				String firstname1 = ((PCompetitor) a_winner).getFirstname();
				String lastname1 = ((PCompetitor) a_winner).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname1)
							&& p.getLastname().equals(lastname1)) {
						flag = 1;
					}
				}

				if (flag < 1) {
					throw new CompetitionException(
							"there is no competitor with name a_winner for the competition");
				}
				PCompetitor winner = competitorDAO.findByName(
						((PCompetitor) a_winner).getFirstname(),
						((PCompetitor) a_winner).getLastname());
				Bet_Winner bet_winner = new Bet_Winner(bet_subscriber,
						number_tokens, winner);
				debitSubscriber(a_username, number_tokens, managerPassword);
				bet_winnerDAO.persist(bet_winner, competition_winner.getId());

			} else if (competition_podium != null) {// podium competition is a
													// competition who have more
													// than 2 competitors.
				Calendar cal = Calendar.getInstance();
				cal.setTime(competition_podium.getClose_date());
				if (cal.before(Calendar.getInstance())) {
					throw new CompetitionException("the competition is closed ");
				}
				Subscriber bet_subscriber = subscriberDAO
						.findByUsername(a_username);
				Collection<PCompetitor> m = competition_podium
						.getList_competitor();// hai you Tcompetitor mei
												// nong
				int flag = 0;

				String firstname1 = ((PCompetitor) a_winner).getFirstname();
				String lastname1 = ((PCompetitor) a_winner).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname1)
							&& p.getLastname().equals(lastname1)) {
						flag = 1;
					}
				}

				if (flag < 1) {
					throw new CompetitionException(
							"there is no competitor with name a_winner for the competition");
				}

				PCompetitor winner = competitorDAO.findByName(
						((PCompetitor) a_winner).getFirstname(),
						((PCompetitor) a_winner).getLastname());
				Bet_Podium bet_podium = new Bet_Podium(bet_subscriber,
						number_tokens, winner, winner, winner);// ********************Winner
				debitSubscriber(a_username, number_tokens, managerPassword);
				bet_podiumDAO.persist(bet_podium, competition_podium.getId());

			} else {

				throw new ExistingCompetitionException(
						"the competition does not exist");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExistingSubscriberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * delete all bets made by a subscriber on a competition. subscriber's
	 * account is credited with a number of tokens corresponding to the bets
	 * made by the subscriber for the competition. (non-Javadoc)
	 * 
	 * @see
	 * fr.uv1.bettingServices.Betting#deleteBetsCompetition(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteBetsCompetition(String a_competition, String a_username,
			String a_pwdSubs) throws AuthenticationException,
			CompetitionException, ExistingCompetitionException {
		// TODO Auto-generated method stub
		try {
			authenticateSubs(a_pwdSubs, a_username);

			Subscriber bet_subscriber = subscriberDAO
					.findByUsername(a_username);

			Competition_Winner competition_winner = competition_winnerDAO
					.findbyname(a_competition);

			Competition_Podium competition_podium = competition_podiumDAO
					.findbyname(a_competition);

			if (competition_winner != null) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(competition_winner.getClose_date());
				if (cal.before(Calendar.getInstance())) {
					throw new CompetitionException("the competition is closed ");
				}
				Collection<Bet_Winner> bets_winner = bet_winnerDAO
						.findBySubscriber_id_and_competition_id(
								bet_subscriber.getId(),
								competition_winner.getId());
				long money1 = 0;
				// calculate the somme of money of allbets made by the
				// subscriber on
				// the competition
				for (Bet_Winner bet_winner : bets_winner) {
					money1 = money1 + bet_winner.getBet_number_tokens();
				}
				// credit the somme of money back into the account of the
				// subscriber
				creditSubscriber(bet_subscriber.getUsername(), money1,
						managerPassword);
				for (Bet_Winner bet_winner : bets_winner) {

					bet_winnerDAO.delete(bet_winner);

				}

			} else if (competition_podium != null) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(competition_podium.getClose_date());
				if (cal.before(Calendar.getInstance())) {
					throw new CompetitionException("the competition is closed ");
				}
				Collection<Bet_Podium> bets_podium = bet_podiumDAO
						.findBySubscriber_id_and_competition_id(
								bet_subscriber.getId(),
								competition_podium.getId());
				long money2 = 0;
				// calculate the somme of money of allbets made by the
				// subscriber on
				// the competition
				for (Bet_Podium bet_podium : bets_podium) {
					money2 = money2 + bet_podium.getBet_number_tokens();
				}
				// credit the somme of money back into the account of the
				// subscriber
				creditSubscriber(bet_subscriber.getUsername(), money2,
						managerPassword);
				for (Bet_Podium bet_podium : bets_podium) {

					bet_podiumDAO.delete(bet_podium);
				}

			} else {
				throw new ExistingCompetitionException(
						"the competition does not exist");

			}
		} catch (SQLException | BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExistingSubscriberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * settle bets on podium.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see fr.uv1.bettingServices.Betting#settlePodium(java.lang.String,
	 * fr.uv1.Competitor.PCompetitor, fr.uv1.Competitor.PCompetitor,
	 * fr.uv1.Competitor.PCompetitor, java.lang.String)
	 */
	@Override
	public void settlePodium(String a_competition, Competitor a_winner,
			Competitor a_second, Competitor a_third, String a_managerPwd)
			throws AuthenticationException, ExistingCompetitionException,
			CompetitionException {
		// TODO Auto-generated method stub
		authenticateMngr(a_managerPwd);
		try {
			Competition_Podium competition_podium = competition_podiumDAO
					.findbyname(a_competition);
			if (competition_podium == null) {
				throw new ExistingCompetitionException(
						"the competition does not exist");
			}

			if (a_winner.equals(a_second) || a_winner.equals(a_third)
					|| a_second.equals(a_third)) {
				throw new CompetitionException(
						"at least two competitors in the podium are the same");
			}
			// return allbets on this competition,and calculate total number of
			// tokens betted for the competition
			Collection<Bet_Podium> allbets = bet_podiumDAO
					.findbyidcompetition(competition_podium.getId());
			for (Bet_Podium bet_podium : allbets) {
				if((bet_podium.getSecond()).getFirstname().equals((bet_podium.getWinner()).getFirstname()) && (bet_podium.getSecond()).getLastname().equals((bet_podium.getWinner()).getLastname()) && (bet_podium.getSecond()).getBorndate().equals((bet_podium.getWinner()).getBorndate())){
					allbets.remove(bet_podium);
				}
			}
			
			long sum_on_competition = 0;
			for (Bet_Podium bet_podium : allbets) {
				sum_on_competition = sum_on_competition
						+ bet_podium.getBet_number_tokens();
			}

			Collection<PCompetitor> m = competition_podium.getList_competitor();
			int flag = 0;

			String firstname1 = ((PCompetitor) a_winner).getFirstname();
			String lastname1 = ((PCompetitor) a_winner).getLastname();
			String borndate1 = ((PCompetitor) a_winner).getBorndate();
			for (PCompetitor p : m) {
				if (p.getFirstname().equals(firstname1)
						&& p.getLastname().equals(lastname1)
						&& p.getBorndate().equals(borndate1)) {
					flag++;
				}
			}
			String firstname2 = ((PCompetitor) a_second).getFirstname();
			String lastname2 = ((PCompetitor) a_second).getLastname();
			String borndate2 = ((PCompetitor) a_second).getBorndate();
			for (PCompetitor p : m) {
				if (p.getFirstname().equals(firstname2)
						&& p.getLastname().equals(lastname2)
						&& p.getBorndate().equals(borndate2)) {
					flag++;
				}
			}
			String firstname3 = ((PCompetitor) a_third).getFirstname();
			String lastname3 = ((PCompetitor) a_third).getLastname();
			String borndate3 = ((PCompetitor) a_third).getBorndate();
			for (PCompetitor p : m) {
				if (p.getFirstname().equals(firstname3)
						&& p.getLastname().equals(lastname3)
						&& p.getBorndate().equals(borndate3)) {
					flag++;
				}
			}
			if (flag < 3) {
				throw new CompetitionException(
						"there is no competitor with name a_winner, a_second or a_third for the competition");
			}

			// return all bets which have the right guess on the podium on this
			// competition

			PCompetitor winner1 = competitorDAO.findByName(
					((PCompetitor) a_winner).getFirstname(),
					((PCompetitor) a_winner).getLastname());
			PCompetitor winner2 = competitorDAO.findByName(
					((PCompetitor) a_second).getFirstname(),
					((PCompetitor) a_second).getLastname());
			PCompetitor winner3 = competitorDAO.findByName(
					((PCompetitor) a_third).getFirstname(),
					((PCompetitor) a_third).getLastname());

			Collection<Bet_Podium> bets_podium_right = bet_podiumDAO
					.findbyresult_id_competitor_podium(winner1.getId(),
							winner2.getId(), winner3.getId());
			if (bets_podium_right != null) {
				long sum_on_podium = 0;
				// and calculate total number of tokens betted for the podium
				for (Bet_Podium bet_podium_right : bets_podium_right) {
					sum_on_podium = sum_on_podium
							+ bet_podium_right.getBet_number_tokens();
				}
				for (Bet_Podium bet_podium_right : bets_podium_right) {
					// in each bet_winner_right,x is the number of tokens betted
					// for the right winner
					long x = bet_podium_right.getBet_number_tokens();
					// subscriber betting on this competition with the right
					// winner a_winner is credited with a number of tokens
					long money = x * sum_on_competition / sum_on_podium;
					creditSubscriber(bet_podium_right.getBet_subscriber()
							.getUsername(), money, a_managerPwd);
				}
			} else
				for (Bet_Podium bet_podium : allbets) {
					creditSubscriber(bet_podium.getBet_subscriber()
							.getUsername(), bet_podium.getBet_number_tokens(),
							a_managerPwd);
				}

			// cancelCompetition(a_competition, a_managerPwd);
			//
			try {
				Competition c = searchCompetition(a_competition);
				System.out.println(c);

				if (c != null) {

					Collection<PCompetitor> competitors = c
							.getList_competitor();

					// delete the relation between competitions and
					// bet****************************
					if (c.getType().equals("Winner")) {
						bet_winnerDAO
								.deletebycompetition((Competition_Winner) c);
					}

					if (c.getType().equals("Podium")) {

						bet_podiumDAO
								.deletebycompetition((Competition_Podium) c);
					}
					// delete the relation between competitions and competitors
					for (PCompetitor competitor : competitors) {
						// System.out.println(competitor);
						competitorDAO.playdelete(competitor, c);
					}

					// delete the competition by the type
					if (c.getType().equals("Winner")) {
						competition_winnerDAO.delete((Competition_Winner) c);
					}

					if (c.getType().equals("Podium")) {

						competition_podiumDAO.delete((Competition_Podium) c);
					}

				} else {
					throw new ExistingCompetitionException(
							"Competition does not exist");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExistingSubscriberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * settle bets on winner.
	 */
	@Override
	public void settleWinner(String a_competition, Competitor a_winner,
			String a_managerPwd) throws AuthenticationException,
			ExistingCompetitionException, CompetitionException {
		// TODO Auto-generated method stub
		authenticateMngr(a_managerPwd);
		try {
			Competition_Winner competition_winner = competition_winnerDAO
					.findbyname(a_competition);
			Competition_Podium competition_podium = competition_podiumDAO
					.findbyname(a_competition);

			if (competition_winner != null) {
				// return allbets on this competition,and calculate total number
				// of
				// tokens betted for the competition
				Collection<Bet_Winner> allbets = bet_winnerDAO
						.findbyidcompetition(competition_winner.getId());

				long sum_on_competition = 0;
				for (Bet_Winner bet_winner : allbets) {
					sum_on_competition = sum_on_competition
							+ bet_winner.getBet_number_tokens();
				}
				Collection<PCompetitor> m = competition_winner
						.getList_competitor();
				int flag = 0;

				String firstname1 = ((PCompetitor) a_winner).getFirstname();
				String lastname1 = ((PCompetitor) a_winner).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname1)
							&& p.getLastname().equals(lastname1)) {
						flag = 1;
					}
				}

				if (flag < 1) {
					throw new CompetitionException(
							"there is no competitor with name a_winner for the competition");
				}
				// return all bets which have the right guess on the winner on
				// this
				// competition
				PCompetitor winner = competitorDAO.findByName(
						((PCompetitor) a_winner).getFirstname(),
						((PCompetitor) a_winner).getLastname());
				Collection<Bet_Winner> bets_winner_right = bet_winnerDAO
						.findbyresult_id_competitor_winner(winner.getId());
				if (bets_winner_right != null) {
					long sum_on_winner = 0;
					// calculate total number of tokens betted for the winner
					for (Bet_Winner bet_winner_right : bets_winner_right) {
						sum_on_winner = sum_on_winner
								+ bet_winner_right.getBet_number_tokens();
					}
					for (Bet_Winner bet_winner_right : bets_winner_right) {
						// in each bet_winner_right,x is the number of tokens
						// betted
						// for the right winner
						long x = bet_winner_right.getBet_number_tokens();
						// subscriber betting on this competition with the right
						// winner a_winner is credited with a number of tokens
						long money = x * sum_on_competition / sum_on_winner;
						creditSubscriber(bet_winner_right.getBet_subscriber()
								.getUsername(), money, a_managerPwd);
					}
				}
				// If no subscriber bets on the right competitor (the real
				// winner),
				// the tokens betted are credited to subscribers betting on the
				// competition according to the number of tokens they betted.
				else
					for (Bet_Winner bet_winner : allbets) {
						creditSubscriber(bet_winner.getBet_subscriber()
								.getUsername(),
								bet_winner.getBet_number_tokens(), a_managerPwd);
					}
			} else if (competition_podium != null) {
				Collection<Bet_Podium> Pallbets = bet_podiumDAO
						.findbyidcompetition(competition_podium.getId());

				long sum_on_competition = 0;
				for (Bet_Podium bet_podium : Pallbets) {
					//********************************if his bet have three same winner, it's the bet Winner and we settle it 
					
					if((bet_podium.getSecond()).getFirstname().equals((bet_podium.getWinner()).getFirstname()) && (bet_podium.getSecond()).getLastname().equals((bet_podium.getWinner()).getLastname()) && (bet_podium.getSecond()).getBorndate().equals((bet_podium.getWinner()).getBorndate())){
						sum_on_competition = sum_on_competition+ bet_podium.getBet_number_tokens();
					}
							
				}
				Collection<PCompetitor> m = competition_podium
						.getList_competitor();
				int flag = 0;

				String firstname1 = ((PCompetitor) a_winner).getFirstname();
				String lastname1 = ((PCompetitor) a_winner).getLastname();
				for (PCompetitor p : m) {
					if (p.getFirstname().equals(firstname1)
							&& p.getLastname().equals(lastname1)) {
						flag = 1;
					}
				}

				if (flag < 1) {
					throw new CompetitionException(
							"there is no competitor with name a_winner for the competition");
				}
				// return all bets which have the right guess on the winner on
				// this
				// competition
				PCompetitor winner = competitorDAO.findByName(
						((PCompetitor) a_winner).getFirstname(),
						((PCompetitor) a_winner).getLastname());
				Collection<Bet_Podium> bets_podiumwinner_right = bet_podiumDAO
						.findbyresult_id_competitor_podium(winner.getId(),winner.getId(),winner.getId());
				if (bets_podiumwinner_right != null) {
					long sum_on_winner = 0;
					// calculate total number of tokens betted for the winner
					for (Bet_Podium bet_winner_right : bets_podiumwinner_right) {
						sum_on_winner = sum_on_winner
								+ bet_winner_right.getBet_number_tokens();
					}
					for (Bet_Podium bet_podiumwinner_right : bets_podiumwinner_right) {
						// in each bet_winner_right,x is the number of tokens
						// betted
						// for the right winner
						long x = bet_podiumwinner_right.getBet_number_tokens();
						// subscriber betting on this competition with the right
						// winner a_winner is credited with a number of tokens
						long money = x * sum_on_competition / sum_on_winner;
						creditSubscriber(bet_podiumwinner_right.getBet_subscriber()
								.getUsername(), money, a_managerPwd);
					}
				}
				// If no subscriber bets on the right competitor (the real
				// winner),
				// the tokens betted are credited to subscribers betting on the
				// competition according to the number of tokens they betted.
				else
					for (Bet_Podium bet_podiumwinner : Pallbets) {
						creditSubscriber(bet_podiumwinner.getBet_subscriber()
								.getUsername(),
								bet_podiumwinner.getBet_number_tokens(), a_managerPwd);
					}

			} else {
				throw new ExistingCompetitionException(
						"the competition does not exist");
			}
			// cancelCompetition(a_competition,
			// a_managerPwd);//***************************************????

			//
			try {
				Competition c = searchCompetition(a_competition);
				System.out.println(c);

				if (c != null) {

					Collection<PCompetitor> competitors = c
							.getList_competitor();

					// delete the relation between competitions and
					// bet****************************
					if (c.getType().equals("Winner")) {
						bet_winnerDAO
								.deletebycompetition((Competition_Winner) c);
					}

					if (c.getType().equals("Podium")) {

						bet_podiumDAO
								.deletebycompetition((Competition_Podium) c);
					}
					// delete the relation between competitions and competitors
					for (PCompetitor competitor : competitors) {
						// System.out.println(competitor);
						competitorDAO.playdelete(competitor, c);
					}

					// delete the competition by the type
					if (c.getType().equals("Winner")) {
						competition_winnerDAO.delete((Competition_Winner) c);
					}

					if (c.getType().equals("Podium")) {

						competition_podiumDAO.delete((Competition_Podium) c);
					}

				} else {
					throw new ExistingCompetitionException(
							"Competition does not exist");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExistingSubscriberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * From Betting interface
	 */
	@Override
	public void changeSubsPwd(String a_username, String newPwd,
			String currentPwd) throws AuthenticationException,
			BadParametersException {
		// TODO Auto-generated method stub
		authenticateSubs(currentPwd, a_username);

		// Change password if valid

		setSubscriberPassword(a_username, newPwd);

	}

	/**
	 * set password of a subscriber
	 * 
	 * @param a_username
	 * @param subscriberPassword
	 * @throws BadParametersException
	 */
	private void setSubscriberPassword(String a_username,
			String subscriberPassword) throws BadParametersException {
		if (subscriberPassword == null)
			throw new BadParametersException("subscriber's password not valid");
		// The password should be valid
		if (!pv.verify(subscriberPassword.toCharArray()))
			throw new BadParametersException("new password is invalid");
		Subscriber subscriber;
		try {
			subscriber = subscriberDAO.findByUsername(a_username);

			subscriber.setPassword(subscriberPassword);
			subscriberDAO.update(subscriber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * From Betting interface
	 */
	@Override
	public void creditSubscriber(String a_username, long number_tokens,
			String a_managerPwd) throws AuthenticationException,
			BadParametersException, ExistingSubscriberException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// find the subscriber
		Subscriber newSubscriber;
		if (number_tokens <= 0) {
			throw new BadParametersException(
					"number of tokens is less than (or equals to) 0");
		}

		try {
			newSubscriber = subscriberDAO.findByUsername(a_username);
			if (newSubscriber != null) {
				newSubscriber.setAccount(newSubscriber.getAccount()
						+ number_tokens);
				subscriberDAO.update(newSubscriber);
			} else {
				throw new ExistingSubscriberException(
						"the subscriber (username) is not registered");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * From Betting interface
	 */
	@Override
	public void debitSubscriber(String a_username, long number_tokens,
			String a_managerPwd) throws AuthenticationException,
			BadParametersException, ExistingSubscriberException,
			SubscriberException {
		// Authenticate manager
		authenticateMngr(a_managerPwd);
		// find the subscriber
		Subscriber newSubscriber;
		try {
			newSubscriber = subscriberDAO.findByUsername(a_username);
			if (newSubscriber != null) {
				long nowaccount = newSubscriber.getAccount();
				if (number_tokens <= 0) {
					throw new SubscriberException(
							"number of tokens is less than (or equals to) 0");
				} else if (number_tokens > nowaccount) {
					throw new SubscriberException("number of tokens not enough");
				} else {
					newSubscriber.setAccount(nowaccount - number_tokens);
					subscriberDAO.update(newSubscriber);
				}
			} else {
				throw new ExistingSubscriberException(
						"the subscriber (username) is not registered");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * From Betting interface
	 */
	@Override
	public ArrayList<String> consultBetsCompetition(String a_competition)
			throws ExistingCompetitionException {
		ArrayList<String> infoBetOnCompetition = new ArrayList<String>();
		Collection<Bet_Podium> current_bets_podium;
		Collection<Bet_Winner> current_bets_winner;
		current_bets_winner = new ArrayList<Bet_Winner>();
		current_bets_podium = new ArrayList<Bet_Podium>();

		try {
			Competition_Podium competition_podium = competition_podiumDAO
					.findbyname(a_competition);
			Competition_Winner competition_winner = competition_winnerDAO
					.findbyname(a_competition);
			if (competition_podium != null) {
				current_bets_podium = competition_podium.getBet_Podium();
				for (Bet_Podium b : current_bets_podium) {
					infoBetOnCompetition.add(b.getBet_subscriber()
							.getUsername()
							+ " has betted "
							+ b.getBet_number_tokens()
							+ " on this competition"
							+ " and bets winner is "
							+ b.getWinner()
							+ ",second is "
							+ b.getSecond()
							+ ",third is "
							+ b.getThird() + "\n");
				}
			} else if (competition_winner != null) {

				current_bets_winner = competition_winner.getBet_Winners();
				for (Bet_Winner b : current_bets_winner) {
					infoBetOnCompetition.add(b.getBet_subscriber()
							.getUsername()
							+ " has betted "
							+ b.getBet_number_tokens()
							+ " on this competition"
							+ " and bets winner is "
							+ b.getWinnerCompetitor()
							+ "\n");
				}
			} else {
				throw new ExistingCompetitionException(
						"the competition does not exist");
			}

		} catch (SQLException | BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return infoBetOnCompetition;
	}

	// *********************TEST DAO***********************************//

	// -----------------------------------------------------------------------------
	public void TESTDAO() throws BadParametersException,
			AuthenticationException, ExistingCompetitorException,
			ExistingSubscriberException, SubscriberException,
			ExistingCompetitionException, CompetitionException {
		subscriberDAO = new SubscriberDAO();
		competition_podiumDAO = new Competition_PodiumDAO();
		competition_winnerDAO = new Competition_WinnerDAO();
		bet_winnerDAO = new Bet_WinnerDAO();
		bet_podiumDAO = new Bet_PodiumDAO();
		competitorDAO = new PCompetitorDAO();

		System.out.println("--- Start ---\n");

		System.out.println("All the subscribers");
		displayAllSubscribers();
		// System.out.println("Subscriber #1: " + subscriberDAO.findById(1));
		// System.out.println();

		/*
		 * System.out.println("Creating a new subscriber"); Subscriber
		 * newSubscriber = new Subscriber(null, "LI", "sB", "YIGE",
		 * "12/12/1900", 99); newSubscriber =
		 * subscriberDAO.persist(newSubscriber);
		 * System.out.println("newSubscriber = " + newSubscriber);//
		 * System.out.println("newSubscriber's id = " + newSubscriber.getId());
		 * System.out.println();
		 * 
		 * System.out.println("All the subscribers"); displayAllSubscribers();
		 * System.out.println("Subscriber #1: " + subscriberDAO.findById(1));
		 * System.out.println();
		 * 
		 * System.out.println("All the subscribers after insertion");
		 * displayAllSubscribers(); String s; s=this.subscribe("LI", "h",
		 * "YIGE2", "a1234567", "01/08/1989");
		 */

		//
		//
		//
		// System.out.println("Updating the new subscriber");
		// newSubscriber.setFirstname("Marcel");
		// newSubscriber.setLastname("DUPONT");
		// subscriberDAO.update(newSubscriber);
		//
		// System.out.println("All the subscribers after updating");
		// displayAllSubscribers();
		//
		// System.out.println("Creating a bet for the new subscriber");
		// System.out.println();
		// System.out.println();
		//
		// System.out.println("Deleting all the bets of the new subscriber");
		// // System.out.println("Deleting the new subscriber");
		// // subscriberDAO.delete(newSubscriber);
		// //
		// // System.out.println("All the subscribers after delete");
		// // displayAllSubscribers();
		//
		// System.out.println("--- End ---\n");
		// //*************************--- TEST
		// :competitor---\***************************************************
		// System.out.println("\n--- TEST :competitor---\n");
		// System.out.println("--- Start ---\n");
		//
		// System.out.println("All the competitors");
		// displayAllcompetitors();
		//
		// System.out.println("Competitor #3: " + competitorDAO.findById(3));
		// System.out.println();
		//
		// System.out.println("Creating a new comprtitor");
		// Competitor newcompetitor = new Competitor("Marcel", "DUPONT");
		//
		// newcompetitor = competitorDAO.persist(newcompetitor);
		// System.out.println("newcompetitor = " + newcompetitor);//
		// System.out.println();
		//
		// System.out.println("Creating 2 new comprtitor");
		// Competitor newcompetitor2 = new Competitor("Holo", "HAOER");
		// newcompetitor = competitorDAO.persist(newcompetitor2);
		// System.out.println("newcompetitor2 = " + newcompetitor2);//
		// System.out.println();
		//
		// System.out.println("All the Competitors after insertion");
		// displayAllcompetitors();
		//
		// System.out.println("Updating the new competitor");
		// newcompetitor.setFirstname("_Marcel");
		// newcompetitor.setLastname("_DUPONT");
		// competitorDAO.update(newcompetitor);
		//
		// System.out.println("All the Competitors after updating");
		// displayAllcompetitors();
		//
		// //
		// // System.out.println("Deleting the new competitor");
		// // competitordao.delete(newcompetitor);
		// //
		// // System.out.println("All the Competitors after delete");
		// // displayAllcompetitors();
		//
		// System.out.println("--- End ---\n");
		//
		//
		//
		// //*************************--- TEST
		// :Competition_Winner---\***************************************************
		// System.out.println("\n--- TEST :Competition_Winner---\n");
		// System.out.println("--- Start ---\n");
		//
		// System.out.println("All the Competition_Winners");
		// displayAllCompetition_Winners();
		//
		// System.out.println("Competition_Winner #3: " +
		// competition_winnerDAO.findById(1));
		// System.out.println();
		//
		// System.out.println("Creating a new competition_winner");
		// Competition_Winner newCompetition_Winner = new
		// Competition_Winner("hVSr", "basketball",
		// Date.valueOf("2013-12-12"),null);
		// newCompetition_Winner =
		// competition_winnerDAO.persist(newCompetition_Winner);
		// System.out.println("newCompetition_Winner = " +
		// newCompetition_Winner);//
		// System.out.println();
		//
		// System.out.println("All the Competition_Winners after insertion");
		// displayAllCompetition_Winners();
		//
		// System.out.println("Updating the new Competition_Winner");
		// newCompetition_Winner.setName("_LIvsLIU");
		// newCompetition_Winner.setType("_FOOT");
		// competition_winnerDAO.update(newCompetition_Winner);
		//
		// System.out.println("All the Competition_Winners after updating");
		// displayAllCompetition_Winners();
		//
		//
		// // System.out.println("Deleting the new Competition_Winner");
		// // competition_winnerdao.delete(newCompetition_Winner);
		// //
		// // System.out.println("All the Competition_Winners after delete");
		// // displayAllCompetition_Winners();
		//
		// System.out.println("--- End ---\n");
		//
		//
		//
		// //*************************--- TEST
		// :Competition_Podium---\***************************************************
		// System.out.println("\n--- TEST :Competition_Podium---\n");
		// System.out.println("--- Start ---\n");
		//
		// System.out.println("All the Competition_Podiums");
		// displayAllCompetition_Podiums();
		//
		// System.out.println("Competition_Podium #3: " +
		// competition_podiumDAO.findById(1));
		// System.out.println();
		//
		// System.out.println("Creating a new competition_Podium");
		// Competition_Podium newcompetition_podium = new
		// Competition_Podium("hVSr", "basketball",
		// Date.valueOf("2013-12-12"),null,null,newcompetitor,newcompetitor,newcompetitor);
		// newcompetition_podium =
		// competition_podiumDAO.persist(newcompetition_podium);
		// System.out.println("newCompetition_Podium = " +
		// newcompetition_podium);//
		// System.out.println();
		//
		// System.out.println("All the Competition_Podiums after insertion");
		// displayAllCompetition_Podiums();
		//
		// System.out.println("Updating the new Competition_Podium");
		// newcompetition_podium.setName("_LIvsLIU");
		// newcompetition_podium.setType("_FOOT");
		// competition_podiumDAO.update(newcompetition_podium);
		//
		// System.out.println("All the Competition_Podiums after updating");
		// displayAllCompetition_Podiums();
		//
		//
		// // System.out.println("Deleting the new Competition_Winner");
		// // competition_podiumdao.delete(newcompetition_podium);
		// //
		// // System.out.println("All the Competition_Podiums after delete");
		// // displayAllCompetition_Podiums();
		//
		// System.out.println("--- End ---\n");
		//
		//
		//
		// //*************************--- TEST
		// :Bet_Winner---\***************************************************
		// System.out.println("\n--- TEST :Bet_Winner---\n");
		// System.out.println("--- Start ---\n");
		//
		// System.out.println("All the Bet_Winners");
		// displayAllBet_Winners();
		//
		// System.out.println("Bet_Winner #3: " + bet_winnerDAO.findById(1));
		// System.out.println();
		//
		// System.out.println("Creating a new Bet_winner");
		// Bet_Winner newBet_Winner = new Bet_Winner(null, newSubscriber,
		// 100000, newcompetitor);
		// newBet_Winner =
		// bet_winnerDAO.persist(newBet_Winner,newCompetition_Winner.getId());
		// System.out.println("newBet_Winner = " + newBet_Winner);//
		// System.out.println();
		//
		// System.out.println("All the Bet_Winners after insertion");
		// displayAllBet_Winners();
		//
		// System.out.println("Updating the new Bet_Winner");
		// newBet_Winner.setBet_number_tokens(99999);
		// newBet_Winner.setWinnerCompetitor(newcompetitor2);
		// bet_winnerDAO.update(newBet_Winner);
		//
		// System.out.println("All the Bet_Winners after updating");
		// displayAllBet_Winners();
		//
		//
		// System.out.println("Deleting the new Bet_Winner");
		// bet_winnerDAO.delete(newBet_Winner);
		//
		// System.out.println("All the Bet_Winners after delete");
		// displayAllBet_Winners();
		//
		// System.out.println("--- End ---\n");
		//
		//
		// //*************************--- TEST
		// :Bet_Podium---\***************************************************
		// System.out.println("\n--- TEST :Bet_Podium---\n");
		// System.out.println("--- Start ---\n");
		//
		// System.out.println("All the Bet_Podiums");
		// displayAllBet_Podiums();
		//
		// System.out.println("Bet_Podium #3: " + bet_podiumDAO.findById(1));
		// System.out.println();
		//
		// System.out.println("Creating a new Bet_Podium");
		// Bet_Podium newBet_Podium = new Bet_Podium(null, newSubscriber,
		// 100000, newcompetitor,newcompetitor2,newcompetitor);
		// newBet_Podium =
		// bet_podiumDAO.persist(newBet_Podium,newcompetition_podium.getId());
		// System.out.println("newBet_Podium = " + newBet_Podium);//
		// System.out.println();
		//
		// System.out.println("All the Bet_Podiums after insertion");
		// displayAllBet_Podiums();
		//
		// System.out.println("Updating the new Bet_Podium");
		// newBet_Podium.setBet_number_tokens(99999);
		// newBet_Podium.setWinner(newcompetitor2);
		// newBet_Podium.setSecond(newcompetitor);
		// newBet_Podium.setThird(newcompetitor2);
		// bet_podiumDAO.update(newBet_Podium);
		//
		// System.out.println("All the Bet_Podiums after updating");
		// displayAllBet_Podiums();
		//
		//
		// System.out.println("Deleting the new Bet_Podium");
		// bet_podiumDAO.delete(newBet_Podium);
		//
		// System.out.println("All the Bet_Podiums after delete");
		// displayAllBet_Podiums();
		//
		// System.out.println("--- End ---\n");
		//
		//
		// //*********************************Test new
		// fonction**************************************///**

		// Collection<Competition_Winner> competition_winner13=
		// searchAllCompetition_WinnersByDate(Date.valueOf("2012-10-10"),"a1234567");
		// System.out.println(competition_winner13);
		// Collection<Competition_Podium> competition_podium13=
		// searchAllCompetition_PodiumsByDate(Date.valueOf("2012-10-10"),"a1234567");
		// System.out.println(competition_podium13);
		// //*********************************Test new 2
		// fonction**************************************///**
		// try {
		// this.subscribe("LI", "xinSB", "lkop", "a1234567", "1990-12-12");
		//
		// this.subscribe("LIUERBI", "xinSB", "lkop", "a1234567", "2000-12-12");
		// } catch (SubscriberException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// *********************************************************//

		// this.subscribe("IIII", "IIII", "IIII", "a1234567", "1990-12-12");
		// this.subscribe("IIII", "IIII", "IIIk", "a1234567", "1990-12-12");
		// for(ArrayList<String> sa :listSubscribers("a1234567")) {
		// for(String s :sa){
		// System.out.println(s);
		//
		// }
		// }
		// this.unsubscribe("IIII", "a1234567");
		// Collection<Competition> cs=listCompetitions();
		// for(Competition c :cs) {
		// System.out.println(c);
		// }
		// Calendar c=Calendar.getInstance();
		//
		// Competitor c1=CreateCompetitor("Lzer", "LiSB", "a1234567");
		// Competitor c2=CreateCompetitor("Ldfqsd", "LiSB", "a1234567");
		// Competitor c3=CreateCompetitor("Lsdfsd", "LiSB", "a1234567");
		//
		// Collection<Competitor> ctors=new ArrayList<Competitor>();
		// ctors.add(c1);
		// ctors.add(c2);
		// ctors.add(c3);
		// addCompetition( "IVVuus3",c, ctors ,"a1234567") ;
		// System.out.println("/***************after add************************/");
		// cs=listCompetitions();
		// for(Competition c4 :cs) {
		// System.out.println(c);
		// }
		// this.addCompetition("woshini", c, ctors, "a1234567");
		// cancelCompetition("woshini","a1234567");
		// Collection<Competition> cs=listCompetitions();
		// for(Competition c5 :cs) {
		// System.out.println(c5);
		// }

		// CompetitorDAO competitordao=new CompetitorDAO();
		// Competitor c3=competitordao.findById(98);
		// this.createCompetitor("Lgh", "LiSB", "sdfghqdsfg", "a1234567");
		// Subscriber newSubscriber = new Subscriber(null, "LI", "sB", "YIGE",
		// "12/12/1900", 99);
		// newSubscriber = subscriberDAO.persist(newSubscriber);
		//
		setManagerPassword("a1234567");

		// this.createCompetitor("SB29", "a1234567");
		// String s;
		// s=this.subscribe("LIu", "h", "YIGE1", "a1234567", "01/08/1989");
		// String s2;
		// s2=this.subscribe("LIu", "h", "YIGE2", "a1234567", "01/08/1989");
		//
		//
		/*
		 * Calendar c=Calendar.getInstance(); PCompetitor
		 * c1=createCompetitor("L1", "LiSB", "22/08/1991", "a1234567");
		 * PCompetitor c2=createCompetitor("L2", "LiSB",
		 * "21/08/1991","a1234567"); PCompetitor c3=createCompetitor("L3",
		 * "LiSB", "20/08/1991","a1234567"); Collection<PCompetitor> ctors=new
		 * ArrayList<PCompetitor>(); ctors.add(c1); ctors.add(c2);
		 * ctors.add(c3); System.out.println("***************"+c);
		 * this.addCompetition("xyy", c, ctors, "a1234567");
		 */
		// Calendar c = Calendar.getInstance();
		// PCompetitor c1=(PCompetitor) createCompetitor("L4", "LiSB",
		// "20/08/1991","a1234567");
		// PCompetitor c2=(PCompetitor) createCompetitor("L5", "LiSB",
		// "21/08/1991","a1234567");
		// PCompetitor c4=(PCompetitor) createCompetitor("L6", "LiSB",
		// "20/08/1991","a1234567");
//		Calendar c = Calendar.getInstance();
//		c.set(2014, 10, 14);
//		Collection<Competitor> ctors = new ArrayList<Competitor>();
//		// PCompetitor c1 = competitorDAO.findById(1);
//		PCompetitor c2 = null;
//		PCompetitor c1 = null;
//		Competitor c4 = null;
//		try {
//			c2 = competitorDAO.findById(2);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			c1 = competitorDAO.findById(1);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PCompetitor c3 = null;
//		try {
//			c3 = competitorDAO.findById(3);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			c4 = competitorDAO.findById(4);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ctors.add(this.createCompetitor("wang", "yi", "1-12-1990", "a1234567"));
//		ctors.add(this.createCompetitor("wang", "er", "1-1-1990", "a1234567"));
		// ctors.add(this.createCompetitor("wang", "yi", "1-1-1990",
		// "a1234567"));
		// addCompetition( "xyyy",c, ctors ,"a1234567") ;
		// addCompetition("aze", c, ctors, "a1234567");
		//
		//this.betOnPodium(50, "a_compet", c2, c1, c3, "salto", "Rqn56fFb");
		// this.betOnPodium(75, "nidaye", c2, c1, c3, "Loab", "W2WsXpRR");
		// this.betOnPodium(25, "nidaye", c1, c2, c3, "Lpab", "qASZbp4d");

		// this.betOnWinner(25, "nimei", c2, "Loab", "W2WsXpRR");
		// this.betOnWinner(75, "nimei", c1, "LolK", "87654321");
		// this.settleWinner("nimei", c1, managerPassword);
		// this.settlePodium("nidaye", c1,c2,c3, managerPassword);

		// this.infosSubscriber("YIGE", "pXbNk4gm");
		// deleteBetsCompetition("xyy","YIGE","pXbNk4gm");

		// displayAllCompetition_Winners();
		// System.out.println("*************************");
		// displayAllCompetition_Podiums();
		// System.out.println("*************************");
		// System.out.println(this.listCompetitions());
		// this.subscribe("duran", "ab", "1-1-1990", "LolK", "a1234567");
		// this.addCompetition("gameo",c , ctors,"a1234567" );
		// this.addCompetition("dfgé9", c, ctors, "a1234567");

		// System.out.println(this.listCompetitions());

		// this.deleteCompetitor("xyy", c4, "a1234567");
		// System.out.println(listCompetitors("xyy"));
		// System.out.println(createCompetitor("df", "a1234567"));
		// displayAllcompetitors();

		// this.changeSubsPwd("LolK", "87", "87654321");

		// this.creditSubscriber("LolK", 100, "sdq4567");
		// this.debitSubscriber("LolK", 100, "a1234567");
		// displayAllCompetition_Winners();
		// displayAllCompetition_Podiums();
		//this.betOnWinner(300, "otra_compet", new PCompetitor("Barca"), "alohi", "WWKqbAgK");
		//this.createCompetitor("jj","a1234567");

	}

	// -----------------------------------------------------------------------------
	public void displayAllSubscribers() throws BadParametersException {
		List<Subscriber> subscribers = null;
		try {
			subscribers = subscriberDAO.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Subscriber subscriber : subscribers) {
			System.out.println(subscriber);
		}
		System.out.println();
	}

	public void displayAllcompetitors() throws BadParametersException {
		Collection<PCompetitor> Competitors = null;
		try {
			Competitors = competitorDAO.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (PCompetitor Competitor : Competitors) {
			System.out.println(Competitor);
		}
		System.out.println();
	}

	public void displayAllCompetition_Winners() throws BadParametersException {
		Collection<Competition_Winner> competition_winners = null;
		try {
			competition_winners = competition_winnerDAO.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Competition_Winner competition_winner : competition_winners) {
			System.out.println(competition_winner);
		}
		System.out.println();
	}

	public void displayAllCompetition_Podiums() throws BadParametersException {
		Collection<Competition_Podium> competition_podiums = null;
		try {
			competition_podiums = competition_podiumDAO.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Competition_Podium competition_podium : competition_podiums) {
			System.out.println(competition_podium);
		}
		System.out.println();
	}

	public void displayAllBet_Winners() throws BadParametersException {
		Collection<Bet_Winner> bet_winners = null;
		try {
			bet_winners = bet_winnerDAO.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Bet_Winner bet_winner : bet_winners) {
			System.out.println(bet_winner);
		}
		System.out.println();
	}

	public void displayAllBet_Podiums() throws BadParametersException {
		Collection<Bet_Podium> bet_podiums = null;
		try {
			bet_podiums = bet_podiumDAO.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Bet_Podium bet_podium : bet_podiums) {
			System.out.println(bet_podium);
		}
		System.out.println();
	}

	// -----------------------------------------------------------------------------
	// this main is for test DAO files
	// -----------------------------------------------------------------------------
	public static void main(String[] args) throws BadParametersException,
			AuthenticationException, ExistingCompetitorException,
			ExistingSubscriberException {
		try {
			BettingSoft b = new BettingSoft("a1234567");
			try {
				b.TESTDAO();
			} catch (ExistingCompetitionException | CompetitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SubscriberException e) {
			e.printStackTrace();
		}
	}

}