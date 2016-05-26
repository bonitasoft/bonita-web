/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.HtmlTemplate;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.server.accessor.impl.EngineApplicationFormDefAccessorImpl;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

/**
 * Unit test for the implementation of the form definition API
 *
 * @author Celine Souchet
 */
@RunWith(MockitoJUnitRunner.class)
public class FormDefinitionAPIImplTest {

    private static final String EXPRESSION_NAME = "expressionName";

    private static final String TRANSIENT_VALUE = "transientValue";

    private static final String TRANSIENT_DATA_NAME = "transientDataName";

    private static final long TENANT_ID = 1L;

    private static final String FORM_ID = "processName--1.0$entry";

    private static final String PAGE_ID = "processPage1";

    private Date deployementDate;

    private final Map<String, Object> urlContext = new HashMap<>();

    private Document document;

    @Mock
    private Map<String, Object> context;

    @Mock
    APISession apiSession;

    @Mock
    private Expression transientDataExpression;

    @Mock
    FormServiceProvider formServiceProvider;

    @Mock
    private Map<String, Serializable> evaluatedExpressions;

    @Mock
    private FormCacheUtil formCacheUtil;

    @Mock
    private Serializable evaluatedValue;

    FormDefinitionAPIImpl formDefinitionAPI;

    private class ParentPojo implements Serializable {

        private static final long serialVersionUID = 1L;
        private String data;

        public void setData(final String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

    }

    private class ChildPojo extends ParentPojo {

        public ChildPojo() {
            super();
        }
    }

    @Before
    public void before() throws Exception {
        doReturn(urlContext).when(context).get(FormServiceProviderUtil.URL_CONTEXT);
        doReturn(apiSession).when(context).get(FormServiceProviderUtil.API_SESSION);
        doReturn(TENANT_ID).when(apiSession).getTenantId();

        //        final FormCacheUtil formCacheUtil = mock(FormCacheUtil.class);
        formDefinitionAPI = spy(new FormDefinitionAPIImpl(1, document, formCacheUtil, deployementDate, Locale.ENGLISH.toString()));
    }

    @Test
    public void getFormPageFromCache() throws Exception {
        // Given
        doReturn(mock(EngineApplicationFormDefAccessorImpl.class)).when(formDefinitionAPI).getApplicationFormDefinition(anyString(), eq(context));
        doReturn(mock(HtmlTemplate.class)).when(formDefinitionAPI).getPageLayout(anyString(), any(Date.class), eq(context));
        final FormPage formPageFirstCall = formDefinitionAPI.getFormPage(FORM_ID, PAGE_ID, context);

        // When
        final FormPage formPage = formDefinitionAPI.getFormPage(FORM_ID, PAGE_ID, context);

        // Then
        Assert.assertNotNull(formPage);
        verify(formCacheUtil, times(2)).getPage(FORM_ID, Locale.ENGLISH.toString(), deployementDate, PAGE_ID);
        verify(formCacheUtil, times(1)).storePage(FORM_ID, Locale.ENGLISH.toString(), deployementDate, formPageFirstCall);
        Assert.assertEquals("processPage1", formPage.getPageId());
    }

    @Test
    public void getTransientDataContextWithBdm() throws Exception {
        final ChildPojo childPojo = new ChildPojo();
        doReturn(true).when(formDefinitionAPI).isProxyfiedBdmValue(any(Serializable.class));
        doReturn(EXPRESSION_NAME).when(transientDataExpression).getName();
        doReturn(formServiceProvider).when(formDefinitionAPI).getFormServiceProvider();
        doReturn(evaluatedExpressions).when(formServiceProvider).resolveExpressions(anyListOf(Expression.class), anyMapOf(String.class, Object.class));
        doReturn(childPojo).when(evaluatedExpressions).get(EXPRESSION_NAME);

        final List<TransientData> transientDataList = new ArrayList<>();
        final TransientData transientData = new TransientData(TRANSIENT_DATA_NAME, ParentPojo.class.getName(), transientDataExpression);
        transientDataList.add(transientData);

        // When
        final Map<String, Serializable> transientDataContext = formDefinitionAPI.getTransientDataContext(transientDataList, Locale.ENGLISH, context);

        // Then
        assertThat(transientDataContext).containsEntry(TRANSIENT_DATA_NAME, childPojo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTransientDataContextWithBadReturnClass() throws Exception {
        // given
        doReturn(EXPRESSION_NAME).when(transientDataExpression).getName();
        doReturn(formServiceProvider).when(formDefinitionAPI).getFormServiceProvider();
        doReturn(evaluatedExpressions).when(formServiceProvider).resolveExpressions(anyListOf(Expression.class), anyMapOf(String.class, Object.class));
        doReturn(new Date()).when(evaluatedExpressions).get(EXPRESSION_NAME);

        final List<TransientData> transientDataList = new ArrayList<>();
        final TransientData transientData = new TransientData(TRANSIENT_DATA_NAME, String.class.getName(), transientDataExpression);
        transientDataList.add(transientData);

        // When
        formDefinitionAPI.getTransientDataContext(transientDataList, Locale.ENGLISH, context);

        // Then exception
    }

    @Test
    public void getTransientDataContextWithValue() throws Exception {
        // Given
        doReturn(EXPRESSION_NAME).when(transientDataExpression).getName();
        doReturn(formServiceProvider).when(formDefinitionAPI).getFormServiceProvider();
        doReturn(evaluatedExpressions).when(formServiceProvider).resolveExpressions(anyListOf(Expression.class), anyMapOf(String.class, Object.class));
        doReturn(TRANSIENT_VALUE).when(evaluatedExpressions).get(EXPRESSION_NAME);

        final List<TransientData> transientDataList = new ArrayList<>();
        final TransientData transientData = new TransientData(TRANSIENT_DATA_NAME, String.class.getName(), transientDataExpression);
        transientDataList.add(transientData);

        // When
        final Map<String, Serializable> transientDataContext = formDefinitionAPI.getTransientDataContext(transientDataList, Locale.ENGLISH, context);

        // Then
        assertThat(transientDataContext).containsEntry(TRANSIENT_DATA_NAME, TRANSIENT_VALUE);

    }

    @Test
    public void getTransientDataContextWithNullValue() throws Exception {
        // Given
        doReturn(EXPRESSION_NAME).when(transientDataExpression).getName();
        doReturn(formServiceProvider).when(formDefinitionAPI).getFormServiceProvider();
        doReturn(evaluatedExpressions).when(formServiceProvider).resolveExpressions(anyListOf(Expression.class), anyMapOf(String.class, Object.class));
        doReturn(null).when(evaluatedExpressions).get(EXPRESSION_NAME);

        final List<TransientData> transientDataList = new ArrayList<>();
        final TransientData transientData = new TransientData(TRANSIENT_DATA_NAME, String.class.getName(), transientDataExpression);
        transientDataList.add(transientData);

        // When
        final Map<String, Serializable> transientDataContext = formDefinitionAPI.getTransientDataContext(transientDataList, Locale.ENGLISH, context);

        // Then
        assertThat(transientDataContext).containsEntry(TRANSIENT_DATA_NAME, null);

    }

    @Test
    public void getTransientDataContextWithoutExpression() throws Exception {
        // Given
        final List<TransientData> transientDataList = new ArrayList<>();
        final TransientData transientData = new TransientData();
        transientData.setName(TRANSIENT_DATA_NAME);
        transientDataList.add(transientData);

        // When
        final Map<String, Serializable> transientDataContext = formDefinitionAPI.getTransientDataContext(transientDataList, Locale.ENGLISH, context);

        // Then
        assertThat(transientDataContext).containsEntry(TRANSIENT_DATA_NAME, null);

    }

}
