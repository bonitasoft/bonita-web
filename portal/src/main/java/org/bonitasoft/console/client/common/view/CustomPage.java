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

import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.utils.Loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Element;

/**
 * @author Anthony Birembaut
 * 
 */
public class CustomPage extends Page {

    public final static String TOKEN = "page_";

    private final String pageName;

    public CustomPage(final String pageName) {
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
        final Container<Html> container = new Container<Html>();
        addBody(container);

        // request to the custom page servlet + display the response
        RequestBuilder theRequestBuilder;
        final String url = buildCustomPageURL(pageName);
        theRequestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        theRequestBuilder.setCallback(new RequestCallback() {

            @Override
            public void onError(final Request aRequest, final Throwable anException) {
                GWT.log("Unable to load the custom page " + pageName, anException);
                Loader.hideLoader();
            }

            @Override
            public void onResponseReceived(final Request request, final Response response) {
                Loader.hideLoader();
                displayCustomPageContent(container, response);
            }
        });
        Loader.showLoader();
        try {
            theRequestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Unable to initiate the request to the custom page servlet for page " + pageName, e);
        }
    }

    protected String buildCustomPageURL(final String pageName) {
        final StringBuilder servletURL = new StringBuilder().append(GWT.getModuleBaseURL()).append("custompage?page=").append(pageName).append("&locale=")
                .append(AbstractI18n.getDefaultLocale().toString()).append("&profile=").append(ClientApplicationURL.getProfileId());
        return servletURL.toString();
    }

    protected void displayCustomPageContent(final Container<Html> container, final Response response) {
        if (response.getStatusCode() == Response.SC_OK) {
            container.empty();
            final Html content = new Html(response.getText());
            container.append(content);
            // injectScripts(content);
        } else if (response.getStatusCode() == Response.SC_INTERNAL_SERVER_ERROR) {
            container.empty();
            container.append(new Html("<p>" + response.getText() + "</p>"));
        }
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
