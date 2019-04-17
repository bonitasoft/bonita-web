/**
 * Copyright (C) 2019 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.engine.page.ContentType;

public class CustomPageContentValidator {

    private static final String INDEX_GROOVY = "Index.groovy";

    private static final String INDEX_HTML = "index.html";

    private static final String PAGE_PROPERTIES = "page.properties";

    private static final String THEME_CSS = "theme.css";

    public void validate(File pageFolder) throws InvalidPageZipContentException {
        final File[] rootFiles = pageFolder.listFiles();
        if (rootFiles == null) {
            throw new InvalidPageZipContentException(
                    "Content not found.");
        }
        Properties pageProperties = Stream.of(rootFiles)
                .filter(file -> file.getName().matches(PAGE_PROPERTIES))
                .findFirst()
                .map(this::loadProperties)
                .orElseThrow(() -> new InvalidPageZipContentException(String.format("%s descriptor is missing.",PAGE_PROPERTIES)));

        String contentType = pageProperties.getProperty(CustomPageService.PROPERTY_CONTENT_TYPE);
        Optional<File> resouresFolder = Stream.of(rootFiles)
                .filter(file -> file.getName().matches(CustomPageService.RESOURCES_PROPERTY))
                .findFirst();
        if (Objects.equals(contentType, ContentType.THEME)) {
            if (!resouresFolder.filter(resources -> new File(resources, THEME_CSS).exists()).isPresent()) {
                throw new InvalidPageZipContentException(String.format("%s is missing.",THEME_CSS));
            }
        } else if (!Objects.equals(contentType, ContentType.API_EXTENSION)) {
            if (Stream.of(rootFiles)
                    .noneMatch(file -> file.getName().matches(INDEX_HTML) || file.getName().matches(INDEX_GROOVY))
                    && !resouresFolder.filter(resources -> new File(resources, INDEX_HTML).exists()
                            || new File(resources, INDEX_GROOVY).exists()).isPresent()) {
                throw new InvalidPageZipContentException(String.format("%s or %s is missing.",INDEX_HTML,INDEX_GROOVY));
            }
        }
    }

    private Properties loadProperties(File pagePropertyFile) {
        Properties pageProperties = new java.util.Properties();
        try (InputStream is = new FileInputStream(pagePropertyFile)) {
            pageProperties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load page.properties file.", e);
        }
        return pageProperties;
    }
    
}
