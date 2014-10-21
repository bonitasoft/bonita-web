package org.bonitasoft.console.common.server.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
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

    private static final int MAX_ELEMENTS_RETRIEVED = 50;

    protected final APISession session;
    private final ProfileAPI profileAPI;
    private final CustomPermissionsMapping customPermissionsMapping;
    private final CompoundPermissionsMapping compoundPermissionsMapping;
    private final boolean apiAuthorizationsCheckEnabled;

    public PermissionsBuilder(final APISession session) throws LoginFailedException {
        this.session = session;
        try {
            profileAPI = TenantAPIAccessor.getProfileAPI(session);
        } catch (final BonitaException e) {
            throw new LoginFailedException(e);

        }
        apiAuthorizationsCheckEnabled = PropertiesFactory.getSecurityProperties(session.getTenantId()).isAPIAuthorizationsCheckEnabled();
        customPermissionsMapping = PropertiesFactory.getCustomPermissionsMapping(session.getTenantId());
        compoundPermissionsMapping = PropertiesFactory.getCompoundPermissionsMapping(session.getTenantId());
    }

    PermissionsBuilder(final APISession session, final ProfileAPI profileAPI, final CustomPermissionsMapping customPermissionsMapping,
            final CompoundPermissionsMapping compoundPermissionsMapping, final boolean apiAuthorizationsCheckEnabled) {
        this.session = session;
        this.profileAPI = profileAPI;
        this.customPermissionsMapping = customPermissionsMapping;
        this.compoundPermissionsMapping = compoundPermissionsMapping;
        this.apiAuthorizationsCheckEnabled = apiAuthorizationsCheckEnabled;
    }

    public Set<String> getPermissions() throws LoginFailedException {
        final HashSet<String> permissions = new HashSet<String>();
        if (apiAuthorizationsCheckEnabled) {
            addProfilesPermissions(permissions);
            addCustomUserPermissions(permissions);
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
        final Set<String> pageTokens = new HashSet<String>();
        final ProfileAPI profileAPI = this.profileAPI;
        int profilesIndex = 0;
        int nbOfProfilesRetrieved = MAX_ELEMENTS_RETRIEVED;
        while (nbOfProfilesRetrieved == MAX_ELEMENTS_RETRIEVED) {
            final List<Profile> profiles = getProfilesForUser(profileAPI, profilesIndex);
            nbOfProfilesRetrieved = profiles.size();
            for (final Profile profile : profiles) {
                addPageAndCustomPermissionsOfProfile(permissions, pageTokens, profileAPI, profile);
            }
            profilesIndex = profilesIndex + nbOfProfilesRetrieved;
        }
        return pageTokens;
    }

    void addPageAndCustomPermissionsOfProfile(final Set<String> permissions, final Set<String> pageTokens, final ProfileAPI profileAPI, final Profile profile) throws SearchException {
        addPagesOfProfile(profileAPI, profile, pageTokens);
        addCustomProfilePermissions(permissions, profile);
    }

    void addPagesOfProfile(final ProfileAPI profileAPI, final Profile profile, final Set<String> pageTokens) throws SearchException {
        int entriesIndex = 0;
        int nbOfProfileEntriesRetrieved = MAX_ELEMENTS_RETRIEVED;
        while (nbOfProfileEntriesRetrieved == MAX_ELEMENTS_RETRIEVED) {
            final List<ProfileEntry> profileEntries = getProfileEntriesForProfile(profileAPI, profile, entriesIndex);
            nbOfProfileEntriesRetrieved = profileEntries.size();
            for (final ProfileEntry profileEntry : profileEntries) {
                if (profileEntry.getType().equals(ProfileEntryItem.VALUE_TYPE.link.name())) {
                    pageTokens.add(profileEntry.getPage());
                }
            }
            entriesIndex = entriesIndex + nbOfProfileEntriesRetrieved;
        }
    }

    void addCustomProfilePermissions(final Set<String> permissions, final Profile profile) {
        permissions.addAll(getCustomPermissions("profile", profile.getName()));
    }

    void addCustomUserPermissions(final Set<String> permissions) {
        permissions.addAll(getCustomPermissions("user", session.getUserName()));
    }

    Set<String> getCustomPermissions(final String type, final String identifier) {
        final Set<String> profileSinglePermissions = new HashSet<String>();
        final List<String> customPermissionsForEntity = getCustomPermissionsRaw(type + "|" + identifier);
        for (final String customPermissionForEntity : customPermissionsForEntity) {
            final List<String> simplePermissions = getCompoundPermissions(customPermissionForEntity);
            if (!simplePermissions.isEmpty()) {
                profileSinglePermissions.addAll(simplePermissions);
            } else {
                profileSinglePermissions.add(customPermissionForEntity);
            }
        }
        return profileSinglePermissions;
    }

    private List<String> getCustomPermissionsRaw(final String key) {
        return customPermissionsMapping.getPropertyAsList(key);
    }

    private List<String> getCompoundPermissions(final String compoundName) {
        return compoundPermissionsMapping.getPropertyAsList(compoundName);
    }

    protected List<Profile> getProfilesForUser(final ProfileAPI profileAPI, final int profilesIndex) {
        return profileAPI.getProfilesForUser(session.getUserId(), profilesIndex, MAX_ELEMENTS_RETRIEVED, ProfileCriterion.ID_ASC);
    }

    protected List<ProfileEntry> getProfileEntriesForProfile(final ProfileAPI profileAPI, final Profile profile, final int entriesIndex) throws SearchException {
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(entriesIndex, MAX_ELEMENTS_RETRIEVED);
        searchOptionsBuilder.filter(ProfileEntrySearchDescriptor.PROFILE_ID, profile.getId());
        final SearchResult<ProfileEntry> profileEntriesResult = profileAPI.searchProfileEntries(searchOptionsBuilder.done());
        return profileEntriesResult.getResult();
    }
}
