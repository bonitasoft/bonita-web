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

import org.bonitasoft.web.rest.model.application.ApplicationDefinition;
import org.bonitasoft.web.rest.model.application.ApplicationItem;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageDefinition;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.RequestBuilder;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutWarning;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormSubmitButton;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Julien Mege
 */
public class DeleteCustomPage extends Page {

    public static final String TOKEN = "deletepageadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    private FormSubmitButton deleteButton;

    private final ArrayList<String> idsAsString;

    private boolean firstPageLinkNotFound = true;

    private boolean firstFormNotFound = true;

    private int pagesLeft;

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
        searchApplicationDependencies();
        searchFormMappingDependencies();
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

    private void searchApplicationDependencies() {
        for (final String pageId : idsAsString) {
            searchApplicationDependenciesForPage(pageId);
            searchApplicationLayoutDependanciesForPage(pageId);
            searchApplicationThemeDependanciesForPage(pageId);
        }
    }

    private void searchApplicationDependenciesForPage(final String pageId) {
        final Map<String, String> filter = new HashMap<String, String>();
        filter.put(ApplicationPageItem.ATTRIBUTE_PAGE_ID, pageId);
        final List<String> deploys = Arrays.asList(ApplicationPageItem.ATTRIBUTE_PAGE_ID, ApplicationPageItem.ATTRIBUTE_APPLICATION_ID);

        new APICaller<ApplicationPageItem>(ApplicationPageDefinition.get()).search(
                0, Integer.MAX_VALUE, null, null, filter,
                deploys,
                new DeletePageProblemCallback());
    }

    private void searchApplicationLayoutDependanciesForPage(final String pageId) {
        final Map<String, String> filter = new HashMap<String, String>();
        filter.put(ApplicationItem.ATTRIBUTE_LAYOUT_ID, pageId);
        final List<String> deploys = Arrays.asList(ApplicationItem.ATTRIBUTE_LAYOUT_ID);

        new APICaller<ApplicationItem>(ApplicationDefinition.get()).search(
                0, Integer.MAX_VALUE, null, null, filter,
                deploys,
                new DeleteApplicationLayoutCallback());
    }

    private void searchApplicationThemeDependanciesForPage(final String pageId) {
        final Map<String, String> filter = new HashMap<String, String>();
        filter.put(ApplicationItem.ATTRIBUTE_THEME_ID, pageId);
        final List<String> deploys = Arrays.asList(ApplicationItem.ATTRIBUTE_THEME_ID);

        new APICaller<ApplicationItem>(ApplicationDefinition.get()).search(
                0, Integer.MAX_VALUE, null, null, filter,
                deploys,
                new DeleteApplicationThemeCallback());
    }

    private class DeletePageProblemCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final List<ApplicationPageItem> applicationPages = JSonItemReader.parseItems(response, ApplicationPageDefinition.get());
            if (applicationPages.size() > 0) {
                addInfoMessageOnApplicationPageLinkFound();
                addBody(new DeletePageProblemsCallout(applicationPages));
            }
        }

    }

    private void addInfoMessageOnApplicationPageLinkFound() {
        if (firstPageLinkNotFound) {
            setBody(new Text(_("These pages cannot be deleted because at least one is used in an application. You must remove a page from an application before you can delete it.")));
            activateDeleteButton(false);
            firstPageLinkNotFound = false;
        }
    }

    private class DeleteApplicationLayoutCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final List<ApplicationItem> applications = JSonItemReader.parseItems(response, ApplicationDefinition.get());
            if (applications.size() > 0) {
                addInfoMessageOnApplicationPageLinkFound();
                addBody(new DeleteApplicationLayoutProblemsCallout(applications));
            }
        }

    }

    private class DeleteApplicationThemeCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final List<ApplicationItem> applications = JSonItemReader.parseItems(response, ApplicationDefinition.get());
            if (applications.size() > 0) {
                addInfoMessageOnApplicationPageLinkFound();
                addBody(new DeleteApplicationThemeProblemsCallout(applications));
            }
        }

    }

    private void searchFormMappingDependencies() {
        //pagesLeft = 0;
        for (final String pageId : idsAsString) {
            searchFormMappingDependenciesForPage(pageId);
        }
    }

    private void searchFormMappingDependenciesForPage(final String pageId) {
        final Map<String, String> filter = new HashMap<String, String>();
        filter.put(ApplicationPageItem.ATTRIBUTE_PAGE_ID, pageId);
        final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "../API/form/mapping?c=10&p=0&f=pageId=" + pageId);
        requestBuilder.setCallback(new DeletePageProblemFormCallback(pageId));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            e.printStackTrace();
        }
    }

    private class DeletePageProblemFormCallback extends APICallback {
        private final String pageId;
        private final Boolean isLastPage = false;

        public DeletePageProblemFormCallback(final String pageId) {
            this.pageId = pageId;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final JSONValue root= JSONParser.parseLenient(response);
            final JSONArray formMappings=root.isArray();
            if (firstPageLinkNotFound) {
                if (formMappings.size()>0) {
                    if (firstFormNotFound) {
                        pagesLeft=1;
                        setBody(new Paragraph(_("Some of the pages you selected for deletion are used by processes:")));
                        firstFormNotFound = false;
                        setTitle(_("Confirm Delete?"));
                    } else {
                        pagesLeft++;
                    }
                    new APICaller<PageItem>(PageDefinition.get()).get(pageId, new GetPageNameCallback(formMappings));
                }
            }
        }

        private class GetPageNameCallback extends APICallback {
            private final JSONArray formMappings;

            public GetPageNameCallback(final JSONArray formMappings) {
                this.formMappings = formMappings;
            }

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final PageItem page = JSonItemReader.parseItem(response, PageDefinition.get());
                addBody(new DeletePageFormProblemsCallout(formMappings, page.getDisplayName()));
                if (--pagesLeft==0) {
                    addBody(new CalloutWarning(_("If you delete a page that is used by a process, the process becomes unresolved.\nBefore deleting a page, you should also check whether it is used in a custom profile navigation.")));
                    addBody(new Paragraph(_("Do you still want to delete the selected pages?")));
                }
            }
        }
    }

    private void activateDeleteButton(final boolean noApplicationLink) {
        deleteButton.setVisible(noApplicationLink);
    }

}

