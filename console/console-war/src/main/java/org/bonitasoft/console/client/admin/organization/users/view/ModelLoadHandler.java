package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Vincent Elcrin
 */
public interface ModelLoadHandler<M> extends EventHandler {

    public abstract void onModelLoad(ModelLoadEvent<M> event);
}
