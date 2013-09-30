/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheUtil {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(CacheUtil.class.getName());

    protected static CacheManager CACHE_MANAGER = null;

    static {
        try {
            final String pathToCacheConfigFile = WebBonitaConstantsUtils.getInstance().getConfFolder().getAbsolutePath() + "/cache-config.xml";
            CACHE_MANAGER = new CacheManager(pathToCacheConfigFile);
        } catch (final Exception e) {
            LOGGER.log(Level.WARNING, "Unable to retrieve the path of the cache configuration file.", e);
            CACHE_MANAGER = new CacheManager();
        }
    }

    protected static synchronized Cache createCache(final String cacheName, final String diskStorePath) {

        // Double-check
        Cache cache = CACHE_MANAGER.getCache(cacheName);
        if (cache == null) {
            CACHE_MANAGER.addCache(cacheName);
            cache = CACHE_MANAGER.getCache(cacheName);
        }
        if (diskStorePath != null) {
            cache.getCacheConfiguration().setDiskStorePath(diskStorePath);
        }
        return cache;
    }

    public static void store(final String cacheName, final String diskStorePath, final Object key, final Object value) {
        Cache cache = CACHE_MANAGER.getCache(cacheName);
        if (cache == null) {
            cache = createCache(cacheName, diskStorePath);
        }

        final Element element = new Element(key, value);
        cache.put(element);
    }

    public static Object get(final String cacheName, final Object key) {
        Object value = null;
        final Cache cache = CACHE_MANAGER.getCache(cacheName);
        if (cache != null) {
            final Element element = cache.get(key);
            if (element != null) {
                value = element.getValue();
            }
        } else {
            LOGGER.log(Level.FINEST, "Cache with name " + cacheName + " doesn't exists or wasn't created yet.");
        }
        return value;
    }

    public static void clear(final String cacheName) {
        final Cache cache = CACHE_MANAGER.getCache(cacheName);
        if (cache != null) {
            cache.removeAll();
        }
    }
}
