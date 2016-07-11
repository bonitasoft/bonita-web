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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

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
        pages.add(new BonitaPageItem("tasklistinguser", _("For user-type of profiles. To view and do tasks  if he is a candidate"), _("For user-type of profiles. To view and do tasks  if he is a candidate"), _("Tasks")));
        pages.add(new BonitaPageItem("tasklistingadmin", _("For administrator-type of profiles. To monitor task completion for all processes"), _("For administrator-type of profiles. To monitor task completion for all processes"), _("Tasks")));
        pages.add(new BonitaPageItem("tasklistingpm", _("For process manager-type of profiles. To monitor task completion, only for processes under her responsibility"), _("For process manager-type of profiles. To monitor task completion, only for processes under her responsibility"), _("Tasks")));
        pages.add(new BonitaPageItem("caselistinguser", _("For user-type of profiles. To visualize cases started by the user or cases he has worked on"), _("For user-type of profiles. To visualize cases started by the user or cases he has worked on"), _("Cases")));
        pages.add(new BonitaPageItem("caselistingadmin", _("For administrator-type of profiles. To monitor cases of all processes"), _("For administrator-type of profiles. To monitor cases of all processes"), _("Cases")));
        pages.add(new BonitaPageItem("caselistingpm", _("For process manager-type of profiles. To monitor cases of processes under her responsibility"), _("For process manager-type of profiles. To monitor cases of processes under her responsibility"), _("Cases")));
        pages.add(new BonitaPageItem("processlistinguser", _("For user-type of profiles. To visualize processes and start cases"), _("For user-type of profiles. To visualize processes and start cases"), _("Processes")));
        pages.add(new BonitaPageItem("processlistingadmin", _("For administrator-type of profiles. To manage processes: install, delete and configure, for all processes"), _("For administrator-type of profiles. To manage processes: install, delete and configure, for all processes"), _("Processes")));
        pages.add(new BonitaPageItem("processlistingpm", _("For process manager-type of profiles. To manage processes under her responsibility"), _("For process manager-type of profiles. To manage processes under her responsibility"), _("Processes")));
        pages.add(new BonitaPageItem("userlistingadmin", _("Manage users"), _("Manage users"), _("Users")));
        pages.add(new BonitaPageItem("grouplistingadmin", _("Manage groups"), _("Manage groups"), _("Groups")));
        pages.add(new BonitaPageItem("rolelistingadmin", _("Manage roles"), _("Manage roles"), _("Roles")));
        pages.add(new BonitaPageItem("importexportorganization", _("Import/export organization"), _("Import/export organization"), _("Import/Export")));
        pages.add(new BonitaPageItem("profilelisting", _("User privilege settings"),  _("User privilege settings"), _("Profiles")));
        pages.add(new BonitaPageItem("reportlistingadminext", _("Analytics"), _("Analytics"), _("Analytics")));
        pages.add(new BonitaPageItem("thememoredetailsadminext", _("Manage Look and Feel"), _("Manage Look and Feel"), _("Look and Feel")));
        pages.add(new BonitaPageItem("pagelisting", _("Manage custom pages"), _("Manage custom pages"), _("Custom Pages")));
        pages.add(new BonitaPageItem("applicationslistingadmin", _("Manage applications"), _("Manage applications"), _("Applications")));
        pages.add(new BonitaPageItem("monitoringadmin", _("For administrator-type of profiles. To monitor all processes"), _("For administrator-type of profiles. To monitor all processes"), _("Monitoring")));
        pages.add(new BonitaPageItem("monitoringpm", _("For process manager-type of profiles. To monitor processes under her responsibility"), _("For process manager-type of profiles. To monitor processes under her responsibility"), _("Monitoring")));
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
