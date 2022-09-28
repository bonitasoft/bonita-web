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
package org.bonitasoft.web.rest.server.framework.exception;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.http.JsonExceptionSerializer;
import org.bonitasoft.web.toolkit.client.ui.utils.ListUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Séverin Moussel
 * 
 */
public class APIAttributesException extends APIException {

    private static final long serialVersionUID = 7808353918962735013L;

    private final List<String> attributesNames;

    public APIAttributesException(final List<String> attributesNames) {
        super((Exception) null);
        this.attributesNames = attributesNames;
    }

    public APIAttributesException(final String... attributesNames) {
        super((Exception) null);
        this.attributesNames = Arrays.asList(attributesNames);
    }

    public APIAttributesException(final List<String> attributesNames, final String message, final Throwable cause) {
        super(message, cause);
        this.attributesNames = attributesNames;
    }

    public APIAttributesException(final List<String> attributesNames, final String message) {
        super(message);
        this.attributesNames = attributesNames;
    }

    public APIAttributesException(final List<String> attributesNames, final Throwable cause) {
        super(cause);
        this.attributesNames = attributesNames;
    }

    /**
     * @return the attributesNames
     */
    public List<String> getAttributesNames() {
        return this.attributesNames;
    }

    @Override
    protected JsonExceptionSerializer buildJson() {
        return super.buildJson()
                .appendAttribute("attributesNames", getAttributesNames());
    }

    @Override
    protected String defaultMessage() {
        return "Malformed attributes : " +
                ListUtils.join(this.attributesNames, ", ");
    }

}
