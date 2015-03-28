package net.sf.sahi.util;

import net.sf.sahi.request.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arne Vandamme
 */
public class BrowserTypeRegistry
{
	private final Map<String, BrowserType> browserTypes = new HashMap<>();

	public void addBrowserType( final BrowserType browserType ) {
		browserTypes.put( browserType.name(), browserType );
	}

	public BrowserType getBrowserType( final HttpRequest request ) {
		String browserTypeParam = request.getParameter( "browserType" );
		return getBrowserType( browserTypeParam );
	}

	public BrowserType getBrowserType( String name ) {
		if ( StringUtils.isBlank( name ) ) {
			return null;
		}
		return browserTypes.get( name );
	}

	public Collection<BrowserType> values() {
		return browserTypes.values();
	}
}
