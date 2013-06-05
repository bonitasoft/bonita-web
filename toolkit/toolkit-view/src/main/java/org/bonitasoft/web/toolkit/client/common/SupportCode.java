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
package org.bonitasoft.web.toolkit.client.common;

/**
 * @author Séverin Moussel
 * 
 */
public class SupportCode {

    private static final String HEADER = "-----------------------------START------------------------------";

    private static final String FOOTER = "------------------------------END-------------------------------";

    public static String encode(final String text) {
        String result = new String(Base64.encode(text.getBytes()));
        result = wrapLines(result);

        return new StringBuilder()
                .append(HEADER)
                .append("\n\n")
                .append(result)
                .append("\n\n")
                .append(FOOTER)
                .toString();
    }

    /**
     * @param result
     * @return
     */
    private static String wrapLines(final String result) {
        final StringBuilder sb = new StringBuilder();
        int pos = 0;
        while (pos < result.length()) {
            final int end = Math.min(pos + 64, result.length());
            sb.append(result.substring(pos, end)).append("\n");
            pos = end;
        }

        return sb.toString().trim();
    }

    public static String decode(final String text) {
        final String result = text
                .replaceAll(HEADER, "")
                .replaceAll(FOOTER, "")
                .replaceAll("[\r\n]", "");

        return new String(Base64.decode(result));
    }
}
