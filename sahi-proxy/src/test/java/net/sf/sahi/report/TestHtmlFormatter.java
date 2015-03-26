package net.sf.sahi.report;

import net.sf.sahi.config.Configuration;
import net.sf.sahi.util.Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
 * @author dlewis
 */
public class TestHtmlFormatter
{
	static {
		Configuration.init();
	}

	private HtmlFormatter formatter = null;

	private String expectedSummaryForEmptyList = new StringBuffer(
			"<tr class=\"SUCCESS\"><td>test</td><td>0</td>" ).append(
			"<td>0</td><td>0</td><td>100%</td><td>0</td></tr>" ).toString();

	private String expectedSummaryForAllTypes = new StringBuffer( "<tr class=\"FAILURE\"><td>test</td><td>3</td>" )
			.append(
					"<td>1</td><td>0</td><td>66%</td><td>0</td></tr>" ).toString();

	@Before
	public void setUp() throws Exception {
		formatter = new HtmlFormatter();
	}

	@Test
	public void getFileName() {
		assertEquals( "test.htm", formatter.getFileName( "test" ) );
	}

	@Test
	@Ignore
	public void getStringResultForSuccessResult() {
		String expected = "<div class=\"SUCCESS\"><a class=\"SUCCESS\">_assertNotNull(_textarea(\"t2\"));</a></div>";
		assertEquals( expected, formatter.getStringResult( ReportUtil
				                                                   .getSuccessResult() ) );
	}

	@Test
	@Ignore
	public void getStringResultForFailureResult() {
		String expected =
				"<div class=\"FAILURE\"><a class=\"FAILURE\">_call(testAccessors()); Assertion Failed.</a></div>";
		assertEquals( expected, formatter.getStringResult( ReportUtil
				                                                   .getFailureResultWithoutDebugInfo() ) );
	}

	@Test
	@Ignore
	public void getStringResultForInfoResult() {
		String expected =
				"<div class=\"INFO\"><a class=\"INFO\" href=\"/_s_/dyn/Log_highlight?href=blah\">_click(_link(\"Form Test\"));</a></div>";
		assertEquals( expected, formatter.getStringResult( ReportUtil
				                                                   .getInfoResult() ) );
	}

	@Test
	public void getResultDataForEmptyList() {
		assertEquals( "", formatter.getResultData( null ) );
	}

	@Test
	public void getResultDataForListWithAllTypesOfResults() {
		String expected = new StringBuffer( formatter.getStringResult( ReportUtil
				                                                               .getInfoResult() ) ).append( "\n" )
		                                                                                           .append(
				                                                                                           formatter
						                                                                                           .getStringResult(
								                                                                                           ReportUtil
										                                                                                           .getSuccessResult() ) )
		                                                                                           .append( "\n" )
		                                                                                           .append(
				                                                                                           formatter
						                                                                                           .getStringResult(
								                                                                                           ReportUtil
										                                                                                           .getFailureResultWithoutDebugInfo() ) )
		                                                                                           .append( "\n" )
		                                                                                           .toString();

		assertEquals( expected, formatter.getResultData( ReportUtil
				                                                 .getListResult() ) );
	}

	@Test
	public void getHeader() {
		String expected = new StringBuffer(
				"<head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n<style>\n" ).append(
				new String( Utils.readFileAsString( Configuration
						                                    .getPlaybackLogCSSFileName( true ) ) ) ).append(
				new String( Utils.readFileAsString( Configuration
						                                    .getConsolidatedLogCSSFileName( true ) ) ) ).append(
				"</style></head>\n" ).toString();
		assertEquals( expected, formatter.getHeader() );
	}

	@Test
	public void getSummaryHeader() {
		String expected =
				"<table class='summary'><tr><td>Test</td><td>Total Steps</td><td>Failures</td><td>Errors</td><td>Success Rate</td><td>Time Taken (ms)</td></tr>";
		assertEquals( expected, formatter.getSummaryHeader() );
	}

	@Test
	public void getSummaryFooter() {
		String expected = "</table>";
		assertEquals( expected, formatter.getSummaryFooter() );
	}

	@Test
	public void getSummaryDataForEmptyList() {
		TestSummary summary = new TestSummary();
		summary.setScriptName( "test" );
		assertEquals( expectedSummaryForEmptyList, formatter
				.getSummaryData( summary ) );
	}

	@Test
	public void getSummaryDataForAllTypesWithoutLink() {
		assertEquals( expectedSummaryForAllTypes, formatter
				.getSummaryData( ReportUtil.getTestSummary() ) );
	}

	@Test
	public void getSummaryDataForAllTypesWithLink() {
		String expected = expectedSummaryForAllTypes.replaceFirst( "test",
		                                                           "<a class=\"SCRIPT\" href=\"test.htm\">test</a>" );
		TestSummary summary = ReportUtil.getTestSummary();
		summary.setLogFileName( "test" );
		summary.setAddLink( true );
		assertEquals( expected, formatter.getSummaryData( summary ) );
	}

	@Test
	public void newLinesConvertedToBRTag() {
		String expected = "Difference in array length:<br/>Expected Length<br/>Another line<br/>abc";
		TestResult result = new TestResult( "Difference in array length:\nExpected Length\nAnother line",
		                                    ResultType.INFO, "abc", "abc" );
		String stringResult = formatter.getStringResult( result );
		assertTrue( stringResult.contains( expected ) );
	}
}
