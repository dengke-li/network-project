package fr.uv1.Competitor;

import fr.uv1.bettingServices.*;

/**
 * this class represent the competitor
 * 
 * @author G3B3
 * 
 */

public class TCompetitor implements Competitor {
	private Integer id;
	private String name;


	/**
	 * @param firstname
	 * @param lastname
	 */
	public TCompetitor(String name) {

		this.setName(name);
	}

	

	/**
	 * 
	 * @param id
	 * @param name
	 */
	public TCompetitor(int id, String name) {
		this.setId(id);
		this.setName(name);

	}



	

	@Override
	public boolean hasValidName() {
		try {
			checkStringName(this.getName());
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
	private static void checkStringName(String name)
			throws BadParametersException {
		if (name == null)
			throw new BadParametersException("name not instantiated");
		if (name.length() < 1)
			throw new BadParametersException(
					"name length less than 1 character");
		// First character a letter
		char c = name.charAt(0);
		if (!Character.isLetter(c))
			throw new BadParametersException("first character is not a letter");
		for (int i = 1; i < name.length(); i++) {
			c = name.charAt(i);
			if (!Character.isLetter(c) && (c != '-') && (c != ' '))
				throw new BadParametersException(
						"the name "
								+name
								+ " contains other characters than letters, dashes and spaces");
		}
	}




	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

}
