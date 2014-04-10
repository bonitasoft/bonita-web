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
package org.bonitasoft.web.toolkit.client.common.util;

import java.util.Iterator;

/**
 * @author Julien Mege
 * 
 */
public abstract class StringUtil {

    /**
     * Convert a String to a boolean using the smart way<br />
     * <ul>
     * <li>if the String is NULL, the Boolean will be NULL.</li>
     * <li>if the String can be cast to a long, the boolean will be TRUE if the long value is > 0, otherwise FALSE.</li>
     * <li>if the String is equal to "true", "yes" or "ok" the boolean will be TRUE.</li>
     * <li>if the String is equal to "false", "no" or "ko" the boolean will be FALSE.</li>
     * <li>all other cases will throw an IllegalArgumentException</li>
     * 
     * @param value
     *            The value to convert
     * 
     * @return This method will return the Boolean value of the value passed.
     * 
     * @throw IllegalArgumentException
     */
    public static Boolean toBoolean(final String value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        }

        // FIXME Manage integer values (<=0 false, >=1 true)

        if ("true".equals(value) || "yes".equals(value) || "ok".equals(value)) {
            return true;
        } else if ("false".equals(value) || "no".equals(value) || "ko".equals(value)) {
            return false;
        }

        try {
            return Integer.valueOf(value) > 0;
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(value + " is not a valid boolean value");
        }
    }

    /**
     * Converts a String to a long plus if the String is NULL, the returned value will be NULL.
     * 
     * @param value
     * @return
     * @throws NumberFormatException
     */
    public static Long toLong(final String value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }

    /**
     * Converts a String to an integer plus if the String is NULL, the returned value will be NULL.
     * 
     * @param value
     * @return
     * @throws NumberFormatException
     */
    public static Integer toInteger(final String value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        return Integer.valueOf(value);
    }

    /**
     * Converts a String to a float plus if the String is NULL, the returned value will be NULL.
     * 
     * @param value
     * @return
     * @throws NumberFormatException
     */
    public static Float toFloat(final String value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        return Float.valueOf(value);
    }

    /**
     * Converts a String to a double plus if the String is NULL, the returned value will be NULL.
     * 
     * @param value
     * @return
     * @throws NumberFormatException
     */
    public static Double toDouble(final String value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        return Double.valueOf(value);
    }

    /**
     * Check if a value is blank (NULL or empty String).
     * 
     * @param value
     *            The value to check
     * @return This method returns TRUE if the value is null OR an empty String, otherwise FALSE.
     */
    public static boolean isBlank(final String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

    /**
     * Ensure value by returning default if that value {@link StringUtil#isBlank(String)}
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String ensure(String value, String defaultValue) {
        if (StringUtil.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * FROM COMMONS-LANG3 3.1 
     * 
     * <p>Joins the elements of the provided {@code Iterable} into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").</p>
     *
     * <p>See the examples here: {@link #join(Object[],String)}. </p>
     *
     * @param iterable  the {@code Iterable} providing the values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    /**
     * FROM COMMONS-LANG3 3.1
     * 
     * <p>Joins the elements of the provided {@code Iterator} into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").</p>
     *
     * <p>See the examples here: {@link #join(Object[],String)}. </p>
     *
     * @param iterator  the {@code Iterator} of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     */
    public static String join(Iterator<?> iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? "" : first.toString();
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }
}
