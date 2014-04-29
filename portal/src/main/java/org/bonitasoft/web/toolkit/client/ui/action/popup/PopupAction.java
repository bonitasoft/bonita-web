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
package org.bonitasoft.web.toolkit.client.ui.action.popup;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Julien Mege
 * 
 * @deprecated
 *             use ActionShowPopup instead and add parameter in constructor of the page via addParameter method
 */
public class PopupAction extends Action {

    protected String token = null;

    public PopupAction() {
        super();
    }

    public PopupAction(final String token) {
        super();
        this.token = token;
    }

    public PopupAction(final String token, final Map<String, String> params) {
        super();
        this.token = token;
        this.setParameters(params);
    }

    public PopupAction(final String token, final Arg... params) {
        super();
        this.token = token;
        this.setParameters(params);
    }

    @Override
    public void execute() {
        ViewController.showPopup(this.token, getParameters());
    }

    protected void setToken(final String token) {
        this.token = token;
    }

}
