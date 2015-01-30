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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageDefinition;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormSubmitButton;

/**
 * @author Julien Mege
 */
public class DeleteCustomPage extends Page {

    public static final String TOKEN = "deletepageadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    private FormSubmitButton deleteButton;

    private final ArrayList<String> idsAsString;

    private boolean firstApplicationNotFound = true;

    static {
        PRIVILEGES.add(PageListingPage.TOKEN);
    }

    public DeleteCustomPage(final ArrayList<String> idsAsString) {
        this.idsAsString = idsAsString;

    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Delete pages"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(new Text(getParameter("message")));
        buildform();
        if (idsAsString.size() > 1) {
            // multiple selection
            addBody(new Text(_("If these pages are used in a profile, the pages and related profile entries will be permanently deleted.")));
        } else {
            addBody(new Text(_("If this page is used in a profile, the page and related profile entries will be permanently deleted.")));
        }
        searchApplicationDependancies();
    }

    private void buildform() {
        final Form form = new Form();
        deleteButton = new FormSubmitButton(new JsId("delete"), _("Delete"), _("Delete the pages"), new Action() {

            @Override
            public void execute() {
                new APICaller<PageItem>(PageDefinition.get()).delete(idsAsString, new APICallback() {

                    @Override
                    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                        ViewController.closePopup();
                        ViewController.refreshCurrentPage();
                    }
                });

            }
        });
        deleteButton.setVisible(true);
        form.addButton(deleteButton);
        form.addCancelButton();
        setFooter(form);
    }

    private void searchApplicationDependancies() {
        for (final String pageId : idsAsString) {
            searchApplicationDependanciesForPage(pageId);
        }

    }

    private void searchApplicationDependanciesForPage(final String pageId) {
        final Map<String, String> filter = new HashMap<String, String>();
        filter.put(ApplicationPageItem.ATTRIBUTE_PAGE_ID, pageId);
        final List<String> deploys = Arrays.asList(ApplicationPageItem.ATTRIBUTE_PAGE_ID, ApplicationPageItem.ATTRIBUTE_APPLICATION_ID);

        new APICaller<ApplicationPageItem>(ApplicationPageDefinition.get()).search(
                0, Integer.MAX_VALUE, null, null, filter,
                deploys,
                new DeletePageProblemCallback());
    }

    private class DeletePageProblemCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final List<ApplicationPageItem> applicationPages = JSonItemReader.parseItems(response, ApplicationPageDefinition.get());
            if (applicationPages.size() > 0) {
                if (firstApplicationNotFound) {
                    setBody(new Text(_("No pages will be deleted as some of them are used in applications.")));
                    activateDeleteButton(false);
                    firstApplicationNotFound = false;
                }
                addBody(new DeletePageProblemsCallout(applicationPages));
            }
        }

    }

    private void activateDeleteButton(final boolean noApplicationLink) {
        deleteButton.setVisible(noApplicationLink);
    }

}

