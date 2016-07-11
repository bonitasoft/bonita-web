/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.tenant;

import java.io.File;
import java.io.IOException;

import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.web.rest.model.tenant.BusinessDataModelDefinition;
import org.bonitasoft.web.rest.model.tenant.BusinessDataModelItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.TenantManagementEngineClient;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

public class APIBusinessDataModel extends ConsoleAPI<BusinessDataModelItem> implements APIHasAdd<BusinessDataModelItem> {


    /**
     * protected for testing
     */
    protected TenantManagementEngineClient getTenantManagementEngineClient() {
        return new EngineClientFactory(new EngineAPIAccessor(getEngineSession())).createTenantManagementEngineClient();
    }

    @Override
    public BusinessDataModelItem add(final BusinessDataModelItem businessDataModel) {
        final byte[] businessDataModelContent = getBusinessDataModelContent(businessDataModel);
        getTenantManagementEngineClient().uninstallBusinessDataModel();
        getTenantManagementEngineClient().installBusinessDataModel(businessDataModelContent);
        // return myself because get is not implemented in engine
        return businessDataModel;
    }

    private byte[] getBusinessDataModelContent(final BusinessDataModelItem item) {
        try {
            return IOUtil.getAllContentFrom(new File(getCompleteTempFilePath(item.getFileUploadPath())));
        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        } catch (final IOException e) {
            throw new APIException("Can't read business data model file", e);
        }
    }

    @Override
    protected ItemDefinition<BusinessDataModelItem> defineItemDefinition() {
        return new BusinessDataModelDefinition();
    }
}
