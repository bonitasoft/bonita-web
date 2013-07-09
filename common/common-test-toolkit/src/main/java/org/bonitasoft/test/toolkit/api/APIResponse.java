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
package org.bonitasoft.test.toolkit.api;

import org.jboss.resteasy.util.HttpResponseCodes;

/**
 * API response.
 * 
 * @author Truc Nguyen
 */
public class APIResponse {

    private final String entity;

    private final int status;

    /**
     * Default Constructor.
     */
    public APIResponse() {
        this("", HttpResponseCodes.SC_OK);
    }

    /**
     * Default Constructor.
     * 
     * @param pEntity
     * @param pStatus
     */
    public APIResponse(final String pEntity, final int pStatus) {
        this.entity = pEntity;
        this.status = pStatus;
    }

    /**
     * @return the entity
     */
    public final String getEntity() {
        return this.entity;
    }

    /**
     * @return the status
     */
    public final int getStatus() {
        return this.status;
    }

}
