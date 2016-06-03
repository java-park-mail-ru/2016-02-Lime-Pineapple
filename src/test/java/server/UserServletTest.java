package server;

import db.exceptions.DatabaseException;
import db.models.User;
import db.models.validation.ValidationException;
import db.services.AccountService;
import org.junit.Test;
import server.rest.UserServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;


public class UserServletTest {
    private AccountService accountService = mock(AccountService.class);
    HttpHeaders httpHeaders;
    StringWriter stringWriter;
    HttpServletResponse response;
    HttpServletRequest request;
    UserServlet userServlet;
    User myuser;
    UserServlet spy;

    @SuppressWarnings("ParameterHidesMemberVariable")
    private HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    private HttpServletRequest getMockedRequest(String url) {
        final HttpSession httpSession = mock(HttpSession.class);
        final HttpServletRequest m = mock(HttpServletRequest.class);
        when(m.getSession()).thenReturn(httpSession);
        when(m.getPathInfo()).thenReturn(url);
        return m;
    }

    private void configureTest() {
        this.httpHeaders = mock(HttpHeaders.class);
        this.stringWriter = new StringWriter();
        try {
            this.response = getMockedResponse(stringWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.request = getMockedRequest("/usfinal er");
        this.userServlet = new UserServlet(accountService);
        this.myuser = new User("user_login", "12345");
        this.spy = spy(userServlet);
    }


    @Test
    public void testServletAdd() throws IOException, ValidationException {
        configureTest();
        try {
            spy.createUser(myuser, httpHeaders);
            verify(accountService, times(1)).addUser(myuser);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testServletUpdate() throws IOException, ValidationException {
        configureTest();
        try {
            userServlet.updateUser(1L, myuser);
            verify(accountService, times(1)).changeUser(myuser);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testServerRemove() throws IOException, ValidationException {
        configureTest();
        try {
            spy.removeUser(1L);
            verify(accountService, times(1)).removeUser(1L);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testServletGetAll() throws IOException, ValidationException {
        configureTest();
        try {
            spy.getAllUsers();
            verify(accountService, times(1)).getUsers();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testScoreTable() throws IOException, ValidationException {
        configureTest();
        try {
            spy.showScoreTable();
            verify(accountService, times(1)).getScores();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


}
