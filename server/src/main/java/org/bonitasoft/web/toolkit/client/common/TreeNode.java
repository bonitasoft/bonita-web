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

/**
 * 
 * @author Séverin Moussel
 * 
 * @param <VALUE_CLASS>
 */
public class TreeNode<VALUE_CLASS> extends Tree<VALUE_CLASS> {

    public TreeNode() {
        super();
    }

    @Override
    public TreeNode<VALUE_CLASS> copy() {
        final TreeNode<VALUE_CLASS> result = new TreeNode<VALUE_CLASS>();

        for (final AbstractTreeNode<VALUE_CLASS> child : this.children) {
            result.addNode(child.copy());
        }

        return result;
    }

}
