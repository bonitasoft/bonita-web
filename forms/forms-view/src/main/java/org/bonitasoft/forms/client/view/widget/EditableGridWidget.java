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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.view.widget;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.view.common.DOMUtils;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Anthony Birembaut
 *
 */
public class EditableGridWidget extends Composite implements HasValueChangeHandlers<List<List<String>>> {
    
    /**
     * Default style for table cells in edition
     */
    private static final String TABLE_CELL_EDITION_STYLE = "bonita_form_table_edition";
    
    /**
     * Default style for editable table cells
     */
    private static final String TABLE_CELL_EDITABLE_STYLE = "bonita_form_table_editable";
    
    /**
     * Default style for table cells
     */
    private static final String TABLE_CELL_DEFAULT_STYLE = "bonita_form_table_cell";
    
    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;

    /**
     * The panel containing non-readonly grid
     */
    protected FlowPanel tableContainer;
    
    /**
     * the row buttons container
     */
    protected FlowPanel rowButtonsContainer;
    
    /**
     * the column buttons container
     */
    protected FlowPanel columnButtonsContainer;
    
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
     * the number of columns by row
     */
    protected int columnCount = 0;

    /**
     * the max number of columns allowed
     */
    protected Integer maxColumns = -1;

    /**
     * the min number of columns allowed
     */
    protected Integer minColumns = -1;

    /**
     * the max number of rows allowed
     */
    protected Integer maxRows = -1;

    /**
     * the min number of rows allowed
     */
    protected Integer minRows = -1;

    /**
     * Add row button
     */
    protected Label addRowButton;

    /**
     * remove row button
     */
    protected Label removeRowButton;
    
    /**
     * Add column button
     */
    protected Label addColumnButton;

    /**
     * remove column button
     */
    protected Label removeColumnButton;
    
    /**
     * The formID retrieved from the request as a String
     */
    protected String formID;
    
    /**
     * The current cells values
     */
    protected List<List<String>> currentCellsValues = new ArrayList<List<String>>();
    
    /**
     * Click handler to add a row
     */
    protected AddRowClickHandler addRowClickHandler = new AddRowClickHandler(this);
    
    /**
     * Click handler to add a column
     */
    protected AddColumnClickHandler addColumnClickHandler = new AddColumnClickHandler(this);
    
    /**
     * Click handler to remove a row
     */
    protected RemoveRowClickHandler removeRowClickHandler = new RemoveRowClickHandler(this);
    
    /**
     * Click handler to remove a column
     */
    protected RemoveColumnClickHandler removeColumnClickHandler = new RemoveColumnClickHandler(this);

    /**
     * Constructor
     * @param selectedItems
     */
    public EditableGridWidget(final String formID, final ReducedFormWidget widgetData, final List<List<String>> value) {

        this.formID = formID;
        this.widgetData = widgetData;
        
        maxColumns = widgetData.getMaxColumns();
        minColumns = widgetData.getMinColumns();
        maxRows = widgetData.getMaxRows();
        minRows = widgetData.getMinRows();

        flowPanel = new FlowPanel();
        
        createWidget(value);

        initWidget(flowPanel);
    }

    protected void createWidget(final List<List<String>> values) {

        flexTable = new FlexTable();
        if (widgetData.isReadOnly()) {
            flexTable.setStyleName("bonita_form_table");
        } else {
            flexTable.setStyleName("bonita_form_editable_grid");

            columnButtonsContainer = new FlowPanel();
            columnButtonsContainer.addStyleName("bonita_form_grid_column_buttons");
            flowPanel.add(columnButtonsContainer);
        }
        final String tableStyle = widgetData.getTableStyle();
        if (tableStyle != null && tableStyle.length() > 0) {
        	flexTable.addStyleName(tableStyle);
        }
        int extendedMaxColumns = maxColumns;
        int maxColumnsIncludingLeftHeader = maxColumns;
        int maxRowsIncludingTopHeader = maxRows;
        final List<String> horizontalHeader = widgetData.getHorizontalHeader();
        if (horizontalHeader != null && !horizontalHeader.isEmpty()) {
            if (widgetData.hasTopHeadings()) {
                topHeadings = true;
                maxRowsIncludingTopHeader++;
            }
            if (widgetData.hasBottomHeadings()) {
                bottomHeadings = true;
            }
        }
        final List<String> verticalHeader = widgetData.getVerticalHeader();
        if (verticalHeader != null && !verticalHeader.isEmpty()) {
            if (widgetData.hasLeftHeadings()) {
                leftHeadings = true;
                extendedMaxColumns++;
                maxColumnsIncludingLeftHeader++;
            }
            if (widgetData.hasRightHeadings()) {
                rightHeadings = true;
                extendedMaxColumns++;
            }
        }
        
        int row = 0;
        int column = 0;
        columnCount = 0;
        if (topHeadings) {
            final List<String> topHeaderCellsValues = new ArrayList<String>();
			final int verticalHeadings = (leftHeadings && rightHeadings) ? 2
					: ((leftHeadings || rightHeadings) ? 1 : 0);
			final int showTableColumns = getTableColumnNum(values) + verticalHeadings;
            for (final String header : horizontalHeader) {
				if (column < showTableColumns) {
                    if (maxColumns < 0 || column < extendedMaxColumns) {
                        topHeaderCellsValues.add(header);
                        createCellContent(row, column, header, false);
                        column++;
                    } else {
                        break;
                    }                    
                }
            }
            currentCellsValues.add(topHeaderCellsValues);
            if (column > columnCount) {
                columnCount = column;
            }
            row++;
        }
        
        if (values != null) {
            for (final List<String> rowValues : values) {
                if (maxRows < 0 || row < maxRowsIncludingTopHeader) {
                    column = 0;
                    String header = null;
                    if (leftHeadings || rightHeadings) {
                        final int headerIndex = topHeadings ? row - 1 : row;
                        if (widgetData.getVerticalHeader().size() > headerIndex) {
                            header = widgetData.getVerticalHeader().get(headerIndex);
                        }
                    }
                    final List<String> currentRowCellsValues = new ArrayList<String>();
                    if (leftHeadings) {
                        currentRowCellsValues.add(header);
                        createCellContent(row, column, header, false);
                        column++;
                    }
                    for (final String value : rowValues) {
                        if (maxColumns < 0 || column < maxColumnsIncludingLeftHeader) {
                            currentRowCellsValues.add(value);
                            createCellContent(row, column, value, false);
                            if (!widgetData.isReadOnly()) {
                                addEditableCellStyle(row, column);
                            }
                            column++;
                        } else {
                            break;
                        }
                    }
                    column = addMissingColumns(row, column, currentRowCellsValues, rowValues.size(), minColumns);
                    if (rightHeadings) {
                        currentRowCellsValues.add(header);
                        createCellContent(row, column, header, false);
                        column++;
                    }
                    currentCellsValues.add(currentRowCellsValues);
                    if (column > columnCount) {
                        columnCount = column;
                    }
                    row++;
                } else {
                    break;
                }
            }
            final int rowsToAdd = minRows - values.size();
            if(rowsToAdd > 0) {
                for (int i = 0; i < rowsToAdd; i++) {
                    createNewRow(true);
                }
            }
        }
        if (topHeadings) {
            final int horizontalHeaderSize = horizontalHeader.size();
            addMissingColumns(0, horizontalHeaderSize, currentCellsValues.get(0), horizontalHeaderSize, columnCount);
        }
        if (bottomHeadings) {
            column = 0;
            row = flexTable.getRowCount();
            final List<String> bottomHeaderCellsValues = new ArrayList<String>();
			final int verticalHeadings = (leftHeadings && rightHeadings) ? 2
					: ((leftHeadings || rightHeadings) ? 1 : 0);
			final int showTableColumns =getTableColumnNum(values) + verticalHeadings;
            for (final String header : horizontalHeader) {
				if (column < showTableColumns) {
                    if (maxColumns < 0 || column < extendedMaxColumns) {
                        bottomHeaderCellsValues.add(header);
                        createCellContent(row, column, header, false);
                        column++;
                    } else {
                        break;
                    }                    
                }
            }
            final int horizontalHeaderSize = horizontalHeader.size();
            if (columnCount == 0) {
                columnCount = showTableColumns;
            }
            addMissingColumns(row, horizontalHeaderSize, bottomHeaderCellsValues, horizontalHeaderSize, columnCount);
            currentCellsValues.add(bottomHeaderCellsValues);
        }
        addHeadingsStyle(flexTable.getRowCount(), columnCount);

        if (widgetData.isReadOnly()) {
            flowPanel.add(flexTable);
        } else {
            handleEvents();
            tableContainer = new FlowPanel();
            tableContainer.setStyleName("bonita_form_table_container");
            tableContainer.add(flexTable);
            flowPanel.add(tableContainer);

            if(widgetData.isVariableColumnNumber()) {
                checkColumnCountAndDisplayRemoveButton();
                checkColumnCountAndDisplayAddButton();
            }
            
            rowButtonsContainer = new FlowPanel();
            rowButtonsContainer.addStyleName("bonita_form_grid_row_buttons");
            flowPanel.add(rowButtonsContainer);
            
            if(widgetData.isVariableRowNumber()) {
                checkRowCountAndDisplayRemoveButton();
                checkRowCountAndDisplayAddButton();
            }
        }
    }

    /**
     * @param values
     * @return
     */
    private int getTableColumnNum(final List<List<String>> values) {
        final List<String> horizontalHeader = widgetData.getHorizontalHeader();
        int tableValueColumns = 0;
        int valuesSize = 0;
        if (values != null && values.size() > 0) {
            valuesSize = values.get(0).size();
        } else if (horizontalHeader != null && horizontalHeader.size() > 0) {
            valuesSize = horizontalHeader.size();
        }
        if (minColumns != -1 && maxColumns != -1) {
            tableValueColumns = (valuesSize < minColumns) ? minColumns : ((valuesSize < maxColumns) ? valuesSize : maxColumns);
        } else if (minColumns != -1) {
            tableValueColumns = (valuesSize < minColumns) ? minColumns : valuesSize;
        } else if (maxColumns != -1) {
            tableValueColumns = (valuesSize < maxColumns) ? valuesSize : maxColumns;
        } else {
            tableValueColumns = valuesSize;
        }
        return tableValueColumns;
    }
    
    protected void handleEvents() {
        flexTable.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final Cell clickedCell = flexTable.getCellForEvent(event);
                final int row = clickedCell.getRowIndex();
                final int column = clickedCell.getCellIndex();
                final Widget textArea = flexTable.getWidget(row, column);
                if (textArea == null
                        && !(topHeadings && row <= 0)
                        && !(bottomHeadings && row >= flexTable.getRowCount() - 1)
                        && !(leftHeadings && column <= 0)
                        && !(rightHeadings && column >= columnCount - 1)) {
                    createCellContent(row, column, true);
                }
            }
        });
    }
    
    protected int addMissingColumns(final int currentRow, int currentColumn, final List<String> currentRowCellsValues, final int currentNbOfItems, final int minNumberOfItems) {
        final int columnsToAdd = minNumberOfItems - currentNbOfItems;
        if(columnsToAdd > 0) {
            for (int i = 0; i < columnsToAdd; i++) {
                currentRowCellsValues.add(null);
                createCellContent(currentRow, currentColumn, null, false);
                if (!widgetData.isReadOnly()) {
                    addEditableCellStyle(currentRow, currentColumn);
                }
                currentColumn++;
            }
        }
        return currentColumn;
    }
    
    protected void checkColumnCountAndDisplayRemoveButton() {
        if (removeColumnButton == null) {
            removeColumnButton = new Label();
            removeColumnButton.setTitle(FormsResourceBundle.getMessages().removeColumnTitle());
            removeColumnButton.setStyleName("bonita_form_grid_remove_button");
            removeColumnButton.addClickHandler(removeColumnClickHandler);
            removeColumnButton.setVisible(false);
            columnButtonsContainer.add(removeColumnButton);
        }
        final int colCount = getColumnCount();
        if (colCount > 0 && colCount > minColumns) {
             removeColumnButton.setVisible(true);
        }
    }
    
    protected class RemoveColumnClickHandler implements ClickHandler {
        
        protected EditableGridWidget editableGridWidget;
        
        /**
         * Default Constructor.
         * @param editableGridWidget
         */
        public RemoveColumnClickHandler(EditableGridWidget editableGridWidget) {
            super();
            this.editableGridWidget = editableGridWidget;
        }
        
        public void onClick(final ClickEvent event) {
            final int column = rightHeadings ? columnCount - 2 : columnCount - 1;
            for (int i = 0; i < flexTable.getRowCount(); i++) {
                flexTable.removeCell(i, column);
                currentCellsValues.get(i).remove(column);
            }
            columnCount--;
            ValueChangeEvent.fire(editableGridWidget, editableGridWidget.getValue());
            final int colCount = getColumnCount();
            if (colCount == 0 || colCount <= minColumns) {
                removeColumnButton.setVisible(false);
            }
            if (colCount == 0) {
                if (removeRowButton != null) {
                    removeRowButton.setVisible(false);
                }
                if (addRowButton != null) {
                    addRowButton.setVisible(false);
                }
            }
            checkColumnCountAndDisplayAddButton();
        }
    }

    protected void checkColumnCountAndDisplayAddButton() {
        if (addColumnButton == null) {
            addColumnButton = new Label();
            addColumnButton.setTitle(FormsResourceBundle.getMessages().addColumnTitle());
            addColumnButton.setStyleName("bonita_form_grid_add_button");
            addColumnButton.addClickHandler(addColumnClickHandler);
            addColumnButton.setVisible(false);
            columnButtonsContainer.add(addColumnButton);
        }
        final int colCount = getColumnCount();
        if ((maxColumns < 0 || colCount < maxColumns) && flexTable.getRowCount() > 0) {
            addColumnButton.setVisible(true);
        }
    }
    
    protected class AddColumnClickHandler implements ClickHandler {
        
        protected EditableGridWidget editableGridWidget;
        
        /**
         * Default Constructor.
         * @param editableGridWidget
         */
        public AddColumnClickHandler(EditableGridWidget editableGridWidget) {
            super();
            this.editableGridWidget = editableGridWidget;
        }
        
        public void onClick(final ClickEvent event) {
            final int oldColCount = getColumnCount();
            createNewColumn();
            ValueChangeEvent.fire(editableGridWidget, editableGridWidget.getValue());
            final int colCount = oldColCount + 1;
            if (maxColumns >= 0 && colCount >= maxColumns) {
                addColumnButton.setVisible(false);
            }
            if (oldColCount == 0) {
                final int rowCount = getRowCount();
                if (rowCount > 0 && rowCount > minRows && removeRowButton != null) {
                    removeRowButton.setVisible(true);
                }
                if ((maxRows < 0 || rowCount < maxRows) && addRowButton != null) {
                    addRowButton.setVisible(true);
                }
            }
            checkColumnCountAndDisplayRemoveButton();
        }
    }

    protected void createNewRow(boolean isInitWidget) {
        int rowIndex = flexTable.getRowCount();
        if(isInitWidget){
            flexTable.insertRow(flexTable.getRowCount());
        }else if (bottomHeadings) {
            flexTable.insertRow(flexTable.getRowCount() - 1);
            rowIndex--;
        }
        final List<String> newRowCellsValues = new ArrayList<String>();
        for (int i = 0; i < columnCount; i++) {
            newRowCellsValues.add(null);
            createCellContent(rowIndex, i, null, false);
            if (!widgetData.isReadOnly()) {
                addEditableCellStyle(rowIndex, i);
            }
        }
        if (columnCount == 0 && flexTable.getRowCount() == 0) {
            newRowCellsValues.add(null);
            createCellContent(0, 0, null, false);
            columnCount++;
            if (!widgetData.isReadOnly()) {
                addEditableCellStyle(0, 0);
            }
        }
        currentCellsValues.add(newRowCellsValues);
    }
    
    protected void createNewColumn() {
        int colIndex = columnCount;
        if (rightHeadings) {
            for (int i = 0; i < flexTable.getRowCount(); i++) {
                flexTable.insertCell(i, columnCount - 1);
            }
            colIndex--;
        }
        for (int i = 0; i < flexTable.getRowCount(); i++) {
            currentCellsValues.get(i).add(colIndex, null);
            createCellContent(i, colIndex, null, false);
            if (!widgetData.isReadOnly()) {
                addEditableCellStyle(i, colIndex);
            }
        }
        columnCount++;
    }
    
    protected void checkRowCountAndDisplayRemoveButton() {
        if (removeRowButton == null) {
            removeRowButton = new Label();
            removeRowButton.setTitle(FormsResourceBundle.getMessages().removeRowTitle());
            removeRowButton.setStyleName("bonita_form_grid_remove_button");
            removeRowButton.addClickHandler(removeRowClickHandler);
            removeRowButton.setVisible(false);
            rowButtonsContainer.add(removeRowButton);
        }
        final int rowCount = getRowCount();
        if (rowCount > 0 && rowCount > minRows) {
            removeRowButton.setVisible(true);
        }
    }
    
    protected class RemoveRowClickHandler implements ClickHandler {
        
        protected EditableGridWidget editableGridWidget;
        
        /**
         * Default Constructor.
         * @param editableGridWidget
         */
        public RemoveRowClickHandler(EditableGridWidget editableGridWidget) {
            super();
            this.editableGridWidget = editableGridWidget;
        }
        
        public void onClick(final ClickEvent event) {
            final int row = bottomHeadings ? flexTable.getRowCount() - 2 : flexTable.getRowCount() - 1;
            flexTable.removeRow(row);
            currentCellsValues.remove(row);
            ValueChangeEvent.fire(editableGridWidget, editableGridWidget.getValue());
            final int rowCount = getRowCount();
            if (rowCount == 0 || rowCount <= minRows) {
                removeRowButton.setVisible(false);
            }
            if (flexTable.getRowCount() == 0) {
                if (removeColumnButton != null) {
                    removeColumnButton.setVisible(false);
                }
                if (addColumnButton != null) {
                    addColumnButton.setVisible(false);
                }
            }
            checkRowCountAndDisplayAddButton();
        }
    }
    
    protected void checkRowCountAndDisplayAddButton() {
        if (addRowButton == null) {
            addRowButton = new Label();
            addRowButton.setTitle(FormsResourceBundle.getMessages().addRowTitle());
            addRowButton.setStyleName("bonita_form_grid_add_button");
            addRowButton.addClickHandler(addRowClickHandler);
            addRowButton.setVisible(false);
            rowButtonsContainer.add(addRowButton);
        }
        final int rowCount = getRowCount();
        if (maxRows < 0 || rowCount < maxRows) {
            addRowButton.setVisible(true);
        }
    }
    
    protected class AddRowClickHandler implements ClickHandler {
        
        protected EditableGridWidget editableGridWidget;
        
        /**
         * Default Constructor.
         * @param editableGridWidget
         */
        public AddRowClickHandler(EditableGridWidget editableGridWidget) {
            super();
            this.editableGridWidget = editableGridWidget;
        }
        
        public void onClick(final ClickEvent event) {
            final int oldRowCount = getRowCount();
            createNewRow(false);
            ValueChangeEvent.fire(editableGridWidget, editableGridWidget.getValue());
            final int rowCount = oldRowCount + 1;
            if (maxRows >= 0 && rowCount >= maxRows) {
                addRowButton.setVisible(false);
            }
            if (oldRowCount == 0) {
                final int colCount = getColumnCount();
                if (colCount > 0 && colCount > minColumns && removeColumnButton != null) {
                     removeColumnButton.setVisible(true);
                }
                if ((maxColumns < 0 || colCount < maxColumns) && addColumnButton != null) {
                    addColumnButton.setVisible(true);
                }
            }
            checkRowCountAndDisplayRemoveButton();
            final DOMUtils domUtils = DOMUtils.getInstance();
            if (DOMUtils.getInstance().isPageInFrame()) {
            }
        }
    }
    
    protected int getRowCount() {
        int rowCount = flexTable.getRowCount();
        if (topHeadings) {
            rowCount--;
        }
        if (bottomHeadings) {
            rowCount--;
        }
        return rowCount;
    }
    
    protected int getColumnCount() {
        int columnCount = this.columnCount;
        if (leftHeadings) {
            columnCount--;
        }
        if (rightHeadings) {
            columnCount--;
        }
        return columnCount;
    }
    
    protected void createCellContent(final int row, final int column, final boolean isEditMode) {
        final String content = currentCellsValues.get(row).get(column);
        createCellContent(row, column, content, isEditMode);
    }
    
    protected class CellBlurHandler implements BlurHandler {

        protected int row;
        
        protected int column;
       
        protected EditableGridWidget editableGridWidget;
        
        /**
         * Default Constructor.
         * @param row
         * @param column
         * @param hasValueChangeHandlers
         */
        public CellBlurHandler(int row, int column, EditableGridWidget editableGridWidget) {
            super();
            this.row = row;
            this.column = column;
            this.editableGridWidget = editableGridWidget;
        }

        /**
         * {@inheritDoc}
         */
        public void onBlur(final BlurEvent event) {
            final String value = ((TextArea)event.getSource()).getValue();
            final String oldValue = currentCellsValues.get(row).get(column);
            if (!((value == null && oldValue == null) || (value != null && value.equals(oldValue)))) {
                currentCellsValues.get(row).set(column, value);
                ValueChangeEvent.fire(editableGridWidget, editableGridWidget.getValue());
            }
            createCellContent(row, column, false);
            flexTable.getCellFormatter().getElement(row, column).removeAttribute("width");
            flexTable.getCellFormatter().getElement(row, column).removeAttribute("height");
            removeEditCellStyle(row, column);
            addViewCellStyle(row, column);
            addEditableCellStyle(row, column);
        }
    }
    
    protected void createCellContent(final int row, final int column, final String content, final boolean isEditMode) {
        if (isEditMode) {
            final String clientHeight = flexTable.getCellFormatter().getElement(row, column).getClientHeight() + "px";
            final String clientWidth = flexTable.getCellFormatter().getElement(row, column).getClientWidth() + "px";
            final TextArea textArea = new TextArea();
            textArea.setValue(content);
            textArea.setVisibleLines(1);
            textArea.addBlurHandler(new CellBlurHandler(row, column, this));
            textArea.addKeyPressHandler(new KeyPressHandler() {
                public void onKeyPress(final KeyPressEvent event) {
                    final int keycode = event.getNativeEvent().getKeyCode();
                    if (keycode == KeyCodes.KEY_TAB) {
                        event.preventDefault();
                        event.stopPropagation();
                        int newColumn = column;
                        int newRow = row;
                        if (event.isShiftKeyDown()) {
                            final int minColumn = leftHeadings ? 1 : 0;
                            if (column > minColumn) {
                                newColumn = column - 1;
                            } else {
                                final int minRow = topHeadings ? 1 : 0;
                                if (row > minRow) {
                                    newRow = row - 1;
                                    newColumn = rightHeadings ? columnCount - 2 : columnCount - 1;
                                }
                            }
                        } else {
                            final int maxColumn = rightHeadings ? columnCount - 2 : columnCount - 1;
                            if (column < maxColumn) {
                                newColumn = column + 1;
                            } else {
                                final int maxRow = bottomHeadings ? flexTable.getRowCount() - 2 : flexTable.getRowCount() - 1;
                                if (row < maxRow) {
                                    newRow = row + 1;
                                    newColumn = leftHeadings ? 1 : 0;
                                }
                            }
                        }
                        createCellContent(newRow, newColumn, true);
                    }
                }
            });
            removeEditableCellStyle(row, column);
            removeViewCellStyle(row, column);
            addEditCellStyle(row, column);
            textArea.setSize(clientWidth, clientHeight);
            flexTable.getCellFormatter().setHeight(row, column, clientHeight);
            flexTable.getCellFormatter().setWidth(row, column, clientWidth);
            flexTable.setWidget(row, column, textArea);
            textArea.setFocus(true);
            if (content != null) {
                textArea.selectAll();
            }
        } else {
            if (widgetData.allowHTMLInField()) {
                flexTable.setHTML(row, column, content);
            } else {
                flexTable.setText(row, column, content);
            }
            addViewCellStyle(row, column);
        }
    }
    
    protected void addViewCellStyle (final int row, final int column) {
        flexTable.getFlexCellFormatter().addStyleName(row, column, TABLE_CELL_DEFAULT_STYLE);
        if (widgetData.getCellsStyle() != null && widgetData.getCellsStyle().length() > 0) {
            flexTable.getFlexCellFormatter().addStyleName(row, column, widgetData.getCellsStyle());
        }
    }
    
    protected void removeViewCellStyle (final int row, final int column) {
        flexTable.getFlexCellFormatter().removeStyleName(row, column, TABLE_CELL_DEFAULT_STYLE);
        if (widgetData.getCellsStyle() != null && widgetData.getCellsStyle().length() > 0) {
            flexTable.getFlexCellFormatter().removeStyleName(row, column, widgetData.getCellsStyle());
        }
    }
    
    protected void addEditCellStyle (final int row, final int column) {
        flexTable.getFlexCellFormatter().addStyleName(row, column, TABLE_CELL_EDITION_STYLE);
    }
    
    protected void removeEditCellStyle (final int row, final int column) {
        flexTable.getFlexCellFormatter().removeStyleName(row, column, TABLE_CELL_EDITION_STYLE);
    }
    
    protected void addEditableCellStyle (final int row, final int column) {
        flexTable.getFlexCellFormatter().addStyleName(row, column, TABLE_CELL_EDITABLE_STYLE);
    }
    
    protected void removeEditableCellStyle (final int row, final int column) {
        flexTable.getFlexCellFormatter().removeStyleName(row, column, TABLE_CELL_EDITABLE_STYLE);
    }
    
    protected void addHeadingsStyle(final int rowCount, final int columnCount) {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if((column == 0 && widgetData.hasLeftHeadings()) || (column == columnCount - 1 && widgetData.hasRightHeadings())) {
                    if (widgetData.getHeadingsStyle() != null && widgetData.getHeadingsStyle().length() > 0) {
                        flexTable.getFlexCellFormatter().addStyleName(row, column, widgetData.getHeadingsStyle());
                    }
                }
                if((row == 0 && widgetData.hasTopHeadings()) || (row == rowCount - 1 && widgetData.hasBottomHeadings())) {
                    if (widgetData.getHeadingsStyle() != null && widgetData.getHeadingsStyle().length() > 0) {
                        flexTable.getFlexCellFormatter().addStyleName(row, column, widgetData.getHeadingsStyle());
                    }
                }
            }
        }
    }
    
    public void setValue(final List<List<String>> value) {
        flowPanel.clear();
        createWidget(value);
    }
    
    public List<List<String>> getValue() {
        final List<List<String>> values = new ArrayList<List<String>>();
        final int startRowIndex = topHeadings ? 1 : 0;
        final int endRowIndex = bottomHeadings ? currentCellsValues.size() - 2 : currentCellsValues.size() - 1;
        for (int row = startRowIndex; row <= endRowIndex; row++) {
            final List<String> rowValues = new ArrayList<String>();
            final int startColumnIndex = leftHeadings ? 1 : 0;
            final int endColumnIndex = rightHeadings ? columnCount - 2 : columnCount - 1;
            for (int column = startColumnIndex; column <= endColumnIndex; column++) {
                rowValues.add(currentCellsValues.get(row).get(column));
            }
            values.add(rowValues);
        }
        return values;
    }
    
    /**
     * {@inheritDoc}
     */
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<List<List<String>>> valueChangeHandler) {
        return addHandler(valueChangeHandler, ValueChangeEvent.getType());
    }
}
