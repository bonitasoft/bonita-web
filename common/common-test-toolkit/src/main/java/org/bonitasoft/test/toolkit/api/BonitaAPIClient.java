package org.bonitasoft.test.toolkit.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.client.ClientResponse;

/**
 * JSON API interface.
 * 
 * @author truc
 */
public interface BonitaAPIClient {

    /** User. */
    String USER_API_PATH = "API/identity/user";

    /** Group. */
    String GROUP_API_PATH = "API/identity/group";

    /** Role. */
    String ROLE_API_PATH = "API/identity/role";

    /** Process. */
    String PROCESS_API_PATH = "API/bpm/process";

    /** Case. */
    String CASE_API_PATH = "API/bpm/case";

    /** Activity. */
    String ACTIVITY_API_PATH = "API/bpm/activity";

    /** Human task. */
    String HUMAN_TASK_API_PATH = "API/bpm/humanTask";

    /** Actor. */
    String ACTOR_API_PATH = "API/bpm/actor";

    /** Actor mapping. */
    String ACTORMEMBER_API_PATH = "API/bpm/actorMember";

    /** Profile. */
    String PROFILE_API_PATH = "API/portal/profile";

    /** Translation. */
    String TRANSLATION_API_PATH = "API/system/i18ntranslation";

    /**
     * Login.
     * 
     * @param pTenantId
     * @param pUserName
     * @param pPassword
     * @param pRedirect
     * @return
     */
    @POST
    @Path("loginservice")
    ClientResponse<String> login(@FormParam("tenant") String pTenantId, @FormParam("username") String pUserName, @FormParam("password") String pPassword, @FormParam("redirect") String pRedirect);

    /**
     * Logout.
     * 
     * @return
     */
    @GET
    @Path("logoutservice")
    ClientResponse<String> logout();

    /**
     * Search.
     * 
     * @return
     */
    @GET
    @Path("{path}")
    @Produces("application/json")
    ClientResponse<String> search(@PathParam("path") String path, @QueryParam("p") int pParam, @QueryParam("c") int cParam, @QueryParam("o") String oParam,
            @QueryParam("f") String fParam, @QueryParam("d") String dParam, @QueryParam("n") String nParam);

    /**
     * Get users.
     * 
     * @param pStart
     * @param pCount
     * @return
     */
    @GET
    @Path(USER_API_PATH)
    @Produces("application/json")
    ClientResponse<String> getUsers(@QueryParam("p") int pStart, @QueryParam("c") int pCount);

    /**
     * Delete users.
     * 
     * @param pBody
     * @return
     */
    @DELETE
    @Path(USER_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> deleteUsers(String pBody);

    /**
     * Create a user.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path(USER_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> createUser(String pBody);

    /**
     * Set user manager.
     * 
     * @param pUserId
     * @param pBody
     * @return
     */
    @PUT
    @Path(USER_API_PATH + "/{id}")
    @Consumes("application/json")
    ClientResponse<String> setUserManager(@PathParam("id") String pUserId, String pBody);

    /**
     * Delete groups.
     * 
     * @param pBody
     * @return
     */
    @DELETE
    @Path(GROUP_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> deleteGroups(String pBody);

    /**
     * Create a group.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path(GROUP_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> createGroup(String pBody);

    /**
     * Delete groups.
     * 
     * @param pBody
     * @return
     */
    @DELETE
    @Path(ROLE_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> deleteRoles(String pBody);

    /**
     * Create a group.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path(ROLE_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> createRole(String pBody);

    /**
     * Add to profile.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path("API/portal/profileMember")
    @Consumes("application/json")
    ClientResponse<String> addToProfile(String pBody);

    /**
     * Get profiles.
     * 
     * @param pStart
     * @return
     */
    @GET
    @Path(PROFILE_API_PATH)
    @Produces("application/json")
    ClientResponse<String> getProfiles(@QueryParam("p") int pStart, @QueryParam("c") int cParam, @QueryParam("o") String pOrder);

    /**
     * Delete profiles.
     * 
     * @param pBody
     * @return
     */
    @DELETE
    @Path(PROFILE_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> deleteProfiles(String pBody);

    /**
     * Create profile.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path(PROFILE_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> createProfile(String pBody);

    /**
     * Install a process.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path(PROCESS_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> installProcess(String pBody);

    /**
     * Import organization.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path("services/organization/import")
    @Consumes("application/json")
    ClientResponse<String> importOrganization(String pBody);

    /**
     * Export organization.
     * 
     * @return
     */
    @GET
    @Path("portal/exportOrganization")
    @Produces("application/xml")
    ClientResponse<String> exportOrganization();

    /**
     * Get processes.
     * 
     * @param pStart
     * @return
     */
    @GET
    @Path(PROCESS_API_PATH)
    @Produces("application/json")
    ClientResponse<String> getProcesses(@QueryParam("o") String pOrder, @QueryParam("f") String pFilterExpression);

    /**
     * Set process state.
     * 
     * @param pProcessId
     * @param pBody
     * @return
     */
    @PUT
    @Path(PROCESS_API_PATH + "/{id}")
    @Consumes("application/json")
    ClientResponse<String> setProcessState(@PathParam("id") String pProcessId, String pBody);

    /**
     * Set process display name.
     * 
     * @param pProcessId
     * @param pBody
     * @return
     */
    @PUT
    @Path(PROCESS_API_PATH + "/{id}")
    @Consumes("application/json")
    ClientResponse<String> setProcessDisplayName(@PathParam("id") String pProcessId, String pBody);

    /**
     * Delete processes.
     * 
     * @param pBody
     * @return
     */
    @DELETE
    @Path(PROCESS_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> deleteProcesses(String pBody);

    /**
     * Get members mapped to an actor.
     * 
     * @param pStart
     * @return
     */
    @GET
    @Path(ACTORMEMBER_API_PATH)
    @Produces("application/json")
    ClientResponse<String> getActorMembers(@QueryParam("p") int pStart, @QueryParam("c") int pCount, @QueryParam("o") String pOrder,
            @QueryParam("f") List<String> pFilterExpressions);

    /**
     * Create a relationship between member and actor.
     * 
     * @param pBody
     * @return
     */
    @POST
    @Path(ACTORMEMBER_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> mapToActor(String pBody);

    /**
     * Delete members.
     * 
     * @param pBody
     * @return
     */
    @DELETE
    @Path(ACTORMEMBER_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> deleteActorMembers(String pBody);

    /**
     * get actors for a process.
     * 
     * @param pBody
     * @return
     */
    @GET
    @Path(ACTOR_API_PATH)
    @Consumes("application/json")
    ClientResponse<String> getActors(@QueryParam("p") int pStart, @QueryParam("c") int pCount, @QueryParam("o") String pOrder,
            @QueryParam("f") String pFilterExpression);

}
