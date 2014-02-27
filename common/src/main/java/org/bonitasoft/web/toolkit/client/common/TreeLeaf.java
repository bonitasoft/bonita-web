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

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.url.UrlSerializer;

/**
 * 
 * @author Séverin Moussel
 * 
 * @param <VALUE_CLASS>
 */
public class TreeLeaf<VALUE_CLASS> extends AbstractTreeNode<VALUE_CLASS> {

    protected VALUE_CLASS value = null;

    public TreeLeaf(final VALUE_CLASS value) {
        super();
        this.value = value;
    }

    public TreeLeaf(final AbstractTreeNode<VALUE_CLASS> parent, final VALUE_CLASS value) {
        super(parent);
        this.value = value;
    }

    public VALUE_CLASS getValue() {
        return this.value;
    }

    public void setValue(final VALUE_CLASS value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value == null ? null : this.value.toString();
    }

    @Override
    public String toJson() {
        return JSonSerializer.serialize(this.value);
    }

    @Override
    public TreeLeaf<VALUE_CLASS> copy() {
        return new TreeLeaf<VALUE_CLASS>(this.value);
    }

    @Override
    public String toUrl(final String key) {
        return UrlSerializer.serialize(key, this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof TreeLeaf)) {
            return false;
        }

        return this.getValue().equals(((TreeLeaf<?>) o).getValue());
    }

}
