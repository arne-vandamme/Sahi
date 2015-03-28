package net.sf.sahi.config;

import net.sf.sahi.Proxy;
import net.sf.sahi.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Arne Vandamme
 */
@Component
public class ProxyConfiguration
{
	private int port = Proxy.DEFAULT_PORT;
	private int bufferSize = 8192;

	private String htdocsRoot = "htdocs/";
	private String downloadDirectory = "temp/download";
	private String browserTypesPath = "config/browser_types.xml";
	private String playbackLogRoot = "playback";
	private String logDirectory = "logs";

	private String basePath = ".";
	private String userDataDir = ".";

	public int getPort() {
		return port;
	}

	@Autowired
	public void setPort( @Value("${proxy.port:9999}") int port ) {
		this.port = port;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	@Autowired
	public void setBufferSize( @Value("${io.buffer_size:8192}") int bufferSize ) {
		this.bufferSize = bufferSize;
	}

	public String getLogDirectory() {
		return logDirectory;
	}

	@Autowired
	public void setLogDirectory( @Value("${logs.dir:logs}") String logDirectory ) {
		this.logDirectory = logDirectory;
	}

	public String getBrowserTypesPath() {
		return new File( browserTypesPath ).getAbsolutePath();
	}

	public String getMimeTypesMappingFile() {
		return Utils.concatPaths( basePath, "config/mime-types.mapping" );
	}

	public String getLogsRoot() {
		String fileName = Utils.concatPaths( userDataDir, logDirectory );
		File file = new File( fileName );
		if ( !file.exists() ) {
			file.mkdirs();
		}
		return fileName;
	}

	/*
	public static int getRemoteSocketTimeout() {
		try {
			return Integer.parseInt( getUserProperty( "proxy.remote_socket_timeout" ) );
		}
		catch ( Exception e ) {
			return 120000;
		}
	}
*/
}
