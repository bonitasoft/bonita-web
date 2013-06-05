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
package org.bonitasoft.web.toolkit.client.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.URL;

/**
 * Build a URL from a set of parameters.
 * 
 * @author Séverin Moussel
 */
public class UrlBuilder {

    private final HashMap<String, Object> parameters = new HashMap<String, Object>();

    private String rootUrl = null;

    private String anchor = null;

    public UrlBuilder() {
    }

    public UrlBuilder(final String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public void setRootUrl(final String rootUrl) {
        this.rootUrl = rootUrl;
    }

    /**
     * Build the URL and return it as an encoded string.
     */
    @SuppressWarnings("unchecked")
    private void buildParameters(final StringBuilder sb, final HashMap<String, Object> parameters, final String parentName) {
        final java.util.Iterator<String> i = parameters.keySet().iterator();

        char prefix = '&';
        while (i.hasNext()) {
            String name = i.next();
            final Object value = parameters.get(name);

            if (parentName != null) {
                name = parentName + "[" + name + "]";
            }

            if (value instanceof String || value instanceof Character || value instanceof Number || value instanceof Boolean) {
                sb.append(prefix).append(name).append('=');
                sb.append(URL.encode(String.valueOf(value)));
            } else if (value instanceof Map) {
                this.buildParameters(sb, (HashMap<String, Object>) value, name);
            } else if (value instanceof List) {
                this.buildParameters(sb, (List<Object>) value, name);
            } else if (value == null) {
                sb.append(prefix).append(name).append('=');
            } else {
                throw new IllegalArgumentException();
            }
            prefix = '&';
        }
    }

    /**
     * Build the URL and return it as an encoded string.
     */
    @SuppressWarnings("unchecked")
    private void buildParameters(final StringBuilder sb, final List<Object> parameters, String parentName) {
        final Iterator<Object> i = parameters.iterator();

        parentName = parentName + "[]";

        while (i.hasNext()) {
            final Object value = i.next();

            if (value == null) {
                sb.append('&').append(parentName).append('=');
            } else if (value instanceof String) {
                sb.append('&').append(parentName).append('=').append(URL.encode((String) value));
            } else if (value instanceof Map) {
                this.buildParameters(sb, (HashMap<String, Object>) value, parentName);
            } else if (value instanceof List) {
                this.buildParameters(sb, (HashMap<String, Object>) value, parentName);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder url = new StringBuilder();

        this.buildParameters(url, this.parameters, null);

        if (this.anchor != null) {
            if (!this.anchor.startsWith("#")) {
                url.append("#");
            }
            url.append(this.anchor);
        }

        return this.rootUrl + url.toString().replaceAll("^&", "?").replaceAll("\\[\\]=", "=").replaceAll("((\\[(.*?)\\])+?)=", "=$3%3d");
    }

    /**
     * Remove a query parameter from the map.
     * 
     * @param name
     *            the parameter name
     */
    public UrlBuilder removeParameter(final String name) {
        this.parameters.remove(name);
        return this;
    }

    /**
     * Set the anchor portion of the location (ex. myAnchor or #myAnchor).
     * 
     * @param anchor
     *            the anchor
     */
    public UrlBuilder setAnchor(final String anchor) {
        this.anchor = anchor;
        return this;
    }

    public UrlBuilder addParameter(final String key, final Object... values) {
        if (values.length == 1) {
            this.parameters.put(key, values[0]);
        } else {
            this.parameters.put(key, values);
        }
        return this;
    }

    public UrlBuilder addParameter(final String key, final HashMap<String, Object> values) {
        this.parameters.put(key, values);
        return this;
    }
}
