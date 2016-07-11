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
package org.bonitasoft.web.toolkit.client.ui.action;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
abstract public class ActionOnItemId extends Action {

    private APIID itemId = null;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ActionOnItemId() {
        super();
    }

    public ActionOnItemId(final APIID itemId) {
        super();
        this.itemId = itemId;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final void setItemId(final APIID itemId) {
        this.itemId = itemId;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CATCH ID SETTER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setParameters(final Map<String, String> params) {
        super.setParameters(params);

        if (params.containsKey("id")) {
            setItemId(APIID.makeAPIID(params.get("id")));
        }
    }

    @Override
    public void setParameters(final TreeIndexed<String> params) {
        super.setParameters(params);

        if (params.containsKey("id")) {
            setItemId(APIID.makeAPIID(params.getValue("id")));
        }
    }

    @Override
    public void setParameters(final Arg... params) {
        super.setParameters(params);

        for (final Arg param : params) {
            if ("id".equals(param.getName())) {
                setItemId(APIID.makeAPIID(param.getValue()));
                break;
            }
        }
    }

    @Override
    public void addParameter(final String name, final String value) {
        super.addParameter(name, value);
        if ("id".equals(name)) {
            setItemId(APIID.makeAPIID(value));
        }
    }

    @Override
    public void addParameter(final String name, final String... values) {
        super.addParameter(name, values);
        if ("id".equals(name)) {
            setItemId(APIID.makeAPIID(values));
        }
    }

    @Override
    public final void addParameter(final String name, final List<String> values) {
        super.addParameter(name, values);
        if ("id".equals(name)) {
            setItemId(APIID.makeAPIID(values));
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EXECUTE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void execute() {
        execute(this.itemId);
    }

    abstract protected void execute(APIID itemId);

}
