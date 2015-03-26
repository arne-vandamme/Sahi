package net.sf.sahi.client;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.UUID;

/**
 * @author Arne Vandamme
 */
public class ClientUtils
{
	private static final int BUFFER_SIZE = 8192;

	private ClientUtils() {
	}

	public static String generateId() {
		//		return "SAHI_HARDCODED_ID";
		return "sahi_" + getUUID();
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace( '-', '0' );
	}

	public static String toJSON( String[] list ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "[" );
		for ( int i = 0; i < list.length; i++ ) {
			sb.append( "\"" );
			sb.append( makeString( list[i] ) );
			sb.append( "\"" );
			if ( i != list.length - 1 ) {
				sb.append( "," );
			}
		}
		sb.append( "]" );
		return sb.toString();
	}

	public static String makeString( String s ) {
		if ( s == null ) {
			return null;
		}
		return escapeDoubleQuotesAndBackSlashes( s ).replaceAll( "\n", "\\\\n" ).replaceAll( "\r", "" );

	}

	public static String escapeDoubleQuotesAndBackSlashes( final String line ) {
		if ( line == null ) {
			return null;
		}
		return line.replaceAll( "\\\\", "\\\\\\\\" ).replaceAll( "\"", "\\\\\"" );
	}

	public static byte[] readURL( final String url ) {
		return readURL( url, true );
	}

	public static byte[] readURLThrowException( final String url ) throws MalformedURLException, IOException {
		byte[] data = null;
		InputStream inputStream = null;
		try {
			inputStream = new URL( url ).openStream();
			data = getBytes( inputStream, -1 );
			inputStream.close();
		}
		finally {
			inputStream.close();
		}
		return data;
	}

	public static byte[] readURL( final String url, boolean printExceptions ) {
		byte[] data = null;
		InputStream inputStream = null;
		try {
			inputStream = new URL( url ).openStream();
			data = getBytes( inputStream, -1 );
			inputStream.close();
		}
		catch ( Exception e ) {
			if ( printExceptions ) {
				e.printStackTrace();
			}
		}
		finally {
			try {
				inputStream.close();
			}
			catch ( Exception e ) {
				if ( printExceptions ) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public static byte[] getBytes( String dataStr ) {
		try {
			return dataStr.getBytes( "UTF-8" );
		}
		catch ( UnsupportedEncodingException e ) {
			return dataStr.getBytes();
		}
	}


	public static byte[] getBytes( final InputStream in, final int contentLength ) throws IOException {
		BufferedInputStream bin = new BufferedInputStream( in, BUFFER_SIZE );

		if ( contentLength != -1 ) {
			int totalBytesRead = 0;
			byte[] buffer = new byte[contentLength];
			while ( totalBytesRead < contentLength ) {
				int bytesRead = -1;
				try {
					bytesRead = bin.read( buffer, totalBytesRead, contentLength - totalBytesRead );
				}
				catch ( EOFException e ) {
				}
				if ( bytesRead == -1 ) {
					break;
				}
				totalBytesRead += bytesRead;
			}
			return buffer;
		}
		else {
			ByteArrayOutputStream byteArOut = new ByteArrayOutputStream();
			BufferedOutputStream bout = new BufferedOutputStream( byteArOut );
			try {
				int totalBytesRead = 0;
				byte[] buffer = new byte[BUFFER_SIZE];

				while ( true ) {
					int bytesRead = -1;
					try {
						bytesRead = bin.read( buffer );
					}
					catch ( EOFException e ) {
					}
					if ( bytesRead == -1 ) {
						break;
					}
					bout.write( buffer, 0, bytesRead );
					totalBytesRead += bytesRead;
				}
			}
			catch ( SocketTimeoutException ste ) {
				ste.printStackTrace();
			}
			bout.flush();
			bout.close();
			return byteArOut.toByteArray();
		}
	}
}
