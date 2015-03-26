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
 * Authors: Dodda Rampradeep and Deepak Lewis
 */
package net.sf.sahi.command;

import net.sf.sahi.request.HttpRequest;
import net.sf.sahi.response.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class TestCommandInvoker
{
	private final File HELPFILE = new File( "help.txt" );

	private CommandInvoker commandInvoker;

	@Before
	public void setUp() throws Exception {
		commandInvoker = new CommandInvoker();
		HELPFILE.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		HELPFILE.delete();
	}

	@Test
	public void executeRunsACommand() throws InterruptedException {
		assertTrue( HELPFILE.exists() );
		HttpResponse response = commandInvoker.execute( prepareMockHttpRequest( getCommandPath( "test.cmd" ), true ) );
		String actualResponse = new String( response.data() );
		assertEquals( CommandInvoker.SUCCESS, actualResponse );
		assertFalse( HELPFILE.exists() );
	}

	@Test
	public void executeReturnsFailureForInvalidCommand() throws InterruptedException {
		HttpResponse response = commandInvoker.execute( prepareMockHttpRequest( "invalid", true ) );
		String actualResponse = new String( response.data() );
		assertEquals( CommandInvoker.FAILURE, actualResponse );
	}

	@Test
	public void executeRunsACommandInAsyncMode() throws InterruptedException {
		HttpResponse response = commandInvoker.execute( prepareMockHttpRequest( getCommandPath( "test.cmd" ), false ) );
		String actualResponse = new String( response.data() );
		assertEquals( CommandInvoker.SUCCESS, actualResponse );
	}

	private HttpRequest prepareMockHttpRequest( String commandToExecute, boolean sync ) {
		HttpRequest request = mock( HttpRequest.class );
		when( request.getParameter( RequestConstants.COMMAND ) ).thenReturn( commandToExecute );
		when( request.getParameter( RequestConstants.SYNC ) ).thenReturn( Boolean.toString( sync ) );

		return request;
	}

	private String getCommandPath( String command ) {
		URL resource = this.getClass().getResource( command );
		String commandToExecute = ( null == resource ) ? "invalid" : resource.getPath();
		if ( commandToExecute.startsWith( "/" ) ) {
			commandToExecute = commandToExecute.replaceFirst( "/", "" );
		}
		return commandToExecute;
	}

}
