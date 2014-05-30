/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.core;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;

/**
 * Created by Vincent Elcrin
 * Date: 02/10/13
 * Time: 16:22
 */
public class UiComponent extends Component implements FormNode {

    private static Panel pound = new AbsolutePanel() {

        @Override
        public void add(Widget child) {
            getChildren().add(child);
            adopt(child);
        }
    };

    static {
        pound.setVisible(false);
        RootPanel.get().add(pound);
    }

    protected final UIObject uiObject;

    public UiComponent(UIObject uiObject) {
        this.uiObject = uiObject;
    }

    @Override
    protected Element makeElement() {

        if(uiObject instanceof Widget) {
            pound.add((Widget) uiObject);
        }

        return uiObject.getElement();
    }
}
