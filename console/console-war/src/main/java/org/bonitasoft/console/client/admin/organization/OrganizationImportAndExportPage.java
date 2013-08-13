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
package org.bonitasoft.console.client.admin.organization;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.FileExtensionAllowedValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.XmlUploadFilter;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Colin PUy
 */
public class OrganizationImportAndExportPage extends Page {

    public static final String TOKEN = "importexportorganization";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(OrganizationImportAndExportPage.TOKEN);
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Import / Export"));
    }

    @Override
    public void buildView() {
        addBody(importSection());
        addBody(exportSection());
    }

    private Section importSection() {
        final Section section = new Section(new JsId("import"), _("Import an existing organization"));
        section.addBody(newWarningCell());
        section.addBody(importContainer());
        return section;
    }

    private ContainerStyled<Component> importContainer() {
        final ContainerStyled<Component> container = new ContainerStyled<Component>();
        container.addClass("cell");
        container.append(new Paragraph(_("This will import a file containing your whole organization data.")));
        container.append(new Paragraph(_("Be careful, your organization will be merged with existing data.")));
        container.append(new Paragraph(_("In case of merge, the priority is given to the file")));
        container.append(uploadForm());
        return container;
    }

    private Form uploadForm() {
        Form uploadForm = new Form(new JsId("identityImportForm"));
        JsId fileJsId = new JsId("organizationDataUpload");
        uploadForm.addFileEntry(fileJsId, _("Organisation file"), _("Please choose a file to import"), 
                GWT.getModuleBaseURL() + "organizationUpload");
        
        ((FileUpload) uploadForm.getEntry(fileJsId)).addFilter(new XmlUploadFilter());
        uploadForm.getEntry(fileJsId).addValidator(new MandatoryValidator(_("Please select a file first")));
        uploadForm.getEntry(fileJsId).addValidator(new FileExtensionAllowedValidator("xml"));
        
        uploadForm.addButton(new JsId("uploadData"), _("Import"), _("Import"), new OrganisationImportAction());
        return uploadForm;
    }

    private Section exportSection() {
        final Section section = new Section(new JsId("export"), _("Export the installed organization"));
        section.addBody(newWarningCell());
        section.addBody(exportContainer());
        return section;
    }

    private ContainerStyled<Component> exportContainer() {
        final ContainerStyled<Component> container = new ContainerStyled<Component>();
        container.addClass("cell");
        container.append(new Paragraph(_("This will export a file containing your whole organization data. ")));
        container.append(new Paragraph(_("Data included are : Users, groups and roles")));
        container.append(exportButton());
        return container;
    }

    private Clickable exportButton() {
        return new ButtonAction("btn-export", _("Export"), _("Export the installed organization"),
                new Url(GWT.getModuleBaseURL() + "exportOrganization"));
    }

    private ContainerStyled<Component> newWarningCell() {
        final ContainerStyled<Component> cellWarning = new ContainerStyled<Component>();
        cellWarning.addClass("cell");
        cellWarning.addClass("warning");
        cellWarning.append(new Text(_("Warning:")));
        return cellWarning;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
