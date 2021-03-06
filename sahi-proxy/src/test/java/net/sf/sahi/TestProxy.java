package net.sf.sahi;

import net.sf.sahi.config.Configuration;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Sahi - Web Automation and Test Tool
 * <p/>
 * Copyright  2006  V Narayan Raman
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TestProxy
{
	@Test
	public void createAndStartProxyFromJava() throws Exception {
		Configuration.init();
		final Proxy proxy = new Proxy();
		assertFalse( proxy.isRunning() );

		try {
			proxy.start();
			Thread.sleep( 3000 );
			assertTrue( proxy.isRunning() );
		}
		catch ( Exception e ) {
			fail( e.getMessage() );
		}
		finally {
			proxy.stop();
			assertFalse( proxy.isRunning() );
		}
	}
}
