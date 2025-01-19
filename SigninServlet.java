import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signin")
public class SigninServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://35.244.46.149:3306/pet_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jeevan@0206";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (PrintWriter out = response.getWriter()) {
            // Load MySQL driver (optional with modern JDBC but included for compatibility)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                out.println("<h3 style='color:red;'>Driver loading failed: " + e.getMessage() + "</h3>");
                return;
            }

            // Check if credentials exist in the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // User found, redirect to dashboard
                        response.sendRedirect("dashboard.html");
                    } else {
                        // Invalid credentials, display error message as a pop-up
                        String errorMessage = "Invalid username or password!";
                        out.println("<html><body>");
                        out.println("<script type='text/javascript'>");
                        out.println("alert('" + errorMessage.replace("'", "\\'") + "');");
                        out.println("window.location.href = 'singin.html';"); // Redirect to signin page
                        out.println("</script>");
                        out.println("</body></html>");
                    }
                }
            } catch (SQLException e) {
                // Displaying the error message as a pop-up alert
                String errorMessage = "Database error: " + e.getMessage();
                out.println("<html><body>");
                out.println("<script type='text/javascript'>");
                out.println("alert('" + errorMessage.replace("'", "\\'") + "');");
                out.println("window.location.href = 'singin.html';"); // Redirect to signin page
                out.println("</script>");
                out.println("</body></html>");
            }
        }
    }
}
