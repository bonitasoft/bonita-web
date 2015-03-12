package org.bonitasoft.livingapps.menu;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.business.application.ApplicationMenu;
import org.bonitasoft.engine.business.application.ApplicationPageNotFoundException;
import org.bonitasoft.engine.exception.SearchException;

public class MenuFactory {

    public interface Collector {

        public boolean isCollectible(ApplicationMenu item);
    }

    private final ApplicationAPI applicationApi;

    public MenuFactory(final ApplicationAPI applicationApi) {
        this.applicationApi = applicationApi;
    }

    public List<Menu> create(final List<ApplicationMenu> menuList) throws ApplicationPageNotFoundException, SearchException {
        return collect(menuList, new RootMenuCollector());
    }

    private Menu create(final ApplicationMenu menu, final List<ApplicationMenu> menuList) throws ApplicationPageNotFoundException, SearchException {
        if(menu.getApplicationPageId() == null) {
            return new MenuContainer(menu,
                    collect(menuList, new ChildrenMenuCollector(menu.getId())));
        }
        return new MenuLink(menu, applicationApi.getApplicationPage(menu.getApplicationPageId()).getToken());
    }

    private List<Menu> collect(final List<ApplicationMenu> items, final Collector collector)
            throws ApplicationPageNotFoundException, SearchException {
        final List<Menu> menuList = new ArrayList<Menu>();
        for (final ApplicationMenu item : items) {
            if(collector.isCollectible(item)) {
                menuList.add(create(item, items));
            }
        }
        return menuList;
    }
}
