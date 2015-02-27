package org.bonitasoft.livingapps.menu;

import java.util.List;

import org.bonitasoft.engine.business.application.ApplicationMenu;


public class MenuContainer implements Menu {

    private final ApplicationMenu menu;

    private final List<Menu> children;

    public MenuContainer(final ApplicationMenu menu, final List<Menu> children) {
        this.menu = menu;
        this.children = children;
    }

    @Override
    public String getHtml() {
        final StringBuilder builder = new StringBuilder()
                .append("<li class=\"dropdown\">")
                .append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">")
                .append(menu.getDisplayName()).append(" <span class=\"caret\"></span></a>")
                .append("<ul class=\"dropdown-menu\" role=\"menu\">");
        for (final Menu child : children) {
            builder.append(child.getHtml());
        }
        return builder.append("</ul></li>").toString();
    }
}
