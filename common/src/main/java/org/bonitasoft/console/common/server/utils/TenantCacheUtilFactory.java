package org.bonitasoft.console.common.server.utils;

/**
 * @author Julien Mege
 *
 */
public class TenantCacheUtilFactory {

    private static TenantCacheUtil tenantCacheUtil;

    /**
     * Get FormCacheUtil of different Domain
     * @return TenatCacheUtil
     */
    public static TenantCacheUtil getTenantCacheUtil() {
        if (tenantCacheUtil == null) {
            tenantCacheUtil = new TenantCacheUtil();
        }
        return tenantCacheUtil;
    }
}
