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
package org.bonitasoft.console.client.mvp;

import com.google.gwt.event.shared.GwtEvent;
import org.bonitasoft.console.client.mvp.event.ModelChangeEvent;
import org.bonitasoft.console.client.mvp.event.ModelChangeHandler;
import org.bonitasoft.console.client.mvp.event.ModelLoadEvent;
import org.bonitasoft.console.client.mvp.event.ModelLoadHandler;
import org.bonitasoft.web.toolkit.client.eventbus.MainEventBus;

/**
 * @author Vincent Elcrin
 */
public abstract class ObservableModel<M> {

    private GwtEvent.Type<ModelChangeHandler<M>> modelChangeType = new GwtEvent.Type<ModelChangeHandler<M>>();

    private GwtEvent.Type<ModelLoadHandler<M>> modelLoadType = new GwtEvent.Type<ModelLoadHandler<M>>();

    public void observe(ModelChangeHandler<M> handler) {
        MainEventBus.getInstance().addHandler(modelChangeType, handler);
    }

    public void observe(ModelLoadHandler<M> handler) {
        MainEventBus.getInstance().addHandler(modelLoadType, handler);
    }

    public void notifyChange(M model) {
        MainEventBus.getInstance().fireEvent(
                new ModelChangeEvent<M>(modelChangeType, model));
    }

    public void notifyLoad(int page, int size, M model) {
        MainEventBus.getInstance().fireEvent(
                new ModelLoadEvent<M>(modelLoadType, page, size, model));
    }
}
