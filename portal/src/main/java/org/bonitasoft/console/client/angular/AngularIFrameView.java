/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 ******************************************************************************/
package org.bonitasoft.console.client.angular;

import static org.bonitasoft.web.toolkit.client.common.util.StringUtil.isBlank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.user.cases.view.IFrameView;
import org.bonitasoft.console.client.user.process.action.ProcessInstantiationCallbackBehavior;
import org.bonitasoft.console.client.user.process.view.ProcessInstantiationEventListener;
import org.bonitasoft.console.client.user.task.action.TaskExecutionCallbackBehavior;
import org.bonitasoft.console.client.user.task.view.TaskExecutionEventListener;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.url.UrlSerializer;
import org.bonitasoft.web.toolkit.client.eventbus.MainEventBus;
import org.bonitasoft.web.toolkit.client.eventbus.events.MenuClickEvent;
import org.bonitasoft.web.toolkit.client.eventbus.events.MenuClickHandler;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Vincent Elcrin
 * @author Julien Reboul
 */
public class AngularIFrameView extends RawView {

    public static final String CASE_LISTING_TOKEN = "caselistinguser";

    public static final String CASE_LISTING_ADMIN_TOKEN = "caselistingadmin";

    public static final String APPLICATION_LISTING_PAGE = "applicationslistingadmin";

    public static final String PROCESS_MORE_DETAILS_ADMIN_TOKEN = "processmoredetailsadmin";

    public static final String CASE_LISTING_ARCHIVED_TAB = "archived";

    public static final String CASE_LISTING_TAB_TOKEN = "_tab";

    public static final String CASE_LISTING_PROCESS_ID_TOKEN = "processId";

    public static final String TASK_LISTING_TOKEN = "tasklistinguser";

    protected final IFrameView iframe;

    protected final static Map<String, List<String>> acceptedToken = initAcceptedTokens();

    private String url;

    private String token;

    public static Map<String, String> angularViewsMap = new HashMap<String, String>();

    /**
     * add a route support to angular
     *
     * @param token the gwt token to match
     * @param route the matching angular route
     */
    public static void addTokenSupport(final String token, final String route) {
        angularViewsMap.put(token, route);
    }

    /**
     * get route associated to given token when it exists, null otherwise
     *
     * @param token the token to get the route from
     * @return the route
     */
    public static String getRoute(final String token) {
        return angularViewsMap.get(token);
    }

    /**
     * @param token2
     * @return
     */
    public static boolean supportsToken(final String token) {
        return angularViewsMap.containsKey(token);
    }

    public AngularIFrameView() {

        iframe = createIFrame();

        MainEventBus.getInstance().addHandler(MenuClickEvent.TYPE, new MenuClickHandler() {

            @Override
            public void onMenuClick(final MenuClickEvent menuClickEvent) {
                // remove angular parameters from url
                final AngularParameterCleaner angularParameterCleaner = new AngularParameterCleaner(menuClickEvent.getToken(), getHash());
                updateHash(angularParameterCleaner.getHashWithoutAngularParameters());
            }
        });
    }

    protected IFrameView createIFrame() {
        return new IFrameView(new ProcessInstantiationEventListener(new ProcessInstantiationCallbackBehavior()),
                new TaskExecutionEventListener(new TaskExecutionCallbackBehavior()));
    }

    /**
     * @return
     */
    private static Map<String, List<String>> initAcceptedTokens() {
        final Map<String, List<String>> results = new HashMap<String, List<String>>();
        results.put(PROCESS_MORE_DETAILS_ADMIN_TOKEN, Arrays.asList("id", "tab"));
        return results;
    }

    public native String getHash() /*-{
        return $wnd.location.hash;
    }-*/;

    public native void updateHash(String hash) /*-{
        $wnd.location.hash = hash;
    }-*/;

    @Override
    public String defineToken() {
        return null;
    }

    @Override
    public void buildView() {
        final SimplePanel panel = new SimplePanel();
        panel.setStyleName("body");
        panel.add(iframe);
        addBody(new UiComponent(panel));
        addClass("page page_custompage_");
    }

    @Override
    protected void refreshAll() {
    }

    /**
     * @param url
     *        Iframe url to set
     */
    public void setUrl(final String url, final String token) {
        setToken(token);
        this.url = url;
        this.token = token;
    }

    /**
     * build angular Url
     *
     * @param url
     *        the angular base path
     * @param token
     *        the current page token
     * @param queryString
     *        the URL query to set
     * @return the angular url to access for the given token
     */
    protected String buildAngularUrl(final String url, final String token, final String queryString) {
        final AngularUrlBuilder angularUrlBuilder = new AngularUrlBuilder(url)
                .appendQueryStringParameter(token + "_id", queryString + "&" + getHash())
                .appendQueryStringParameter(token + "_tab", queryString + "&" + getHash());
        if (acceptedToken.containsKey(token)) {
            for (final String param : acceptedToken.get(token)) {
                angularUrlBuilder.appendQueryStringParameter(param, queryString + "&" + getHash());
            }
        }
        return angularUrlBuilder.build() + (isBlank(queryString) ? "" : "?" + queryString.replaceAll(token + '_', ""));
    }

    /**
     * @return the token
     */
    @Override
    public String getToken() {
        return token;
    }

    public void display(final TreeIndexed<String> params) {
        iframe.setLocation(buildAngularUrl(url, token, UrlSerializer.serialize(params)));
    }
}
