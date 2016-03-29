package servlets;

//import account.server.AccountService;
import db.models.User;
import db.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomePageServlet extends HttpServlet {
    static final Logger LOGGER = LogManager.getLogger(HomePageServlet.class.getName());
    public static final String PAGE_URL = "/home";
    private final AccountService accountServer;

    public HomePageServlet(AccountService accountServer) {
        this.accountServer = accountServer;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        final String remove = request.getParameter("remove");

        if (remove != null) {
            accountServer.removeUser("11");
            response.getWriter().println("Hasta la vista!");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        final int limit = accountServer.getUsersLimit();
        final int count = accountServer.getUsersCount();

        LOGGER.info("Limit: {}. Count {}", limit, count);
        if (limit > count) {
            LOGGER.info("User pass");
            accountServer.addUser(new User("name", "pass"));
            response.getWriter().println("Hello, world!");
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            LOGGER.info("User were rejected");
            response.getWriter().println("Server is closed for maintenance!");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
