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
package org.bonitasoft.web.toolkit.client.common.url;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;

/**
 * @author Séverin Moussel
 *
 */
public class UrlUnserializer extends UrlUtil {

    public static TreeIndexed<String> unserializeTreeNodeIndexed(final String url) {
        final TreeIndexed<String> tree = new TreeIndexed<String>();

        if (url != null) {
            final String query = url.replaceAll("^.*\\?|#.*$", "");

            final String[] params = query.split("&");

            for (int i = 0; i < params.length; i++) {
                if (params[i].length() == 0) {
                    continue;
                }

                final String[] keyValue = params[i].split("=");

                final String key = keyValue[0];
                String value = "";
                if (keyValue.length > 1) {
                    value = keyValue[1];
                }

                final String[] path = key.replaceAll("\\]", "").split("\\[");

                final String[] unescapedPath = new String[path.length];
                for (int j = 0; j < path.length; j++) {
                    unescapedPath[j] = unescape(path[j]);
                }

                tree.addValueByPath(unescapedPath, unescape(value));

            }
        }

        return tree;
    }
}
