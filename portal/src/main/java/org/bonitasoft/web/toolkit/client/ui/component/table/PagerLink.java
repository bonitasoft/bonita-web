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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.Link;

/**
 * @author Julien Mege
 */
class PagerLink extends Link {

    private boolean active = true;

    private boolean current = false;

    /**
     * Default constructor.
     * 
     * @param table
     * @param number
     * @param label
     * @param active
     */
    public PagerLink(final Table table, final int number, final String label, final boolean active) {
        this(table, number, label, active, false);
    }

    /**
     * Default constructor.
     * 
     * @param table
     * @param number
     * @param label
     * @param active
     * @param current
     */
    public PagerLink(final Table table, final int number, final String label, final boolean active, final boolean current) {
        super(label, _("Go to page %pagenumber%", new Arg("pagenumber", number)), active ? new PagerAction(table, number) : null);
        this.active = active;
        this.current = current;
        setEnabled(active);
    }

    @Override
    protected void postProcessHtml() {
        this.element.addClassName((this.active ? "enable" : "disable") + (this.current ? " current" : ""));

        super.postProcessHtml();
    }

    @Override
    public PagerLink addClass(final String className) {
        super.addClass(className);
        return this;
    }

}
