package server;

import db.models.User;
import db.services.AccountService;
import org.junit.Test;
import server.rest.UserServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;


public class UserServletTest {
    private AccountService accountServer = mock(AccountService.class);

    private HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        final PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        return response;
    }

    private HttpServletRequest getMockedRequest(String url) {
        HttpSession httpSession = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getSession()).thenReturn(httpSession);
        when(request.getPathInfo()).thenReturn(url);

        return request;
    }

    @Test
    public void testServletAdd() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest("/user");

        UserServlet userServlet = new UserServlet(accountServer);
        UserServlet spy = spy(userServlet);
        User myuser = new User("lalka", "12345");
        userServlet.createUser(myuser);

        verify(accountServer, times(1)).addUser(myuser);
    }
}
