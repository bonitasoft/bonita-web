/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.page.itemListingPage;

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.menu.Menu;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.TableColumn;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel, Paul Amar
 * @param <T>
 *        The class of the items that will be displayed in this page.
 */
public abstract class ItemListingPage<T extends IItem> extends Page {

    /**
     * The size of icons to display with filters.
     */
    private static final int FILTERS_ICON_SIZE = 20;

    /**
     * The list of tables available to display in the middle panel.
     */
    private final Map<String, ItemListingTable> tables = new LinkedHashMap<String, ItemListingTable>();

    /**
     * The filter currently selected and displayed.
     */
    private ItemListingFilter currentFilter = null;

    /**
     * The search bar.
     */
    protected boolean showSearchBar = true;

    /**
     * The search form for all tables.
     */
    protected final Form tablesSearch = new Form(new JsId("search"));

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public ItemListingPage() {
        super();
        addClass("itemlistingpage");
        setAllowAutomatedUpdate(false);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the currentFilter
     */
    public ItemListingFilter getCurrentFilter() {
        return this.currentFilter;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BUILD VIEW
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The left panel containing the filters.
     */
    private final Section filtersPanel = new Section(new JsId("filters_panel"));

    /**
     * The middle panel containing the tables.
     */
    private final Section tablesPanel = new Section(new JsId("tables_panel"));

    /**
     * The right panel containing the details.
     */
    private final Section detailsPanel = new Section(new JsId("details_panel"));

    /**
     * Build the current view content.
     */
    @Override
    public final void buildView() {
        this.detailsPanel.setId("details_panel");

        initTablesPanel();
        initFiltersPanel();
        initDetailsPanel();

        addBody(
                this.filtersPanel,
                this.tablesPanel,
                this.detailsPanel);

    }

    public ItemListingPage<T> selectResourceFilter(final APIID filterId) {
        addParameter(UrlOption.RESOURCE_FILTER, filterId.toString());
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS PANEL
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Actions on top of the filters panel
     */
    private ContainerStyled<Clickable> filterActions = null;

    /**
     * First filter section.
     */
    private Section primaryFilters = null;

    /**
     * Second filter section. Contains less important filters
     */
    private Section secondaryFilters = null;

    /**
     * Third filter section. Contains resource dependent filters
     */
    public Section resourceFilters = null;

    /**
     * Map of all filters defined.<br />
     * Used to check if the filter defined in parameter exists.
     */
    private final Map<String, Link> filtersLinks = new LinkedHashMap<String, Link>();

    /**
     * Initialize the filter panel (left panel).
     */
    private void initFiltersPanel() {
        // this.filtersPanel.addHeader(new Title(getTitle().getText()));

        initFilterPanelActions();
        initPrimaryFilters();
        initSecondaryFilters();
        initResourceFilters();

        if (hasParameter(UrlOption.FILTER) && ItemListingPage.this.filtersLinks.containsKey(getParameter(UrlOption.FILTER))) {
            selectFilter(getParameter(UrlOption.FILTER));
        } else if (!hasParameter(UrlOption.RESOURCE_FILTER) || this.resourceFilters == null) {
            selectFirstFilter();
        }

    }

    void selectFirstFilter() {
        $(ItemListingPage.this.filtersLinks.values().iterator().next().getElement()).click();
    }

    /**
     * Initialize the action on top of the filters list.
     */
    private void initFilterPanelActions() {
        final List<Clickable> filterPanelActions = defineFilterPanelActions();

        if (filterPanelActions != null && filterPanelActions.size() > 0) {
            this.filterActions = new ContainerStyled<Clickable>(new JsId("actions"));
            for (final Clickable link : filterPanelActions) {
                this.filterActions.append(link);
            }
            this.filtersPanel.addBody(this.filterActions);
        }
    }

    /**
     * Define the filters that will displayed as most important.
     *
     * @return This method must return the list of filters to display in the right display order
     */
    protected List<Clickable> defineFilterPanelActions() {
        return null;
    }

    /**
     * Initialize the primary filter section.
     */
    private void initPrimaryFilters() {
        this.primaryFilters = new Section(new JsId("primary_filters"));
        this.filtersPanel.addBody(this.primaryFilters);

        addTitleToSection(definePrimaryFiltersTitle(), this.primaryFilters);
        addLinkFilters(definePrimaryFilters(), this.primaryFilters);
    }

    /**
     * Define the filters that will displayed as most important.
     *
     * @return This method must return the list of filters to display in the right display order
     */
    protected abstract LinkedList<ItemListingFilter> definePrimaryFilters();

    /**
     * (OPTIONAL) Define title of the primary filters section
     *
     * @return This method can return the title of the section. If null then no text will be added
     */
    protected Title definePrimaryFiltersTitle() {
        return null;
    }

    /**
     * Initialize the secondary filter section.
     */
    private void initSecondaryFilters() {
        final List<ItemListingFilter> filters = defineSecondaryFilters();
        if (filters == null) {
            return;
        }
        this.secondaryFilters = new Section(new JsId("secondary_filters"));
        this.filtersPanel.addBody(this.secondaryFilters);

        addTitleToSection(defineSecondaryFiltersTitle(), this.secondaryFilters);
        addLinkFilters(filters, this.secondaryFilters);
    }

    /**
     * (OPTIONAL) Define the filters that will displayed as less important.
     *
     * @return This method must return the list of filters to display in the right display order
     */
    protected LinkedList<ItemListingFilter> defineSecondaryFilters() {
        return null;
    }

    /**
     * (OPTIONAL) Define title of the secondary filters section
     *
     * @return This method can return the title of the section. If null then no text will be added
     */
    protected Title defineSecondaryFiltersTitle() {
        return null;
    }

    /**
     * Initialize the resourceFilter section.
     */
    private void initResourceFilters() {
        final ItemListingResourceFilter filter = defineResourceFilters();
        if (filter == null) {
            return;
        }
        if (filter.getFiltersMapping() == null || filter.getFiltersMapping().size() == 0) {
            throw new IllegalArgumentException("Filter mapping can't be empty");
        }

        this.resourceFilters = new Section(new JsId("resource_filters"));
        this.filtersPanel.addBody(this.resourceFilters);

        ResourceFilterFiller<T> filler = new ResourceFilterFiller<T>(this, filter);
        this.resourceFilters.addFiller(filler);
    }

    /**
     * Define the filters automatically filled with items from a linked resource.
     *
     * @return This method must return the definition of a resource dependent filters list
     */
    protected abstract ItemListingResourceFilter defineResourceFilters();

    /**
     * (OPTIONAL) Define title of the resource filters section
     *
     * @return This method can return the title of the section. If null then no text will be added
     */
    protected Title defineResourceFiltersTitle() {
        return null;
    }

    /**
     * Add filters to a defined section.
     *
     * @param filters
     *        The filters to add
     * @param section
     *        The section to fill with filters
     */
    private void addLinkFilters(final List<ItemListingFilter> filters, final Section section) {
        for (final ItemListingFilter filter : filters) {
            ChangeFilterAction action = new ChangeFilterAction(filter);
            Link link = createLinkFilter(filter, action);
            section.addBody(link);
            filtersLinks.put(filter.getName(), link);
        }
    }

    private Link createLinkFilter(final ItemListingFilter filter, final ChangeFilterAction action) {
        final Link link = new Link(
                new JsId(filter.getName().toLowerCase()),
                filter.getLabel(),
                filter.getTooltip() + (filter.hasAdditionnalInfo() ? " " + filter.getAdditionnalInfo() : ""),
                action
                );
        link.forceToolTip();
        action.setLink(link);
        filter.setLink(link);

        if (filter.hasAdditionnalInfo()) {
            Element span = DOM.createSpan();
            span.setInnerText(filter.getAdditionnalInfo());
            HTML.append(link.getElement(), span);
        }

        if (filter.getImageUrl() != null) {
            final Image image = new Image(new Url("css/transparent.gif"), FILTERS_ICON_SIZE, FILTERS_ICON_SIZE, filter.getTooltip());
            if (filter.getImageUrl().isEmpty()) {
                image.addClass("empty");
                HTML.prepend(link.getElement(), image.getElement());
            } else {
                HTML.prepend(link.getElement(), new Image(filter.getImageUrl(), FILTERS_ICON_SIZE, FILTERS_ICON_SIZE, filter.getTooltip()).getElement());
            }
        }
        return link;
    }

    /**
     * Add title to the header of the section
     *
     * @param title
     *        Title to add
     * @param section
     *        Section where the title will be added
     */
    public void addTitleToSection(final Title title, final Section section) {
        if (title != null) {
            section.addHeader(title);
        }
    }

    boolean hasResourceFilterParameter() {
        return hasParameter(UrlOption.RESOURCE_FILTER) && ItemListingPage.this.filtersLinks.containsKey(getParameter(UrlOption.RESOURCE_FILTER));
    }

    void displayFilters(final List<ItemListingFilter> filters) {
        ItemListingPage.this.addLinkFilters(filters, ItemListingPage.this.resourceFilters);
    }

    void selectFilter(final String filterId) {
        final Link defaultFilterLink = this.filtersLinks.get(filterId);
        $(defaultFilterLink.getElement()).click();
    }

    /**
     * ChangeFilterAction, private class to update Tables, get the currentFilter.
     */
    private class ChangeFilterAction extends Action {

        /**
         * The action called to sort the tables.
         *
         * @author Paul AMAR
         */
        private final class ChangeSortAction extends Action {

            /**
             * The column to sort.
             */
            private final TableColumn sort;

            private final boolean desc;

            /**
             * Default Constructor.
             *
             * @param sort
             *        The column to sort
             */
            private ChangeSortAction(final TableColumn sort, final boolean desc) {
                this.sort = sort;
                this.desc = desc;
            }

            /**
             * @see Action#execute()
             */
            @Override
            public void execute() {
                for (final String tableName : ChangeFilterAction.this.filter.getTablesToDisplay()) {
                    final ItemTable table = ItemListingPage.this.tables.get(tableName).getItemTable();
                    table.setOrder(sort.getSortName(), desc);
                }
            }
        }

        /**
         * The filter to display.
         */
        private final ItemListingFilter filter;

        /**
         * The link of the filter to display.
         */
        private Link link;

        /**
         * Default constructor.
         *
         * @param filter
         *        The filter to display
         */
        public ChangeFilterAction(final ItemListingFilter filter) {
            this.filter = filter;
        }

        /**
         * @param link
         *        the link to set
         */
        public void setLink(final Link link) {
            this.link = link;
        }

        /**
         * @see Action#execute()
         */
        @Override
        public void execute() {
            $(ItemListingPage.this.detailsPanel.getElement()).empty();

            // URL update
            ClientApplicationURL.removeAttribute(UrlOption.RESOURCE_FILTER);
            ClientApplicationURL.removeAttribute(UrlOption.FILTER);
            ClientApplicationURL.addAttribute(this.filter.isResourceFilter() ? UrlOption.RESOURCE_FILTER : UrlOption.FILTER, this.filter.getName());
            ClientApplicationURL.refreshUrl(false);

            ItemListingPage.this.currentFilter = this.filter;

            // Update current marker
            $("a", ItemListingPage.this.filtersPanel.getElement()).removeClass("current");
            this.link.addClass("current");

            resetSearch();
            updateTablesDisplay();
            updateTableSorts();
        }

        /**
         * Show a table defined by its name.
         *
         * @param tableName
         *        The name of the table to show
         */
        private void showTable(final String tableName) {
            $(ItemListingPage.this.tables.get(tableName).getElement()).show();
        }

        /**
         * Show all tables.
         */
        @SuppressWarnings("unused")
        private void showAllTables() {
            $(".table_section", ItemListingPage.this.tablesPanel.getElement()).show();
        }

        /**
         * Hide a table defined by its name.
         *
         * @param tableName
         *        The name of the table to hide
         */
        @SuppressWarnings("unused")
        private void hideTable(final String tableName) {
            $(ItemListingPage.this.tables.get(tableName).getElement()).hide();
        }

        /**
         * Hide all tables.
         */
        private void hideAllTables() {
            $(".table_section", ItemListingPage.this.tablesPanel.getElement()).hide();
        }

        /**
         * Clear the search input.
         */
        private void resetSearch() {
            ItemListingPage.this.tablesSearch.reset();

            ItemListingPage.this.tablesSearch.setEntryValue("search", "");
            for (final String tableName : this.filter.getTablesToDisplay()) {
                showTable(tableName);
                ItemListingPage.this.tables.get(tableName).getItemTable().setSearch(null);
            }

        }

        /**
         * Choose tables to display for this filter.
         */
        private void updateTablesDisplay() {
            // Reset
            hideAllTables();

            // Show only necessary tables
            for (final String tableName : this.filter.getTablesToDisplay()) {
                final ItemTable itemTable = ItemListingPage.this.tables.get(tableName).getItemTable();

                // Reset
                itemTable
                        .resetLines()
                        .resetHiddenFilters()
                        .addHiddenFilters(ItemListingPage.this.tables.get(tableName).getDefaultHiddenFilters())
                        .addHiddenFilters(this.filter.getAdditionalFilters())
                        .setPage(0);

                // Default selected line
                itemTable.setDefaultSelectedLine(0);
                if (ClientApplicationURL.getPageAttributes().getValue("_id") != null) {
                    itemTable.setDefaultSelectedId(
                            APIID.makeAPIID(ClientApplicationURL.getPageAttributes().getValue("_id")));
                }

                // Update
                showTable(tableName);
                itemTable.refresh();
            }
        }

        /**
         * Choose sorting available for this filter.
         */
        private void updateTableSorts() {
            // Reset
            ItemListingPage.this.menuSorts.clear();

            // Reset
            ItemListingPage.this.menuSorts.addJsOption("align", "right");
            // Look for sorts
            final List<String> addedSorts = new ArrayList<String>();
            for (final String tableName : this.filter.getTablesToDisplay()) {
                final ItemTable table = ItemListingPage.this.tables.get(tableName).getItemTable();
                final List<TableColumn> sorts = table.getColumns();
                for (final TableColumn sort : sorts) {
                    // Only one sort with the same label
                    if (sort.isSortable() && !addedSorts.contains(sort.getSortName())) {

                        // add sort
                        ItemListingPage.this.menuSorts.addMenuItem(
                                new MenuLink(sort.getJsId(), sort.getLabel().toString() + " " + _("ascending"), sort.getTooltip() == null ? "" : sort
                                        .getTooltip(),
                                        new ChangeSortAction(sort, true)));
                        addedSorts.add(sort.getSortName());

                        // add reverse sort
                        ItemListingPage.this.menuSorts.addMenuItem(
                                new MenuLink(sort.getJsId(), sort.getLabel().toString() + " " + _("descending"), sort.getTooltip() == null ? "" : sort
                                        .getTooltip(),
                                        new ChangeSortAction(sort, false)));
                        addedSorts.add(sort.getSortName());
                    }
                }
            }

        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TABLES PANEL
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The menu containing the sorts available.
     */
    private final Menu sortMenu = new Menu(new JsId("sort"));

    /**
     * The menu folder containing the sorts available.
     */
    private final MenuFolder menuSorts = new MenuFolder(_("Sort by"));

    /**
     * Initialize the table search form.
     */
    private void initTableSearch() {

        if (this.showSearchBar) {

            this.tablesSearch
                    .addTextEntryWithPlaceholder(new JsId("query"), "", _("Enter the text to search for"), _("Search..."))
                    .addButton(new JsId("search"), _("Search"), _("Update this page using the defined search query"), new FormAction() {

                        @Override
                        public void execute() {
                            for (final String tableName : ItemListingPage.this.currentFilter.getTablesToDisplay()) {
                                final ItemTable table = ItemListingPage.this.tables.get(tableName).getItemTable();
                                table.setSearch(this.getParameter("query"));
                                table.setPage(0);
                                table.refresh();
                            }
                        }
                    });
        }

    }

    /**
     * Initialize the table panel (middle panel).
     */
    private void initTablesPanel() {
        sortMenu.addFolder(menuSorts);
        menuSorts.addJsOption("selectMode", true).addJsOption("align", "right");
        tablesPanel.addHeader(sortMenu);

        for (ItemListingTable itemListingTable : defineTables()) {

            itemListingTable.getItemTable().setFillOnRefresh(false);
            itemListingTable.getItemTable().setFillOnLoad(false);

            this.tables.put(itemListingTable.getJsId().toString(), itemListingTable);

            final ItemTable table = itemListingTable.getItemTable()
                    .setFillOnLoad(false)
                    .setShowSearch(true)
                    .setView(VIEW_TYPE.VIEW_DETAILS);

            sortTable(table, defineDefaultSort());

            this.tablesPanel.addHeader(getTitle());
            this.tablesPanel.addBody(itemListingTable);

            // Add quickDetailsAction
            table.setDefaultAction(new UpdateQuickDetailsAction<T>(this, itemListingTable));
        }

        // add the table Search
        initTableSearch();
        this.tablesPanel.addHeader(this.tablesSearch);
    }

    private void sortTable(final ItemTable table, ItemListingSort itemListingSort) {
        if (table.getOrder() == null && itemListingSort != null) {
            table.setOrder(itemListingSort.getSortName(), itemListingSort.isSortAscending());
        }
    }

    /**
     * Define the default sorting.
     *
     * @return This method must return the default sort that must be used.
     * @deprecated don't add sort order on page but on tables, use {@link ItemTable#setOrder(String, boolean)}
     */
    @Deprecated
    protected ItemListingSort defineDefaultSort() {
        return null;
    }

    /**
     * Define the list of tables that can be displayed by filters.
     *
     * @return This method must return a list of ItemListingTable
     */
    protected abstract LinkedList<ItemListingTable> defineTables();

    public void updateQuickDetailPanel(ItemQuickDetailsPage<?> itemQuickDetailsPage, String itemId) {
        final TreeIndexed<String> params = itemQuickDetailsPage.getParameters();
        params.addValue("id", itemId);

        ClientApplicationURL.addAttribute("_id", itemId);
        ClientApplicationURL.refreshUrl(false);
        ViewController.showView(itemQuickDetailsPage.getToken(), detailsPanel.getElement(), params);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DETAILS PANEL
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initialize the details panel (right panel).
     */
    private void initDetailsPanel() {
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // REFRESH
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void refresh() {
        $(ItemListingPage.this.detailsPanel.getElement()).empty();

        // Refresh resource filters

        // Refresh middle tables

        for (final String tableName : this.getCurrentFilter().getTablesToDisplay()) {
            final ItemTable table = this.tables.get(tableName).getItemTable();

            final String idInUrl = ClientApplicationURL.getPageAttributes().getValue("_id");
            if (idInUrl != null) {
                table.setDefaultSelectedId(APIID.makeAPIID(idInUrl));
            }
            table.refresh();
        }

        // Refresh details panel if necessary

        // $(getCurrentFilter().getLink().getElement()).click();

    }

    void selectRightResourceFilter() {
        if (hasResourceFilterParameter()) {
            selectFilter(getParameter(UrlOption.RESOURCE_FILTER));
        } else if (!hasParameter(UrlOption.FILTER)) {
            selectFirstFilter();
        }
    }

}
