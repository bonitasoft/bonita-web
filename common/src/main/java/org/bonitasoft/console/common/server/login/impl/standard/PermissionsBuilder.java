package org.bonitasoft.console.common.server.login.impl.standard;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.profile.ProfileEntrySearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;


public class PermissionsBuilder {

    private static final int MAX_ELEMENTS_RETRIEVED = 200;

    private final APISession session;

    public PermissionsBuilder(final APISession session) {
        this.session = session;
    }

    public List<String> getPermissions() throws LoginFailedException {

        final List<String> permissions = new ArrayList<String>();
        if (isAuthorizationsChecksEnabled()) {
            final CustomPermissionsMapping customPermissionsMapping = getCustomPermissionsMapping();
            permissions.addAll(getProfilesPermissions(customPermissionsMapping));
            permissions.addAll(getCustomUserPermissions(customPermissionsMapping));
        }
        return permissions;
    }

    protected boolean isAuthorizationsChecksEnabled() {
        return PropertiesFactory.getSecurityProperties(session.getTenantId()).isAPIAuthorizationsCheckEnabled();
    }

    protected CustomPermissionsMapping getCustomPermissionsMapping() {
        return PropertiesFactory.getCustomPermissionsMapping(session.getTenantId());
    }

    protected CompoundPermissionsMapping getCompoundPermissionsMapping() {
        return PropertiesFactory.getCompoundPermissionsMapping(session.getTenantId());
    }

    protected List<String> getProfilesPermissions(final CustomPermissionsMapping customPermissionsMapping) throws LoginFailedException {
        final List<String> permissions = new ArrayList<String>();
        final List<String> pageTokens;
        try {
            pageTokens = getAllPagesForUser(customPermissionsMapping, permissions);
        } catch (final BonitaException e) {
            throw new LoginFailedException(e);
        }
        final CompoundPermissionsMapping compoundPermissionsMapping = getCompoundPermissionsMapping();
        for (final String pageToken : pageTokens) {
            permissions.addAll(compoundPermissionsMapping.getPropertyAsList(pageToken));
        }
        return permissions;
    }

    protected List<String> getAllPagesForUser(final CustomPermissionsMapping customPermissionsMapping, final List<String> permissions)
            throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, SearchException {
        final List<String> pageTokens = new ArrayList<String>();
        final ProfileAPI profileAPI = getProfileAPI();
        int profilesIndex = 0;
        int nbOfProfilesRetrieved = MAX_ELEMENTS_RETRIEVED;
        while (nbOfProfilesRetrieved == MAX_ELEMENTS_RETRIEVED) {
            final List<Profile> profiles = getProfilesForUser(profileAPI, profilesIndex);
            nbOfProfilesRetrieved = profiles.size();
            for (final Profile profile : profiles) {
                pageTokens.addAll(getProfilePages(profileAPI, profile));
                permissions.addAll(getCustomProfilePermissions(customPermissionsMapping, profile));
            }
            profilesIndex = profilesIndex + nbOfProfilesRetrieved;
        }
        return pageTokens;
    }

    protected List<String> getProfilePages(final ProfileAPI profileAPI, final Profile profile) throws SearchException {
        final List<String> pageTokens = new ArrayList<String>();
        int entriesIndex = 0;
        int nbOfProfileEntriesRetrieved = MAX_ELEMENTS_RETRIEVED;
        while (nbOfProfileEntriesRetrieved == MAX_ELEMENTS_RETRIEVED) {
            final List<ProfileEntry> profileEntries = getProfileEntriesForProfile(profileAPI, profile, entriesIndex);
            nbOfProfileEntriesRetrieved = profileEntries.size();
            for (final ProfileEntry profileEntry : profileEntries) {
                pageTokens.add(profileEntry.getPage());
            }
            entriesIndex = entriesIndex + nbOfProfileEntriesRetrieved;
        }
        return pageTokens;
    }

    protected List<String> getCustomProfilePermissions(final CustomPermissionsMapping customPermissionsMapping, final Profile profile) {
        return customPermissionsMapping.getPropertyAsList("profile|" + profile.getName());
    }

    protected List<String> getCustomUserPermissions(final CustomPermissionsMapping customPermissionsMapping) {
        return customPermissionsMapping.getPropertyAsList("user|" + session.getUserName());
    }

    protected ProfileAPI getProfileAPI() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getProfileAPI(session);
    }

    protected List<Profile> getProfilesForUser(final ProfileAPI profileAPI, final int profilesIndex) {
        return profileAPI.getProfilesForUser(session.getId(), profilesIndex, MAX_ELEMENTS_RETRIEVED, null);
    }

    protected List<ProfileEntry> getProfileEntriesForProfile(final ProfileAPI profileAPI, final Profile profile, final int entriesIndex) throws SearchException {
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(entriesIndex, MAX_ELEMENTS_RETRIEVED);
        searchOptionsBuilder.filter(ProfileEntrySearchDescriptor.PROFILE_ID, profile.getId());
        final SearchResult<ProfileEntry> profileEntriesResult = profileAPI.searchProfileEntries(searchOptionsBuilder.done());
        final List<ProfileEntry> profileEntries = profileEntriesResult.getResult();
        return profileEntries;
    }
}
