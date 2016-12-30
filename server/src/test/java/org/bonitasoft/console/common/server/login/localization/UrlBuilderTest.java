package org.bonitasoft.console.common.server.login.localization;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.dev.util.collect.HashMap;


@RunWith(MockitoJUnitRunner.class)
public class UrlBuilderTest {

    UrlBuilder urlBuilder;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void appendParameter_should_add_service_param_to_URL() throws URISyntaxException {
        urlBuilder = new UrlBuilder("http://www.cas-service.com/login");
        urlBuilder.appendParameter("service", "http://www.bonitasoft.com/bonita");

        assertThat(urlBuilder.build()).isEqualTo("http://www.cas-service.com/login?service=http%3A%2F%2Fwww.bonitasoft.com%2Fbonita");
    }

    @Test
    public void appendParameter_should_add_service_param_to_URL_with_hash() throws URISyntaxException {
        urlBuilder = new UrlBuilder("http://www.cas-service.com/login#_pf=2");
        urlBuilder.appendParameter("service", "http://www.bonitasoft.com/bonita");

        assertThat(urlBuilder.build()).isEqualTo("http://www.cas-service.com/login?service=http%3A%2F%2Fwww.bonitasoft.com%2Fbonita#_pf=2");
    }

    @Test
    public void appendParameter_should_add_id_params_to_URL() throws URISyntaxException {
        urlBuilder = new UrlBuilder("http://www.cas-service.com/login");
        urlBuilder.appendParameter("ids", new UrlValue("1", "2", "3").toString());

        assertThat(urlBuilder.build()).isEqualTo("http://www.cas-service.com/login?ids=1%2C2%2C3");
    }

    @Test
    public void appendParameter_should_add_params_to_URL() throws URISyntaxException {
        Map<String, String[]> params = new HashMap<>();
        params.put("service", new String[] { "http://www.bonitasoft.com/bonita" });
        params.put("ids", new String[] { "4", "5", "6" });
        urlBuilder = new UrlBuilder("http://www.cas-service.com/login?redirect=false");
        urlBuilder.appendParameters(params);

        assertThat(urlBuilder.build())
                .isEqualTo("http://www.cas-service.com/login?redirect=false&service=http%3A%2F%2Fwww.bonitasoft.com%2Fbonita&ids=4%2C5%2C6");
    }

    @Test
    public void appendParameter_should_not_add_params_already_defined_to_URL() throws Exception {
        urlBuilder = new UrlBuilder("http://www.cas-service.com/login?service=http://www.bonitasoft.com");
        urlBuilder.appendParameter("service", "http://www.bonitasoft.com/bonita");

        assertThat(urlBuilder.build()).isEqualTo("http://www.cas-service.com/login?service=http%3A%2F%2Fwww.bonitasoft.com");
    }
}
