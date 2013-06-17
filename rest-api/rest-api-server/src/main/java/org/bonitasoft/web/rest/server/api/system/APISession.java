/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.system;

import java.util.List;

import org.bonitasoft.web.rest.server.api.CommonAPI;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.common.session.SessionItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.session.exception.SessionException;

/**
 * @author Julien Mege
 */
public class APISession extends CommonAPI<SessionItem> {

    public static final String APISESSION = "apiSession";

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(SessionDefinition.TOKEN);
    }

    @Override
    public SessionItem get(final APIID unusedId) {

        final org.bonitasoft.engine.session.APISession apiSession = getEngineSession();
        final SessionItem session = new SessionItem();
        if (apiSession != null) {

            try {
                session.setAttribute(SessionItem.ATTRIBUTE_SESSIONID, String.valueOf(apiSession.getId()));
                session.setAttribute(SessionItem.ATTRIBUTE_USERID, String.valueOf(apiSession.getUserId()));
                session.setAttribute(SessionItem.ATTRIBUTE_USERNAME, apiSession.getUserName());
                session.setAttribute(SessionItem.ATTRIBUTE_IS_TECHNICAL_USER, String.valueOf(apiSession.isTechnicalUser()));
            } catch (final Exception e) {
                throw new APIException(new SessionException(e.getMessage(), e));
            }
        }

        return session;
    }

    @Override
    protected void fillDeploys(final SessionItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final SessionItem item, final List<String> counters) {
    }
}
