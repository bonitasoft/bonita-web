/**
 * Copyright (C) 2014-2015 BonitaSoft S.A.
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

import static org.apache.commons.io.FileUtils.deleteQuietly;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.utils.BDMClientDependenciesResolver;

/**
 * @author Elias Ricken de Medeiros
 * @author Charles Souillard
 * @author Baptiste Mesta
 * @author Matthieu Chaffotte
 */
public class ChildFirstClassLoader extends MonoParentJarFileClassLoader {

    protected Map<String, byte[]> nonJarResources = new HashMap<String, byte[]>();

    private boolean isActive = true;

    private static final Logger LOGGER = Logger.getLogger(ChildFirstClassLoader.class.getName());

    private final CustomPageDependenciesResolver customPageDependenciesResolver;

    private final String version;

    private final BDMClientDependenciesResolver bdmDependenciesResolver;

    ChildFirstClassLoader(String pageName, CustomPageDependenciesResolver customPageDependenciesResolver, BDMClientDependenciesResolver bdmDependenciesResolver,
            ClassLoader parent) {
        super(pageName, new URL[] {}, parent);
        this.customPageDependenciesResolver = customPageDependenciesResolver;
        this.bdmDependenciesResolver = bdmDependenciesResolver;
        this.version = bdmDependenciesResolver.getBusinessDataModelVersion();
    }

    public void addCustomPageResources() {
        final Map<String, byte[]> customPageDependencies = customPageDependenciesResolver.resolveCustomPageDependencies();
        for (final Map.Entry<String, byte[]> resource : customPageDependencies.entrySet()) {
            if (resource.getKey().matches(".*\\.jar")) {
                final byte[] data = resource.getValue();
                try {
                    final File file = File.createTempFile(resource.getKey(), null, customPageDependenciesResolver.getTempFolder());
                    file.deleteOnExit();
                    FileUtils.writeByteArrayToFile(file, data);
                    addURL(new File(file.getAbsolutePath()).toURI().toURL());
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, String.format("Failed to add file %s in classpath", resource.getKey()), e);
                    }
                }
            } else {
                nonJarResources.put(resource.getKey(), resource.getValue());
            }
        }
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        /*
         * if (!isActive) {
         * throw new RuntimeException(this.toString() + " is not active anymore. Don't use it.");
         * }
         */
        InputStream is = getInternalInputstream(name);
        if (is == null && name.length() > 0 && name.charAt(0) == '/') {
            is = getInternalInputstream(name.substring(1));
        }
        return is;
    }

    private InputStream getInternalInputstream(final String name) {
        final byte[] classData = loadProcessResource(name);
        if (classData != null) {
            return new ByteArrayInputStream(classData);
        }
        final InputStream is = super.getResourceAsStream(name);
        if (is != null) {
            return is;
        }
        return null;
    }

    private byte[] loadProcessResource(final String resourceName) {
        return nonJarResources.containsKey(resourceName) ? nonJarResources.get(resourceName) : new byte[0];
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        /*
         * if (!isActive) {
         * throw new RuntimeException(this.toString() + " is not active anymore. Don't use it.");
         * }
         */
        Class<?> c = null;
        c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findClass(name);
            } catch (final ClassNotFoundException e) {
                // ignore
            } catch (final LinkageError le) {
                // might be because of a duplicate loading (concurrency loading), retry to find it one time See BS-2483
                c = findLoadedClass(name);
                if (c == null) {
                    // was not because of duplicate loading: throw the exception
                    throw le;
                }
            }
        }

        if (c == null) {
            c = getParent().loadClass(name);
        }

        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    public void release() {
        deleteQuietly(customPageDependenciesResolver.getTempFolder());
        isActive = false;
    }

    @Override
    public String toString() {
        return super.toString() + ", name=" + getName() + ", isActive: " + isActive + ", parent= " + getParent();
    }
}
