/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General protected License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General protected License for more details.
 * 
 * You should have received a copy of the GNU General protected License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;

/**
 * @author Séverin Moussel
 */
abstract class ParametersStorage {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ABSTRACT DEFINITIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Remove all the stored parameters
     */
    abstract protected void resetParameters();

    /**
     * Read all stored parameters
     * 
     * @return This method returns the currently stored parameters;
     */
    protected TreeIndexed<String> readParameters() {
        return this.readParameters(false);
    }

    /**
     * Read all stored parameters
     * 
     * @param rewrite
     *            Define if the reading must be followed by a save
     * @return This method returns the currently stored parameters;
     */
    abstract protected TreeIndexed<String> readParameters(final boolean rewrite);

    /**
     * Save the parameters to the defined storage
     * 
     * @param parameters
     */
    abstract protected void writeParameters(final TreeIndexed<String> parameters);

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COMMON METHODS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Replace all parameters by the passed ones. Using this method also remove the parameters that are not redefined.
     * 
     * @param params
     *            The new parameters to set.
     */
    protected void _setParameters(final HashMap<String, String> params) {
        this._setParameters(params == null ? null : new TreeIndexed<String>(params));
    }

    /**
     * Replace all parameters by the passed ones. Using this method also remove the parameters that are not redefined.
     * 
     * @param params
     *            The new parameters to set.
     */
    protected void _setParameters(final TreeIndexed<String> params) {
        if (params == null) {
            resetParameters();
        } else {
            writeParameters(params);
        }
    }

    /**
     * Add a parameter. If this parameter already exists, the value will be changed
     * 
     * @param name
     * @param value
     */
    protected void _addParameter(final String name, final String value) {
        writeParameters(this.readParameters(false).addValue(name, value));
    }

    /**
     * Add a parameter. If this parameter already exists, the value will be changed
     * 
     * @param name
     * @param value
     */
    protected void _addParameter(final String name, final Map<String, String> value) {
        final TreeIndexed<String> parameters = this.readParameters(false);

        parameters.addNode(name, new TreeIndexed<String>().addValues(value));

        writeParameters(parameters);
    }

    /**
     * Add a parameter. If this parameter already exists, the value will be changed
     * 
     * @param name
     * @param value
     */
    protected void _addParameter(final String name, final List<String> value) {
        final TreeIndexed<String> parameters = this.readParameters(false);

        parameters.addNode(name, new Tree<String>().addValues(value));

        writeParameters(parameters);
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @return This method returns the value of a parameter or NULL if the parameter doesn't exist or is an array.
     */
    protected String _getParameter(final String name) {
        return this._getParameter(name, null);
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @param defaultValue
     * @return This method returns the value of a parameter or {defaultValue} if the parameter doesn't exist or is an array.
     */
    protected String _getParameter(final String name, final String defaultValue) {
        final TreeIndexed<String> parameters = this.readParameters();

        if (!parameters.containsKey(name)) {
            return defaultValue;
        }

        final AbstractTreeNode<String> param = parameters.get(name);

        if (!(param instanceof TreeLeaf<?>)) {
            return defaultValue;
        }

        return param.toString();
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @return This method returns the value of a parameter or NULL if the parameter doesn't exist.
     */
    protected List<String> _getArrayParameter(final String name) {
        return this._getArrayParameter(name, new ArrayList<String>());
    }

    /**
     * Get the value of a parameter.
     * 
     * @param name
     * @param defaultValue
     * @return This method returns the value of a parameter or {defaultValue} if the parameter doesn't exist.
     */
    protected List<String> _getArrayParameter(final String name, final List<String> defaultValue) {
        final TreeIndexed<String> parameters = this.readParameters();

        if (!parameters.containsKey(name)) {
            return defaultValue;
        }

        final AbstractTreeNode<String> param = parameters.get(name);

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

    protected TreeIndexed<String> _getParameters() {
        return this.readParameters();
    }
}
