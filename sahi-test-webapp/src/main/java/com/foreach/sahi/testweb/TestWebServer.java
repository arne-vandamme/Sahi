package com.foreach.sahi.testweb;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.resource.Resource;

/**
 * @author Arne Vandamme
 */
public class TestWebServer implements Runnable
{
	public Server server;
	public int port;

	private Thread t;

	public void start() {
		t = new Thread( this );
		t.start();
	}

	public void stop() {
		try {
			server.stop();
			t.join();

			t = null;
		}
		catch ( Exception unimportant ) {

		}
	}

	@Override
	public void run() {
		server = new Server();
		SocketConnector _socketConnector = new SocketConnector();
		_socketConnector.setHost( "127.0.1.1" );
		_socketConnector.setPort( 10000 ); // finds automagically a free port
		server.setConnectors( new Connector[] { _socketConnector } );

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource( Resource.newClassPathResource( "/html/" ) );
		resourceHandler.setWelcomeFiles( new String[] { "index.htm" } );

		server.setHandler( resourceHandler );

		try {
			server.start();
			port = _socketConnector.getLocalPort();
			synchronized ( this ) {
				notifyAll();
			}
			server.join();
		}
		catch ( InterruptedException e ) {
			try {
				server.stop();
			}
			catch ( Exception e1 ) {
				throw new RuntimeException( e1 );
			}
		}
		catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}
}
