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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.HtmlTemplate;
import org.bonitasoft.forms.server.accessor.impl.EngineApplicationFormDefAccessorImpl;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
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

    private static final long TENANT_ID = 1L;

    private static final String FORM_ID = "processName--1.0$entry";

    private static final String PAGE_ID = "processPage1";

    private Date deployementDate;

    private final Map<String, Object> urlContext = new HashMap<String, Object>();

    private Document document;

    @Mock
    private Map<String, Object> context;

    @Mock
    APISession apiSession;

    @Before
    public void before() {
        System.setProperty("bonita.home", "target/bonita-home/bonita");
        doReturn(urlContext).when(context).get(FormServiceProviderUtil.URL_CONTEXT);
        doReturn(apiSession).when(context).get(FormServiceProviderUtil.API_SESSION);
        doReturn(TENANT_ID).when(apiSession).getTenantId();
    }

    @Test
    public void getFormPageFromCache() throws Exception {
        // Given
        final FormCacheUtil formCacheUtil = mock(FormCacheUtil.class);
        final FormDefinitionAPIImpl api = spy(new FormDefinitionAPIImpl(1, document, formCacheUtil, deployementDate, Locale.ENGLISH.toString()));
        doReturn(mock(EngineApplicationFormDefAccessorImpl.class)).when(api).getApplicationFormDefinition(anyString(), eq(context));
        doReturn(mock(HtmlTemplate.class)).when(api).getPageLayout(anyString(), any(Date.class), eq(context));
        final FormPage formPageFirstCall = api.getFormPage(FORM_ID, PAGE_ID, context);

        // When
        final FormPage formPage = api.getFormPage(FORM_ID, PAGE_ID, context);

        // Then
        Assert.assertNotNull(formPage);
        verify(formCacheUtil, times(2)).getPage(FORM_ID, Locale.ENGLISH.toString(), deployementDate, PAGE_ID);
        verify(formCacheUtil, times(1)).storePage(FORM_ID, Locale.ENGLISH.toString(), deployementDate, formPageFirstCall);
        Assert.assertEquals("processPage1", formPage.getPageId());
    }
}
