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
package org.bonitasoft.console.common.server.utils;

import java.util.List;

/**
 * @author Haojie Yuan
 * 
 */
public class ListUtil {

    public static List<?> paginate(final List<?> list, final int page, final int resultsByPage) {

        final int startIndex = page * resultsByPage;
        if (startIndex > list.size()) {
            final List<?> result = list.subList(0, 0);
            result.clear();
            return result;
        }

        final int endIndex = Math.min(startIndex + resultsByPage, list.size());

        return list.subList(startIndex, endIndex);
    }

}
