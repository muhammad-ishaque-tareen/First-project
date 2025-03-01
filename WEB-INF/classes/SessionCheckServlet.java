import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SessionCheckServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("loggedIn") != null) {
            // Manually create JSON string
            String username = (String) session.getAttribute("username");
            String fullName = (String) session.getAttribute("fullName");
            
            // Escape special characters in the strings
            username = username.replace("\"", "\\\"");
            fullName = fullName.replace("\"", "\\\"");
            
            String jsonResponse = String.format(
                "{\"loggedIn\": true, \"username\": \"%s\", \"fullName\": \"%s\"}",
                username, fullName
            );
            out.println(jsonResponse);
        } else {
            out.println("{\"loggedIn\": false}");
        }
    }
}