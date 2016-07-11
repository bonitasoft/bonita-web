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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISearchIndexOutOfRange;
import org.bonitasoft.web.toolkit.client.common.exception.http.HttpException;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.page.MessageTyped;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

/**
 * @author Séverin Moussel
 */
class ItemTableFiller extends Filler<ItemTable> {

    @Override
    protected final void getData(final APICallback callback) {
        new APICaller(this.target.getItemDefinition()).search(
                this.target.getPage(),
                this.target.getNbLinesByPage(),
                this.target.getOrder(),
                this.target.getSearch(),
                this.target.getFilters(),
                this.target.getDeploys(),
                this.target.getCounters(),
                callback
                );
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers.get("Content-Range"));

        this.target.setPager(
                resultPager.getCurrentPage(),
                resultPager.getNbTotalResults(),
                resultPager.getNbResultsByPage()
                );

        // FIXME add type to Table
        this.target.setItems((List<IItem>) JSonItemReader.parseItems(json, this.target.getItemDefinition()));

        this.target.updateView();
    }

    @Override
    protected void onError(final RuntimeException e) {
        MessageTyped.TYPE type = MessageTyped.TYPE.ALERT;

        // Use notifications for invisible elements
        if (!$(this.target.getElement()).is(":visible")) {
            type = MessageTyped.TYPE.WARNING;
        }

        // Use notifications for repeated fillers
        else if (getRepeatEvery() > 0) {
            type = MessageTyped.TYPE.WARNING;
        }

        // API specific errors
        if (e instanceof APIException) {
            final APIException ex = (APIException) e;
            if (APISearchIndexOutOfRange.class.toString().equals(ex.getOriginalClassName())) {
                Message.show(type, _("The page you try to display is out of range."));
                return;
            }
        }

        // Server generic errors
        if (e instanceof ServerException) {
            Message.show(type, _("The elements you try to display can't be retrieved due to a server problem.") + "\n"
                    + _("Please contact your administrator."));
            Message.log(e);
            return;
        }

        // Http generic errors
        if (e instanceof HttpException) {
            Message.show(type, _("The elements you try to display can't be retrieved due to a connection problem.") + "\n"
                    + _("Please contact your administrator."));
            Message.log(e);
            return;
        }

        // Uncatched errors
        super.onError(e);
    }
}
