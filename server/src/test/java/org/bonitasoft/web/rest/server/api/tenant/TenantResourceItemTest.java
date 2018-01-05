package org.bonitasoft.web.rest.server.api.tenant;

import org.bonitasoft.engine.tenant.TenantResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantResourceItemTest {

    @Mock
    private TenantResource tenantResource;

    @Test
    public void should_format_lastUpdateDate_as_ISO8601_string_when_tenantResourceItem_is_created(){
        String dateAsString = "2018-01-05T09:04:19Z";
        Instant instant = Instant.ofEpochSecond(1515143059);
        when(tenantResource.getLastUpdateDate()).thenReturn(OffsetDateTime.ofInstant(instant, ZoneOffset.UTC));

        TenantResourceItem tenantResourceItem = new TenantResourceItem(tenantResource);

        assertThat(tenantResourceItem.getLastUpdateDate()).isEqualTo(dateAsString);
    }
}
