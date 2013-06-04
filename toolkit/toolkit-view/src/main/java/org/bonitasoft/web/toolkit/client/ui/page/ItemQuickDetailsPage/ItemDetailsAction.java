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
package org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemDetailsAction {

    private final Action action;

    private final String label;

    private final String tooltip;

    private final JsId jsId;
    
    private boolean enabled = true;

    public ItemDetailsAction(final JsId id, final String label, final String tooltip, final Action action) {
        super();
        this.action = action;
        this.label = label;
        this.tooltip = tooltip;
        this.jsId = id;
    }

    public ItemDetailsAction(final JsId id, final String label, final String tooltip, final String pageToken) {
        super();
        this.action = new RedirectionAction(pageToken);
        this.label = label;
        this.tooltip = tooltip;
        this.jsId = id;
    }

    public ItemDetailsAction(final JsId id, final String label, final String tooltip, final String pageToken, final Map<String, String> parameters) {
        super();
        this.action = new RedirectionAction(pageToken, parameters);
        this.label = label;
        this.tooltip = tooltip;
        this.jsId = id;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return this.tooltip;
    }

    /**
     * @return the jsid
     */
    public JsId getJsId() {
        return this.jsId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
