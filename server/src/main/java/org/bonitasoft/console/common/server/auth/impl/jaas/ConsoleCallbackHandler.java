/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.auth.impl.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Console call back handler
 * 
 * @author Qixiang Zhang
 * 
 */
public class ConsoleCallbackHandler implements CallbackHandler {

    /**
     * User name
     */
    private final String name;

    /**
     * User password
     */
    private final String password;

    /**
     * User of tenant id
     */
    private final String tenantId;

    /**
     * 
     * Default Constructor.
     * 
     * @param name
     *            user name
     * @param password
     *            user password
     * @param tenantId
     *            tenant id
     */
    public ConsoleCallbackHandler(final String name, final String password, final String tenantId) {
        this.name = name;
        this.password = password;
        this.tenantId = tenantId;
    }

    /*
     * (non-Javadoc)
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (final Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                final NameCallback nc = (NameCallback) callback;
                nc.setName(this.name);
            } else if (callback instanceof PasswordCallback) {
                final PasswordCallback pc = (PasswordCallback) callback;
                pc.setPassword(this.password.toCharArray());
            } else if (callback instanceof TextInputCallback) {
                final TextInputCallback tc = (TextInputCallback) callback;
                tc.setText(this.tenantId);
            }

        }
    }

}
