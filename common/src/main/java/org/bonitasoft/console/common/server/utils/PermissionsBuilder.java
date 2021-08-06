package org.bonitasoft.console.common.server.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.session.APISession;

public class PermissionsBuilder {

    public static final String PROFILE_TYPE_AUTHORIZATION_PREFIX = "profile";

    public static final String USER_TYPE_AUTHORIZATION_PREFIX = "user";

    protected final APISession session;
    private final ApplicationAPI applicationAPI;
    private final CustomPermissionsMapping customPermissionsMapping;
    private final CompoundPermissionsMapping compoundPermissionsMapping;

    PermissionsBuilder(final APISession session,
            final ApplicationAPI applicationAPI, final CustomPermissionsMapping customPermissionsMapping,
            final CompoundPermissionsMapping compoundPermissionsMapping) {
        this.session = session;
        this.applicationAPI = applicationAPI;
        this.customPermissionsMapping = customPermissionsMapping;
        this.compoundPermissionsMapping = compoundPermissionsMapping;
    }

    public Set<String> getPermissions() {
        Set<String> permissions;
        if (session.isTechnicalUser()) {
            permissions = Collections.emptySet();
        } else {
            permissions = new HashSet<>();
            addProfilesPermissions(permissions);
            addCustomUserPermissions(permissions);
        }
        return permissions;
    }

    void addProfilesPermissions(final Set<String> permissions) {
        for (final String pageToken : getAllPagesForUser(permissions)) {
            permissions.addAll(getCompoundPermissions(pageToken));
        }
    }

    /**
     * return the page names the user can access and add custom permissions of the profile in the permissions set
     *
     * @param permissions
     *        the set to complete
     * @return
     *         the page names the user can access
     */
    Set<String> getAllPagesForUser(final Set<String> permissions) {
        final Set<String> pageTokens = new HashSet<>();
        for (final String profile : session.getProfiles()) {
                addPageAndCustomPermissionsOfProfile(permissions, pageTokens, profile);
            }
        return pageTokens;
    }

    void addPageAndCustomPermissionsOfProfile(final Set<String> permissions, final Set<String> pageTokens,
            final String profile) {
        addPagesOfApplication(profile, pageTokens);
        addCustomProfilePermissions(permissions, profile);
        addProfilesPermissions(permissions, profile);
    }

    private void addProfilesPermissions(final Set<String> permissions, final String profile) {
        permissions.add(PROFILE_TYPE_AUTHORIZATION_PREFIX + "|" + profile);
    }

    void addPagesOfApplication(final String profile, final Set<String> pageTokens) {
        final List<String> allPagesForProfile = applicationAPI.getAllPagesForProfile(profile);
        pageTokens.addAll(allPagesForProfile);
    }

    private void addCustomProfilePermissions(final Set<String> permissions, final String profile) {
        permissions.addAll(getCustomPermissions(PROFILE_TYPE_AUTHORIZATION_PREFIX, profile));
    }

    void addCustomUserPermissions(final Set<String> permissions) {
        permissions.addAll(getCustomPermissions(USER_TYPE_AUTHORIZATION_PREFIX, session.getUserName()));
    }

    Set<String> getCustomPermissions(final String type, final String identifier) {
        final Set<String> profileSinglePermissions = new HashSet<>();
        final Set<String> customPermissionsForEntity = getCustomPermissionsRaw(type, identifier);
        for (final String customPermissionForEntity : customPermissionsForEntity) {
            final Set<String> simplePermissions = getCompoundPermissions(customPermissionForEntity);
            if (!simplePermissions.isEmpty()) {
                profileSinglePermissions.addAll(simplePermissions);
            } else {
                profileSinglePermissions.add(customPermissionForEntity);
            }
        }
        return profileSinglePermissions;
    }

    private Set<String> getCustomPermissionsRaw(final String type, final String identifier) {
        return customPermissionsMapping.getPropertyAsSet(type + "|" + identifier);
    }

    Set<String> getCompoundPermissions(final String compoundName) {
        return compoundPermissionsMapping.getPropertyAsSet(compoundName);
    }

}
