/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.url.UrlSerializer;

/**
 * 
 * @author Séverin Moussel
 * 
 * @param <VALUE_CLASS>
 */
public class Tree<VALUE_CLASS> extends AbstractTreeNode<VALUE_CLASS> implements Iterable<AbstractTreeNode<VALUE_CLASS>> {

    protected List<AbstractTreeNode<VALUE_CLASS>> children = new ArrayList<AbstractTreeNode<VALUE_CLASS>>();;

    public Tree() {
    }

    public Tree(final Tree<VALUE_CLASS> parent) {
        super(parent);
    }

    public AbstractTreeNode<VALUE_CLASS> addNode(final AbstractTreeNode<VALUE_CLASS> node) {
        if (node == null) {
            return null;
        }

        node.setParent(this);

        this.children.add(node);
        return node;
    }

    public Tree<VALUE_CLASS> addValue(final VALUE_CLASS value) {
        if (value != null && !this.contains(value)) {
            this.addNode(new TreeLeaf<VALUE_CLASS>(this, value));
        }
        return this;
    }

    public Tree<VALUE_CLASS> addValues(final List<VALUE_CLASS> values) {
        if (values != null) {
            for (final VALUE_CLASS value : values) {
                this.addValue(value);
            }
        }

        return this;
    }

    public List<VALUE_CLASS> getValues() {
        final List<VALUE_CLASS> values = new ArrayList<VALUE_CLASS>();
        for (final AbstractTreeNode<VALUE_CLASS> node : this.children) {
            if (node instanceof TreeLeaf) {
                values.add(((TreeLeaf<VALUE_CLASS>) node).getValue());
            }
        }

        return values;
    }

    public void clear() {
        this.children.clear();
    }

    public AbstractTreeNode<VALUE_CLASS> get(final int index) {
        return this.children.get(index);
    }

    @Override
    public String toString() {
        return this.children.toString();
    }

    public int size() {
        return this.children.size();
    }

    public boolean contains(final VALUE_CLASS value) {
        return this.getValues().contains(value);
    }

    @Override
    public Iterator<AbstractTreeNode<VALUE_CLASS>> iterator() {
        return this.children.iterator();
    }

    @Override
    public String toJson() {
        return JSonSerializer.serializeCollection(this.children);
    }

    @Override
    public Tree<VALUE_CLASS> copy() {
        final Tree<VALUE_CLASS> result = new Tree<VALUE_CLASS>();

        for (final AbstractTreeNode<VALUE_CLASS> child : this.children) {
            result.addNode(child.copy());
        }

        return result;
    }

    @Override
    public String toUrl(final String key) {
        return UrlSerializer.serialize(key, this.children);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof Tree)) {
            return false;
        }

        final Tree<?> tree = (Tree<?>) o;

        if (tree.size() != this.size()) {
            return false;
        }

        for (final VALUE_CLASS valueThis : this.getValues()) {
            for (final Object valueOther : tree.getValues()) {
                if (!valueThis.equals(valueOther)) {
                    return false;
                }
            }
        }

        for (final Object valueOther : tree.getValues()) {
            for (final VALUE_CLASS valueThis : this.getValues()) {
                if (!valueThis.equals(valueOther)) {
                    return false;
                }

            }
        }

        return true;
    }

    public List<AbstractTreeNode<VALUE_CLASS>> getNodes() {
        return this.children;
    }
}
