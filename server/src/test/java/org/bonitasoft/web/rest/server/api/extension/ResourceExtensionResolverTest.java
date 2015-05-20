package org.bonitasoft.web.rest.server.api.extension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.page.PageMappingService;
import org.bonitasoft.console.common.server.page.PageReference;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.data.Method;

/**
 * @author Laurent Leseigneur
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceExtensionResolverTest {

    public static final String API_EXTENSION_POST_MAPPING_KEY = "apiExtension|POST|myPostResource";
    public static final String API_EXTENSION_GET_MAPPING_KEY = "apiExtension|GET|helloWorld";

    public static final long PAGE_ID = 2L;
    @Mock
    private Request request;

    @Mock
    private PageResourceProvider pageResourceProvider;

    File file;

    FileInputStream fileInputStream;

    @Mock
    private APISession apiSession;

    @Mock
    private PageMappingService pageMappingService;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    private PageReference pageReference;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void before() throws Exception {
        URL resource = ResourceExtensionResolverTest.class.getResource("page.properties");
        file = new File(resource.toURI());
        fileInputStream = new FileInputStream(file);

        doReturn(fileInputStream).when(pageResourceProvider).getResourceAsStream("page.properties");

    }

    @Test
    public void should_post_resolve_class_file_name() throws Exception {
        //given
        Request request = new Request(Method.POST, "/bonita/API/extension/myPostResource");
        ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request, new PageMappingService());

        //when
        final String s = resourceExtensionResolver.resolveClassFileName(pageResourceProvider);

        //then
        assertThat(s).isEqualTo("PostResource.groovy");
    }

    @Test
    public void should_get_resolve_class_file_name() throws Exception {
        //given
        Request request = new Request(Method.GET, "/bonita/API/extension/helloWorld");
        ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request, new PageMappingService());

        //when
        final String s = resourceExtensionResolver.resolveClassFileName(pageResourceProvider);

        //then
        assertThat(s).isEqualTo("Index.groovy");
    }

    @Test
    public void should_not_resolve_class_file_name() throws Exception {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("error while getting resource:apiExtension|POST|notResource");

        //given
        Request request = new Request(Method.POST, "/bonita/API/extension/notResource");
        ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request, new PageMappingService());

        //when then exception
        resourceExtensionResolver.resolveClassFileName(pageResourceProvider);

    }

    @Test
    public void should_generate_mapping_key() throws Exception {
        //given
        Request request = new Request(Method.POST, "/bonita/API/extension/myPostResource");
        ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request, new PageMappingService());

        //when
        final String mappingKey = resourceExtensionResolver.generateMappingKey();

        //then
        assertThat(mappingKey).isEqualTo(API_EXTENSION_POST_MAPPING_KEY);
    }

    @Test
    public void should_generate_mapping_key_with_parameters() throws Exception {
        //given
        Request request = new Request(Method.GET, "/bonita/API/extension/helloWorld?param1=a&param2=b");
        ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request, new PageMappingService());

        //when
        final String mappingKey = resourceExtensionResolver.generateMappingKey();

        //then
        assertThat(mappingKey).isEqualTo(API_EXTENSION_GET_MAPPING_KEY);
    }

    @Test
    public void should_resolve_pageId() throws Exception {
        //given
        Request request = new Request(Method.POST, "/bonita/API/extension/myPostResource");
        doReturn(Locale.FRENCH).when(httpServletRequest).getLocale();
        doReturn(pageReference).when(pageMappingService).getPage(httpServletRequest, apiSession, API_EXTENSION_POST_MAPPING_KEY, Locale.FRENCH, false);
        doReturn(PAGE_ID).when(pageReference).getPageId();

        ResourceExtensionResolver resourceExtensionResolver = spy(new ResourceExtensionResolver(request, pageMappingService));
        doReturn(httpServletRequest).when(resourceExtensionResolver).getHttpServletRequest();

        //when
        final Long pageId = resourceExtensionResolver.resolvePageId(apiSession);

        //then
        verify(pageMappingService).getPage(any(HttpServletRequest.class), eq(apiSession), eq(API_EXTENSION_POST_MAPPING_KEY), any(Locale.class), eq(false));
        assertThat(pageId).isEqualTo(PAGE_ID);
    }

    @Test
    public void should_resolve_pageId_with_parameters() throws Exception {
        //given
        Request request = new Request(Method.GET, "/bonita/API/extension/helloWorld?param1=a&param2=b");

        doReturn(Locale.FRENCH).when(httpServletRequest).getLocale();
        doReturn(pageReference).when(pageMappingService).getPage(httpServletRequest, apiSession, API_EXTENSION_GET_MAPPING_KEY, Locale.FRENCH, false);
        doReturn(PAGE_ID).when(pageReference).getPageId();
        doReturn("/bonita/API/extension/helloWorld").when(httpServletRequest).getContextPath();

        ResourceExtensionResolver resourceExtensionResolver = spy(new ResourceExtensionResolver(request, pageMappingService));
        doReturn(httpServletRequest).when(resourceExtensionResolver).getHttpServletRequest();

        //when
        final Long pageId = resourceExtensionResolver.resolvePageId(apiSession);

        //then
        verify(pageMappingService).getPage(any(HttpServletRequest.class), eq(apiSession), eq(API_EXTENSION_GET_MAPPING_KEY), any(Locale.class), eq(false));
        assertThat(pageId).isEqualTo(PAGE_ID);
    }

    @Test
    public void should_mapping_key_exclude_parameters() throws Exception {
        //given
        Request request = new Request(Method.GET, "/bonita/API/extension/helloWorld?param1=a&param2=b");
        doReturn("/bonita/API/extension/helloWorld").when(httpServletRequest).getContextPath();
        ResourceExtensionResolver resourceExtensionResolver = spy(new ResourceExtensionResolver(request, pageMappingService));
        doReturn(httpServletRequest).when(resourceExtensionResolver).getHttpServletRequest();

        //when
        final String mappingKey = resourceExtensionResolver.generateMappingKey();

        //then
        assertThat(mappingKey).isEqualTo(API_EXTENSION_GET_MAPPING_KEY);
    }

    @Test(expected = NotFoundException.class)
    public void should_unresolved_pageId_throw_exception() throws Exception {
        //given
        Request request = new Request(Method.POST, "/bonita/API/extension/myPostResource");
        doReturn(Locale.FRENCH).when(httpServletRequest).getLocale();
        NotFoundException notFoundException = new NotFoundException("page not found");
        doThrow(notFoundException).when(pageMappingService).getPage(httpServletRequest, apiSession, API_EXTENSION_POST_MAPPING_KEY, Locale.FRENCH, false);
        doReturn(PAGE_ID).when(pageReference).getPageId();

        ResourceExtensionResolver resourceExtensionResolver = spy(new ResourceExtensionResolver(request, pageMappingService));
        doReturn(httpServletRequest).when(resourceExtensionResolver).getHttpServletRequest();

        //when
        final Long pageId = resourceExtensionResolver.resolvePageId(apiSession);

        //then
        verify(pageMappingService).getPage(any(HttpServletRequest.class), eq(apiSession), eq(API_EXTENSION_POST_MAPPING_KEY), any(Locale.class), eq(false));
        assertThat(pageId).isEqualTo(PAGE_ID);
    }

}
