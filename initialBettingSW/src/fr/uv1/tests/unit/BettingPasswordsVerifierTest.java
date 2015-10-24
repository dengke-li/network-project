package fr.uv1.tests.unit;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import fr.uv1.bettingServices.BettingPasswordsVerifier;

public class BettingPasswordsVerifierTest {
	private BettingPasswordsVerifier b;
	
	@Before
	public void setUp() throws Exception {
		b=new BettingPasswordsVerifier();
	}

	@Test
	public void testVerify() {
		assertFalse(b.verify(null));
		char[] c1={'1','A','2','E','D'};
		assertFalse(b.verify(c1));
		char[] c2={'1','A','2','E','D','-','/',' '};
		assertFalse(b.verify(c2));
		char[] c3={'1','A','2','E','D','a','c','b'};
		assertTrue(b.verify(c3));
	}

	
}
