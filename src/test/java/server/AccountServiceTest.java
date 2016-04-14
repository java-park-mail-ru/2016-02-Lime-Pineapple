package server;

import db.models.User;
import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import org.junit.Test;
import server.rest.UserServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;


public class AccountServiceTest {

    private AccountService testedService = new ExampleAccountService();
    @Test
    void databaseConnectionTest() {

    }
}
