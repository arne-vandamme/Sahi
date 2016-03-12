package net.sf.sahi;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class HttpHeaders
{
	private final Map<String, List<String>> headers = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );

	public void addHeader( String key, String value ) {
		getOrCreate( key ).add( value );
	}

	public void setHeader( String key, String value ) {
		List<String> entry = new ArrayList<>();
		entry.add( value );

		headers.put( key, entry );
	}

	public boolean hasHeader( String key ) {
		return headers.containsKey( key );
	}

	public Collection<String> keys() {
		return headers.keySet();
	}

	public String getHeader( String key ) {
		return StringUtils.join( headers.get( key ), "," );
	}

	public List<String> getHeaders( String key ) {
		return headers.get( key );
	}

	public void addHeaders( String key, List<String> newHeaders ) {
		if ( newHeaders != null ) {
			getOrCreate( key ).addAll( newHeaders );
		}
	}

	private List<String> getOrCreate( String key ) {
		List<String> entry = headers.get( key );

		if ( entry == null ) {
			entry = new ArrayList<>();
			headers.put( key, entry );
		}

		return entry;
	}

	public String getLastHeader( String key ) {
		List<String> entry = headers.get( key );

		if ( entry == null ) {
			return null;
		}
		return entry.get( entry.size() - 1 );
	}

	public String toString() {
		StringBuilder output = new StringBuilder();

		for ( Map.Entry<String, List<String>> entry : headers.entrySet() ) {
			if ( entry.getKey() != null ) {
				for ( String headerValue : entry.getValue() ) {
					output.append( entry.getKey() )
					      .append( ": " )
					      .append( headerValue )
					      .append( "\r\n");
				}
			}
		}

		return output.toString();
	}

	public void removeHeader( String key ) {
		headers.remove( key );
	}

	public boolean isEmpty() {
		return headers.isEmpty();
	}

	public int size() {
		return headers.size();
	}
}
