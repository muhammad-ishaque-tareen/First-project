
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/weather_app";
    private static final String DB_USER = "Tareen";
    private static final String DB_PASSWORD = "Afn@n1234";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Updated query to fetch full_name as well
            String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                // Store more user information in session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("fullName", rs.getString("full_name"));
                session.setAttribute("loggedIn", true);

                // Success alert and redirection
                out.println("<script>");
                out.println("alert('Login successful! Welcome, " + rs.getString("full_name") + ".');");
                out.println("window.location.href = 'dash.html';");
                out.println("</script>");
            } else {
                out.println("<script>");
                out.println("alert('Invalid username or password. Please try again.');");
                out.println("window.location.href = 'login.html';");
                out.println("</script>");
            }
        } catch (Exception e) {
            out.println("<script>");
            out.println("alert('An error occurred: " + e.getMessage() + "');");
            out.println("window.location.href = 'login.html';");
            out.println("</script>");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
