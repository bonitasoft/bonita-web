package org.bonitasoft.console.client.angular;

import org.bonitasoft.console.client.user.cases.view.IFrameView;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Vincent Elcrin
 */
public class AngularIFrameView extends RawView {

    private final String url;

    private static final AngularResourceRoot ROOT = GWT.create(AngularResourceRoot.class);

    public AngularIFrameView(final String token, final String url) {
        setToken(token);
        this.url = url;
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
}
