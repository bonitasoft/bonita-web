package org.bonitasoft.console.common.server.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

public class CacheFilterTest {

    @Mock
    private FilterChain chain;

    @Mock
    private HttpServletRequestAccessor request;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession httpSession;
    
    @Mock
    private FilterConfig filterConfig;
    
    @Mock
    private ServletContext servletContext;

    @Spy
    CacheFilter cacheFilter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        doReturn(httpSession).when(request).getHttpSession();
        when(request.asHttpServletRequest()).thenReturn(httpRequest);
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer());
        when(servletContext.getContextPath()).thenReturn("");
        when(filterConfig.getServletContext()).thenReturn(servletContext);
        when(filterConfig.getInitParameterNames()).thenReturn(Collections.emptyEnumeration());
        cacheFilter.init(filterConfig);
    }

    @Test
    public void testFilterWithExcludedURL() throws Exception {
        final String url = "test";
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer(url));
        doReturn(true).when(cacheFilter).matchExcludePatterns(url);
        cacheFilter.doFilter(httpRequest, httpResponse, chain);
        verify(cacheFilter, times(0)).proceedWithFiltering(httpRequest, httpResponse, chain);
        verify(cacheFilter, times(1)).excludePatternFiltering(httpRequest, httpResponse, chain);
        verify(chain, times(1)).doFilter(httpRequest, httpResponse);
    }

    @Test
    public void testMatchExcludePatterns() throws Exception {

        matchExcludePattern("/apps/home/", true);
        matchExcludePattern("/apps/home/css/style.css", false);
        matchExcludePattern("http://localhost:8080/bonita/portal/resource/page/content/", true);
        matchExcludePattern("http://localhost:8080/bonita/portal/resource/page/content/image/logo.png", false);
        matchExcludePattern("http://localhost:8080/portal/themeResource/resource", false);
        matchExcludePattern("http://localhost:8080/portal/custom-page/API/identity/user/1", true);
        matchExcludePattern("http://localhost:8080/bonita/portal/custom-page/custompage_cacheBustingBug1/?locale=en&profile=101&_f=allpagesfilter&_id=22", true);
    }

    @Test
    public void testCompileNullPattern() throws Exception {
        assertThat(cacheFilter.compilePattern(null)).isNull();
    }

    @Test
    public void testCompileWrongPattern() throws Exception {
        assertThat(cacheFilter.compilePattern("((((")).isNull();
    }

    @Test
    public void testCompileSimplePattern() throws Exception {
        final String patternToCompile = "test";
        assertThat(cacheFilter.compilePattern(patternToCompile)).isNotNull().has(new Condition<Pattern>() {

            @Override
            public boolean matches(final Pattern pattern) {
                return pattern.pattern().equalsIgnoreCase(patternToCompile);
            }
        });
    }

    @Test
    public void testCompileExcludePattern() throws Exception {
        final String patternToCompile = "^/(bonita/)?((mobile/)?login.jsp$)|(images/)|(redirectCasToCatchHash.jsp)|(loginservice)|(serverAPI)|(/mobile/js/)|(maintenance.jsp$)|(API/platform/)|(platformloginservice$)|(portal/themeResource$)|(portal/scripts)|(/bonita/?$)|(logoutservice)";
        assertThat(cacheFilter.compilePattern(patternToCompile)).isNotNull().has(new Condition<Pattern>() {

            @Override
            public boolean matches(final Pattern pattern) {
                return pattern.pattern().equalsIgnoreCase(patternToCompile);
            }
        });
    }

    private void matchExcludePattern(final String urlToMatch, final Boolean mustMatch) {
        doReturn(Pattern.compile(CacheFilter.CACHE_FILTER_EXCLUDED_RESOURCES_PATTERN)).when(cacheFilter).getExcludePattern();
        if (cacheFilter.matchExcludePatterns(urlToMatch) != mustMatch) {
            Assertions.fail("Matching excludePattern and the Url " + urlToMatch + " must return " + mustMatch);
        }
    }
    
}
