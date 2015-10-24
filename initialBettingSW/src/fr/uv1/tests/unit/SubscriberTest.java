package fr.uv1.tests.unit;



public class SubscriberTest {

//	private Subscriber subs;
//
//	@Before
//	public void setUp() throws Exception {
//		subs = new Subscriber(new String("Duran"), new String("Miguel"),new String("worldChamp"),19);
//	}
//
//	/*
//	 * Subscribers should be created with valid strings
//	 */
//	@Test
//	public void testSubscriber() {
//		assertTrue(subs.getFirstName().equals("Miguel"));
//		assertTrue(subs.getLastName().equals("Duran"));
//		assertTrue(subs.getUsername().equals("worldChamp"));
//		assertTrue(subs.getPassword() != null);
//	}
//
//	@Test(expected=BadParametersException.class)
//	public void testInvalidSubscriber() throws BadParametersException {
//		// not instantiated parameters
//		new Subscriber(null, null, null,19);
//// 		new Subscriber(null, null, new String("worldChamp"));
////		new Subscriber(new String("Duran"), null, new String("worldChamp"));
////		new Subscriber(null, new String("Miguel"), new String("worldChamp"));
////		new Subscriber(null, new String("Miguel"), null);
////		new Subscriber(new String("Duran"), null, null);
////		new Subscriber(new String("Duran"), new String("Miguel"), null);
//		
//		// invalid parameters
////		new Subscriber(new String("Duran"), new String("Miguel"), new String(""));
////		new Subscriber(new String(""), new String(""), new String(""));
////		new Subscriber(new String(""), new String(""), new String("worldChamp"));
////		new Subscriber(new String(""), new String("Miguel"), new String("worldChamp"));
////		new Subscriber(new String("Duran"), new String(""), new String("worldChamp"));
////		new Subscriber(new String("Duran"), new String(""), new String(""));
////		new Subscriber(new String("Duran"), new String(""), new String("worldChamp"));
//	}
//	
//	@Test
//	public void testHasUsername() {
//		assertTrue(subs.getUsername().equals("worldChamp"));
//
//		assertFalse(subs.getUsername().equals("wddfkjddfk"));
//	}
//
//	@Test
//	public void testEqualsObject() throws BadParametersException {
//		// Same subscriber = same username
//		Subscriber s = new Subscriber(new String("Duran"),
//				new String("Miguel"), new String("worldChamp"),19);
//		assertTrue(subs.equals(s));
//
//		s = new Subscriber(new String("Durano"), new String("Miguel"),
//				new String("worldChamp"),19);
//		assertTrue(subs.equals(s));
//
//		s = new Subscriber(new String("Duran"), new String("Miguelo"),
//				new String("worldChamp"),19);
//		assertTrue(subs.equals(s));
//
//		s = new Subscriber(new String("Durano"), new String("Miguelo"),
//				new String("worldChamp"),19);
//		assertTrue(subs.equals(s));
//
//		// Different subscriber = different username
//		s = new Subscriber(new String("Duran"), new String("Miguel"),
//				new String("worldC"),19);
//		assertFalse(subs.equals(s));
//	}
//	
//	//we add these:
//	@Test
//	public void testequal() throws BadParametersException {
//		assertTrue(subs.equals(new Subscriber("liu","hongcheng","worldChamp",19)));
//		assertFalse(subs.equals(new Subscriber("Duran","Marc","dengke",19)));
//		assertFalse(subs.equals(new Subscriber("Marc","Miguel","jianfei",19)));
//		
//	}
//	
//	@Test
//	public void testgetLastname() throws BadParametersException {
//		assertTrue(subs.getLastName().equals("Duran"));
//		
//	}
//	
//	@Test
//	public void testgetFirstname() throws BadParametersException {
//		assertTrue(subs.getFirstName().equals("Miguel"));
//		
//	}
//
//	@Test
//	public void testgetUsername() throws BadParametersException {
//		assertTrue(subs.getUsername().equals("worldChamp"));
//		
//	}
//			
//	@Test
//	public void testsetLastname() throws BadParametersException {	
//		subs.setLastname("David");
//		assertTrue(subs.getLastName().equals("David"));
//		
//	}
//	
//	@Test
//	public void testsetFirstname() throws BadParametersException {
//		subs.setFirstname("Max");
//		assertTrue(subs.getFirstName().equals("Max"));
//		
//	}
//
//	@Test
//	public void testsetUsername() throws BadParametersException {
//		subs.setUsername("Hello");
//		assertTrue(subs.getUsername().equals("Hello"));
//		
//	}
//			
//	
//		
//	
//	
//	
}