package org.bonitasoft.forms.client.view.widget;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class TableWidgetTest {

    @Mock
    ReducedFormWidget formWidget;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_get_last_page_index_work_with_an_empty_available_values_list() throws Exception {

        when(formWidget.getTableAvailableValues()).thenReturn(new ArrayList<List<ReducedFormFieldAvailableValue>>());
        when(formWidget.getMaxRows()).thenReturn(10);

        final TableWidget tableWidget = new TableWidget(formWidget, new ArrayList<String>());

        Assert.assertEquals("The last page index should be 0 in case the available values list is empty", 0, tableWidget.getLastPageIndex());
    }

    @Test
    public void should_get_last_page_index_work_with_an_available_values_list() throws Exception {

        final ArrayList<List<ReducedFormFieldAvailableValue>> availableValuesList = new ArrayList<List<ReducedFormFieldAvailableValue>>();
        for (int i = 0; i < 5; i++) {
            availableValuesList.add(new ArrayList<ReducedFormFieldAvailableValue>());
        }
        when(formWidget.getTableAvailableValues()).thenReturn(availableValuesList);
        when(formWidget.getMaxRows()).thenReturn(2);

        final TableWidget tableWidget = new TableWidget(formWidget, new ArrayList<String>());

        Assert.assertEquals("The last page index should be 2 in case there is 5 rows of available values and 2 rows max by page", 2,
                tableWidget.getLastPageIndex());
    }

    @Test
    public void should_get_last_page_index_work_with_an_available_values_list_matching_the_max_item_per_page() throws Exception {

        final ArrayList<List<ReducedFormFieldAvailableValue>> availableValuesList = new ArrayList<List<ReducedFormFieldAvailableValue>>();
        for (int i = 0; i < 4; i++) {
            availableValuesList.add(new ArrayList<ReducedFormFieldAvailableValue>());
        }
        when(formWidget.getTableAvailableValues()).thenReturn(availableValuesList);
        when(formWidget.getMaxRows()).thenReturn(4);

        final TableWidget tableWidget = new TableWidget(formWidget, new ArrayList<String>());

        Assert.assertEquals("The last page index should be 0 in case there is 4 rows of available values and 4 rows max by page", 0,
                tableWidget.getLastPageIndex());
    }
}
