package org.bonitasoft.forms.client.view.widget;

import static org.mockito.Mockito.*;

import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class FormButtonWidgetTest {

    FormButtonWidget buttonWidget;

    @Mock
    ReducedFormWidget formWidget;

    @Before
    public void setUp() throws Exception {
        buttonWidget = new FormButtonWidget(formWidget);
    }

    @Test
    public void testAddClickHandler_and_click_should_trigger_handler() throws Exception {
        ClickHandler clickHandler = mock(ClickHandler.class);
        buttonWidget.addClickHandler(clickHandler);
        ClickEvent clickEvent = mock(ClickEvent.class);
        buttonWidget.onClick(clickEvent);
        verify(clickHandler, times(1)).onClick(clickEvent);
    }

    @Test
    public void testAddClickHandler_twice_and_click_should_trigger_handler_once() throws Exception {
        ClickHandler clickHandler = mock(ClickHandler.class);
        buttonWidget.addClickHandler(clickHandler);
        buttonWidget.addClickHandler(clickHandler);
        ClickEvent clickEvent = mock(ClickEvent.class);
        buttonWidget.onClick(clickEvent);
        verify(clickHandler, times(1)).onClick(clickEvent);
    }

    @Test
    public void testAddClickHandler_with_id_and_click_should_trigger_handler() throws Exception {
        ClickHandler clickHandler = mock(ClickHandler.class);
        buttonWidget.addClickHandler("form", clickHandler);
        ClickEvent clickEvent = mock(ClickEvent.class);
        buttonWidget.onClick(clickEvent);
        verify(clickHandler, times(1)).onClick(clickEvent);
    }

    @Test
    public void testAddClickHandler_with_id_twice_and_click_should_trigger_handler_once() throws Exception {
        ClickHandler clickHandler = mock(ClickHandler.class);
        buttonWidget.addClickHandler("form", clickHandler);
        buttonWidget.addClickHandler("form", clickHandler);
        ClickEvent clickEvent = mock(ClickEvent.class);
        buttonWidget.onClick(clickEvent);
        verify(clickHandler, times(1)).onClick(clickEvent);
    }

    @Test
    public void testAddClickHandler_with_id_twice_and_without_once_and_click_should_trigger_handler_once() throws Exception {
        ClickHandler clickHandler = mock(ClickHandler.class);
        buttonWidget.addClickHandler("form", clickHandler);
        buttonWidget.addClickHandler("form", clickHandler);
        buttonWidget.addClickHandler(clickHandler);
        ClickEvent clickEvent = mock(ClickEvent.class);
        buttonWidget.onClick(clickEvent);
        verify(clickHandler, times(2)).onClick(clickEvent);
    }

    @Test
    public void test_two_AddClickHandler_with_id_twice_and_once_and_click_should_trigger_handler_once_each() throws Exception {
        ClickHandler clickHandler1 = mock(ClickHandler.class);
        ClickHandler clickHandler2 = mock(ClickHandler.class);
        buttonWidget.addClickHandler("form1", clickHandler1);
        buttonWidget.addClickHandler("form1", clickHandler1);
        buttonWidget.addClickHandler("form2", clickHandler2);
        ClickEvent clickEvent = mock(ClickEvent.class);
        buttonWidget.onClick(clickEvent);
        verify(clickHandler1, times(1)).onClick(clickEvent);
        verify(clickHandler2, times(1)).onClick(clickEvent);
    }

}
