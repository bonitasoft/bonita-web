/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
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
        pages.add(new BonitaPageItem("tasklistingpm", _("Manage tasks of apps that an entity with this profile can supervise as PM."), _("Manage tasks of apps that an entity with this profile can supervise as PM."), _("Tasks")));
        pages.add(new BonitaPageItem("caselistinguser", _("visualize cases"), _("visualize cases"), _("Cases")));
        pages.add(new BonitaPageItem("caselistingadmin", _("manage cases"), _("manage cases"), _("Cases")));
        pages.add(new BonitaPageItem("caselistingpm", _("Manage cases of apps that an entity with this profile can supervise as PM."), _("Manage cases of apps that an entity with this profile can supervise as PM."), _("Cases")));
        pages.add(new BonitaPageItem("processlistinguser", _("visualize & start apps"), _("visualize & start apps"), _("Apps")));
        pages.add(new BonitaPageItem("processlistingadmin", _("manage apps"), _("manage apps"), _("Apps")));
        pages.add(new BonitaPageItem("processlistingpm", _("Manage apps that an entity with this profile can supervise as PM."), _("Manage apps that an entity with this profile can supervise as PM."), _("Apps")));
        pages.add(new BonitaPageItem("userlistingadmin", _("manage users"), _("manage users"), _("Users")));
        pages.add(new BonitaPageItem("grouplistingadmin", _("manage groups"), _("manage groups"), _("Groups")));
        pages.add(new BonitaPageItem("rolelistingadmin", _("manage roles"), _("manage roles"), _("Roles")));
        pages.add(new BonitaPageItem("importexportorganization", _("import/export organization"), _("import/export organization"), _("Import/Export")));
        pages.add(new BonitaPageItem("profilelisting", _("user privilege settings"),  _("user privilege settings"), _("Profiles")));
        pages.add(new BonitaPageItem("reportlistingadminext", _("Monitoring"), _("Monitoring"), _("Analytics")));
        pages.add(new BonitaPageItem("thememoredetailsadminext", _("Manage Look and Feel"), _("Manage Look and Feel"), _("Look and Feel")));
        pages.add(new BonitaPageItem("pagelisting", _("Manage custom pages"), _("Manage custom pages"), _("Custom Pages")));
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
            if (!(pagesTokenToSkip.contains(page.getToken()))) {
                pagelist.add(page);
            }
        }
        return pagelist;
    }
}
