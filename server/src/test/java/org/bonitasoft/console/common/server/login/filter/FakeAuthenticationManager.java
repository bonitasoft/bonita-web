package org.bonitasoft.console.common.server.login.filter;

import org.bonitasoft.console.common.server.auth.impl.standard.StandardAuthenticationManagerImpl;

public class FakeAuthenticationManager extends StandardAuthenticationManagerImpl {

    private long defaultTenantId;

    public FakeAuthenticationManager(long defaultTenantId) {
        this.defaultTenantId = defaultTenantId;
    }

    @Override
    protected long getDefaultTenantId() {
        return defaultTenantId;
    }
}
