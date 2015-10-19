/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChildFirstClassLoaderTest {

    @Mock
    private CustomPageDependenciesResolver customPageDependenciesResolver;
    private ChildFirstClassLoader classLoader;
    @Rule
    public TemporaryFolder tmpRule = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        when(customPageDependenciesResolver.getTempFolder()).thenReturn(tmpRule.newFolder());
    }

    @After
    public void tearDown() throws Exception {
        if(classLoader != null){
            classLoader.close();
        }
    }

    @Test
    public void should_add_custom_page_jar_resources_in_classloader_urls() throws Exception {
        classLoader = newClassloader();
        when(customPageDependenciesResolver.resolveCustomPageDependencies()).thenReturn(loadedResources("util.jar"));

        classLoader.addCustomPageResources();

        assertThat(classLoader.getURLs()).hasSize(1);
    }

    @Test
    public void should_add_custom_page_non_jar_resources_in_classloader() throws Exception {
        classLoader = newClassloader();
        when(customPageDependenciesResolver.resolveCustomPageDependencies()).thenReturn(loadedResources("util.properties"));

        classLoader.addCustomPageResources();

        assertThat(classLoader.getURLs()).isEmpty();
        assertThat(classLoader.getResourceAsStream("util.properties")).isNotNull();
    }

    private Map<String, byte[]> loadedResources(String... resourceNames) {
        final Map<String, byte[]> resources = new HashMap<>();
        for(final String resource : resourceNames){
            resources.put(resource, new byte[0]);
        }
        return resources;
    }

    private ChildFirstClassLoader newClassloader() {
        return new ChildFirstClassLoader("myPage", customPageDependenciesResolver, Thread.currentThread().getContextClassLoader());
    }

}
