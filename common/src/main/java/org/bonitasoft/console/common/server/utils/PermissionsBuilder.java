package org.bonitasoft.console.common.server.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.ProfileNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;

public class PermissionsBuilder {

    public static final String PROFILE_TYPE_AUTHORIZATION_PREFIX = "profile";

    public static final String USER_TYPE_AUTHORIZATION_PREFIX = "user";

    protected final APISession session;
    private final ProfileAPI profileAPI;
    private final ApplicationAPI applicationAPI;
    private final CustomPermissionsMapping customPermissionsMapping;
    private final CompoundPermissionsMapping compoundPermissionsMapping;

    PermissionsBuilder(final APISession session, final ProfileAPI profileAPI,
            final ApplicationAPI applicationAPI, final CustomPermissionsMapping customPermissionsMapping,
            final CompoundPermissionsMapping compoundPermissionsMapping) {
        this.session = session;
        this.profileAPI = profileAPI;
        this.applicationAPI = applicationAPI;
        this.customPermissionsMapping = customPermissionsMapping;
        this.compoundPermissionsMapping = compoundPermissionsMapping;
    }

    public Set<String> getPermissions() throws LoginFailedException {
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

    void addProfilesPermissions(final Set<String> permissions) throws LoginFailedException {
        final Set<String> pageTokens;
        try {
            pageTokens = getAllPagesForUser(permissions);
        } catch (final ProfileNotFoundException e) {
            throw new LoginFailedException(e);
        }
        for (final String pageToken : pageTokens) {
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
    Set<String> getAllPagesForUser(final Set<String> permissions) throws ProfileNotFoundException {
        final Set<String> pageTokens = new HashSet<>();
        for (final String profile : session.getProfiles()) {
                addPageAndCustomPermissionsOfProfile(permissions, pageTokens, profile);
            }
        return pageTokens;
    }

    void addPageAndCustomPermissionsOfProfile(final Set<String> permissions, final Set<String> pageTokens,
            final String profile) throws ProfileNotFoundException {
        addPagesOfProfile(profile, pageTokens);
        addPagesOfApplication(profile, pageTokens);
        addCustomProfilePermissions(permissions, profile);
        addProfilesPermissions(permissions, profile);
    }

    private void addProfilesPermissions(final Set<String> permissions, final String profile) {
        permissions.add(PROFILE_TYPE_AUTHORIZATION_PREFIX + "|" + profile);
    }

    void addPagesOfProfile(final String profile, final Set<String> pageTokens) throws ProfileNotFoundException {
        final List<ProfileEntry> profileEntries = profileAPI.getProfileEntries(profile);
            for (final ProfileEntry profileEntry : profileEntries) {
                if (profileEntry.getType().equals(ProfileEntryItem.VALUE_TYPE.link.name())) {
                    pageTokens.add(profileEntry.getPage());
                }
            }
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
