/**
 * 
 */
package org.bonitasoft.test.toolkit.api;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpStatus;
import org.bonitasoft.test.toolkit.api.json.AddToProfile;
import org.bonitasoft.test.toolkit.api.json.CreateGroup;
import org.bonitasoft.test.toolkit.api.json.CreateProfile;
import org.bonitasoft.test.toolkit.api.json.CreateRole;
import org.bonitasoft.test.toolkit.api.json.CreateUser;
import org.bonitasoft.test.toolkit.api.json.ImportOrganization;
import org.bonitasoft.test.toolkit.api.json.InstallProcess;
import org.bonitasoft.test.toolkit.api.json.MapToActor;
import org.bonitasoft.test.toolkit.api.json.SetProcessDisplayName;
import org.bonitasoft.test.toolkit.api.json.SetProcessState;
import org.bonitasoft.test.toolkit.api.json.SetUserManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to make API calls (eg REST API calls).
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class APIHelper {

    private String siteUrl;

    public static final long DEFAULT_TENANT_ID = 1;

    public static final String TECHUSER_LOGIN = "install";

    public static final String TECHUSER_PASSWORD = "install";

    public static final int SEARCH_COUNT = 100;

    /** JSON parser. */
    protected JSONParser jsonParser;

    /** XML reader. */
    protected SAXReader xmlReader;

    /** Logger. */
    protected final Logger logger;

    /** API Client. */
    private final BonitaAPIClient client;

    /** Client executor. */
    private final ApacheHttpClient4Executor executor;

    /** Member type. */
    public enum MemberType {
        USER, GROUP, ROLE, ROLE_AND_GROUP
    }

    /** Process activation state. */
    public enum ProcessActivationState {
        ENABLED, DISABLED;
    }

    /**
     * Constructor.
     * 
     * @param pTenantId
     * @param pSiteUrl
     * @param pUserName
     * @param pPassword
     */
    public APIHelper(final long pTenantId, final String pSiteUrl, final String pUserName, final String pPassword) {
        this.logger = LoggerFactory.getLogger(APIHelper.class);
        this.logger.info("Login on tenant [{}] with user [{}]", pTenantId, pUserName);

        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        this.executor = new ApacheHttpClient4Executor();
        setSiteUrl(pSiteUrl);
        this.client = ProxyFactory.create(BonitaAPIClient.class, pSiteUrl, this.executor);

        final ClientResponse<String> res = this.client.login(String.valueOf(pTenantId), pUserName, pPassword, Boolean.FALSE.toString());
        consumeResponse(res);

        this.jsonParser = new JSONParser();
        this.xmlReader = new SAXReader();
    }

    /**
     * Constructor (using default tenant id).
     * 
     * @param siteUrl
     * @param pUserName
     * @param pPassword
     */
    public APIHelper(final String siteUrl, final String pUserName, final String pPassword) {
        this(DEFAULT_TENANT_ID, siteUrl, pUserName, pPassword);
    }

    /**
     * Constructor using technical user credentials.
     * 
     * @param siteUrl
     */
    public APIHelper(final String siteUrl) {
        this(siteUrl, TECHUSER_LOGIN, TECHUSER_PASSWORD);
    }

    /**
     * Constructor using technical user credentials.
     * 
     * @param siteUrl
     */
    public APIHelper(final String siteUrl, final long pTenantId) {
        this(pTenantId, siteUrl, TECHUSER_LOGIN, TECHUSER_PASSWORD);
    }

    /**
     * Clean and set site URL.
     * 
     * @param pSiteUrl
     */
    private final void setSiteUrl(final String pSiteUrl) {
        this.siteUrl = pSiteUrl;
        if (!this.siteUrl.endsWith("/")) {
            this.siteUrl = this.siteUrl + "/";
        }
    }

    /**
     * @return
     */
    public final BonitaAPIClient getClient() {
        return this.client;
    }

    /**
     * Get all users.
     * 
     * @return JSONArray
     */
    public final JSONArray getAllUsers() {
        System.out.println("[INFO] APIHelper => getAllUsers()");
        final ClientResponse<String> res = this.client.getUsers(0, SEARCH_COUNT);
        final String entity = consumeResponse(res).getEntity();
        JSONArray users = new JSONArray();
        try {
            users = (JSONArray) this.jsonParser.parse(entity);
        } catch (final ParseException e) {
            this.logger.error(e.getMessage());
        }
        return users;
    }

    /**
     * Get all groups.
     * 
     * @return JSONArray
     */
    public final JSONArray getAllGroups() {
        System.out.println("[INFO] APIHelper => getAllGroups()");
        return search(BonitaAPIClient.GROUP_API_PATH, 0, SEARCH_COUNT, "name ASC", null, null, null);
    }

    /**
     * Get all roles.
     * 
     * @return JSONArray
     */
    public final JSONArray getAllRoles() {
        System.out.println("[INFO] APIHelper => getAllRoles()");
        return search(BonitaAPIClient.ROLE_API_PATH, 0, SEARCH_COUNT, "name ASC", null, null, null);
    }

    /**
     * Get all profiles.
     * 
     * @return JSONArray
     */
    public final JSONArray getAllProfiles() {
        System.out.println("[INFO] APIHelper => getAllProfiles()");
        return search(BonitaAPIClient.PROFILE_API_PATH, 0, SEARCH_COUNT, "name ASC", null, null, null);
    }

    /**
     * Make a search.
     * 
     * @return JSONArray
     */
    public final JSONArray search(final String searchURL, final int pParam, final int cParam, final String oParam, final String fParam,
            final String dParam, final String nParam) {
        final ClientResponse<String> res = this.client.search(searchURL, pParam, cParam, oParam, fParam, dParam, nParam);
        final String entity = consumeResponse(res).getEntity();
        JSONArray items = new JSONArray();
        try {
            items = (JSONArray) this.jsonParser.parse(entity);
        } catch (final ParseException e) {
            this.logger.error(e.getMessage());
        }
        return items;
    }

    /**
     * Make a search and return response.
     * 
     * @return JSONArray
     */
    public final APIResponse searchResponse(final String searchURL, final int pParam, final int cParam, final String oParam, final String fParam,
            final String dParam, final String nParam) {
        final ClientResponse<String> res = this.client.search(searchURL, pParam, cParam, oParam, fParam, dParam, nParam);
        return consumeResponse(res);
    }

    /**
     * Get all processes.
     * 
     * @return JSONArray
     */
    public final JSONArray getAllProcesses() {
        System.out.println("[INFO] APIHelper => getAllProcesses()");
        final String filterExpression = null; // "VIEW=ADMINISTRATOR";
        final String order = "deploymentDate DESC";
        final ClientResponse<String> res = this.client.getProcesses(order, filterExpression);
        final String entity = consumeResponse(res).getEntity();
        JSONArray processes = new JSONArray();
        try {
            processes = (JSONArray) this.jsonParser.parse(entity);
        } catch (final ParseException e) {
            this.logger.error(e.getMessage());
        }
        return processes;
    }

    /**
     * Find user and return its id.
     * 
     * @param pUsername
     * @return
     * @throws Exception
     */
    public final String getUserId(final String pUsername) throws Exception {
        System.out.println("[INFO] APIHelper => getUserId(" + pUsername + ")");
        final JSONObject jsonObj = getUserJSONObject(pUsername);
        return (String) jsonObj.get("id");
    }

    /**
     * Find user and return the response as JSON object.
     * 
     * @param pUsername
     * @return
     * @throws Exception
     */
    public final JSONObject getUserJSONObject(final String pUsername) throws Exception {
        System.out.println("[INFO] APIHelper => getUserJSONObject(" + pUsername + ")");
        final JSONArray users = getAllUsers();
        for (final Object obj : users) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String username = (String) jsonObj.get("userName");
            if (pUsername.equals(username)) {
                return jsonObj;
            }
        }
        throw new Exception("Username " + pUsername + " not found");
    }

    /**
     * Find actor and return its id.
     * 
     * @param pActorName
     * @param pProcessId
     * @return
     * @throws Exception
     */
    public final String getActorId(final String pProcessId, final String pActorName) throws Exception {
        System.out.println("[INFO] APIHelper => getActorId(" + pProcessId + ", " + pActorName + ")");
        final JSONArray users = getAllActors(pProcessId);
        for (final Object obj : users) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String actorName = (String) jsonObj.get("name");
            if (pActorName.equals(actorName)) {
                return (String) jsonObj.get("id");
            }
        }
        throw new Exception("Actor name " + pActorName + " not found for process id " + pProcessId);
    }

    /**
     * Find group and return its id.
     * 
     * @param pGroupName
     * @return
     * @throws Exception
     */
    public final String getGroupId(final String pGroupName) throws Exception {
        System.out.println("[INFO] APIHelper => getGroupId(" + pGroupName + ")");
        final JSONArray groups = getAllGroups();
        for (final Object obj : groups) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String name = (String) jsonObj.get("name");
            if (pGroupName.equals(name)) {
                return (String) jsonObj.get("id");
            }
        }
        throw new Exception("Group " + pGroupName + " not found");
    }

    /**
     * Find role and return its id.
     * 
     * @param pRoleName
     * @return
     * @throws Exception
     */
    public final String getRoleId(final String pRoleName) throws Exception {
        System.out.println("[INFO] APIHelper => getRoleId(" + pRoleName + ")");
        final JSONArray roles = getAllRoles();
        for (final Object obj : roles) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String name = (String) jsonObj.get("name");
            if (pRoleName.equals(name)) {
                return (String) jsonObj.get("id");
            }
        }
        throw new Exception("Role " + pRoleName + " not found");
    }

    /**
     * Find profile and return its id.
     * 
     * @param pProfileName
     * @return
     * @throws Exception
     */
    public final String getProfileId(final String pProfileName) throws Exception {
        System.out.println("[INFO] APIHelper => getProfileId(" + pProfileName + ")");
        final JSONArray profiles = getAllProfiles();
        for (final Object obj : profiles) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String name = (String) jsonObj.get("name");
            if (pProfileName.equals(name)) {
                return (String) jsonObj.get("id");
            }
        }
        throw new Exception("Profile " + pProfileName + " not found");
    }

    /**
     * Find processes and return all matching ids.
     * 
     * @param pProcessName
     * @return
     */
    // public final List<String> getProcessIds(final String pProcessName) {
    // final List<String> ids = new ArrayList<String>();
    // final JSONArray processes = getAllProcesses();
    // for (final Object obj : processes) {
    // final JSONObject jsonObj = (JSONObject) obj;
    // final String name = (String) jsonObj.get("name");
    // if (pProcessName.equals(name)) {
    // ids.add((String) jsonObj.get("id"));
    // }
    // }
    // return ids;
    // }

    /**
     * Find processes and return all matching ids.
     * 
     * @param pProcessName
     *            null to get all processes
     * @param pVersion
     *            null to get all versions of pProcessName
     * @return
     */
    public final List<String> getProcessIds(final String pProcessName, final String pVersion) {
        final List<String> ids = new ArrayList<String>();
        final JSONArray processes = getAllProcesses();
        for (final Object obj : processes) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String name = (String) jsonObj.get("name");
            final String version = (String) jsonObj.get("version");
            if (pProcessName == null) {
                ids.add((String) jsonObj.get("id"));
            } else {
                if (pProcessName.equals(name)) {
                    if (pVersion == null || pVersion.equals(version)) {
                        ids.add((String) jsonObj.get("id"));
                    }
                }
            }
        }
        return ids;
    }

    /**
     * Get process id by process name (first found).
     * 
     * @param pProcessName
     * @param pVersion
     * @return
     */
    public final String getProcessId(final String pProcessName, final String pVersion) {
        System.out.println("[INFO] APIHelper => getProcessId (" + pProcessName + "," + pVersion + ")");
        final List<String> processIdList = getProcessIds(pProcessName, pVersion);
        return processIdList != null ? processIdList.get(0) : null;
    }

    /**
     * Get process id by process name and display name (first found).
     * 
     * @param pProcessName
     * @param pDisplayName
     * @param pVersion
     * @return
     */
    public final String getProcessId(final String pProcessName, final String pDisplayName, final String pVersion) {
        System.out.println("[INFO] APIHelper => getProcessId (" + pProcessName + "," + pDisplayName + "," + pVersion + ")");
        String id = "";
        final JSONArray processes = getAllProcesses();
        for (final Object obj : processes) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String name = (String) jsonObj.get("name");
            final String displayName = (String) jsonObj.get("displayName");
            final String version = (String) jsonObj.get("version");
            if (pProcessName.equals(name) && pDisplayName.equals(displayName)) {
                if (pVersion == null || pVersion.equals(version)) {
                    id = (String) jsonObj.get("id");
                }
                break;
            }
        }
        return id;
    }

    /**
     * Delete a user.
     * 
     * @param pUsername
     */
    public final APIResponse deleteUser(final String pUsername) {
        System.out.println("[INFO] APIHelper => deleteUser (" + pUsername + ")");
        APIResponse apiResponse = new APIResponse();
        String id = null;
        try {
            id = getUserId(pUsername);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (id != null) {
            final JSONArray idsToDelete = new JSONArray();
            idsToDelete.add(id);
            final ClientResponse<String> res = this.client.deleteUsers(idsToDelete.toJSONString());
            apiResponse = consumeResponse(res);
        }
        return apiResponse;
    }

    /**
     * Delete a group.
     * 
     * @param pGroupName
     */
    public final void deleteGroup(final String pGroupName) {
        System.out.println("[INFO] APIHelper => deleteGroup (" + pGroupName + ")");
        String id = null;
        try {
            id = getGroupId(pGroupName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (id != null) {
            final JSONArray idsToDelete = new JSONArray();
            idsToDelete.add(id);
            final ClientResponse<String> res = this.client.deleteGroups(idsToDelete.toJSONString());
            consumeResponse(res);
        }
    }

    /**
     * Delete a role.
     * 
     * @param pRoleName
     */
    public final void deleteRole(final String pRoleName) {
        System.out.println("[INFO] APIHelper => deleteRole (" + pRoleName + ")");
        String id = null;
        try {
            id = getRoleId(pRoleName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (id != null) {
            final JSONArray idsToDelete = new JSONArray();
            idsToDelete.add(id);
            final ClientResponse<String> res = this.client.deleteRoles(idsToDelete.toJSONString());
            consumeResponse(res);
        }
    }

    /**
     * Delete a profile.
     * 
     * @param pProfileName
     */
    public final void deleteProfile(final String pProfileName) {
        System.out.println("[INFO] APIHelper => deleteProfile (" + pProfileName + ")");
        String id = null;
        try {
            id = getProfileId(pProfileName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (id != null) {
            final JSONArray idsToDelete = new JSONArray();
            idsToDelete.add(id);
            final ClientResponse<String> res = this.client.deleteProfiles(idsToDelete.toJSONString());
            consumeResponse(res);
        }
    }

    /**
     * Delete all processes with the same name (all versions).
     * 
     * @param pProcessName
     */
    public final APIResponse deleteAllProcesses(final String pProcessName) {
        return this.deleteAllProcesses(pProcessName, null);
    }

    /**
     * Delete all processes given name and version.
     * 
     * @param pProcessName
     */
    public final APIResponse deleteAllProcesses(final String pProcessName, final String pVersion) {
        System.out.println("[INFO] APIHelper => deleteAllProcesses (" + pProcessName + "," + pVersion + ")");
        final List<String> ids = getProcessIds(pProcessName, pVersion);
        final JSONArray idsToDelete = new JSONArray();
        for (final String id : ids) {
            setProcessStateById(id, ProcessActivationState.DISABLED);
            idsToDelete.add(id);
        }
        final ClientResponse<String> res = this.client.deleteProcesses(idsToDelete.toJSONString());
        return consumeResponse(res);
    }

    /**
     * Create a user.
     * 
     * @param pCreateUser
     * @return response as String
     */
    public final APIResponse createUser(final CreateUser pCreateUser) {
        System.out.println("[INFO] APIHelper => createUser (" + pCreateUser.toString() + ")");
        final ClientResponse<String> res = this.client.createUser(pCreateUser.toJSONObject().toJSONString());
        return consumeResponse(res);
    }

    /**
     * Set manager for a user.
     * 
     * @param pUserId
     * @param pManagerId
     * @return
     */
    public final String setUserManager(final String pUserId, final String pManagerId) {
        System.out.println("[INFO]setUserManager( " + pUserId + " , " + pManagerId.toString() + ")");
        final SetUserManager setUserManager = new SetUserManager(pUserId, pManagerId);
        final ClientResponse<String> res = this.client.setUserManager(pUserId, setUserManager.toJSONObject().toJSONString());
        final String entity = consumeResponse(res).getEntity();
        return entity;
    }

    /**
     * Get member id from member name.
     * 
     * @param pMemberyType
     * @param pMemberName
     * @return
     */
    private String getMemberId(final MemberType pMemberyType, final String pMemberName) {
        String memberId = null;
        try {
            switch (pMemberyType) {
                case GROUP:
                    memberId = getGroupId(pMemberName);
                    break;

                case ROLE:
                    memberId = getRoleId(pMemberName);
                    break;

                default:
                    memberId = getUserId(pMemberName);
                    break;
            }
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        return memberId;
    }

    /**
     * Add member to profile.
     * 
     * @param pProfileName
     * @param pMemberType
     * @param pMemberName
     * @return
     */
    public final APIResponse addToProfile(final String pProfileName, final MemberType pMemberType, final String pMemberName) {
        APIResponse apiResponse = new APIResponse();
        String profileId = null;
        String memberId = null;
        try {
            profileId = getProfileId(pProfileName);
            memberId = getMemberId(pMemberType, pMemberName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (profileId != null && memberId != null) {
            final AddToProfile addToProfile = new AddToProfile(profileId, pMemberType, memberId);
            final ClientResponse<String> res = this.client.addToProfile(addToProfile.toJSONObject().toJSONString());
            apiResponse = consumeResponse(res);
        }
        return apiResponse;
    }

    /**
     * Add membership (role in group) to profile.
     * 
     * @param pProfileName
     * @param pRoleName
     * @param pGroupName
     * @return
     */
    public final APIResponse addMembershipToProfile(final String pProfileName, final String pRoleName, final String pGroupName) {
        APIResponse apiResponse = new APIResponse();
        String profileId = null;
        String roleId = null;
        String groupId = null;
        try {
            profileId = getProfileId(pProfileName);
            roleId = getRoleId(pRoleName);
            groupId = getGroupId(pGroupName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (profileId != null && roleId != null && groupId != null) {
            final AddToProfile addToProfile = new AddToProfile(profileId, MemberType.ROLE, roleId);
            addToProfile.setMemberType(MemberType.GROUP);
            addToProfile.setMemberId(groupId);
            final ClientResponse<String> res = this.client.addToProfile(addToProfile.toJSONObject().toJSONString());
            apiResponse = consumeResponse(res);
        }
        return apiResponse;
    }

    /**
     * Install a process given an uploaded process bar.
     * 
     * @param pFileupload
     * @return
     */
    public final APIResponse installProcess(final String pFileupload) {
        final InstallProcess installProcess = new InstallProcess(pFileupload);
        final ClientResponse<String> res = this.client.installProcess(installProcess.toJSONObject().toJSONString());
        return consumeResponse(res);
    }

    /**
     * Import an organization given an uploaded orgnization XML.
     * 
     * @param pOrganizationDataUpload
     * @return
     */
    public final APIResponse importOrganization(final String pOrganizationDataUpload) {
        System.out.println("[INFO] APIHelper => importOrganization (" + pOrganizationDataUpload + ")");
        final ImportOrganization importOrganization = new ImportOrganization(pOrganizationDataUpload);
        final ClientResponse<String> res = this.client.importOrganization(importOrganization.toJSONObject().toJSONString());
        return consumeResponse(res);
    }

    /**
     * Export organization.
     * 
     * @return
     */
    public final Document exportOrganization() {
        System.out.println("[INFO] APIHelper => exportOrganization()");
        Document result = null;
        final ClientResponse<String> res = this.client.exportOrganization();
        final String entity = consumeResponse(res).getEntity();
        System.out.println("[INFO] APIHelper => exportOrganization() - Entity: [" + entity + "]");
        try {
            result = this.xmlReader.read(new StringReader(entity));
        } catch (final DocumentException de) {
            throw new Error(de);
        }
        return result;
    }

    /**
     * Update a process state (all processes with given name any version).
     * 
     * @param pProcessName
     * @param pState
     * @return
     */
    public final List<APIResponse> setProcessState(final String pProcessName, final ProcessActivationState pState) {
        System.out.println("[INFO]setProcessState( " + pProcessName + " , " + pState.toString() + ")");
        final List<APIResponse> responses = new ArrayList<APIResponse>();
        final List<String> ids = getProcessIds(pProcessName, null);
        for (final String processId : ids) {
            responses.add(setProcessStateById(processId, pState));
        }
        return responses;
    }

    /**
     * Update a process state given its id.
     * 
     * @param pProcessName
     * @param pState
     * @return
     */
    public final APIResponse setProcessStateById(final String pProcessId, final ProcessActivationState pState) {

        System.out.println("[INFO]setProcessStateById( " + pProcessId + " , " + pState.toString() + ")");
        final SetProcessState setProcessState = new SetProcessState(pProcessId, pState);
        final ClientResponse<String> res = this.client.setProcessState(pProcessId, setProcessState.toJSONObject().toJSONString());
        return consumeResponse(res);
    }

    /**
     * Set display name of a process.
     * 
     * @param pProcessId
     * @param pDisplayName
     * @return
     */
    public final String setProcessDisplayName(final String pProcessId, final String pDisplayName) {
        System.out.println("[INFO]setProcessDisplayName( " + pProcessId + " , " + pDisplayName.toString() + ")");
        final SetProcessDisplayName setProcessDisplayName = new SetProcessDisplayName(pProcessId, pDisplayName);
        final ClientResponse<String> res = this.client.setProcessDisplayName(pProcessId, setProcessDisplayName.toJSONObject().toJSONString());
        final String entity = consumeResponse(res).getEntity();
        return entity;
    }

    /**
     * Get all processes.
     * 
     * @return JSONArray
     */
    public final JSONArray getAllActors(final String pProcessId) {
        System.out.println("[INFO]getAllActors( " + pProcessId + ")");
        final ClientResponse<String> res = this.client.getActors(0, SEARCH_COUNT, "name ASC", "process_id=" + pProcessId);
        final String entity = consumeResponse(res).getEntity();
        JSONArray actors = new JSONArray();
        try {
            actors = (JSONArray) this.jsonParser.parse(entity);
        } catch (final ParseException e) {
            this.logger.error(e.getMessage());
        }
        return actors;
    }

    /**
     * Get members of a process actor.
     * 
     * @param pActorId
     * @param pMemberType
     * @return
     */
    public final JSONArray getActorMembers(final String pActorId, final MemberType pMemberType) {
        System.out.println("[INFO]getActorMembers( " + pActorId + "," + pMemberType.toString() + ")");
        final List<String> filterExpressions = new ArrayList<String>();
        filterExpressions.add("actor_id=" + pActorId);
        filterExpressions.add("MEMBER_TYPE=" + pMemberType.toString());
        final ClientResponse<String> res = this.client.getActorMembers(0, SEARCH_COUNT, "name ASC", filterExpressions);
        final String entity = consumeResponse(res).getEntity();
        JSONArray actorMembers = new JSONArray();
        try {
            actorMembers = (JSONArray) this.jsonParser.parse(entity);
        } catch (final ParseException e) {
            this.logger.error(e.getMessage());
        }
        return actorMembers;
    }

    /**
     * Get the id of a process actor member.
     * 
     * @param pActorId
     * @param pMemberType
     * @param pMemberName
     * @return
     * @throws Exception
     */
    public final String getActorMemberId(final String pActorId, final MemberType pMemberType, final String pMemberName) throws Exception {
        System.out.println("[INFO]getActorMembers( " + pActorId + "," + pMemberType.toString() + ", " + pMemberName + ")");
        final JSONArray actorMembers = getActorMembers(pActorId, pMemberType);
        for (final Object obj : actorMembers) {
            final JSONObject jsonObj = (JSONObject) obj;
            final String name = (String) jsonObj.get("name");
            if (pMemberName.equals(name)) {
                return (String) jsonObj.get("actor_Member_id");
            }
        }
        throw new Exception("Actor member " + pMemberName + " not found");
    }

    /**
     * Delete a member mapped to a process actor.
     * 
     * @param pProcessId
     * @param pActorName
     * @param pMemberType
     * @param pMemberName
     * @return
     */
    public final void deleteActorMember(final String pProcessId, final String pActorName, final MemberType pMemberType, final String pMemberName) {
        System.out.println("[INFO]deleteActorMember( " + pProcessId + "," + pActorName + ", " + pMemberType.toString() + "," + pMemberName + ")");
        String actorMemberId = null;
        try {
            final String actorId = getActorId(pProcessId, pActorName);
            actorMemberId = getActorMemberId(actorId, pMemberType, pMemberName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (actorMemberId != null) {
            final JSONArray idsToDelete = new JSONArray();
            idsToDelete.add(actorMemberId);
            final ClientResponse<String> res = this.client.deleteActorMembers(idsToDelete.toJSONString());
            consumeResponse(res);
        }
    }

    /**
     * Map user to actor.
     * 
     * @param pProfileName
     * @param pMemberyType
     * @param pMemberName
     * @return
     */
    public final String mapUserToActor(final String pProcessId, final String pActorName, final MemberType pMemberyType, final String pUserName) {
        System.out.println("[INFO]mapUserToActor( " + pProcessId + "," + pActorName + ", " + pMemberyType.toString() + "," + pUserName + ")");
        String actorId = null;
        String userId = null;
        String entity = "";
        try {
            userId = getUserId(pUserName);
            actorId = getActorId(pProcessId, pActorName);
        } catch (final Exception e) {
            this.logger.warn(e.getMessage());
        }
        if (userId != null && actorId != null) {
            final MapToActor actMapping = new MapToActor(actorId, pMemberyType, userId);
            final ClientResponse<String> res = this.client.mapToActor(actMapping.toJSONObject().toJSONString());
            entity = consumeResponse(res).getEntity();
        }
        return entity;
    }

    /**
     * Create group.
     * 
     * @param pCreateGroup
     * @return
     */
    public final String createGroup(final CreateGroup pCreateGroup) {
        System.out.println("[INFO]createGroup( " + pCreateGroup.toString());
        final ClientResponse<String> res = this.client.createGroup(pCreateGroup.toJSONObject().toString());
        return consumeResponse(res).getEntity();
    }

    /**
     * Create role.
     * 
     * @param pCreateRole
     * @return
     */
    public final String createRole(final CreateRole pCreateRole) {
        System.out.println("[INFO]createRole( " + pCreateRole.toString());
        final ClientResponse<String> res = this.client.createRole(pCreateRole.toJSONObject().toString());
        return consumeResponse(res).getEntity();
    }

    /**
     * Create profile.
     * 
     * @param pCreateProfile
     * @return
     */
    public final String createProfile(final CreateProfile pCreateProfile) {
        final ClientResponse<String> res = this.client.createProfile(pCreateProfile.toJSONObject().toString());
        return consumeResponse(res).getEntity();
    }

    /**
     * Get i18n translation.
     * 
     * @param pLanguageCode
     *            in [de, en, es, fr, it, pt_BR]
     * @return Map<String, String> (key, value) pairs map
     */
    public final Map<String, String> getI18nTranslation(final String pLanguageCode) {
        this.logger.info("Loading i18n translation for language [{}]", pLanguageCode);
        final JSONArray jsonArray = search(BonitaAPIClient.TRANSLATION_API_PATH, 0, SEARCH_COUNT, null, "locale=" + pLanguageCode, null, null);
        final Map<String, String> map = new HashMap<String, String>();
        JSONObject jo;
        String cleanKey;
        String cleanValue;
        for (int i = 0; i < jsonArray.size(); i++) {
            jo = (JSONObject) jsonArray.get(i);
            cleanKey = ((String) jo.get("key")).trim();
            cleanValue = ((String) jo.get("value")).trim();
            map.put(cleanKey, cleanValue);
        }
        return map;
    }

    /**
     * Consume a client response (logging and connection release).
     * 
     * @param pResponse
     * @return APIResponse object
     */
    public APIResponse consumeResponse(final ClientResponse<String> pResponse) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final StackTraceElement apiMethod = stackTraceElements[2];
        final Object[] loggerArgs = new String[5];
        loggerArgs[0] = apiMethod.getMethodName();
        loggerArgs[3] = apiMethod.getFileName();
        loggerArgs[4] = String.valueOf(apiMethod.getLineNumber());

        APIResponse apiResponse = new APIResponse();
        try {
            final String entity = pResponse.getEntity(String.class);
            final int status = pResponse.getStatus();
            loggerArgs[1] = String.valueOf(status);
            loggerArgs[2] = HttpStatus.getStatusText(status);
            apiResponse = new APIResponse(entity, status);
            this.logger.debug("Response {}", entity);
            if (isSuccessStatusCode(status)) {
                this.logger.info("[{}] - {} {} ({}:{})", loggerArgs);
            } else {
                this.logger.error("[{}] - {} {} ({}:{})", loggerArgs);
            }
        } catch (final Exception e) {
            this.logger.error(e.getMessage());
        } finally {
            if (pResponse != null) {
                pResponse.releaseConnection();
            }
        }
        return apiResponse;
    }

    /**
     * Consume a client response (logging and connection release).
     * 
     * @param pResponse
     * @return APIResponse object
     */
    public int getResponseCode(final ClientResponse<String> pResponse) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final StackTraceElement apiMethod = stackTraceElements[2];
        final Object[] loggerArgs = new String[5];
        loggerArgs[0] = apiMethod.getMethodName();
        loggerArgs[3] = apiMethod.getFileName();
        loggerArgs[4] = String.valueOf(apiMethod.getLineNumber());

        APIResponse apiResponse = new APIResponse();
        final int status;
        try {
            final String entity = pResponse.getEntity(String.class);
            status = pResponse.getStatus();
            loggerArgs[1] = String.valueOf(status);
            loggerArgs[2] = HttpStatus.getStatusText(status);
            apiResponse = new APIResponse(entity, status);
            this.logger.debug("Response {}", entity);
            final String logPattern = "[{}] - {} {} ({}:{})";
            if (isSuccessStatusCode(status)) {
                this.logger.info(logPattern, loggerArgs);
            } else {
                this.logger.error(logPattern, loggerArgs);
            }
        } catch (final Exception e) {
            this.logger.error(e.getMessage());
        } finally {
            if (pResponse != null) {
                pResponse.releaseConnection();
            }
        }
        return apiResponse.getStatus();
    }

    /**
     * Whether status code is successful (>= 200 && < 400).
     * 
     * @param pStatusCode
     * @return
     */
    public static boolean isSuccessStatusCode(final int pStatusCode) {
        return pStatusCode >= HttpResponseCodes.SC_OK && pStatusCode < HttpResponseCodes.SC_BAD_REQUEST;
    }

    /*
     * --------------------------------------------------
     * GENERIC METHODS
     * --------------------------------------------------
     */

    /**
     * Send HTTP GET to a given resource given the resource path including query params.
     * Can be used for generice search for example.
     * 
     * @param pResourcePathWithQueryParams
     *            eg. "API/system/i18ntranslation?p=0&c=0&f=locale%3den"
     * @return {@link APIResponse}
     * @throws Exception
     */
    public final APIResponse httpGet(final String pResourcePathWithQueryParams) throws Exception {
        final String uriTemplate = this.siteUrl + pResourcePathWithQueryParams;
        final ClientRequest request = new ClientRequest(uriTemplate, this.executor);
        final ClientResponse<String> response = request.get();
        return consumeResponse(response);
    }

    /**
     * Send HTTP POST to a given resource with body.
     * 
     * @param pResourcePath
     * @param pRequestBody
     * @return
     * @throws Exception
     */
    public final APIResponse httpPost(final String pResourcePath, final Object pRequestBody) throws Exception {
        final String uriTemplate = this.siteUrl + pResourcePath;
        final ClientRequest request = new ClientRequest(uriTemplate, this.executor);
        request.body(MediaType.APPLICATION_JSON_TYPE, pRequestBody.toString());
        final ClientResponse<String> response = request.post();
        return consumeResponse(response);
    }

    /**
     * Send HTTP PUT to a given resource with body.
     * 
     * @param pResourcePath
     * @param pRequestBody
     * @return
     * @throws Exception
     */
    public final APIResponse httpPut(final String pResourcePath, final Object pRequestBody) throws Exception {
        final String uriTemplate = this.siteUrl + pResourcePath;
        final ClientRequest request = new ClientRequest(uriTemplate, this.executor);
        request.body(MediaType.APPLICATION_JSON_TYPE, pRequestBody.toString());
        final ClientResponse<String> response = request.put();
        return consumeResponse(response);
    }

}
