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
package org.bonitasoft.web.rest.server.api.deployer;

import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.framework.Deployer;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Julien Mege
 */
public class PageDeployer implements Deployer {

    private final DatastoreHasGet<PageItem> getter;

    private final String attribute;

    public PageDeployer(final DatastoreHasGet<PageItem> getter, final String attribute) {
        this.getter = getter;
        this.attribute = attribute;
    }

    @Override
    public String getDeployedAttribute() {
        return attribute;
    }

    @Override
    public void deployIn(final IItem item) {
        if (isDeployable(attribute, item)) {
            item.setDeploy(attribute, getPage(getPageId(item)));
        }
    }

    private APIID getPageId(final IItem item) {
        return item.getAttributeValueAsAPIID(attribute);
    }

    private PageItem getPage(final APIID pageId) {
        return getter.get(pageId);
    }

    protected final boolean isDeployable(final String attribute, final IItem item) {
        return item.getAttributeValueAsAPIID(attribute) != null;
    }

}
