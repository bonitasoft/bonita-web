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
package org.bonitasoft.web.rest.server.framework.exception;

/**
 * @author Séverin Moussel
 * 
 */
public class APIFileUploadNotFoundException extends APIAttributeException {

    private static final long serialVersionUID = -5738651518696086103L;

    private final String filePath;

    public APIFileUploadNotFoundException(final String attributeName, final String filePath) {
        super(attributeName);
        this.filePath = filePath;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    protected String defaultMessage() {
        return "Uploaded file " + getAttributeName() + "(" + getFilePath() + ") not found for API \"" + getApi() + "#" + getResource() + "\"";
    }

    @Override
    protected void toJsonAdditionnalAttributes(final StringBuilder json) {
        super.toJsonAdditionnalAttributes(json);

        addJsonAdditionalAttribute("filepath", getFilePath(), json);
    }

}
