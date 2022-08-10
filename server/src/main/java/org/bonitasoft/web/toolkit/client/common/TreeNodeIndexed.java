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

import java.util.Map;

/**
 * 
 * @author Séverin Moussel
 * 
 * @param <VALUE_CLASS>
 */
public class TreeNodeIndexed<VALUE_CLASS> extends TreeIndexed<VALUE_CLASS> {

    public TreeNodeIndexed() {
        super();
    }

    public TreeNodeIndexed(final Map<String, VALUE_CLASS> map) {
        super(null, map);
    }

    @Override
    public TreeNodeIndexed<VALUE_CLASS> copy() {
        final TreeNodeIndexed<VALUE_CLASS> result = new TreeNodeIndexed<VALUE_CLASS>();

        for (final String key : this.children.keySet()) {
            result.addNode(key, this.children.get(key).copy());
        }

        return result;
    }
}
