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

import java.util.Collections;
import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys;


/**
 * @author Fabio Lombardi
 *
 */
public class MenuNameAttributeReader extends AbstractAttributeReader implements HasDeploys {
    private final String attributeToDeploy;

    /**
     * Default Constructor.
     * @param attributeToRead
     */
    public MenuNameAttributeReader(final String attributeToDeploy, final String attributeToRead) {
        super(attributeToRead);
        this.attributeToDeploy = attributeToDeploy;
    }

    @Override
    protected String _read(final IItem item) {
        String menuName = "";
        if (item.getAttributeValue(attributeToDeploy).equals("0")) {
            menuName = item.getAttributeValue(leadAttribute);
        } else {
            final IItem deployedItem = item.getDeploy(attributeToDeploy);
            if (deployedItem != null) {
                menuName = deployedItem.getAttributeValue(leadAttribute);
            }
        }

        return "( " + _("in menu ") + menuName + " )";
    }

    @Override
    public List<String> getDeploys() {
        return Collections.singletonList(attributeToDeploy);
    }

    public String getAttributeToRead() {
        return leadAttribute;
    }
}
