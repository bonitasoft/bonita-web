/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.cases.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Vincent Elcrin
 */
public class IFrameView extends Composite {

    interface Binder extends UiBinder<HTMLPanel, IFrameView> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @UiField
    IFrameElement frame;

    @UiField
    SimplePanel toolbar;

    public IFrameView(final String url) {
        initWidget(binder.createAndBindUi(this));
        frame.setId("bonitaframe");
        frame.setSrc(url);
    }

    public void addTool(final Widget widget) {
        toolbar.removeStyleName("empty");
        toolbar.add(widget);
    }
}
