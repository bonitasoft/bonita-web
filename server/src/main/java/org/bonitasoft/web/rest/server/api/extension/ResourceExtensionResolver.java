package org.bonitasoft.web.rest.server.api.extension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bonitasoft.console.common.server.page.PageMappingService;
import org.bonitasoft.console.common.server.page.PageReference;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.restlet.Request;
import org.restlet.ext.servlet.ServletUtils;

/**
 * @author Laurent Leseigneur
 */
public class ResourceExtensionResolver {

    public static final String MAPPING_KEY_SEPARATOR = "|";
    public static final String MAPPING_KEY_PREFIX = "apiExtension";
    public static final String API_EXTENSION_TEMPLATE_PREFIX = "/API/extension/";
    private final Request request;
    private final PageMappingService pageMappingService;

    public ResourceExtensionResolver(Request request, PageMappingService pageMappingService) {
        this.request = request;
        this.pageMappingService = pageMappingService;
    }

    public Long resolvePageId(APISession apiSession) throws BonitaException {
        final HttpServletRequest httpServletRequest = getHttpServletRequest();
        final PageReference pageReference;
        pageReference = pageMappingService.getPage(httpServletRequest, apiSession, generateMappingKey(), httpServletRequest.getLocale(), false);
        return pageReference.getPageId();
    }

    protected HttpServletRequest getHttpServletRequest() {
        return ServletUtils.getRequest(this.request);
    }

    public String generateMappingKey() {
        final StringBuilder builder = new StringBuilder();
        final String requestAsString = getHttpServletRequest().getRequestURI();
        final String pathTemplate = StringUtils.substringAfter(requestAsString, API_EXTENSION_TEMPLATE_PREFIX);

        builder.append(MAPPING_KEY_PREFIX)
                .append(MAPPING_KEY_SEPARATOR)
                .append(request.getMethod().getName())
                .append(MAPPING_KEY_SEPARATOR)
                .append(pathTemplate);

        return builder.toString();
    }

    public File resolveRestApiControllerFile(PageResourceProviderImpl pageResourceProvider) throws NotFoundException {
        final Properties properties = new Properties();

        try (final InputStream resourceAsStream = pageResourceProvider.getResourceAsStream("page.properties");) {
            properties.load(resourceAsStream);
        } catch (final IOException e) {
            throw new NotFoundException("error while getting resource:" + generateMappingKey());
        }

        final String apiExtensionList = (String) properties.get("apiExtensions");
        final String[] apiExtensions = apiExtensionList.split(",");

        return toFile(pageResourceProvider, findMatchingExtension(properties, apiExtensions));

    }

    private String findMatchingExtension(Properties properties, String[] apiExtensions) throws NotFoundException {
        for (final String apiExtension : apiExtensions) {
            final String method = (String) properties.get(String.format("%s.method", apiExtension.trim()));
            final String pathTemplate = (String) properties.get(String.format("%s.pathTemplate", apiExtension.trim()));
            final String classFileName = (String) properties.get(String.format("%s.classFileName", apiExtension.trim()));

            if (extensionMatches(method, pathTemplate)) {
                return classFileName;
            }
        }
        throw new NotFoundException("error while getting resource:" + generateMappingKey());
    }

    private File toFile(PageResourceProviderImpl pageResourceProvider, String classFileName) {
        if (classFileName.startsWith("/")) {
            classFileName = classFileName.substring(1);
        }
        final String[] paths = classFileName.split("/");
        final Path restApiControllerPath = paths.length == 1 ? Paths.get(paths[0]) : Paths.get(paths[0], Arrays.copyOfRange(paths, 1, paths.length));
        return pageResourceProvider.getPageDirectory().toPath().resolve(restApiControllerPath).toFile();
    }

    private boolean extensionMatches(String method, String pathTemplate) {
        return request.getMethod().getName().equals(method)
                && getHttpServletRequest().getRequestURI().contains(String.format("%s%s", API_EXTENSION_TEMPLATE_PREFIX, pathTemplate));
    }
}
