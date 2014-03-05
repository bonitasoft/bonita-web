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
package org.bonitasoft.web.toolkit.client.ui.utils;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.query.client.GQuery;

/**
 * Convenient class to add css class "current" to elements.
 * 
 * @author Vincent Elcrin
 * 
 */
public class ComponentSelector {

    private final Component target;

    public final static String CSS_SELECTOR_CLASS = "current";

    private boolean multipleSelection;

    private static final String ALL_SELECTOR = "*";

    public ComponentSelector(final Component target) {
        this(target, false);
    }

    public ComponentSelector(final Component target, final boolean multipleSelection) {
        this.target = target;
        this.multipleSelection = multipleSelection;
    }

    public ComponentSelector select(GQuery gQuery) {
        if (!isMultipleSelection()) {
            unselect();
        }

        gQuery.addClass(CSS_SELECTOR_CLASS);
        return this;
    }

    /**
     * Remove selector class from all selected element contained in the target.
     */
    public ComponentSelector unselect() {
        $(ALL_SELECTOR, target.getElement()).removeClass(CSS_SELECTOR_CLASS);
        return this;
    }

    /**
     * Remove css selector class from item defined by jsid.
     * 
     * @param itemJsId
     */
    public ComponentSelector unselect(GQuery gQuery) {
        gQuery.removeClass(CSS_SELECTOR_CLASS);
        return this;
    }

    /**
     * @return the multiple
     */
    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    /**
     * @param multipleSelection
     *            the multipleSelection to set
     */
    public ComponentSelector setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
        return this;
    }

}
