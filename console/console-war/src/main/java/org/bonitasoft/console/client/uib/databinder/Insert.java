package org.bonitasoft.console.client.uib.databinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

interface Insert {

    /**
     * Insert child into parent.
     *
     * @param parent
     * @param child
     * @return inserted node
     */
    Node into(Element parent, Node child);
}
