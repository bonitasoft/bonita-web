/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.api;

import java.util.Date;

import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.bonitasoft.forms.server.api.impl.FormDefinitionAPIImpl;
import org.bonitasoft.forms.server.api.impl.FormExpressionsAPIImpl;
import org.bonitasoft.forms.server.api.impl.FormValidationAPIImpl;
import org.bonitasoft.forms.server.api.impl.FormWorkflowAPIImpl;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Document;

/**
 * @author Anthony Birembaut
 *
 */
public class FormAPIFactory {

    public static IFormExpressionsAPI getFormExpressionsAPI() {
        return new FormExpressionsAPIImpl();
    }

    public static IFormWorkflowAPI getFormWorkflowAPI() {
        return new FormWorkflowAPIImpl();
    }

    public static IFormValidationAPI getFormValidationAPI() {
        return new FormValidationAPIImpl();
    }

    /**
     * @param tenantID
     *            the tenant ID
     * @param document
     *            the document
     * @param processDeployementDate
     *            the process deployment date
     * @param locale
     *            locale
     * @return the from definition API implementation
     * @throws InvalidFormDefinitionException
     */
    public static IFormDefinitionAPI getFormDefinitionAPI(final long tenantID, final Document document, final Date processDeployementDate, final String locale)
            throws InvalidFormDefinitionException {
        long tenantToUse = tenantID;
        if (tenantID == -1) {
            final String tenantIDStr = PropertiesFactory.getPlatformTenantConfigProperties().getDefaultTenantId();
            if (tenantIDStr != null) {
                tenantToUse = Long.parseLong(tenantIDStr);
            }
        }
        final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
        return new FormDefinitionAPIImpl(tenantToUse, document, formCacheUtil, processDeployementDate, locale);
    }
}
