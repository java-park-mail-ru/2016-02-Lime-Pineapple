package server;

import db.models.User;
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
    public void testServletAdd() throws Exception {
        final StringWriter stringWriter = new StringWriter();

        final HttpServletResponse response = getMockedResponse(stringWriter);
        final HttpServletRequest request = getMockedRequest("/usfinal er");
        final UserServlet userServlet = new UserServlet(accountServer);
        final User myuser = new User("lalka", "12345");
        final UserServlet spy = spy(userServlet);
        userServlet.createUser(myuser, getMockedHeaders());

        verify(accountServer, times(1)).addUser(myuser);
    }
}
