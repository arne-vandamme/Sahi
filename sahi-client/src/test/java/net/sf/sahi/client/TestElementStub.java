package net.sf.sahi.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
public class TestElementStub
{
	private Browser browser = new Browser();

	@Test
	public void toStringForSingleArg() {
		ElementStub stub = new ElementStub( "textbox", browser, "abc" );
		assertEquals( "_sahi._textbox(\"abc\")", stub.toString() );
	}

	@Test
	public void toStringForVariableArgs() {
		ElementStub stub = new ElementStub( "textbox", browser, "abc", "def" );
		assertEquals( "_sahi._textbox(\"abc\", \"def\")", stub.toString() );
	}

	@Test
	public void toStringForSingleNumberArg() {
		ElementStub stub = new ElementStub( "textbox", browser, 1 );
		assertEquals( "_sahi._textbox(1)", stub.toString() );
	}

	@Test
	public void toStringForNumberArgs() {
		ElementStub stub = new ElementStub( "textbox", browser, 1, 231 );
		assertEquals( "_sahi._textbox(1, 231)", stub.toString() );
	}

	@Test
	public void toStringForSingleElementStubArg() {
		ElementStub stub = new ElementStub( "textbox", browser, new ElementStub( "checkbox", browser, "checkme" ) );
		assertEquals( "_sahi._textbox(_sahi._checkbox(\"checkme\"))", stub.toString() );
	}

	@Test
	public void toStringForElementStubArgs() {
		ElementStub stub = new ElementStub( "textbox", browser, new ElementStub( "checkbox", browser, "checkme" ),
		                                    new ElementStub( "radio", browser, "r" ) );
		assertEquals( "_sahi._textbox(_sahi._checkbox(\"checkme\"), _sahi._radio(\"r\"))", stub.toString() );
	}

	@Test
	public void toStringForIn() {
		ElementStub stub = browser.button( "delete" ).in( browser.div( "myDiv" ) );
		assertEquals( "_sahi._button(\"delete\", _sahi._in(_sahi._div(\"myDiv\")))", stub.toString() );
	}

	@Test
	public void toStringForDoubleIn() {
		ElementStub stub = browser.button( "delete" ).in( browser.div( "myDiv" ).in( browser.div( "myDiv2" ) ) );
		assertEquals( "_sahi._button(\"delete\", _sahi._in(_sahi._div(\"myDiv\", _sahi._in(_sahi._div(\"myDiv2\")))))",
		              stub.toString() );
	}

	@Test
	public void toStringForNear() {
		ElementStub stub = browser.button( "delete" ).near( browser.link( "user" ) );
		assertEquals( "_sahi._button(\"delete\", _sahi._near(_sahi._link(\"user\")))", stub.toString() );
	}

	@Test
	public void toStringForParentNode() {
		ElementStub stub = browser.button( "delete" ).parentNode();
		assertEquals( "_sahi._parentNode(_sahi._button(\"delete\"))", stub.toString() );
		stub = browser.button( "delete" ).parentNode( "table" );
		assertEquals( "_sahi._parentNode(_sahi._button(\"delete\"), \"table\")", stub.toString() );
		stub = browser.button( "delete" ).parentNode( "table", 5 );
		assertEquals( "_sahi._parentNode(_sahi._button(\"delete\"), \"table\", \"5\")", stub.toString() );
	}

	@Test
	public void indexIsCorrectlyAdded() throws Exception {
		ElementStub stub = browser.textbox( "abc" );
		stub.setIndex( 2 );
		assertEquals( "_sahi._textbox(\"abc[2]\")", stub.toString() );
		stub = browser.button( "delete" ).in( browser.div( "myDiv" ) );
		stub.setIndex( 3 );
		assertEquals( "_sahi._button(\"delete[3]\", _sahi._in(_sahi._div(\"myDiv\")))", stub.toString() );
	}
}
