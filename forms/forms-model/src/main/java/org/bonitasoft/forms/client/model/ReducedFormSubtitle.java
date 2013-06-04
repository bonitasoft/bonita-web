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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.model;

import java.io.Serializable;

/**
 * @author qixiang.zhang
 * 
 */
public class ReducedFormSubtitle implements Serializable {

    /**
     * Possible sub title positions
     */
    public static enum SubTitlePosition {
        TOP, BOTTOM
    };

    /**
     * UID
     */
    private static final long serialVersionUID = -7264776649206015842L;

    /**
     * sub title label
     */
    private String label;

    /**
     * label position
     */
    private SubTitlePosition position;

    /**
     * Default constructor
     */
    public ReducedFormSubtitle() {
        super();
        // Mandatory for serialization.
    }

    /**
     * 
     * constructor.
     * @param position
     */
    public ReducedFormSubtitle(final SubTitlePosition position) {
        this.position = position;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the position
     */
    public SubTitlePosition getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(SubTitlePosition position) {
        this.position = position;
    }

}
