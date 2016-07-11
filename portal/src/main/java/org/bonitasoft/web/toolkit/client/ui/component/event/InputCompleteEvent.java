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
package org.bonitasoft.web.toolkit.client.ui.component.event;

import org.bonitasoft.web.toolkit.client.eventbus.SubjectEvent;

public class InputCompleteEvent extends SubjectEvent<InputCompleteHandler> {

    public static final Type<InputCompleteHandler> TYPE = new Type<InputCompleteHandler>();

    private final String value;

    public InputCompleteEvent(String value) {
        this.value = value;
    }

    @Override
    public Type<InputCompleteHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(InputCompleteHandler handler) {
        handler.onComplete(this);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Input complete event";
    }

}
