package fr.uv1.bettingServices;

import fr.uv1.utils.*;

/*
 * check the validity of a password
 * letters and digits are allowed. Password length should at least be LONG_PWD characters.
 * implements interface PasswordVerifier
 */
public class BettingPasswordsVerifier implements PasswordVerifier {

	@Override
	public boolean verify(char[] password) {
		if (password == null)
			return false;
		if (password.length < Constraints.LONG_PWD)
			return false;
		for (int i = 0; i < password.length; i++) {
			char c = password[i];
			if (!Character.isDigit(c) && !Character.isLetter(c))
				return false;
		}
		return true;
	}
}
