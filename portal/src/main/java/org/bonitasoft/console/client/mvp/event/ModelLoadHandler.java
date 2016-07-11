package org.bonitasoft.console.client.mvp.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Vincent Elcrin
 */
public interface ModelLoadHandler<M> extends EventHandler {

    public abstract void onModelLoad(ModelLoadEvent<M> event);
}
