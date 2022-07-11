package org.bonitasoft.console.common.server.utils;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class TenantCacheUtil {
    /**
     * Logger
     */
    private static Logger LOGGER = LoggerFactory.getLogger(TenantCacheUtil.class.getName());

    protected static String CACHE_DISK_STORE_PATH = null;

    protected static String PROCESS_ACTOR_INITIATOR_CACHE = "processActorInitiatorCache";

    protected long tenantID;

    protected TenantCacheUtil(final long tenantID) {
        try {
            CACHE_DISK_STORE_PATH = WebBonitaConstantsUtils.getTenantInstance().getFormsWorkFolder().getAbsolutePath();
            this.tenantID = tenantID;
        } catch (final Exception e) {
            LOGGER.warn( "Unable to retrieve the path of the cache disk store directory path.", e);
        }
    }



    public Long getProcessActorInitiatorId(final Long processId) {
        return (Long) CacheUtil.get(CACHE_DISK_STORE_PATH, PROCESS_ACTOR_INITIATOR_CACHE, processId);
    }

    public Long storeProcessActorInitiatorId(final Long processId, final Long actorInitiatorId) {
        CacheUtil.store(CACHE_DISK_STORE_PATH, PROCESS_ACTOR_INITIATOR_CACHE, processId, actorInitiatorId);
        return actorInitiatorId;
    }

    public void clearAll() {
        CacheUtil.clear(CACHE_DISK_STORE_PATH, PROCESS_ACTOR_INITIATOR_CACHE);
    }

}
