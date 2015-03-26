package net.sf.sahi.report;

import net.sf.sahi.config.Configuration;
import net.sf.sahi.test.TestLauncher;
import net.sf.sahi.util.Utils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

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

/**
 * User: dlewis
 * Date: Dec 11, 2006
 * Time: 4:50:00 PM
 */
public class TestSahiReporter
{
	static {
		Configuration.init();
	}

	private SahiReporter reporter;
	private Formatter mockFormatter;

	@Before
	public void setUp() throws Exception {
		mockFormatter = mock( Formatter.class );
		reporter = new SahiReporter( "", mockFormatter )
		{
			public boolean createSuiteLogFolder() {
				return false;
			}
		};
	}

	@Test
	public void generateSuiteReport() {
		when( mockFormatter.getFileName( anyString() ) ).thenReturn( "testFile" );
		when( mockFormatter.getHeader() ).thenReturn( "data" );
		when( mockFormatter.getSummaryHeader() ).thenReturn( "data" );
		when( mockFormatter.getSummaryFooter() ).thenReturn( "data" );
		when( mockFormatter.getFooter() ).thenReturn( "data" );

		reporter.generateSuiteReport( new ArrayList<TestLauncher>() );

		verify( mockFormatter ).getSuiteLogFileName();
	}

	@Test
	public void getLogDirForNullLogDir() {
		assertEquals( Configuration.getPlayBackLogsRoot(), reporter.getLogDir() );
	}

	@Test
	public void getLogDirForCustomLogDir() {
		reporter.setLogDir( "customDir" );
		assertEquals( "customDir", reporter.getLogDir() );
	}

	@Test
	public void getLogDirForNullLogDirWithCreateSuiteFolderSetToTrue() {
		reporter = new SahiReporter( "", mockFormatter )
		{
			public boolean createSuiteLogFolder() {
				return true;
			}
		};
		reporter.setSuiteName( "junit" );
		if ( Utils.isWindows() ) {
			assertTrue( reporter.getLogDir().startsWith( Configuration.getPlayBackLogsRoot() + "\\junit__" ) );
		}
		else {
			assertTrue( reporter.getLogDir().startsWith( Configuration.getPlayBackLogsRoot() + "/junit__" ) );
		}

	}
}
