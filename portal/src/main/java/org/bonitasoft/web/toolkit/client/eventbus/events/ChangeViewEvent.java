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
package org.bonitasoft.web.toolkit.client.eventbus.events;

import org.bonitasoft.web.toolkit.client.eventbus.SubjectEvent;
import org.bonitasoft.web.toolkit.client.ui.RawView;

/**
 * @author Vincent Elcrin
 * 
 */
public class ChangeViewEvent extends SubjectEvent<ChangeViewHandler> {

    public static final Type<ChangeViewHandler> TYPE = new Type<ChangeViewHandler>();

    private final RawView view;

    /**
     * Default Constructor.
     */
    public ChangeViewEvent(RawView view) {
        this.view = view;
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public Type<ChangeViewHandler> getAssociatedType() {
        return TYPE;
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    public void dispatch(ChangeViewHandler handler) {
        handler.onViewChange(this);
    }

    /**
     * @return the view
     */
    public RawView getView() {
        return view;
    }

}
