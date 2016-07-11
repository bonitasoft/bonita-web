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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.actor.ActorNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Vincent Elcrin
 */
public class ProcessItemConverter extends ItemConverter<ProcessItem, ProcessDeploymentInfo> {

    private final ProcessAPI processApi;

    public ProcessItemConverter(final ProcessAPI processApi) {
        this.processApi = processApi;
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

        try {
            item.setActorInitiatorId(processApi.getActorInitiator(engineItem.getProcessId()).getId());
        } catch (ActorNotFoundException e) {
            item.setActorInitiatorId(-1L);
        } catch (ProcessDefinitionNotFoundException e) {
            throw new APIException(_("Process definition not found for id %processId%", new Arg("processId", String.valueOf(engineItem.getProcessId()))), e);
        }

        return item;
    }

}
