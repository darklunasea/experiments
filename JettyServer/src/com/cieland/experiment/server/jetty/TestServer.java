package com.cieland.experiment.server.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class TestServer {

	public static void main(String[] args)
	{
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.addConnector(connector);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);		
		context.setContextPath("/");
		server.setHandler(context);
		
		ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
		context.addServlet(holderEvents, "/*");
		
		try{
			server.start();
			server.dump(System.err);
			server.join();
		}
		catch(Throwable t){
			t.printStackTrace(System.err);
		}
	}
	
}
