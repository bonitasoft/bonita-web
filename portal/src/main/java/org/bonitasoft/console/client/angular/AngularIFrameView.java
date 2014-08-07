package org.bonitasoft.console.client.angular;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.SimplePanel;
import org.bonitasoft.console.client.user.cases.view.IFrameView;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

/**
 * @author Vincent Elcrin
 */
public class AngularIFrameView extends RawView {

    private String url;

    private static final AngularResourceRoot ROOT = GWT.create(AngularResourceRoot.class);

    public AngularIFrameView(String token, String url) {
        setToken(token);
        this.url = url;
    }

    @Override
    public String defineToken() {
        return null;
    }

    @Override
    public void buildView() {
        SimplePanel panel = new SimplePanel();
        panel.setStyleName("body");
        panel.add(new IFrameView(ROOT.contextualize(url)));
        addBody(new UiComponent(panel));
        addClass("page");
    }

    @Override
    protected void refreshAll() {
    }
}
