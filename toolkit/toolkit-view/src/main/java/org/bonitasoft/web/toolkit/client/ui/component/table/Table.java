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

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Random;
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

    public Table addColumn(final String label) {
        return this.addColumn(null, label, null, false, true);
    }

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
        columns.append(column);
        String sortName = column.getSortName();
        if (sortName != null) {
            order = sortName + (column.isSortAscending() ? " ASC" : " DESC");
        }
        return this;
    }

    public Table setColumnPos(final JsId jsid, final int index) {
        columns.move(jsid, index);
        return this;
    }

    public TableColumn getColumn(final JsId jsid) {
        return columns.get(jsid);
    }

    /**
     * @return the columns
     */
    public List<TableColumn> getColumns() {
        return columns.getComponents();
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
            setCheckboxesValue(getCheckbox(checkboxId), true, false);
            if (defaultAction != null) {
                defaultAction.execute();
            }
        }

        public void setDefaultAction(Action defaultAction) {
            this.defaultAction = defaultAction;
        }

    }

    public Table addLine(String checkboxId, final String className, final Action defaultAction, Boolean allowGroupedAction) {
        if (checkboxId == null) {
            checkboxId = String.valueOf(lines.size() - 1);
        }

        final TableLine line = new TableLine();
        if (className != null) {
            line.addClass(className);
        }
        if (itemIdOnRow) {
            line.addClass("APIID_" + checkboxId);
        }

        if (selectLineOnClick) {
            CheckLineAction action = new CheckLineAction(checkboxId);
            action.setDefaultAction(defaultAction);
            line.setDefaultAction(action);
        } else {
            line.setDefaultAction(defaultAction);
        }

        lines.append(line);

        // If we need checkboxes, we automaticaly had the column
        if (hasGroupedActions()) {
            final XMLAttributes attributes = new XMLAttributes();
            if (allowGroupedAction) {
                attributes.add("id", HTML.getUniqueId());
                this.addCell(new Html(HTML.checkbox("id", checkboxId, selectedIds.contains(checkboxId), attributes)));
            } else {
                attributes.add("class", "emptyCheckBox");
                this.addCell(new Html(HTML.div()));
            }

        }

        return this;

    }

    public Table addLine(String checkboxId, final String className, final Action defaultAction) {
        return addLine(checkboxId, className, defaultAction, true);
    }

    public Table addCell(final AbstractComponent... components) {
        getLastLine().append(new TableCell(components, columns.get(getLastLine().size()).getJsId().toString()));
        return this;
    }

    public Table addCell(final String text) {
        final TableColumn column = columns.get(getLastLine().size());

        getLastLine().append(new TableCell(
                ModifierEngine.modify(text, column.getOutputModifiers()),
                column.getJsId().toString()
                ));
        return this;
    }

    public TableLine getLastLine() {
        return lines.getLast();
    }

    public TableColumn getLastColumn() {
        if (lines.size() > 0) {
            return columns.get(getLastLine().size());
        } else {
            return columns.getLast();
        }
    }

    public TableCell getLastCell() {
        return getLastLine().getLast();
    }

    public Table resetLines() {
        lines.empty();
        lines = new ContainerStyled<TableLine>();
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
        currentPage = page;
    }

    public int getNbResults() {
        return nbResults;
    }

    public int getNbPages() {
        return Double.valueOf(Math.ceil(Integer.valueOf(nbResults).doubleValue() / Integer.valueOf(nbLinesByPage).doubleValue())).intValue();
    }

    public int getNbLinesByPage() {
        return nbLinesByPage;
    }

    public int getPage() {
        return currentPage;
    }

    public void changePage(final int page) {
        currentPage = page;
        refresh();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE HTML
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {
        rootElement = DOM.createDiv();
        rootElement.addClassName("datatable");
        rootElement.addClassName("table_view_" + defaultView.toString());

        rootElement.appendChild(makePager("pager_top"));
        if (defaultView != VIEW_TYPE.FORM) {
            rootElement.appendChild(makeGroupedActions());
        }
        rootElement.appendChild(makeFilters());
        rootElement.appendChild(makeTable());
        rootElement.appendChild(makePager("pager_bottom"));

        if (defaultView == VIEW_TYPE.FORM) {
            rootElement.appendChild(makeGroupedActions());
        }

        disableActionLinks();

        return rootElement;
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
        ViewController.updateUI(rootElement, true);
        // this.updateScript(this.rootElement);
    }

    private boolean isCurrentPageTooHigh(final int nbPages) {
        return currentPage >= nbPages && nbPages > 0;
    }

    private Element makeTable() {

        // TABLE
        tableElement = DOM.createDiv();
        tableElement.addClassName("table");

        // THEAD
        final Element header = DOM.createDiv();
        header.addClassName("thead");
        columns.setRootTag("div", "tr");
        header.appendChild(columns.getElement());
        tableElement.appendChild(header);

        // TBODY
        lines.setRootTag("div", "tbody");

        linesElement = lines.getElement();
        tableElement.appendChild(linesElement);

        return tableElement;
    }

    private Element makeFilters() {

        final TableFilterText filter = new TableFilterText(_("Search"), _("Type to search"), "search", defaultSearch);
        filter.setTable(this);
        filters.prepend(filter);

        final Element filtersArea = DOM.createDiv();
        filtersArea.addClassName("tablefilters");

        filters.setRootTag("div", "tablefilters");
        filters.setWrapTagName("div");

        final Element filtersElement = filters.getElement();

        if (!showSearch) {
            $(filtersElement.getChild(0)).hide();
            $(filtersElement).hide();
        }

        return filtersElement;
    }

    private Element makeGroupedActions() {
        groupedActions.setRootTag("div", "actions");

        return groupedActions.getElement();
    }

    private void updateTable() {

        if (!saveCheckboxes) {
            clearSelectedIds();
        }

        disableActionLinks();

        if (lines.size() == 0) {
            addCheckAllCheckbox();
            addEmptyCssClass();
            replaceLinesElement(createEmptyLinesElement());
        } else {
            removeEmptyCssClass();

            // Refill header
            columns.resetGeneration();
            $(".thead", getElement()).empty().append(columns.getElement());
            addCheckAllCheckbox();

            // Refill body
            lines.setRootTag("div", "tbody");
            tableElement.addClassName(String.valueOf(Random.nextInt()));

            replaceLinesElement(lines.getElement());
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
        tableElement.replaceChild(linesElement, this.linesElement);
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
                        processEvent($(e));
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
                        setCheckboxesValue(getAllCheckboxes(), (Boolean) checkedValue, false);
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
        Table.this.selectedIds.remove(itemId);
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
        return addHandler(handler, ItemCheckedEvent.TYPE);
    }

    public HandlerRegistration addItemUncheckedHandler(ItemUncheckedHandler handler) {
        return addHandler(handler, ItemUncheckedEvent.TYPE);
    }

    private void disableActionLinks() {
        for (Link link : groupedActions.getComponents()) {
            if (!link.hasClass(ALWAYS_ENABLE_CLASS)) {
                link.setEnabled(false);
            }
        }
    }

    private void enableActionsLinks() {
        for (Link link : groupedActions.getComponents()) {
            if (!link.hasClass(ALWAYS_ENABLE_CLASS)) {
                link.setEnabled(true);
            }
        }
    }

    private GQuery getAllCheckboxes() {
        return $(".td_checkboxes input", tableElement);
    }

    private Element makePager(final String className) {
        final Element pager = new Pager(this, currentPage + 1, nbResults, nbLinesByPage).addClass(className).getElement();
        $(pager).hide();

        pagerElements.add(pager);
        return pager;
    }

    private void updatePager() {
        for (int i = 0; i < pagerElements.size(); i++) {
            final Element pager = new Pager(this, currentPage + 1, nbResults, nbLinesByPage).addClass(i == 0 ? "pager_top" : "pager_bottom")
                    .getElement();
            pagerElements.get(i).getParentElement().replaceChild(pager, pagerElements.get(i));
            pagerElements.set(i, pager);
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
        groupedActions.append(link);
        return this;
    }

    private void ensureCheckboxesVisibility() {
        // Check if the CheckBoxes column already exists
        final JsId jsid = new JsId("checkboxes");
        if (columns.get(jsid) == null) {
            columns.prepend(new TableColumn(this, jsid, ""));
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
        addGroupedAction(new ButtonAction("btn-" + id.toString(), label, tooltip, action), force);
        return this;
    }

    /**
     * Add an action to the table - action not link to checkbox, always visible
     */
    public Table addAction(Link link) {
        link.addClass(ALWAYS_ENABLE_CLASS);
        groupedActions.append(link);
        return this;
    }

    public Table addGroupedAction(final JsId id, final String label, final String tooltip, final Action action) {
        return this.addGroupedAction(id, label, tooltip, action, false);
    }

    public boolean hasGroupedActions() {
        return groupedActions.size() > 0;
    }

    /**
     * @return the groupedActions
     */
    public List<Link> getGroupedActions() {
        return groupedActions.getComponents();
    }

    private final List<String> selectedIds = new ArrayList<String>();

    private boolean saveCheckboxes = false;

    public Table saveCheckboxes(final boolean save) {
        saveCheckboxes = save;
        return this;
    }

    public boolean isSaveCheckboxes() {
        return saveCheckboxes;
    }

    /**
     * CLear checked lines memory and reset checkboxes
     */
    public void clearSelectedIds() {
        selectedIds.clear();
        clearCheckboxes();
    }

    /**
     * Uncheck all checkboxes
     */
    private void clearCheckboxes() {
        setCheckboxesValue(getAllCheckboxes(), false, false);
    }

    public List<String> getSelectedIds() {
        return selectedIds;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected ContainerStyled<TableFilter> filters = new ContainerStyled<TableFilter>();

    private boolean itemIdOnRow = false;

    public Table resetFilters() {
        filters.empty();
        return this;
    }

    public Table setShowSearch(final boolean show) {
        showSearch = show;

        if (isGenerated()) {
            final GQuery filter = $(".tablefilters > div:has(input[name=search])", rootElement);
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
        filters.append(filter);

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

        filters.append(select);
        return this;
    }

    public Table addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler) {
        return this.addSelectFilter(label, tooltip, name, filler, null);
    }

    public Table addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler, final String defaultValue) {
        final TableFilterSelect select = new TableFilterSelect(label, tooltip, name, defaultValue);
        select.setTable(this);
        select.setFiller(filler);

        filters.append(select);
        return this;
    }

    public Table addHiddenFilter(final String name, final String value) {

        final TableFilterHidden hidden = new TableFilterHidden(name, value);
        hidden.setTable(this);
        filters.append(hidden);
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

        final String search = $(".tablefilters input[name=search]:not(.empty)", getElement()).val();

        if ("null".equalsIgnoreCase(search)) {
            return "";
        }

        return search;

    }

    public Table setSearch(final String query) {
        defaultSearch = query;

        if (isGenerated()) {
            $(".tablefilters input[name=search]:not(.empty)", getElement()).val(this.defaultSearch);
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

        return order;
    }

    public Table setOrder(final String sortName) {
        return this.setOrder(sortName, true);
    }

    public Table setOrder(final String sortName, final boolean sortAscending) {
        order = sortName + (sortAscending ? " ASC" : " DESC");

        if (isGenerated()) {
            for (int i = 0; i < columns.size(); i++) {
                columns.get(i).setSorted(false);
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
        if (refreshEvery != null) {
            this.refreshFiller.setRepeatEvery(refreshEvery);
        }
    }

    public final void setSelectLineOnClick(boolean value) {
        selectLineOnClick = value;
    }

    @Override
    public void refresh() {
        if (refreshFiller != null) {
            refreshFiller.run();
        }
        runFillers();
    }

    public void setView(final VIEW_TYPE view) {
        defaultView = view;
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
        refreshEvery = milliseconds;

        if (refreshFiller != null) {
            refreshFiller.setRepeatEvery(milliseconds);
        }
    }

    public int getLinesNumber() {
        return lines.size();
    }

    private GQuery getCheckbox(String checkboxId) {
        return $(".td_checkboxes input[value=\"" + checkboxId + "\"]", tableElement);
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
        return checkboxes != null && checkboxes.length() > 0 && checkboxes.is("input[type='checkbox']");
    }

    private native void fireCssLabelEvent(Element e)
    /*-{
        $wnd.$(e).trigger("cssChange");
    }-*/;

    public void setItemIdOnRow(boolean itemIdOnRow) {
        this.itemIdOnRow = itemIdOnRow;

    }
}
