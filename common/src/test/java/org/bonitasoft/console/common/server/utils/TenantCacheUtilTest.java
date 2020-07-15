package org.bonitasoft.console.common.server.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TenantCacheUtilTest {

    @Test
    public void shouldStoreActorInitiator() {
        final TenantCacheUtil tenantCacheUtil = TenantCacheUtilFactory.getTenantCacheUtil(1);
        tenantCacheUtil.storeProcessActorInitiatorId(2L, 3L);
        final Long actorInitiatorRetrievedFromCacheByParameters = TenantCacheUtilFactory.getTenantCacheUtil(1).getProcessActorInitiatorId(2L);
        assertNotNull(actorInitiatorRetrievedFromCacheByParameters);
        assertEquals(3L, actorInitiatorRetrievedFromCacheByParameters.longValue());
    }
}
