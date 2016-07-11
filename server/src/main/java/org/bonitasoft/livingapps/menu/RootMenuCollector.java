package org.bonitasoft.livingapps.menu;

import org.bonitasoft.engine.business.application.ApplicationMenu;
import org.bonitasoft.livingapps.menu.MenuFactory.Collector;


class RootMenuCollector implements Collector {

    @Override
    public boolean isCollectible(final ApplicationMenu menu) {
        return menu.getParentId() == null;
    }
}
