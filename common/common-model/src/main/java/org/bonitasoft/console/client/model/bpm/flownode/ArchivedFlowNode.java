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
package org.bonitasoft.console.client.model.bpm.flownode;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public interface ArchivedFlowNode  {
    
    public static final String ATTRIBUTE_SOURCE_OBJECT_ID = "sourceObjectId";
    
    public static final String FILTER_IS_TERMINAL = "isTerminal";
    
    public static final String VALUE_IS_TERMINAL_TRUE = "true";
    public static final String VALUE_IS_TERMINAL_FALSE = "false";
    
    public final static String ATTRIBUTE_ARCHIVED_DATE = "archivedDate";

    public abstract void setArchivedDate(final String date);

    public abstract void setArchivedDate(final Date date);

    public abstract Date getArchivedDate();

    public abstract boolean isArchived();
    
    public void setSourceObjectId(final APIID id);

    public void setSourceObjectId(final String id);

    public void setSourceObjectId(final Long id);

    public APIID getSourceObjectId();

}
