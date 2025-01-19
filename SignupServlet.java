import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        try (PrintWriter out = response.getWriter()) {
            // Validate passwords
            if (!password.equals(confirmPassword)) {
                out.println("<h3 style='color:red;'>Passwords do not match!</h3>");
                return;
            }

            // Load MySQL driver (optional with modern JDBC but included for compatibility)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                out.println("<h3 style='color:red;'>Driver loading failed: " + e.getMessage() + "</h3>");
                return;
            }

            // Insert data into the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, password);

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        // Redirect to dashboard with success message
                        response.sendRedirect("singin.html?message=Registration successful!");
                    } else {
                        out.println("<h3 style='color:red;'>Error in registration. Please try again.</h3>");
                    }
                }
            } catch (SQLException e) {
                // Displaying the error message as a pop-up alert
                String errorMessage = "Database error: " + e.getMessage();
                out.println("<html><body>");
                out.println("<script type='text/javascript'>");
                out.println("alert('" + errorMessage.replace("'", "\\'") + "');");
                out.println("window.location.href = 'singup.html';"); // Redirect to signup page
                out.println("</script>");
                out.println("</body></html>");
            }
        }
    }
}
