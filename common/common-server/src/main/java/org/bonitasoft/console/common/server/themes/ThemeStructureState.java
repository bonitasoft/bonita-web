/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.themes;

import java.io.Serializable;

/**
 * @author Cuisha Gai
 * 
 */
public class ThemeStructureState implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5719661467542860822L;

    boolean missBonitaConsoleHTML = true;

    boolean missBonitaFormHTML = true;

    boolean missMainLessFile = true;

    /**
     * @return the missBonitaConsoleHTML
     */
    public boolean isMissBonitaConsoleHTML() {
        return missBonitaConsoleHTML;
    }

    /**
     * @param missBonitaConsoleHTML
     *            the missBonitaConsoleHTML to set
     */
    public void setMissBonitaConsoleHTML(final boolean missBonitaConsoleHTML) {
        this.missBonitaConsoleHTML = missBonitaConsoleHTML;
    }

    /**
     * @return the missBonitaFormHTML
     */
    public boolean isMissBonitaFormHTML() {
        return missBonitaFormHTML;
    }

    /**
     * @param missBonitaFormHTML
     *            the missBonitaFormHTML to set
     */
    public void setMissBonitaFormHTML(final boolean missBonitaFormHTML) {
        this.missBonitaFormHTML = missBonitaFormHTML;
    }

    /**
     * @return the missThemeDescriptor
     */
    public boolean isMissMainLessFile() {
        return missMainLessFile;
    }

    /**
     * @param missThemeDescriptor
     *            the missThemeDescriptor to set
     */
    public void setMissMainLessFile(final boolean missMainLessFile) {
        this.missMainLessFile = missMainLessFile;
    }

    public boolean hasError() {
        return missBonitaConsoleHTML || missMainLessFile;
    }

}
