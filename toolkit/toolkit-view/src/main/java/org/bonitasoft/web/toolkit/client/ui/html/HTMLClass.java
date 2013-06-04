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
package org.bonitasoft.web.toolkit.client.ui.html;

/**
 * @author Séverin Moussel
 * 
 */
public class HTMLClass extends SpecificAttribute {

    public HTMLClass() {
        super();
    }

    public HTMLClass(final String className) {
        super(className);
    }

    public HTMLClass(final String... classNames) {
        super();
        addClasses(classNames);
    }

    @Override
    public HTMLClass addClass(final String className) {
        return (HTMLClass) super.addClass(className);
    }

    public HTMLClass addClasses(final String... classNames) {
        for (int i = 0; i < classNames.length; i++) {
            addClass(classNames[i]);
        }
        return this;
    }

    @Override
    protected String getAttributeName() {
        return "class";
    }

}
