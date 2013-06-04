/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.platform.client;

import org.bonitasoft.platform.client.monitoring.model.JvmDynamicMonitoringDefinition;
import org.bonitasoft.platform.client.monitoring.model.JvmStaticMonitoringDefinition;
import org.bonitasoft.platform.client.monitoring.model.SystemPropertyDefinition;
import org.bonitasoft.platform.client.platform.model.PlatformDefinition;
import org.bonitasoft.platform.client.tenant.model.TenantDefinition;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryCommon;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Haojie Yuan
 */
public class PlatformFactoryCommon extends ApplicationFactoryCommon {

    @Override
    public ItemDefinition defineItemDefinitions(final String token) {
        if (TenantDefinition.TOKEN.equals(token)) {
            return new TenantDefinition();
        } else if (PlatformDefinition.TOKEN.equals(token)) {
            return new PlatformDefinition();
        } else if (JvmDynamicMonitoringDefinition.TOKEN.equals(token)) {
            return new JvmDynamicMonitoringDefinition();
        } else if (JvmStaticMonitoringDefinition.TOKEN.equals(token)) {
            return new JvmStaticMonitoringDefinition();
        } else if (SystemPropertyDefinition.TOKEN.equals(token)) {
            return new SystemPropertyDefinition();
        } else if (SessionDefinition.TOKEN.equals(token)) {
            return new SessionDefinition();
        } else {
            return null;
        }
    }
}
