/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.system;

import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Julien Reboul
 * 
 */
public class TenantAdminItem extends Item implements ItemHasUniqueId {

    public static final String ATTRIBUTE_IS_PAUSED = "paused";

    @Override
    public void setId(final String id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    public void setIsPaused(final boolean isPaused) {
        setAttribute(ATTRIBUTE_IS_PAUSED, isPaused);
    }

    public boolean isPaused() {
        return Boolean.parseBoolean(getAttributeValue(ATTRIBUTE_IS_PAUSED));
    }

    @Override
    public ItemDefinition<TenantAdminItem> getItemDefinition() {
        return new TenantAdminDefinition();
    }

}
