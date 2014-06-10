package org.bonitasoft.console.client.angular;

import com.google.gwt.user.client.ui.HTML;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

/**
 * @author Vincent Elcrin
 */
public class AngularIFrameView extends RawView {

    public final String token;

    public AngularIFrameView(String token) {
        this.token = token;
        setToken(token);
    }

    @Override
    public String defineToken() {
        return "whatever";
    }

    @Override
    public void buildView() {
        HTML html = new HTML("<div><p>Here goes the Angular application</p></div>");
        html.setStyleName("body");
        addBody(new UiComponent(html));
        addClass("page");
    }

    @Override
    protected void refreshAll() {

    }
}
