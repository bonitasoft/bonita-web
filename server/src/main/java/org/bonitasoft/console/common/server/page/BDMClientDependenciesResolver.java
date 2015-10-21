/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.page;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.business.data.BusinessDataRepositoryException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.engine.session.APISession;

public class BDMClientDependenciesResolver {

    private final APISession session;

    private static Logger LOGGER = Logger.getLogger(BDMClientDependenciesResolver.class.getName());

    private final Set<String> dependenciesNames = new HashSet<>();

    public BDMClientDependenciesResolver(APISession session) {
        this.session = session;
    }

    public URL[] getBDMDependencies() throws IOException {
        if(!isBDMDeployed()){
            return new URL[0];
        }
        final File currentBDMFolder = getCurrentBDMFolder();
        if (shouldUpdateBDMDependencies(currentBDMFolder)) {
            cleanBDMFolder(currentBDMFolder);
            createBDMClientFolder(currentBDMFolder);
        }
        return getBDMLibrariesURLs(currentBDMFolder);
    }

    private boolean isBDMDeployed() {
        return getBusinessDataModelVersion()!= null;
    }

    private boolean shouldUpdateBDMDependencies(File currentBDMFolder) {
        return !currentBDMFolder.exists() || currentBDMFolder.listFiles().length == 0;
    }

    private File getCurrentBDMFolder() {
        File bdmWorkDir = null;
        final String businessDataModelVersion = getBusinessDataModelVersion();
        if (businessDataModelVersion != null) {
            bdmWorkDir = new File(WebBonitaConstantsUtils.getInstance(session.getTenantId()).geBDMWorkFolder(),
                    businessDataModelVersion);
        }
        return bdmWorkDir;
    }

    private void cleanBDMFolder(final File currentBDMFolder) {
        final File parentFile = currentBDMFolder.getParentFile();
        if (parentFile != null && parentFile.exists()) {
            for (final File previousDeployedBDM : parentFile.listFiles()) {
                if (previousDeployedBDM.isDirectory()) {
                    try {
                        FileUtils.deleteDirectory(previousDeployedBDM);
                    } catch (final IOException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Unable to delete obsolete BDM libraries", e);
                        }
                    }
                }
            }
        }
    }

    private void createBDMClientFolder(final File bdmWorkDir) {
        if (!bdmWorkDir.exists()) {
            bdmWorkDir.mkdirs();
        }
        ByteArrayInputStream inpuStream = null;
        try {
            final TenantAdministrationAPI tenantAdministrationAPI = getTenantAdminstrationAPI();
            inpuStream = new ByteArrayInputStream(tenantAdministrationAPI.getClientBDMZip());
            IOUtil.unzipToFolder(inpuStream, bdmWorkDir);
        } catch (final BonitaHomeNotSetException | IOException | BusinessDataRepositoryException | ServerAPIException | UnknownAPITypeException e) {
            final String message = "Unable to create the class loader for the BDM libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        }
    }

    protected TenantAdministrationAPI getTenantAdminstrationAPI() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getTenantAdministrationAPI(session);
    }

    public String getBusinessDataModelVersion() {
        try {
            final TenantAdministrationAPI tenantAdministrationAPI = getTenantAdminstrationAPI();
            return tenantAdministrationAPI.getBusinessDataModelVersion();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to retrieve business data model version", e);
            return null;
        }
    }

    private URL[] getBDMLibrariesURLs(final File bdmFolder) throws IOException {
        final List<URL> urls = new ArrayList<URL>();
        for(final File file :  bdmFolder.listFiles()){
            if(file.getName().endsWith(".jar")){
                dependenciesNames.add(file.getName());
                urls.add(file.toURI().toURL());
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }


    public boolean isABDMDependency(String resourceName) {
        return dependenciesNames.contains(resourceName);
    }

}
