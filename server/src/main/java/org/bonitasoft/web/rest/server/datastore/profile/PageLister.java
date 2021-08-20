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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.t_;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;

/**
 * @author Fabio Lombardi
 *
 */
public class PageLister {

    protected final static List<BonitaPageItem> pages = new ArrayList<BonitaPageItem>();
    static {
        pages.add(new BonitaPageItem("tasklistinguser", AbstractI18n.t_("For user-type of profiles. To view and do tasks  if he is a candidate"), AbstractI18n.t_("For user-type of profiles. To view and do tasks  if he is a candidate"), AbstractI18n.t_("Tasks")));
        pages.add(new BonitaPageItem("tasklistingadmin", AbstractI18n.t_("For administrator-type of profiles. To monitor task completion for all processes"), AbstractI18n.t_("For administrator-type of profiles. To monitor task completion for all processes"), AbstractI18n.t_("Tasks")));
        pages.add(new BonitaPageItem("tasklistingpm", AbstractI18n.t_("For process manager-type of profiles. To monitor task completion, only for processes under her responsibility"), AbstractI18n.t_("For process manager-type of profiles. To monitor task completion, only for processes under her responsibility"), AbstractI18n.t_("Tasks")));
        pages.add(new BonitaPageItem("caselistinguser", AbstractI18n.t_("For user-type of profiles. To visualize cases started by the user or cases he has worked on"), AbstractI18n.t_("For user-type of profiles. To visualize cases started by the user or cases he has worked on"), AbstractI18n.t_("Cases")));
        pages.add(new BonitaPageItem("caselistingadmin", AbstractI18n.t_("For administrator-type of profiles. To monitor cases of all processes"), AbstractI18n.t_("For administrator-type of profiles. To monitor cases of all processes"), AbstractI18n.t_("Cases")));
        pages.add(new BonitaPageItem("caselistingpm", AbstractI18n.t_("For process manager-type of profiles. To monitor cases of processes under her responsibility"), AbstractI18n.t_("For process manager-type of profiles. To monitor cases of processes under her responsibility"), AbstractI18n.t_("Cases")));
        pages.add(new BonitaPageItem("custompage_processlistBonita", AbstractI18n.t_("For user-type of profiles. UI Designer page to visualize processes and start cases"), AbstractI18n.t_("UI Designer page to visualize processes and start cases"), AbstractI18n.t_("Processes")));
        pages.add(new BonitaPageItem("processlistingadmin", AbstractI18n.t_("For administrator-type of profiles. To manage processes: install, delete and configure, for all processes"), AbstractI18n.t_("For administrator-type of profiles. To manage processes: install, delete and configure, for all processes"), AbstractI18n.t_("Processes")));
        pages.add(new BonitaPageItem("processlistingpm", AbstractI18n.t_("For process manager-type of profiles. To manage processes under her responsibility"), AbstractI18n.t_("For process manager-type of profiles. To manage processes under her responsibility"), AbstractI18n.t_("Processes")));
        pages.add(new BonitaPageItem("userlistingadmin", AbstractI18n.t_("Manage users"), AbstractI18n.t_("Manage users"), AbstractI18n.t_("Users")));
        pages.add(new BonitaPageItem("grouplistingadmin", AbstractI18n.t_("Manage groups"), AbstractI18n.t_("Manage groups"), AbstractI18n.t_("Groups")));
        pages.add(new BonitaPageItem("rolelistingadmin", AbstractI18n.t_("Manage roles"), AbstractI18n.t_("Manage roles"), AbstractI18n.t_("Roles")));
        pages.add(new BonitaPageItem("custompage_adminInstallExportOrganizationBonita", AbstractI18n.t_("Install/export organization"), AbstractI18n.t_("Install/export organization"), AbstractI18n.t_("Install/Export")));
        pages.add(new BonitaPageItem("profilelisting", AbstractI18n.t_("User privilege settings"),  AbstractI18n.t_("User privilege settings"), AbstractI18n.t_("Profiles")));
        pages.add(new BonitaPageItem("reportlistingadminext", AbstractI18n.t_("Analytics"), AbstractI18n.t_("Analytics"), AbstractI18n.t_("Analytics")));
        pages.add(new BonitaPageItem("thememoredetailsadminext", AbstractI18n.t_("Manage Look and Feel"), AbstractI18n.t_("Manage Look and Feel"), AbstractI18n.t_("Look and Feel")));
        pages.add(new BonitaPageItem("pagelisting", AbstractI18n.t_("Manage custom pages"), AbstractI18n.t_("Manage custom pages"), AbstractI18n.t_("Custom Pages")));
        pages.add(new BonitaPageItem("applicationslistingadmin", AbstractI18n.t_("Manage applications"), AbstractI18n.t_("Manage applications"), AbstractI18n.t_("Applications")));
        pages.add(new BonitaPageItem("monitoringadmin", AbstractI18n.t_("For administrator-type of profiles. To monitor all processes"), AbstractI18n.t_("For administrator-type of profiles. To monitor all processes"), AbstractI18n.t_("Monitoring")));
        pages.add(new BonitaPageItem("monitoringpm", AbstractI18n.t_("For process manager-type of profiles. To monitor processes under her responsibility"), AbstractI18n.t_("For process manager-type of profiles. To monitor processes under her responsibility"), AbstractI18n.t_("Monitoring")));
        pages.add(new BonitaPageItem("licensemonitoringadmin", AbstractI18n.t_("License"), AbstractI18n.t_("License"), AbstractI18n.t_("License")));
        pages.add(new BonitaPageItem("bdm", AbstractI18n.t_("BDM"), AbstractI18n.t_("Manage BDM"), AbstractI18n.t_("BDM")));
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
