package org.bonitasoft.console.common.server.login;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerFactory;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerNotFoundException;
import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.bonitasoft.console.common.server.utils.PermissionsBuilder;
import org.bonitasoft.console.common.server.utils.PermissionsBuilderAccessor;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;


public class LoginManager {

    /**
     * default locale
     */
    private static final String DEFAULT_LOCALE = "en";

    public void login(final HttpServletRequestAccessor request, final long tenantId, final UserLogger userLoger, final Credentials credentials)
            throws AuthenticationFailedException, ServletException, LoginFailedException {
        final Map<String, Serializable> credentialsMap = getAuthenticationManager(tenantId).authenticate(request, credentials);
        APISession apiSession;
        if (credentialsMap == null || credentialsMap.isEmpty()) {
            apiSession = userLoger.doLogin(credentials);
        } else {
            apiSession = userLoger.doLogin(credentialsMap);
        }
        storeCredentials(request, apiSession);
    }

    protected AuthenticationManager getAuthenticationManager(final long tenantId) throws ServletException {
        try {
            // should really not use the static like.
            return AuthenticationManagerFactory.getAuthenticationManager(tenantId);
        } catch (final AuthenticationManagerNotFoundException e) {
            throw new ServletException(e);
        }
    }

    protected void storeCredentials(final HttpServletRequestAccessor request, final APISession session) throws LoginFailedException {
        String local = DEFAULT_LOCALE;
        if (request.getParameterMap().get("_l") != null
                && request.getParameterMap().get("_l").length >= 0) {
            local = request.getParameterMap().get("_l")[0];
        }
        final User user = new User(request.getUsername(), local);
        final PermissionsBuilder permissionsBuilder = createPermissionsBuilder(session);
        final Set<String> permissions = permissionsBuilder.getPermissions();
        SessionUtil.sessionLogin(user, session, permissions, request.getHttpSession());
    }

    protected PermissionsBuilder createPermissionsBuilder(final APISession session) throws LoginFailedException {
        return PermissionsBuilderAccessor.createPermissionBuilder(session);
    }
}
