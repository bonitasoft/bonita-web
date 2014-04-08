package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Vincent Elcrin
 */
public interface ModelChangeHandler<M> extends EventHandler {

    public abstract void onModelChange(ModelChangeEvent<M> event);
}
