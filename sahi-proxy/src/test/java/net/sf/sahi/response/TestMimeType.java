package net.sf.sahi.response;

import net.sf.sahi.config.Configuration;
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
public class TestMimeType
{
	static {
		Configuration.init();
	}

	@Test
	public void getExtension() {
		assertEquals( ".js", MimeType.getExtension( "aa.js" ) );
		assertEquals( ".JS", MimeType.getExtension( "aa.JS" ) );
		assertEquals( ".htm", MimeType.getExtension( "aa.htm" ) );
		assertEquals( "", MimeType.getExtension( "aa" ) );
	}

	@Test
	public void mimeTypeMapping() {
		assertEquals( "application/javascript", MimeType.get( ".JS", "text/plain" ) );
		assertEquals( "text/html", MimeType.get( ".htm", "text/plain" ) );
	}

	@Test
	public void getMimeTypeOfFile() {
		assertEquals( "application/javascript", MimeType.getMimeTypeOfFile( "qq.JS" ) );
		assertEquals( "text/html", MimeType.getMimeTypeOfFile( "a.b.c.htm" ) );
		assertEquals( "text/plain", MimeType.getMimeTypeOfFile( "xxxx" ) );
	}
}
