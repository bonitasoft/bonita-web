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

import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;
import org.bonitasoft.web.toolkit.client.common.url.UrlSerializable;

/**
 * 
 * @author Séverin Moussel
 * 
 * @param <VALUE_CLASS>
 */
public abstract class AbstractTreeNode<VALUE_CLASS> implements JsonSerializable, UrlSerializable {

    protected AbstractTreeNode<VALUE_CLASS> parent = null;

    public AbstractTreeNode() {
    }

    public AbstractTreeNode(final AbstractTreeNode<VALUE_CLASS> parent) {
        this.parent = parent;
    }

    public AbstractTreeNode<VALUE_CLASS> getParent() {
        return this.parent;
    }

    public void setParent(final AbstractTreeNode<VALUE_CLASS> parent) {
        this.parent = parent;
    }

    public abstract AbstractTreeNode<VALUE_CLASS> copy();

}
