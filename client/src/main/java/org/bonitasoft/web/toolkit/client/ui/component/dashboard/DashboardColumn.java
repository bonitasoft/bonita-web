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
package org.bonitasoft.web.toolkit.client.ui.component.dashboard;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;

/**
 * @author Séverin Moussel
 * 
 */
public class DashboardColumn extends ContainerStyled<Gadget> {

    public DashboardColumn() {
        super();
    }

    public DashboardColumn(final Gadget... components) {
        super(components);
    }

    public DashboardColumn(final JsId jsid, final Gadget... components) {
        super(jsid, components);
    }

    public DashboardColumn(final JsId jsid) {
        super(jsid);
    }

    @Override
    protected void preProcessHtml() {
        setRootTag("div", "column");
        setWrapTag(null, null);
    }

}
