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
package org.bonitasoft.web.toolkit.client.ui.action;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.RawView;

/**
 * @author Vincent Elcrin
 */
public class ActionShowView extends Action {

    private final String token;
    private final TreeIndexed<String> parameters;

    public ActionShowView(final RawView view) {
        token = view.getToken();
        parameters = view.getParameters();
    }

    public ActionShowView(final String token, final TreeIndexed<String> parameters) {
        this.token = token;
        this.parameters = parameters;
    }

    @Override
    public void execute() {
        ViewController.showView(token, parameters);
    }
}
