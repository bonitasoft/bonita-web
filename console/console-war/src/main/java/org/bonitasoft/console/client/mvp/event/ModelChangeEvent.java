package org.bonitasoft.console.client.mvp.event;

import com.google.gwt.event.shared.GwtEvent;
import org.bonitasoft.web.toolkit.client.eventbus.SubjectEvent;

/**
 * @author Vincent Elcrin
 */
public class ModelChangeEvent<M> extends SubjectEvent<ModelChangeHandler<M>> {

    private Type<ModelChangeHandler<M>> type;

    private M model;

    public ModelChangeEvent(GwtEvent.Type<ModelChangeHandler<M>> type, M model) {
        this.type = type;
        this.model = model;
    }

    public M getModel() {
        return model;
    }

    @Override
    public GwtEvent.Type<ModelChangeHandler<M>> getAssociatedType() {
        return type;
    }

    @Override
    public void dispatch(ModelChangeHandler<M> handler) {
        handler.onModelChange(this);
    }

}
