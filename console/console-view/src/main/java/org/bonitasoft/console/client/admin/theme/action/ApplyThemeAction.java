/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.theme.action;

import java.util.Iterator;
import java.util.Map;

import org.bonitasoft.console.client.admin.theme.view.ListThemePage;
import org.bonitasoft.console.client.model.portal.theme.ThemeItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonUtil;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.form.UpdateItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;

/**
 * @author Gai Cuisha
 * 
 */
public class ApplyThemeAction extends UpdateItemFormAction<ThemeItem> {

    /**
     * Default Constructor.
     * 
     * @param itemDefinition
     */
    public ApplyThemeAction(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    public ApplyThemeAction(final ItemDefinition itemDefinition, final AbstractForm form) {
        super(itemDefinition, form);
    }

    @Override
    public void execute() {
        final String itemId = this.getParameter("id");
        new APICaller<ThemeItem>(this.itemDefinition).update(itemId, this.form, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                // get themeName from response
                final JSONArray jsonArray = JSONParser.parseStrict(response).isArray();
                String themeName = null;
                for (int i = 0; i < jsonArray.size(); i++) {
                    final JSONObject item = (JSONObject) jsonArray.get(i);
                    final Iterator<String> j = item.keySet().iterator();
                    while (j.hasNext()) {
                        final String key = j.next();
                        if (item.get(key) instanceof JSONNull) {
                            themeName = null;
                        } else if (ThemeItem.ATTRIBUTE_NAME.equals(key)) {
                            themeName = JSonUtil.unquote(item.get(key).isString().toString());
                        }
                    }
                }
                // redirect to homepage
                final StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append(GWT.getModuleBaseURL() + "homepage?ui=user&theme=" + themeName);
                // final String locale = Window.Location.getParameter(URLUtils.LOCALE_PARAM);
                // if (locale != null) {
                // urlBuilder.append("&locale=" + locale);
                // }
                // final String domain = Window.Location.getParameter(URLUtils.DOMAIN_PARAM);
                // if (domain != null) {
                // urlBuilder.append("&domain=" + domain);
                // }
                urlBuilder.append("#" + ListThemePage.TOKEN);
                Window.Location.assign(URL.encode(urlBuilder.toString()));
            }
        });

    }
}
