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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.*;

import org.bonitasoft.console.client.common.view.StartedByDelegateAttributeReder;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Vincent Elcrin
 * 
 */
public class MetadataCaseBuilder extends MetadataBuilder {

    public MetadataCaseBuilder addAppsVersion() {
        add(createAppsVersion());
        return this;
    }
    
    private ItemDetailsMetadata createAppsVersion() {
        return new ItemDetailsMetadata(new DeployedAttributeReader(CaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_VERSION), 
                _("Process version"),  _("The version of the process that created this case"));
    }

    public MetadataCaseBuilder addStartDate() {
        add(createStartDate());
        return this;
    }
    
    private ItemDetailsMetadata createStartDate() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_START_DATE, _("Started on"), 
                _("The date while the case has been started"));
    }
    
    public MetadataCaseBuilder addStartedBy(CaseItem item) {
        if (item.getStartedByUserId() == null || item.getStartedBySubstituteUserId() == null
                || item.getStartedByUserId().toLong().equals(item.getStartedBySubstituteUserId().toLong())) {
            add(createStartedBy());
        } else {
            add(addStartedBySubstitute(item.getStartedByUser(), item.getStartedBySubstituteUser()));
        }
        return this;
    }

    private ItemDetailsMetadata createStartedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID),
                _("Started by"), _("The user that has started this case"));
    }
    
    private ItemDetailsMetadata addStartedBySubstitute(UserItem executedByUser, UserItem startedBySubstituteUser) {
        StartedByDelegateAttributeReder attributeReader = new StartedByDelegateAttributeReder(CaseItem.ATTRIBUTE_STARTED_BY_SUBSTITUTE_USER_ID);
        attributeReader.setStartedBySubstitute(startedBySubstituteUser);
        attributeReader.setStartedBy(executedByUser);
        return new ItemDetailsMetadata(attributeReader,
                _("Started by"),
                _("Name of the user who started this case"));
    }


    public MetadataCaseBuilder addState() {
        add(createState());
        return this;
    }

    private ItemDetailsMetadata createState() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_STATE, _("State"), _("The state of the case"));
    }
    
    public MetadataCaseBuilder addLastUpdateDate() {
        add(createLastUpdateDate());
        return this;
    }
    
    private ItemDetailsMetadata createLastUpdateDate() {
        return new ItemDetailsMetadata(ArchivedCaseItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Last updated"),
                _("The date when the case was updated"));
    }
}
