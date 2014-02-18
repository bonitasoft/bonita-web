/**
 * Copyright (C) 2012 BonitaSoft S.A.
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

import java.util.Date;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

public class Formatter {

    public static String formatPriority(String value) {
        return new PriorityFormatter().format(value);
    }

    public static String formatUser(final UserItem user) {
        if(user == null) {
            return _("Unassigned");
        }
        return user.getFirstName() + " " + user.getLastName();
    }

    public static String formatDate(final String date, final DateFormat.FORMAT format) {
        if(StringUtil.isBlank(date)) {
            return _("No data");
        }
        return DateFormat.formatToFormat(date, DateFormat.FORMAT.SQL, format);
    }

    public static String formatDate(final Date date, final DateFormat.FORMAT format) {
        if(date == null) {
            return _("No data");
        }
        return DateFormat.dateToFormat(date, format);
    }
}
