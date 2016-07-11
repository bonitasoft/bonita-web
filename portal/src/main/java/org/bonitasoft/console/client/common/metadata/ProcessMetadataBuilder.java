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
package org.bonitasoft.console.client.common.metadata;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Colin PUY
 * 
 */
public class ProcessMetadataBuilder extends MetadataBuilder {

    public void addVersion() {
        add(version());
    }

    private ItemDetailsMetadata version() {
        return new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_VERSION, _("Version"), _("The version of the process"));
    }

    public void addConfigurationState() {
        add(configurationState());
    }

    private ItemDetailsMetadata configurationState() {
        return new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE, _("Configuration state "), _("The configuration state of the process"));
    }

    public void addLastUpdateDate() {
        add(lastUpdateDate());
    }

    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Last updated on"), _("The date when the process was updated"));
    }

    public void addActivationState() {
        add(activationState());
    }

    private ItemDetailsMetadata activationState() {
        return new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_ACTIVATION_STATE, _("Activation state"), _("The state of the process"));
    }
    
    public void addInstalledDate() {
        add(installedDate());
    }
    
    private ItemDetailsMetadata installedDate() {
        return new ItemDetailsMetadata(new DateAttributeReader(ProcessItem.ATTRIBUTE_DEPLOYMENT_DATE), _("Installed on"),
                _("The date when this process has been installed"));
    }
    
    public void addInstalledBy() {
        add(installedBy());
    }
    
    private ItemDetailsMetadata installedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID),
                _("Installed by"), _("The user that has uploaded this process"));
    }
}
