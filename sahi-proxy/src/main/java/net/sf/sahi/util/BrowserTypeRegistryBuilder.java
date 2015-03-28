package net.sf.sahi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Arne Vandamme
 */
public class BrowserTypeRegistryBuilder
{
	private static final Logger LOG = LoggerFactory.getLogger( BrowserTypeRegistry.class );

	private File browserTypesXmlFile;

	private BrowserTypeRegistry registry;

	public BrowserTypeRegistryBuilder( File browserTypesXmlFile ) {
		this.browserTypesXmlFile = browserTypesXmlFile;
	}

	public synchronized BrowserTypeRegistry build() {
		registry = new BrowserTypeRegistry();

		DocumentBuilder parser;
		try {
			LOG.info( "Reading browser types from {}", browserTypesXmlFile );

			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = parser.parse( browserTypesXmlFile );
			final Element root = document.getDocumentElement();
			final NodeList childNodes = root.getElementsByTagName( "browserType" );
			for ( int i = 0; i < childNodes.getLength(); i++ ) {
				Element el = (Element) childNodes.item( i );
				BrowserType browserType = new BrowserType( el );

				registry.addBrowserType( browserType );
			}
		}
		catch ( SAXException | IOException | ParserConfigurationException e ) {
			LOG.error( e.getMessage(), e );
		}

		verifyAvailableBrowserTypes();

		return registry;
	}

	private void verifyAvailableBrowserTypes() {
		for ( BrowserType browserType : registry.values() ) {
			final String expanded = Utils.expandSystemProperties( browserType.path(), true );
			if ( !new File( expanded ).exists() ) {
				LOG.warn( "{} was not fount at {}", browserType.displayName(), expanded );
			}
		}
	}
}
