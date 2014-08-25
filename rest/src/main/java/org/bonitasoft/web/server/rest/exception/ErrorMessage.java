/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.server.rest.exception;

import javax.ws.rs.core.Response.Status;

/**
 * Common error message
 * 
 * @author Colin Puy
 */
public class ErrorMessage {

    private int status;
    private String type;
    private String message;
    
    public ErrorMessage(Exception exception) {
        this.type = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
    }

    public String getType() {
        return type;
    }
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
    
    public ErrorMessage withStatus(Status status) {
        this.status = status.getStatusCode();
        return this;
    }
}
