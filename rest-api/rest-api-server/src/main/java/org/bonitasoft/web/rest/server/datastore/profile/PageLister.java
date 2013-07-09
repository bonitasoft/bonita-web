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

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;

/**
 * @author Fabio Lombardi
 * 
 */
public class PageLister {

    protected final static List<BonitaPageItem> pages = new ArrayList<BonitaPageItem>();
    static {
        pages.add(new BonitaPageItem("tasklistinguser", "visualize & do tasks", "", "Tasks"));
        pages.add(new BonitaPageItem("tasklistingadmin", "manage tasks", "", "Tasks"));
        pages.add(new BonitaPageItem("caselistinguser", "visualize cases", "", "Cases"));
        pages.add(new BonitaPageItem("caselistingadmin", "manage cases", "", "Cases"));
        pages.add(new BonitaPageItem("processlistinguser", "visualize & start apps", "", "Apps"));
        pages.add(new BonitaPageItem("processlistingadmin", "manage apps", "", "Apps"));
        pages.add(new BonitaPageItem("userlistingadmin", "manage users", "", "Users"));
        pages.add(new BonitaPageItem("grouplistingadmin", "manage groups", "", "Groups"));
        pages.add(new BonitaPageItem("rolelistingadmin", "manage roles", "", "Roles"));
        pages.add(new BonitaPageItem("importexportorganization", "import/export organization", "", "Import/Export"));
        pages.add(new BonitaPageItem("profilelisting", "user privilage settings", "", "User rights"));
        pages.add(new BonitaPageItem("reportlistingadminext", "Monitoring", "", "Analytics"));
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
