package org.bonitasoft.livingapps.menu;

import org.bonitasoft.engine.business.application.ApplicationMenu;
import org.bonitasoft.livingapps.menu.MenuFactory.Collector;


class ChildrenMenuCollector implements Collector {

    private final Long parentId;

    public ChildrenMenuCollector(final Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean isCollectible(final ApplicationMenu menu) {
        return parentId.equals(menu.getParentId());
    }
}
