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
package org.bonitasoft.web.toolkit.client.ui.component.dropdown;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerDummy;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;

/**
 * @author Séverin Moussel
 * 
 */
public class DropDownItem extends Link {

    private final ContainerDummy<AbstractComponent> details = new ContainerDummy<AbstractComponent>();

    public DropDownItem(final String label, final String tooltip, final Action action) {
        super(label, tooltip, action);
    }

    public DropDownItem(final String label, final String tooltip, final String token) {
        super(label, tooltip, token);
    }

    public DropDownItem(final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        super(label, tooltip, token, parameters);
    }

    public final DropDownItem setDetails(final String text) {
        this.details.append(new Text(text));
        return this;
    }

    public final DropDownItem setDetails(final AbstractComponent... components) {
        this.details.append(components);
        return this;
    }

    @Override
    protected void postProcessHtml() {
        appendComponentToHtml(this.element, this.details);

        super.postProcessHtml();
    }

}
