/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.RawView;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * This class is a custom version of the GWT FlowPanel.<br>
 * This version allows to call the triggerLoad function of the view controller when the main panel is loaded.<br>
 * <br>
 * 
 * @author Julien Mege
 */
public final class CustomPanel extends FlowPanel {

    private RawView view = null;

    public CustomPanel(final RawView view) {
        this.view = view;
    }

    @Override
    public final void onLoad() {
        this.view.onLoad();
        ViewController.getInstance().triggerLoad();
    }
}
