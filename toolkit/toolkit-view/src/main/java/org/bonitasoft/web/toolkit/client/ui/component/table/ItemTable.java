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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedJsId;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasCounters;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasReaders;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.popup.DeleteMultipleItemsPopupAction;
import org.bonitasoft.web.toolkit.client.ui.action.popup.ItemDeletePopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Refreshable;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerDummy;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.DefaultItemTableCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public class ItemTable extends AbstractTable implements Refreshable, FormNode {

    protected Table table = null;

    protected ItemDefinition itemDefinition = null;

    private final ArrayList<AbstractAttributeReader> columns = new ArrayList<AbstractAttributeReader>();

    protected List<ItemTableActionSet> actionSets = new LinkedList<ItemTableActionSet>();

    protected Map<String, ItemTableCellFormatter> cellFormatters = new HashMap<String, ItemTableCellFormatter>();

    private int actionColumnPosition = -1;

    private APIID defaultSelectedId = null;

    private Integer defaultSelectedLine = null;

    private boolean registerRefresh = true;

    private HashMap<String, String> attributesForGroupedActions = new HashMap<String, String>();

    private boolean itemIdOnRow = false;

    protected final HashMap<String, IItem> loadedItems = new HashMap<String, IItem>();

    public ItemTable(final ItemDefinition itemDefinition) {
        this(null, itemDefinition);
    }

    public ItemTable(final ItemDefinition itemDefinition, boolean itemIdOnRow) {
        this(null, itemDefinition);
        this.itemIdOnRow = itemIdOnRow;
    }

    public ItemTable(final JsId jsid, final ItemDefinition itemDefinition) {
        super(jsid);

        assert itemDefinition != null;
        this.itemDefinition = itemDefinition;
        this.table = new Table(jsid);
        setFillOnRefresh(true);
    }

    public HandlerRegistration addItemCheckedHandler(ItemCheckedHandler handler) {
        return this.table.addItemCheckedHandler(handler);
    }

    public HandlerRegistration addItemUncheckedHandler(ItemUncheckedHandler handler) {
        return this.table.addItemUncheckedHandler(handler);
    }

    public HandlerRegistration addItemTableLoadedHandler(ItemTableLoadedHandler handler) {
        return addHandler(handler, ItemTableLoadedEvent.TYPE);
    }

    public ItemTable setRegisterRefresh(final boolean register) {
        this.registerRefresh = register;
        return this;
    }

    /**
     * Define the line to select by default.
     * 
     * @param line
     *            the zero based index of the line
     */
    public ItemTable setDefaultSelectedLine(final Integer line) {
        this.defaultSelectedLine = line;
        return this;
    }

    public Integer getDefaultSelectedLine() {
        return this.defaultSelectedLine;
    }

    public void setDefaultSelectedId(final APIID id) {
        this.defaultSelectedId = id;
    }

    public APIID getDefaultSelectedId() {
        return this.defaultSelectedId;
    }

    public final ItemDefinition<?> getItemDefinition() {
        return this.itemDefinition;
    }

    @Override
    public final ItemTable setFillOnLoad(final boolean fillOnLoad) {
        super.setFillOnLoad(fillOnLoad);
        this.table.setFillOnLoad(fillOnLoad);
        return this;
    }

    public ItemTable setItems(final List<IItem> items) {
        resetLines();
        addItems(items);
        fireEvent(new ItemTableLoadedEvent(items));
        return this;
    }

    public final ItemTable addItems(final List<IItem> items) {

        if (!this.table.isSaveCheckboxes()) {
            this.table.clearSelectedIds();
        }

        int i = 1; // index of the first line of the table
        int selectedIndex = -1;
        for (final IItem item : items) {
            this.addItem(item);
            if (this.defaultSelectedId != null && this.defaultSelectedId.equals(item.getId())) {
                selectedIndex = i;
            } else if (this.defaultSelectedLine != null && this.defaultSelectedLine == i - 1) {
                selectedIndex = i;
            }
            i++;
        }
        this.table.updateHtml();
        if (selectedIndex > -1) {
            $(".tr_" + selectedIndex, getElement()).click();
        }

        return this;
    }

    protected final ItemTable addItem(final IItem item) {
        return this.addItem(item, null);
    }

    protected ItemTable addItem(final IItem item, final String className) {

        loadedItems.put(item.getId().toString(), item);

        if (this.actionColumnPosition == -1 && !this.actionSets.isEmpty()) {
            addActionColumn();
        }

        // Get default action (if there is one)
        Action defAction = null;
        final int lineNumber = this.table.getLinesNumber();
        if (this.defaultAction != null) {
            defAction = new Action() {

                @Override
                public void execute() {
                    ItemTable.this.defaultAction.addParameter("id", item.getId().toString());
                    ItemTable.this.defaultAction.addParameter("cell_index", String.valueOf(lineNumber + 1));// +1 because the line is build in the next
                                                                                                            // instruction
                    ItemTable.this.defaultAction.execute();
                }
            };
        } else if (this.actionSets != null) {
            for (final ItemTableActionSet actionSet : this.actionSets) {
                defAction = actionSet.getDefaultAction(item);
                if (defAction != null) {
                    break;
                }
            }
        }
        if (defAction != null) {
            defAction.addParameter("id", item.getId().toString());
            defAction.addParameter("cell_index", String.valueOf(this.table.getLinesNumber() + 1));// +1 because the line is build in the next instruction
        }

        // Create the line component
        this.table.setItemIdOnRow(this.itemIdOnRow);
        this.table.addLine(item.getId().toString(), className, defAction, isGroupedActionAllowed(item));

        // Fill it with data columns
        addItemCells(item);

        // Add the actions column
        // this.addItemActions(item);

        return this;
    }

    private Boolean isGroupedActionAllowed(IItem item) {
        if (attributesForGroupedActions.isEmpty()) {
            return true;
        } else {
            Iterator it = attributesForGroupedActions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                if (item.getAttributeValue(pairs.getKey().toString()).equals(pairs.getValue())) {
                    return false;
                }
            }
            return true;
        }
    }

    public ItemTable addAttributeToCheckForGroupedActions(String attributeName, String value) {
        attributesForGroupedActions.put(attributeName, value);
        return this;
    }

    protected final void addItemCells(final IItem item) {
        int index = 0;

        for (final AbstractAttributeReader column : this.columns) {

            if (index == this.actionColumnPosition) {
                this.addItemActions(item);
            } else {

                ItemTableCellFormatter cellFormatter = null;

                if (column instanceof HasDeploys) {
                    /**
                     * Get first cell formatter met for attributes deployed on our table
                     */
                    for (final String deployedAttribute : ((HasDeploys) column).getDeploys()) {

                        final String compoundAttribute = new DeployedJsId(deployedAttribute, column.getLeadAttribute()).toString();
                        if (this.cellFormatters.containsKey(compoundAttribute)) {
                            cellFormatter = this.cellFormatters.get(compoundAttribute);
                            break;
                        }
                    }

                } else {
                    cellFormatter = this.cellFormatters.get(column.getLeadAttribute());
                }

                if (cellFormatter == null) {
                    cellFormatter = new DefaultItemTableCellFormatter();
                }

                cellFormatter.setTable(this.table);
                cellFormatter.setColumn(this.table.getLastColumn());
                cellFormatter.setLine(this.table.getLastLine());
                cellFormatter.setAttribute(column);
                cellFormatter.setItem(item);

                cellFormatter.execute();
            }
            index++;
        }
    }

    protected void addItemActions(final IItem item) {
        this.addItemActions(item, getActionsFor(item));
    }

    protected void addItemActions(final IItem item, final List<ItemTableAction> actions) {
        final ContainerDummy<ItemTableAction> actionComponents = new ContainerDummy<ItemTableAction>();
        for (final ItemTableAction itemTableAction : actions) {
            itemTableAction.addParameter("cell_index", String.valueOf(this.table.getLinesNumber()));
            itemTableAction.addParameter("id", item.getId().toString());
            actionComponents.append(itemTableAction);
        }
        this.table.addCell(actionComponents);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemTable resetFilters() {
        this.table.resetFilters();
        return this;
    }

    public final ItemTable addTextFilter(final String label, final String tooltip, final String name) {
        this.table.addTextFilter(label, tooltip, name);
        return this;
    }

    public final ItemTable addTextFilter(final String label, final String tooltip, final String name, final String defaultValue) {
        this.table.addTextFilter(label, tooltip, name, defaultValue);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final Map<String, String> values) {
        this.table.addSelectFilter(label, tooltip, name, values);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final LinkedHashMap<String, String> values,
            final String defaultValue) {
        this.table.addSelectFilter(label, tooltip, name, values, defaultValue);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler) {
        this.table.addSelectFilter(label, tooltip, name, filler);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler, final String defaultValue) {
        this.table.addSelectFilter(label, tooltip, name, filler, defaultValue);
        return this;
    }

    public final ItemTable addHiddenFilter(final String name, final String value) {
        this.table.addHiddenFilter(name, value);
        return this;
    }

    public final ItemTable addHiddenFilter(final String name, final APIID value) {
        if (value == null) {
            return addHiddenFilter(name, (String) null);
        }
        return addHiddenFilter(name, value.toString());
    }

    public ItemTable resetHiddenFilters() {
        this.table.resetHiddenFilters();
        return this;
    }

    public final ItemTable addHiddenFilters(final Map<String, String> filters) {
        this.table.addHiddenFilters(filters);
        return this;
    }

    public final ItemTable addHiddenFilter(final Map<String, String> filters) {
        this.table.addHiddenFilters(filters);
        return this;
    }

    public final Map<String, String> getFilters() {
        return this.table.getFilters();
    }

    public final Map<String, String> getHiddenFilters() {
        return this.table.getHiddenFilters();
    }

    public final List<String> getDeploys() {
        final List<String> result = new ArrayList<String>();
        for (final AbstractAttributeReader reader : this.columns) {
            getAttributeReaderDeploys(reader, result);
        }
        return result;
    }

    private List<String> getAttributeReaderDeploys(final AbstractAttributeReader reader, final List<String> deploys) {
        if (reader instanceof HasDeploys) {
            deploys.addAll(((HasDeploys) reader).getDeploys());
        }
        if (reader instanceof HasReaders) {
            final Map<String, AbstractAttributeReader> readers = ((HasReaders) reader).getAttributeReaders();
            if (readers != null) {
                for (final AbstractAttributeReader attributeReader : readers.values()) {
                    getAttributeReaderDeploys(attributeReader, deploys);
                }
            }
        }
        return deploys;
    }

    public final List<String> getCounters() {
        final List<String> result = new ArrayList<String>();
        // go through readers
        for (final AbstractAttributeReader reader : this.columns) {
            if (reader instanceof HasCounters) {
                result.addAll(((HasCounters) reader).getCounters());
            }
        }

        // go through actions
        for (final ItemTableActionSet<?> actionSet : this.actionSets) {
            if (actionSet instanceof HasCounters) {
                result.addAll(((HasCounters) actionSet).getCounters());
            }
        }

        return result;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COLUMNS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemTable addActionColumn() {
        // this.table.addColumn(new JsId("actions"), _("Actions"));
        this.actionColumnPosition = this.columns.size();
        this.columns.add(null);

        return this;
    }

    public final ItemTable addColumn(final AbstractAttributeReader attribute, final String label) {
        return this.addColumn(attribute, label, false);
    }

    public final ItemTable addColumn(final AbstractAttributeReader attribute, final String label, final boolean sorted, final boolean ascendant) {
        this.table.addColumn(new JsId(attribute.getLeadAttribute()), label, attribute.getLeadAttribute(), sorted, ascendant);
        this.columns.add(attribute);

        return this;
    }

    public final ItemTable addColumn(final AbstractAttributeReader attribute, final String label, final boolean sortable) {
        JsId columnJsid;

        // Get first attribute's reader first deploy to identify column. Used by formatters.
        if (attribute instanceof HasDeploys) {
            columnJsid = new DeployedJsId(((HasDeploys) attribute).getDeploys().get(0), attribute.getLeadAttribute());
        } else {
            columnJsid = new JsId(attribute.getLeadAttribute());
        }

        this.table.addColumn(columnJsid, label, sortable ? attribute.getLeadAttribute() : null);
        this.columns.add(attribute);

        return this;
    }

    public final ItemTable addColumn(final String attributeName, final String label) {
        return this.addColumn(attributeName, label, false);
    }

    public final ItemTable addColumn(final String attributeName, final String label, final boolean sorted, final boolean ascendant) {
        this.table.addColumn(new JsId(attributeName), label, attributeName, sorted, ascendant);
        this.columns.add(new AttributeReader(attributeName));

        return this;
    }

    public final ItemTable addColumn(final String attributeName, final String label, final boolean sortable) {
        if (sortable) {
            this.table.addColumn(new JsId(attributeName), label, attributeName);
        } else {
            this.table.addColumn(new JsId(attributeName), label);
        }
        this.columns.add(new AttributeReader(attributeName));

        return this;
    }

    /**
     * @param jsid
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getColumn(org.bonitasoft.web.toolkit.client.ui.JsId)
     */
    public TableColumn getColumn(final JsId jsid) {
        return this.table.getColumn(jsid);
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getColumns()
     */
    public List<TableColumn> getColumns() {
        return this.table.getColumns();
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getLastColumn()
     */
    public TableColumn getLastColumn() {
        return this.table.getLastColumn();
    }

    public final ArrayList<String> getColumnsName() {
        final ArrayList<String> results = new ArrayList<String>();
        for (final AbstractAttributeReader attribute : this.columns) {
            results.add(attribute.getLeadAttribute());
        }
        return results;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CELL FORMATTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final ItemTable addCellFormatter(final String attributeName, final ItemTableCellFormatter cellFormatter) {
        this.cellFormatters.put(attributeName, cellFormatter);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Action defaultAction = null;

    /**
     * @param defaultAction
     *            the defaultAction to set
     */
    public final ItemTable setDefaultAction(final Action defaultAction) {
        this.defaultAction = defaultAction;
        return this;
    }

    public final ItemTable resetActions() {
        this.actionSets.clear();
        return this;
    }

    public final ItemTable setActions(final ItemTableActionSet itemTableActionSet) {
        resetActions();
        return addActions(itemTableActionSet);
    }

    public final ItemTable addActions(final ItemTableActionSet itemTableActionSet) {
        itemTableActionSet.setItemTable(this);
        this.actionSets.add(itemTableActionSet);

        return this;
    }

    public final ItemTable addGroupedAction(final JsId id, final String label, final String tooltip, final Action action) {
        this.table.addGroupedAction(id, label, tooltip, action);
        return this;
    }

    /**
     * Add an action to the table - action not link to checkbox, always visible
     */
    public final ItemTable addAction(Link link) {
        this.table.addAction(link);
        return this;
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getGroupedActions()
     */
    public List<Link> getGroupedActions() {
        return this.table.getGroupedActions();
    }

    /**
     * 
     * @param link
     * @param force
     *            Force the visibility of the action (can't be disabled)
     * @return
     */
    public final ItemTable addGroupedAction(Link link, final boolean force) {
        this.table.addGroupedAction(link, force);
        return this;
    }

    public final ItemTable addGroupedAction(Link link) {
        return addGroupedAction(link, false);
    }

    /**
     * 
     * @param label
     * @param tooltip
     * @param action
     * @param force
     *            Force the visibility of the action (can't be disabled)
     */
    public final ItemTable addGroupedAction(final JsId id, final String label, final String tooltip, final Action action, final boolean force) {
        this.table.addGroupedAction(id, label, tooltip, action, force);
        return this;
    }

    public final ItemTable addGroupedDeleteAction(final String tooltip, final ItemDefinition definition) {
        this.table.addGroupedAction(new JsId("delete"), _("Delete"), tooltip, new ItemDeletePopupAction(definition));
        return this;
    }

    public final ItemTable addGroupedMultipleDeleteAction(final String tooltip, final ItemDefinition definition, final String itemName,
            final String itemNamePlural) {
        this.table.addGroupedAction(new JsId("delete"), _("Delete"), tooltip, new DeleteMultipleItemsPopupAction(definition, itemName, itemNamePlural));
        return this;
    }

    public List<ItemTableAction> getActionsFor(final IItem item) {
        if (this.actionSets == null) {
            return new LinkedList<ItemTableAction>();
        }

        final List<ItemTableAction> actions = new LinkedList<ItemTableAction>();

        for (final ItemTableActionSet set : this.actionSets) {
            actions.addAll(set.getActionsFor(item));
        }
        return actions;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final ItemTable setNbLinesByPage(final int nbLinesByPage) {
        this.table.setPager(0, 0, nbLinesByPage);
        return this;
    }

    public final ItemTable setView(final VIEW_TYPE view) {
        this.table.setView(view);
        return this;
    }

    public final ItemTable saveCheckboxes(final boolean save) {
        this.table.saveCheckboxes(save);
        return this;
    }

    public final ItemTable setRefreshEvery(final int milliseconds) {
        this.table.setRefreshEvery(milliseconds);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HTML GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {
        if (this.actionSets != null && this.actionSets.size() > 0 && this.actionColumnPosition == -1) {
            final JsId actionJsId = new JsId("actions");

            this.table.addColumn(actionJsId, _("actions"));
            if (this.actionColumnPosition > -1) {
                this.table.setColumnPos(actionJsId, this.actionColumnPosition);
            }
        }

        return this.table.getElement();
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();

        final ItemTableFiller filler = new ItemTableFiller();
        this.table.setRefreshFiller(filler);
        filler.setTarget(this);
        setFiller(filler);

    }

    @Override
    public void refresh() {
        refresh(null);
    }

    public void refresh(final Action callback) {
        runFillers(callback);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERIC DELEGATIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final ItemTable addClass(final String className) {
        if (this.table != null) {
            this.table.addClass(className);
        }
        return this;
    }

    public final int getNbPages() {
        return this.table.getNbPages();
    }

    public final int getNbLinesByPage() {
        return this.table.getNbLinesByPage();
    }

    public final int getPage() {
        return this.table.getPage();
    }

    public final List<String> getSelectedIds() {
        return this.table.getSelectedIds();
    }

    public final String getSearch() {
        return this.table.getSearch();
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#setSearch(java.lang.String)
     */
    public final ItemTable setSearch(final String query) {
        this.table.setSearch(query);
        return this;
    }

    public final ItemTable setSelectLineOnClick(boolean value) {
        this.table.setSelectLineOnClick(value);
        return this;
    }

    public final String getOrder() {
        return this.table.getOrder();
    }

    public final ItemTable resetLines() {
        this.table.resetLines();
        this.loadedItems.clear();
        return this;
    }

    public final ItemTable setPager(final int currentPage, final int nbPages, final int nbLinesByPage) {
        this.table.setPager(currentPage, nbPages, nbLinesByPage);
        return this;
    }

    public final ItemTable setPage(final int page) {
        this.table.setPage(page);
        return this;
    }

    public final void changePage(final int page) {
        this.table.changePage(page);
    }

    @Override
    public final String toString() {
        return this.table.toString();
    }

    public final boolean hasGroupedActions() {
        return this.table.hasGroupedActions();
    }

    public final ItemTable setOrder(final String sortName, final boolean sortAscending) {
        this.table.setOrder(sortName, sortAscending);
        return this;
    }

    public ItemTable setShowSearch(final boolean show) {
        this.table.setShowSearch(show);
        return this;
    }

    public void updateView() {
        this.table.updateView();
    }

    public IItem getItem(String itemId) {
        return loadedItems.get(itemId);
    }

}
