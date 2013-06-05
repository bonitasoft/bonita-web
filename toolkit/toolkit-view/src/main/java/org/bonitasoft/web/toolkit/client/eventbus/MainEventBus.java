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
package org.bonitasoft.web.toolkit.client.eventbus;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * Application's only event bus
 * 
 * @author Vincent Elcrin
 * 
 */
public class MainEventBus extends EventBus {

    private final EventBus impl;

    private static final MainEventBus INSTANCE = new MainEventBus();

    /**
     * Hold local list of named handlers. Extends the idea of source but observer side. Unique handler per name.
     */
    private final Map<String, HandlerRegistration> namedHandlers = new HashMap<String, HandlerRegistration>();

    /**
     * Default Constructor.
     */
    private MainEventBus() {
        impl = new SimpleEventBus();
    }

    public static MainEventBus getInstance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.shared.EventBus#addHandler(com.google.gwt.event.shared.GwtEvent.Type, com.google.gwt.event.shared.EventHandler)
     */
    @Override
    public <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
        return impl.addHandler(type, handler);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.shared.EventBus#addHandlerToSource(com.google.gwt.event.shared.GwtEvent.Type, java.lang.Object,
     * com.google.gwt.event.shared.EventHandler)
     */
    @Override
    public <H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler) {
        return impl.addHandlerToSource(type, source, handler);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.shared.EventBus#fireEvent(com.google.gwt.event.shared.GwtEvent)
     */
    @Override
    public void fireEvent(GwtEvent<?> event) {
        impl.fireEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.shared.EventBus#fireEventFromSource(com.google.gwt.event.shared.GwtEvent, java.lang.Object)
     */
    @Override
    public void fireEventFromSource(GwtEvent<?> event, Object source) {
        impl.fireEventFromSource(event, source);
    }

    // ////////////////////////////////////////////////////////////////////////////////////
    // / Named handlers
    // ////////////////////////////////////////////////////////////////////////////////////

    public <H extends EventHandler> HandlerRegistration addNamedHandler(Type<H> type, String name, H handler) {
        unregisterNamedHandler(name);
        return saveNamedHandlerRegistration(name, addHandler(type, handler));
    }

    public <H extends EventHandler> HandlerRegistration addNamedHandlerToSource(Type<H> type, String name, Object source, H handler) {
        unregisterNamedHandler(name);
        return saveNamedHandlerRegistration(name, addHandlerToSource(type, source, handler));
    }

    public void unregisterNamedHandler(String name) {
        if (namedHandlers.containsKey(name)) {
            namedHandlers.remove(name).removeHandler();
        }
    }

    private HandlerRegistration saveNamedHandlerRegistration(String name, HandlerRegistration registration) {
        namedHandlers.put(name, registration);
        return registration;
    }

}
