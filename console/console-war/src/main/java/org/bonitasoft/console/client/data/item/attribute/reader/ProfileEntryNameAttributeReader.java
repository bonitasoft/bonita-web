/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.data.item.attribute.reader;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Collections;
import java.util.List;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys;

/**
 * @author Julien Mege
 * 
 */
public class ProfileEntryNameAttributeReader extends AbstractAttributeReader implements HasDeploys {

    /**
     * The name of the attribute to deploy in the main Item
     */
    private final String attributeToDeploy;

    private final String defaultDeployedAttributeToRead;

    public ProfileEntryNameAttributeReader(final String attributeToRead, final String attributeToDeploy, final String defaultDeployedAttributeToRead) {
        super(attributeToRead);
        this.attributeToDeploy = attributeToDeploy;
        this.defaultDeployedAttributeToRead = defaultDeployedAttributeToRead;
    }

    @Override
    protected String _read(final IItem item) {
        final String customName = item.getAttributeValue(leadAttribute);
        if (!StringUtil.isBlank(customName)) {
            return _(customName);
        }

        final IItem deployedItem = item.getDeploy(this.attributeToDeploy);
        if (deployedItem != null) {
            return _(deployedItem.getAttributeValue(defaultDeployedAttributeToRead));
        }

        return "-";
    }

    /*
     * Deploys reader can only return one entry.
     */
    @Override
    public List<String> getDeploys() {
        return Collections.singletonList(this.attributeToDeploy);
    }

    public String getAttributeToRead() {
        return this.leadAttribute;
    }

}
