package com.sugarcrm.candybean.server.configuration;

import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class ConfigurationServerDriver {

	public static Logger logger = Logger.getLogger(ConfigurationServerDriver.class.getName());
	
	public static void main (String args[]) throws Exception{
		logger.info("Starting JETTY server");
	    Server server = new Server(8080);
	    
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "resources/html/configure.html" });
        resourceHandler.setResourceBase(".");
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        resourceHandler.setWelcomeFiles(new String[]{ "resources/html/configure.html" });
        resourceHandler.setResourceBase(".");
        context.addServlet(new ServletHolder(new ConfigurationServlet()),"/cfg");
        context.addServlet(new ServletHolder(new SaveConfigurationServlet()),"/cfg/save");
        context.addServlet(new ServletHolder(new LoadConfigurationServlet()),"/cfg/load");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, context });
        server.setHandler(handlers);
 
        server.start();
		logger.info("Configuration server started at: http://localhost:8080/cfg");
        server.join();
	}

}
