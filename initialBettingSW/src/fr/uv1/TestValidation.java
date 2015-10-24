/**
 * 
 */
package fr.uv1;

import fr.uv1.bettingServices.BadParametersException;
import fr.uv1.bettingServices.Betting;
import fr.uv1.bettingServices.BettingSoft;
import fr.uv1.tests.validation.*;

/**
 * @author irebai
 *
 */
public class TestValidation {

	/**
	 * 
	 */
	public TestValidation() {
		// TODO create instance of class TestSubscribersBettingService and invoke launchSubscribersValidationtest();
	
		new FirstIncrementValidationTests() {
			@Override
			public Betting plugToBetting() {
				BettingSoft bettingSoft = null;
				try {
					bettingSoft = new BettingSoft("a1234567");
				} catch (BadParametersException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bettingSoft;
			}
			
			@Override
			public String getManagerPassword() {
				// TODO Auto-generated method stub
				return "a1234567";
			}
		};
		new SecondIncrementValidationTests() {
			@Override
			public Betting plugToBetting() {
				BettingSoft bettingSoft = null;
				try {
					bettingSoft = new BettingSoft("a1234567");
				} catch (BadParametersException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bettingSoft;
			}
			
			@Override
			public String getManagerPassword() {
				// TODO Auto-generated method stub
				return "a1234567";
			}
		};
		
		
		new ThirdIncrementValidationTests() {
			@Override
			public Betting plugToBetting() {
				BettingSoft bettingSoft = null;
				try {
					bettingSoft = new BettingSoft("a1234567");
				} catch (BadParametersException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bettingSoft;
			}
			
			@Override
			public String getManagerPassword() {
				// TODO Auto-generated method stub
				return "a1234567";
			}
		};
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestValidation();
	}
}
