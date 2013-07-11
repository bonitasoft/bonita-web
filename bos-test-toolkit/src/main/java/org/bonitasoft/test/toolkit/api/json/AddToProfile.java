package org.bonitasoft.test.toolkit.api.json;

import org.bonitasoft.test.toolkit.api.APIHelper.MemberType;

/**
 * JSON builder for createGroup request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class AddToProfile extends MembershipRequest {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "addToProfileRequest.json";

    private String profileId;

    public AddToProfile(final String pProfileId, final MemberType pMemberType, final String pMemberId) {
        super(JSON_RESOURCE);
        setProfileId(pProfileId);
        setMemberType(pMemberType);
        setMemberId(pMemberId);
    }

    public String getProfileId() {
        return this.profileId;
    }

    public void setProfileId(final String profileId) {
        this.profileId = profileId;
        this.jsonObject.put("profile_id", profileId);
    }

    // CHECKSTYLE:ON

}
