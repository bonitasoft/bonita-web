package org.bonitasoft.console.common.server.filter;

import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;

import org.apache.commons.lang3.StringUtils;

public class URLExcludePattern {

    /** the Pattern of url not to filter */
    public Pattern excludePattern;

    private static final Logger LOGGER = Logger.getLogger(URLExcludePattern.class.getName());
    
    public URLExcludePattern(final FilterConfig filterConfig, String defaultExcludePattern) {
        final String contextPath = filterConfig.getServletContext().getContextPath();
        String processedDefaultExcludePattern;
        if (contextPath.length() > 0 && !contextPath.equals("/bonita")) {
            String webappName = contextPath.substring(1);
            processedDefaultExcludePattern = defaultExcludePattern.replace("bonita", webappName);
        } else {
            processedDefaultExcludePattern = defaultExcludePattern;
        }
        final String configExcludePattern = filterConfig.getInitParameter("excludePattern");
        excludePattern = compilePattern(StringUtils.defaultString(configExcludePattern, processedDefaultExcludePattern));
    }

    protected Pattern compilePattern(final String stringPattern) {
        if (StringUtils.isNotBlank(stringPattern)) {
            try {
                return Pattern.compile(stringPattern);
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, " impossible to create pattern from [" + stringPattern + "] : " + e);
            }
        }
        return null;
    }

    /**
     * check the given url against the local url exclude pattern
     *
     * @param url
     *        the url to check
     * @return true if the url match the pattern
     */
    public boolean matchExcludePatterns(final String url) {
        try {
            boolean isExcluded;
            if (getExcludePattern() == null) {
                isExcluded = false;
            } else {
                String path = new URL(url).getPath();
                // interprete ../
                String normalizedPath = new URI(url).normalize().getPath();
                isExcluded = getExcludePattern().matcher(path).find()
                        && getExcludePattern().matcher(normalizedPath).find();
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                if (isExcluded) {
                    LOGGER.log(Level.FINE, " Exclude pattern match with this url:" + url);
                } else {
                    LOGGER.log(Level.FINE, " Exclude pattern does not match with this url:" + url);
                }
            }
            return isExcluded;
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "impossible to get URL from given input [" + url + "]:" + e);
            }
            return getExcludePattern().matcher(url).find();

        }
    }

    public Pattern getExcludePattern() {
        return excludePattern;
    }
}