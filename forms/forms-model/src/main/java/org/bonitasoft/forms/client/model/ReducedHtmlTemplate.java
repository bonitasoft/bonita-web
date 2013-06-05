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
public class ReducedHtmlTemplate implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1024560167545212457L;

    /**
     * content of the body
     */
    private String bodyContent;
    
    /**
     * content id of the body
     */
    private String bodyContentId;
    
    /**
     * content of the header
     */
    private List<HeadNode> headNodes;

    /**
     * Body attributes like CSS classes...
     */
    private Map<String, String> bodyAttributes;
    
    /**
     * Some templates have a dynamic message (confirmation template...)
     */
    private String dynamicMessage;
    
    /**
     * Default constructor
     */
    public ReducedHtmlTemplate() {
        super();
        // Mandatory for serialization
    }
    
    /**
     * Constructor
     * @param bodyContent
     * @param bodyClassNames
     * @param headContent
     */
    public ReducedHtmlTemplate(final String bodyContent, final Map<String, String> bodyAttributes, final List<HeadNode> headNodes) {
        this.bodyContent = bodyContent;
        this.bodyAttributes = bodyAttributes;
        this.headNodes = headNodes;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(final String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public Map<String, String> getBodyAttributes() {
        return bodyAttributes;
    }

    public void setBodyAttributes(final Map<String, String> bodyAttributes) {
        this.bodyAttributes = bodyAttributes;
    }

    public List<HeadNode> getHeadNodes() {
        return headNodes;
    }

    public void setHeadNodes(final List<HeadNode> headNodes) {
        this.headNodes = headNodes;
    }

    public String getDynamicMessage() {
        return dynamicMessage;
    }

    public void setDynamicMessage(String dynamicMessage) {
        this.dynamicMessage = dynamicMessage;
    }
    
    public String getBodyContentId() {
        return this.bodyContentId;
    }

    public void setBodyContentId(final String bodyContentId) {
        this.bodyContentId = bodyContentId;
    }

}
