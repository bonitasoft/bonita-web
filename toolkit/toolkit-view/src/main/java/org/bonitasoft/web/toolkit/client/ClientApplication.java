/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client;

import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.json.JSonUnserializerClient;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.ui.ClientDateFormater;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.utils.I18n;
import org.bonitasoft.web.toolkit.client.ui.utils.Loader;
import org.bonitasoft.web.toolkit.client.ui.utils.Loader.POSITION;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

/**
 * @author Julien Mege, Anthony Birembaut
 */
public abstract class ClientApplication implements EntryPoint {

    private static boolean RUNNING = false;

    protected ClientApplicationURL clientApplicationURL = defineClientApplicationURL();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new CatchAllExceptionHandler());

        // Avoid double run
        if (RUNNING) {
            return;
        }
        RUNNING = true;

        // Event
        this.triggerBeforeLoad();

        // Standalone application
        // if (History.getToken().length() > 0 && !History.getToken().startsWith("?")) {
        // ClientApplication.this.onLoad();
        // }

        // Toolkit based application
        // else {
        initToolkit();
        // }
    }

    /**
     * Initialize a toolkit based application
     */
    private void initToolkit() {
        JSonItemReader.APPLY_VALIDATORS = false;

        Item.setApplyInputModifiersByDefault(true);
        Item.setApplyValidatorsByDefault(false);
        Item.setApplyOutputModifiersByDefault(true);

        JSonItemReader.setUnserializer(new JSonUnserializerClient());

        this.clientApplicationURL.parseUrl();

        // Init the i18n instance
        I18n.getInstance();

        CommonDateFormater.setDateFormater(new ClientDateFormater());

        // Pages and Definitions factories
        ApplicationFactoryClient.setDefaultFactory(ClientApplication.this.defineApplicationFactoryClient());
        ApplicationFactoryCommon.setDefaultFactory(ClientApplication.this.defineApplicationFactoryCommon());

        // 1 - Load Session
        this.clientApplicationURL.initSession(new Action() {

            @Override
            public void execute() {

                // 2 - Load i18n
                ClientApplication.this.clientApplicationURL.initLang(new Action() {

                    @Override
                    public void execute() {

                        // 3 - Catch url changes
                        ClientApplication.this.initCatchUrl();

                        // 4 - Display main view
                        ClientApplication.this.clientApplicationURL.initView();

                        ClientApplication.this.triggerLoad();
                    }
                });
            }

        });

    }

    public abstract ApplicationFactoryClient defineApplicationFactoryClient();

    public abstract ApplicationFactoryCommon defineApplicationFactoryCommon();

    protected ClientApplicationURL defineClientApplicationURL() {
        return new ClientApplicationURL();
    }

    public static void startLoading() {
        Loader.showLoader(POSITION.FULL_OVERLAY);
    }

    public static void stopLoading() {
        Loader.hideLoader(POSITION.FULL_OVERLAY);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EVENTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Called while the url has changed, just before displaying the view
     */
    private final Action onUrlChange = null;

    /**
     * Called after the app initialization
     */

    private final Action onLoad = null;

    /**
     * Called before the app initialization
     */
    private final Action onBeforeLoad = null;

    /**
     * Called while the url has changed, just before displaying the view
     */
    protected void onUrlChange() {

    }

    /**
     * Called after the app initialization
     */
    protected void onLoad() {

    }

    /**
     * Called before the app initialization
     */
    protected void onBeforeLoad() {

    }

    public void triggerUrlChange() {
        onUrlChange();
        if (this.onUrlChange != null) {
            this.onUrlChange.execute();
        }
    }

    public void triggerLoad() {
        onLoad();
        if (this.onLoad != null) {
            this.onLoad.execute();
        }
    }

    public void triggerBeforeLoad() {
        onBeforeLoad();
        if (this.onBeforeLoad != null) {
            this.onBeforeLoad.execute();
        }
    }

    protected void initCatchUrl() {
        // Url Change event definition
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                ClientApplication.this.triggerUrlChange();
                // TODO update the profile selected in loginBox if profile has changed
                ClientApplication.this.clientApplicationURL.refreshView();
            }
        });
    }

    protected void refreshView() {
        this.clientApplicationURL.refreshView();
    }
}
