/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.page.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import org.bonitasoft.console.client.admin.profile.view.ProfileListingPage;
import org.bonitasoft.console.client.common.metadata.PageMetadataBuilder;
import org.bonitasoft.engine.page.ContentType;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.SHA1;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.NameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

/**
 * @author Fabio Lombardi
 */
public class PageQuickDetailsPage extends ItemQuickDetailsPage<PageItem> {

    public static final String TOKEN = "pagequickdetails";

    private static final String PAGE_ID_PARAM = "id";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(PageListingPage.TOKEN);
    }

    public PageQuickDetailsPage() {
        super(PageDefinition.get());
    }

    @Override
    protected List<String> defineDeploys() {
        return asList(PageItem.ATTRIBUTE_CREATED_BY_USER_ID, PageItem.ATTRIBUTE_UPDATED_BY_USER_ID);

    }

    @Override
    protected void defineTitle(final PageItem page) {
        setTitle(_(new NameAttributeReader().read(page)));
        addDescription(_(new DescriptionAttributeReader().read(page)));
    }

    @Override
    protected void buildToolbar(final PageItem page) {
        final ButtonAction editButton = new ButtonAction("editpage", _("Edit"), _("Edit selected page"), new CheckValidSessionBeforeAction(new ActionShowPopup(
                new EditCustomPage(page.getId(), page.getDisplayName()))));
        editButton.setEnabled(!page.isProvided());
        addToolbarLink(editButton);
        addToolbarLink(new ButtonAction("exportpage", _("Export"), _("Click to export this page"), new Url(GWT.getModuleBaseURL() + "pageDownload?"
                + PAGE_ID_PARAM + "=" + page.getId())));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final PageItem page) {
        final PageMetadataBuilder metadatas = new PageMetadataBuilder();
        if (!page.isProvided()) {
            metadatas.addInstalledDate();
            metadatas.addLastUpdateDate();
            metadatas.addInstalledBy();
            metadatas.addUpdatedBy();
        }
        return metadatas.build();
    }

    @Override
    protected void buildBody(final PageItem page) {
        addBody(nameForUrlSection(page));
        addBody(contentSection(page));
        addBody(visibleToSection(page));
    }

    protected Section nameForUrlSection(final PageItem page) {
        final Section section = new Section(_("Name for the URL")).addBody(new Paragraph(page.getUrlToken()));
        section.addClass("nameForUrl");
        section.setId(CssId.QD_SECTION_NAMEFORUL);
        return section;
    }

    private AbstractComponent contentSection(final PageItem page) {
        final Section contentSection;
        if (page.isProvided()) {
            contentSection = new Section(_("More information"));
            if (ContentType.PAGE.equals(page.getContentType())) {
                contentSection.addBody(
                        contentTypeParagraph(
                                _("Page"),
                                _("Content type is 'page'."),
                                _("To view an imported page, add it to an application page list and navigation.")));
            }
            if (ContentType.FORM.equals(page.getContentType())) {
                contentSection
                        .addBody(
                        contentTypeParagraph(
                                _("Form"),
                                _("Content type is 'form'."),
                                _("A form is a page mapped to case start or a human task."),
                                _("Create a form from the contract in Bonita BPM Studio."),
                                _("By default, forms are included in the process bar file for deployment."),
                                _("Subscription edition: To replace a form in a deployed process, upload the form zip and update the form mapping in the Portal process view."),
                                _("To share a form between processes, upload the zip and map it to the relevant processes and tasks.")));

            }
            if (ContentType.LAYOUT.equals(page.getContentType())) {
                contentSection.addBody(
                        contentTypeParagraph(
                                _("Layout"),
                                _("Content type is 'layout'.")));
            }
            if (ContentType.THEME.equals(page.getContentType())) {
                contentSection.addBody(
                        contentTypeParagraph(
                                _("Theme"),
                                _("Content type is 'theme'."),
                                _("The index file must be present in the zip but is ignored, so can be empty.")));

            }
            if (ContentType.API_EXTENSION.equals(page.getContentType())) {
                contentSection.addBody(
                        contentTypeParagraph(
                                _("Rest API extension"),
                                _("Content type is 'apiExtension'.")));
            }
        } else {
            contentSection = new Section(_("Content")).addBody(new Paragraph(page.getContentName()));
            contentSection.addClass("content");
        }
        contentSection.setId(CssId.QD_SECTION_PAGE_CONTENT);
        return contentSection;
    }

    private Html contentTypeParagraph(String contentType, String contentTypeDescription, String... contentTypeParagraphs) {
        StringBuilder htmlBuilder = new StringBuilder();
        appendParagraph(htmlBuilder, _("A resource is imported as a zip archive containing a page.properties file and a resources folder."));
        appendParagraph(htmlBuilder,
                _("The resources folder must contain an Index.groovy class or an index.html file and  optionally can contain some additional resources."));
        appendParagraph(htmlBuilder, _("The content type is defined in page.properties."));
        appendParagraph(htmlBuilder, _("If you create a resource with the UI designer, the exported zip automatically has the correct format."));

        htmlBuilder.append("<br>");

        htmlBuilder.append("<p>").append("<label>").append(contentType).append(":</label>").append(contentTypeDescription).append("</p>");
        for (String paragraph : contentTypeParagraphs) {
            appendParagraph(htmlBuilder, paragraph);
        }

        return new Html(htmlBuilder.toString());
    }

    private void appendParagraph(StringBuilder htmlBuilder, String paragraph) {
        htmlBuilder.append("<p>").append(paragraph).append("</p>");
    }

    private AbstractComponent visibleToSection(final PageItem page) {
        final Section visibleToSection = new Section(_("Visible to"), profileTable(page).setView(VIEW_TYPE.VIEW_LIST));

        if (!page.isProvided()) {
            visibleToSection.addBody(new Html("<p>"
                    + _("Users with the app above can see this page.") + "</p>"));
        }
        visibleToSection.setId(CssId.QD_SECTION_PAGE_VISIBLETO);
        visibleToSection.addClass("visibleTo");
        return visibleToSection;
    }

    protected ItemTable profileTable(final PageItem item) {
        return new ItemTable(ProfileEntryDefinition.get())
                .addColumn(new DeployedAttributeReader(ProfileEntryItem.ATTRIBUTE_PROFILE_ID, ProfileItem.ATTRIBUTE_NAME), _("Profile"), true, true)
                .addColumn(new MenuNameAttributeReader(ProfileEntryItem.ATTRIBUTE_PARENT_ID, ProfileEntryItem.ATTRIBUTE_NAME), _("Menu name"))
                .addHiddenFilter(ProfileEntryItem.ATTRIBUTE_PAGE, item.getUrlToken())
                .setShowSearch(false);
    }

    protected String hasRightToAccessProfilePage() {
        return new String(SHA1.calcSHA1(ProfileListingPage.TOKEN.concat(new String(Session.getParameter("session_id"))))).toUpperCase();
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

}
