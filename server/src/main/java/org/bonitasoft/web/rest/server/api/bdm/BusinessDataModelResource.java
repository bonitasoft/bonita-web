/**
 * Copyright (C) 2018 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.bdm;

import java.io.File;
import java.io.IOException;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.business.data.BusinessDataRepositoryDeploymentException;
import org.bonitasoft.engine.business.data.InvalidBusinessDataModelException;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bdm.BusinessDataModelItem;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.api.tenant.TenantResourceItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * @author Anthony Birembaut
 */
public class BusinessDataModelResource extends CommonResource {


    private final TenantAdministrationAPI tenantAdministrationAPI;
    
    private final BonitaHomeFolderAccessor bonitaHomeFolderAccessor;
    
    private final APISession apiSession;
    
    public BusinessDataModelResource(final TenantAdministrationAPI tenantAdministrationAPI, BonitaHomeFolderAccessor bonitaHomeFolderAccessor, APISession apiSession) {
        this.apiSession = apiSession;
        this.bonitaHomeFolderAccessor = bonitaHomeFolderAccessor;
        this.tenantAdministrationAPI = tenantAdministrationAPI;
    }
    
    @Post("json")
    public TenantResourceItem addBDM(final BusinessDataModelItem businessDataModelItem) {
        if (!isTenantPaused()) {
            setStatus(Status.CLIENT_ERROR_FORBIDDEN, new APIException("Unable to install the Business Data Model. Please pause the BPM Services first. Go to Configuration > BPM Services."));
            return null;
        }
        try {
            final byte[] businessDataModelContent = getBusinessDataModelContent(businessDataModelItem);
            tenantAdministrationAPI.updateBusinessDataModel(businessDataModelContent);
            return new TenantResourceItem(tenantAdministrationAPI.getBusinessDataModelResource(), businessDataModelItem.getFileUpload());
        } catch (APIForbiddenException e) {
            setStatus(Status.CLIENT_ERROR_FORBIDDEN, e);
            return null;
        } catch (final InvalidBusinessDataModelException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e, "Invalid Business Data Model content");
            return null;
        } catch (final BusinessDataRepositoryDeploymentException e) {
            throw new APIException("An error has occurred when deploying Business Data Model.", e);
        }
    }
    
    @Get("json")
    public TenantResourceItem getBDM() {
        try {
            return new TenantResourceItem(tenantAdministrationAPI.getBusinessDataModelResource());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
    
    public boolean isTenantPaused() {
        return tenantAdministrationAPI.isPaused();
    }
    
    private byte[] getBusinessDataModelContent(final BusinessDataModelItem item) {
        try {
            return IOUtil.getAllContentFrom(new File(getCompleteTempFilePath(item.getFileUpload())));
        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        } catch (final IOException e) {
            throw new APIException("Can't read business data model file", e);
        }
    }
    
    public String getCompleteTempFilePath(final String path) throws IOException {
        return bonitaHomeFolderAccessor.getCompleteTempFilePath(path, apiSession.getTenantId());
    }

}
