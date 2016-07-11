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
package org.bonitasoft.web.toolkit.client.data.item.attribute.validator;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Paul AMAR
 * 
 */
public class StringFormatURLValidator extends AbstractStringFormatValidator {

    public StringFormatURLValidator() {
        super("(((?:http|https|ftp|mailto):\\/\\/[a-zA-Z0-9\\/\\?=#&%~\\-]+(.[a-zA-Z0-9\\/\\?=#&%~\\-]+)+)|(www(.[a-zA-Z0-9\\/\\?=_#&%~\\-\\)\\(]+){2,}))");
    }

    @Override
    protected String defineErrorMessage() {
        return _("%attribute% is not a valid URL");
    }

}
