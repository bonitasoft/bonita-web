package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.event.shared.GwtEvent;
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
