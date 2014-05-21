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

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

/**
 * Define an action on Item(s)
 * 
 * @author Séverin Moussel
 * 
 */
public class ItemTableAction extends Link {

    public static enum ICON {
        NONE,
        VIEW,
        EDIT,
        DELETE,
        EXECUTE,
        CLAIM,
        ASSIGN,
        RELEASE,
        DISMISS
    };

    private Url image = null;

    private ICON icon = null;

    private boolean alwaysVisible = false;

    /**
     * Default Constructor.
     * 
     * @param jsid
     *            The jsId of the component that will trigger the action
     * @param label
     *            The label of the component that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final JsId jsid, final String label, final String tooltip, final Action action, final boolean alwaysVisible) {
        super(jsid, label, tooltip, action);
        this.alwaysVisible = alwaysVisible;
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     *            The jsId of the component that will trigger the action
     * @param label
     *            The label of the component that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final JsId jsid, final String label, final String tooltip, final Action action) {
        this(jsid, label, tooltip, action, false);
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param label
     *            The label of the component that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final String label, final String tooltip, final Action action) {
        this(null, label, tooltip, action);
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param label
     *            The label of the component that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final String label, final String tooltip, final Action action, final boolean alwaysVisible) {
        this(null, label, tooltip, action, alwaysVisible);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     *            The jsId of the component that will trigger the action
     * @param image
     *            The Url of the image to display as a button that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final JsId jsid, final Url image, final String tooltip, final Action action) {
        this(jsid, "", tooltip, action);
        this.image = image;
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     *            The jsId of the component that will trigger the action
     * @param image
     *            The Url of the image to display as a button that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final JsId jsid, final Url image, final String tooltip, final Action action, final boolean alwaysVisible) {
        this(jsid, "", tooltip, action, alwaysVisible);
        this.image = image;
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param image
     *            The Url of the image to display as a button that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final Url image, final String tooltip, final Action action) {
        this(null, image, tooltip, action);
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param image
     *            The Url of the image to display as a button that will trigger the action
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final Url image, final String tooltip, final Action action, final boolean alwaysVisible) {
        this(null, image, tooltip, action, alwaysVisible);
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final ICON icon, final String tooltip, final Action action) {
        this(null, icon, tooltip, action);
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final ICON icon, final String tooltip, final Action action, final boolean alwaysVisible) {
        this(null, icon, tooltip, action, alwaysVisible);
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final JsId jsid, final ICON icon, final String tooltip, final Action action) {
        this(jsid, "", tooltip, action);
        this.icon = icon;
    }

    /**
     * Default Constructor without JsId.
     * 
     * @param tooltip
     *            The tooltip of the component that will trigger the action
     * @param action
     *            The action to trigger
     */
    public ItemTableAction(final JsId jsid, final ICON icon, final String tooltip, final Action action, final boolean alwaysVisible) {
        this(jsid, "", tooltip, action, alwaysVisible);
        this.icon = icon;
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();

        if (this.image != null) {
            this.element.appendChild(XML.makeElement(HTML.img(this.image.toString(), this.tooltip)));
        } else if (this.icon != null) {
            this.element.addClassName("icon_" + this.icon.toString());
            $(this.element).empty();
        }

        if (this.alwaysVisible) {
            this.element.addClassName("nogroup");
        }
    }

}
