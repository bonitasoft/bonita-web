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
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Zhiheng Yang
 * 
 */
public class InitialAttachment implements Serializable {

    private static final long serialVersionUID = 6091690117619511317L;

    private String name;

    private String label;

    private String description;

    private byte[] content;

    private String fileName;

    private Map<String, String> metaData;

    public InitialAttachment() {
        super();
    }

    /**
     * Default Constructor.
     * 
     * @param name
     * @param label
     * @param description
     * @param content
     * @param fileName
     * @param metaData
     */
    public InitialAttachment(final String name, final String label, final String description, final byte[] content, final String fileName,
            final Map<String, String> metaData) {
        this.name = name;
        this.label = label;
        this.description = description;
        this.content = content;
        this.fileName = fileName;
        this.metaData = metaData;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(final Map<String, String> metaData) {
        this.metaData = metaData;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
