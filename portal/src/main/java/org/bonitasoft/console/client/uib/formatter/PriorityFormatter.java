/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.uib.formatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;

public class PriorityFormatter {

    Map<String, String> i18n = new HashMap<String, String>();

    public PriorityFormatter() {
        i18n.put(IHumanTaskItem.VALUE_PRIORITY_HIGHEST, _("Highest"));
        i18n.put(IHumanTaskItem.VALUE_PRIORITY_ABOVE_NORMAL, _("High"));
        i18n.put(IHumanTaskItem.VALUE_PRIORITY_NORMAL, _("Normal"));
        i18n.put(IHumanTaskItem.VALUE_PRIORITY_UNDER_NORMAL, _("Low"));
        i18n.put(IHumanTaskItem.VALUE_PRIORITY_LOWEST, _("Lowest"));
    }

    public String format(String value) {
        String formatted = i18n.get(value);
        if(formatted == null) {
            return value;
        }
        return formatted;
    }
}
