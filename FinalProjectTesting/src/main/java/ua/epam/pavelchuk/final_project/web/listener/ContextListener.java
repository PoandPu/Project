package ua.epam.pavelchuk.final_project.web.listener;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Context listener. Initialize log4j.
 * @author 
 *
 */
public class ContextListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(ContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log("Servlet context destruction");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log("Servlet context initialization starts");
		
		ServletContext servletContext = event.getServletContext();
		initLog4J(servletContext);
	
		log("Servlet context initialization finished");
	}

	/**
	 * Initializes log4j framework.
	 * 
	 * @param servletContext
	 */
	private void initLog4J(ServletContext servletContext) {
		log("Log4J initialization started");
		try {
			PropertyConfigurator.configure(
				servletContext.getRealPath("WEB-INF/log4j.properties"));
			LOG.debug("Log4j has been initialized");
		} catch (Exception ex) {
			log("Cannot configure Log4j");
			ex.printStackTrace();
		}		
		log("Log4J initialization finished");
	}
	
	private void log(String msg) {
		System.out.println("[ContextListener] " + msg);
	}
}
