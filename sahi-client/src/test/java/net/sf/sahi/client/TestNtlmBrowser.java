package net.sf.sahi.client;

import net.sf.sahi.config.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class TestNtlmBrowser
{
	private Browser browser;
	private String sahiBasePath = ".";
	private String userDataDirectory = "./userdata/";
	private String baseURL = "http://sahipro.com";

	@Before
	public void setUp() throws Exception {
		Configuration.initJava( sahiBasePath, userDataDirectory );

		toggleIEProxy( true );

		browser = new Browser( "ie" );
		browser.open();
	}

	@After
	public void tearDown() throws Exception {
		browser.close();
		toggleIEProxy( false );
	}

	@Test
	public void ntlmBrowser() {
		browser.navigateTo( baseURL + "/html/demo/formTest.htm" );
		browser.textbox( "t1" ).setValue( "aaa" );

		browser.waitFor( 10000 );
		browser.restartPlayback();

		browser.link( "Back" ).click();
		browser.link( "Table Test" ).click();
		assertEquals( "Cell with id", browser.cell( "CellWithId" ).getText() );
	}

	public void toggleIEProxy( boolean enable ) {
		try {
			Runtime.getRuntime().exec(
					new String[] { sahiBasePath + "\\tools\\toggle_IE_proxy.exe", ( enable ? "enable" : "disable" ) } );
			Thread.sleep( 1000 );
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
