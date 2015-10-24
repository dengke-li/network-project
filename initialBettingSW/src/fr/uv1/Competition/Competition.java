package fr.uv1.Competition;

import java.util.Collection;
import java.sql.Date;

import fr.uv1.Competitor.PCompetitor;
import fr.uv1.bettingServices.BadParametersException;

/**
 * @uml.dependency supplier="fr.uv1.bettingServices.Competitor" this class
 *                 represent the competition
 */
public class Competition {
	private Integer competition_id;
	private String name;
	private String type;
	private Date close_date;
	private Collection<PCompetitor> list_competitor;

	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param close_date
	 * @param list_competitor
	 * @throws BadParametersException 
	 */
	public Competition(Integer id, String name, String type, Date close_date,
			Collection<PCompetitor> list_competitor) throws BadParametersException {
		this.competition_id = id;
		this.setName(name);
		this.type = type;
		this.close_date = close_date;
		this.list_competitor = list_competitor;
	}

	public Competition(String name, String type, Date close_date) throws BadParametersException {
		this.setName(name);
		this.type = type;
		this.close_date = close_date;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws BadParametersException {
		if (name == null)
				throw new BadParametersException("name is not valid");


			checkStringName(name);
		
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getClose_date() {
		return close_date;
	}

	public void setClose_date(Date close_date) {
		this.close_date = close_date;
	}

	public Integer getId() {
		return competition_id;
	}

	public void setId(Integer competition_id) {
		this.competition_id = competition_id;
	}

	public Collection<PCompetitor> getList_competitor() {
		return list_competitor;
	}

	public void setList_competitor(Collection<PCompetitor> list_competitor) {
		this.list_competitor = list_competitor;
	}
	
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
//			if (!Character.isLetter(c) && (c != '-') && (c != ' ')&& (c != '_'))
			if (!Character.isLetter(c) && (c != '_'))
				throw new BadParametersException(
						"the name "
								+ name
								+ " contains other characters than letters, dashes and spaces");
		}
	}

}
