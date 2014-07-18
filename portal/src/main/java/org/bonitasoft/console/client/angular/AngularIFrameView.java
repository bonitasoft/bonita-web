package org.bonitasoft.console.client.angular;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.HTML;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

/**
 * @author Vincent Elcrin
 */
public class AngularIFrameView extends RawView {

    private String url;

    public interface Template extends SafeHtmlTemplates {

        @Template("<iframe src='{0}' id='bonitaframe'></iframe>")
        public SafeHtml iFrame(SafeUri src);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);

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
        HTML frame = new HTML(TEMPLATE.iFrame(UriUtils.fromSafeConstant(ROOT.contextualize(url))));
        frame.setStyleName("body");
        addBody(new UiComponent(frame));
        addClass("page");
    }

    @Override
    protected void refreshAll() {
    }
}
