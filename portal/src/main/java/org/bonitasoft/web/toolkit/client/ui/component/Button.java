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
package org.bonitasoft.web.toolkit.client.ui.component;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.utils.TypedString;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * A standard push-button component of the USerXP toolkit
 * 
 * @author Séverin Moussel
 */
public class Button extends Link {

    @UiConstructor
    public Button(String label) {
        super(label, null, (Action) null);
    }
    /**
     * This constructor must be prefered to other
     */
    public Button(String id, String label, String tooltip, Action action) {
        super(label, tooltip, action);
        setId(id);
    }
    
    public Button(String id, String label, String tooltip, TypedString link) {
        super(label, tooltip, link);
        setId(id);
    }

    public Button(final JsId jsid, final String label, final String tooltip, final Action action) {
        super(jsid, label, tooltip, action);
    }

    public Button(final JsId jsid, final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        super(jsid, label, tooltip, token, parameters);
    }

    public Button(final JsId jsid, final String label, final String tooltip, final String token) {
        super(jsid, label, tooltip, token);
    }

    public Button(final JsId jsid, final String label, final String tooltip, final TypedString link) {
        super(jsid, label, tooltip, link);
    }

    public Button(final String label, final String tooltip, final Action action) {
        super(label, tooltip, action);
    }

    public Button(final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        super(label, tooltip, token, parameters);
    }

    public Button(final String label, final String tooltip, final String token) {
        super(label, tooltip, token);
    }

    public Button(final String label, final String tooltip, final TypedString link) {
        super(label, tooltip, link);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE HTML
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void postProcessHtml() {
        this.element.addClassName("btn");
        super.postProcessHtml();
    }

}
