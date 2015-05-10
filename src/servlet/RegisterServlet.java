package servlet;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import util.User;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

@WebServlet(name = "Register", urlPatterns = { "/Register" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RegisterServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String country = request.getParameter("country");
		String errorMsg = null;
		if(email == null || email.equals("")){
			errorMsg = "Email ID can't be null or empty.";
		}
		if(password == null || password.equals("")){
			errorMsg = "Password can't be null or empty.";
		}
		if(name == null || name.equals("")){
			errorMsg = "Name can't be null or empty.";
		}
		if(country == null || country.equals("")){
			errorMsg = "Country can't be null or empty.";
		}

		if(errorMsg != null){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.html");
			PrintWriter out= response.getWriter();
			out.println("<font color=red>"+errorMsg+"</font>");
			rd.include(request, response);
		}else{
			logger.info("User entered the folowing data: email = "+email+", password: "+password+", name: "+name+", country: "+country);
			ConnectionSource  connectionSource = (ConnectionSource) getServletContext().getAttribute("DBConnection");

			if(connectionSource == null){
				logger.info("connectionSource is null!!");
			}


			try {
				Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);
				logger.info("finished creating dao!");
				User newUser = new User();
				newUser.setEmail(email);
				newUser.setPassword(password);
				newUser.setName(name);
				newUser.setCountry(country);

				userDao.create(newUser);
				logger.info("finished creating new user");

				//forward to login page to login
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
				PrintWriter out= response.getWriter();
				out.println("<font color=green>Registration successful, please login below.</font>");
				rd.include(request, response);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("SQLException in dao try block");
				e.printStackTrace();
				throw new ServletException("DB Connection problem.");
			}

			// -------- old DB logic
			//			PreparedStatement ps = null;
			//			try {
			//				ps = con.prepareStatement("insert into Users(name,email,country, password) values (?,?,?,?)");
			//				logger.info("finished prepering the sql");
			//				ps.setString(1, name);
			//				ps.setString(2, email);
			//				ps.setString(3, country);
			//				ps.setString(4, password);
			//
			//				ps.execute();
			//				logger.info("ps executed successfully!");
			//
			//
			//				//forward to login page to login
			//				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
			//				PrintWriter out= response.getWriter();
			//				out.println("<font color=green>Registration successful, please login below.</font>");
			//				rd.include(request, response);
			//			} catch (SQLException e) {
			//				e.printStackTrace();
			//				logger.error("Database connection problem");
			//				throw new ServletException("DB Connection problem.");
			//			}finally{
			//				try {
			//					logger.info("trying to close ps...");
			//					ps.close();
			//					logger.info("ps closed successfully!!!");
			//				} catch (SQLException e) {
			//					logger.error("SQLException in closing PreparedStatement");
			//				}
			//			}
		}

	}

}