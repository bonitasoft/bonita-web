package org.bonitasoft.console.common.server.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Julien Mege
 *
 */
public class TenantCacheUtilFactory {

    private static Map<Long, TenantCacheUtil> map = new HashMap();

    /**
     * Get FormCacheUtil of different Domain
     * @return TenatCacheUtil
     */
    public static TenantCacheUtil getTenantCacheUtil(final long tenantID) {
        if(!map.containsKey(tenantID)){
            map.put(tenantID, new TenantCacheUtil(tenantID));
        }
        return map.get(tenantID);
    }
}
