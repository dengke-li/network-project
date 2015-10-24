package fr.uv1.utils;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date; /*we use SQL.date , it's more interesting for SQL */

import fr.uv1.bettingServices.BadParametersException;

/**
 * @author Philippe TANGUY
 */
public class Utils {
	// -----------------------------------------------------------------------------
	private static DateFormat formatDeDate = new SimpleDateFormat("dd-MM-yyyy");

	// -----------------------------------------------------------------------------
	/**
	 * Convertit une date (chaîne de caractères) exprimée au format "dd-MM-yyyy"
	 * (exemple 05/09/2010 ou 5/9/2010) en une instance de la classe Date.<br/>
	 * L'opération inverse est assurée par la méthode
	 * {@link bibliotheque_fr.service.dateToString}
	 * 
	 * @param dateString
	 *            la date dans une chaîne de catactères
	 * @return la date dans une instance de {@link java.sql.Date}
	 */
	@SuppressWarnings("deprecation")
	public static Date stringToDate (String dateString) throws BadParametersException{
		Date d = null;
		try {
			d = new Date(formatDeDate.parse(dateString).getYear(), formatDeDate
					.parse(dateString).getMonth(), formatDeDate.parse(
					dateString).getDate());
		} catch (ParseException e) {			

			throw new BadParametersException("date  invalid or not instantiated");
		}
		return d;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Convertit une date (instance de {@link java.util.Date}) en une chaîne de
	 * caractères exprimée au format "dd/MM/yyyy" (exemple 05/09/2010 ou
	 * 5/9/2010).<br/>
	 * L'opération inverse est assurée par la méthode
	 * {@link bibliotheque_fr.service.stringToDate}
	 * 
	 * @param uneDate
	 * @return
	 */
	public static String dateToString(Date uneDate) {
		return formatDeDate.format(uneDate);
	}

	// -----------------------------------------------------------------------------
	/**
	 * Renvoie la différence en nombre de jours entre deux dates.<br/>
	 * Si d2 est après d1 le nombre de jours sera positif, négatif dans le cas
	 * contraire.
	 * 
	 * @param d1
	 *            première date
	 * @param d2
	 *            deuxième date
	 * @return un entier représentant le nombre de jour
	 */
	public static int diffEntreDates(Date d1, Date d2) {
		long t1 = d1.getTime();
		long t2 = d2.getTime();
		// 86400000 = nbre de millisecondes en 24 heures
		return (int) ((t1 - t2) / 86400000);
	}

	/**
	 * get the age
	 * 
	 * @param sql Date d
	 *            
	 * @return int age
	 */
	public static int getage(Date d) {
		Calendar cal = Calendar.getInstance();
		@SuppressWarnings("deprecation")
		Date now = new Date(cal.get(Calendar.YEAR) - 1900,
				cal.get(Calendar.MONTH), (cal.get(Calendar.DATE)));
		return Utils.diffEntreDates(now, d) / 365;
	}
	// -----------------------------------------------------------------------------
}
