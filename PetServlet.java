import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PetServlet")
public class PetServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pet_tracker?zeroDateTimeBehavior=convertToNull";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "Jeevan@0206";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String petName = request.getParameter("petName");
        String ownerName = request.getParameter("ownerName");
        String petType = request.getParameter("petType");
        String age = request.getParameter("age");
        String healthIssue = request.getParameter("healthIssue");

        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            // Insert query
            String sql = "INSERT INTO pets (pet_name, owner_name, pet_type, age, health_issue) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, petName);
            statement.setString(2, ownerName);
            statement.setString(3, petType);
            statement.setInt(4, Integer.parseInt(age));
            statement.setString(5, healthIssue);

            // Execute update
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Redirect to dashboard with a success message
                response.sendRedirect("dashboard.html?message=Pet added successfully!");
            } else {
                // Redirect to dashboard with a failure message
                response.sendRedirect("dashboard.html?message=Failed to add pet.");
            }

            // Close resources
            statement.close();
            connection.close();
        } catch (Exception e) {
            response.getWriter().println("Database Error: " + e.getMessage());
        }
    }
}
