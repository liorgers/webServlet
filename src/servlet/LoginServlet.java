package servlet;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import util.User;

@WebServlet(name = "Login", urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(LoginServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String errorMsg = null;
		if(email == null || email.equals("")){
			errorMsg ="User Email can't be null or empty";
		}
		if(password == null || password.equals("")){
			errorMsg = "Password can't be null or empty";
		}

		if(errorMsg != null){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
			PrintWriter out= response.getWriter();
			out.println("<font color=red>"+errorMsg+"</font>");
			rd.include(request, response);
		}else{

			ConnectionSource  connectionSource = (ConnectionSource) getServletContext().getAttribute("DBConnection");

			try {
				Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);
				logger.info("finished creating dao!");
				List<User> requestingUser = userDao.queryForEq("email", email);
				logger.info("finished query for eq!");
				if(requestingUser.isEmpty()){
					logger.error("requestingUser is null!");
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
					PrintWriter out= response.getWriter();
					logger.error("User not found with email="+email);
					out.println("<font color=red>No user found with given email id, please register first.</font>");
					rd.include(request, response);
				}
				else{
					User foundUser = requestingUser.get(0);
					logger.info("User found with details="+foundUser);
					logger.info("validating password...");
					if(!foundUser.getPassword().equals(password)){
						logger.error("invalid password...");
						logger.error("user insert password = " + password + ". db password = " + foundUser.getPassword());
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
						PrintWriter out= response.getWriter();
						out.println("<font color=red>incorrect password</font>");
						rd.include(request, response);
					}
					else{
						logger.info("password is correct! initializing session and setting user in session");
						HttpSession session = request.getSession();
						session.setAttribute("User", foundUser);
						response.sendRedirect("home.jsp");;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("SQLException when creating userDAO!!");
				e.printStackTrace();
				throw new ServletException("DB Connection problem.");
			}



			// ---------- old db logic ---------------

			//			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			//			PreparedStatement ps = null;
			//			ResultSet rs = null;
			//			try {
			//				ps = con.prepareStatement("select id, name, email,country from Users where email=? and password=? limit 1");
			//				ps.setString(1, email);
			//				ps.setString(2, password);
			//				rs = ps.executeQuery();
			//
			//				if(rs != null && rs.next()){
			//
			//					User user = new User(rs.getString("name"), rs.getString("email"), rs.getString("country"), rs.getInt("id"));
			//					logger.info("User found with details="+user);
			//					HttpSession session = request.getSession();
			//					session.setAttribute("User", user);
			//					response.sendRedirect("home.jsp");;
			//				}else{
			//					RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
			//					PrintWriter out= response.getWriter();
			//					logger.error("User not found with email="+email);
			//					out.println("<font color=red>No user found with given email id, please register first.</font>");
			//					rd.include(request, response);
			//				}
			//			} catch (SQLException e) {
			//				e.printStackTrace();
			//				logger.error("Database connection problem");
			//				throw new ServletException("DB Connection problem.");
			//			}finally{
			//				try {
			//					rs.close();
			//					ps.close();
			//				} catch (SQLException e) {
			//					logger.error("SQLException in closing PreparedStatement or ResultSet");;
			//				}
			//
			//			}

			// end old db logic
		}
	}

}