package org.bonitasoft.console.client.angular;

import static com.google.gwt.query.client.GQuery.*;

import org.bonitasoft.console.client.user.cases.view.IFrameView;
import org.bonitasoft.web.toolkit.client.SelfRenderingView;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Vincent Elcrin
 * @author Julien Reboul
 */
public class AngularIFrameView extends RawView implements SelfRenderingView {

    private String url;

    private boolean displayed = false;

    private static final AngularResourceRoot ROOT = GWT.create(AngularResourceRoot.class);

    public AngularIFrameView(final String token, final String url, final String tokens) {
        setToken(token);
        this.url = appendTabFromTokensToUrl(token, tokens, url) + tokens;
    }

    /**
     * @param tokens
     */
    protected static String appendTabFromTokensToUrl(final String pageToken, final String tokens, final String url) {
        if (tokens != null) {
            final MatchResult tabMatcher = RegExp.compile("(^|[&\\?#])" + pageToken + "_tab=([^&\\?#]*)([&\\?#]|$)").exec(tokens);
            if (tabMatcher != null && tabMatcher.getGroupCount() > 0) {
                return url + "/" + tabMatcher.getGroup(2);
            }
        }
        return url;
    }

    @Override
    public String defineToken() {
        return null;
    }

    @Override
    public void buildView() {
        final SimplePanel panel = new SimplePanel();
        panel.setStyleName("body");
        new CheckValidSessionBeforeAction(new Action() {
            @Override
            public void execute() {
                panel.add(new IFrameView(ROOT.contextualize(url)));
            }
        }).execute();
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
    public void setUrl(final String url) {
        this.url = url;
        setToken(token);
    }

    /**
     * @return the token
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.SelfRenderingView#render(com.google.gwt.user.client.Element)
     */
    @Override
    public void render(final Element rootElement) {
        if (!displayed) {
            $(rootElement).empty();
            displayed = true;
        }
    }
}
