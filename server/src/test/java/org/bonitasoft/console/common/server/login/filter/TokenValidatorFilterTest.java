package org.bonitasoft.console.common.server.login.filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TokenValidatorFilterTest {

    public static final String SESSION_CSRF_TOKEN = "csrftoken";

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    private TokenValidatorFilter filter = spy(new TokenValidatorFilter());

    @Before
    public void setUp() throws Exception {
        when(filter.isCsrfProtectionEnabled()).thenReturn(true);
        request.getSession().setAttribute("api_token", SESSION_CSRF_TOKEN);
    }

    @Test
    public void should_check_csrf_token_from_request_header() throws Exception {
        request.addHeader("X-Bonita-API-Token", SESSION_CSRF_TOKEN);

        boolean valid = filter.checkValidCondition(request, response);

        assertThat(valid).isTrue();
    }

    @Test
    public void should_not_check_csrf_token_for_GET_request() throws Exception {
        request.setMethod("GET");

        boolean valid = filter.checkValidCondition(request, response);

        assertThat(valid).isTrue();
    }

    @Test
    public void should_set_401_status_when_csrf_request_header_is_wrong() throws Exception {
        request.addHeader("X-Bonita-API-Token", "notAValidToken");

        boolean valid = filter.checkValidCondition(request, response);

        assertThat(valid).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    public void should_set_401_status_when_csrf_request_header_is_not_set() throws Exception {

        boolean valid = filter.checkValidCondition(request, response);

        assertThat(valid).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }


    @Test
    public void should_check_csrf_token_from_request_parameter() throws Exception {
        request.addParameter("CSRFToken", SESSION_CSRF_TOKEN);

        boolean valid = filter.checkValidCondition(request, response);

        assertThat(valid).isTrue();
    }

    @Test
    public void should_set_401_status_when_csrf_request_parameter_is_wrong() throws Exception {
        request.addParameter("CSRFToken", "notAValidToken");

        boolean valid = filter.checkValidCondition(request, response);

        assertThat(valid).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }
}
