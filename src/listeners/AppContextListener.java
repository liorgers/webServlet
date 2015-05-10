package listeners;

import java.io.File;
//import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Logger; 

//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

//import servlet.RegisterServlet;
//import util.DBConnectionManager;
//import util.User;

@WebListener
public class AppContextListener implements ServletContextListener {

	static Logger logger = Logger.getLogger(AppContextListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();

		//initialize log4j
		String log4jConfig = ctx.getInitParameter("log4j-config");
		if(log4jConfig == null){
			System.err.println("No log4j-config init param, initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		}else {
			String webAppPath = ctx.getRealPath("/");
			String log4jProp = webAppPath + log4jConfig;
			File log4jConfigFile = new File(log4jProp);
			if (log4jConfigFile.exists()) {
				System.out.println("Initializing log4j with: " + log4jProp);
				DOMConfigurator.configure(log4jProp);
			} else {
				System.err.println(log4jProp + " file not found, initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		System.out.println("log4j configured properly");

		//initialize DB Connection
		String dbURL = ctx.getInitParameter("dbURL");
		String user = ctx.getInitParameter("dbUser");
		String pwd = ctx.getInitParameter("dbPassword");

		logger.info("dburl = "+dbURL+", dbuser = "+user+", dbpassword = "+pwd);

		try {
			logger.info("trying to create connection source...");
			// new code
			ConnectionSource connectionSource = new JdbcConnectionSource(dbURL, user, pwd);

			// end new code

			//old DB connections
			//        DBConnectionManager connectionManager = new DBConnectionManager(dbURL, user, pwd);
			ctx.setAttribute("DBConnection", connectionSource);
			if(ctx.getAttribute("DBConnection") == null){
				logger.info("DBConnection in ctx is null!!");
			}
			logger.info("finished to set attribute to ctx");
			System.out.println("DB Connection initialized successfully.");

		} catch (SQLException e) {
			logger.info("inside sql exception");
			e.printStackTrace();
		}

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ConnectionSource connectionSource = (ConnectionSource) servletContextEvent.getServletContext().getAttribute("DBConnection");
		try {
			connectionSource.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}