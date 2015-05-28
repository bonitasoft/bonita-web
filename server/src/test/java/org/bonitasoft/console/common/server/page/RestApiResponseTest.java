package org.bonitasoft.console.common.server.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Laurent Leseigneur
 */
public class RestApiResponseTest {

    RestApiResponseBuilder restApiResponseBuilder;

    @Before
    public void setUp() throws Exception {
        restApiResponseBuilder = new RestApiResponseBuilder();
    }

    @Test
    public void testGetResponse_with_default_values() throws Exception {
        //when
        final RestApiResponse response = restApiResponseBuilder.withResponse("response").build();

        //then
        RestApiResponseAssert.assertThat(response)
                .hasResponse("response")
                .hasMediaType("application/json")
                .hasHttpStatus(200)
                .hasCharacterSet("UTF-8")
                .hasNoAdditionalCookies();

        assertThat(response.getAdditionalHeaders()).isEmpty();
    }

    @Test
    public void testGetHttpStatus() throws Exception {
        //when
        final RestApiResponse response = restApiResponseBuilder.withResponseStatus(404).build();

        //then
        RestApiResponseAssert.assertThat(response).hasHttpStatus(404);

    }

    @Test
    public void testGetAdditionalHeaders() throws Exception {
        //when
        final RestApiResponse response = restApiResponseBuilder.withAdditionalHeader("x-header1", "value1")
                .withAdditionalHeader("x-header2", "value2")
                .build();

        //then
        assertThat(response.getAdditionalHeaders()).hasSize(2).contains(entry("x-header1", "value1"), entry("x-header1", "value1"));

    }

    @Test
    public void testGetAdditionalCookies() throws Exception {
        //when
        Cookie cookie1 = new Cookie("name1", "value1");
        Cookie cookie2 = new Cookie("name2", "value2");

        final RestApiResponse response = restApiResponseBuilder.withAdditionalCookie(cookie1)
                .withAdditionalCookie(cookie2)
                .build();

        //then
        RestApiResponseAssert.assertThat(response).hasAdditionalCookies(cookie1, cookie2);

    }

    @Test
    public void testGetCharacterSet() throws Exception {
        //given
        final String characterSet = "utf-16";

        //when
        final RestApiResponse response = restApiResponseBuilder.withCharacterSet(characterSet)
                .build();

        //then
        RestApiResponseAssert.assertThat(response).hasCharacterSet(characterSet);
    }

    @Test
    public void testGetMediaType() throws Exception {
        //when
        String mediaType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

        //when
        final RestApiResponse response = restApiResponseBuilder.withMediaType(mediaType)
                .build();

        //then
        RestApiResponseAssert.assertThat(response).hasMediaType(mediaType);

    }


}