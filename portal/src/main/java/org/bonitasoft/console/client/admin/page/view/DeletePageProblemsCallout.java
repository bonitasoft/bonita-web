/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.admin.page.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.List;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutDanger;

/**
 * Callout listing living Applications using the page to delete
 *
 * @author Julien MEGE
 */
public class DeletePageProblemsCallout extends CalloutDanger {

    protected org.bonitasoft.web.toolkit.client.ui.component.List ul;

    public DeletePageProblemsCallout(final java.util.List<ApplicationPageItem> applicationPages) {
        super(_("'%pageName%' is used by:",
                new Arg("pageName", applicationPages.get(0).getPage().getDisplayName())));
        ul = buildProblemsList(applicationPages);
        append(ul);
    }

    private List buildProblemsList(final java.util.List<ApplicationPageItem> applicationPages) {
        final List ul = new List();
        for (final ApplicationPageItem applicationPage : applicationPages) {
            ul.append(new Text(_("'%applicationName%' as page '%associationName%'",
                    new Arg("applicationName", applicationPage.getApplication().getToken()),
                    new Arg("associationName", applicationPage.getToken()))));
        }
        return ul;
    }

}
