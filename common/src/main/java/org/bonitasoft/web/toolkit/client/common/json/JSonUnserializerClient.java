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
package org.bonitasoft.web.toolkit.client.common.json;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Séverin Moussel
 * 
 */
public class JSonUnserializerClient extends JSonUtil implements JSonUnserializer {

    private static JSonUnserializer INSTANCE = null;

    public JSonUnserializerClient() {
        INSTANCE = this;
    }

    public static AbstractTreeNode<String> unserializeTree(final String json) {
        return INSTANCE._unserializeTree(json);
    }

    @Override
    public AbstractTreeNode<String> _unserializeTree(final String json) {
        return unserializeTreeNode(JSONParser.parseStrict(json));
    }

    private TreeIndexed<String> unserializeTreeNode(final JSONObject object) {
        final TreeIndexed<String> result = new TreeIndexed<String>();
        for (final String key : object.keySet()) {
            result.addNode(key, unserializeTreeNode(object.get(key)));
        }
        return result;
    }

    private Tree<String> unserializeTreeNode(final JSONArray array) {
        final Tree<String> result = new Tree<String>();
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            result.addNode(unserializeTreeNode(array.get(i)));
        }
        return result;
    }

    private AbstractTreeNode<String> unserializeTreeNode(final JSONValue value) {
        if (value.isArray() != null) {
            return unserializeTreeNode(value.isArray());
        }
        else if (value.isObject() != null) {
            return unserializeTreeNode(value.isObject());
        }
        else if (value.isBoolean() != null) {
            return new TreeLeaf<String>(value.isBoolean().booleanValue() ? "1" : "0");
        }
        else if (value.isNumber() != null) {
            return new TreeLeaf<String>(value.isNumber().toString());
        }
        else if (value.isString() != null) {
            return new TreeLeaf<String>(value.isString().stringValue());
        }
        return null;
    }
}
