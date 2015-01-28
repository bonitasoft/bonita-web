package org.bonitasoft.livingapps.menu;

import org.bonitasoft.engine.business.application.ApplicationMenu;


public class MenuLink implements Menu {

    private final ApplicationMenu menu;
    private final String pageToken;

    public MenuLink(final ApplicationMenu menu, final String pageToken) {
        this.menu = menu;
        this.pageToken = pageToken;
    }

    @Override
    public String getHtml() {
        return "<li><a href=\"" + pageToken + "\">" + menu.getDisplayName() + "</a></li>";
    }
}
