/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.test.toolkit.api.json;

import org.bonitasoft.test.toolkit.api.APIHelper.MemberType;

/**
 * JSON builder for membership requests.
 * 
 * @author Truc Nguyen
 */
@SuppressWarnings("unchecked")
public abstract class MembershipRequest extends BonitaJSON {

    protected MemberType memberType;

    protected String memberId;

    /**
     * Default Constructor.
     * 
     * @param pJsonResource
     */
    public MembershipRequest(final String pJsonResource) {
        super(pJsonResource);
    }

    /**
     * Default Constructor.
     * 
     * @param pJsonResource
     */
    public MembershipRequest(final String pJsonResource, final MemberType pMemberType, final String pMemberId) {
        super(pJsonResource);
        setMemberType(pMemberType);
        setMemberId(pMemberId);
    }

    public MemberType getMemberType() {
        return this.memberType;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberType(final MemberType memberType) {
        this.memberType = memberType;
        this.jsonObject.put("member_type", memberType.toString());
    }

    public void setMemberId(final String memberId) {
        this.memberId = memberId;
        final MemberType type = getMemberType();
        switch (type) {
            case GROUP:
                this.jsonObject.remove("user_id");
                this.jsonObject.put("group_id", memberId);
                break;

            case ROLE:
                this.jsonObject.remove("user_id");
                this.jsonObject.put("role_id", memberId);
                break;

            default:
                this.jsonObject.put("user_id", memberId);
                break;
        }
    }

}
