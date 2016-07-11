/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component;

import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;


/**
 * @author Rohart Bastien
 *
 */
public class DoubleSection extends Section {

    public DoubleSection(final JsId jsId) {
        this(jsId, (String) null, (AbstractComponent) null);
    }

    public DoubleSection(final JsId jsId, final String title) {
        this(jsId, title, (AbstractComponent) null);
    }

    public DoubleSection(final JsId jsId, final AbstractComponent... body) {
        this(jsId, null, body);
    }

    public DoubleSection(final String title) {
        this(null, title, (AbstractComponent) null);
    }

    public DoubleSection(final String title, final AbstractComponent... body) {
        this(null, title, body);
    }

    public DoubleSection(final JsId jsId, final String title, final AbstractComponent... body) {
        super(jsId, title, body);
        this.addClass(CssClass.DOUBLE_SECTION);
    }

}
