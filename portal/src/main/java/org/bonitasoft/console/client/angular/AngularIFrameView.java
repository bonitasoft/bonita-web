package org.bonitasoft.console.client.angular;

import org.bonitasoft.console.client.user.cases.view.IFrameView;
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

    public static final String CASE_LISTING_ADMIN_TOKEN = "caselistingadmin";

    public static final String CASE_LISTING_ARCHIVED_TAB = "archived";

    public static final String CASE_LISTING_TAB_TOKEN = "_tab";

    public static final String CASE_LISTING_PROCESS_ID_TOKEN = "_processId";

    private final IFrameView iframe = new IFrameView();

    public AngularIFrameView() {
        MainEventBus.getInstance().addHandler(MenuClickEvent.TYPE, new MenuClickHandler() {

            @Override
            public void onMenuClick(final MenuClickEvent menuClickEvent) {
                // remove angular parameters from url
                final AngularParameterCleaner angularParameterCleaner = new AngularParameterCleaner(menuClickEvent.getToken(), getHash());

                updateHash(angularParameterCleaner.getHashWithoutAngularParameters());
            }
        });
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
     *            Iframe url to set
     */
    public void setUrl(final String url, final String token, final String queryString) {
        setToken(token);
        iframe.setUrl(buildAngularUrl(url, token, queryString));
    }

    /**
     * build angular Url
     *
     * @param url
     *            the angular base path
     * @param token
     *            the current page token
     * @param queryString
     *            the URL query to set
     * @return the angular url to access for the given token
     */
    protected String buildAngularUrl(final String url, final String token, final String queryString) {
        return new AngularUrlBuilder(url)
        .appendQueryStringParameter(token + "_id", getHash())
        .appendQueryStringParameter(token + "_tab", getHash())
        .build() + queryString;
    }

    /**
     * @return the token
     */
    @Override
    public String getToken() {
        return token;
    }

}
