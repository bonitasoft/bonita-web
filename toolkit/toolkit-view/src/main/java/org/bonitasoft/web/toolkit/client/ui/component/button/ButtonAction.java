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
package org.bonitasoft.web.toolkit.client.ui.component.button;

import com.google.gwt.uibinder.client.UiConstructor;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.utils.TypedString;

/**
 * @author Colin PUY
 * 
 */
public class ButtonAction extends Button {

    public static final String CSS_CLASS = "btn-action";

    @UiConstructor
    public ButtonAction(String label) {
        super(label, null, (Action) null);
    }

    public ButtonAction(String label, String tooltip, Action action) {
        super(label, tooltip, action);
    }
    
    public ButtonAction(String id, String label, String tooltip, Action action) {
        super(id, label, tooltip, action);
    }
    
    public ButtonAction(String id, final String label, final String tooltip, final TypedString link) {
        super(id, label, tooltip, link);
    }
    
    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();
        element.addClassName(CSS_CLASS);
    }
}
