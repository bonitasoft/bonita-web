package org.bonitasoft.forms.server.accessor.impl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Locale;

import org.bonitasoft.forms.client.model.FormWidget;
import org.junit.Test;

public class FormCacheUtilTest {

    @Test
    public void testStoreFormWidget() {
        final FormWidget widget1 = new FormWidget();
        widget1.setId("widget1");
        final Date processDeploymentDate = new Date();
        final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(1);
        formCacheUtil.storeFormWidget("formID", "pageID", Locale.ENGLISH.toString(), processDeploymentDate, widget1);
        final FormWidget formWidget1RetrievedFromCacheByParameters = FormCacheUtilFactory.getTenantFormCacheUtil(1).getFormWidget("formID", "pageID",
                widget1.getId(),
                Locale.ENGLISH.toString(), processDeploymentDate);
        assertNotNull(formWidget1RetrievedFromCacheByParameters);
        assertEquals("widget1", widget1.getId());
    }
}
