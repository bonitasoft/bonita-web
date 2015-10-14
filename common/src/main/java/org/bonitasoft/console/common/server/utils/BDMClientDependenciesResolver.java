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
package org.bonitasoft.console.common.server.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.business.data.BusinessDataRepositoryException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;

public class BDMClientDependenciesResolver {

    private final APISession session;

    private static Logger LOGGER = Logger.getLogger(BDMClientDependenciesResolver.class.getName());

    public BDMClientDependenciesResolver(APISession session) {
        this.session = session;
    }

    public URL[] getBDMDependencies() throws IOException {
        final File currentBDMFolder = getCurrentBDMFolder();
        if (shouldUpdateBDMDependencies(currentBDMFolder)) {
            cleanBDMFolder(currentBDMFolder);
        }
        if (currentBDMFolder != null) {
            if (!currentBDMFolder.exists() || currentBDMFolder.listFiles().length == 0) {
                updateBDMClientFolder(currentBDMFolder);
            }
        }
        return getBDMLibrariesURLs(currentBDMFolder);
    }

    public boolean shouldUpdateBDMDependencies(File currentBDMFolder) {
        return currentBDMFolder != null && !currentBDMFolder.exists();
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
        if (currentBDMFolder != null) {
            final File parentFile = currentBDMFolder.getParentFile();
            if (parentFile != null && parentFile.exists()) {
                final File[] listFiles = currentBDMFolder.getParentFile().listFiles();
                if (listFiles != null) {
                    for (final File previousDeployedBDM : listFiles) {
                        if (previousDeployedBDM.isDirectory()) {
                            try {
                                FileUtils.deleteDirectory(previousDeployedBDM);
                            } catch (final IOException e) {
                                final String message = "Unable to delete obsolete BDM libraries";
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING, message, e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateBDMClientFolder(final File bdmWorkDir) {
        if (!bdmWorkDir.exists()) {
            bdmWorkDir.mkdirs();
        }
        try {
            final TenantAdministrationAPI tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(session);
            final byte[] clientBDMZip = tenantAdministrationAPI.getClientBDMZip();
            unzipContentToFolder(clientBDMZip, bdmWorkDir);
        } catch (final BonitaHomeNotSetException | IOException | BusinessDataRepositoryException | ServerAPIException | UnknownAPITypeException e) {
            final String message = "Unable to create the class loader for the BDM libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        }
    }

    private void unzipContentToFolder(final byte[] zipContent, final File targetFolder) throws IOException {
        ByteArrayInputStream is = null;
        ZipInputStream zis = null;
        FileOutputStream out = null;
        try {
            is = new ByteArrayInputStream(zipContent);
            zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final String entryName = entry.getName();
                if (entryName.endsWith(".jar")) {
                    final File file = new File(targetFolder, entryName);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    out = new FileOutputStream(file);
                    int len = 0;
                    final byte[] buffer = new byte[1024];
                    while ((len = zis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (zis != null) {
                zis.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public String getBusinessDataModelVersion() {
        try {
            final TenantAdministrationAPI tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(session);
            final String lastBDMDeployementId = tenantAdministrationAPI.getBusinessDataModelVersion();
            return lastBDMDeployementId;
        } catch (final Exception e) {
            final String message = "Unable to retrieve business data model version";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            return null;
        }
    }

    private URL[] getBDMLibrariesURLs(final File bdmFolder) throws IOException {
        final List<URL> urls = new ArrayList<URL>();
        if (bdmFolder.exists()) {
            final File[] bdmFiles = bdmFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(final File arg0, final String arg1) {
                    return arg1.endsWith(".jar");
                }
            });
            if (bdmFiles != null) {
                for (int i = 0; i < bdmFiles.length; i++) {
                    urls.add(bdmFiles[i].toURI().toURL());
                }
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The bdm directory doesn't exists.");
            }
        }
        final URL[] urlArray = new URL[urls.size()];
        urls.toArray(urlArray);
        return urlArray;
    }


}
