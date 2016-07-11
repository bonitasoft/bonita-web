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
import java.util.Map;

/**
 * Head node
 * 
 * @author Anthony Birembaut
 */
public class HeadNode implements Serializable {
    
    /**
     * UUID
     */
    private static final long serialVersionUID = 6377695143258421687L;

    /**
     * tagName of a head node
     */
    private String tagName;
    
    /**
     * attributes of a head node
     */
    private Map<String, String> attributes;
    
    /**
     * content of a head node
     */
    private String innerHtml;

    /**
     * Constructor
     * @param tagName
     * @param attributes
     * @param innerHtml
     */
    public HeadNode(final String tagName, final Map<String, String> attributes, final String innerHtml) {
        super();
        this.tagName = tagName;
        this.attributes = attributes;
        this.innerHtml = innerHtml;
    }
    
    /**
     * Default Constructor
     */
    public HeadNode() {
        super();
        // Mandatory for serialization
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(final String tagName) {
        this.tagName = tagName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(final Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getInnerHtml() {
        return innerHtml;
    }

    public void setInnerHtml(final String innerHtml) {
        this.innerHtml = innerHtml;
    }
    
}
