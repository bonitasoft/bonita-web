package org.bonitasoft.console.client.mvp.event;

import org.bonitasoft.web.toolkit.client.eventbus.SubjectEvent;

/**
 * @author Vincent Elcrin
 */
public class ModelLoadEvent<M> extends SubjectEvent<ModelLoadHandler<M>> {

    private Type<ModelLoadHandler<M>> type;

    private int page;

    private int size;

    private M model;

    public ModelLoadEvent(Type<ModelLoadHandler<M>> type, int page, int size, M model) {
        this.type = type;
        this.page = page;
        this.size = size;
        this.model = model;
    }

    public M getModel() {
        return model;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    @Override
    public Type<ModelLoadHandler<M>> getAssociatedType() {
        return type;
    }

    @Override
    public void dispatch(ModelLoadHandler<M> handler) {
        handler.onModelLoad(this);
    }

}
