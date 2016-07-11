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
package org.bonitasoft.web.toolkit.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Séverin Moussel
 */
public abstract class Callable {

    private TreeIndexed<String> params = new TreeIndexed<String>();

    public void setParameters(final Map<String, String> params) {
        this.params.clear();
        if (params != null) {
            this.params.addValues(params);
        }
    }

    public void setParameters(final TreeIndexed<String> params) {
        if (params != null) {
            this.params = params.copy();
        } else {
            this.params.clear();
        }
    }

    public void setParameters(final Arg... params) {
        this.params.clear();
        for (final Arg arg : params) {
            addParameter(arg.getName(), arg.getValue());
        }
    }

    public void addParameter(final String name, final String value) {
        params.addValue(name, value);
    }

    public void addParameter(final String name, final String... values) {
        params.addValue(name, values);
    }

    public void addParameter(final String name, final Map<String, String> params) {
        ((TreeIndexed<String>) this.params.addNode(name, new TreeIndexed<String>())).addValues(params);
    }

    public void addParameter(final String name, final List<String> params) {
        ((Tree<String>) this.params.addNode(name, new Tree<String>())).addValues(params);
    }

    public boolean hasParameter(final String name) {
        return this.getParameter(name, null) != null;
    }

    public boolean hasParameter(final String name, final String value) {
        return value.equals(this.getParameter(name, null));
    }

    public String getParameter(final String name) {
        return this.getParameter(name, null);
    }

    public String getParameter(final String name, final String defaultValue) {
        if (!params.containsKey(name)) {
            return defaultValue;
        }

        final AbstractTreeNode<String> param = params.get(name);

        if (param == null) {
            return null;
        }

        if (!(param instanceof TreeLeaf<?>)) {
            return defaultValue;
        }

        return param.toString();
    }

    public List<String> getArrayParameter(final String name) {
        return this.getArrayParameter(name, new ArrayList<String>());
    }

    public List<String> getArrayParameter(final String name, final List<String> defaultValue) {
        if (!params.containsKey(name)) {
            return defaultValue;
        }

        final AbstractTreeNode<String> param = params.get(name);

        if (param instanceof TreeLeaf<?>) {
            final List<String> results = new ArrayList<String>();
            results.add(((TreeLeaf<String>) param).getValue());
            return results;
        } else if (!(param instanceof Tree<?>)) {
            return defaultValue;
        }

        final List<String> results = ((Tree<String>) param).getValues();

        if (results == null) {
            return defaultValue;
        }

        return results;
    }

    public TreeIndexed<String> getParameters() {
        return params;
    }

}
