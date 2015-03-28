package net.sf.sahi.config;

import net.sf.sahi.Proxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Arne Vandamme
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestProxyConfiguration.Config.class)
public class TestProxyConfiguration
{
	@Autowired
	private ProxyConfiguration proxyConfiguration;

	@Test
	public void defaultPropertyValues() {
		ProxyConfiguration config = new ProxyConfiguration();
		assertEquals( Proxy.DEFAULT_PORT, config.getPort() );
	}

	@Test
	public void configuredPropertyValues() {
		assertEquals( 8080, proxyConfiguration.getPort() );
	}

	@org.springframework.context.annotation.Configuration
	@PropertySource("classpath:proxy.properties")

	protected static class Config
	{
		@Bean
		public ProxyConfiguration proxyConfiguration() {
			return new ProxyConfiguration();
		}

		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}
	}
}
