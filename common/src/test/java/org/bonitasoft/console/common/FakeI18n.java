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

package org.bonitasoft.console.common;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;

/**
 * Created by Vincent Elcrin
 * Date: 23/09/13
 * Time: 18:40
 */
public class FakeI18n extends AbstractI18n {

    private String l10n;

    public FakeI18n() {
        I18N_instance = this;
    }

    @Override
    public void loadLocale(LOCALE locale) {
    }

    @Override
    protected String getText(LOCALE locale, String key) {
        return l10n;
    }

    public void setL10n(String value) {
        l10n = value;
    }
}