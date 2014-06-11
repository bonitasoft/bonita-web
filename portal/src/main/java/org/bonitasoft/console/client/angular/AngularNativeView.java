package org.bonitasoft.console.client.angular;

import java.util.LinkedList;
import java.util.Queue;

import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
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

    private class ChainingScriptLoadingCallBack implements Callback<Void, Exception> {

        protected Queue<String> scriptsToLoad = new LinkedList<String>();
        /**
         * @see com.google.gwt.core.client.Callback#onFailure(java.lang.Object)
         */
        @Override
        public void onFailure(Exception reason) {
            Window.alert("impossible to load ");
        }

        /**
         * @see com.google.gwt.core.client.Callback#onSuccess(java.lang.Object)
         */
        @Override
        public void onSuccess(Void result) {
            if (scriptsToLoad.isEmpty()) {

            } else {
                ScriptInjector.fromUrl(scriptsToLoad.poll()).setWindow(ScriptInjector.TOP_WINDOW).setCallback(this).inject();
            }
        }

        /**
         * Default Constructor.
         */
        public ChainingScriptLoadingCallBack() {
            scriptsToLoad.add("//localhost:9000/bower_components/angular-resource/angular-resource.js");
            scriptsToLoad.add("//localhost:9000/bower_components/angular-cookies/angular-cookies.js");
            scriptsToLoad.add("//localhost:9000/bower_components/angular-sanitize/angular-sanitize.js");
            scriptsToLoad.add("//localhost:9000/bower_components/angular-route/angular-route.js");
            scriptsToLoad.add("//localhost:9000/scripts/app.js");
            scriptsToLoad.add("//localhost:9000/scripts/controllers/main.js");
        }
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
