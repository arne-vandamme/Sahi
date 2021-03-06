package net.sf.sahi.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
 * User: nraman
 * Date: May 18, 2005
 * Time: 8:42:08 PM
 */
public class TestMultiPartSubRequest
{
	@Test
	public void parse() {
		String s = "form-data; name=\"f1\"; filename=\"test.txt\"";
		MultiPartSubRequest multiPartSubRequest = new MultiPartSubRequest();
		multiPartSubRequest.setNameAndFileName( s );
		assertEquals( "f1", multiPartSubRequest.name() );
		assertEquals( "test.txt", multiPartSubRequest.fileName() );
	}

	@Test
	public void getValue() {
		assertEquals( "f1", MultiPartSubRequest.getValue( "name=\"f1\"" ) );
		assertEquals( "test.txt", MultiPartSubRequest.getValue( "filename=\"test.txt\"" ) );
	}

}
