/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client;

import org.bonitasoft.console.client.menu.view.LoginBox;
import org.bonitasoft.forms.client.FormsApplicationLoader;
import org.bonitasoft.forms.client.view.common.BonitaUrlContext;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryClient;
import org.bonitasoft.web.toolkit.client.ClientApplication;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;

/**
 * @author Yongtao Guo, Haojie Yuan, Ruiheng Fan
 */
public class ConsoleClient extends ClientApplication {

    @Override
    protected void onLoad() {
        registerJSNIMethods();
    
        final BonitaUrlContext bonitaUrlContext = BonitaUrlContext.get();
        if (bonitaUrlContext.isFormApplicationMode()) {
            new FormsApplicationLoader(URLUtilsFactory.getInstance(), bonitaUrlContext).load();
        } else {
            onConsoleLoad();
        }


    }

    protected void registerJSNIMethods() {
        JsniPublisher.publishMethods();
    }

    protected void onConsoleLoad() {
        if ("true".equals(Session.getParameter("is_technical_user"))) {
            refreshView();
        }
    
        // The login box will initialize the view if the login works well
        ViewController.showView(getLoginBoxView(), "login");
    }

    protected LoginBox getLoginBoxView() {
        return new LoginBox();
    }

    @Override
    public ApplicationFactoryClient defineApplicationFactoryClient() {
        return new ConsoleFactoryClient();
    }

    @Override
    public ItemDefinitionFactory defineApplicationFactoryCommon() {
        return new ModelFactory();
    }
}
