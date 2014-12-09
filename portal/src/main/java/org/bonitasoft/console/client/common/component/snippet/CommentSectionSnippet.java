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
package org.bonitasoft.console.client.common.component.snippet;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.owner.comment.action.AddCommentAction;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCommentDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CommentDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CommentItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Text;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ImageCellFormater;

/**
 * @author Vincent Elcrin
 * 
 */
public class CommentSectionSnippet implements SectionSnippet {

    private int nbLinesByPage = 10;

    private final APIID caseId;

    private final ArchivedCaseDiscovery archivedCaseDiscovery;

    /**
     * Request the type of case (archived or not)
     * before to build the section asynchronously.
     */
    public CommentSectionSnippet(final APIID caseId) {
        this.caseId = caseId;
        archivedCaseDiscovery = new AsynchronousArchivedCaseDiscovery(caseId);
    }

    /**
     * Build archived or not archived section.
     */
    public CommentSectionSnippet(final APIID caseId, boolean archived) {
        this.caseId = caseId;
        archivedCaseDiscovery = new ArchivedCaseDiscovery(archived);
    }

    @Override
    public Section build() {
        final Section commentSection = new Section(_("Comments"));
        commentSection.setId(CssId.SECTION_COMMENT);
        commentSection.addClass(CssClass.SECTION_TYPE_COMMENT);
        archivedCaseDiscovery.isArchived(new ArchiveDiscoveryCallback() {

            @Override
            public void isArchived(boolean archived) {
                buildSection(commentSection, archived);
            }
        });
        return commentSection;
    }

    public void buildSection(Section section, boolean archived) {
        ItemTable table = createCommentTable(getDefinition(archived),
                caseId);
        section.addBody(table);
        if (!archived) {
            section.addBody(createSubmitionForm(caseId,
                    new AddCommentAction(table)));
        }
        /*
         * Need to refresh manually as the section
         * can be built asynchronously
         */
        table.refresh();
    }

    private ItemDefinition<?> getDefinition(boolean archived) {
        return archived
                ? ArchivedCommentDefinition.get()
                : CommentDefinition.get();
    }

    public int getNbLinesByPage() {
        return this.nbLinesByPage;
    }

    public CommentSectionSnippet setNbLinesByPage(final int nbLinesByPage) {
        this.nbLinesByPage = nbLinesByPage;
        return this;
    }

    private ItemTable createCommentTable(final ItemDefinition<?> itemDefinition,
            final APIID caseId) {
        final ItemTable table = getCommentTable(itemDefinition, caseId);
        table.setNbLinesByPage(getNbLinesByPage());
        table.setView(VIEW_TYPE.VIEW_LIST);
        return table;
    }

    private Form createSubmitionForm(final APIID caseId, Action action) {
        final Form form = new Form(new JsId("commentForm"))
                .addHiddenEntry(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, caseId.toString())
                .addFormEntry(createTextField())
                .addButton(new JsId("addcommentbutton"), _("Add Comment"), _("Add a comment"), action);
        return form;
    }

    private Text createTextField() {
        Text comment = new Text(new JsId("content"), _("Discuss :"), _("Type new comment"));
        comment.setPlaceholder(_("Type new comment"));
        comment.setMaxLength(200L);
        return comment;
    }

    private ItemTable getCommentTable(final ItemDefinition<?> itemDefinition, final APIID caseId) {
        return new ItemTable(new JsId("comments"), itemDefinition)
                // filter
                .addHiddenFilter(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, caseId.toString())
                // columns
                .addColumn(new DeployedAttributeReader(CommentItem.ATTRIBUTE_USER_ID,
                        UserItem.ATTRIBUTE_ICON), _("Icon"))
                .addColumn(new DeployedAttributeReader(CommentItem.ATTRIBUTE_USER_ID,
                        UserItem.ATTRIBUTE_USERNAME), _("User name"))
                .addColumn(CommentItem.ATTRIBUTE_CONTENT, _("Content"))
                .addColumn(CommentItem.ATTRIBUTE_POST_DATE, _("Post time"))
                // order
                .setOrder(CommentItem.ATTRIBUTE_POST_DATE, false)
                // cell formatter
                .addCellFormatter(CommentItem.ATTRIBUTE_USER_ID + "_" + UserItem.ATTRIBUTE_ICON, new ImageCellFormater(UserItem.DEFAULT_USER_ICON));
    }

}
