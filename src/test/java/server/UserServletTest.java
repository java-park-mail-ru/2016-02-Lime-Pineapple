package server;

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
import javax.ws.rs.core.Context;

import static org.mockito.Mockito.*;


public class UserServletTest {
    private AccountService accountServer = mock(AccountService.class);
    private HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    private HttpServletRequest getMockedRequest(String url) {
        final HttpSession httpSession = mock(HttpSession.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getPathInfo()).thenReturn(url);
        return request;
    }

    private HttpHeaders getMockedHeaders() {
        final HttpHeaders httpHeaders = mock(HttpHeaders.class);

        return httpHeaders;
    }


    @Test
    public void testServletAdd() throws IOException, ValidationException {
        final StringWriter stringWriter = new StringWriter();

        final HttpServletResponse response = getMockedResponse(stringWriter);
        final HttpServletRequest request = getMockedRequest("/usfinal er");
        final UserServlet userServlet = new UserServlet(accountServer);
        final User myuser = new User("lalka", "12345");
        final UserServlet spy = spy(userServlet);
        //userServlet.createUser(myuser);
        userServlet.createUser(myuser, getMockedHeaders());
        verify(accountServer, times(1)).addUser(myuser);
    }

    @Test
    public void testServletUpdate() throws IOException, ValidationException {
    final StringWriter stringWriter = new StringWriter();
        final HttpServletResponse response = getMockedResponse(stringWriter);
        final HttpServletRequest request = getMockedRequest("/usfinal er");
        final UserServlet userServlet = new UserServlet(accountServer);
        final User myuser = new User("lalka", "12345");
        final UserServlet spy = spy(userServlet);
        //userServlet.createUser(myuser);
        myuser.setNickname("nick");
        userServlet.updateUser(1L, myuser);
        verify(accountServer, times(1)).changeUser(myuser);
    }
    @Test
    public void testServerRemove() throws IOException, ValidationException {
        final StringWriter stringWriter = new StringWriter();
        final HttpServletResponse response = getMockedResponse(stringWriter);
        final HttpServletRequest request = getMockedRequest("/usfinal er");
        final UserServlet userServlet = new UserServlet(accountServer);
        final User myuser = new User("lalka", "12345");
        final UserServlet spy = spy(userServlet);
        //userServlet.createUser(myuser);
        userServlet.removeUser(1L);
        verify(accountServer, times(1)).removeUser(1L);
    }
    @Test
    public void testServletGetAll() throws IOException, ValidationException {
        final StringWriter stringWriter = new StringWriter();
        final HttpServletResponse response = getMockedResponse(stringWriter);
        final HttpServletRequest request = getMockedRequest("/usfinal er");
        final UserServlet userServlet = new UserServlet(accountServer);
        final User myuser = new User("user_login", "12345");
        final UserServlet spy = spy(userServlet);
        for (Integer i = 0; i < 10; i++) {
            myuser.setLogin("user_login"+i.toString());
            myuser.setNickname("NickUser"+i.toString());
            //userServlet.createUser(myuser);
        }
        verify(accountServer, times(1)).getUsers();
    }
    @Test
    public void testScoreTable() throws IOException, ValidationException {
        final StringWriter stringWriter = new StringWriter();
        final HttpServletResponse response = getMockedResponse(stringWriter);
        final HttpServletRequest request = getMockedRequest("/usfinal er");
        final UserServlet userServlet = new UserServlet(accountServer);
        final User myuser = new User("user_login", "12345");
        final UserServlet spy = spy(userServlet);
        for (Integer i = 0; i < 10; i++) {
            myuser.setLogin("user_login"+i.toString());
            myuser.setNickname("NickUser"+i.toString());
            myuser.increaseScore(10*i);
            //userServlet.createUser(myuser);
        }
        userServlet.showScoreTable();
        verify(accountServer, times(1)).getUserScores();
    }


}
