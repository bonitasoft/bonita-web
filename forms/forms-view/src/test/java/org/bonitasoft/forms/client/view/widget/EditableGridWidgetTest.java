package org.bonitasoft.forms.client.view.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class EditableGridWidgetTest {

    private EditableGridWidget editableGrid;

    @Mock
    ReducedFormWidget formWidget;

    @GwtMock
    FlexCellFormatter flexCellFormatter;

    @GwtMock
    FlexTable flexTable;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_setValue_update_cell_values() throws Exception {

        when(flexTable.getFlexCellFormatter()).thenReturn(flexCellFormatter);
        when(formWidget.getMaxRows()).thenReturn(-1);
        when(formWidget.getMaxColumns()).thenReturn(-1);

        editableGrid = new EditableGridWidget("formId", formWidget, getCellsValue("cellValue1", "cellValue2"));

        final List<List<String>> newValue = getCellsValue("newCellValue1", "newCellValue2");
        editableGrid.setValue(newValue);

        assertThat(editableGrid.getValue()).isEqualTo(newValue);
    }

    protected List<List<String>> getCellsValue(final String... cellValues) {
        final List<List<String>> cellsNewContent = new ArrayList<List<String>>();
        for (final String cellValue : cellValues) {
            final List<String> rowContent = new ArrayList<String>();
            rowContent.add(cellValue);
            cellsNewContent.add(rowContent);
        }
        return cellsNewContent;
    }

}
