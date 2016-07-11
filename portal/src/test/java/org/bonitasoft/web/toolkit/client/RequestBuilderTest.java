package org.bonitasoft.web.toolkit.client;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class RequestBuilderTest {

    @Test
    public void send_should_add_header_if_api_token_is_in_session() throws Exception {
        final RequestBuilder requestBuilder = spy(new RequestBuilder(RequestBuilder.GET, "url"));

        doReturn("token").when(requestBuilder).getAPIToken();
        try {
            requestBuilder.send();
        } catch (final Exception e) {
            //RequestBuilder#send generates exception when not executed in a browser
        } finally {
            verify(requestBuilder, times(1)).getAPIToken();
            verify(requestBuilder, times(1)).setHeader("X-Bonita-API-Token", "token");
        }
    }

    @Test
    public void send_should_not_add_header_if_api_token_is_not_in_session() throws Exception {
        final RequestBuilder requestBuilder = spy(new RequestBuilder(RequestBuilder.GET, "url"));

        doReturn(null).when(requestBuilder).getAPIToken();
        try {
            requestBuilder.send();
        } catch (final Exception e) {
            //RequestBuilder#send generates exception when not executed in a browser
        } finally {
            verify(requestBuilder, times(1)).getAPIToken();
            verify(requestBuilder, never()).setHeader(eq("X-Bonita-API-Token"), anyString());
        }
    }

}
