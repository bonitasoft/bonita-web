/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 ******************************************************************************/

package org.bonitasoft.console.client.admin.page.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.application.ApplicationItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.List;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutDanger;

/**
 * Callout listing living Applications using the Theme to delete
 *
 * @author Julien MEGE
 */
public class DeleteApplicationThemeProblemsCallout extends CalloutDanger {

    protected List ul;

    public DeleteApplicationThemeProblemsCallout(final java.util.List<ApplicationItem> applications) {
        super(_("'%themeName%' is used as Theme for:",
                new Arg("themeName", applications.get(0).getTheme().getDisplayName())));
        ul = buildProblemsList(applications);
        append(ul);
    }

    private List buildProblemsList(final java.util.List<ApplicationItem> applications) {
        final List ul = new List();
        for (final ApplicationItem application : applications) {
            ul.append(new Text(application.getDisplayName()));
        }
        return ul;
    }

}
