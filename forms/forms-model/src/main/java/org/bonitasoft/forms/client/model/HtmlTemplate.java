/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
import java.util.List;
import java.util.Map;

/**
 * Object containing the elements required to display an application/page template
 * 
 * @author Anthony Birembaut
 */
public class HtmlTemplate implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1234560167545212457L;

    /**
     * Some templates have a dynamic message (confirmation template...)
     */
    private Expression dynamicMessageExpression;
    
    /**
     * the reduced version of HTML template
     */
    private ReducedHtmlTemplate reducedHtmlTemplate;
    
    /**
     * Default constructor
     * Mandatory for serialization
     */
    public HtmlTemplate() {
        super();
        reducedHtmlTemplate = new ReducedHtmlTemplate();
    }
    
    /**
     * Constructor
     * @param bodyContent
     * @param bodyClassNames
     * @param headContent
     */
    public HtmlTemplate(final String bodyContent, final Map<String, String> bodyAttributes, final List<HeadNode> headNodes) {
        this.reducedHtmlTemplate = new ReducedHtmlTemplate(bodyContent, bodyAttributes, headNodes);
    }

    public String getBodyContent() {
        return reducedHtmlTemplate.getBodyContent();
    }

    public void setBodyContent(final String bodyContent) {
        reducedHtmlTemplate.setBodyContent(bodyContent);
    }

    public Map<String, String> getBodyAttributes() {
        return reducedHtmlTemplate.getBodyAttributes();
    }

    public void setBodyAttributes(final Map<String, String> bodyAttributes) {
        reducedHtmlTemplate.setBodyAttributes(bodyAttributes);
    }

    public List<HeadNode> getHeadNodes() {
        return reducedHtmlTemplate.getHeadNodes();
    }

    public void setHeadNodes(final List<HeadNode> headNodes) {
        reducedHtmlTemplate.setHeadNodes(headNodes);
    }

    public Expression getDynamicMessageExpression() {
        return dynamicMessageExpression;
    }

    public void setDynamicMessageExpression(final Expression dynamicMessageExpression) {
        this.dynamicMessageExpression = dynamicMessageExpression;
    }
    
    public String getDynamicMessage() {
        return reducedHtmlTemplate.getDynamicMessage();
    }

    public void setDynamicMessage(String dynamicMessage) {
        reducedHtmlTemplate.setDynamicMessage(dynamicMessage);
    }
    
    public ReducedHtmlTemplate getReducedHtmlTemplate() {
        return reducedHtmlTemplate;
    }

    public void setReducedHtmlTemplate(ReducedHtmlTemplate reducedHtmlTemplate) {
        this.reducedHtmlTemplate = reducedHtmlTemplate;
    }
    
    public String getBodyContentId() {
        return this.reducedHtmlTemplate.getBodyContentId();
    }

    public void setBodyContentId(final String bodyContentId) {
        this.reducedHtmlTemplate.setBodyContentId(bodyContentId);
    }

}
