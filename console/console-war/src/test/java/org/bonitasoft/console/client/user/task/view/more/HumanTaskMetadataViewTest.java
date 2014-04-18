/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.user.task.view.more;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.*;
import static org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.bonitasoft.console.client.uib.formatter.Formatter;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMetadataView.Binder;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.ClientDateFormater;
import org.bonitasoft.web.toolkit.client.ui.utils.I18n;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeUiBinderProvider;


/**
 * @author Julien Reboul
 */
@RunWith(GwtMockitoTestRunner.class)
public class HumanTaskMetadataViewTest {

    @GwtMock
    SpanElement spanElement;

    @GwtMock
    AnchorElement anchorElement;

    @GwtMock
    DivElement divElement;

    @GwtMock
    LabelElement labelElement;

    @GwtMock
    ParagraphElement paragraphElement;

    @BeforeClass
    public static void classSetUp() {
        I18n.getInstance();
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
        CommonDateFormater.setDateFormater(new ClientDateFormater());
    }

    @Before
    public void setUp() throws Exception {
        GwtMockito.useProviderForType(Binder.class, new FakeUiBinderProvider());
    }

    @Test
    public void testHumanTaskMetadataViewShouldOnlyHaveAssignedUserAndLastUpdate() throws Exception {
        IHumanTaskItem humanTaskItem = mock(IHumanTaskItem.class);
        when(humanTaskItem.getPriority()).thenReturn(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        when(humanTaskItem.getState()).thenReturn(IHumanTaskItem.VALUE_STATE_ABORTED);
        when(humanTaskItem.getExecutedByUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getExecutedBySubstituteUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getCaseId()).thenReturn(APIID.makeAPIID(10L));
        when(humanTaskItem.getDueDate()).thenReturn("2012-12-05 00:00:00.000");
        //due to some bug in GWTMockito that sets a mock DateTimeFormatInfo to its DateTimeFormat with no behaviour for ampms() method that should return a new String[]{"AM", "PM"}
        // we cannot use the date formatting with 'MM/dd/YYYY h:mm a'
        // when(humanTaskItem.getAssignedDate()).thenReturn("2011-01-04 12:25:00.123");
        UserItem userItem = mock(UserItem.class);
        when(userItem.getFirstName()).thenReturn("UserFirstname");
        when(userItem.getLastName()).thenReturn("UserLastname");
        when(humanTaskItem.getAssignedUser()).thenReturn(userItem);
        when(humanTaskItem.getExecutedByUser()).thenReturn(userItem);

        HumanTaskMetadataView humanTaskMetadataView = new HumanTaskMetadataView(humanTaskItem);
        String priorityValue = Formatter.formatPriority(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        verify(spanElement, times(1)).setInnerText(priorityValue);
        String assignedToValue = Formatter.formatUser(userItem);
        verify(spanElement, times(2)).setInnerText(assignedToValue);
        String dueDateValue = Formatter.formatDate(humanTaskItem.getDueDate(), DISPLAY_RELATIVE);
        verify(spanElement, times(1)).setInnerText(dueDateValue);
        verify(divElement, never()).removeFromParent();

        String lastUpdateDateLabel = humanTaskMetadataView.messages.last_update_date_label();
        String lastUpdateDateTitle = humanTaskMetadataView.messages.last_update_date_title();
        verify(labelElement, times(1)).setTitle(lastUpdateDateTitle);
        verify(labelElement, times(1)).setInnerText(lastUpdateDateLabel);
        // String assignedDate = Formatter.formatDate(humanTaskItem.getAssignedDate(), DISPLAY);
        // verify(spanElement, times(1)).setInnerText(assignedDate);

    }

    @Test
    public void testHumanTaskMetadataViewShouldHaveAssignedUserAndSubstituteAndLastUpdate() throws Exception {
        IHumanTaskItem humanTaskItem = mock(IHumanTaskItem.class);
        when(humanTaskItem.getPriority()).thenReturn(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        when(humanTaskItem.getState()).thenReturn(IHumanTaskItem.VALUE_STATE_FAILED);
        when(humanTaskItem.getExecutedByUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getExecutedBySubstituteUserId()).thenReturn(APIID.makeAPIID(2L));
        when(humanTaskItem.getCaseId()).thenReturn(APIID.makeAPIID(10L));
        when(humanTaskItem.getDueDate()).thenReturn("2012-12-05 00:00:00.000");
        // due to some bug in GWTMockito that sets a mock DateTimeFormatInfo to its DateTimeFormat with no behaviour for ampms() method that should return a new
        // String[]{"AM", "PM"}
        // we cannot use the date formatting with 'MM/dd/YYYY h:mm a'
        // when(humanTaskItem.getAssignedDate()).thenReturn("2011-01-04 12:25:00.123");
        UserItem userItem = mock(UserItem.class);
        when(userItem.getFirstName()).thenReturn("UserFirstname");
        when(userItem.getLastName()).thenReturn("UserLastname");
        when(humanTaskItem.getAssignedUser()).thenReturn(userItem);
        when(humanTaskItem.getExecutedByUser()).thenReturn(userItem);
        UserItem userSubstituteItem = mock(UserItem.class);
        when(userSubstituteItem.getFirstName()).thenReturn("PMFirstname");
        when(userSubstituteItem.getLastName()).thenReturn("PMLastname");
        when(humanTaskItem.getExecutedBySubstituteUser()).thenReturn(userSubstituteItem);

        HumanTaskMetadataView humanTaskMetadataView = new HumanTaskMetadataView(humanTaskItem);
        String priorityValue = Formatter.formatPriority(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        verify(spanElement, times(1)).setInnerText(priorityValue);
        String assignedToValue = Formatter.formatUser(userItem);
        verify(spanElement, times(1)).setInnerText(assignedToValue);
        String dueDateValue = Formatter.formatDate(humanTaskItem.getDueDate(), DISPLAY_RELATIVE);
        verify(spanElement, times(1)).setInnerText(dueDateValue);
        String executedByUser = Formatter.formatUser(humanTaskItem.getExecutedBySubstituteUser()) + _(" for ")
                + Formatter.formatUser(humanTaskItem.getExecutedByUser());
        verify(spanElement, times(1)).setInnerText(executedByUser);
        verify(divElement, never()).removeFromParent();

        String lastUpdateDateLabel = humanTaskMetadataView.messages.last_update_date_label();
        String lastUpdateDateTitle = humanTaskMetadataView.messages.last_update_date_title();
        verify(labelElement, times(1)).setTitle(lastUpdateDateTitle);
        verify(labelElement, times(1)).setInnerText(lastUpdateDateLabel);
        // String assignedDate = Formatter.formatDate(humanTaskItem.getAssignedDate(), DISPLAY);
        // verify(spanElement, times(1)).setInnerText(assignedDate);

    }

    @Test
    public void testHumanTaskMetadataViewShouldHaveAssignedUserAndSubstituteAndDoneOn() throws Exception {
        IHumanTaskItem humanTaskItem = mock(IHumanTaskItem.class);
        when(humanTaskItem.getPriority()).thenReturn(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        when(humanTaskItem.getState()).thenReturn(IHumanTaskItem.VALUE_STATE_COMPLETED);
        when(humanTaskItem.getExecutedByUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getExecutedBySubstituteUserId()).thenReturn(APIID.makeAPIID(2L));
        when(humanTaskItem.getCaseId()).thenReturn(APIID.makeAPIID(10L));
        when(humanTaskItem.getDueDate()).thenReturn("2012-12-05 00:00:00.000");
        // due to some bug in GWTMockito that sets a mock DateTimeFormatInfo to its DateTimeFormat with no behaviour for ampms() method that should return a new
        // String[]{"AM", "PM"}
        // we cannot use the date formatting with 'MM/dd/YYYY h:mm a'
        // when(humanTaskItem.getAssignedDate()).thenReturn("2011-01-04 12:25:00.123");
        UserItem userItem = mock(UserItem.class);
        when(userItem.getFirstName()).thenReturn("UserFirstname");
        when(userItem.getLastName()).thenReturn("UserLastname");
        when(humanTaskItem.getExecutedByUser()).thenReturn(userItem);
        when(humanTaskItem.getAssignedUser()).thenReturn(userItem);
        UserItem userSubstituteItem = mock(UserItem.class);
        when(userSubstituteItem.getFirstName()).thenReturn("PMFirstname");
        when(userSubstituteItem.getLastName()).thenReturn("PMLastname");
        when(humanTaskItem.getExecutedBySubstituteUser()).thenReturn(userSubstituteItem);

        HumanTaskMetadataView humanTaskMetadataView = new HumanTaskMetadataView(humanTaskItem);
        String priorityValue = Formatter.formatPriority(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        verify(spanElement, times(1)).setInnerText(priorityValue);
        String assignedToValue = Formatter.formatUser(userItem);
        verify(spanElement, times(1)).setInnerText(assignedToValue);
        String dueDateValue = Formatter.formatDate(humanTaskItem.getDueDate(), DISPLAY_RELATIVE);
        verify(spanElement, times(1)).setInnerText(dueDateValue);
        String executedByUser = Formatter.formatUser(humanTaskItem.getExecutedBySubstituteUser()) + _(" for ")
                + Formatter.formatUser(humanTaskItem.getExecutedByUser());
        verify(spanElement, times(1)).setInnerText(executedByUser);
        verify(divElement, never()).removeFromParent();

        String lastUpdateDateLabel = humanTaskMetadataView.messages.done_on_label();
        verify(labelElement, never()).setTitle(anyString());
        verify(labelElement, times(1)).setInnerText(lastUpdateDateLabel);
        // String assignedDate = Formatter.formatDate(humanTaskItem.getAssignedDate(), DISPLAY);
        // verify(spanElement, times(1)).setInnerText(assignedDate);

    }

}
