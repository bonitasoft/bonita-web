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
package org.bonitasoft.console.client.javascript.libs;

import org.bonitasoft.console.client.javascript.JsResource;
import org.bonitasoft.console.client.javascript.Lib;

/**
 * @author Vincent Elcrin
 * 
 */
public class JQueryPlus extends Lib {

    public JQueryPlus() {
        super(
                new JsResource(JQueryPlusRes.INSTANCE.extend()),
                new JsResource(JQueryPlusRes.INSTANCE.debug()),
                new JsResource(JQueryPlusRes.INSTANCE.vartypes()),
                new JsResource(JQueryPlusRes.INSTANCE.compatibility()),
                new JsResource(JQueryPlusRes.INSTANCE.dom()),
                new JsResource(JQueryPlusRes.INSTANCE.options()),
                new JsResource(JQueryPlusRes.INSTANCE.css()),
                new JsResource(JQueryPlusRes.INSTANCE.uiManager()),
                new JsResource(JQueryPlusRes.INSTANCE.togglePanel()),
                new JsResource(JQueryPlusRes.INSTANCE.enable()),
                new JsResource(JQueryPlusRes.INSTANCE.selectors()),
                new JsResource(JQueryPlusRes.INSTANCE.color()),
                new JsResource(JQueryPlusRes.INSTANCE.events()),
                new JsResource(JQueryPlusRes.INSTANCE.manipulation()),
                new JsResource(JQueryPlusRes.INSTANCE.checkbox()),
                new JsResource(JQueryPlusRes.INSTANCE.tabs()),
                new JsResource(JQueryPlusRes.INSTANCE.loading()));
    }
}
