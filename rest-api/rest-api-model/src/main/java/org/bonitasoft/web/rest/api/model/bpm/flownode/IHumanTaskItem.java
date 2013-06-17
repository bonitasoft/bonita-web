/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.api.model.bpm.flownode;

import java.util.Date;

import org.bonitasoft.web.rest.api.model.bpm.process.ActorItem;
import org.bonitasoft.web.rest.api.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public interface IHumanTaskItem extends ITaskItem {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES NAMES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_ASSIGNED_USER_ID = "assigned_id";

    public static final String ATTRIBUTE_ASSIGNED_DATE = "assigned_date";

    public static final String ATTRIBUTE_PRIORITY = "priority";

    public static final String ATTRIBUTE_DUE_DATE = "dueDate";

    public static final String ATTRIBUTE_ACTOR_ID = "actorId";
    
    /*
     * Same as ATTRIBUTE_PARENT_CONTAINER_ID
     * Should be in manual task but lives there because of deploy restrictions
     */
    public static final String ATTRIBUTE_PARENT_TASK_ID = "parentTaskId";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES VALUES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String VALUE_PRIORITY_HIGHEST = "highest";

    public static final String VALUE_PRIORITY_ABOVE_NORMAL = "above_normal";

    public static final String VALUE_PRIORITY_NORMAL = "normal";

    public static final String VALUE_PRIORITY_UNDER_NORMAL = "under_normal";

    public static final String VALUE_PRIORITY_LOWEST = "lowest";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String COUNT_ATTACHMENT_NUMBER = "nb_of_attachment";

    public static final String COUNT_ACTOR_USER_NUMBER = "nb_of_actor_user";

    public static final String COUNT_COMMENT_NUMBER = "nb_of_comment";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String FILTER_TEAM_MANAGER_ID = "team_manager_id";

    public static final String FILTER_USER_ID = "user_id";

    public static final String FILTER_HIDDEN_TO_USER_ID = "hidden_user_id";

    public static final String FILTER_IS_ASSIGNED = "is_claimed";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // SETTERS

    public void setAssignedId(final String id);

    public void setAssignedId(final APIID id);

    public void setAssignedId(final Long id);

    public void setAssignedDate(final String date);

    public void setAssignedDate(final Date date);

    public void setPriority(final String priority);

    public void setDueDate(final String date);

    public void setDueDate(final Date date);

    public void setActorId(final APIID id);

    public void setActorId(final String actorId);

    public void setActorId(final Long actorId);

    // Counters
    public void setNbOfAttachment(final String count);

    public void setNbOfAttachment(final int count);

    public void setNbOfComment(final String count);

    public void setNbOfComment(final int count);

    public void setNbOfActorUser(final String count);

    public void setNbOfActorUser(final int count);

    // GETTERS

    public APIID getActorId();

    public String getPriority();

    public String getDueDate();

    public APIID getAssignedId();

    public String getAssignedDate();

    // Counters
    public Integer getNbOfAttachment();

    public Integer getNbOfComment();

    public Integer getNbOfActorUser();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public UserItem getAssignedUser();

    public ActorItem getActor();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isAssigned();

    public boolean isUnassigned();

}
