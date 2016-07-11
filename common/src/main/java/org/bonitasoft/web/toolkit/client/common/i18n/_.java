/*
 * Copyright (C) 2013 BonitaSoft S.A.
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

package org.bonitasoft.web.toolkit.client.common.i18n;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * Created by Vincent Elcrin
 * Date: 25/09/13
 * Time: 14:09
 *
 * This class name is compatible with internationalization mechanism gettext.
 */
public class _ {

    private String message;
    private Arg[] args;

    public _(String message) {
        this.message = message;
    }

    public _(String message, Arg... args) {
        this(message);
        this.args = args;
    }

    public String localize(LOCALE locale) {
        if(args != null) {
            return _(message, locale, args);
        }
        return _(message, locale);
    }
}
