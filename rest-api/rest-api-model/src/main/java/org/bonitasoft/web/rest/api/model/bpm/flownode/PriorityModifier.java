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
package org.bonitasoft.web.rest.api.model.bpm.flownode;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.AbstractStringModifier;

/**
 * 
 * 
 * @author Julien Mege
 * 
 */
public class PriorityModifier extends AbstractStringModifier {

    public PriorityModifier() {
        super();
    }

    @Override
    public String clean(final String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        } else if (IHumanTaskItem.VALUE_PRIORITY_HIGHEST.equals(value)) {
            return _("Highest");
        } else if (IHumanTaskItem.VALUE_PRIORITY_ABOVE_NORMAL.equals(value)) {
            return _("High");
        } else if (IHumanTaskItem.VALUE_PRIORITY_NORMAL.equals(value)) {
            return _("Normal");
        } else if (IHumanTaskItem.VALUE_PRIORITY_UNDER_NORMAL.equals(value)) {
            return _("Low");
        } else if (IHumanTaskItem.VALUE_PRIORITY_LOWEST.equals(value)) {
            return _("Lowest");
        } else {
            return value;
        }
    }
}
