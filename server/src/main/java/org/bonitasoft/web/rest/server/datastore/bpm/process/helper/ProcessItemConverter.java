/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.process.helper;

import org.bonitasoft.console.common.server.utils.TenantCacheUtil;
import org.bonitasoft.console.common.server.utils.TenantCacheUtilFactory;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.actor.ActorNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Vincent Elcrin
 */
public class ProcessItemConverter extends ItemConverter<ProcessItem, ProcessDeploymentInfo> {

    private final ProcessAPI processApi;
    private final Long tenantId;

    public ProcessItemConverter(final ProcessAPI processApi, final Long tenantId) {
        this.processApi = processApi;
        this.tenantId = tenantId;
    }

    @Override
    public ProcessItem convert(ProcessDeploymentInfo engineItem) {

        final ProcessItem item = new ProcessItem();
        item.setId(engineItem.getProcessId());
        item.setName(engineItem.getName());
        item.setVersion(engineItem.getVersion());
        item.setDescription(engineItem.getDescription());
        item.setDeployedByUserId(engineItem.getDeployedBy());
        item.setDeploymentDate(engineItem.getDeploymentDate());
        item.setActivationState(engineItem.getActivationState().name());
        item.setConfigurationState(engineItem.getConfigurationState().name());
        item.setDisplayName(engineItem.getDisplayName());
        item.setDisplayDescription(engineItem.getDisplayDescription());
        item.setLastUpdateDate(engineItem.getLastUpdateDate());
        item.setActorInitiatorId(getActorInitiator(engineItem));

        return item;
    }

    private Long getActorInitiator(ProcessDeploymentInfo engineItem) {
        TenantCacheUtil tenantCacheUtil = TenantCacheUtilFactory.getTenantCacheUtil(tenantId);
        Long actorInitiatorId = tenantCacheUtil.getProcessActorInitiatorId(engineItem.getProcessId());
        if (actorInitiatorId == null) {
            actorInitiatorId = tenantCacheUtil.storeProcessActorInitiatorId(engineItem.getProcessId(), getActorInitiatorFromEngine(engineItem));
        }
        return actorInitiatorId;
    }

    private Long getActorInitiatorFromEngine(ProcessDeploymentInfo engineItem) {
        Long actorInitiatorId;
        try {
            actorInitiatorId = processApi.getActorInitiator(engineItem.getProcessId()).getId();
        } catch (ActorNotFoundException e) {
            actorInitiatorId = -1L;
        } catch (ProcessDefinitionNotFoundException e) {
            throw new APIException(_("Process definition not found for id %processId%", new Arg("processId", String.valueOf(engineItem.getProcessId()))), e);
        }
        return actorInitiatorId;
    }

}
