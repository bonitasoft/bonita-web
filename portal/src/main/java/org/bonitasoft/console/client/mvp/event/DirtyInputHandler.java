package org.bonitasoft.console.client.mvp.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Vincent Elcrin
 */
public interface DirtyInputHandler<T> extends EventHandler {

    public void onDirtyInput(DirtyInputEvent<T> event);
}
