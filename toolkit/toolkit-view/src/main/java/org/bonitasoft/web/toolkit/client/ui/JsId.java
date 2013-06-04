/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui;

import java.util.Random;

/**
 * This class is used to ensure the homogeneity and the validity of the id chosen by the toolkit user.<br>
 * It force the id to be lower case, and replace the spaces by underscores. <br>
 * It also provides some utilities like equals function, get prefixed id or generate random id.
 * 
 * @author Julien Mege
 */
public class JsId {

    private String jsId = "";

    /**
     * Function to test the equality between two JsId.
     * 
     * @param obj
     *            the object to compare with the current JsId
     */
    @Override
    public final boolean equals(final Object obj) {
        return !(obj instanceof JsId) && ((JsId) obj).toString() == this.toString();
    }

    @Override
    public int hashCode() {
        return this.jsId.hashCode();
    }

    public JsId(final String id) {

        this.jsId = id.replace(" ", "_"); // Lower case removed to keep the Item entry names with the good case
    }

    @Override
    public final String toString() {
        return this.jsId;
    }

    public final String toString(final String prefix) {
        return prefix + "_" + this.jsId;
    }

    /**
     * Create a random jsid
     * The generated JsId.
     */
    public final static JsId getRandom() {
        String randString = "";
        StringBuffer sb = new StringBuffer();
        String block = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFIJKLMNOPQRSTUVWXYZ";
        sb.append(block).append(block.toUpperCase()).append("0123456789");
        block = sb.toString();
        sb = new StringBuffer();
        final Random random = new Random();
        try {
            for (int i = 0; i < 10; i++) {
                sb.append(Character.toString(block.charAt(random.nextInt(block.length() - 1))));
            }
            randString = sb.toString();
        } catch (final ArrayIndexOutOfBoundsException e) {

        } catch (final NumberFormatException e) {

        } catch (final Exception e) {

        }

        return new JsId(randString);
    }

}
