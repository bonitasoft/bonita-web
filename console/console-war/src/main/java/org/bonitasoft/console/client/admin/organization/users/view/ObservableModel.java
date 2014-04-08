package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.event.shared.GwtEvent;
import org.bonitasoft.web.toolkit.client.eventbus.MainEventBus;

/**
 * @author Vincent Elcrin
 */
public abstract class ObservableModel<M> {

    private GwtEvent.Type<ModelChangeHandler<M>> type = new GwtEvent.Type<ModelChangeHandler<M>>();

    public void observe(ModelChangeHandler<M> handler) {
        MainEventBus.getInstance().addHandler(type, handler);
    }

    public void notifyChange(M model) {
        MainEventBus.getInstance().fireEvent(
                new ModelChangeEvent<M>(type, model));
    }
}
