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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class TableLine extends ContainerStyled<TableCell> {

    private Action defaultAction = null;

    /**
     * @param defaultAction
     *            the defaultAction to set
     */
    public void setDefaultAction(final Action defaultAction) {
        this.defaultAction = defaultAction;
    }

    @Override
    protected void initTagNames() {
        this.rootTagName = "div";
        this.rootTagClass = "tr";
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();

        if (this.defaultAction != null) {

            $(this.element).click(new Function() {

                @Override
                public void f(final Element e) {
                    TableLine.this.defaultAction.execute();
                }

            });
        }

    }

}
