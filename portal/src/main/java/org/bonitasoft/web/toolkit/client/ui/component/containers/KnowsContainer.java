package org.bonitasoft.web.toolkit.client.ui.component.containers;

import org.bonitasoft.web.toolkit.client.ui.component.core.Node;

public interface KnowsContainer {

    public void setContainer(Container<? extends Node> container);

    public Container<? extends Node> getContainer();
}
