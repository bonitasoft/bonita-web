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
package org.bonitasoft.web.toolkit.client.ui.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.common.TreeNode;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
abstract public class ActionOnItemIds extends Action {

    private final List<APIID> itemIds = new ArrayList<APIID>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ActionOnItemIds() {
        super();
    }

    public ActionOnItemIds(final APIID... itemIds) {
        super();
        this.addItemIds(itemIds);
    }

    public ActionOnItemIds(final List<APIID> itemIds) {
        super();
        this.addItemIds(itemIds);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final void addItemId(final String itemId) {
        this.itemIds.add(APIID.makeAPIID(itemId));
    }

    public final void addItemId(final APIID itemId) {
        this.itemIds.add(itemId);
    }

    public final void addItemIds(final APIID... itemIds) {
        addItemIds(Arrays.asList(itemIds));
    }

    public final void addItemIds(final List<APIID> itemIds) {
        this.itemIds.addAll(itemIds);
    }

    public final void setItemId(final APIID itemId) {
        this.itemIds.clear();
        this.itemIds.add(itemId);
    }

    public final void setItemIds(final APIID... itemIds) {
        setItemIds(Arrays.asList(itemIds));
    }

    public final void setItemIds(final List<APIID> itemIds) {
        this.itemIds.clear();
        this.itemIds.addAll(itemIds);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CATCH ID SETTER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setParameters(final Map<String, String> params) {
        super.setParameters(params);

        if (params.containsKey("id")) {
            setItemId(APIID.makeAPIID(params.get("id")));
        }
    }

    @Override
    public void setParameters(final TreeIndexed<String> params) {
        super.setParameters(params);

        if (params.containsKey("id")) {
            final AbstractTreeNode<String> node = params.get("id");
            if (node instanceof TreeNode<?>) {
                final List<String> ids = ((TreeNode<String>) node).getValues();
                for (final String id : ids) {
                    addItemId(APIID.makeAPIID(id));
                }
            } else if (node instanceof TreeLeaf<?>) {
                setItemId(APIID.makeAPIID(((TreeLeaf<String>) node).getValue()));
            } else {
                throw new IllegalArgumentException("ID can't be set as an indexed tree");
            }
        }
    }

    @Override
    public void setParameters(final Arg... params) {
        super.setParameters(params);

        for (final Arg param : params) {
            if ("id".equals(param.getName())) {
                setItemId(APIID.makeAPIID(param.getValue()));
                break;
            }
        }
    }

    @Override
    public void addParameter(final String name, final String value) {
        super.addParameter(name, value);
        if ("id".equals(name)) {
            this.itemIds.clear();
            addItemId(value);
        }
    }

    @Override
    public void addParameter(final String name, final String... values) {
        super.addParameter(name, values);
        if ("id".equals(name)) {
            this.itemIds.clear();
            for (final String value : values) {
                addItemId(APIID.makeAPIID(value));
            }
        }
    }

    @Override
    public final void addParameter(final String name, final List<String> values) {
        super.addParameter(name, values);
        if ("id".equals(name)) {
            this.itemIds.clear();
            for (final String value : values) {
                addItemId(APIID.makeAPIID(value));
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EXECUTE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void execute() {
        execute(this.itemIds);
    }

    abstract protected void execute(List<APIID> itemIds);

}
