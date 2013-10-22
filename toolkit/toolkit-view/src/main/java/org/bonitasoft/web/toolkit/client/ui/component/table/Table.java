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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Random;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifierEngine;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Refreshable;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.table.filters.TableFilter;
import org.bonitasoft.web.toolkit.client.ui.component.table.filters.TableFilterHidden;
import org.bonitasoft.web.toolkit.client.ui.component.table.filters.TableFilterSelect;
import org.bonitasoft.web.toolkit.client.ui.component.table.filters.TableFilterText;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.html.XMLAttributes;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

import java.util.*;
import java.util.Map.Entry;

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author Séverin Moussel
 */
public class Table extends AbstractTable implements Refreshable {

    private static final String ALWAYS_ENABLE_CLASS = "force";

    public static enum VIEW_TYPE {
        VIEW_LIST {

            @Override
            public String toString() {
                return "list";
            }
        },
        VIEW_DETAILS {

            @Override
            public String toString() {
                return "details";
            }
        },
        VIEW_TABLE {

            @Override
            public String toString() {
                return "table";
            }
        },
        FORM {

            @Override
            public String toString() {
                return "form";
            }
        },
        TREE {

            @Override
            public String toString() {
                return "tree";
            }
        }
    };

    protected VIEW_TYPE defaultView = VIEW_TYPE.VIEW_TABLE;

    protected ContainerStyled<TableLine> lines = new ContainerStyled<TableLine>();

    protected Element rootElement = null;

    private Element tableElement = null;

    private Element linesElement = null;

    private final List<Element> pagerElements = new ArrayList<Element>(2);

    private int currentPage = 0;

    private int nbResults = 0;

    private int nbLinesByPage = 10;

    private boolean showSearch = true;

    private boolean selectLineOnClick = true;

    private String defaultSearch = "";

    private String order = null;

    private Filler<? extends AbstractTable> refreshFiller = null;

    private Integer refreshEvery = null;

    public static enum COLUMN_POSITION {
        LAST, FIRST
    };

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Table(final JsId jsid) {
        this(jsid, null);
    }

    public Table(final TableFiller tableFiller) {
        this(null, tableFiller);
    }

    public Table() {
        this((JsId) null);
    }

    public Table(final JsId jsid, final TableFiller tableFiller) {
        super(jsid);
        setFiller(tableFiller);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COLUMNS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected ContainerStyled<TableColumn> columns = new ContainerStyled<TableColumn>();

    public Table addColumn(final JsId jsid, final String label) {
        return this.addColumn(new TableColumn(this, jsid, label, null, false, true));
    }

    public Table addColumn(final JsId jsid, final String label, final String sortName) {
        return this.addColumn(new TableColumn(this, jsid, label, sortName, true, true));
    }

    public Table addColumn(final JsId jsid, final String label, final String sortName, final boolean sorted, final boolean sortAscending) {
        return addColumn(new TableColumn(this, jsid, label, sortName, sorted, sortAscending));
    }

    public Table addColumn(TableColumn column) {
        this.columns.append(column);
        String sortName = column.getSortName();
        if (sortName != null) {
            this.order = sortName + (column.isSortAscending() ? " ASC" : " DESC");
        }
        return this;
    }

    public Table setColumnPos(final JsId jsid, final int index) {
        this.columns.move(jsid, index);
        return this;
    }

    public TableColumn getColumn(final JsId jsid) {
        return this.columns.get(jsid);
    }

    /**
     * @return the columns
     */
    public List<TableColumn> getColumns() {
        return this.columns.getComponents();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LINES AND CELLS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Table addLine() {
        return this.addLine(null, null, null);
    }

    public Table addLine(final String checkBoxId) {
        return this.addLine(checkBoxId, null, null);
    }

    public Table addLine(final String checkBoxId, final String className) {
        return this.addLine(checkBoxId, className, null);
    }

    public Table addLine(final Action defaultAction) {
        return this.addLine(null, null, defaultAction);
    }

    public Table addLine(final String checkBoxId, final Action defaultAction) {
        return this.addLine(checkBoxId, null, defaultAction);
    }

    private class CheckLineAction extends Action {

        private final String checkboxId;

        private Action defaultAction;

        public CheckLineAction(String checkboxId) {
            this.checkboxId = checkboxId;
        }

        @Override
        public void execute() {
            clearCheckboxes();
            setCheckboxesValue(getCheckbox(this.checkboxId), true, false);
            if (this.defaultAction != null) {
                this.defaultAction.execute();
            }
        }

        public void setDefaultAction(Action defaultAction) {
            this.defaultAction = defaultAction;
        }

    }

    public Table addLine(String checkboxId, final String className, final Action defaultAction, Boolean allowGroupedAction) {
        if (checkboxId == null) {
            checkboxId = String.valueOf(this.lines.size() - 1);
        }

        final TableLine line = new TableLine();
        if (className != null) {
            line.addClass(className);
        }
        if (this.itemIdOnRow) {
            line.addClass("APIID_" + checkboxId);
        }

        if (this.selectLineOnClick) {
            CheckLineAction action = new CheckLineAction(checkboxId);
            action.setDefaultAction(defaultAction);
            line.setDefaultAction(action);
        } else {
            line.setDefaultAction(defaultAction);
        }

        this.lines.append(line);

        // If we need checkboxes, we automaticaly had the column
        if (hasGroupedActions()) {
            final XMLAttributes attributes = new XMLAttributes();
            if (allowGroupedAction) {
                attributes.add("id", HTML.getUniqueId());
                this.addCell(new Html(HTML.checkbox("id", checkboxId, this.selectedIds.contains(checkboxId), attributes)));
            } else {
                attributes.add("class", "emptyCheckBox");
                this.addCell(new Html(HTML.div()));
            }

        }

        return this;

    }

    public Table addLine(String checkboxId, final String className, final Action defaultAction) {
        return this.addLine(checkboxId, className, defaultAction, true);
    }

    public Table addCell(final AbstractComponent... components) {
        getLastLine().append(new TableCell(components, this.columns.get(getLastLine().size()).getJsId().toString()));
        return this;
    }

    public Table addCell(final String text) {
        final TableColumn column = this.columns.get(getLastLine().size());

        getLastLine().append(new TableCell(
                ModifierEngine.modify(text, column.getOutputModifiers()),
                column.getJsId().toString()
                ));
        return this;
    }

    public TableLine getLastLine() {
        return this.lines.getLast();
    }

    public TableColumn getLastColumn() {
        if (this.lines.size() > 0) {
            return this.columns.get(getLastLine().size());
        } else {
            return this.columns.getLast();
        }
    }

    public TableCell getLastCell() {
        return getLastLine().getLast();
    }

    public Table resetLines() {
        this.lines.empty();
        this.lines = new ContainerStyled<TableLine>();
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PAGER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Table setPager(final int currentPage, final int nbResults, final int nbLinesByPage) {
        setPage(currentPage);
        this.nbResults = nbResults;
        this.nbLinesByPage = nbLinesByPage;
        return this;
    }

    public void setPage(final int page) {
        this.currentPage = page;
    }

    public int getNbResults() {
        return this.nbResults;
    }

    public int getNbPages() {
        return Double.valueOf(Math.ceil(Integer.valueOf(this.nbResults).doubleValue() / Integer.valueOf(this.nbLinesByPage).doubleValue())).intValue();
    }

    public int getNbLinesByPage() {
        return this.nbLinesByPage;
    }

    public int getPage() {
        return this.currentPage;
    }

    public void changePage(final int page) {
        this.currentPage = page;
        refresh();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE HTML
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {
        this.rootElement = DOM.createDiv();
        this.rootElement.addClassName("datatable");
        this.rootElement.addClassName("table_view_" + this.defaultView.toString());

        this.rootElement.appendChild(makePager("pager_top"));
        if (this.defaultView != VIEW_TYPE.FORM) {
            this.rootElement.appendChild(makeGroupedActions());
        }
        this.rootElement.appendChild(makeFilters());
        this.rootElement.appendChild(makeTable());
        this.rootElement.appendChild(makePager("pager_bottom"));

        if (this.defaultView == VIEW_TYPE.FORM) {
            this.rootElement.appendChild(makeGroupedActions());
        }

        disableActionLinks();

        return this.rootElement;
    }

    public void updateHtml() {
        final int nbPages = getNbPages();
        if (isCurrentPageTooHigh(nbPages)) {
            setPage(nbPages - 1);
            refresh();
            return;
        }

        updateTable();
        updatePager();
        ViewController.updateUI(this.rootElement, true);
        // this.updateScript(this.rootElement);
    }

    private boolean isCurrentPageTooHigh(final int nbPages) {
        return this.currentPage >= nbPages && nbPages > 0;
    }

    private Element makeTable() {

        // TABLE
        this.tableElement = DOM.createDiv();
        this.tableElement.addClassName("table");

        // THEAD
        final Element header = DOM.createDiv();
        header.addClassName("thead");
        this.columns.setRootTag("div", "tr");
        header.appendChild(this.columns.getElement());
        this.tableElement.appendChild(header);

        // TBODY
        this.lines.setRootTag("div", "tbody");

        this.linesElement = this.lines.getElement();
        this.tableElement.appendChild(this.linesElement);

        return this.tableElement;
    }

    private Element makeFilters() {

        final TableFilterText filter = new TableFilterText(_("Search"), _("Type to search"), "search", this.defaultSearch);
        filter.setTable(this);
        this.filters.prepend(filter);

        final Element filtersArea = DOM.createDiv();
        filtersArea.addClassName("tablefilters");

        this.filters.setRootTag("div", "tablefilters");
        this.filters.setWrapTagName("div");

        final Element filtersElement = this.filters.getElement();

        if (!this.showSearch) {
            $(filtersElement.getChild(0)).hide();
            $(filtersElement).hide();
        }

        return filtersElement;
    }

    private Element makeGroupedActions() {
        this.groupedActions.setRootTag("div", "actions");

        return this.groupedActions.getElement();
    }

    private void updateTable() {

        if (!this.saveCheckboxes) {
            clearSelectedIds();
        }

        disableActionLinks();

        if (this.lines.size() == 0) {
            addCheckAllCheckbox();
            addEmptyCssClass();
            replaceLinesElement(createEmptyLinesElement());
        } else {
            removeEmptyCssClass();

            // Refill header
            this.columns.resetGeneration();
            $(".thead", getElement()).empty().append(this.columns.getElement());
            addCheckAllCheckbox();

            // Refill body
            this.lines.setRootTag("div", "tbody");
            this.tableElement.addClassName(String.valueOf(Random.nextInt()));

            replaceLinesElement(this.lines.getElement());
            addChangeEventHandler(getAllCheckboxes());
        }
    }

    private void removeEmptyCssClass() {
        getElement().removeClassName("empty");
    }

    private void addEmptyCssClass() {
        getElement().addClassName("empty");
    }

    private Element createEmptyLinesElement() {
        return XML.makeElement(HTML.div(new HTMLClass("emptytable")) + _("No data") + HTML._div());
    }

    private void replaceLinesElement(final Element linesElement) {
        this.tableElement.replaceChild(linesElement, this.linesElement);
        this.linesElement = linesElement;
    }

    private void addChangeEventHandler(GQuery checkboxes) {
        checkboxes.each(new Function() {

            @Override
            public void f(final Element k) {
                final GQuery checkbox = $(k);
                checkbox.change(new Function() {

                    @Override
                    public boolean f(Event e) {
                        Table.this.processEvent($(e));
                        return true;
                    }
                });

                // fix click propagation to line
                checkbox.click(new Function() {

                    @Override
                    public boolean f(final Event e) {
                        e.stopPropagation();
                        checkbox.trigger(Event.ONCHANGE);
                        return true;
                    }
                });

            }
        });
    }

    private void addCheckAllCheckbox() {
        final String checkAllId = HTML.getUniqueId();
        CheckBox checkBox = new CheckBox();
        $(".th_checkboxes", tableElement).empty().append(

                $(HTML.checkbox("checkall", "0", new XMLAttributes("id", checkAllId))).click(new Function() {

                    @Override
                    public void f(final Element e) {
                        Object checkedValue = $(e).prop("checked");
                        Table.this.setCheckboxesValue(Table.this.getAllCheckboxes(), (Boolean) checkedValue, false);
                    }

                })).append(
                $(HTML.label(_("All"), checkAllId)));
    }

    private void processEvent(final GQuery cb) {
        final GQuery labels = cb.closest("div").children("label");
        String itemId = cb.val();
        if (cb.is(":checked")) {
            onCheckItem(labels, itemId);
        } else {
            onUncheckItem(labels, itemId);
        }

        // Check all if no checkbox unchecked
        final boolean noCheckboxCheched = $(".td_checkboxes input", Table.this.getElement()).filter(":checked").length() == $(".td_checkboxes input",
                Table.this.getElement()).length();
        if (noCheckboxCheched) {
            setCheckAllCheckboxesValue($(".th_checkboxes input", Table.this.getElement()), true);
        } else {
            setCheckAllCheckboxesValue($(".th_checkboxes input", Table.this.getElement()), false);
        }

        // Set datatable class to to inform about selected or not
        if ($(".td_checkboxes input", Table.this.getElement()).filter(":checked").length() > 0) {
            $(getElement()).addClass("linechecked");
            enableActionsLinks();
        } else {
            $(getElement()).removeClass("linechecked");
            disableActionLinks();
        }
    }

    private void onUncheckItem(final GQuery labels, String itemId) {
        if (labels.length() > 0) {
            labels.removeClass("checked");
        }
        if (Table.this.selectedIds.contains(itemId)) {
            Table.this.selectedIds.remove(itemId);
        }
        fireEvent(new ItemUncheckedEvent(Table.this.selectedIds, itemId));
    }

    private void onCheckItem(final GQuery labels, String itemId) {
        if (labels.length() > 0) {
            labels.addClass("checked");
        }
        fireEvent(new ItemCheckedEvent(Table.this.selectedIds, itemId));
        if (!Table.this.selectedIds.contains(itemId)) {
            Table.this.selectedIds.add(itemId);
        }
    }

    public HandlerRegistration addItemCheckedHandler(ItemCheckedHandler handler) {
        return this.addHandler(handler, ItemCheckedEvent.TYPE);
    }

    public HandlerRegistration addItemUncheckedHandler(ItemUncheckedHandler handler) {
        return this.addHandler(handler, ItemUncheckedEvent.TYPE);
    }

    private void disableActionLinks() {
        for (Link link : this.groupedActions.getComponents()) {
            if (!link.hasClass(ALWAYS_ENABLE_CLASS)) {
                link.setEnabled(false);
            }
        }
    }

    private void enableActionsLinks() {
        for (Link link : this.groupedActions.getComponents()) {
            if (!link.hasClass(ALWAYS_ENABLE_CLASS)) {
                link.setEnabled(true);
            }
        }
    }

    private GQuery getAllCheckboxes() {
        return $(".td_checkboxes input", tableElement);
    }

    private Element makePager(final String className) {
        final Element pager = new Pager(this, this.currentPage + 1, this.nbResults, this.nbLinesByPage).addClass(className).getElement();
        $(pager).hide();

        this.pagerElements.add(pager);
        return pager;
    }

    private void updatePager() {
        for (int i = 0; i < this.pagerElements.size(); i++) {
            final Element pager = new Pager(this, this.currentPage + 1, this.nbResults, this.nbLinesByPage).addClass(i == 0 ? "pager_top" : "pager_bottom")
                    .getElement();
            this.pagerElements.get(i).getParentElement().replaceChild(pager, this.pagerElements.get(i));
            this.pagerElements.set(i, pager);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GROUPED ACTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected ContainerStyled<Link> groupedActions = new ContainerStyled<Link>();

    /**
     * Add action to the table and enable checkboxes
     * 
     * @param link
     *            link to add
     * @param force
     *            force button visibility
     * @return
     *         current table instance
     */
    public Table addGroupedAction(Link link, final boolean force) {
        ensureCheckboxesVisibility();
        link.setAction(wrapActionToPassIds(link.getAction(), force));
        if (force) {
            link.addClass(ALWAYS_ENABLE_CLASS);
        }
        // Add the action
        this.groupedActions.append(link);
        return this;
    }

    private void ensureCheckboxesVisibility() {
        // Check if the CheckBoxes column already exists
        final JsId jsid = new JsId("checkboxes");
        if (this.columns.get(jsid) == null) {
            this.columns.prepend(new TableColumn(this, jsid, ""));
        }
    }

    private Action wrapActionToPassIds(final Action action, final boolean force) {
        return new Action() {

            // Set the selected ids as parameters and call the action
            @Override
            public void execute() {
                if (force || Table.this.getSelectedIds().size() > 0) {
                    action.addParameter("id", Table.this.getSelectedIds());
                    action.execute();
                }
            }
        };
    }

    /**
     * 
     * @param label
     * @param tooltip
     * @param action
     *            The action to call on click
     * @param force
     *            Force the visibility of the button (can't be disabled)
     * 
     * @deprecated
     *             Create your own link and use {@link #addGroupedAction(Link, boolean)} instead
     */
    @Deprecated
    public Table addGroupedAction(final JsId id, final String label, final String tooltip, final Action action, final boolean force) {
        this.addGroupedAction(new ButtonAction("btn-" + id.toString(), label, tooltip, action), force);
        return this;
    }

    /**
     * Add an action to the table - action not link to checkbox, always visible
     */
    public Table addAction(Link link) {
        link.addClass(ALWAYS_ENABLE_CLASS);
        this.groupedActions.append(link);
        return this;
    }

    public Table addGroupedAction(final JsId id, final String label, final String tooltip, final Action action) {
        return this.addGroupedAction(id, label, tooltip, action, false);
    }

    public boolean hasGroupedActions() {
        return this.groupedActions.size() > 0;
    }

    /**
     * @return the groupedActions
     */
    public List<Link> getGroupedActions() {
        return this.groupedActions.getComponents();
    }

    private final List<String> selectedIds = new ArrayList<String>();

    private boolean saveCheckboxes = false;

    public Table saveCheckboxes(final boolean save) {
        this.saveCheckboxes = save;
        return this;
    }

    public boolean isSaveCheckboxes() {
        return this.saveCheckboxes;
    }

    /**
     * CLear checked lines memory and reset checkboxes
     */
    public void clearSelectedIds() {
        this.selectedIds.clear();
        clearCheckboxes();
    }

    /**
     * Uncheck all checkboxes
     */
    private void clearCheckboxes() {
        setCheckboxesValue(getAllCheckboxes(), false, false);
    }

    public List<String> getSelectedIds() {
        return this.selectedIds;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected ContainerStyled<TableFilter> filters = new ContainerStyled<TableFilter>();

    private boolean itemIdOnRow = false;

    public Table resetFilters() {
        this.filters.empty();
        return this;
    }

    public Table setShowSearch(final boolean show) {
        this.showSearch = show;

        if (isGenerated()) {
            final GQuery filter = $(".tablefilters > div:has(input[name=search])", this.rootElement);
            if (!show) {
                filter.hide();
            } else {
                filter.show();
            }
        }
        return this;
    }

    public Table addTextFilter(final String label, final String tooltip, final String name) {
        return this.addTextFilter(label, tooltip, name, null);
    }

    public Table addTextFilter(final String label, final String tooltip, final String name, final String defaultValue) {
        final TableFilterText filter = new TableFilterText(label, tooltip, name, defaultValue);
        filter.setTable(this);
        this.filters.append(filter);

        return this;
    }

    public Table addSelectFilter(final String label, final String tooltip, final String name, final Map<String, String> values) {
        return this.addSelectFilter(label, tooltip, name, values, null);
    }

    public Table addSelectFilter(final String label, final String tooltip, final String name, final Map<String, String> values,
            final String defaultValue) {
        final TableFilterSelect select = new TableFilterSelect(label, tooltip, name, defaultValue);
        select.setTable(this);

        for (final Entry<String, String> entry : values.entrySet()) {
            select.addOption(entry.getKey(), entry.getValue(), entry.getValue().equals(defaultValue));
        }

        this.filters.append(select);
        return this;
    }

    public Table addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler) {
        return this.addSelectFilter(label, tooltip, name, filler, null);
    }

    public Table addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler, final String defaultValue) {
        final TableFilterSelect select = new TableFilterSelect(label, tooltip, name, defaultValue);
        select.setTable(this);
        select.setFiller(filler);

        this.filters.append(select);
        return this;
    }

    public Table addHiddenFilter(final String name, final String value) {

        final TableFilterHidden hidden = new TableFilterHidden(name, value);
        hidden.setTable(this);
        this.filters.append(hidden);
        return this;
    }

    public Table addHiddenFilters(final Map<String, String> filters) {
        if (filters != null) {
            for (final Entry<String, String> entry : filters.entrySet()) {
                addHiddenFilter(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public Table resetHiddenFilters() {

        // Save non hidden filters
        final List<TableFilter> filters = new LinkedList<TableFilter>();
        for (final TableFilter filter : this.filters.getComponents()) {
            if (!(filter instanceof TableFilterHidden)) {
                filters.add(filter);
            }
        }

        // Remove all filters
        resetFilters();

        // restore saved non hidden filters
        for (final TableFilter filter : filters) {
            this.filters.append(filter);
        }

        return this;
    }

    public HashMap<String, String> getFilters() {
        final HashMap<String, String> filters = new HashMap<String, String>();

        for (final TableFilter filter : this.filters.getComponents()) {
            if (filter.getName() != "search") {
                filters.put(filter.getName(), filter.getValue() != null ? filter.getValue() : "");
            }
        }

        return filters;
    }

    public HashMap<String, String> getHiddenFilters() {
        final HashMap<String, String> filters = new HashMap<String, String>();

        for (final TableFilter filter : this.filters.getComponents()) {
            if (filter instanceof TableFilterHidden && filter.getName() != "search" && filter.getValue() != null && filter.getValue().length() > 0) {
                filters.put(filter.getName(), filter.getValue());
            }
        }

        return filters;
    }

    public String getSearch() {

        if (!$(getElement()).isVisible()) {
            return "";
        }
        return _getSearch(getElement());
    }

    public Table setSearch(final String query) {
        this.defaultSearch = query;

        if (isGenerated()) {
            this.setSearch(getElement(), defaultSearch);
        }

        return this;
    }

    private native void setSearch(Element e, String defaultSearch)
    /*-{
     $wnd.$(".tablefilters input[name=search]", e).not(".empty").val(defaultSearch);
    
     }-*/;

    private native String _getSearch(Element e)
    /*-{
      return $wnd.$(".tablefilters .tablefiltertext input", e).val();
    
     }-*/;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ORDER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getOrder() {
        // TODO call columns

        return this.order;
    }

    public Table setOrder(final String sortName) {
        return this.setOrder(sortName, true);
    }

    public Table setOrder(final String sortName, final boolean sortAscending) {
        this.order = sortName + (sortAscending ? " ASC" : " DESC");

        if (isGenerated()) {
            for (int i = 0; i < this.columns.size(); i++) {
                this.columns.get(i).setSorted(false);
            }

            refresh();
        }

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TOOLS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Define a filler to be called only on refresh.<br/>
     * This method is used to pass the ItemTableFiller.
     */
    public final void setRefreshFiller(final Filler<? extends AbstractTable> refreshFiller) {
        this.refreshFiller = refreshFiller;
        if (this.refreshEvery != null) {
            this.refreshFiller.setRepeatEvery(this.refreshEvery);
        }
    }

    public final void setSelectLineOnClick(boolean value) {
        this.selectLineOnClick = value;
    }

    @Override
    public void refresh() {
        if (this.refreshFiller != null) {
            this.refreshFiller.run();
        }
        this.runFillers();
    }

    public void setView(final VIEW_TYPE view) {
        this.defaultView = view;
        saveCheckboxes(view == VIEW_TYPE.FORM);
    }

    @Override
    public Table addClass(final String className) {
        super.addClass(className);
        return this;
    }

    public void updateView() {
        this.updateView(getElement());
    };

    private native void updateView(Element e)
    /*-{
        //$wnd.$(e).updateUI();
        
    }-*/;

    /**
     * @param milliseconds
     */
    public void setRefreshEvery(final int milliseconds) {
        this.refreshEvery = milliseconds;

        if (this.refreshFiller != null) {
            this.refreshFiller.setRepeatEvery(milliseconds);
        }
    }

    public int getLinesNumber() {
        return this.lines.size();
    }

    private GQuery getCheckbox(String checkboxId) {
        return $(".td_checkboxes input[value=\"" + checkboxId + "\"]", this.tableElement);
    }

    private void setCheckboxesValue(GQuery checkboxes, boolean value, boolean silent) {
        if (isCheckable(checkboxes)) {
            if (value) {
                checkboxes.prop("checked", true);
            } else {
                checkboxes.prop("checked", false);
            }
            if (!silent) {
                checkboxes.trigger(Event.ONCHANGE);
            }
        }
    }

    private void setCheckAllCheckboxesValue(GQuery checkbox, boolean value) {
        setCheckboxesValue(checkbox, value, true);
        fireCssLabelEvent(tableElement);
    }

    private boolean isCheckable(GQuery checkboxes) {
        return checkboxes != null && checkboxes.length() > 0 && checkboxes.is(":checkbox");
    }

    private native void fireCssLabelEvent(Element e)
    /*-{
        $wnd.$(e).trigger("cssChange");
    }-*/;

    public void setItemIdOnRow(boolean itemIdOnRow) {
        this.itemIdOnRow = itemIdOnRow;

    }
}
