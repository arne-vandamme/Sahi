package net.sf.sahi;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TestHttpHeaders
{
	@Test
	public void keyIsStoredCaseInsensitive() {
		HttpHeaders headers = new HttpHeaders();
		headers.addHeader( "Transfer-Encoding", "value" );

		assertTrue( headers.hasHeader( "transfer-encoding" ) );
		assertTrue( headers.hasHeader( "Transfer-Encoding" ) );
		assertTrue( headers.hasHeader( "Transfer-encoding" ) );

		headers.removeHeader( "Transfer-encoding" );
		assertFalse( headers.hasHeader( "transfer-encoding" ) );
		assertFalse( headers.hasHeader( "Transfer-Encoding" ) );
		assertFalse( headers.hasHeader( "Transfer-encoding" ) );

		assertTrue( headers.keySet().isEmpty() );
	}

	@Test
	public void multipleHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.addHeader( "Domains", "www.google.com" );
		headers.addHeader( "Domains", "www.google.be" );

		assertEquals( "www.google.com,www.google.be", headers.getHeader( "Domains" ) );
		assertEquals( Arrays.asList( "www.google.com", "www.google.be" ), headers.getHeaders( "Domains" ) );
	}
}
