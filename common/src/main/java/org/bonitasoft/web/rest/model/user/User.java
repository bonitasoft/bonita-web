/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guo Yongtao
 *
 */
public class User implements Serializable {

    /**
     * ID used for serialization.
     */
    private static final long serialVersionUID = 1940844173066923676L;

    /**
     * The username
     */
    protected String username;

    /**
     * Indicates the locale to use to display the user interface
     */
    protected String locale;

    /**
     * the tenant id
     */
    private long tenantId;

    /**
     * Indicates whether the credential transmission mechanism should be used
     */
    private boolean useCredentialTransmission;

    /**
     * Indicates whether this user correspond to an auto login account
     */
    private boolean isAutoLogin;

    /**
     * Indicates whether this user correspond to an anonymous login
     */
    private boolean isAnonymous;

    /**
     * the user rights
     */
    private List<String> availableFeatures = new ArrayList<String>();

    /**
     *
     * Default constructor.
     */
    public User() {
        super();
        // Mandatory for serialization.
    }

    /**
     * Default Constructor.
     *
     * @param username
     * @param aIsAdmin
     * @param aLocale
     * @param aUserRights
     * @param useCredentialTransmission
     */
    public User(final String username, final String locale) {
        this.username = username;
        this.locale = locale;
    }

    public void setFeatures(final List<String> featureList) {
        this.availableFeatures = featureList;
    }

    /**
     * @return the userUUID
     */
    public String getUsername() {
        return this.username;
    }

    public String getLocale() {
        return this.locale;
    }

    public long getTenantId() {
        return this.tenantId;
    }

    public void setUseCredentialTransmission(final boolean useCredentialTransmission) {
        this.useCredentialTransmission = useCredentialTransmission;
    }

    public boolean useCredentialTransmission() {
        return this.useCredentialTransmission;
    }

    private void setUsername(final String username) {
        this.username = username;
    }

    private void setLocale(final String locale) {
        this.locale = locale;
    }

    public void setTenantId(final long tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isAutoLogin() {
        return this.isAutoLogin;
    }

    public void setAutoLogin(final boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }

    public boolean isAnonymous() {
        return this.isAnonymous;
    }

    public void setAnonymous(final boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    /**
     * Update the user profile with the given values.
     */
    public void update(final User userProfile) {
        setLocale(userProfile.getLocale());
        setUseCredentialTransmission(userProfile.useCredentialTransmission());
        setUsername(userProfile.getUsername());
        setFeatures(userProfile.getFeatures());
    }

    public void addFeature(String feature) {
        if (this.availableFeatures != null) {
            this.availableFeatures.add(feature);
        }
    }

    public List<String> getFeatures() {
        return this.availableFeatures;
    }
}
