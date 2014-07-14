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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasCounters;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.popup.ItemDeletePopupAction;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

/**
 * @author Firstname Lastname
 * 
 */
public abstract class ItemTableActionSet<T extends IItem> implements HasCounters, HasDeploys {

    private ItemTable itemTable = null;

    private final List<ItemTableAction> actions = new LinkedList<ItemTableAction>();

    private Action defaultAction = null;

    private final List<String> counters = new ArrayList<String>();

    private final List<String> deploys = new ArrayList<String>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ADD AN ACTION WITH A LABEL
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add an action
     * 
     * @param label
     *            The text to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @deprecated preferable to pass an object to a method instead of a lot of parameters.<br/> 
     *  use {@link #addAction(ItemTableAction)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final String label, final String tooltip, final Action action) {
        return this.addAction((JsId) null, label, tooltip, action, false);
    }

    /**
     * Add an action
     * 
     * @param jsid
     *            The jsid to set on the component displaying the action
     * @param label
     *            The text to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @deprecated preferable to pass an object to a method instead of a lot of parameters. Furthermore param jsid is not used<br/> 
     *  use {@link #addAction(ItemTableAction)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final JsId jsid, final String label, final String tooltip, final Action action) {
        return this.addAction(jsid, label, tooltip, action, false);
    }

    /**
     * Add an action
     * 
     * @param label
     *            The text to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @param setAsDefault
     *            Define this action as the action to call while clicking on the line itself
     * @deprecated preferable to pass an object to a method instead of a lot of parameters.<br/> 
     *  use {@link #addAction(ItemTableAction, boolean)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final String label, final String tooltip, final Action action, final boolean setAsDefault) {
        return this.addAction((JsId) null, label, tooltip, action, setAsDefault);
    }

    /**
     * Add an action
     * 
     * @param jsid
     *            The jsid to set on the component displaying the action
     * @param label
     *            The text to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @param setAsDefault
     *            Define this action as the action to call while clicking on the line itself
     *            
     * @deprecated preferable to pass an object to a method instead of a lot of parameters. Furthermore param jsid is not used<br/> 
     *  use {@link #addAction(ItemTableAction, boolean)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final JsId jsid, final String label, final String tooltip, final Action action, final boolean setAsDefault) {
        final ItemTableAction itemTableAction = new ItemTableAction(label, tooltip, action);
        this.actions.add(itemTableAction);
        if (setAsDefault && this.defaultAction == null) {
            itemTableAction.addClass("default");
            this.defaultAction = action;
        }
        return this;
    }

    /**
     * Add an {@link ItemTableAction} to the {@link ItemTableActionSet}
     * 
     * @param itemTableAction   the {@link ItemTableAction} to be added
     */
    public ItemTableActionSet<T> addAction(final ItemTableAction itemTableAction) {
        return addAction(itemTableAction, false);
    }
    
    /**
     * Add an {@link ItemTableAction} to the {@link ItemTableActionSet}
     * 
     * @param itemTableAction   the {@link ItemTableAction} to be added
     * @param setAsDefault  Define this action as the action to call while clicking on the line itself
     */
    public ItemTableActionSet<T> addAction(final ItemTableAction itemTableAction, final boolean setAsDefault) {
        this.actions.add(itemTableAction);
        if (setAsDefault && this.defaultAction == null) {
            itemTableAction.addClass("default");
            this.defaultAction = itemTableAction.getAction();
        }
        return this;
    }
    
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ADD AN ACTION WITH AN IMAGE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add an action
     * 
     * @param image
     *            The image to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @deprecated preferable to pass an object to a method instead of a lot of parameters<br/> 
     *  use {@link #addAction(ItemTableAction)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final Url image, final String tooltip, final Action action) {
        return this.addAction((JsId) null, image, tooltip, action, false);
    }

    /**
     * Add an action
     * 
     * @param jsid
     *            The jsid to set on the component displaying the action
     * @param image
     *            The image to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @deprecated preferable to pass an object to a method instead of a lot of parameters. Furthermore param jsid is not used<br/> 
     *  use {@link #addAction(ItemTableAction)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final JsId jsid, final Url image, final String tooltip, final Action action) {
        return this.addAction(jsid, image, tooltip, action, false);
    }

    /**
     * Add an action
     * 
     * @param image
     *            The image to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @param setAsDefault
     *            Define this action as the action to call while clicking on the line itself
     * @deprecated preferable to pass an object to a method instead of a lot of parameters.<br/> 
     *  use {@link #addAction(ItemTableAction, boolean)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final Url image, final String tooltip, final Action action, final boolean setAsDefault) {
        return this.addAction((JsId) null, image, tooltip, action, setAsDefault);
    }

    /**
     * Add an action
     * 
     * @param jsid
     *            The jsid to set on the component displaying the action
     * @param image
     *            The image to display on the component
     * @param tooltip
     *            The tooltip that will help to describe the action
     * @param action
     *            The action to run.
     * @param setAsDefault
     *            Define this action as the action to call while clicking on the line itself
     * @deprecated preferable to pass an object to a method instead of a lot of parameters. Furthermore param jsid is not used<br/> 
     *  use {@link #addAction(ItemTableAction, boolean)}
     */
    @Deprecated
    public ItemTableActionSet<T> addAction(final JsId jsid, final Url image, final String tooltip, final Action action, final boolean setAsDefault) {

        final ItemTableAction itemTableAction = new ItemTableAction(image, tooltip, action);
        this.actions.add(itemTableAction);

        if (setAsDefault && this.defaultAction == null) {
            itemTableAction.addClass("default");
            this.defaultAction = action;
        }
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ADD A STANDARD DELETE ACTION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Shortcut to add a delete action
     * 
     * @param tooltip
     *            The tooltip that will help to describe the action
     */
    public ItemTableActionSet<T> addActionDelete(final String tooltip) {
        return this.addActionDelete(tooltip, this.itemTable.getItemDefinition());
    }

    /**
     * Shortcut to add a delete action
     * 
     * @param tooltip
     *            The tooltip that will help to describe the action
     */
    public ItemTableActionSet<T> addActionDelete(final String tooltip, final ItemDefinition itemDefinition) {
        return this.addActionDelete(tooltip, new ItemDeletePopupAction(itemDefinition));
    }

    /**
     * Shortcut to add a delete action
     * 
     * @param tooltip
     *            The tooltip that will help to describe the action
     */
    public ItemTableActionSet<T> addActionDelete(final String tooltip, final ItemDeletePopupAction deleteAction) {
        ItemTableAction deleteItemTableAction = new ItemTableAction(_("Delete"), tooltip, deleteAction);
        return this.addAction(deleteItemTableAction);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTER/GETTER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param itemTable
     *            the itemTable to set
     */
    public void setItemTable(final ItemTable itemTable) {
        this.itemTable = itemTable;
    }

    public ItemTable getItemTable() {
        return itemTable;
    }

    public List<ItemTableAction> getActionsFor(final T item) {
        this.actions.clear();
        this.defaultAction = null;
        defineActions(item);
        // for (final ItemTableAction action : this.actions) {
        // action.getAction().addParameter("id", item.getId());
        // }

        return this.actions;
    }

    public Action getDefaultAction(final T item) {
        this.actions.clear();
        this.defaultAction = null;
        defineActions(item);

        return this.defaultAction;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTERS & DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasCounters#getCounters()
     */
    @Override
    public List<String> getCounters() {
        return this.counters;
    }

    /**
     * Has to be called out side of defineAction method if the item passed to it need to benefits of it
     * 
     * @param attributeName
     *            of the counter
     * @return
     */
    public ItemTableActionSet<T> addCounter(final String attributeName) {
        this.counters.add(attributeName);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys#getDeploys()
     */
    @Override
    public List<String> getDeploys() {
        return this.deploys;
    }

    /**
     * Has to be called out side of defineAction method if the item passed to it need to benefits of it
     * 
     * @param attributeName
     *            of the deploy
     * @return
     */
    public ItemTableActionSet<T> addDeploy(final String attributeName) {
        this.deploys.add(attributeName);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ABSTRACTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract void defineActions(final T item);

}
