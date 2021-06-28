package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class UrlBuilderTest {

    UrlBuilder urlBuilder;
    
    private final static String specialULRCharacters = "!'();:@&=+$,/?# ";
    
    private final static String specialULRCharactersEncoded = "%21%27%28%29%3B%3A%40%26%3D%2B%24%2C%2F%3F%23+";

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
    
    @Test
    public void should_add_params_to_URL_with_special_chars_in_hash() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita?param=value#?form=l" + specialULRCharactersEncoded + "f");
        urlBuilder.appendParameter("param2", "value2");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita?param=value&param2=value2#?form=l" + specialULRCharactersEncoded + "f");
    }
    
    @Test
    public void should_add_params_to_URL_with_hash() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita?param=value#path/with/slash?hashparam=1&hashparam2=2");
        urlBuilder.appendParameter("param2", "value2");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita?param=value&param2=value2#path/with/slash?hashparam=1&hashparam2=2");
    }
    
    @Test
    public void should_add_params_to_URL_with_v6_form_id_in_hash() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita?param=value#form=proc%C3%A9with%23+in%20name--2.0--taskwith+%24$entry&task=25&assignTask=true");
        urlBuilder.appendParameter("param2", "value2");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita?param=value&param2=value2#form=proc%C3%A9with%23+in%20name--2.0--taskwith+%24$entry&task=25&assignTask=true");
    }
    
    @Test
    public void should_add_params_to_URL_with_special_chars_in_query() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita?param=value&form=l" + specialULRCharactersEncoded + "f");
        urlBuilder.appendParameter("param2", "value" + specialULRCharacters + "2");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita?param=value&form=l" + specialULRCharactersEncoded + "f&param2=value" + specialULRCharactersEncoded + "2");
    }
    
    @Test
    public void should_leave_path_unchanged() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita+soft?param=value");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita+soft?param=value");
    }
    
    @Test
    public void should_leave_path_unchanged2() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita%2Bsoft?param=value");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita%2Bsoft?param=value");
    }
    
    @Test
    public void should_leave_path_with_space_unchanged() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita%20soft?param=value");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita%20soft?param=value");
    }
    
    @Test
    public void should_leave_URL_without_querystring_unchanged() throws Exception {
        urlBuilder = new UrlBuilder("http://localhost:8080/bonita/homepage");

        assertThat(urlBuilder.build()).isEqualTo("http://localhost:8080/bonita/homepage");
    }
}
