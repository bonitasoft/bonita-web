package org.bonitasoft.console.client.angular;

import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Vincent Elcrin
 */
public class AngularNativeView extends RawView {

    interface Binder extends UiBinder<HTMLPanel, AngularNativeView> {
    }

    protected static Binder binder = GWT.create(Binder.class);


    public AngularNativeView(String token) {
        setToken(token);
    }

    @Override
    public String defineToken() {
        return null;
    }

    @Override
    public void buildView() {
        addBody(new UiComponent(binder.createAndBindUi(this)));
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                ScriptInjector
                        .fromString("angular.bootstrap(document.getElementById('angular-application'), ['portaljsApp', 'ngRoute', 'ngCookies', 'ngResource']);")
                        .setWindow(ScriptInjector.TOP_WINDOW).inject();
            }
        });
        addClass("page");
    }

    @Override
    protected void refreshAll() {
    }
}
