package net.sf.sahi.config;

import net.sf.sahi.Proxy;
import net.sf.sahi.util.BrowserTypeRegistry;
import net.sf.sahi.util.BrowserTypeRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.File;

/**
 * @author Arne Vandamme
 */
@org.springframework.context.annotation.Configuration
@PropertySource(value = "${sahi.baseDir:.}/sahi.properties", ignoreResourceNotFound = true)
public class ApplicationConfiguration
{
	@Autowired
	private Environment environment;

	@Bean
	public ProxyConfiguration proxyConfiguration() {
		ProxyConfiguration proxyConfiguration = new ProxyConfiguration();

		return proxyConfiguration;
	}

	@Bean
	public BrowserTypeRegistry browserTypeRegistry( ProxyConfiguration proxyConfiguration ) {
		return new BrowserTypeRegistryBuilder( new File( proxyConfiguration.getBrowserTypesPath() ) ).build();
	}

	@Bean
	public Proxy proxy() {
		int proxyPort = environment.getProperty( "proxy.port", Integer.class, Proxy.DEFAULT_PORT );

		return new Proxy( proxyPort, true );
	}
	/*
	@Bean
	public ProxyConfiguration proxyConfiguration() {
		return new ProxyConfiguration();
	}
	*/
}
