package net.sf.sahi.client;

/**
 * Sahi - Web Automation and Test Tool
 *
 * Copyright  2006  V Narayan Raman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.foreach.sahi.testweb.TestWebServer;
import net.sf.sahi.Proxy;
import net.sf.sahi.config.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractSahiTest
{
	protected Browser browser;
	protected Browser b;
	protected Proxy proxy;
	protected String sahiBasePath = "c:/sahi-5.0";
	protected String userDataDirectory = "c:/sahi-5.0/userdata/";
	protected boolean isProxyInSameProcess = true;
	protected String browserName;

	public abstract void setBrowser();

	private static final TestWebServer webServer = new TestWebServer();

	@BeforeClass
	public static void startWebServer() {
		webServer.start();
	}

	@AfterClass
	public static void stopWebServer() {
		webServer.stop();
	}

	@Before
	public void setUp() {
		Configuration.initJava( sahiBasePath, userDataDirectory );

		if ( isProxyInSameProcess ) {
			proxy = new Proxy();
			proxy.start( true );
		}

		setBrowser();

		browser = new Browser( browserName );
		b = browser;
		browser.open();
	}

	@After
	public void tearDown() {
		browser.setSpeed( 100 );
		browser.close();
		if ( isProxyInSameProcess ) {
			proxy.stop();
		}
	}

	public void setBrowser( String browserName ) {
		this.browserName = browserName;
	}
}
