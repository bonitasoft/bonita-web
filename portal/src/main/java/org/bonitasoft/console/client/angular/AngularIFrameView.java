package org.bonitasoft.console.client.angular;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

/**
 * @author Vincent Elcrin
 */
public class AngularIFrameView extends RawView {

    public interface Template extends SafeHtmlTemplates {

        @Template("<iframe src='{0}' id='bonitaframe'></iframe>")
        public SafeHtml iFrame(String src);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);

    public AngularIFrameView(String token) {
        setToken(token);
    }

    @Override
    public String defineToken() {
        return null;
    }

    @Override
    public void buildView() {
        HTML frame = new HTML(TEMPLATE.iFrame("http://127.0.0.1:9000/"));
        frame.setStyleName("body");
        addBody(new UiComponent(frame));
        addClass("page");
    }

    @Override
    protected void refreshAll() {
    }
}
