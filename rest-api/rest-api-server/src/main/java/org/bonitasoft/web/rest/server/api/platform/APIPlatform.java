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
package org.bonitasoft.web.rest.server.api.platform;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.DateUtil;
import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.platform.Platform;
import org.bonitasoft.engine.platform.PlatformNotFoundException;
import org.bonitasoft.engine.platform.PlatformState;
import org.bonitasoft.engine.platform.StartNodeException;
import org.bonitasoft.web.rest.api.model.platform.PlatformDefinition;
import org.bonitasoft.web.rest.api.model.platform.PlatformItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 *         this class might be transform in NON-REST API
 */
public class APIPlatform extends org.bonitasoft.web.rest.server.api.PlatformAPI<PlatformItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(PlatformDefinition.TOKEN);
    }

    @Override
    public PlatformItem add(final PlatformItem item) {
        try {
            final PlatformAPI platformAPI = getPlatformAPI();
            if (!platformAPI.isPlatformCreated()) {
                platformAPI.createAndInitializePlatform();
            }
            return get(null);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public PlatformItem update(final APIID id, final Map<String, String> attributes) {
        final String platformState = attributes.get(PlatformItem.ATTRIBUTE_STATE);
        try {
            final PlatformAPI platformAPI = getPlatformAPI();
            if (platformAPI.isPlatformCreated()) {
                try {
                    if (platformState.equals("start")) {
                        platformAPI.startNode();
                    } else if (platformState.equals("stop")) {
                        platformAPI.stopNode();
                    }
                } catch (final StartNodeException sne) {
                    throw new APIException(sne);
                }
            }
            return get(null);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public PlatformItem get(final APIID id) {
        PlatformItem clientItem = null;
        try {
            final PlatformAPI platformAPI = getPlatformAPI();
            final Platform platform = platformAPI.getPlatform();// engine team have to clean it
            final String createdDate = DateUtil.convertLongToDate(platform.getCreated());
            final PlatformState platformState = platformAPI.getPlatformState();
            String platformStateStr = "";
            if (platformState != null) {
                platformStateStr = platformState.toString();
            }
            clientItem = new PlatformItem(platform.getVersion(), platform.getPreviousVersion(), platform.getInitialVersion(), createdDate,
                    platform.getCreatedBy(), platformStateStr);
            return clientItem;
        } catch (final PlatformNotFoundException ex) {
            clientItem = new PlatformItem();
            return clientItem;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private PlatformAPI getPlatformAPI() {
        try {
            return PlatformAPIAccessor.getPlatformAPI(getSession());
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            final PlatformAPI platformAPI = getPlatformAPI();
            if (platformAPI.isPlatformCreated()) {
                platformAPI.cleanAndDeletePlaftorm();
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected void fillDeploys(final PlatformItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final PlatformItem item, final List<String> counters) {
    }

}
