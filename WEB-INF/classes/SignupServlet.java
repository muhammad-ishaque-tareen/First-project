import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String dbURL = "jdbc:mysql://localhost:3306/weather_app";
        String dbUser = "Tareen"; 
        String dbPassword = "Afn@n1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            String sql = "INSERT INTO Users (full_name, user_name, password) VALUES (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, password);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<script>");
                out.println("alert('Signup successful!');");
                out.println("window.location.href = 'login.html';");
                out.println("</script>");
            } else {
                out.println("<p>Signup failed. Please try again.</p>");
            }
            

            statement.close();
            connection.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
