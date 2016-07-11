/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.client.view.common;

import org.bonitasoft.forms.client.rpc.FormsService;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Forms RPC services accessor
 * 
 * @author Anthony Birembaut
 */
public class RpcFormsServices {

    /**
     * form service
     */
    protected static FormsServiceAsync formsService;

    /**
     * Default constructor
     */
    protected RpcFormsServices() {
        // Nothing to do here.
    }

    /**
     * @return the URL of the file upload servlet
     */
    public static String getFileUploadURL() {
        return GWT.getModuleBaseURL() + "fileUpload";
    }

    /**
     * @return the URL of the attachment download servlet
     */
    public static String getAttachmentDownloadURL() {
        return GWT.getModuleBaseURL() + "formsDocumentDownload";
    }

    /**
     * @return the URL of the attachment image servlet
     */
    public static String getAttachmentImageURL() {
        return GWT.getModuleBaseURL() + "formsDocumentImage";
    }

    /**
     * @return an instance of FormsServiceAsync
     */
    public static FormsServiceAsync getFormsService() {
        if (formsService == null) {
            formsService = (FormsServiceAsync) GWT.create(FormsService.class);
            final ServiceDefTarget endpoint = (ServiceDefTarget) formsService;
            endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "formsService");
        }
        return formsService;
    }
}
