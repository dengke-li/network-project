package fr.uv1.Competitor;

import fr.uv1.bettingServices.*;

/**
 * this class represent the competitor
 * 
 * @author G3B3
 * 
 */

public class PCompetitor implements Competitor {
	private Integer id;
	private String firstname;
	private String lastname;
	private String borndate;

	/**
	 * @param firstname
	 * @param lastname
	 * @throws BadParametersException 
	 */
	public PCompetitor(String firstname, String lastname) throws BadParametersException {

		this.setFirstname(firstname);
		this.setLastname(lastname);
		this.borndate = null;
	}

	
	public PCompetitor(String name) throws BadParametersException{
		this.setFirstname(name);
		this.setLastname("team");
		this.borndate = "1-1-1900";
	}
	/**
	 * 
	 * @param firstname
	 * @param lastname
	 * @param borndate
	 * @throws BadParametersException 
	 */
	public PCompetitor(String firstname, String lastname, String borndate) throws BadParametersException {

		this.setFirstname(firstname);
		this.setLastname(lastname);
		this.setBorndate(borndate);
	}

	/**
	 * 
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @throws BadParametersException 
	 */
	public PCompetitor(Integer id, String firstname, String lastname) throws BadParametersException {
		this.id = id;
		this.setFirstname(firstname);
		this.setLastname(lastname);
	}

	/**
	 * 
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param borndate
	 * @throws BadParametersException 
	 */
	public PCompetitor(Integer id, String firstname, String lastname,
			String borndate) throws BadParametersException {
		this.id = id;
		this.setFirstname(firstname);
		this.setLastname(lastname);
		this.borndate = borndate;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) throws BadParametersException {
		
			checkStringFirstName(firstname);
			this.firstname =firstname;			

	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) throws BadParametersException {
	
			checkStringLastName(lastname);
			this.lastname = lastname;			
		
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String toString() {
		return " " + firstname + " " + lastname + " "+borndate;
	}

	public String getBorndate() {
		return borndate;
	}

	public void setBorndate(String borndate) {
		this.borndate = borndate;
	}

	@Override
	public boolean hasValidName() {
		try {
			checkStringLastName(this.getLastname());
			checkStringFirstName(this.getFirstname());
			return true;
		} catch (BadParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * check the validity of a string for a subscriber lastname, letters, dashes
	 * and spaces are allowed. First character should be a letter. lastname
	 * length should at least be 1 character
	 * 
	 * @param a_lastname
	 *            string to check.
	 * 
	 * @throws BadParametersException
	 *             raised if invalid.
	 */
	private static void checkStringLastName(String a_lastname)
			throws BadParametersException {
		if (a_lastname == null)
			throw new BadParametersException("name not instantiated");
		if (a_lastname.length() < 1)
			throw new BadParametersException(
					"name length less than 1 character");
		// First character a letter
		char c = a_lastname.charAt(0);
		if (!Character.isLetter(c))
			throw new BadParametersException("first character is not a letter");
		for (int i = 1; i < a_lastname.length(); i++) {
			c = a_lastname.charAt(i);
			if (!Character.isLetter(c) && (c != '-') && (c != ' '))
				throw new BadParametersException(
						"the name "
								+ a_lastname
								+ " contains other characters than letters, dashes and spaces");
		}
	}

	/**
	 * check the validity of a string for a subscriber firstname, letters,
	 * dashes and spaces are allowed. First character should be a letter.
	 * firstname length should at least be 1 character
	 * 
	 * @param a_firstname
	 *            string to check.
	 * 
	 * @throws BadParametersException
	 *             raised if invalid.
	 */
	private static void checkStringFirstName(String a_firstname)
			throws BadParametersException {
		// Same rules as for the last name
		checkStringLastName(a_firstname);

	}

}
