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
package org.bonitasoft.web.toolkit.client.data.api.request;

import org.bonitasoft.web.toolkit.client.common.UrlBuilder;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

import com.google.gwt.http.client.RequestBuilder;

/**
 * @author Séverin Moussel
 * 
 */
public class APIDeleteRequest extends AbstractAPIManyIdsRequest {

    public APIDeleteRequest(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    @Override
    public void run() {
        if (this.ids.size() == 0) {
            throw new APIException("Delete must take at least one id.");
        } else if (this.ids.size() == 1) {
            this.request = new RequestBuilder(RequestBuilder.DELETE, this.itemDefinition.getAPIUrl() + "/" + this.ids.get(0));
        } else {

            final UrlBuilder url = new UrlBuilder(this.itemDefinition.getAPIUrl());

            String sb = "[";
            for (int i = 0; i < this.ids.size(); i++) {
                sb += (i > 0 ? "," : "") + "\"" + this.ids.get(i) + "\"";
            }
            sb += "]";

            this.request = new RequestBuilder(RequestBuilder.DELETE, url.toString());
            this.request.setRequestData(sb);

        }
        super.run();
    }
}
