/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.profile;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.*;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;

/**
 * @author Fabio Lombardi
 *
 */
public class PageLister {

    protected final static List<BonitaPageItem> pages = new ArrayList<BonitaPageItem>();
    static {
        pages.add(new BonitaPageItem("tasklistinguser", _("visualize & do tasks"), _("visualize & do tasks"), _("Tasks")));
        pages.add(new BonitaPageItem("tasklistingadmin", _("manage tasks"), _("manage tasks"), _("Tasks")));
        pages.add(new BonitaPageItem("tasklistingpm", _("Manage tasks of processes that an entity with this profile can supervise as PM."), _("Manage tasks of processes that an entity with this profile can supervise as PM."), _("Tasks")));
        pages.add(new BonitaPageItem("caselistinguser", _("visualize cases"), _("visualize cases"), _("Cases")));
        pages.add(new BonitaPageItem("caselistingadmin", _("manage cases"), _("manage cases"), _("Cases")));
        pages.add(new BonitaPageItem("caselistingpm", _("Manage cases of processes that an entity with this profile can supervise as PM."), _("Manage cases of processes that an entity with this profile can supervise as PM."), _("Cases")));
        pages.add(new BonitaPageItem("processlistinguser", _("visualize & start processes"), _("visualize & start processes"), _("Processes")));
        pages.add(new BonitaPageItem("processlistingadmin", _("manage processes"), _("manage processes"), _("Processes")));
        pages.add(new BonitaPageItem("processlistingpm", _("Manage processes that an entity with this profile can supervise as PM."), _("Manage processes that an entity with this profile can supervise as PM."), _("Processes")));
        pages.add(new BonitaPageItem("userlistingadmin", _("manage users"), _("manage users"), _("Users")));
        pages.add(new BonitaPageItem("grouplistingadmin", _("manage groups"), _("manage groups"), _("Groups")));
        pages.add(new BonitaPageItem("rolelistingadmin", _("manage roles"), _("manage roles"), _("Roles")));
        pages.add(new BonitaPageItem("importexportorganization", _("import/export organization"), _("import/export organization"), _("Import/Export")));
        pages.add(new BonitaPageItem("profilelisting", _("user privilege settings"),  _("user privilege settings"), _("Profiles")));
        pages.add(new BonitaPageItem("reportlistingadminext", _("Monitoring"), _("Monitoring"), _("Analytics")));
        pages.add(new BonitaPageItem("thememoredetailsadminext", _("Manage Look and Feel"), _("Manage Look and Feel"), _("Look and Feel")));
        pages.add(new BonitaPageItem("pagelisting", _("Manage custom pages"), _("Manage custom pages"), _("Custom Pages")));
        pages.add(new BonitaPageItem("applicationslistingadmin", _("Manage applications"), _("Manage applications"), _("Applications")));
        pages.add(new BonitaPageItem("monitoringadmin", _("Monitoring"), _("Monitoring"), _("Monitoring")));
        pages.add(new BonitaPageItem("monitoringpm", _("Monitoring"), _("Monitoring"), _("Monitoring")));
        pages.add(new BonitaPageItem("licensemonitoringadmin", _("License"), _("License"), _("License")));
    }

    public List<BonitaPageItem> getPages() {
        return pages;
    }

    public BonitaPageItem getPage(final String token) {
        for (final BonitaPageItem page : pages) {
            if (page.getToken().equals(token)) {
                return page;
            }
        }
        return null;
    }

    public List<BonitaPageItem> getPages(final List<String> pagesTokenToSkip) {
        final List<BonitaPageItem> pagelist = new ArrayList<BonitaPageItem>();

        for (final BonitaPageItem page : pages) {
            if (!pagesTokenToSkip.contains(page.getToken())) {
                pagelist.add(page);
            }
        }
        return pagelist;
    }
}
