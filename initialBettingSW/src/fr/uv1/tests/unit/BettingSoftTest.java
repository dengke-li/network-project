package fr.uv1.tests.unit;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import fr.uv1.bettingServices.AuthenticationException;
import fr.uv1.bettingServices.BadParametersException;
import fr.uv1.bettingServices.Betting;
import fr.uv1.bettingServices.BettingSoft;
import fr.uv1.bettingServices.ExistingSubscriberException;

//import fr.uv1.bettingServices.Subscriber;

public class BettingSoftTest {

        
        private static Betting aSoft;
        @Before
        public void setUp() throws Exception {
                aSoft = new BettingSoft(new String("ilesCaimans"));
        }
        
        
        // ********************************
        // * Tests "subscribe" *
        // ********************************
       
        /*
         *  Allow testing the  BadParametersException from the bad parameters of the method subscribe()
         */
        @Test(expected=BadParametersException.class)
        public void subscribetest() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
        	// not instantiated parameters
            //aSoft.subscribe(null, new String("Albert"),new String("worldChamp"), new String("ilesCaimans"),19);
//			aSoft.subscribe(new String("Duran"), null,new String("worldChamp"), new String("ilesCaimans"));
//			aSoft.subscribe(new String("Duran"), new String("Albert"), null,new String("ilesCaimans"));
//			aSoft.subscribe(new String("Duran"), new String("Albert"),new String("worldChamp"), null);
            
            // invalid parameters
//			aSoft.subscribe(new String(""), new String("Albert"), new String("worldChamp"), new String("ilesCaimans"));
//			aSoft.subscribe(new String("Duran"), new String(""), new String("worldChamp"), new String("ilesCaimans"));
//			aSoft.subscribe(new String("Nobel"), new String("Alfred"), new String(""), new String("ilesCaimans"));
//			aSoft.subscribe(new String("Duran"), new String("Roberto"),new String("worldChamp"), new String(""));
        }
        

        /*
         * Allow testing the  ExistingSubscriberException from the the method Subscribe()
         */
        
        //The same subscriber
//        @Test(expected=ExistingSubscriberException.class)
//        public void ExistingSubscriberExceptionSubscribetest1() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
//        		aSoft.subscribe(new String("Duran"), new String("Albert"),
//                                new String("fanfan"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Duran"), new String("Albert"),
//                        new String("fanfan"), new String("ilesCaimans"),19);}
//      
//        // same firstname, username ; different lastname
//        @Test(expected=ExistingSubscriberException.class)
//        public void ExistingSubscriberExceptionSubscribetest2() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
//        		aSoft.subscribe(new String("Duran"), new String("Albert"),
//                                new String("fanfan"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Durano"), new String("Albert"),
//                        new String("fanfan"), new String("ilesCaimans"),19);}
//       
//        // same lastname, username; different firstname
//        @Test(expected=ExistingSubscriberException.class)
//        public void ExistingSubscriberExceptionSubscribetest3() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException { 
//        		aSoft.subscribe(new String("Duran"), new String("Albert"),
//                                new String("fanfan"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Duran"), new String("Alfred"),
//                        new String("fanfan"), new String("ilesCaimans"),19);
//        }
//      
//        // same lastname, firstname; different username
//        @Test
//        public void ExistingSubscriberExceptionSubscribetest4() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
//        	
//        		aSoft.subscribe(new String("Duran"), new String("Albert"),
//                        new String("fanfan"), new String("ilesCaimans"),19);
//        		aSoft.subscribe(new String("Duran"), new String("Albert"),
//        				new String("fanfin"), new String("ilesCaimans"),19);
//		 }
//       
//        // same firstname; different lastname, username
//        @Test
//        public void ExistingSubscriberExceptionSubscribetest5() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
//                aSoft.subscribe(new String("Duran"), new String("Albert"),
//                                new String("fanfan"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Durano"), new String("Albert"),
//                        		new String("fanfin"), new String("ilesCaimans"),19);
//        }
//        
//        // same lastname; different username and firstname
//        @Test
//        public void ExistingSubscriberExceptionSubscribetest6() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
//                aSoft.subscribe(new String("Duran"), new String("Albert"),
//                                new String("fanfan"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Duran"), new String("Morgan"),
//                        		new String("fanfon"), new String("ilesCaimans"),19);
//        }
//      
//        // different lastname, firstname and username
//        @Test
//        public void ExistingSubscriberExceptionSubscribetest7() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {
//        		aSoft.subscribe(new String("Duran"), new String("Albert"),
//                    new String("fanfan"), new String("ilesCaimans"),19);	
//        		aSoft.subscribe(new String("Mato"), new String("Anna"), new String(
//                        "salto"), new String("ilesCaimans"),19);
//        }
//        
//        
//        
//        /*
//         * Allow testing the length of mdp of Subscriber 
//         */
//        @Test
//        public void subscribermdplengthtest() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException {                
//                assertTrue(aSoft.subscribe(new String("Duran"), new String("Miguel"),
//                                new String("worldChamp"), new String("ilesCaimans"),19).length() == 8);
//        }
        
        
    
        
    
        // ********************************
        // * Tests "unsubscribe" *
        // ********************************
        
        /*
         *  Allow testing the  ExistingSubscriberException from the method unsubscribe()
         */
        
        //User name of subscriber is null
        @Test(expected=ExistingSubscriberException.class)
        public void ExistingSubscriberExceptionUnsubscribetest1() throws AuthenticationException,ExistingSubscriberException, SQLException, BadParametersException {
    			aSoft.unsubscribe(null, new String("ilesCaimans"));
    	}
        
        //Unsubscribe one subscriber two times
//        @Test(expected=ExistingSubscriberException.class)
////        public void testUnsubscribe6() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException{
////    			aSoft.subscribe(new String("Duran"), new String("Alfred"),new String("fanfan"), new String("ilesCaimans"),19);
////    			aSoft.unsubscribe(new String("fanfan"), new String("ilesCaimans"));
////    			aSoft.unsubscribe(new String("fanfan"), new String("ilesCaimans"));
//        }
    
        //Unsubscribe one inexistent subscriber
//        @Test(expected=ExistingSubscriberException.class)
//        public void testUnsubscribe7() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException{
//    			aSoft.unsubscribe(new String("tito"), new String("ilesCaimans"));
//        }
    
        
        
        /*
         *  Allow testing the  AuthenticationException from the method unsubscribe()
         */
        //Tests null manager password
        @Test(expected=AuthenticationException.class)
        public void ExistingSubscriberExceptionUnsubscribetest2() throws AuthenticationException,ExistingSubscriberException, SQLException, BadParametersException {
    			aSoft.unsubscribe(new String("nanard"), null);}

        // Tests incorrect manager password
        @Test(expected=AuthenticationException.class)
        public void ExistingSubscriberExceptionUnsubscribetest3() throws AuthenticationException,ExistingSubscriberException, SQLException, BadParametersException {        
        		aSoft.unsubscribe(new String("nanard"), new String(""));}
        
        // Uses the number of subscribers to test the function of unsbscribe()
//        @Test
//        public void testUnsubscribe() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException{
//        		aSoft.subscribe(new String("linus"), new String("Albert"),
//                                new String("worldCu"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("lukia"), new String("Albliu"),
//                                new String("rldCpu"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Duran"), new String("Albert"), new String("iskkfu"),
//                                new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("linus"), new String("Albert"),
//                                new String("wouuuu"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("lukia"), new String("Albliu"),
//                                new String("wodCpd"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("Duran"), new String("Albert"), new String("jfiuuu"),
//                                new String("ilesCaimans"),19);
//                aSoft.unsubscribe(new String("worldCu"), new String("ilesCaimans"));
//                assertTrue(aSoft.listSubscribers(new String("ilesCaimans")).size()==5);
//        }
//    
      
        // ********************************
        // * Tests "listsubscribers" *
        // ********************************
        
        /*
         *  Allow testing the  AuthenticationException from the method listSubscribers()
         */
        
        //Tests null manager password
        @Test(expected=AuthenticationException.class)
        public void AuthenticationExceptionlistSubscriberstest1() throws AuthenticationException, SQLException, BadParametersException{
                aSoft.listSubscribers(null);
                        }
        //Tests the number of subscribers before any subscription
        @Test
        public void AuthenticationExceptionlistSubscriberstest2() throws AuthenticationException, SQLException, BadParametersException{
                assertTrue(aSoft.listSubscribers(new String("ilesCaimans")).size() == 0);
                        }
        //Tests the number of subscribers after subscriptions and tests the list returned by the listSubscribers() 
//        @Test
//        public void testlistSubscribers() throws AuthenticationException, ExistingSubscriberException, BadParametersException, SQLException{
//                aSoft.subscribe(new String("linus"), new String("Albert"),
//                                new String("worldC"), new String("ilesCaimans"),19);
//                aSoft.subscribe(new String("lukia"), new String("Albliu"),
//                                new String("worldCp"), new String("ilesCaimans"),19);                
//                assertTrue(aSoft.listSubscribers(new String("ilesCaimans")).size() == 2);
//                
//                aSoft.subscribe(new String("Durakun"), new String("Albljt"),
//                                new String("worki"), new String("ilesCaimans"),19);
//                assertTrue(aSoft.listSubscribers(new String("ilesCaimans")).size() == 3);
//                
//                //the result of the method  listSubscribers()
//                ArrayList<ArrayList<String>> result = aSoft.listSubscribers(new String("ilesCaimans"));
//                		//******************************************************************************
//                		//System.out.println(result);
//                		//we fund one error by this test; the error in the method listSubscribers() is
//                		//that in the line number 114, the creation of the subsData should be in the 
//                		//circle "for".
//                		//******************************************************************************
//                assertTrue(result.get(0).get(0).equals(new String("linus")));                        
//                assertTrue(result.get(0).get(1).equals(new String("Albert")));
//                assertTrue(result.get(0).get(2).equals(new String("worldC")));
//                        
//                assertTrue(result.get(1).get(0).equals(new String("lukia")));
//                assertTrue(result.get(1).get(1).equals(new String("Albliu")));
//                assertTrue(result.get(1).get(2).equals(new String("worldCp")));
//                        
//                assertTrue(result.get(2).get(0).equals(new String("Durakun")));
//                assertTrue(result.get(2).get(1).equals(new String("Albljt")));
//                assertTrue(result.get(2).get(2).equals(new String("worki")));
//                        
//        }
                    
        // ********************************
        // * Tests "authenticateMngr" *
        // ********************************
        
//        @Test(expected=AuthenticationException.class)
//        public void authenticateMngrtest1() throws AuthenticationException{
//    			aSoft.authenticateMngr(null);
//        }
        
//        @Test(expected=AuthenticationException.class)
//        public void authenticateMngrtest2() throws AuthenticationException{
//        			aSoft.authenticateMngr(new String("ilesLofotens"));
//        }
//    
//        @Test(expected=AuthenticationException.class)
//        public void authenticateMngrtest3() throws AuthenticationException{
//    			aSoft.authenticateMngr(new String(""));
//        }
    
        // ********************************
        // * Tests "changeMngrPwd" *
        // ********************************
//         
//        @Test(expected=AuthenticationException.class)            
//        public void changeMngrPwdtest1() throws AuthenticationException, BadParametersException{
//    			aSoft.changeMngrPwd(null, null);
//        }
//    
//        @Test(expected=AuthenticationException.class)            
//        public void changeMngrPwdtest2() throws AuthenticationException, BadParametersException{
//        		aSoft.changeMngrPwd(new String("ilesCanaries"), null);
//    	}
//    
//        
//        @Test(expected=AuthenticationException.class)            
//        public void changeMngrPwdtest3() throws AuthenticationException, BadParametersException{
//                aSoft.changeMngrPwd(new String("ilesCanaries"), new String("qsd"));
//        }
//    
//        @Test(expected=BadParametersException.class)
//        public void changeMngrPwdtest4() throws BadParametersException, AuthenticationException{
//                aSoft.changeMngrPwd(null, new String("ilesCaimans"));
//        }
//        
//        @Test(expected=BadParametersException.class)
//        public void changeMngrPwdtest5() throws BadParametersException, AuthenticationException{
//        		aSoft.changeMngrPwd(new String("d"), new String("ilesCaimans"));
//        }
//        
//        @Test(expected=BadParametersException.class)
//        public void changeMngrPwdtest6() throws BadParametersException, AuthenticationException{
//        		aSoft.changeMngrPwd(new String("ddfqsdqsdfqsfdf$"), new String(
//                        "ilesCaimans"));
//        }
}
