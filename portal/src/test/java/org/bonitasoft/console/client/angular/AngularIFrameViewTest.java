package org.bonitasoft.console.client.angular;

import static org.junit.Assert.*;

import javax.xml.bind.Binder;

import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.ui.ClientDateFormater;
import org.bonitasoft.web.toolkit.client.ui.utils.I18n;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeUiBinderProvider;

@RunWith(GwtMockitoTestRunner.class)
public class AngularIFrameViewTest {

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
    public void appendTabFromTokensToUrlWithArchivedTabTokenShouldBeAppendToUrl() throws Exception {
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("aaa", "aaa_tab=archived", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("bbb", "&bbb_tab=archived", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("ccc", "ccc_tab=archived&", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("ddd", "ddd_tab=archived&test=faux", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("eee", "test=faux&eee_tab=archived", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived",
                AngularIFrameView.appendTabFromTokensToUrl("fff", "test=vrai&fff_tab=archived&test=faux", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived",
                AngularIFrameView.appendTabFromTokensToUrl("ggg", "&ggg_tab=archived&aaa_tab=archived&ggg_tab=archived&", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("aa", "?aa_tab=archived", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("bb", "?test&bb_tab=archived#", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("cc", "#?test&cc_tab=archived", "/admin/cases/list"));
        assertEquals("/admin/cases/list/archived", AngularIFrameView.appendTabFromTokensToUrl("dd", "test=vrai//&dd_tab=archived", "/admin/cases/list"));
    }

}
