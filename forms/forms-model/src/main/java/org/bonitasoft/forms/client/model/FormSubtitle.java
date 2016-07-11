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

import org.bonitasoft.forms.client.model.ReducedFormSubtitle.SubTitlePosition;

/**
 * @author qixiang.zhang
 * 
 */
public class FormSubtitle implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -8364776649206015842L;
    
    /**
     * sub title label expression
     */
    private Expression labelExpression;
    
    /**
     * The reduced form subtitle
     */
    private ReducedFormSubtitle reducedFormSubTitle;

    /**
     * Default constructor
     * Mandatory for serialization
     */
    public FormSubtitle() {
        super();
        reducedFormSubTitle = new ReducedFormSubtitle();
    }

    /**
     * 
     * constructor.
     * @param label
     * @param position
     */
    public FormSubtitle(final SubTitlePosition position) {
        reducedFormSubTitle = new ReducedFormSubtitle(position);
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return reducedFormSubTitle.getLabel();
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        if (label != null) {
            reducedFormSubTitle.setLabel(label);
        }
    }

    /**
     * @return the position
     */
    public SubTitlePosition getPosition() {
        return reducedFormSubTitle.getPosition();
    }

    /**
     * @param position the position to set
     */
    public void setPosition(SubTitlePosition position) {
        reducedFormSubTitle.setPosition(position);
    }

    /**
     * @return the label Expression
     */
    public Expression getLabelExpression() {
        return labelExpression;
    }

    /**
     * @param labelExpression the label expression to set
     */
    public void setLabelExpression(Expression labelExpression) {
        this.labelExpression = labelExpression;
    }
    
    /**
     * @return the reducedFormSubTitle
     */
    public ReducedFormSubtitle getReducedFormSubTitle() {
        return reducedFormSubTitle;
    }

    /**
     * @param reducedFormSubTitle the reducedFormSubTitle to set
     */
    public void setReducedFormSubTitle(ReducedFormSubtitle reducedFormSubTitle) {
        this.reducedFormSubTitle = reducedFormSubTitle;
    }

}
