/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.profile;

import java.util.ArrayList;
import java.util.List;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;

/**
 * @author Fabio Lombardi
 * 
 */
public class PageLister {

    protected final static List<BonitaPageItem> pages = new ArrayList<BonitaPageItem>();
    static {
        pages.add(new BonitaPageItem("tasklistinguser", _("visualize & do tasks"), "", _("Tasks")));
        pages.add(new BonitaPageItem("tasklistingadmin", _("manage tasks"), "", _("Tasks")));
        pages.add(new BonitaPageItem("tasklistingpm", _("manage tasks"), "", _("Tasks")));
        pages.add(new BonitaPageItem("caselistinguser", _("visualize cases"), "", _("Cases")));
        pages.add(new BonitaPageItem("caselistingadmin", _("manage cases"), "", _("Cases")));
        pages.add(new BonitaPageItem("caselistingpm", _("manage cases"), "", _("Cases")));
        pages.add(new BonitaPageItem("processlistinguser", _("visualize & start apps"), "", _("Apps")));
        pages.add(new BonitaPageItem("processlistingadmin", _("manage apps"), "", _("Apps")));
        pages.add(new BonitaPageItem("processlistingpm", _("manage apps"), "", _("Apps")));
        pages.add(new BonitaPageItem("userlistingadmin", _("manage users"), "", _("Users")));
        pages.add(new BonitaPageItem("grouplistingadmin", _("manage groups"), "", _("Groups")));
        pages.add(new BonitaPageItem("rolelistingadmin", _("manage roles"), "", _("Roles")));
        pages.add(new BonitaPageItem("importexportorganization", _("import/export organization"), "", _("Import/Export")));
        pages.add(new BonitaPageItem("profilelisting", _("user privilege settings"), "", _("Profiles")));
        pages.add(new BonitaPageItem("reportlistingadminext", _("Monitoring"), "", _("Analytics")));
        pages.add(new BonitaPageItem("thememoredetailsadminext", _("Manage Look and Feel"), "", _("Look and Feel")));
    }

    public List<BonitaPageItem> getPages() {
        return pages;
    }

    public BonitaPageItem getPage(String token) {
        for (BonitaPageItem page : pages) {
            if (page.getToken().equals(token)) {
                return page;
            }
        }
        return null;
    }

    public List<BonitaPageItem> getPages(List<String> pagesTokenToSkip) {
        final List<BonitaPageItem> pagelist = new ArrayList<BonitaPageItem>();

        for (BonitaPageItem page : pages) {
            if (!(pagesTokenToSkip.contains(page.getToken()))) {
                pagelist.add(page);
            }
        }
        return pagelist;
    }
}
