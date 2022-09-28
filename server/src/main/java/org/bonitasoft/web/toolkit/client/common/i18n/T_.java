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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.t_;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

public class T_ {

    private final String message;
    private Arg[] args;

    public T_(String message) {
        this.message = message;
    }

    public T_(String message, Arg... args) {
        this(message);
        this.args = args;
    }

    public String localize(LOCALE locale) {
        if(args != null) {
            return t_(message, locale, args);
        }
        return AbstractI18n.t_(message, locale);
    }
}
