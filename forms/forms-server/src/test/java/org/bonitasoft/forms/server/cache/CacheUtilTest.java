/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.cache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.ehcache.CacheManager;

/**
 * @author Rohart Bastien
 */
public class CacheUtilTest {

    protected CacheManager cacheManager = null;

    @Before
    public void setUp() {
        cacheManager = CacheUtil.getCacheManager(System.getProperty("java.io.tmpdir"));
        assertNotNull("Cannot create cache", CacheUtil.createCache(cacheManager, cacheManager.getName()));
    }

    @After
    public void tearDown() {
        if (cacheManager != null) {
            CacheUtil.clear(cacheManager.getDiskStorePath(), cacheManager.getName());
        }
    }

    @Test
    public void testCreateCaches() {
        try {
            assertNotNull("Cannot create caches", CacheUtil.createCache(cacheManager, cacheManager.getName()));
        } finally {
            CacheUtil.clear(cacheManager.getDiskStorePath(), cacheManager.getName());
        }
    }

    @Test
    public void testStore() {
        CacheUtil.store(cacheManager.getDiskStorePath(), cacheManager.getName(),
                new String("testStoreKey"), new String("testStoreValue"));
        assertNotNull("Cannot store", cacheManager.getCache(cacheManager.getName()));
    }

    @Test
    public void testGet() {
        CacheUtil.store(cacheManager.getDiskStorePath(), cacheManager.getName(),
                new String("testStoreKey"), new String("testStoreValue"));
        assertNotNull("Cannot get the element in the cache", CacheUtil.get(cacheManager.getDiskStorePath(), cacheManager.getName(),
                "testStoreKey"));
    }

    @Test
    public void testClear() {
        CacheUtil.clear(cacheManager.getDiskStorePath(), cacheManager.getName());
        assertNull("Cannot clear the cache", CacheUtil.get(cacheManager.getName(), cacheManager.getDiskStorePath(),
                "testStoreKey"));
    }

}
