package org.bonitasoft.test.toolkit.api.json;

import org.bonitasoft.test.toolkit.api.APIHelper.MemberType;

/**
 * JSON builder for mapToActor request.
 * 
 * @author Nicolas TITH
 */
@SuppressWarnings("unchecked")
public class MapToActor extends MembershipRequest {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "mapToActor.json";

    private String actorId;

    public MapToActor(final String pActorId, final MemberType pMemberType, final String pMemberId) {
        super(JSON_RESOURCE);
        setActorId(pActorId);
        setMemberType(pMemberType);
        setMemberId(pMemberId);
    }

    public String getActorId() {
        return this.actorId;
    }

    public void setActorId(final String actorId) {
        this.actorId = actorId;
        this.jsonObject.put("actor_id", actorId);
    }

    // CHECKSTYLE:ON

}
