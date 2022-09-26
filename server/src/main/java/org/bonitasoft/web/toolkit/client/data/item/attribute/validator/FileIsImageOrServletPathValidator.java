/**
 * Copyright (C) 2021 BonitaSoft S.A.
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

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;

/**
 *
 * This validates that the attribute has either an image file extension or starts with a servlet path
 *
 * @author Dumitru Corini
 * @author Anthony Birembaut
 */
public class FileIsImageOrServletPathValidator extends AbstractStringFormatValidator {

    private static final String[] IMAGE_EXTENSIONS = {"png", "jpg", "jpeg", "bmp", "wbmp", "tga", "gif", "PNG", "JPG", "JPEG", "BMP", "WBMP", "TGA", "GIF"};

    public FileIsImageOrServletPathValidator(final String servletPath) {
        super(makeRegexp(IMAGE_EXTENSIONS, servletPath));
    }

    private static String makeRegexp(final String[] extensions, final String servletPath) {
        final StringBuilder sb = new StringBuilder();
        for (final String extension : extensions) {
            sb.append(extension).append("|");
        }

        String preparedServletPath = servletPath.replace(".", "\\.").replace("/", "\\/");

        return "^" + preparedServletPath + "|\\.(" + sb.substring(0, sb.length() - 1) + ")$";
    }

    @Override
    protected String defineErrorMessage() {
        return AbstractI18n.t_("%attribute% file format not allowed or not starting with correct servlet path");
    }

}
