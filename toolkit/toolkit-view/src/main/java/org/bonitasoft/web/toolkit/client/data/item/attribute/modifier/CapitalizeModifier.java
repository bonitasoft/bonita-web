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
package org.bonitasoft.web.toolkit.client.data.item.attribute.modifier;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * @author Séverin Moussel
 * 
 */
public class CapitalizeModifier extends AbstractStringModifier {

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.toolkit.item.attribute.modifier.AbstractStringModifier#clean(java.lang.String)
     */
    @Override
    public String clean(final String value) {
        final String realValue = value.toLowerCase();
        final MatchResult matcher;
        final RegExp regExp = RegExp.compile("((^|[\\.!\\?;\\\\r\\\\n\\\\t])+[a-zA-Z])");

        final StringBuilder result = new StringBuilder();
        int lastIndex = 0;

        if ((matcher = regExp.exec(realValue)) != null) {
            result.append(value.substring(lastIndex, matcher.getIndex()));
            result.append(matcher.getGroup(0).toUpperCase());
            lastIndex = matcher.getIndex() + matcher.getGroup(0).length();
        }

        result.append(value.substring(lastIndex));

        return result.toString();
    }

}
