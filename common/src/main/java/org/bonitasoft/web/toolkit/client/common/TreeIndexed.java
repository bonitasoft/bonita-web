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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.url.UrlSerializer;

/**
 * 
 * @author Séverin Moussel
 * 
 * @param <VALUE_CLASS>
 */
public class TreeIndexed<VALUE_CLASS> extends AbstractTreeNode<VALUE_CLASS> {

    protected Map<String, AbstractTreeNode<VALUE_CLASS>> children = new HashMap<String, AbstractTreeNode<VALUE_CLASS>>();

    public TreeIndexed() {
    }

    public TreeIndexed(final Map<String, VALUE_CLASS> map) {
        this(null, map);
    }

    public TreeIndexed(final AbstractTreeNode<VALUE_CLASS> parent, final Map<String, VALUE_CLASS> map) {
        this(parent);
        this.addValues(map);
    }

    public TreeIndexed(final AbstractTreeNode<VALUE_CLASS> parent) {
        super(parent);
    }

    public AbstractTreeNode<VALUE_CLASS> addNode(final String key, final AbstractTreeNode<VALUE_CLASS> node) {
        if (node == null) {
            return null;
        }

        node.setParent(this);

        this.children.put(key, node);
        return node;
    }

    public TreeIndexed<VALUE_CLASS> addValue(final String key, final VALUE_CLASS value) {
        if (value != null) {
            this.addNode(key, new TreeLeaf<VALUE_CLASS>(this, value));
        }
        return this;
    }

    public TreeIndexed<VALUE_CLASS> addValue(final String key, final VALUE_CLASS... values) {
        if (values != null) {
            if (values.length > 1) {
                final Tree<VALUE_CLASS> node = new Tree<VALUE_CLASS>();
                for (int i = 0; i < values.length; i++) {
                    node.addValue(values[i]);
                }
                this.addNode(key, node);
            } else {
                this.addNode(key, new TreeLeaf<VALUE_CLASS>(values[0]));
            }
        }
        return this;
    }

    public TreeIndexed<VALUE_CLASS> addValues(final Map<String, VALUE_CLASS> values) {
        if (values != null) {
            for (final String key : values.keySet()) {
                this.addValue(key, values.get(key));
            }
        }

        return this;
    }

    public final TreeIndexed<VALUE_CLASS> addValueByPath(final String path, final VALUE_CLASS value) {
        final String[] pathArray = path.split("\\.");
        return this.addValueByPath(pathArray, value);
    }

    public TreeIndexed<VALUE_CLASS> addValueByPath(final String[] path, final VALUE_CLASS value) {
        final LinkedList<String> pathArray = new LinkedList<String>();
        for (int i = 0; i < path.length; i++) {
            pathArray.add(path[i]);
        }
        return this.addValueByPath(pathArray, value);
    }

    public TreeIndexed<VALUE_CLASS> addValueByPath(final LinkedList<String> path, final VALUE_CLASS value) {
        final String key = path.poll();
        AbstractTreeNode<VALUE_CLASS> currentNode = this.get(key);

        if (currentNode == null) {
            if (path.size() == 0) {
                currentNode = new TreeLeaf<VALUE_CLASS>(value);
            } else {
                currentNode = new TreeIndexed<VALUE_CLASS>();
            }
            this.addNode(key, currentNode);
        } else if (currentNode instanceof TreeLeaf<?>) {
            final TreeLeaf<VALUE_CLASS> oldNode = (TreeLeaf<VALUE_CLASS>) currentNode;
            currentNode = new Tree<VALUE_CLASS>();
            ((Tree<VALUE_CLASS>) currentNode).addNode(oldNode);
            this.addNode(key, currentNode);
        }

        if (currentNode instanceof Tree<?>) {
            ((Tree<VALUE_CLASS>) currentNode).addValue(value);
        } else if (currentNode instanceof TreeIndexed<?>) {
            ((TreeIndexed<VALUE_CLASS>) currentNode).addValueByPath(path, value);
        }

        return this;
    }

    public LinkedHashMap<String, VALUE_CLASS> getValues() {
        final LinkedHashMap<String, VALUE_CLASS> values = new LinkedHashMap<String, VALUE_CLASS>();
        for (final String key : this.children.keySet()) {
            final AbstractTreeNode<VALUE_CLASS> node = this.children.get(key);
            if (node instanceof TreeLeaf) {
                values.put(key, ((TreeLeaf<VALUE_CLASS>) node).getValue());
            }
        }

        return values;
    }

    public TreeIndexed<VALUE_CLASS> clear() {
        this.children.clear();
        return this;
    }

    public boolean containsKey(final String key) {
        return this.children.containsKey(key);
    }

    public AbstractTreeNode<VALUE_CLASS> get(final String key) {
        return this.children.get(key);
    }

    public Set<String> keySet() {
        return this.children.keySet();
    }

    public VALUE_CLASS getValue(final String key) {
        final AbstractTreeNode<VALUE_CLASS> node = this.children.get(key);
        if (node != null && node instanceof TreeLeaf<?>) {
            return ((TreeLeaf<VALUE_CLASS>) node).getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return this.children.toString();
    }

    public int size() {
        return this.children.size();
    }

    @Override
    public String toJson() {
        return JSonSerializer.serializeMap(this.children);
    }

    @Override
    public TreeIndexed<VALUE_CLASS> copy() {
        final TreeIndexed<VALUE_CLASS> result = new TreeIndexed<VALUE_CLASS>();

        for (final String key : this.children.keySet()) {
            result.addNode(key, this.children.get(key).copy());
        }

        return result;
    }

    /**
     * Remove a node by its key
     * 
     * @param key
     *            The key of the node to remove
     */
    public void removeNode(final String key) {
        this.children.remove(key);
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
        if (o == null || !(o instanceof TreeIndexed)) {
            return false;
        }

        final TreeIndexed<?> tree = (TreeIndexed<?>) o;

        if (tree.size() != this.size()) {
            return false;
        }

        for (final String key : this.keySet()) {
            if (!this.get(key).equals(tree.get(key))) {
                return false;
            }
        }

        for (final String key : tree.keySet()) {
            if (!tree.get(key).equals(this.get(key))) {
                return false;
            }
        }

        return true;
    }

    public Map<String, AbstractTreeNode<VALUE_CLASS>> getNodes() {
        return this.children;
    }

}
