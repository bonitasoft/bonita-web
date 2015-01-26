/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.common.view;

import java.util.List;

import org.bonitasoft.console.client.user.cases.view.IFrameView;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * @author Anthony Birembaut
 * 
 */
public class CustomPageWithFrame extends Page {

    public final static String TOKEN = "custompage_";

    private final String pageName;

    public CustomPageWithFrame(final String pageName) {
        this.pageName = pageName;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public String getToken() {
        return pageName;
    }

    @Override
    public void buildView() {
        addBody(createIframe(pageName));
    }

    private Component createIframe(final String pageName) {
        return new UiComponent(new IFrameView(buildCustomPageURL(pageName)));
    }

    protected String buildCustomPageURL(final String pageName) {
        final StringBuilder servletURL = new StringBuilder().append(GWT.getModuleBaseURL()).append("custompage?page=").append(pageName).append("&locale=")
                .append(AbstractI18n.getDefaultLocale().toString()).append("&profile=").append(ClientApplicationURL.getProfileId());
        return servletURL.toString();
    }

    /**
     * We don't need any header and it screw up the page's size.
     * 
     * @param header
     * @return
     */
    @Override
    protected List<Element> makeHeaderElements(final Container<AbstractComponent> header) {
        return null;
    }

    /**
     * We don't need any footer
     * 
     * @param footer
     * @return
     */
    @Override
    protected List<Element> makeFooterElements(Container<AbstractComponent> footer) {
        return null;
    }

    @Override
    public void defineTitle() {
        setTitle(pageName);
    }

}
