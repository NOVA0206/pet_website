import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookAppointmentServlet")
public class BookAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pet_tracker?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jeevan@0206";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Retrieve form parameters
        String petName = request.getParameter("pet-name");
        String petType = request.getParameter("pet-type");
        String ownerName = request.getParameter("owner-name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String reason = request.getParameter("reason");

        // Validate inputs
        if (petName == null || petName.trim().isEmpty() || 
            ownerName == null || ownerName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() || 
            phone == null || phone.trim().isEmpty() ||
            date == null || date.trim().isEmpty() || 
            time == null || time.trim().isEmpty() || 
            reason == null || reason.trim().isEmpty()) {
            
            response.getWriter().println("All fields are required.");
            return;
        }

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO appointments (pet_name, pet_type, owner_name, email, phone, preferred_date, preferred_time, reason) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    // Set values to the prepared statement
                    stmt.setString(1, petName);
                    stmt.setString(2, petType);
                    stmt.setString(3, ownerName);
                    stmt.setString(4, email);
                    stmt.setString(5, phone);
                    stmt.setString(6, date);
                    stmt.setString(7, time);
                    stmt.setString(8, reason);

                    // Execute the update
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        // Appointment booked successfully
                        response.sendRedirect("dashboard.html?message=Appointment booked successfully!");
                    } else {
                        // If insertion failed
                        response.sendRedirect("appointment.html?message=Failed to book appointment.");
                    }
                } catch (SQLException e) {
                    // Handle SQL execution errors
                    response.getWriter().println("Error during SQL execution: " + e.getMessage());
                }
            } catch (SQLException e) {
                // Handle database connection errors
                response.getWriter().println("Database connection error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            // Handle MySQL driver loading errors
            response.getWriter().println("Driver loading failed: " + e.getMessage());
        }
    }
}
