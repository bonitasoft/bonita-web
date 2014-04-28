/*
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

package org.bonitasoft.web.toolkit.client.ui.component.event;

import org.bonitasoft.web.toolkit.client.eventbus.SubjectEvent;

/**
 * Created by Vincent Elcrin
 * Date: 02/10/13
 * Time: 11:41
 */
public class ActionEvent extends SubjectEvent<ActionHandler> {

    public static final Type<ActionHandler> TYPE = new Type<ActionHandler>();

    @Override
    public Type<ActionHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ActionHandler action) {
        action.execute(this);
    }

}
