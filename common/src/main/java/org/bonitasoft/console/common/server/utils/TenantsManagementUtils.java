/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;

/**
 * Tenant management utils class
 *
 * @author Anthony Birembaut
 */
public class TenantsManagementUtils {

    /**
     * The user ID parameter for the user profiles check
     */
    protected static final String USER_ID = "userId";

    /**
     * The command name for the profiles check
     */
    protected static final String GET_PROFILES_FOR_USER = "getProfilesForUser";

    /**
     * Copy file
     *
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    protected static void copyFile(final File sourceFile, final File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(sourceFile);
            inBuff = new BufferedInputStream(input);

            output = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(output);

            final byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * Copy a directory
     *
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    protected static void copyDirectory(final String sourceDir, final String targetDir) throws IOException {
        // new target folder
        new File(targetDir).mkdirs();

        // get the file or folder of sorcefolder
        final File[] file = new File(sourceDir).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                final File sourceFile = file[i];
                final File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                final String dir1 = sourceDir + File.separator + file[i].getName();
                final String dir2 = targetDir + File.separator + file[i].getName();
                copyDirectory(dir1, dir2);
            }
        }
    }

    /**
     * Delete a directory
     *
     * @param targetDir
     */
    protected static void deleteDirectory(final String targetDir) {
        final File file = new File(targetDir);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                final File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    final String dir = targetDir + File.separator + files[i].getName();
                    deleteDirectory(dir);
                }
            }
            file.delete();
        }
    }

    /**
     * Check for user's pofile
     */
    public static boolean hasProfileForUser(final APISession apiSession) throws NotFoundException, InvalidSessionException, BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException {
        final List<Profile> userProfiles = getProfileApi(apiSession).getProfilesForUser(apiSession.getUserId(), 0, 1, ProfileCriterion.ID_ASC);
        return !userProfiles.isEmpty();
    }

    private static ProfileAPI getProfileApi(final APISession session) throws InvalidSessionException, BonitaHomeNotSetException, ServerAPIException,
            UnknownAPITypeException {
        return TenantAPIAccessor.getProfileAPI(session);
    }

    /**
     * copy the tenant template directory
     *
     * @return true if the tenant directory was created
     * @param tenantId
     * @throws IOException
     * @throws BonitaException
     */
    public static synchronized boolean addDirectoryForTenant(final long tenantId) throws IOException, BonitaException {
        // add tenant folder
        final String targetDirPath = WebBonitaConstantsUtils.getInstance().getTenantsFolder().getPath() + File.separator + tenantId;
        final String sourceDirPath = WebBonitaConstantsUtils.getInstance().getTenantTemplateFolder().getPath();
        // copy configuration files
        final File workDir = new File(targetDirPath + File.separator + WebBonitaConstants.workFolderName);
        final File confDir = new File(targetDirPath + File.separator + WebBonitaConstants.confFolderName);
        if (!workDir.exists() || !confDir.exists() || confDir.list().length == 0) {
            try {
                deleteDirectory(targetDirPath);
                copyDirectory(sourceDirPath, targetDirPath);
                return true;
            } catch (final IOException e) {
                deleteDirectory(targetDirPath);
                throw e;
            }
        }
        return false;
    }

    /**
     * Get default tenant ID
     *
     * @throws DefaultTenantIdException
     *         If default tenant id couldn't be retrieved
     */
    public static long getDefaultTenantId() {
        try {
            final APISession session = TenantAPIAccessor.getLoginAPI().login(getTechnicalUserUsername(), getTechnicalUserPassword());
            final long tenantId = session.getTenantId();
            TenantAPIAccessor.getLoginAPI().logout(session);
            return tenantId;
        } catch (final Exception e) {
            throw new DefaultTenantIdException(e);
        }
    }

    public static String getTechnicalUserUsername() throws Exception {
        return PropertiesFactory.getPlatformTenantConfigProperties().defaultTenantUserName();
    }

    public static String getTechnicalUserPassword() throws Exception {
        return PropertiesFactory.getPlatformTenantConfigProperties().defaultTenantPassword();
    }
}
