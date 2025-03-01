import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PasswordUpdateServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/weather_app";
    private static final String DB_USER = "Tareen";
    private static final String DB_PASSWORD = "Afn@n1234";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get session and check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            out.println("<script>");
            out.println("alert('Not logged in');");
            out.println("window.location.href = 'login.html';");
            out.println("</script>");
            return;
        }

        String username = (String) session.getAttribute("username");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        if (currentPassword == null || newPassword == null || confirmPassword == null) {
            out.println("<script>");
            out.println("alert('All fields are required');");
            out.println("window.location.href = 'dash.html';");
            out.println("</script>");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            out.println("<script>");
            out.println("alert('New passwords do not match');");
            out.println("window.location.href = 'dash.html';");
            out.println("</script>");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtCheck = null;
        PreparedStatement pstmtUpdate = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // First, verify current password
            String checkSql = "SELECT * FROM users WHERE user_name = ? AND password = ?";
            pstmtCheck = conn.prepareStatement(checkSql);
            pstmtCheck.setString(1, username);
            pstmtCheck.setString(2, currentPassword);
            rs = pstmtCheck.executeQuery();

            if (!rs.next()) {
                out.println("<script>");
                out.println("alert('Current password is incorrect');");
                out.println("window.location.href = 'dash.html';");
                out.println("</script>");
                return;
            }

            // Update password
            String updateSql = "UPDATE users SET password = ? WHERE user_name = ?";
            pstmtUpdate = conn.prepareStatement(updateSql);
            pstmtUpdate.setString(1, newPassword);
            pstmtUpdate.setString(2, username);
            
            int rowsUpdated = pstmtUpdate.executeUpdate();
            
            if (rowsUpdated > 0) {
                out.println("<script>");
                out.println("alert('Password updated successfully');");
                out.println("window.location.href = 'dash.html';");
                out.println("</script>");
            } else {
                out.println("<script>");
                out.println("alert('Password update failed');");
                out.println("window.location.href = 'dash.html';");
                out.println("</script>");
            }

        } catch (Exception e) {
            out.println("<script>");
            out.println("alert('Error: " + e.getMessage().replace("\"", "\\\"") + "');");
            out.println("window.location.href = 'dash.html';");
            out.println("</script>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtCheck != null) pstmtCheck.close();
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}