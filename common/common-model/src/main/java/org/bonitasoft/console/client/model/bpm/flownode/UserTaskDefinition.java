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
package org.bonitasoft.console.client.model.bpm.flownode;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.EnumValidator;

/**
 * @author Séverin Moussel
 */
public class UserTaskDefinition extends HumanTaskDefinition {

    public static final String TOKEN = "usertask";

    private static final String API_URL = "../API/bpm/userTask";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        super.defineAttributes();
        // Restrict type to manual tasks
        getAttribute(UserTaskItem.ATTRIBUTE_TYPE)
                .removeValidator(EnumValidator.class.getName())
                .addValidator(new EnumValidator(
                        UserTaskItem.VALUE_TYPE_MANUAL_TASK
                        ));
    }

    @Override
    public IUserTaskItem _createItem() {
        return new UserTaskItem();
    }

    @Override
    public APICaller<? extends IUserTaskItem> getAPICaller() {
        return new APICaller<UserTaskItem>(this);
    }

    public static UserTaskDefinition get() {
        return (UserTaskDefinition) Definitions.get(TOKEN);
    }
}
