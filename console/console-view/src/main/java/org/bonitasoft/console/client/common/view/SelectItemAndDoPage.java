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
package org.bonitasoft.console.client.common.view;

import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class SelectItemAndDoPage extends Page {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract Action defineSubmitButtonAction();

    protected abstract String defineSubmitButtonLabel();

    protected abstract String defineSubmitButtonTooltip();

    protected abstract List<SelectItemAndDoEntry> defineEntries();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BUILD CONTENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void buildView() {
        // Before
        buildBefore();

        // Build form
        final SelectItemAndDoForm selectItemAndDoForm = new SelectItemAndDoForm(
                defineEntries(),
                defineSubmitButtonLabel(),
                defineSubmitButtonTooltip(),
                defineSubmitButtonAction());
        selectItemAndDoForm.setHiddenEntries(getParameters());

        addBody(selectItemAndDoForm);
    }

    protected void buildBefore() {
        // Do nothing if not overriden
    }

}
