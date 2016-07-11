package org.bonitasoft.console.common.server.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.ProfileEntrySearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;

public class PermissionsBuilder {

    public static final String PROFILE_TYPE_AUTHORIZATION_PREFIX = "profile";

    public static final String USER_TYPE_AUTHORIZATION_PREFIX = "user";

    protected static final int MAX_ELEMENTS_RETRIEVED = 200;

    protected final APISession session;
    private final ProfileAPI profileAPI;
    private final ApplicationAPI applicationAPI;
    private final CustomPermissionsMapping customPermissionsMapping;
    private final CompoundPermissionsMapping compoundPermissionsMapping;
    private final boolean apiAuthorizationsCheckEnabled;

    protected PermissionsBuilder(final APISession session, final ProfileAPI profileAPI,final ApplicationAPI applicationAPI, final CustomPermissionsMapping customPermissionsMapping,
            final CompoundPermissionsMapping compoundPermissionsMapping, final SecurityProperties securityProperties) {
        this.session = session;
        this.profileAPI = profileAPI;
        this.applicationAPI = applicationAPI;
        this.customPermissionsMapping = customPermissionsMapping;
        this.compoundPermissionsMapping = compoundPermissionsMapping;
        apiAuthorizationsCheckEnabled = securityProperties.isAPIAuthorizationsCheckEnabled();
    }

    public Set<String> getPermissions() throws LoginFailedException {
        Set<String> permissions;
        if (session.isTechnicalUser()) {
            permissions = Collections.emptySet();
        } else {
            permissions = new HashSet<>();
            if (apiAuthorizationsCheckEnabled) {
                addProfilesPermissions(permissions);
                addCustomUserPermissions(permissions);
            }
        }
        return permissions;
    }

    void addProfilesPermissions(final Set<String> permissions) throws LoginFailedException {
        final Set<String> pageTokens;
        try {
            pageTokens = getAllPagesForUser(permissions);
        } catch (final SearchException e) {
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
     * @throws SearchException
     */
    Set<String> getAllPagesForUser(final Set<String> permissions) throws SearchException {
        final Set<String> pageTokens = new HashSet<>();
        int profilesIndex = 0;
        int nbOfProfilesRetrieved = MAX_ELEMENTS_RETRIEVED;
        while (nbOfProfilesRetrieved == MAX_ELEMENTS_RETRIEVED) {
            final List<Profile> profiles = profileAPI.getProfilesForUser(session.getUserId(), profilesIndex, MAX_ELEMENTS_RETRIEVED, ProfileCriterion.ID_ASC);
            nbOfProfilesRetrieved = profiles.size();
            for (final Profile profile : profiles) {
                addPageAndCustomPermissionsOfProfile(permissions, pageTokens, profile);
            }
            profilesIndex = profilesIndex + nbOfProfilesRetrieved;
        }
        return pageTokens;
    }

    void addPageAndCustomPermissionsOfProfile(final Set<String> permissions, final Set<String> pageTokens, final Profile profile) throws SearchException {
        addPagesOfProfile(profile, pageTokens);
        addPagesOfApplication(profile, pageTokens);
        addCustomProfilePermissions(permissions, profile);
        addProfilesPermissions(permissions, profile);
    }

    void addProfilesPermissions(final Set<String> permissions, final Profile profile) {
        permissions.add(PROFILE_TYPE_AUTHORIZATION_PREFIX + "|" + profile.getName());
    }

    void addPagesOfProfile(final Profile profile, final Set<String> pageTokens) throws SearchException {
        int entriesIndex = 0;
        int nbOfProfileEntriesRetrieved = MAX_ELEMENTS_RETRIEVED;
        while (nbOfProfileEntriesRetrieved == MAX_ELEMENTS_RETRIEVED) {
            final List<ProfileEntry> profileEntries = getProfileEntriesForProfile(profile, entriesIndex);
            nbOfProfileEntriesRetrieved = profileEntries.size();
            for (final ProfileEntry profileEntry : profileEntries) {
                if (profileEntry.getType().equals(ProfileEntryItem.VALUE_TYPE.link.name())) {
                    pageTokens.add(profileEntry.getPage());
                }
            }
            entriesIndex = entriesIndex + nbOfProfileEntriesRetrieved;
        }
    }

    void addPagesOfApplication(final Profile profile, final Set<String> pageTokens) throws SearchException {
        final List<String> allPagesForProfile = applicationAPI.getAllPagesForProfile(profile.getId());
        pageTokens.addAll(allPagesForProfile);
    }

    void addCustomProfilePermissions(final Set<String> permissions, final Profile profile) {
        permissions.addAll(getCustomPermissions(PROFILE_TYPE_AUTHORIZATION_PREFIX, profile.getName()));
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

    Set<String> getCustomPermissionsRaw(final String type, final String identifier) {
        return customPermissionsMapping.getPropertyAsSet(type + "|" + identifier);
    }

    Set<String> getCompoundPermissions(final String compoundName) {
        return compoundPermissionsMapping.getPropertyAsSet(compoundName);
    }

    List<ProfileEntry> getProfileEntriesForProfile(final Profile profile, final int entriesIndex) throws SearchException {
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(entriesIndex, MAX_ELEMENTS_RETRIEVED);
        searchOptionsBuilder.filter(ProfileEntrySearchDescriptor.PROFILE_ID, profile.getId());
        final SearchResult<ProfileEntry> profileEntriesResult = profileAPI.searchProfileEntries(searchOptionsBuilder.done());
        return profileEntriesResult.getResult();
    }
}
