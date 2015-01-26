/**
 * Copyright (C) 2014 BonitaSoft S.A.
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.WidgetType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwtmockito.GwtMockitoTestRunner;

/**
 * @author Julien Reboul
 */

@RunWith(GwtMockitoTestRunner.class)
public class FormFieldWidgetTest {

    protected FormFieldWidget formFieldWidget;

    @Before
    public void setUp() throws Exception {

        final ReducedFormWidget reducedFormWidget = mock(ReducedFormWidget.class);
        final Map contextMap = mock(Map.class);
        final FormFieldValue formFieldValue = mock(FormFieldValue.class);
        when(reducedFormWidget.getInitialFieldValue()).thenReturn(formFieldValue);

        when(reducedFormWidget.getId()).thenReturn("id");

        when(reducedFormWidget.getType()).thenReturn(WidgetType.TEXTBOX);

        when(reducedFormWidget.getDisplayFormat()).thenReturn("displayFormat");

        when(formFieldValue.getValueType()).thenReturn("valueType");

        when(reducedFormWidget.getFieldOutputType()).thenReturn("fieldOutputValue");
        formFieldWidget = new FormFieldWidget(reducedFormWidget, contextMap, "", "");
    }

    @Test
    public void testAddValueChangeHandler_with_Null_should_Not_Break() throws Exception {
        try {
            formFieldWidget.addValueChangeHandler(null);
            fail("NPe should have been raised");
        } catch (final NullPointerException e) {
            assertThat(e).hasMessage("the given ValueChangeHandler is not defined");
        }
    }

    @Test
    public void testAddValueChangeHandler_with_ValueChangeHandlerOnDifferentWidget_Should_Add_it() throws Exception {
        final ValueChangeHandler valueChangeHandler = mock(ValueChangeHandler.class);
        formFieldWidget.addValueChangeHandler(valueChangeHandler);
        final ValueChangeEvent valueChangeEvent = mock(ValueChangeEvent.class);
        formFieldWidget.onValueChange(valueChangeEvent);
        verify(valueChangeHandler).onValueChange(valueChangeEvent);
    }

    @Test
    public void testAddValueChangeHandler_with_Two_ValueChangeHandlerOnDifferentWidget_Should_Add_Them() throws Exception {
        final ValueChangeHandler valueChangeHandler1 = mock(ValueChangeHandler.class);
        formFieldWidget.addValueChangeHandler(valueChangeHandler1);
        final ValueChangeHandler valueChangeHandler2 = mock(ValueChangeHandler.class);
        formFieldWidget.addValueChangeHandler(valueChangeHandler2);
        final ValueChangeEvent valueChangeEvent = mock(ValueChangeEvent.class);
        formFieldWidget.onValueChange(valueChangeEvent);
        verify(valueChangeHandler1).onValueChange(valueChangeEvent);
        verify(valueChangeHandler2).onValueChange(valueChangeEvent);
    }

    @Test
    public void testAddValueChangeHandler_with_Several_Times_SameValueChangeHandlerOnWidget_Should_Add_Them_Once() throws Exception {
        final ValueChangeHandler valueChangeHandler1 = mock(ValueChangeHandler.class);
        formFieldWidget.addValueChangeHandler(valueChangeHandler1);
        formFieldWidget.addValueChangeHandler(valueChangeHandler1);
        formFieldWidget.addValueChangeHandler(valueChangeHandler1);
        formFieldWidget.addValueChangeHandler(valueChangeHandler1);
        formFieldWidget.addValueChangeHandler(valueChangeHandler1);

        final ValueChangeEvent valueChangeEvent = mock(ValueChangeEvent.class);
        formFieldWidget.onValueChange(valueChangeEvent);
        verify(valueChangeHandler1).onValueChange(valueChangeEvent);
    }

}
