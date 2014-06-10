package org.bonitasoft.console.client.mvp.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Vincent Elcrin
 */
public interface ModelChangeHandler<M> extends EventHandler {

    public abstract void onModelChange(ModelChangeEvent<M> event);
}
