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
package org.bonitasoft.console.client.admin.process.action;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemIds;

/**
 * @author Séverin Moussel
 * 
 */
public class ChangeProcessStateAction extends ActionOnItemIds {

    private final String state;

    public ChangeProcessStateAction(final String state) {
        super();
        this.state = state;
    }

    public ChangeProcessStateAction(final String state, final APIID... itemIds) {
        super(itemIds);
        this.state = state;
    }

    public ChangeProcessStateAction(final String state, final List<APIID> itemIds) {
        super(itemIds);
        this.state = state;
    }

    @Override
    protected void execute(final List<APIID> ids) {
        for (final APIID id : ids) {
            ProcessDefinition.get().getAPICaller().update(
                    id,
                    MapUtil.asMap(new Arg(ProcessItem.ATTRIBUTE_ACTIVATION_STATE, this.state)),
                    new APICallback() {

                        @Override
                        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                            ViewController.refreshCurrentPage();
                        }

                        @Override
                        public void onError(final String message, final Integer errorCode) {
                            // Do nothing
                        }
                    });
        }
    }

}
