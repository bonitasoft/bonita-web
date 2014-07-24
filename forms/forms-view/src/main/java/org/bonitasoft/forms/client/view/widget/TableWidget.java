/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.client.view.widget;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.ReducedFormWidget.SelectMode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Selectable table widget
 * 
 * @author Anthony Birembaut
 * 
 */
public class TableWidget extends Composite implements HasValueChangeHandlers<List<String>>, ValueChangeHandler<List<String>>, ClickHandler {

    /**
     * Default style for selected table rows
     */
    private static final String SELECTED_ROW_DEFAULT_STYLE = "bonita_form_table_selected";

    /**
     * Default style for selectable table rows
     */
    private static final String SELECTABLE_ROW_DEFAULT_STYLE = "bonita_form_table_selectable";

    /**
     * Default style for table cells
     */
    private static final String TABLE_CELL_DEFAULT_STYLE = "bonita_form_table_cell";

    /**
     * Default style for table pagination controls
     */
    private static final String TABLE_PAGINATION_CONTROL_STYLE = "bonita_form_pagination_control";

    /**
     * Default style for table pagination text
     */
    private static final String TABLE_PAGINATION_TEXT_STYLE = "bonita_form_pagination_text";

    /**
     * Default style for table pagination controls container
     */
    private static final String TABLE_PAGINATION_CONTAINER_STYLE = "bonita_form_pagination_container";

    /**
     * The table available values
     */
    protected List<List<ReducedFormFieldAvailableValue>> availableValues;

    /**
     * The index of the column which is used as the value of the selected row(s)
     */
    protected int valueColumnIndex;

    /**
     * The selected items indexes
     */
    protected List<Integer> selectedItemIndexes = new ArrayList<Integer>();

    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;

    /**
     * The table that display the content
     */
    protected FlexTable flexTable;

    /**
     * The widget definition
     */
    protected ReducedFormWidget widgetData;

    /**
     * indicates whether the left column of a table widget should be considered as header or not
     */
    protected boolean leftHeadings;

    /**
     * indicates whether the top column of a table widget should be considered as header or not
     */
    protected boolean topHeadings;

    /**
     * indicates whether the right column of a table widget should be considered as header or not
     */
    protected boolean rightHeadings;

    /**
     * indicates whether the bottom row of a table widget should be considered as header or not
     */
    protected boolean bottomHeadings;

    /**
     * the max number of rows allowed
     */
    protected Integer maxRows = -1;

    /**
     * the max number of columns reached by a row
     */
    protected int columnCount = 0;

    /**
     * The index of the current page (for paginated tables)
     */
    protected int currentPageIndex = 0;

    /**
     * value change handlers registered for the widget
     */
    protected List<ValueChangeHandler<List<String>>> valueChangeHandlers;

    /**
     * Constructor
     * 
     * @param selectedItems
     */
    public TableWidget(final ReducedFormWidget widgetData, final List<String> selectedItems) {

        this.widgetData = widgetData;

        this.availableValues = widgetData.getTableAvailableValues();

        this.valueColumnIndex = widgetData.getValueColumnIndex();

        this.maxRows = widgetData.getMaxRows();

        this.flowPanel = new FlowPanel();

        createWidget(selectedItems);

        initWidget(this.flowPanel);
    }

    protected List<List<ReducedFormFieldAvailableValue>> getAvailableValuesList() {
        if (this.availableValues != null && SelectMode.NONE.equals(this.widgetData.getSelectMode()) && this.maxRows > 0) {

            if (this.currentPageIndex > getLastPageIndex()) {
                this.currentPageIndex = getLastPageIndex();
            }
            int startIndex = this.currentPageIndex * this.maxRows;
            return this.availableValues.subList(startIndex, Math.min(startIndex + this.maxRows, this.availableValues.size()));
        } else {
            return this.availableValues;
        }
    }

    protected int getLastPageIndex() {
        int lastPageIndex = 0;
        if (this.maxRows > 0) {
            final int nbOfValues = this.availableValues.size();
            lastPageIndex = nbOfValues / this.maxRows - 1;
            if (nbOfValues % this.maxRows > 0) {
                lastPageIndex++;
            }
        }
        return lastPageIndex;
    }

    /**
     * get the max column size of availableValues list
     * 
     * @return maxColumnNumber
     */
    protected int getMaxColumnNumber() {
        final List<List<ReducedFormFieldAvailableValue>> availableValuesList = getAvailableValuesList();
        int maxColumnNumber = 0;
        if (availableValuesList != null && availableValuesList.size() > 0) {
            for (int i = 0; i < availableValuesList.size(); i++) {
                if (maxColumnNumber < availableValuesList.get(i).size()) {
                    maxColumnNumber = availableValuesList.get(i).size();
                }
            }
        }
        if (this.widgetData.hasLeftHeadings()) {
            maxColumnNumber++;
        }
        if (this.widgetData.hasRightHeadings()) {
            maxColumnNumber++;
        }
        return maxColumnNumber;
    }

    protected void createWidget(final List<String> selectedItems) {

        this.flexTable = new FlexTable();
        this.flexTable.setStyleName("bonita_form_table");
        final String tableStyle = this.widgetData.getTableStyle();
        if (tableStyle != null && tableStyle.length() > 0) {
            this.flexTable.addStyleName(tableStyle);
        }
        int row = 0;
        int column = 0;
        this.columnCount = 0;
        final List<String> horizontalHeader = this.widgetData.getHorizontalHeader();
        if (horizontalHeader != null && !horizontalHeader.isEmpty()) {
            if (this.widgetData.hasTopHeadings()) {
                this.topHeadings = true;
                final int maxColumNumber = getMaxColumnNumber();
                for (final String header : horizontalHeader) {
                    if (column >= maxColumNumber) {
                        break;
                    }
                    this.flexTable.setWidget(row, column, getCellContent(header));
                    column++;
                }
                row++;
            }
            if (this.widgetData.hasBottomHeadings()) {
                this.bottomHeadings = true;
            }
        }
        final List<String> verticalHeader = this.widgetData.getVerticalHeader();
        if (verticalHeader != null && !verticalHeader.isEmpty()) {
            if (this.widgetData.hasLeftHeadings()) {
                this.leftHeadings = true;
            }
            if (this.widgetData.hasRightHeadings()) {
                this.rightHeadings = true;
            }
        }
        final List<List<ReducedFormFieldAvailableValue>> tableAvailableValues = getAvailableValuesList();
        if (tableAvailableValues != null) {
            for (final List<ReducedFormFieldAvailableValue> rowAvailableValues : tableAvailableValues) {
                column = 0;
                String header = null;
                if (this.leftHeadings || this.rightHeadings) {
                    int headerIndex = row;
                    if (this.topHeadings) {
                        headerIndex--;
                    }
                    if (this.widgetData.getVerticalHeader().size() > headerIndex) {
                        header = this.widgetData.getVerticalHeader().get(headerIndex);
                    }
                }
                if (this.leftHeadings) {
                    this.flexTable.setWidget(row, column, getCellContent(header));
                    column++;
                }
                for (final ReducedFormFieldAvailableValue availableValue : rowAvailableValues) {
                    if (availableValue != null) {
                        this.flexTable.setWidget(row, column, getCellContent(availableValue.getLabel()));
                    }
                    column++;
                }
                if (this.rightHeadings) {
                    this.flexTable.setWidget(row, column, getCellContent(header));
                    column++;
                }
                if (column > this.columnCount) {
                    this.columnCount = column;
                }
                row++;
            }
        }
        if (this.bottomHeadings) {
            column = 0;
            final int maxColumNumber = getMaxColumnNumber();
            for (final String header : this.widgetData.getHorizontalHeader()) {
                if (column >= maxColumNumber) {
                    break;
                }
                this.flexTable.setWidget(row, column, getCellContent(header));
                column++;
            }
            row++;
        }
        handleSelection();
        addCellsStyle(this.flexTable.getRowCount(), this.columnCount);
        final FlowPanel tableContainer = new FlowPanel();
        tableContainer.setStyleName("bonita_form_table_container");
        tableContainer.add(this.flexTable);
        this.flowPanel.add(tableContainer);
        setValue(selectedItems, false);

        if (SelectMode.NONE.equals(this.widgetData.getSelectMode()) && this.maxRows > 0) {
            final FlowPanel paginationPanel = new FlowPanel();
            paginationPanel.setStyleName(TABLE_PAGINATION_CONTAINER_STYLE);

            final Label lastPage = new Label(">I");
            lastPage.setStyleName(TABLE_PAGINATION_CONTROL_STYLE);
            lastPage.setTitle(FormsResourceBundle.getMessages().lastPageTitle());
            lastPage.setVisible(false);
            lastPage.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    TableWidget.this.currentPageIndex = getLastPageIndex();
                    TableWidget.this.flowPanel.clear();
                    createWidget(null);
                }
            });
            paginationPanel.add(lastPage);

            final Label nextPage = new Label(">");
            nextPage.setStyleName(TABLE_PAGINATION_CONTROL_STYLE);
            nextPage.setTitle(FormsResourceBundle.getMessages().nextPageTitle());
            nextPage.setVisible(false);
            nextPage.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    if (TableWidget.this.currentPageIndex < getLastPageIndex()) {
                        TableWidget.this.currentPageIndex++;
                    }
                    TableWidget.this.flowPanel.clear();
                    createWidget(null);
                }
            });
            paginationPanel.add(nextPage);

            if (this.currentPageIndex < getLastPageIndex()) {
                nextPage.setVisible(true);
                lastPage.setVisible(true);
            }

            final int firstItem = this.currentPageIndex * this.maxRows + 1;
            final int lastItem = firstItem + tableAvailableValues.size() - 1;
            final Label currentPage = new Label(FormsResourceBundle.getMessages().paginationWithinLabel(Integer.toString(firstItem),
                    Integer.toString(lastItem), Integer.toString(this.availableValues.size())));
            currentPage.setStyleName(TABLE_PAGINATION_TEXT_STYLE);
            paginationPanel.add(currentPage);

            final Label previousPage = new Label("<");
            previousPage.setStyleName(TABLE_PAGINATION_CONTROL_STYLE);
            previousPage.setTitle(FormsResourceBundle.getMessages().previousPageTitle());
            previousPage.setVisible(false);
            previousPage.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    if (TableWidget.this.currentPageIndex > 0) {
                        TableWidget.this.currentPageIndex--;
                    }
                    TableWidget.this.flowPanel.clear();
                    createWidget(null);
                }
            });
            paginationPanel.add(previousPage);

            final Label firstPage = new Label("I<");
            firstPage.setStyleName(TABLE_PAGINATION_CONTROL_STYLE);
            firstPage.setTitle(FormsResourceBundle.getMessages().firstPageTitle());
            firstPage.setVisible(false);
            firstPage.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    TableWidget.this.currentPageIndex = 0;
                    TableWidget.this.flowPanel.clear();
                    createWidget(null);
                }
            });
            paginationPanel.add(firstPage);

            if (this.currentPageIndex > 0) {
                firstPage.setVisible(true);
                previousPage.setVisible(true);
            }

            this.flowPanel.add(paginationPanel);
        }
    }

    protected Widget getCellContent(final String content) {
        final HTML cellContent = new HTML();
        if (this.widgetData.allowHTMLInField()) {
            cellContent.setHTML(content);
        } else {
            cellContent.setText(content);
        }
        return cellContent;
    }

    protected void handleSelection() {
        if (!SelectMode.NONE.equals(this.widgetData.getSelectMode()) && !this.widgetData.isReadOnly()) {
            for (int row = 0; row < this.flexTable.getRowCount(); row++) {
                if (!(this.topHeadings && row <= 0)
                        && !(this.bottomHeadings && row >= this.flexTable.getRowCount() - 1)) {
                    this.flexTable.getRowFormatter().setStyleName(row, SELECTABLE_ROW_DEFAULT_STYLE);
                }
            }
            this.flexTable.addClickHandler(this);
        }
    }

    protected void addSelectedCellStyle(final int row) {
        this.flexTable.getRowFormatter().addStyleName(row, SELECTED_ROW_DEFAULT_STYLE);
        if (this.widgetData.getSelectedItemsStyle() != null && this.widgetData.getSelectedItemsStyle().length() > 0) {
            this.flexTable.getRowFormatter().addStyleName(row, this.widgetData.getSelectedItemsStyle());
        }
    }

    protected void removeSelectedCellStyle(final int row) {
        this.flexTable.getRowFormatter().removeStyleName(row, SELECTED_ROW_DEFAULT_STYLE);
        if (this.widgetData.getSelectedItemsStyle() != null && this.widgetData.getSelectedItemsStyle().length() > 0) {
            this.flexTable.getRowFormatter().removeStyleName(row, this.widgetData.getSelectedItemsStyle());
        }
    }

    protected void addCellsStyle(final int rowCount, final int columnCount) {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                this.flexTable.getFlexCellFormatter().setStyleName(row, column, TABLE_CELL_DEFAULT_STYLE);
                if (this.widgetData.getCellsStyle() != null && this.widgetData.getCellsStyle().length() > 0) {
                    this.flexTable.getFlexCellFormatter().addStyleName(row, column, this.widgetData.getCellsStyle());
                }
                if (column == 0 && this.widgetData.hasLeftHeadings() || column == columnCount - 1 && this.widgetData.hasRightHeadings()) {
                    if (this.widgetData.getHeadingsStyle() != null && this.widgetData.getHeadingsStyle().length() > 0) {
                        this.flexTable.getFlexCellFormatter().addStyleName(row, column, this.widgetData.getHeadingsStyle());
                    }
                }
                if (row == 0 && this.widgetData.hasTopHeadings() || row == rowCount - 1 && this.widgetData.hasBottomHeadings()) {
                    if (this.widgetData.getHeadingsStyle() != null && this.widgetData.getHeadingsStyle().length() > 0) {
                        this.flexTable.getFlexCellFormatter().addStyleName(row, column, this.widgetData.getHeadingsStyle());
                    }
                }
            }
        }
    }

    public void setValue(final List<String> selectedItems, final boolean fireEvents) {
        if (!SelectMode.NONE.equals(this.widgetData.getSelectMode())) {
            for (int row = 0; row < this.flexTable.getRowCount(); row++) {
                removeSelectedCellStyle(row);
            }
            this.selectedItemIndexes.clear();
            if (selectedItems != null && !selectedItems.isEmpty() && this.availableValues != null) {
                for (final List<ReducedFormFieldAvailableValue> rowAvailableValues : this.availableValues) {
                    if (rowAvailableValues.size() >= this.valueColumnIndex
                            && rowAvailableValues.get(this.valueColumnIndex) != null
                            && selectedItems.contains(rowAvailableValues.get(this.valueColumnIndex).getValue())) {
                        int rowIndex = this.availableValues.indexOf(rowAvailableValues);
                        if (this.topHeadings) {
                            rowIndex++;
                        }
                        addSelectedCellStyle(rowIndex);
                        if (SelectMode.MULTIPLE.equals(this.widgetData.getSelectMode())) {
                            this.selectedItemIndexes.add(Integer.valueOf(rowIndex));
                        } else if (SelectMode.SINGLE.equals(this.widgetData.getSelectMode())) {
                            this.selectedItemIndexes.add(0, Integer.valueOf(rowIndex));
                        }
                    }
                }
            }
            if (fireEvents) {
                for (final ValueChangeHandler<List<String>> valueChangeHandler : valueChangeHandlers) {
                    valueChangeHandler.onValueChange(null);
                }
            }
        }
    }

    public List<String> getValue() {
        final List<String> selectedItems = new ArrayList<String>();
        for (final Integer selectedItemIndex : this.selectedItemIndexes) {
            int selectedValueIndex = selectedItemIndex.intValue();
            if (this.topHeadings) {
                selectedValueIndex--;
            }
            final List<ReducedFormFieldAvailableValue> rowAvailableValues = this.availableValues.get(selectedValueIndex);
            final ReducedFormFieldAvailableValue availableValue = rowAvailableValues.get(this.valueColumnIndex);
            if (availableValue != null) {
                selectedItems.add(availableValue.getValue());
            }
        }
        return selectedItems;
    }

    public void setAvailableValues(final List<List<ReducedFormFieldAvailableValue>> availableValues, final boolean fireEvents) {
        this.flowPanel.clear();
        this.availableValues = availableValues;
        createWidget(null);
        if (fireEvents) {
            for (final ValueChangeHandler<List<String>> valueChangeHandler : valueChangeHandlers) {
                valueChangeHandler.onValueChange(null);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(final ClickEvent clickEvent) {
        if (!SelectMode.NONE.equals(this.widgetData.getSelectMode()) && !this.widgetData.isReadOnly()) {
            final Cell clickedCell = this.flexTable.getCellForEvent(clickEvent);
            final int rowIndex = clickedCell.getRowIndex();
            if (!(this.topHeadings && rowIndex <= 0)
                    && !(this.bottomHeadings && rowIndex >= this.flexTable.getRowCount() - 1)) {
                if (SelectMode.MULTIPLE.equals(this.widgetData.getSelectMode())) {
                    if (this.selectedItemIndexes.contains(Integer.valueOf(rowIndex))) {
                        removeSelectedCellStyle(rowIndex);
                        this.selectedItemIndexes.remove(Integer.valueOf(rowIndex));
                    } else {
                        addSelectedCellStyle(rowIndex);
                        this.selectedItemIndexes.add(Integer.valueOf(rowIndex));
                    }
                } else {
                    if (!this.selectedItemIndexes.isEmpty() && this.selectedItemIndexes.get(0).equals(Integer.valueOf(rowIndex))) {
                        removeSelectedCellStyle(rowIndex);
                        this.selectedItemIndexes.remove(Integer.valueOf(rowIndex));
                    } else {
                        Integer oldSelectedItemIndex = null;
                        if (!this.selectedItemIndexes.isEmpty()) {
                            oldSelectedItemIndex = this.selectedItemIndexes.get(0);
                        }
                        if (oldSelectedItemIndex != null) {
                            removeSelectedCellStyle(oldSelectedItemIndex.intValue());
                            this.selectedItemIndexes.remove(oldSelectedItemIndex);
                        }
                        addSelectedCellStyle(rowIndex);
                        this.selectedItemIndexes.add(0, Integer.valueOf(rowIndex));
                    }
                }
                for (final ValueChangeHandler<List<String>> valueChangeHandler : valueChangeHandlers) {
                    valueChangeHandler.onValueChange(null);
                }
            }
        }
    }

    /**
     * Custom Handler registration
     */
    protected class EventHandlerRegistration implements HandlerRegistration {

        protected EventHandler eventHandler;

        public EventHandlerRegistration(final EventHandler eventHandler) {
            this.eventHandler = eventHandler;
        }

        @SuppressWarnings("unchecked")
        public void removeHandler() {
            if (eventHandler instanceof ValueChangeHandler) {
                valueChangeHandlers.remove(eventHandler);
            }

        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<List<String>> event) {
        for (final ValueChangeHandler<List<String>> valueChangeHandler : valueChangeHandlers) {
            valueChangeHandler.onValueChange(event);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> handler) {
        if (valueChangeHandlers == null) {
            valueChangeHandlers = new ArrayList<ValueChangeHandler<List<String>>>();
        }
        valueChangeHandlers.add(handler);
        return new EventHandlerRegistration(handler);
    }
}
