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
package org.bonitasoft.console.common.server.login.localization;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.console.common.server.login.LoginManager;

/**
 * @author Vincent Elcrin
 * 
 */
public class RedirectUrlBuilder {

    abstract class AppendParameter {

        public abstract void append(UrlBuilder urlBuilder, String key, UrlValue urlValue);
    }

    private final UrlBuilder urlBuilder;

    private final List<String> blackList = Arrays.asList(
            LoginManager.REDIRECT_URL
            );

    public RedirectUrlBuilder(final String redirectUrl) {
        urlBuilder = createBuilder(redirectUrl != null ? redirectUrl : "");
    }

    public RedirectUrlBuilder appendParameters(final Map<String, String[]> parameters) {
        appendParameters(parameters, new AppendParameter() {

            @Override
            public void append(final UrlBuilder urlBuilder, final String key, final UrlValue urlValue) {
                urlBuilder.appendParameter(key, urlValue);
            }
        });
        return this;
    }

    public RedirectUrl build() {
        return new RedirectUrl(urlBuilder.build());

    }

    public void appendParameters(final Map<String, String[]> parameters, final AppendParameter appender) {
        final Iterator<Entry<String, String[]>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<String, String[]> next = iterator.next();
            if (!isBlackListed(next.getKey())) {
                appender.append(urlBuilder, next.getKey(), new UrlValue(next.getValue()));
            }
        }
    }

    private boolean isBlackListed(final String key) {
        return blackList.contains(key);
    }

    private UrlBuilder createBuilder(final String baseUrl) {
        return new UrlBuilder(baseUrl);
    }
}
