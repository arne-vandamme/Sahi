package net.sf.sahi.issue;

import net.sf.sahi.config.Configuration;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
 * Time: 12:38:08 PM
 */
public class TestJiraIssueCreator
{
	private JiraIssueCreator issueCreator;
	private XmlRpcClient mockXmlRpcClient;

	@Before
	public void setUp() throws Exception {
		Configuration.init();

		if ( mockXmlRpcClient == null ) {
			mockXmlRpcClient = mock( XmlRpcClient.class );
			issueCreator = new JiraIssueCreator( mockXmlRpcClient );
		}

		when( mockXmlRpcClient.execute( eq( "jira1.login" ), any( Object[].class ) ) ).thenReturn( "loginToken" );
	}

	@After
	public void tearDown() throws Exception {
		reset( mockXmlRpcClient );
	}

	@Test
	public void logout() throws Exception {
		issueCreator.logout();

		verify( mockXmlRpcClient ).execute( eq( "jira1.logout" ), any( Object[].class ) );
	}

	@Test
	public void initializeXmlRpcClient() throws Exception {
		XmlRpcClientConfigImpl mockConfigImpl = mock( XmlRpcClientConfigImpl.class );

		reset( mockXmlRpcClient );

		issueCreator.initializeXmlRpcClient( mockXmlRpcClient, mockConfigImpl );

		verify( mockXmlRpcClient ).setConfig( mockConfigImpl );
		verify( mockConfigImpl ).setServerURL( any(URL.class) );
	}

	@Test
	@Ignore
	@SuppressWarnings("unchecked")
	public void createIssue() throws Exception {
		issueCreator.setIssueParams( new HashMap() );
		/*
		mockXmlRpcClient.expects( once() ).method( "execute" ).with( eq( "jira1.createIssue" ), new Constraint()
		{
			public boolean eval( Object object ) {
				Object[] arr = (Object[]) object;
				Map issueParams = (Map) arr[1];

				return arr[0] instanceof String && issueParams.containsKey( "summary" ) && issueParams.containsKey(
						"description" );
			}

			public StringBuffer describeTo( StringBuffer stringBuffer ) {
				return null;
			}
		} );
		*/
		issueCreator.createIssue( new Issue( "", "" ) );
	}
//
//	public void testGetIssueParametersWithParameterNotFound() throws Exception {
//		try {
//			mockXmlRpcClient.expects( once() ).method( "execute" ).with( eq( "jira1.getProjects" ), ANYTHING ).will(
//					returnValue( new Object[0] ) );
//			issueCreator.getIssueParameters();
//			fail( "Should throw RuntimeException" );
//		}
//		catch ( RuntimeException e ) {
//			assertTrue( true );
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public void testGetIssueParametersWithParameterFound() throws Exception {
//		mockXmlRpcClient.expects( once() ).method( "execute" ).with( eq( "jira1.getProjects" ), ANYTHING ).will(
//				returnValue( new Object[] { getParamMap( "Sahi Integration" ) } ) );
//		mockXmlRpcClient.expects( once() ).method( "execute" ).with( eq( "jira1.getIssueTypes" ), ANYTHING ).will(
//				returnValue( new Object[] { getParamMap( "Sahi Bug" ) } ) );
//		mockXmlRpcClient.expects( once() ).method( "execute" ).with( eq( "jira1.getPriorities" ), ANYTHING ).will(
//				returnValue( new Object[] { getParamMap( "Major" ) } ) );
//		mockXmlRpcClient.expects( once() ).method( "execute" ).with( eq( "jira1.getComponents" ), ANYTHING ).will(
//				returnValue( new Object[] { getParamMap( "Regression" ) } ) );
//		mockXmlRpcClient.expects( atLeastOnce() ).method( "execute" ).with( eq( "jira1.getVersions" ), ANYTHING ).will(
//				returnValue( new Object[] { getParamMap( "0.1" ) } ) );
//		Map issueParams = issueCreator.getIssueParameters();
//		assertTrue( issueParams.containsKey( "project" ) );
//		assertTrue( issueParams.containsKey( "type" ) );
//		assertTrue( issueParams.containsKey( "priority" ) );
//		assertTrue( issueParams.containsKey( "assignee" ) );
//		assertTrue( issueParams.containsKey( "components" ) );
//	}

	private Map<String, String> getParamMap( String paramValue ) {
		Map<String, String> map = new HashMap<String, String>();
		map.put( "name", paramValue );
		return map;
	}
}
