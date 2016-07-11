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
import static org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader.DEFAULT_FORMAT;

import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.console.client.uib.SafeHtmlParser;
import org.bonitasoft.web.rest.model.identity.AbstractContactDataItem;
import org.bonitasoft.web.rest.model.identity.ProfessionalContactDataItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.CompoundAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Element;

/**
 * @author Colin PUY
 * 
 */
public class UserMetadataBuilder extends MetadataBuilder {

    public interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<span><a href='mailto:{0}'>{0}</a></span>")
        SafeHtml email(String email);
    }
    
    public static Templates TEMPLATES = GWT.create(Templates.class);
    
    public void addFirstName() {
        add(firstName());
    }

    private ItemDetailsMetadata firstName() {
        return new ItemDetailsMetadata(UserItem.ATTRIBUTE_FIRSTNAME, _("First name"), _("First name of the user"));
    }

    public void addLastName() {
        add(lastName());
    }

    private ItemDetailsMetadata lastName() {
        return new ItemDetailsMetadata(UserItem.ATTRIBUTE_LASTNAME, _("Last name"), _("Last name of the user"));
    }

    public void addLastUpdateDate() {
        add(lastUpdateDate());
    }
    
    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(
                new DateAttributeReader(UserItem.ATTRIBUTE_LAST_UPDATE_DATE),
                _("Last update"), _("The date of the last update of the user"));
    }
    
    public void addCreationDate() {
        add(creationDate());
    }

    private ItemDetailsMetadata creationDate() {
        return new ItemDetailsMetadata(UserItem.ATTRIBUTE_CREATION_DATE, _("Creation date"), _("The date of the creation of the user"));
    }

    public void addLastConnectionDate() {
        add(lastConnectionDate(DEFAULT_FORMAT));
    }
    
    public void addLastConnectionDate(FORMAT format) {
        add(lastConnectionDate(format));
    }

    private ItemDetailsMetadata lastConnectionDate(FORMAT format) {
        return new ItemDetailsMetadata(
                new DateAttributeReader(UserItem.ATTRIBUTE_LAST_CONNECTION_DATE, format)
                        .setDefaultValue(_("No data")),
                _("Last login"), _("The date of the last connection of the user"));
    }

    public void addEmail(AbstractContactDataItem contact) {
        add(eMail(contact));
    }

    private ItemDetailsMetadata eMail(AbstractContactDataItem contact) {
        String email = contact.getEmail() == null ? _("No data") : contact.getEmail(); 
        SpanElement span = SpanElement.as(Element.as(SafeHtmlParser.parseFirst(TEMPLATES.email(email))));
        Definition definition = new Definition(_("Email") + ": ", new Html(span));
        definition.addClass("email");
        return new ItemDetailsMetadata(
                new DeployedAttributeReader(UserItem.DEPLOY_PROFESSIONAL_DATA, ProfessionalContactDataItem.ATTRIBUTE_EMAIL), 
                new Html(definition.getElement()));
    }

    public void addUserName() {
        add(userName());
    }

    private ItemDetailsMetadata userName() {
        return new ItemDetailsMetadata(
                new AttributeReader(UserItem.ATTRIBUTE_USERNAME),
                _("Username"), _("Username of the user"));
    }

    public void addJobTitle() {
        add(jobTitle());
    }
    
    private ItemDetailsMetadata jobTitle() {
        return new ItemDetailsMetadata(
                new AttributeReader(UserItem.ATTRIBUTE_JOB_TITLE),
                _("Job title"), _("Job title of the user"));
    }

    public void addManager() {
        add(manager());
    }

    private ItemDetailsMetadata manager() {
        return new ItemDetailsMetadata(
                new CompoundAttributeReader(UserItem.ATTRIBUTE_ID, _("%user%"))
                        .addReader("user", new DeployedUserReader(UserItem.ATTRIBUTE_MANAGER_ID)),
                _("Manager"), _("Manager of the specified user"));
    }
}
