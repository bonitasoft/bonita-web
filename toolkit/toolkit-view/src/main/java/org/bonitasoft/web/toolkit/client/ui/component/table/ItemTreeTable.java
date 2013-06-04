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

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

/**
 * @author Séverin Moussel
 * 
 */
public final class ItemTreeTable extends ItemTable {

    private final String parentIdAttributeName;

    private final String indexAttributeName;

    private ItemTableActionSet parentsActions = null;

    private ItemTableActionSet parentsGroupActions = null;

    private ItemTableActionSet childrenActions = null;

    public ItemTreeTable(final ItemDefinition itemDefinition, final String parentIdAttributeName, final String indexAttributeName) {
        super(itemDefinition);
        this.parentIdAttributeName = parentIdAttributeName;
        this.indexAttributeName = indexAttributeName;
        setFiller(new ItemTreeTableFiller(parentIdAttributeName, indexAttributeName));
        setView(Table.VIEW_TYPE.TREE);
    }

    public final ItemTreeTable addParentsActions(final ItemTableActionSet parentsActions) {
        this.parentsActions = parentsActions;
        return this;
    }

    public final ItemTreeTable addChildrenActions(final ItemTableActionSet childrenActions) {
        this.childrenActions = childrenActions;
        return this;
    }

    public final ItemTreeTable addParentsGroupActions(final ItemTableActionSet parentsGroupActions) {
        this.parentsGroupActions = parentsGroupActions;
        return this;
    }

    private IItem lastParentItemAdded = null;

    @Override
    public ItemTreeTable setItems(final List<IItem> items) {
        super.setItems(items);

        if (this.lastParentItemAdded != null && this.parentsGroupActions != null) {
            this.table.addLine(this.lastParentItemAdded.getId().toString(), "action child");
            this.addItemActions(this.lastParentItemAdded, this.parentsGroupActions.getActionsFor(this.lastParentItemAdded));
        }
        this.lastParentItemAdded = null;

        return this;
    }

    @Override
    public final ItemTreeTable addItem(final IItem item, final String className) {
        String tmpClassName = className == null ? "" : className;

        insertParentsGroupAction(item);

        final String parentId = item.getAttributeValue(this.parentIdAttributeName);

        if (parentId == null || "0".equals(parentId) || parentId.length() == 0) {
            tmpClassName += " parent";
        } else {
            tmpClassName += " child";
        }

        // Insert the item line
        this.table.addLine(item.getId().toString(), tmpClassName);
        addItemCells(item);

        if (parentId == null || "0".equals(parentId) || parentId.length() == 0) {
            if (this.parentsActions != null) {
                this.addItemActions(item, this.parentsActions.getActionsFor(item));
            }
        } else {
            if (this.childrenActions != null) {
                this.addItemActions(item, this.childrenActions.getActionsFor(item));
            }
        }
        this.addItemActions(item);

        return this;
    }

    private void insertParentsGroupAction(final IItem item) {
        final String parentId = item.getAttributeValue(this.parentIdAttributeName);

        // Insert a line with parent group actions
        if (parentId == null || "0".equals(parentId) || parentId.length() == 0) {
            if (this.lastParentItemAdded != null && this.parentsGroupActions != null) {
                this.table.addLine(this.lastParentItemAdded.getId().toString(), "child action");
                this.addItemActions(this.lastParentItemAdded, this.parentsGroupActions.getActionsFor(this.lastParentItemAdded));
            }
            this.lastParentItemAdded = item;
        }

    }

    @Override
    protected final void preProcessHtml() {

        addActions(new ItemTableActionSet() {

            @Override
            protected void defineActions(final IItem item) {
                try {
                    this.addAction(new JsId("moveup"), new Url("images/up.png"), _("Move this element before the previous one"), new Action() {

                        @Override
                        public void execute() {
                            final int index = Integer.valueOf(item.getAttributeValue(ItemTreeTable.this.indexAttributeName));
                            item.setAttribute(ItemTreeTable.this.indexAttributeName, String.valueOf(index - 3));
                            ItemTreeTable.this.itemDefinition.getAPICaller().update(item, new APICallback() {

                                @Override
                                public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                                    ItemTreeTable.this.refresh();
                                }

                            });
                        }
                    });

                    this.addAction(new JsId("movedown"), new Url("images/down.png"), _("Move this element after the next one"), new Action() {

                        @Override
                        public void execute() {
                            final int index = Integer.valueOf(item.getAttributeValue(ItemTreeTable.this.indexAttributeName));
                            item.setAttribute(ItemTreeTable.this.indexAttributeName, String.valueOf(index + 3));
                            ItemTreeTable.this.itemDefinition.getAPICaller().update(item, new APICallback() {

                                @Override
                                public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                                    ItemTreeTable.this.refresh();
                                }

                            });
                        }
                    });
                } catch (final NumberFormatException e) {
                    // Do nothing
                }

            }
        });

        super.preProcessHtml();
    }

}
