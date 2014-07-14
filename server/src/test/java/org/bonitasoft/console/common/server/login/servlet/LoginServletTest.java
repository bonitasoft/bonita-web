package org.bonitasoft.console.common.server.login.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Created by Vincent Elcrin
 * Date: 10/09/13
 * Time: 15:18
 */
public class LoginServletTest {


    @Test
    public void testPasswordIsDroppedWhenParameterIsLast() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm");

        assertThat(cleanQueryString, is("?username=walter.bates"));
    }

    @Test
    public void testPasswordIsDroppedWhenParameterIsBeforeHash() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm#hash");

        assertThat(cleanQueryString, is("?username=walter.bates#hash"));
    }

    @Test
    public void testUrlIsDroppedWhenParameterIsFirstAndBeforeHash() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm#hash");

        assertThat(cleanQueryString, is("?username=walter.bates#hash"));
    }

    @Test
    public void testUrlStayTheSameIfNoPasswordArePresent() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?param=value#dhash");

        assertThat(cleanQueryString, is("?param=value#dhash"));
    }

    @Test
    public void testPasswordIsDroppedEvenIfQueryMarkUpIsntThere() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("password=bpm#dhash1&dhash2");

        assertThat(cleanQueryString, is("#dhash1&dhash2"));
    }

    @Test
    public void testUrlStayEmptyIfParameterIsEmpty() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("");

        assertThat(cleanQueryString, is(""));
    }

    @Test
    public void testDropPasswordOnRealUrl() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanUrl = servlet.dropPassword("?username=walter.bates&password=bpm&redirectUrl=http%3A%2F%2Flocalhost%3A8080%2Fbonita%2Fportal%2Fhomepage%3Fui%3Dform%26locale%3Den%23form%3DPool-\n" +
                "-1.0%24entry%26process%3D8506394779365952706%26mode%3Dapp");

        assertThat(cleanUrl, is("?username=walter.bates&redirectUrl=http%3A%2F%2Flocalhost%3A8080%2Fbonita%2Fportal%2Fhomepage%3Fui%3Dform%26locale%3Den%23form%3DPool-\n" +
                "-1.0%24entry%26process%3D8506394779365952706%26mode%3Dapp"));
    }

}
