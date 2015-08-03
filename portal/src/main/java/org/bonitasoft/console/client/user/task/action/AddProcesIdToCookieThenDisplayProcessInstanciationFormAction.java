/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.user.task.action;

import java.util.ArrayList;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;

/**
 * @author Nicolas
 * 
 */
public class AddProcesIdToCookieThenDisplayProcessInstanciationFormAction extends Action {

    private static final String ALREADY_STARTED_ARRAY_COOKIE_KEY = "AlreadyStartedProcessId";

    protected final RawView view;

    public AddProcesIdToCookieThenDisplayProcessInstanciationFormAction(RawView view) {
        this.view = view;
    }

    @Override
    public void execute() {
        TreeIndexed<String> parameters = view.getParameters();
        setAlreadyStartedCookie(parameters);
        ViewController.showView(view.getToken(), parameters);
    }

    protected void setAlreadyStartedCookie(TreeIndexed<String> parameters) {
        String processIdArrayAsString = Cookies.getCookie(ALREADY_STARTED_ARRAY_COOKIE_KEY);
        String processId = parameters.getValue("id");
        ArrayList<String> processIdArray = new ArrayList<String>();
        processIdArray.add(processId);
        if (processIdArrayAsString != null) {
            JSONValue jsonValue = JSONParser.parseStrict(processIdArrayAsString);
            JSONArray jsonArray = jsonValue.isArray();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    processIdArray.add(jsonArray.get(i).isString().stringValue());
                }
            }
        }
        String alreadyStartedCookieValue = JSonSerializer.serialize(processIdArray);
        Cookies.setCookie(ALREADY_STARTED_ARRAY_COOKIE_KEY, alreadyStartedCookieValue);
    }
}
