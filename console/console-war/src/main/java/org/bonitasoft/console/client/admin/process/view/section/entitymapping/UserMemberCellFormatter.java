/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.console.client.admin.process.view.section.entitymapping;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;

/**
 * Add title and class if user is deactivated
 *  
 * @author Colin PUY
 */
public class UserMemberCellFormatter extends ItemTableCellFormatter {

    @Override
    public void execute() {
        this.table.addCell(_(getText()));
        if (isUserDeactivated()) {
            this.table.getLastCell().addClass("deactivated");
            this.table.getLastCell().setTooltip(_("Deactivated user"));
        }
    }

    private boolean isUserDeactivated() {
        UserItem user = getUser();
        return user != null && !user.isEnabled();
    }
    
    private UserItem getUser() {
        return (UserItem) ((ActorMemberItem) item).getDeploy(ActorMemberItem.ATTRIBUTE_USER_ID);
    }
    
    private String getText() {
        return this.attributeReader.read(this.item);
    }
}
