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

import org.bonitasoft.console.client.admin.page.action.DeleteMultipleItemsPopupCustomPageAction;
import org.bonitasoft.engine.page.ContentType;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.I18NCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * @author Fabio Lombardi
 *
 */
public class PageListingPage extends ItemListingPage<PageItem> {

    public static final String TOKEN = "pagelisting";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(PageListingPage.TOKEN);
    }

    protected static final String TABLE_ALL_PAGES = "allpages";

    private static final String FILTER_PRIMARY_ALL_PAGES = "allpagesfilter";
    private static final String FILTER_PRIMARY_PAGES = "pages";
    private static final String FILTER_PRIMARY_FORMS = "forms";
    private static final String FILTER_PRIMARY_LAYOUT = "layout";
    private static final String FILTER_PRIMARY_THEME = "theme";
    private static final String FILTER_PRIMARY_API = "apiextension";

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(new ItemListingFilter(FILTER_PRIMARY_ALL_PAGES, _("All"), _("All pages"), TABLE_ALL_PAGES));
        filters.add(new ItemListingFilter(FILTER_PRIMARY_PAGES, _("Pages"), _("Pages"), TABLE_ALL_PAGES).addFilter(PageItem.FILTER_CONTENT_TYPE, ContentType.PAGE));
        filters.add(new ItemListingFilter(FILTER_PRIMARY_FORMS, _("Forms"), _("Forms"), TABLE_ALL_PAGES).addFilter(PageItem.FILTER_CONTENT_TYPE, ContentType.FORM));
        filters.add(new ItemListingFilter(FILTER_PRIMARY_LAYOUT, _("Layouts"), _("Layouts"), TABLE_ALL_PAGES).addFilter(PageItem.FILTER_CONTENT_TYPE, "layout"));
        filters.add(new ItemListingFilter(FILTER_PRIMARY_THEME, _("Themes"), _("Themes"), TABLE_ALL_PAGES).addFilter(PageItem.FILTER_CONTENT_TYPE, "theme"));
        filters.add(new ItemListingFilter(FILTER_PRIMARY_API, _("REST API extensions"), _("REST API extensions"), TABLE_ALL_PAGES).addFilter(PageItem.FILTER_CONTENT_TYPE, ContentType.API_EXTENSION));
        return filters;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Manage resources"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public String defineJsId() {
        return "pageList";
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(PageItem.ATTRIBUTE_IS_PROVIDED, true);
    }

    @Override
    protected LinkedList<ItemListingFilter> defineSecondaryFilters() {
        return null;
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return null;
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allPagesTable());
        return tables;
    }

    private ItemListingTable allPagesTable() {
        final ItemTable itemTable = itemTable()
                .addAttributeToCheckForGroupedActions(PageItem.ATTRIBUTE_IS_PROVIDED, "true");
        return new ItemListingTable(new JsId(TABLE_ALL_PAGES), _("Enabled"), itemTable, getItemQuickDetailPage());
    }

    private ItemTable itemTable() {
        showSearchBar = false;
        return new ItemTable(new JsId("profile"), Definitions.get(PageDefinition.TOKEN)).addHiddenFilter("processDefinitionId", "")
                .addColumn(PageItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true, true)
                .addColumn(PageItem.ATTRIBUTE_DESCRIPTION, _("Description"), false)
                .addColumn(PageItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Updated on"), true, false)
                .addCellFormatter(PageItem.ATTRIBUTE_DISPLAY_NAME, new I18NCellFormatter())
                .addCellFormatter(PageItem.ATTRIBUTE_DESCRIPTION, new I18NCellFormatter())
                .setShowSearch(false)
                .addGroupedAction(new JsId("delete"), _("Delete"), _("Delete selected pages"),
                        new DeleteMultipleItemsPopupCustomPageAction(PageDefinition.get(), _("page"), _("pages")));

    }

    private ItemQuickDetailsPage<? extends IItem> getItemQuickDetailPage() {
        return new PageQuickDetailsPage();
    }

    private Clickable createLink() {
        return new Link(_("Add"), _("Add a new custom page"), new CheckValidSessionBeforeAction(new ActionShowPopup(new AddCustomPage())));
    }

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        // TODO: Add the security manager
        // if (FeatureManager.isFeatureAvailable(ClientFeature.CUSTOM_PAGES)) {
        return asList(createLink());
        // } else {
        // return super.defineFilterPanelActions();
        // }
    }

}
