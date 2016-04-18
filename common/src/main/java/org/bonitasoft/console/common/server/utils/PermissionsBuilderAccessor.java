package org.bonitasoft.console.common.server.utils;

import java.io.IOException;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

public class PermissionsBuilderAccessor {

    public static PermissionsBuilder createPermissionBuilder(final APISession session) throws LoginFailedException {
        ProfileAPI profileAPI;
        ApplicationAPI applicationAPI;
        try {
            profileAPI = TenantAPIAccessor.getProfileAPI(session);
            applicationAPI = TenantAPIAccessor.getLivingApplicationAPI(session);
        } catch (final BonitaException e) {
            throw new LoginFailedException(e);
        }
        final SecurityProperties securityProperties = PropertiesFactory.getSecurityProperties(session.getTenantId());
        reloadPropertiesIfInDebug(securityProperties, new PlatformManagementUtils());
        final CustomPermissionsMapping customPermissionsMapping = PropertiesFactory.getCustomPermissionsMapping(session.getTenantId());
        final CompoundPermissionsMapping compoundPermissionsMapping = PropertiesFactory.getCompoundPermissionsMapping(session.getTenantId());
        return new PermissionsBuilder(session, profileAPI, applicationAPI, customPermissionsMapping, compoundPermissionsMapping, securityProperties);
    }

    static void reloadPropertiesIfInDebug(SecurityProperties securityProperties, PlatformManagementUtils platformManagementUtils) throws LoginFailedException {
        if (securityProperties.isAPIAuthorizationsCheckInDebugMode()) {
            try {
                platformManagementUtils.initializePlatformConfiguration();
            } catch (BonitaException | IOException e) {
                throw new LoginFailedException("Properties are in debug mode, unable to reload configuration", e);
            }
        }
    }
}
