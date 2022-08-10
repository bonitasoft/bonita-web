package org.bonitasoft.console.common.server.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Anthony Birembaut
 */
public abstract class ExcludingPatternFilter implements Filter {

    protected URLExcludePattern urlExcludePattern;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlExcludePattern = new URLExcludePattern(filterConfig, getDefaultExcludedPages());
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String url = ((HttpServletRequest) request).getRequestURL().toString();
        if (matchExcludePatterns(url)) {
            excludePatternFiltering(request, response, chain);
        } else {
            proceedWithFiltering(request, response, chain);
        }
    }

    public void excludePatternFiltering(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    public abstract void proceedWithFiltering(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException;

    public abstract String getDefaultExcludedPages();

    public Pattern compilePattern(final String stringPattern) {
        return urlExcludePattern.compilePattern(stringPattern);
    }

    /**
     * check the given url against the local url exclude pattern
     *
     * @param url
     *        the url to check
     * @return true if the url match the pattern
     */
    public boolean matchExcludePatterns(final String url) {
        return urlExcludePattern.matchExcludePatterns(url);
    }

    public Pattern getExcludePattern() {
        return urlExcludePattern.getExcludePattern();
    }
    
}
