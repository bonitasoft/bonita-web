/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.cases.view;

import java.util.Map;

import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.RequestBuilder;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public abstract class AbstractOverviewFormMappingRequester {

    static final String ATTRIBUTE_FORM_MAPPING_TYPE = "type";

    static final String PROCESS_OVERVIEW_FORM_MAPPING_TYPE = "PROCESS_OVERVIEW";

    static final String ATTRIBUTE_FORM_MAPPING_TARGET = "target";

    static final String NONE_FORM_MAPPING_TARGET = "NONE";

    public void searchFormMappingForInstance(final String processId) {
        final String processIdFilter = URL.encodeQueryString(PageItem.ATTRIBUTE_PROCESS_ID + "=" + processId);
        final String mappingTypeFilter = URL.encodeQueryString(ATTRIBUTE_FORM_MAPPING_TYPE + "=" + PROCESS_OVERVIEW_FORM_MAPPING_TYPE);
        final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "../API/form/mapping?c=1&p=0&f=" + processIdFilter + "&f="
                + mappingTypeFilter);
        requestBuilder.setCallback(new FormMappingCallback(processId));
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
            GWT.log("Error while creating the from mapping request", e);
        }
    }

    protected class FormMappingCallback extends APICallback {

        private final String processId;

        public FormMappingCallback(final String processId) {
            this.processId = processId;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final JSONValue root = JSONParser.parseLenient(response);
            final JSONArray formMappings = root.isArray();
            if (formMappings.size() == 1) {
                final JSONValue formMappingValue = formMappings.get(0);
                final JSONObject formMapping = formMappingValue.isObject();
                if (formMapping.containsKey(ATTRIBUTE_FORM_MAPPING_TARGET)
                        && NONE_FORM_MAPPING_TARGET.equals(formMapping.get(ATTRIBUTE_FORM_MAPPING_TARGET).isString().stringValue())) {
                    onMappingNotFound();
                } else {
                    onMappingFound();
                }
            } else {
                onMappingNotFound();
            }
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            GWT.log("Error while getting the overview mapping for process " + processId);
            super.onError(message, errorCode);
        }
    }

    public abstract void onMappingFound();

    public abstract void onMappingNotFound();
}