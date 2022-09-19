/**
 * 
 */
package org.bonitasoft.test.toolkit.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

    public static final int SEARCH_COUNT = 100;
    
    private static final String BONITA_API_TOKEN_HEADER = "X-Bonita-API-Token";
    
    private final String apiToken;

    private String siteUrl;

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
     * @param pSiteUrl
     * @param pUserName
     * @param pPassword
     */
    public APIHelper(final String pSiteUrl, final String pUserName, final String pPassword) {
        this.logger = LoggerFactory.getLogger(APIHelper.class);
        this.logger.info("Login with user [{}]", pUserName);

        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCookieStore(cookieStore)
                .build();
        this.executor = new ApacheHttpClient4Executor(httpClient) {
            @Override
            public void commitHeaders(ClientRequest request, HttpRequestBase httpMethod) {
                super.commitHeaders(request, httpMethod);
                if (apiToken != null) {
                    httpMethod.setHeader(BONITA_API_TOKEN_HEADER, apiToken);
                }
            }
        };
        setSiteUrl(pSiteUrl);
        this.client = ProxyFactory.create(BonitaAPIClient.class, pSiteUrl, this.executor);
        final ClientResponse<String> res = this.client.login(pUserName, pPassword, Boolean.FALSE.toString());
        consumeResponse(res);
        
        this.apiToken = fetchAPIToken(cookieStore);

        this.jsonParser = new JSONParser();
        this.xmlReader = new SAXReader();
    }

    private String fetchAPIToken(CookieStore cookieStore) {
        for (Cookie cookie : cookieStore.getCookies()) {
            if (BONITA_API_TOKEN_HEADER.equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
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
     *            null to get all processes
     * @param pVersion
     *            null to get all versions of pProcessName
     * @return
     */
    public final List<String> getProcessIds(final String pProcessName, final String pVersion) {
        final List<String> ids = new ArrayList<>();
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
        return getProcessIds(pProcessName, pVersion).get(0);
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
     * Whether status code is successful (>= 200 && < 400).
     * 
     * @param pStatusCode
     * @return
     */
    public static boolean isSuccessStatusCode(final int pStatusCode) {
        return pStatusCode >= HttpResponseCodes.SC_OK && pStatusCode < HttpResponseCodes.SC_BAD_REQUEST;
    }

}
