/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.model;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Anthony Birembaut
 *
 */
public enum ActivityAttribute {
    
    state,
    assignee,
    actor,
    lastUpdate,
    name,
    description,
    createdDate,
    reachedStateDate,
    executedBy,
    expectedEndDate,
    priority,
    type,
    remainingTime;
    
    private static String PREFIX = "bonita_step_";
    
    public static ActivityAttribute valueOfAttibute(final String strValue) {
        if (strValue != null && strValue.length() > PREFIX.length()) {
            return valueOf(strValue.substring(PREFIX.length()));
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public static Collection<String> attributeValues() {
        final Collection<String> attributes = new HashSet<String>();
        for (final ActivityAttribute activityAttribute : ActivityAttribute.values()) {
            attributes.add(PREFIX + activityAttribute.name());
        }
        return attributes;
    }
}
