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
package org.bonitasoft.console.client.mvp;

import com.google.gwt.user.cellview.client.CellList;

/**
 * @author Vincent Elcrin
 */
public class CellStyle implements CellList.Style {

    private final String style;

    public CellStyle(String style) {
        this.style = style;
    }

    @Override
    public String cellListEvenItem() {
        return "even " + style;
    }

    @Override
    public String cellListKeyboardSelectedItem() {
        return "selected";
    }

    @Override
    public String cellListOddItem() {
        return "odd " + style;
    }

    @Override
    public String cellListSelectedItem() {
        return "selected";
    }

    @Override
    public String cellListWidget() {
        return null;
    }

    @Override
    public boolean ensureInjected() {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
