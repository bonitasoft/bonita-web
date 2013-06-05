/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Rohart Bastien
 *
 */
public class CacheUtilTest {

    static {
        final String bonitaHome = System.getProperty("bonita.home");
        if (bonitaHome == null) {
            System.err.println("\n\n*** Forcing bonita.home to target/bonita \n\n\n");
            System.setProperty("bonita.home", "target/bonita/home");
        } else {
            System.err.println("\n\n*** bonita.home already set to: " + bonitaHome + " \n\n\n");
        }
    }
    
    @Before
    public void setUp() {
        assertNotNull("Cannot create cache", CacheUtil.createCache(CacheUtil.CACHE_MANAGER.getName(),
                CacheUtil.CACHE_MANAGER.getDiskStorePath()));
    }
    
    @After
    public void tearDown() {
        CacheUtil.clear(CacheUtil.CACHE_MANAGER.getName());
    }
   

    @Test
    public void testCreateCaches() {
        try{
        assertNotNull("Cannot create caches", CacheUtil.createCache(CacheUtil.CACHE_MANAGER.getName(),
                CacheUtil.CACHE_MANAGER.getDiskStorePath()));
        }finally{
            CacheUtil.clear(CacheUtil.CACHE_MANAGER.getName());
        }
    }
    
    @Test
    public void testStore() {
        CacheUtil.store(CacheUtil.CACHE_MANAGER.getName(), CacheUtil.CACHE_MANAGER.getDiskStorePath(),
                new String("testStoreKey"), new String("testStoreValue"));
         assertNotNull("Cannot store", CacheUtil.CACHE_MANAGER.getCache(CacheUtil.CACHE_MANAGER.getName()));
    }
    
    @Test
    public void testGet() {
        CacheUtil.store(CacheUtil.CACHE_MANAGER.getName(), CacheUtil.CACHE_MANAGER.getDiskStorePath(),
                new String("testStoreKey"), new String("testStoreValue"));
        assertNotNull("Cannot get the element in the cache", CacheUtil.get(CacheUtil.CACHE_MANAGER.getName(),
                "testStoreKey"));
    }
    
    @Test
    public void testClear() {
        CacheUtil.clear(CacheUtil.CACHE_MANAGER.getName());
        assertNull("Cannot clear the cache", CacheUtil.get(CacheUtil.CACHE_MANAGER.getName(),
                "testStoreKey"));
    }

}
