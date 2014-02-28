package org.bonitasoft.console.common.server.login.servlet;

import junit.framework.Assert;
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

        Assert.assertEquals(cleanQueryString, "?username=walter.bates");
    }

    @Test
    public void testPasswordIsDroppedWhenParameterIsBeforeHash() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm#hash");

        Assert.assertEquals(cleanQueryString, "?username=walter.bates#hash");
    }

    @Test
    public void testUrlIsDroppedWhenParameterIsFirstAndBeforeHash() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm#hash");

        Assert.assertEquals(cleanQueryString, "?username=walter.bates#hash");
    }

    @Test
    public void testUrlStayTheSameIfNoPasswordArePresent() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("?param=value#dhash");

        Assert.assertEquals(cleanQueryString, "?param=value#dhash");
    }

    @Test
    public void testPasswordIsDroppedEvenIfQueryMarkUpIsntThere() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("password=bpm#dhash1&dhash2");

        Assert.assertEquals(cleanQueryString, "#dhash1&dhash2");
    }

    @Test
    public void testUrlStayEmptyIfParameterIsEmpty() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanQueryString = servlet.dropPassword("");

        Assert.assertEquals(cleanQueryString, "");
    }

    @Test
    public void testDropPasswordOnRealUrl() throws Exception {
        LoginServlet servlet = new LoginServlet();

        String cleanUrl = servlet.dropPassword("?username=walter.bates&password=bpm&redirectUrl=http%3A%2F%2Flocalhost%3A8080%2Fbonita%2Fportal%2Fhomepage%3Fui%3Dform%26locale%3Den%23form%3DPool-\n" +
                "-1.0%24entry%26process%3D8506394779365952706%26mode%3Dapp");

        Assert.assertEquals(cleanUrl, "?username=walter.bates&redirectUrl=http%3A%2F%2Flocalhost%3A8080%2Fbonita%2Fportal%2Fhomepage%3Fui%3Dform%26locale%3Den%23form%3DPool-\n" +
                "-1.0%24entry%26process%3D8506394779365952706%26mode%3Dapp");
    }

}
